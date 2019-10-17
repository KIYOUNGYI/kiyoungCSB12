package com.kiyoung.cloud.storage.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kiyoung.cloud.common.BaseIBatisPostgreDaoImpl;

@Repository
public class PostgreSQLDaoImpl extends BaseIBatisPostgreDaoImpl 
{
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	public String getAccountByEmpNo(String email) throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("getUserNoByEmail", email);
	}
	
	public List getUserSessionInfoByPortalId(String portalId) throws Exception
	{
		return getSqlMapClientTemplate().queryForList("getUserSessionInfoByPortalId",portalId);
	}
}
