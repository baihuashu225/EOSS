<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.jelly.eoss.util.Const"%>
<%
	Object obj = request.getSession().getAttribute(Const.LOGIN_SESSION_KEY);
	if(obj == null){
		request.getRequestDispatcher("/toLogin"").forward(request, response);
	}else{
		request.getRequestDispatcher("/layout/main"").forward(request, response);
	}
%>