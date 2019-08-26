package com.zengshi.ecp.server.front.dto;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * Description: 提供给外部系统的分页 类<br>
 * Date: 2014年2月24日 <br>
 * 
 */
public class PageResponseDTO<T extends BaseResponseDTO> implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer pageNo = 1;// 请求查询的页码

	private Integer pageSize;// 每页显示条数

	private List<T> result;// 结果集

	private long count = 0;// 总条数

	private long pageCount;
	
	/**
	 * 
	 * buildByBaseInfo: 根据 BaseInfo 初始化的 分页返回结果信息；
	 *   内容包括：pageNo   = BaseInfo.pageNo ；
	 *         pageSize =  BaseInfo.pageSize ；
     * 
	 * @param info
	 * @return 
	 * @since JDK 1.6
	 */
	public static <T extends BaseResponseDTO> PageResponseDTO<T> buildByBaseInfo(BaseInfo info , Class<T> clazz){
	    PageResponseDTO<T> t = new PageResponseDTO<T>();
	    t.setPageNo(info.getPageNo());
	    t.setPageSize(info.getPageSize());
	    return t;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize==null?10:pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * 获取开始行
	 *   取的是上一页的结束行；从0开始；
     *   例如：第2页，每页10条记录，那么本页的起始记录：（2-1）×10 ；
	 * @return
	 */
	public int getStartRowIndex() {
		return (this.getPageNo() - 1) * this.getPageSize();
	}

	/**
	 * 获取结束行
	 *   取的是本页的结束行，
	 *   例如：第2页，每页10条记录，那么该值为：2×10 ；
	 * @return
	 */
	public int getEndRowIndex() {
		return this.getPageNo() * this.getPageSize();
	}

	/**
	 * 获取最大页数
	 * 
	 * @return
	 */
	public long getPageCount() {
		long quotient = this.getCount() / this.getPageSize();
		long remainder = this.getCount() % this.getPageSize();
		pageCount = quotient;
		if (remainder > 0) {
			pageCount += 1;
		}
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	
	public String toString() {
    	return  JSON.toJSONString(this);
    }

}
