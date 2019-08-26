/**
 * 
 */
package com.zengshi.ecp.frame.vo;

import com.zengshi.paas.common.ICriteria;

import java.io.Serializable;

/**
 * 查询条件的父类；
 * 
 * 
 * 修改记录：
 * 2016.02.01
 * 增加setOrderByClause方法用于处理表格排序列及排序方式处理
 */
@SuppressWarnings("serial")
public class BaseCriteria implements ICriteria,Serializable{
	private int limitClauseStart = -1;

	private int limitClauseCount = -1;

	public int getLimitClauseStart() {
		return limitClauseStart;
	}

	public void setLimitClauseStart(int limitClauseStart) {
		this.limitClauseStart = limitClauseStart;
	}

	public int getLimitClauseCount() {
		return limitClauseCount;
	}

	public void setLimitClauseCount(int limitClauseCount) {
		this.limitClauseCount = limitClauseCount;
	}

	/**
	 * 重写setOrderByClause，用于接口部分可以处理表格排序列名称及排序方式
	 * 当前位置并不重写，具体表模型的example中会自动生成setOrderByClause同名方法，进行重写
	 */
	//@Override
	public void setOrderByClause(String orderByClause) {}
}
