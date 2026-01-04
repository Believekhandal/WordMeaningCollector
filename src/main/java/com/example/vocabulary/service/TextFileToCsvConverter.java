package com.example.vocabulary.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextFileToCsvConverter {

    @Autowired
    private MeaningFinderApiCaller meaningFinderApiCaller;

    @Value("${api.call-delay:5000}")
    private long apiCallDelay;

    private static final Pattern LINE_VALIDATOR = Pattern.compile("[\\w ]+");
    private static final Pattern WORD_EXTRACTOR = Pattern.compile("[a-zA-Z]+");
    private static final Pattern VALID_WORD_PATTERN = Pattern.compile("^[a-zA-Z]{2,}$");

    /**
     * Validates if a word looks like a valid English word and not corrupted OCR text
     */
    private boolean isValidWord(String word) {
        if (word == null || word.length() < 2 || word.length() > 50) {
            return false;
        }

        // Must match basic word pattern (letters only, 2+ chars)
        if (!VALID_WORD_PATTERN.matcher(word).matches()) {
            return false;
        }

        // Check for mixed case (potential OCR corruption)
        boolean hasUpper = word.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = word.chars().anyMatch(Character::isLowerCase);

        if (hasUpper && hasLower) {
            // Allow proper nouns (first letter capital) but reject mixed case like "bUminal"
            if (!Character.isUpperCase(word.charAt(0)) || word.substring(1).chars().anyMatch(Character::isUpperCase)) {
                return false;
            }
        }

        // Check for suspicious character patterns that indicate OCR errors
        if (word.contains("~") || word.contains("«") || word.contains("»") ||
            word.contains("­") || word.contains("~") || word.contains(".")) {
            return false;
        }

        return true;
    }

    @Async
    public void getMeaningFile(String inputfilepath, String outputfilepath) {
        long startTime = System.currentTimeMillis();
        log.info("Starting meaning collection for file: {}", inputfilepath);

        Set<String> uniqueWords = new HashSet<>();
        Path path = Paths.get(inputfilepath);

        try {
            uniqueWords = Files.lines(path)
                    .filter(line -> LINE_VALIDATOR.matcher(line).find())
                    .flatMap(line -> {
                        List<String> words = new ArrayList<>();
                        Matcher matcher = WORD_EXTRACTOR.matcher(line);
                        while (matcher.find()) {
                            String word = matcher.group();
                            if (isValidWord(word)) {
                                words.add(word.toLowerCase());
                            } else {
                                log.debug("Filtered out invalid word: {}", word);
                            }
                        }
                        return words.stream();
                    })
                    .collect(Collectors.toSet());
        } catch (IOException ex) {
            log.error("Exception occurred while reading from file: {}", ex.getMessage());
        }

        log.info("Found {} unique words to process", uniqueWords.size());

        Map<String, String> wordMeaningMap = new ConcurrentHashMap<>();

        // Initialize all words with "NA" to ensure they all get written to CSV
        uniqueWords.forEach(word -> wordMeaningMap.put(word, "NA"));

        // Use virtual threads for API calls (Java 21+ Project Loom)
        // Virtual threads provide better scalability and resource utilization compared to platform threads
        // They are lightweight and can handle thousands of concurrent operations efficiently
        var virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

        List<CompletableFuture<Void>> futures = uniqueWords.stream()
                .map(word -> CompletableFuture.runAsync(() -> {
                    try {
                        // Add configurable delay between API calls to avoid rate limiting
                        if (apiCallDelay > 0) {
                            Thread.sleep(apiCallDelay);
                        }

                        String meaning = meaningFinderApiCaller.findMeaning(word);
                        wordMeaningMap.put(word, meaning); // Override the "NA" with actual meaning
                    } catch (Exception e) {
                        log.error("Error processing word: {}", word, e);
                        // Keep "NA" which is already set
                    }
                }, virtualThreadExecutor))
                .collect(Collectors.toList());

        // Wait for all futures to complete with timeout
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(30, TimeUnit.SECONDS); // 30 second timeout for all operations
            log.info("All API calls completed successfully");
        } catch (Exception e) {
            log.warn("Timeout occurred while processing words ({}), some API calls may still be running in background", e.getMessage());
            // Don't cancel futures - let them complete naturally, but proceed with CSV writing
            // The wordMeaningMap already has "NA" for words that haven't completed
        } finally {
            // Properly shutdown the virtual thread executor
            virtualThreadExecutor.shutdown();
            try {
                if (!virtualThreadExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    virtualThreadExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                virtualThreadExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        log.info("Word meanings collected successfully in {} ms", System.currentTimeMillis() - startTime);
        
        csvWriter(wordMeaningMap, outputfilepath);
        log.info("File Created Successfully at: {} ##########END", outputfilepath);
    }

    public static void csvWriter(Map<String, String> myHashMap, String outputfilepath) {
        String eol = System.getProperty("line.separator");

        try (Writer writer = new FileWriter(outputfilepath)) {
            // Sort keys for better readability in CSV
            List<String> sortedWords = new ArrayList<>(myHashMap.keySet());
            Collections.sort(sortedWords);
            
            for (String word : sortedWords) {
                writer.append(word).append(',').append(myHashMap.get(word)).append(eol);
            }
        } catch (IOException ex) {
            log.error("Exception occurred while writing CSV: ", ex);
        }
    }
}
