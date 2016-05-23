package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.evan.jaron.core.AppContext;

@RequestMapping("/config")
@Controller
public class ConfigReload {
	
	private static Log log = LogFactory.getLog(AppContext.class);
	private static final String CONFIGUATION_FILE = "/config.properties";

	@ResponseBody
	@RequestMapping(value="/reload", method = {GET, POST})
	public String reload(){
		try {
            InputStream in = AppContext.class.getResourceAsStream(CONFIGUATION_FILE);
            Properties p = new Properties(); 
            p.load(in);

            for (Iterator<Object> iterator = p.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                AppContext.getInstance().put(key, p.getProperty(key)); 
            }
            
        } catch (Exception e) {
            log.warn("Can not init properties, cause by reading configuation.properties failed");
            return "{state:0}";
        }
		return "{state:1}";
	}
}
