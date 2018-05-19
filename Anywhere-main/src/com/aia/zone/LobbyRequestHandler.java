package com.aia.zone;

import com.aia.constant.EventParams;
import com.aia.constant.LobbyCommand;
import com.aia.constant.ResponseCode;
import com.aia.db.bean.UserBean;
import com.aia.db.dao.DatabaseEngine;
import com.aia.db.dao.UserDAO;
import com.aia.utils.OTPManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class LobbyRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject param) {

		// String command = param.getUtfString(EventParams.REQUEST_ID);
		String command = param.getUtfString("REQUEST_ID");
		if (command.equals(LobbyCommand.REGISTER)) {
			onRegister(user, param);
		} else if (command.equals(LobbyCommand.VERIFY_OTP)) {
			onVerifyOtp(user, param);
		} else if (command.equals(LobbyCommand.GET_USER_DATA)) {
			onGetUserData(user, param);
		}
	}

	private void onRegister(User user, ISFSObject param) {
		UserDAO userDAO = DatabaseEngine.getInstance().userDAO;
		ISFSObject result;

		String phonenumber = param.getUtfString(EventParams.PHONENUMBER);
		UserBean userBean = userDAO.getUserByPhoneNumber(phonenumber);
		if (userBean != null)
			result = this.getResultFailedObject("SDT đã tồn tại.");
		else {
			String otpApiResponse = OTPManager.getInstance().sendOTP(phonenumber);
			if (otpApiResponse == null)
				result = this.getResultFailedObject("Không thể gửi tin nhắn OTP.");
			else
				result = this.getResultSuccessObject();
		}
		send(LobbyCommand.REGISTER, result, user);
	}

	private void onVerifyOtp(User user, ISFSObject param) {
		String otpCode = param.getUtfString(EventParams.OTP_CODE);
		String phonenumber = param.getUtfString(EventParams.PHONENUMBER);
		boolean validOtp = OTPManager.getInstance().verifyOTP(phonenumber, otpCode);
		if (!validOtp) {
			ISFSObject result = getResultFailedObject("Mã OTP không hợp lệ.");
			send(LobbyCommand.VERIFY_OTP, result, user);
			return;
		}

		String name = param.getUtfString(EventParams.NAME);
		String password = param.getUtfString(EventParams.PASSWORD);
		String avatar = param.getUtfString(EventParams.AVATAR);
		String address = param.getUtfString(EventParams.ADDRESS);

		UserBean newUser = new UserBean();
		newUser.setPassword(password);
		newUser.setName(name);
		newUser.setPhonenumber(phonenumber);
		newUser.setAvatar(avatar);
		newUser.setAddress(address);

		UserDAO userDAO = DatabaseEngine.getInstance().userDAO;
		boolean valid = userDAO.insert(newUser);
		ISFSObject result;
		if (!valid)
			result = getResultFailedObject("Tài khoản không hợp lệ.");
		else {
			result = getResultSuccessObject();
		}
		send(LobbyCommand.VERIFY_OTP, result, user);
	}

	private void onGetUserData(User user, ISFSObject param) {
		// String password = param.getUtfString(EventParams.PASSWORD);
		// String phonenumber = param.getUtfString(EventParams.PHONENUMBER);
		// String clientSign = param.getUtfString(EventParams.SIGN);
		// String serverSign = MD5Good.hash(phonenumber + Config.MD5SECRETCODE +
		// password);
		// ISFSObject result;
		// if (!clientSign.equals(serverSign)) {
		// result = getResultFailedObject("Chứ kí không hợp lệ.");
		// send(LobbyCommand.GET_USER_DATA, result, user);
		// } else {
		// UserBean queryUser =
		// DatabaseEngine.getInstance().userDAO.getUserByPhoneNumber(phonenumber);
		// if (!queryUser.getPassword().equals(password)) {
		// result = getResultFailedObject("Tài khoản không hợp lệ.");
		// send(LobbyCommand.LOGIN, result, user);
		// } else {
		String username = user.getName();
		trace("========== DEBUG ========== on get user data: username = " + username);
		String phonenumber = param.getUtfString(EventParams.PHONENUMBER);
		UserBean queryUser = DatabaseEngine.getInstance().userDAO.getUserByPhoneNumber(phonenumber);
		ISFSObject result = getResultSuccessObject();
		ISFSObject content = new SFSObject();
		content.putUtfString(EventParams.NAME, queryUser.getName());
		content.putUtfString(EventParams.AVATAR, queryUser.getAvatar());
		content.putUtfString(EventParams.ADDRESS, queryUser.getAddress());
		content.putUtfString(EventParams.STATIC_CODE, queryUser.getStatic_code());
		result.putSFSObject(EventParams.CONTENT, content);
		send(LobbyCommand.GET_USER_DATA, result, user);
		// }
		// }
	}

	protected ISFSObject getResultSuccessObject() {
		ISFSObject result = new SFSObject();
		result.putInt(EventParams.STATUS, ResponseCode.SUCCESS);
		return result;
	}

	protected ISFSObject getResultFailedObject(String message) {
		ISFSObject result = new SFSObject();
		result.putInt(EventParams.STATUS, ResponseCode.FAIL);
		result.putUtfString(EventParams.MESSAGE, message);
		return result;
	}
}
