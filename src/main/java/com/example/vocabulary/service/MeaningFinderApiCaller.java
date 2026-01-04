package com.example.vocabulary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MeaningFinderApiCaller {
	@Autowired
	WordMeaningFinderService wordMeaningFinderService;

	public String findMeaning(String word) {
		String meaningOfWord = "NA";
		try {
			var response = wordMeaningFinderService.callDictionaryApi(word);
			if (response.getBody() != null) {
				meaningOfWord = response.getBody().toString();
				log.info("meaningOfWord: "+meaningOfWord);
			} else {
				log.warn("No meaning found for word: {}", word);
			}
		} catch (Exception e) {
			log.error("Exception occurred while getting meaning of word: {}", word, e);
		}
		return meaningOfWord;
	}
}
