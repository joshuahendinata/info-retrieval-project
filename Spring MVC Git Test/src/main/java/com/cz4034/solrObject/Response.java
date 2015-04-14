package com.cz4034.solrObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"numFound",
"start",
"maxScore",
"docs"
})
public class Response {

@JsonProperty("numFound")
private int numFound;
@JsonProperty("start")
private int start;
@JsonProperty("maxScore")
private double maxScore;
@JsonProperty("docs")
private List<Doc> docs = new ArrayList<Doc>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The numFound
*/
@JsonProperty("numFound")
public int getNumFound() {
return numFound;
}

/**
* 
* @param numFound
* The numFound
*/
@JsonProperty("numFound")
public void setNumFound(int numFound) {
this.numFound = numFound;
}

/**
* 
* @return
* The start
*/
@JsonProperty("start")
public int getStart() {
return start;
}

/**
* 
* @param start
* The start
*/
@JsonProperty("start")
public void setStart(int start) {
this.start = start;
}

/**
* 
* @return
* The maxScore
*/
@JsonProperty("maxScore")
public double getMaxScore() {
return maxScore;
}

/**
* 
* @param maxScore
* The maxScore
*/
@JsonProperty("maxScore")
public void setMaxScore(double maxScore) {
this.maxScore = maxScore;
}

/**
* 
* @return
* The docs
*/
@JsonProperty("docs")
public List<Doc> getDocs() {
return docs;
}

/**
* 
* @param docs
* The docs
*/
@JsonProperty("docs")
public void setDocs(List<Doc> docs) {
this.docs = docs;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}