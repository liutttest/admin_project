package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.SysUser;
import com.evan.common.user.service.FaFalseUsersService;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.utils.FalseUserCacheLoader;
import com.evan.finance.admin.utils.RoleCacheLoader;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.security.SecurityTokenHolder;
import com.evan.jaron.plugins.security.UserNotAvailableException;
import com.evan.jaron.plugins.security.UserNotFoundException;
import com.evan.jaron.plugins.security.WrongPasswordException;
import com.evan.jaron.plugins.security.domain.IPermission;
import com.evan.jaron.plugins.security.service.ISecurityService;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.FileUpload;
import com.evan.nd.common_file.FileUpload.UploadResults;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;

@RequestMapping("/")
@Controller
public class MainController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private ISecurityService securityService;
	@Autowired
	private FaFalseUsersService faFalseUsersService;
	@Autowired
    private FileUpload fileUpload;
	@Autowired
	private FwFilesService fwFilesService;

	@RequestMapping(value="main",method = { GET })
	public String main() {
		return "main/index";
	}
	
	@RequestMapping(value = "/login", method = { GET })
	public String login() {
		return "main/login";
	}
	
	/**
	 * 跳转统一的错误提示页面(luy)
	 * 
	 * @param request
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/error", method = { GET, POST })
	public String toError(HttpServletRequest request, @RequestParam Map<?, ?> map, Model model) {
		return "main/error";
	}

	@RequestMapping(method = { GET })
	public String loginnew() {
		if (RequestUtils.getLoginedUser() == null) {
			return "main/login";
		} else {
			return "redirect:/main";
		}

	}
	
	/**
	 * 用户登录(liutt)
	 * 
	 * @param response
	 * @param userName
	 * @param password
	 */
	@RequestMapping(value = "/doLogin", method = POST)
	@ResponseBody
	public String doLogin(HttpServletResponse response,
			@RequestParam("username") String userName,
			@RequestParam("password") String password,
			@RequestParam("type") String type) {
		
		// 错误信息
		String errMsg = "";
		
		try {
			securityService.login(response, new String[]{userName, type}, password, false);
		} catch (UserNotFoundException e) {
//			errMsg = getMessage("security.errMsg.userNotFound");
			errMsg = getMessage("security.errMsg.nameOrPwdError"); // 提示“用户名或者密码错误！”
		} catch (UserNotAvailableException e) {
			errMsg = getMessage("security.errMsg.userNotAvailable");
		} catch (WrongPasswordException e) {
			errMsg = getMessage("security.errMsg.nameOrPwdError"); // 提示“用户名或者密码错误！”
		} finally {
			if (!errMsg.equals("")) {
				logger.error(errMsg);
			}
		}
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (errMsg.equals("")) {
			Collection<IPermission> list = SecurityTokenHolder.getSecurityToken().getPermissions();
			// TODO 每次登陆成功后缓存角色列表，供工作流使用
			List<String> roles = new ArrayList<String>();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				IPermission iPermission = (IPermission) iterator.next();
				if ("00".equals(iPermission.getGroupCode()) && iPermission.getCode().indexOf("func")==0) {
					roles.add(iPermission.getUrl());
				}
			}
			
			//查询当前用户的假用户
			//假用户缓存
			List<String> listUser = faFalseUsersService.getFalseUsersByUserId(SecurityTokenHolder.getSecurityToken().getUser().getUserId());
			FalseUserCacheLoader.getInstance().setRoles(userName, listUser);
			/*roles.add(WorkFlowUtils.role_fcd_first_verify);
			roles.add(WorkFlowUtils.role_fcd_review_verify);
			roles.add(WorkFlowUtils.role_fcd_bank_verify);
			roles.add(WorkFlowUtils.role_fcd_scene_verify);
			roles.add(WorkFlowUtils.role_sxd_first_verify);
			roles.add(WorkFlowUtils.role_sxd_review_verify);
			roles.add(WorkFlowUtils.role_sxd_bank_verify);
			roles.add(WorkFlowUtils.role_sxd_scene_verify);
			roles.add(WorkFlowUtils.role_loan_bank_handle);
			roles.add(WorkFlowUtils.role_repayment_bank_handle);
			roles.add(WorkFlowUtils.role_quickloan_confirm);
			roles.add(WorkFlowUtils.role_quickloan_web);
			roles.add(WorkFlowUtils.role_quickloan_noweb);
			roles.add(WorkFlowUtils.role_negotiate_processing);
			roles.add(WorkFlowUtils.role_advice_processing);*/
			RoleCacheLoader.getInstance().setRoles(userName, roles);
			
			// 未出现异常，登录成功
			return JsonUtils.toJson(RequestUtils.successResult(map));
		}
		else {
			// 登录失败,返回登录状态和错误信息
			return JsonUtils.toJson(RequestUtils.failResult(errMsg));
		}
	}
	
	@RequestMapping(value = "/logout", method = { GET, POST })
	public String logout(HttpServletResponse response) {
		SecurityTokenHolder.cleanSecurityToken(response);

		return "main/login";
	}
	
	/**
     * 上传文件(lixj)
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = POST)
    @ResponseBody
    public String uploadFile(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        FwFiles fwFiles = new FwFiles();
        SysUser sysUser = (SysUser) SecurityTokenHolder.getSecurityToken().getUser();
        Long comId = ((SysUser) SecurityTokenHolder.getSecurityToken().getUser()).getComId();
        fwFiles.setCreateUserid(MapUtils.getLong(map, "createUserid", sysUser.getUserId()));
        fwFiles.setComId(comId);

        UploadResults result = fileUpload.uploadFile(fwFiles, request);
        if (result != UploadResults.UPLOAD_SUCCESS) {
            String errMsg = null;
            switch (result) {
                case UPLOAD_FAIL: // 上传失败
                case UPLOAD_NOT_FORM_DATA: // 不是通过表单的方式提交
                case FAILED_TO_SAVE: // 插入数据库时出错
                    errMsg = getMessage("fileupload.errMsg.fail");
                    break;
                case FILE_FORMAT_ERROR: // 文件格式错误
                    errMsg = getMessage("fileupload.errMsg.formatError");
                    break;
                case IS_NOT_IMAGE: // 不是图片
                    errMsg = getMessage("fileupload.errMsg.notImage");
                    break;
                case IMAGE_RESOLUTION_ERROR: // 图片分辨率错误
                    errMsg = getMessage("fileupload.errMsg.resolutionError");
                    break;
                case FILE_SIZE_LIMIT_EXCEEDED: // 单个文件大小超过最大值
                    errMsg = getMessage("fileupload.errMsg.fileSizeError");
                    break;
                case SIZE_LIMIT_EXCEEDED:  // 总的文件大小超过最大值
                    errMsg = getMessage("fileupload.errMsg.sizeError");
                    break;
                default:
                    break;
            }

            // 上传失败
            return JsonUtils.toJson(RequestUtils.failResult(errMsg));
        } else {
            // 上传成功
            return JsonUtils.toJson(RequestUtils.successResult(fwFiles));
        }
    }
    
    /**
     * 删除文件（单条）
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteFile", method = {GET, POST})
    @ResponseBody
    public String deleteFile(@RequestParam() Map<String, Object> map) {
        String fileId = MapUtils.getString(map, "fileId");// 文件Id
        int result = fwFilesService.removeFwFiles(fileId);
        if (result == 1) {
            return JsonUtils.toJson(RequestUtils.successResult(null));
        } else {
            return JsonUtils.toJson(RequestUtils.failResult(""));
        }
    }
}
