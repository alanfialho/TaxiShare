package com.br.resources;

public enum Types {
	
	THOROUGHFARE(1),
	SUBTHOROUGHFARE(2),
	SUBLOCALITY(3),
	LOCALITY(4),
	ADMINISTRATIVE_AREA_LEVEL_1(5),
	COUNTRYNAME(6),
	LATITUDE(7),
	LONGITUDE(8),
	POSTALCODE(9);

	 
	private int statusCode;
 
	private Types(int s) {
		statusCode = s;
	}
 
	public int getStatusCode() {
		return statusCode;
	}   
	
	};   


