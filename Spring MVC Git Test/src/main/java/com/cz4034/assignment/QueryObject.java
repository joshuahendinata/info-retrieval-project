package com.cz4034.assignment;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryObject {
	private String queryKey;
	private String category;
	private int startRow = 0, tweetNum = 10; 
	private String loc = "";
	private String UID = "";

	public QueryObject(){
		this.queryKey = "";
		this.category = "*";
		this.startRow = 0;
		this.tweetNum = 10;
		this.loc = "";
		this.UID = UUID.randomUUID().toString();
	}
	
	public void setNewQuery(String newQuery){
		this.queryKey = newQuery;
		this.category = "*";
		this.startRow = 0;
		this.tweetNum = 10;
		this.loc = "";
	}
	
	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	@Override
	public String toString() {
		return "QueryObject [queryKey=" + queryKey + ", category=" + category
				+ ", startRow=" + startRow + ", tweetNum=" + tweetNum
				+ ", loc=" + loc + ", UID=" + UID + "]";
	}

	public void checkStatus(String inputCat){
		// If all is chosen, change to *
		if(inputCat.equals("all")){
			inputCat = "*";
		}
		
		// If the selection is the same (through scrolling) load another 10 tweets
		if(inputCat.equals(category)){
			this.startRow += this.tweetNum;
		}
		// else, starts from 0 and query 10 tweets
		else{
			this.category = inputCat;
			this.startRow = 0;
		}
	}

	public int getStartRow() {
		return startRow;
	}

	public int getTweetNum(){
		return tweetNum;
	}
	
	public String getCategory(){
		return category;
	}

	public void setCategory(String inputCat){
		this.category = inputCat;
	}
	
	public String getQueryKey(){
		return queryKey.replaceAll("[\\[\\]{}\\\\^]+","");
	}
	
	public void setQueryKey(String queryInput){
		this.queryKey = queryInput.replaceAll("[\\[\\]{}\\\\^]+","");
	}

	public String getLoc(){
		return loc;
	}
	
	public void setLoc(String locInput){
		String x = "0.0", y = "0.0";
		
		// Reset row everytime it is called
		startRow = 0;
		
		if(locInput.equals("loc1")){
			loc = ""; return; // no query 
		}
		else if(locInput.equals("loc2")){
			x = "0.0"; y = "0.0";
		}
		else if(locInput.equals("loc3")){
			x = "34.050"; y = "-118.250"; //LA
		}else if(locInput.equals("loc4")){
			x = "40.7127"; y = "-74.0059"; //NYC
		}else if(locInput.equals("loc5")){
			x = "1.3"; y = "103.8"; //SG
		}else if(locInput.equals("loc6")){
			x = "51.5072"; y = "-0.1275"; //London
		}else if(locInput.equals("loc7")){
			x = "48.8567"; y = "2.3508"; //Paris
		}
		
		loc = "{!geofilt}&spatial=true"
				+ "&pt=" + x 
				+ ","	+ y
				+ "&sfield=pos_p"
				+ "&d=200";
	}
}
