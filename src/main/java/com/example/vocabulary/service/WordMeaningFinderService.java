package com.example.vocabulary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.vocabulary.beans.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WordMeaningFinderService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public ResponseEntity<Object> callDictionaryApi(String word)
			throws JsonProcessingException {
		// Try primary API first
		ResponseEntity<Object> primaryResult = callPrimaryDictionaryApi(word);
		if (primaryResult.getStatusCode().is2xxSuccessful()) {
			return primaryResult;
		}

		// If primary API fails, try fallback APIs
		log.debug("Primary API failed for word '{}', trying fallback APIs", word);

		// Try Datamuse API as first fallback
		ResponseEntity<Object> datamuseResult = callDatamuseApi(word);
		if (datamuseResult.getStatusCode().is2xxSuccessful()) {
			log.debug("Found meaning for word '{}' using Datamuse API", word);
			return datamuseResult;
		}

		// Try Urban Dictionary as second fallback
		ResponseEntity<Object> urbanResult = callUrbanDictionaryApi(word);
		if (urbanResult.getStatusCode().is2xxSuccessful()) {
			log.debug("Found meaning for word '{}' using Urban Dictionary API", word);
			return urbanResult;
		}

		log.debug("All APIs failed for word '{}'", word);
		return ResponseEntity.notFound().build();
	}

	private ResponseEntity<Object> callPrimaryDictionaryApi(String word) throws JsonProcessingException {
		try {
			String fooResourceUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
			ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
			log.debug("response received for word: {}", word);

			String body = response.getBody();
			if (body == null || body.isEmpty()) {
				log.warn("Empty response body for word: {}", word);
				return ResponseEntity.notFound().build();
			}

			// The API returns an array, but we only need the first element.
			Root[] roots = objectMapper.readValue(body, Root[].class);
			if (roots.length > 0 && roots[0] != null) {
				Root root = roots[0];
				if (root.getMeanings() != null && !root.getMeanings().isEmpty()) {
					var meaning = root.getMeanings().get(0);
					if (meaning.getDefinitions() != null && !meaning.getDefinitions().isEmpty()) {
						String definition = meaning.getDefinitions().get(0).getDefinition();
						if (definition != null && !definition.trim().isEmpty()) {
							return ResponseEntity.ok().body(definition);
						}
					}
				}
			}

			log.warn("No valid definition found for word: {}", word);
			return ResponseEntity.notFound().build();
		} catch (HttpClientErrorException.NotFound e) {
			log.debug("Word '{}' not found in primary dictionary API (404)", word);
			return ResponseEntity.notFound().build();
		} catch (HttpClientErrorException.TooManyRequests e) {
			log.warn("Rate limit exceeded for word '{}' (429)", word);
			return ResponseEntity.notFound().build();
		} catch (HttpClientErrorException e) {
			log.warn("HTTP client error for word '{}': {}", word, e.getStatusCode());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Exception occurred while calling primary dictionary API for word: {}", word, e);
			return ResponseEntity.notFound().build();
		}
	}

	private ResponseEntity<Object> callDatamuseApi(String word) {
		try {
			String datamuseUrl = "https://api.datamuse.com/words?sp=" + word + "&md=d&max=1";
			ResponseEntity<String> response = restTemplate.getForEntity(datamuseUrl, String.class);

			String body = response.getBody();
			if (body == null || body.isEmpty() || body.equals("[]")) {
				return ResponseEntity.notFound().build();
			}

			// Parse Datamuse response - it's an array of word objects
			var datamuseResponse = objectMapper.readTree(body);
			if (datamuseResponse.isArray() && datamuseResponse.size() > 0) {
				var firstResult = datamuseResponse.get(0);
				if (firstResult.has("defs") && firstResult.get("defs").isArray() && firstResult.get("defs").size() > 0) {
					String def = firstResult.get("defs").get(0).asText();
					// Datamuse format: "n\tdefinition text" - extract just the definition part
					if (def.contains("\t")) {
						def = def.substring(def.indexOf("\t") + 1);
					}
					return ResponseEntity.ok().body(def);
				}
			}

			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.debug("Datamuse API failed for word '{}': {}", word, e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private ResponseEntity<Object> callUrbanDictionaryApi(String word) {
		try {
			String urbanUrl = "https://api.urbandictionary.com/v0/define?term=" + word;
			ResponseEntity<String> response = restTemplate.getForEntity(urbanUrl, String.class);

			String body = response.getBody();
			if (body == null || body.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			// Parse Urban Dictionary response
			var urbanResponse = objectMapper.readTree(body);
			if (urbanResponse.has("list") && urbanResponse.get("list").isArray() && urbanResponse.get("list").size() > 0) {
				var firstDefinition = urbanResponse.get("list").get(0);
				if (firstDefinition.has("definition")) {
					String definition = firstDefinition.get("definition").asText();
					// Add note that this is from Urban Dictionary (informal)
					return ResponseEntity.ok().body(definition + " [Urban Dictionary]");
				}
			}

			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.debug("Urban Dictionary API failed for word '{}': {}", word, e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
