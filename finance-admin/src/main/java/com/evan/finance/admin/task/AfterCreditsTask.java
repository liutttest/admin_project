package com.evan.finance.admin.task;

public interface AfterCreditsTask {
	/**
	 * 获取银行准入验证结果
	 */
	public void queryBankForInterestOverdueInfo();
	public void queryBankForInterestWarning();
}
