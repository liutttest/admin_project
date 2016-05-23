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
public class FalseUserCacheLoader {
	private static Map<String, List<String>> userNameMap = null;

	private static class FalseUserCacheLoaderHoder {
		// 静态初始化器，由JVM来保证线程安全
		private static FalseUserCacheLoader instance = new FalseUserCacheLoader();
	}

	// 私有化构造方法
	private FalseUserCacheLoader() {
		userNameMap = new HashMap<String, List<String>>();
	}

	public static FalseUserCacheLoader getInstance() {
		return FalseUserCacheLoaderHoder.instance;
	}

	public List<String> getRoles(String userKey) {
		return userNameMap.get(userKey);
	}

	public List<String> setRoles(String userKey, List<String> roles) {
		userNameMap.put(userKey, roles);
		return userNameMap.get(userKey);
	}
	
	public boolean haveRole(String userKey, String role) {
		List<String> roles = userNameMap.get(userKey);
		for(String r : roles) {
			if (r.equals(role)) {
				return true;
			}
		}
		return false;
	}
}
