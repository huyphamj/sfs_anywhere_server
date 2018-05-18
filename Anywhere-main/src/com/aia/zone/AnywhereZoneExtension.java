package com.aia.zone;

import com.aia.db.dao.DatabaseEngine;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class AnywhereZoneExtension extends SFSExtension {

	@Override
	public void init() {
		DatabaseEngine.getInstance().start();
		
		addEventHandler(SFSEventType.USER_LOGIN, LoginHandler.class);
		addEventHandler(SFSEventType.USER_DISCONNECT, DisconnectHandler.class);
		addEventHandler(SFSEventType.USER_LOGOUT, LogoutHandler.class);

//		addRequestHandler(EventKey.MESSAGE, MessageHandler.class);
		addRequestHandler("lobby", LobbyRequestHandler.class);
	}
}
