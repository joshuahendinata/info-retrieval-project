package solrObject;

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
"responseHeader",
"response"
})
public class SolrObject {

@JsonProperty("responseHeader")
private ResponseHeader responseHeader;
@JsonProperty("response")
private Response response;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The responseHeader
*/
@JsonProperty("responseHeader")
public ResponseHeader getResponseHeader() {
return responseHeader;
}

/**
* 
* @param responseHeader
* The responseHeader
*/
@JsonProperty("responseHeader")
public void setResponseHeader(ResponseHeader responseHeader) {
this.responseHeader = responseHeader;
}

/**
* 
* @return
* The response
*/
@JsonProperty("response")
public Response getResponse() {
return response;
}

/**
* 
* @param response
* The response
*/
@JsonProperty("response")
public void setResponse(Response response) {
this.response = response;
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