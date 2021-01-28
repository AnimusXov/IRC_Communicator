package org.irccom.helper;

import lombok.Getter;
import org.apache.commons.validator.routines.UrlValidator;

public class Validator{
	private static final String[] schemes = {"http","https"};
	
	@Getter
	private static final UrlValidator urlValidator = new UrlValidator(schemes);
	
	public  static boolean isUrlValid(String address){
		return urlValidator.isValid(address);
	}
}
