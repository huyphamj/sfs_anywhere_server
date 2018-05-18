package com.aia.utils;

public class BGateSmsApi {

	public static String sendOtp(String phone, String message) {
		DefaultHttpConnection httpConnect = new DefaultHttpConnection();
		String app_code = "BONGDA69";
		String app_key = "1fd7e7322f032c62dd3dc5013d5e689e";
		String sign = MD5Good.hash(phone + app_code + message + app_key);
		String params = "app_code=" + app_code + "&to_phone=" + phone + "&message=" + message + "&sign=" + sign;
		String url = "http://sms.pipostudio.com:8080/ws/twilio/sms";
		String response = httpConnect.post(url, params);
		return response;
	}

}
