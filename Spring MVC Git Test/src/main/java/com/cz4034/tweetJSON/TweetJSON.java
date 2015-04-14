package com.cz4034.tweetJSON;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetJSON {
	private Response response;
	private Facet_counts facet_counts;
	
	public Response getResponse() {
		return response;
	}
	
	public Facet_counts getFacet_counts(){
		return facet_counts;
	}
}
