package com.security.imageuploadserver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 将socketListener随tomcat启动
 * @author lujie
 *
 */
public class SocketListener implements ServletContextListener{
	//socket server线程
	private SocketThread socketThread;

    /**
     *销毁当Servlet容器终止Web应用时调用该方法
     */
    public void contextDestroyed(ServletContextEvent arg0) {
       if (null != socketThread && !socketThread.isInterrupted()) {
           // 关闭线程
           socketThread.closeSocketServer();
           // 中断线程
           socketThread.interrupt();
       }
    }

    /**
     * 初始化当Servlet容器启动Web应用时调用该方法
     */
    public void contextInitialized(ServletContextEvent arg0) {
       if (null == socketThread) {
    	   //新建线程类
           socketThread = new SocketThread(null); //此处调用SocketThread class
           //启动线程
           socketThread.start();
       }

    }
}
