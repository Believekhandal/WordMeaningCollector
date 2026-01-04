package com.example.vocabulary.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.vocabulary.service.TextFileToCsvConverter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author believe
 * @apiNote trigger for textFileToCsvConverter
 * @since 2022
 */

@Slf4j
@RestController
public class Controller {
	@Autowired
	TextFileToCsvConverter textFileToCsvConverter;

	@Value("${filename.input}")
	String inputfilepath;
	@Value("${filename.output}")
	String outputfilepath;

	@PostMapping("/api/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body("File is empty");
			}

			// Validate file type
			String contentType = file.getContentType();
			String originalFilename = file.getOriginalFilename();
			if (originalFilename == null ||
				(!originalFilename.toLowerCase().endsWith(".txt") &&
				 !originalFilename.toLowerCase().endsWith(".srt") &&
				 !originalFilename.toLowerCase().endsWith(".sub") &&
				 !originalFilename.toLowerCase().endsWith(".vtt"))) {
				return ResponseEntity.badRequest().body("Invalid file type. Only .txt, .srt, .sub, .vtt files are allowed");
			}

			// Save uploaded file to the input path
			Path targetPath = Paths.get(inputfilepath);
			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

			log.info("File uploaded successfully: {}", originalFilename);
			return ResponseEntity.ok("File uploaded successfully");

		} catch (IOException e) {
			log.error("Error uploading file", e);
			return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
		}
	}

	@GetMapping("/api/download/csv")
	public ResponseEntity<Resource> downloadCsv() {
		try {
			Path csvPath = Paths.get(outputfilepath);
			Resource resource = new UrlResource(csvPath.toUri());

			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType("text/csv"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vocabulary.csv\"")
					.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			log.error("Error downloading CSV file", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping(value = "meaningFinder/trigger")
	public String trigger() {
		log.info("File Creation Mechanism Started");
		textFileToCsvConverter.getMeaningFile(inputfilepath, outputfilepath);
		return "triggered";
	}
}
