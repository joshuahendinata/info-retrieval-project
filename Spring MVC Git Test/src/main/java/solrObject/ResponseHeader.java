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
"status",
"QTime",
"params"
})
public class ResponseHeader {

@JsonProperty("status")
private int status;
@JsonProperty("QTime")
private int QTime;
@JsonProperty("params")
private Params params;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The status
*/
@JsonProperty("status")
public int getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
@JsonProperty("status")
public void setStatus(int status) {
this.status = status;
}

/**
* 
* @return
* The QTime
*/
@JsonProperty("QTime")
public int getQTime() {
return QTime;
}

/**
* 
* @param QTime
* The QTime
*/
@JsonProperty("QTime")
public void setQTime(int QTime) {
this.QTime = QTime;
}

/**
* 
* @return
* The params
*/
@JsonProperty("params")
public Params getParams() {
return params;
}

/**
* 
* @param params
* The params
*/
@JsonProperty("params")
public void setParams(Params params) {
this.params = params;
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