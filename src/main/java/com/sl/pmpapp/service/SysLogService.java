package com.sl.pmpapp.service;

import java.util.List;
import java.util.Map;

import com.sl.pmpapp.entity.SysLog;
/**
 * 系统日志
 * @author msw
 * @data
 */
public interface SysLogService {
	SysLog queryObject(Long id);
	
	List<SysLog> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysLog sysLog);
	
	void update(SysLog sysLog);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
