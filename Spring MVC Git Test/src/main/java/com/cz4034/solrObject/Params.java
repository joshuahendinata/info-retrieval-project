package com.cz4034.solrObject;

import java.util.HashMap;
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
"q",
"indent",
"wt"
})
public class Params {

@JsonProperty("q")
private String q;
@JsonProperty("indent")
private String indent;
@JsonProperty("wt")
private String wt;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The q
*/
@JsonProperty("q")
public String getQ() {
return q;
}

/**
* 
* @param q
* The q
*/
@JsonProperty("q")
public void setQ(String q) {
this.q = q;
}

/**
* 
* @return
* The indent
*/
@JsonProperty("indent")
public String getIndent() {
return indent;
}

/**
* 
* @param indent
* The indent
*/
@JsonProperty("indent")
public void setIndent(String indent) {
this.indent = indent;
}

/**
* 
* @return
* The wt
*/
@JsonProperty("wt")
public String getWt() {
return wt;
}

/**
* 
* @param wt
* The wt
*/
@JsonProperty("wt")
public void setWt(String wt) {
this.wt = wt;
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