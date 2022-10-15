package com.example.vocabulary.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.vocabulary.beans.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WordMeaningFinderService {

	Root root;
	
    RestTemplate restTemplate = new RestTemplate();


//	@GetMapping(value = "meaningFinder/{word}")
	public ResponseEntity<Object> callDictionaryApi(String word)
			throws JsonMappingException, JsonProcessingException {
//		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		log.info("response received: "+response);

//		System.out.println(response);
		String regex = "^\\[|\\]$";
		String res = response.getBody().replaceAll(regex, "");

		root = new ObjectMapper().readValue(res, Root.class);
		return ResponseEntity.ok().body(root.getMeanings().get(0).getDefinitions().get(0).getDefinition());
	}
}
