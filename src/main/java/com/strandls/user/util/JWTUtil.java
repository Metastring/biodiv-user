package com.strandls.user.util;

import java.util.Date;

public class JWTUtil {
	
	private static final long ACCESS_TOKEN_EXPIRY_DAYS = 1;
	private static final long REFRESH_TOKEN_EXPIRY_DAYS = 30;
	
	public static Date getAccessTokenExpiryDate() {
		final Date now = new Date();
		long expDate = now.getTime() + (ACCESS_TOKEN_EXPIRY_DAYS * (24 * 3600 * 1000));
		return new Date(expDate);
	}

	public static Date getRefreshTokenExpiryDate() {
		final Date now = new Date();
		long expDate = now.getTime() + (REFRESH_TOKEN_EXPIRY_DAYS * (24 * 3600 * 1000));
		return new Date(expDate);
	}

}
