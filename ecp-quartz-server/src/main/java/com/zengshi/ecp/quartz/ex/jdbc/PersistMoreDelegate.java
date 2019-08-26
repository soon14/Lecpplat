package com.zengshi.ecp.quartz.ex.jdbc;

import java.sql.Connection;

import org.quartz.impl.jdbcjobstore.DriverDelegate;

public interface PersistMoreDelegate extends DriverDelegate {

	public void insertJobExecutionException(Connection conn, String msg, String exDetail);
}
