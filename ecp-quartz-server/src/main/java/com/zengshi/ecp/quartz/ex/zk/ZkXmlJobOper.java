package com.zengshi.ecp.quartz.ex.zk;

public interface ZkXmlJobOper {
	public void addAndModifyJob(String jobName, String jobXmlContent);

	public void removeJob(String jobName, String jobGroup);
}
