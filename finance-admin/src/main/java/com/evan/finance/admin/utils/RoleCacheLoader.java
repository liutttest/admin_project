package com.evan.finance.admin.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色缓存
 * 
 * @author stone
 *
 */
public class RoleCacheLoader {
	private static Map<String, List<String>> roleMap = null;

	private static class RoleCacheLoaderHolder {
		// 静态初始化器，由JVM来保证线程安全
		private static RoleCacheLoader instance = new RoleCacheLoader();
	}

	// 私有化构造方法
	private RoleCacheLoader() {
		roleMap = new HashMap<String, List<String>>();
	}

	public static RoleCacheLoader getInstance() {
		return RoleCacheLoaderHolder.instance;
	}

	public List<String> getRoles(String userKey) {
		return roleMap.get(userKey);
	}

	public List<String> setRoles(String userKey, List<String> roles) {
		roleMap.put(userKey, roles);
		return roleMap.get(userKey);
	}
	
	public boolean haveRole(String userKey, String role) {
		List<String> roles = roleMap.get(userKey);
		for(String r : roles) {
			if (r.equals(role)) {
				return true;
			}
		}
		return false;
	}
}
