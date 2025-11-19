package org.siva.smartcontactmanager.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class Helper {
	
	public void removeSessionMessage() {
		HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		session.removeAttribute("successMsg");
		session.removeAttribute("failMsg");
		session.removeAttribute("warningMsg");
		session.removeAttribute("infoMsg");
	}

}
