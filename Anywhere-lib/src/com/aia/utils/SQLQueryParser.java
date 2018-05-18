package com.aia.utils;

public class SQLQueryParser {
	public class SqlQueryParserException extends Exception {
		public SqlQueryParserException(String message) {
			super(message);
		}
	}

	public String parseSqlQuery(String sql, Object[] obs) throws SqlQueryParserException {
		sql += " ";
		String[] splited = sql.split("\\?");
		if (splited.length - 1 != obs.length) {
			throw new SqlQueryParserException("Numbers of parameters does not match. Parameters: " + (splited.length - 1)
					+ ", objects: " + obs.length + ".");
		}
		String des = splited[0];
		for (int i = 1; i < splited.length; i++) {
			if (obs[i - 1] instanceof Integer)
				des += obs[i - 1];
			else
				des += "'" + obs[i - 1] + "'";
			des += splited[i];
		}
		return des;
	}

	public static void main(String[] args) {
		String sql = "select * from user where id = ? and username = ? and password = ? and age = ?";
		Object[] obs = { 1, "USERNAME", "PASSWORD", 123 };
		try {
			String a = new SQLQueryParser().parseSqlQuery(sql, obs);
			System.out.println(a);
		} catch (SqlQueryParserException e) {
			e.printStackTrace();
		}
	}
}
