package com.cz4034.tweetJSON;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Facet_counts {
	private Facet_fields facet_fields;
	
	public Facet_fields getFacet_fields(){
		return facet_fields;
	}
}
