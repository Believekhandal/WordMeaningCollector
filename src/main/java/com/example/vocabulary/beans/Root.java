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
public class Root{
    public String word;
    public String phonetic;
    public ArrayList<Phonetic> phonetics;
    public ArrayList<Meaning> meanings;
    public License license;
    public ArrayList<String> sourceUrls;
}

