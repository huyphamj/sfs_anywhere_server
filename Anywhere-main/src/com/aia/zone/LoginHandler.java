package com.aia.zone;

import com.aia.constant.Config;
import com.aia.constant.EventParams;
import com.aia.constant.ResponseCode;
import com.aia.db.bean.UserBean;
import com.aia.db.dao.DatabaseEngine;
import com.aia.utils.MD5Good;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		String phonenumber = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
		ISFSObject loginData = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
		if (phonenumber.length() > 0 && (!phonenumber.startsWith("Guest"))) {
			doLoginNormal(loginData, phonenumber);
		}
	}

	private void doLoginNormal(ISFSObject param, String phonenumber) throws SFSLoginException {
		String password = param.getUtfString(EventParams.PASSWORD);
		String clientSign = param.getUtfString(EventParams.SIGN);
		trace("do login normal with phonenumber: " + phonenumber + ", password: " + password + ", sign: " + clientSign);

		String serverSign = MD5Good.hash(phonenumber + Config.MD5SECRETCODE + password);
		if (!clientSign.equals(serverSign)) {
			ISFSObject errorInfo = new SFSObject();
			SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
			errorInfo.putInt(EventParams.STATUS, ResponseCode.FAIL);
			errorInfo.putUtfString(EventParams.MESSAGE, "Chữ kí không hợp lệ");
			data.addParameter("<<" + errorInfo.toJson() + ">>");
			throw new SFSLoginException("Invalid signature", data);
		} else {
			// ko dùng db thì comment 45, comment anywherezoneExtension.java dòng 11 và uncomment dùng 46 47 48 
			UserBean queryUser = DatabaseEngine.getInstance().userDAO.getUserByPhoneNumber(phonenumber);
//			UserBean queryUser = new UserBean();
//			queryUser.setPhonenumber("0987654321");
//			queryUser.setPassword("e10adc3949ba59abbe56e057f20f883e");
			if (!queryUser.getPassword().equals(password)) {
				ISFSObject errorInfo = new SFSObject();
				SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
				errorInfo.putInt(EventParams.STATUS, ResponseCode.FAIL);
				errorInfo.putUtfString(EventParams.MESSAGE, "Tài khoản không hợp lệ");
				data.addParameter("<<" + errorInfo.toJson() + ">>");
				throw new SFSLoginException("Invalid account", data);
			} else {

			}
		}
	}

}
