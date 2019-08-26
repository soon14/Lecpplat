package com.zengshi.ecp.quartz.ex.jdbc;

import org.quartz.TriggerKey;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDelegate extends StdJDBCDelegate implements PersistMoreDelegate {
	private static final Logger log = LoggerFactory.getLogger(MysqlDelegate.class);

	public static final String TABLE_TRIGGERS_HISTORY = "TRIGGERS_HISTORY";

	public static final String TABLE_JOB_EXECUTION_EXCEPTION = "JOB_EXECUTION_EXCEPTION";

	public static final String INSERT_JOB_EXECUTION_EXCEPTION = "INSERT INTO " + TABLE_PREFIX_SUBST + TABLE_JOB_EXECUTION_EXCEPTION + " VALUES(?,?,?)";

	public static final String INSERT_TRIGGER_HISTORY = "INSERT INTO " + TABLE_PREFIX_SUBST + TABLE_TRIGGERS_HISTORY + "(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,JOB_NAME,JOB_GROUP,DESCRIPTION,NEXT_FIRE_TIME,PREV_FIRE_TIME,PRIORITY,TRIGGER_STATE,TRIGGER_TYPE,START_TIME,END_TIME,CALENDAR_NAME,MISFIRE_INSTR,JOB_DATA) "
			+ " SELECT SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,JOB_NAME,JOB_GROUP,DESCRIPTION,NEXT_FIRE_TIME,PREV_FIRE_TIME,PRIORITY,TRIGGER_STATE,TRIGGER_TYPE,START_TIME,END_TIME,CALENDAR_NAME,MISFIRE_INSTR,JOB_DATA FROM " + TABLE_PREFIX_SUBST + TABLE_TRIGGERS + " WHERE "
			+ COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_TRIGGER_NAME + " = ? AND " + COL_TRIGGER_GROUP + " = ?";


	// ---------------------------------------------------------------------------
	// protected methods that can be overridden by subclasses
	// ---------------------------------------------------------------------------

	public MysqlDelegate() {
		log.info("Mysql Delegate call...............");
	}


	@SuppressWarnings("deprecation")
/*	protected Blob writeDataToBlob(ResultSet rs, int column, byte[] data) throws SQLException {

		Blob blob = rs.getBlob(column); // get blob

		if (blob == null) {
			throw new SQLException("Driver's Blob representation is null!");
		}

		if (blob instanceof oracle.sql.BLOB) { // is it an oracle blob?
			((oracle.sql.BLOB) blob).putBytes(1, data);
			((oracle.sql.BLOB) blob).trim(data.length);
			return blob;
		} else {
			throw new SQLException("Driver's Blob representation is of an unsupported type: " + blob.getClass().getName());
		}
	}*/

	/*
	 * 扩展，在quartz删除触发器之前，先将触发器移到历史表
	 * 
	 * @see org.quartz.impl.jdbcjobstore.StdJDBCDelegate#deleteTrigger(java.sql.
	 * Connection, org.quartz.TriggerKey)
	 */
	public int deleteTrigger(Connection conn, TriggerKey triggerKey) throws SQLException {
		// save trigger to history before deleted
		PreparedStatement ps = null;
		int insertNum = 0;
		try {
			ps = conn.prepareStatement(rtp(INSERT_TRIGGER_HISTORY));
			ps.setString(1, triggerKey.getName());
			ps.setString(2, triggerKey.getGroup());
			insertNum = ps.executeUpdate();
		} finally {
			closeStatement(ps);
		}
		int delNum = super.deleteTrigger(conn, triggerKey);
		if (insertNum != delNum)
			log.error("insert history num not equal delete num,insert num is" + insertNum + " delete num is" + delNum);
		return delNum;
	}


	@Override
	public void insertJobExecutionException(Connection conn, String msg, String exDetail) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(rtp(INSERT_JOB_EXECUTION_EXCEPTION));
			ps.setString(1, msg);
			ps.setBigDecimal(2, new BigDecimal(System.currentTimeMillis()));
			ps.setString(3, exDetail);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			closeStatement(ps);
		}

	}
}
