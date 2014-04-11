package com.run.uguard.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * 
 * @author zhouzefeng
 * @use    用于加载web.xml配置参数
 */

public class WebAppParameter extends HttpServlet{
	public static int socketPort = 0;
	public static int serverBlock = 0;
	public static String sysPath = null;
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			socketPort = Integer.parseInt(config.getInitParameter("Port"));
			serverBlock = Integer.parseInt(config.getInitParameter("Block"));
			sysPath = config.getServletContext().getRealPath("");
			if(UGuardSystem.init()){
				System.out.println("**----------System initialization!----------**");
			}else{
				System.out.println("**----------Failed to initialize!----------**");
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
