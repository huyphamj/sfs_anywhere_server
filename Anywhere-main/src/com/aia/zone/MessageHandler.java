package com.aia.zone;

import com.aia.constant.LobbyCommand;
import com.aia.constant.EventParams;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class MessageHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject param) {
		ISFSObject res = new SFSObject();
		res.putInt(EventParams.IS_SUCCESS, 1);
		send(LobbyCommand.MESSAGE, res, user);
	}

}
