package no.snms.app.domain;

public class GeolocationSearchResult {
	
	Geolocation [] results;
	String status;
	 
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Geolocation[] getResults() {
		return results;
	}

	public void setResults(Geolocation[] results) {
		this.results = results;
	} 
	
	

}
