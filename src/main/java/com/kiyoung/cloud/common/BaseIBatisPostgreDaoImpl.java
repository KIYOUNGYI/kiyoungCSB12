package com.kiyoung.cloud.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;

@Repository
public class BaseIBatisPostgreDaoImpl extends SqlMapClientDaoSupport implements BaseIBatisPostgreDao {
	
	@Autowired
	public void initSqlMapClient(@Qualifier("postgreSQLDB") SqlMapClient sqlMapClientTemplate) {
		super.setSqlMapClient(sqlMapClientTemplate);
	}

}