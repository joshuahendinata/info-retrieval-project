package com.cz4034.tweetJSON;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Facet_fields {
	private List<String> categoryClass = new ArrayList<String>();
	
	@JsonProperty("class")
	public List<String> getClassCategory(){
		return categoryClass;
	}
}
