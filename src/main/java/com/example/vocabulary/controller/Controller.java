package com.example.vocabulary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping(value = "meaningFinder/trigger")
	public String trigger() {
		log.info("File Creation Mechanism Started");
		textFileToCsvConverter.getMeaningFile(inputfilepath, outputfilepath);
		return "triggered";
	}
}
