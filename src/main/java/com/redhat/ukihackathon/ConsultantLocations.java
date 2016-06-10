package com.redhat.ukihackathon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

public class ConsultantLocations {
	
	List<ConsultantLocation> locations = new ArrayList<ConsultantLocation>();

	public List<ConsultantLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<ConsultantLocation> locations) {
		this.locations = locations;
	}
}
