package org.guili.ecshop.util;

import org.apache.ibatis.session.SqlSession;

/**
 * basic support
 * @author guili
 *
 */
public class BasicSqlSupport {

	protected SqlSession session;

	public SqlSession getSession() {
		return session;
	}

	public void setSession(SqlSession session) {
		this.session = session;
	}
	
}
