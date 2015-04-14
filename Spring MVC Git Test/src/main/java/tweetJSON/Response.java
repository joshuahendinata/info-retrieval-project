package tweetJSON;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
	private String numFound;
	private ArrayList<Docs> docs = new ArrayList<Docs>();
	
	public ArrayList<Docs> getDocs(){
		return docs;
	}	
	
	public String getNumFound(){
		return numFound;
	}
}
