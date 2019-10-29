package com.kiyoung.cloud.storage.dao;

import java.util.List;
import java.util.Map;

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
	
	public Map<String,Object> getBizAndUserConnId(String portalId) throws Exception
	{
		return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("getBizAndUserConnId",portalId);
	}
	
	public Map<String,Object> getAccountByUserConnectID(String userConnectID) throws Exception
	{
		return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("getAccountByUserConnectID",userConnectID);
	}
}
