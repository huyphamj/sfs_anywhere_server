package com.aia.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.aia.constant.Config;

public class OTPManager {
	private static OTPManager INSTANCE;
	private Map<String, String> mapOtp;
	private Map<String, Long> mapTimeOtp;

	private OTPManager() {
		mapOtp = new HashMap<String, String>();
	}

	public static OTPManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OTPManager();
		return INSTANCE;
	}

	public String sendOTP(String phone) {
		if (mapOtp.containsKey(phone)) {
			mapOtp.remove(phone);
			mapTimeOtp.remove(phone);
		}
		String otpCode = generateOTP();
		mapOtp.put(phone, otpCode);
		mapTimeOtp.put(phone, System.currentTimeMillis());
		return BGateSmsApi.sendOtp(phone, otpCode);
	}

	public boolean verifyOTP(String phonenumber, String otpCode) {
		String storedCode = mapOtp.get(phonenumber);
		if (System.currentTimeMillis() - Config.OTP_EXPIRED_TIME * 1000 > mapTimeOtp.get(phonenumber)) {
			mapOtp.remove(phonenumber);
			mapTimeOtp.remove(phonenumber);
			return false;
		}
		if (storedCode.equals(otpCode)) {
			mapOtp.remove(phonenumber);
			mapTimeOtp.remove(phonenumber);
			return true;
		} else
			return false;
	}

	private String generateOTP() {
		Random random = new Random();
		int code = random.nextInt(8999) + 1000;
		return code + "";
	}

}
