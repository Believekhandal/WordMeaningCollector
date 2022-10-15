package com.example.vocabulary.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextFileToCsvConverter {

	@Autowired
	MeaningFinderApiCaller meaningFinderApiCaller;
	
	@Value("${filename.input}")
	String inputfilepath;
	@Value("${filename.output}")
	String outputfilepath;

	@Async
	public void getMeaningFile() {

		HashSet<String> listOfLines = new HashSet<>();
		Path path = Paths.get(inputfilepath);
		try {
			Files.lines(path).filter(line -> Pattern.compile("[\\w ]+").matcher(line).find())
					.forEach(a -> listOfLines.add(a));
		} catch (IOException ex) {
			log.error("Exception occured while reading from file: " + ex.getMessage());
		}
		Map<String, String> map = new HashMap<>();
		try {
			for (String word : listOfLines) {
				String s1 = word;
				Pattern p = Pattern.compile("[a-zA-Z]+");
				Matcher m1 = p.matcher(s1);
				while (m1.find()) {
					String meaningOfWord = meaningFinderApiCaller.findMeaning(m1.group());
					map.put(m1.group(), meaningOfWord);
				}
			}
		} catch (RestClientException e) {
			log.error("Exception occurred: ", e);
		} finally {
			log.info("words meaning collected in map successfully");
		}
		csvWriter(map,outputfilepath);
		log.info("File Created Successfully  ##########END");
	}

	// 10seconds without threading
	/**
	 * @param listOfMap
	 * @param writer
	 * @throws IOException
	 */
	public static void csvWriter(Map<String, String> myHashMap, String outputfilepath) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter(outputfilepath)) {
			for (Map.Entry<String, String> entry : myHashMap.entrySet()) {
				writer.append(entry.getKey()).append(',').append(entry.getValue()).append(eol);
			}
		} catch (IOException ex) {
			log.error("Exception occurred: ", eol);
		}
	}
}