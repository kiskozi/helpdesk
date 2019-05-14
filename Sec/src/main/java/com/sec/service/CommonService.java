package com.sec.service;

import org.springframework.stereotype.Service;

@Service
public class CommonService {
	
	public Long idToLong(String idInString) {
		Long toLong = 0L;
		if ( idInString != null && idInString.trim() != "" ) {
			try {
				toLong = Long.parseLong(idInString);
			} catch (NumberFormatException e) {}
		}
		return toLong;
	}
	
}
