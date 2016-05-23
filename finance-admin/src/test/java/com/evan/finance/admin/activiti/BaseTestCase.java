package com.evan.finance.admin.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-activiti.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class BaseTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ProcessEngine processEngine;
	
	@Test
	public void test() {
		System.out.println("test");
		RepositoryService repositoryService = processEngine.getRepositoryService();  
		  
        DeploymentBuilder builder = repositoryService.createDeployment();  
        builder.addClasspathResource("HellowWorld.activiti.bpmn");
        builder.deploy();  
        // select * from `ACT_GE_PROPERTY`;这时这个表中会多条数据  
  
        RuntimeService runtimeService = processEngine.getRuntimeService();  
        runtimeService.startProcessInstanceByKey("myProcess");//启动流程，ID必须与你配置的一致  
  
        System.out.println("ok......");  
	}

}