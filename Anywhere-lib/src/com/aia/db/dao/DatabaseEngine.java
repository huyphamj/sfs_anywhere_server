package com.aia.db.dao;

import org.json.simple.JSONObject;

import common.jdbc.JdbcConnectionPool;
import common.jdbc.JdbcParameter;

public class DatabaseEngine {
	// ===== Config db =====//
	private String host = "localhost";
	private String username = "root";
	private String password = "Huy211296";
	private int port = 3306;
	private String dbName = "anywhere";

	// ===== Declare DAO =====//
	public UserDAO userDAO;

	private JdbcParameter parameter;
	private JdbcConnectionPool pool;

	private static DatabaseEngine INSTANCE;

	private DatabaseEngine() {

	}

	public static DatabaseEngine getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DatabaseEngine();
		return INSTANCE;
	}

	public void start() {
		JSONObject json = new JSONObject();
		parameter = new JdbcParameter();
		parameter.setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbName);
		parameter.setUsername(username);
		parameter.setPassword(password);
		pool = new JdbcConnectionPool(parameter);

		userDAO = new UserDAO(pool);
	}
}
