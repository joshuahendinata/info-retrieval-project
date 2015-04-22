package com.cz4034.assignment;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
	
	@Override
	public String toString() {
		return "Page [name=" + name + ", about=" + about + ", phone=" + phone
				+ ", website=" + website + ", category_list=" + category_list
				+ "]";
	}

	private String name;
    private String about;
    private String phone;
    private String website;
    private Map<String, ArrayList<String>> category_list;
    
    public Page(String name, String about, String phone, String website) {
		this.name = name;
		this.about = about;
		this.phone = phone;
		this.website = website;
	}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getAbout() {
        return about;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}