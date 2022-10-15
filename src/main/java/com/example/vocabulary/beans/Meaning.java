package com.example.vocabulary.beans;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meaning{
    public String partOfSpeech;
    public ArrayList<Definition> definitions;
    public ArrayList<Object> synonyms;
    public ArrayList<Object> antonyms;
}

