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
"id",
"cat",
"name",
"price",
"inStock",
"author",
"series_t",
"sequence_i",
"genre_s",
"_version_"
})
public class Doc {

@JsonProperty("id")
private String id;
@JsonProperty("cat")
private List<String> cat = new ArrayList<String>();
@JsonProperty("name")
private List<String> name = new ArrayList<String>();
@JsonProperty("price")
private List<Double> price = new ArrayList<Double>();
@JsonProperty("inStock")
private List<Boolean> inStock = new ArrayList<Boolean>();
@JsonProperty("author")
private List<String> author = new ArrayList<String>();
@JsonProperty("series_t")
private List<String> seriesT = new ArrayList<String>();
@JsonProperty("sequence_i")
private int sequenceI;
@JsonProperty("genre_s")
private String genreS;
@JsonProperty("_version_")
private String Version;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The id
*/
@JsonProperty("id")
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The cat
*/
@JsonProperty("cat")
public List<String> getCat() {
return cat;
}

/**
* 
* @param cat
* The cat
*/
@JsonProperty("cat")
public void setCat(List<String> cat) {
this.cat = cat;
}

/**
* 
* @return
* The name
*/
@JsonProperty("name")
public List<String> getName() {
return name;
}

/**
* 
* @param name
* The name
*/
@JsonProperty("name")
public void setName(List<String> name) {
this.name = name;
}

/**
* 
* @return
* The price
*/
@JsonProperty("price")
public List<Double> getPrice() {
return price;
}

/**
* 
* @param price
* The price
*/
@JsonProperty("price")
public void setPrice(List<Double> price) {
this.price = price;
}

/**
* 
* @return
* The inStock
*/
@JsonProperty("inStock")
public List<Boolean> getInStock() {
return inStock;
}

/**
* 
* @param inStock
* The inStock
*/
@JsonProperty("inStock")
public void setInStock(List<Boolean> inStock) {
this.inStock = inStock;
}

/**
* 
* @return
* The author
*/
@JsonProperty("author")
public List<String> getAuthor() {
return author;
}

/**
* 
* @param author
* The author
*/
@JsonProperty("author")
public void setAuthor(List<String> author) {
this.author = author;
}

/**
* 
* @return
* The seriesT
*/
@JsonProperty("series_t")
public List<String> getSeriesT() {
return seriesT;
}

/**
* 
* @param seriesT
* The series_t
*/
@JsonProperty("series_t")
public void setSeriesT(List<String> seriesT) {
this.seriesT = seriesT;
}

/**
* 
* @return
* The sequenceI
*/
@JsonProperty("sequence_i")
public int getSequenceI() {
return sequenceI;
}

/**
* 
* @param sequenceI
* The sequence_i
*/
@JsonProperty("sequence_i")
public void setSequenceI(int sequenceI) {
this.sequenceI = sequenceI;
}

/**
* 
* @return
* The genreS
*/
@JsonProperty("genre_s")
public String getGenreS() {
return genreS;
}

/**
* 
* @param genreS
* The genre_s
*/
@JsonProperty("genre_s")
public void setGenreS(String genreS) {
this.genreS = genreS;
}

/**
* 
* @return
* The Version
*/
@JsonProperty("_version_")
public String getVersion() {
return Version;
}

/**
* 
* @param Version
* The _version_
*/
@JsonProperty("_version_")
public void setVersion(String Version) {
this.Version = Version;
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