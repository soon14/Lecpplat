package com.zengshi.ecp.server.dao.mapper.common;

import com.zengshi.ecp.server.dao.model.common.DataInout;
import com.zengshi.ecp.server.dao.model.common.DataInoutCriteria;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

public interface DataInoutMapper {
    Long countByExample(DataInoutCriteria example) throws DataAccessException;

    int deleteByExample(DataInoutCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(BigDecimal id) throws DataAccessException;

    int insert(DataInout record) throws DataAccessException;

    int insertSelective(DataInout record) throws DataAccessException;

    List<DataInout> selectByExample(DataInoutCriteria example) throws DataAccessException;

    DataInout selectByPrimaryKey(BigDecimal id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") DataInout record, @Param("example") DataInoutCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") DataInout record, @Param("example") DataInoutCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(DataInout record) throws DataAccessException;

    int updateByPrimaryKey(DataInout record) throws DataAccessException;
}
