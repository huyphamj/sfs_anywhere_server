package com.aia.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.aia.db.bean.UserBean;
import com.aia.utils.SQLQueryParser;
import com.aia.utils.SQLQueryParser.SqlQueryParserException;

import common.jdbc.JdbcConnectionPool;
import common.jdbc.core.RowMapper;
import common.jdbc.core.simple.SimpleJdbcDaoSupport;

public class UserDAO extends SimpleJdbcDaoSupport {
	public UserDAO(JdbcConnectionPool pool) {
		super(pool);
	}

	public class UserMapper implements RowMapper {
		@Override
		public UserBean mapRow(ResultSet rs, int arg1) throws SQLException {
			UserBean u = new UserBean();
			u.setPassword(rs.getString("password"));
			u.setPhonenumber(rs.getString("phonenumber"));
			u.setName(rs.getString("name"));
			u.setAvatar(rs.getString("avatar"));
			u.setAddress(rs.getString("address"));
			u.setStatic_code(rs.getString("static_code"));
			u.setDynamic_code(rs.getString("dynamic_code"));
			return u;
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserBean> getAllUsers() {
		String sql = "SELECT * FROM user";
		return getSimpleJdbcTemplate().query(sql, new UserMapper());
	}

	public UserBean getUserByPhoneNumber(String phonenumber) {
		String sql = "SELECT * FROM user WHERE phonenumber = " + phonenumber;
		List<UserBean> list = getSimpleJdbcTemplate().query(sql, new UserMapper());
		if (list.size() == 0 || list == null)
			return null;
		else
			return list.get(0);
	}

	public boolean insert(UserBean user) {
		try {
			UserBean existUser = getUserByPhoneNumber(user.getName());
			if (existUser == null)
				return false;
			String sql = "INSERT INTO user(password, phonenumber, name, avatar, address, static_code, dynamic_code) VALUES(?,?,?,?,?,?,?)";
			Object[] obs = { user.getPassword(), user.getPhonenumber(), user.getName(), user.getAvatar(),
					user.getAddress(), user.getStatic_code(), user.getDynamic_code() };
			sql = new SQLQueryParser().parseSqlQuery(sql, obs);
			getSimpleJdbcTemplate().update(sql);
			return true;
		} catch (SqlQueryParserException e) {
			e.printStackTrace();
			return false;
		}
	}
}
