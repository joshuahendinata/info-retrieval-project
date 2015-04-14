package com.cz4034.tweetJSON;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Docs {
	
	private String id;
	private List<String> text = new ArrayList<String>();
	private List<String> _class = new ArrayList<String>();
	private List<String> user = new ArrayList<String>();
	private String time;
	private List<String> lat = new ArrayList<String>();
	private List<String> lng = new ArrayList<String>();
	private String timeConverted; 
	private String version;
	//private Random rand = new Random();
	
	public String getId() {
		return id;
	}
	
	public List<String> getText() {
		return text;
	}
	
	public List<String> getClass_() {
		return _class;
	}
	
	public List<String> getUser() {
		return user;
	}
	
	@JsonProperty("_version_")
	public void setVersion(String version){
		this.version = version;
	}
	
	public String getVersion() {
		return version;
	}
	
	public List<String> getLat(){
		//lat = rand.nextInt(160) - 80;
		return lat;
	}

	public List<String> getLng(){
		//lng = rand.nextInt(160) - 80;
		return lng;
	}

	public String getTime() {
		
		return time;
	}

	@JsonProperty("time")
	public void setTime(List<String> time) {
		SimpleDateFormat dt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		Date date = null;
		try {
			date = dt.parse(time.get(0));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		timeConverted = new SimpleDateFormat("dd MMM yyyy - HH:mm").format(date);
		
		this.time = timeConverted;
	}
}