package com.security.imageuploadserver;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

public class SocketThread extends Thread{
	private ServerSocket serverSocket = null;
	
	public SocketThread(ServerSocket serverSocket) {
		try {
			if(null == serverSocket)
			{
				this.serverSocket = new ServerSocket(5699);
				System.out.println("socket start");
			}
		}catch (Exception e) {
			System.out.println("SocketThread创建socket服务出错");
			e.printStackTrace();
		}
	}
	
	//监听线程
	public void run(){
		System.out.println("欢迎使用财务报销系统，服务器启动");
		
		while(!this.isInterrupted()){ 
			try {
				Socket socket = serverSocket.accept();
				
				if(null != socket && !socket.isClosed())
				{
					new SocketOperate(socket).start();
				}
				socket.setSoTimeout(30000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeSocketServer(){
		try {
			if(null != serverSocket && !serverSocket.isClosed())
			{
				serverSocket.close();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

}
