package com.evan.finance.admin.activi.nodes;

import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FaTemplate;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FaTemplateService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.FailMsgType;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.finance.admin.utils.WorkFlowUtils;
@Service
public class WorkFlowSendFailMsgService implements JavaDelegate{

	Logger logger = LoggerFactory.getLogger(WorkFlowSendFailMsgService.class);
	
	@Autowired
	private MonitorTools monitorTools;
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	@Autowired
	private FaTempSceneService faTempSceneService;
	
	@Autowired
	private FaTemplateService faTemplateService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private TaskService taskService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String taskDefKey = (String)execution.getVariable("taskDefKey");
		//判断业务失败是从哪个场景过来的
		String scene = "";
		if (FailMsgType.ACCESS.equals(taskDefKey)) {
			scene = ConstantTool.SCENE.ZRSB;
		}else if(FailMsgType.QUOTA.equals(taskDefKey)){
			scene = ConstantTool.SCENE.EDSQSB;
		}else{
			scene = ConstantTool.SCENE.XCKHSB;
		}
		//查询失败场景
		FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(scene);
		String busId = execution.getProcessBusinessKey();
		logger.error("senderrormsg---------"+faTempScene.getSendType());
		//1为自动发送通知
		if ("1".equals(faTempScene.getSendType())) {
			//自动发送通知 判断发送方式
			//自动 发送通知
			//请求成功
			
			String isSendEmail = "";
			String emailTitle = "";
			String emailContent = "";
			
			// 判断是否发送邮件
			if ("1".equals(faTempScene.getIsSendEmail()))
			{
				// 查询邮件模版
				FaTemplate emailTemplate = faTemplateService.findDefaultBySceneId(faTempScene.getTsId(), ConstantTool.TempleteType.EMAIL);
				if (emailTemplate != null)
				{
					isSendEmail = "on";
					emailTitle = emailTemplate.getTitle();
					emailContent = emailTemplate.getContent();
				}
			}
			
			String isSendSms = "";
			String smsContent = "";
			
			// 判断是否发送短信
			if ("1".equals(faTempScene.getIsSendSms()))
			{
				// 查询短信模版
				FaTemplate smsTemplate = faTemplateService.findDefaultBySceneId(faTempScene.getTsId(), ConstantTool.TempleteType.SMS);
				if (smsTemplate != null)
				{
					isSendSms = "on";
					smsContent = smsTemplate.getContent();
				}
			}
			
			String isSendMessage = "";
			String messageTitle = "";
			String messageContent = "";
			
			// 判断是否发送短信
			if ("1".equals(faTempScene.getIsSendMessage()))
			{
				// 查询短信模版
				FaTemplate messageTemplate = faTemplateService.findDefaultBySceneId(faTempScene.getTsId(), ConstantTool.TempleteType.MESSAGE);
				if (messageTemplate != null)
				{
					isSendMessage = "on";
					messageTitle = messageTemplate.getTitle();
					messageContent = messageTemplate.getContent();
				}
			}
			
			// 发送
			Map<String, Object> map = notificationService.doSendNotification(busId, isSendEmail, emailTitle, emailContent, false, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
			String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, map);
			monitorTools.infoBusinessAccess("发送准入验证失败通知结果", busId, contentString);
			execution.setVariable(WorkFlowUtils.sendFailMsg, true);
			//修改数据库状态 为驳回小状态09
			fwBusinessSxdService.updateState(busId,ApplyState.REJECT,0L);
		}else{
			//0手动发送通知
			execution.setVariable(WorkFlowUtils.sendFailMsg, false);
			execution.setVariable("scene", scene);
			//修改数据库状态 为驳回小状态0901
			//fwBusinessSxdService.updateState(busId,ApplyState.REJECT,0L);
			//taskService.setVariable(execution.getId(), "scene", scene);
		}
		
	}

}
