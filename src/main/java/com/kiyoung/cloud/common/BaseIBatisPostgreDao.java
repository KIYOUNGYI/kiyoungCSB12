package com.kiyoung.cloud.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ibatis.sqlmap.client.SqlMapClient;

public interface BaseIBatisPostgreDao {

	@Autowired
	public void initSqlMapClient(
			@Qualifier("postgreSQLDB") SqlMapClient sqlMapClientTemplate);
}