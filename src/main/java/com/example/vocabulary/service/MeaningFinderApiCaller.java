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
			meaningOfWord = wordMeaningFinderService.callDictionaryApi(word).getBody().toString();
			log.info("meaningOfWord: "+meaningOfWord);
		} catch (Exception e) {
			log.error("Exception occurred while getting meaning of word: "+ word);
		}
		return meaningOfWord;
	}
}
