package com.security.imageuploadserver;
import static util.LogRecord.logger;
import getVerify.verifyImage;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.imageHandle.OperateImage;
import com.imageHandle.SoundBinImage;
import com.open.test.JavaInvokeCpp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.*;
import java.text.*;

import com.crypto.MyServer;
import com.db.JdbcUtil;
import com.open.test.JavaInvokeCpp;
import com.imageHandle.ClearImageHelper;

import static util.LogRecord.logger;
import datamodels.AnlynasResult;
import datamodels.VerficationCodeInfo;
import datamodels.VerficationCodeResult;

public class SocketOperate extends Thread implements Runnable{
	private static Integer invoicePicNum = 0;//发票图片序号
	private Socket socket;
	BufferedReader in = null;
	PrintWriter out = null;
	MyServer S = null;
	private final String  invoiceDir = "D:\\image\\";
	private final String  cutImageDir = "H:\\Software\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\";
	
	public SocketOperate(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		String invoicePicFilename = invoiceDir+"invoice_image_";
		invoicePicFilename += invoicePicNum+".jpg";
		String invoiceDealFilename = invoiceDir+"invoice_deal_image_";
		invoiceDealFilename += invoicePicNum+".jpg";
		
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet resultSet = null;
		String invoiceAllInfo[] = new String[20];//发票信息包括发票代码、发票号码、发票密码
		String[] invoiceResult = null;//识别结果
		String username = null;
		String password = null;
		String time = null;
		
		//接收并解密图片
		try {
			logger.info("[INFO]========== Server ready ");
       	 	S = new MyServer();
       	 	socket.setSoTimeout(3600*1000);
       	 	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
       	 	out = new PrintWriter(socket.getOutputStream());
       	 	username = in.readLine();
       	 	password = in.readLine();
        	logger.info("The user is: "+username+",password: "+password);
        	
    	    S.GetM(in,out,invoicePicFilename);
    	    logger.info("[INFO]========== END ");
    	    
    	    File testDataDir = new File(invoicePicFilename); 
    	    ClearImageHelper.cleanImage(testDataDir,invoiceDealFilename);
    	    
    	    logger.info("收到图片"+invoicePicFilename+"开始识别该图片");
    	    
    	    //获取上传时间
    	    Date now = new Date();
    	    DateFormat d = DateFormat.getDateTimeInstance();
    	    time = d.format(now);
    	    
    	    String checkCode;//校验码

    	    invoiceAllInfo = doOCRInvoice(invoicePicFilename);//识别发票的函数
    	    //invoiceInfo = doOCRInvoice(invoiceDealFilename);
    	    logger.info("发票code为："+invoiceAllInfo[0]);
    	    logger.info("发票number为："+invoiceAllInfo[2]);
    	    logger.info("发票checkcode1为："+invoiceAllInfo[4]);
    	    logger.info("发票checkcode2为："+invoiceAllInfo[6]);
    	    
    	    invoiceAllInfo[8] = invoiceAllInfo[8].substring(0, 4) + invoiceAllInfo[8].substring(6, 8) + invoiceAllInfo[8].substring(10, 12);
    	    logger.info("发票date为："+invoiceAllInfo[8]);
    	    
    	    checkCode = invoiceAllInfo[4].substring(4);
    	    invoiceAllInfo[4] = checkCode + invoiceAllInfo[6];
    	    logger.info("校验码为："+invoiceAllInfo[4]);
    	  
    	    if(invoiceAllInfo != null){
    	    	invoiceResult = postCheckInvoice(invoiceAllInfo);//送往接口验证的函数
    	    }
		}catch (Exception e){
				logger.info("[INFO]========== working ok");
			}
		
		/**
		 * @author lujie
		 * compare information and return result to client
		 */
		
		/**
		 * @author lujie
		 * insert invoiceInfo into the mysql
		 */
		/*try {
			//调用工具类中的静态方法来获取连接
			connection = JdbcUtil.getConnection();
			String sql1 = "insert into ess_ugp_invoiceinfo (userid,code,number,checkcode,date,uploadtime,pass,operatetime) values(?,?,?,?,?,?,?,?)";
			pstmt1 = connection.prepareStatement(sql1);
			pstmt1.setString(1,username);
			pstmt1.setString(2,invoiceAllInfo[2]);
			pstmt1.setString(3,invoiceAllInfo[0]);
			pstmt1.setString(4,invoiceAllInfo[4]);
			pstmt1.setString(5,invoiceAllInfo[8]);
			pstmt1.setString(6,time);
			pstmt1.setInt(7,0);
			pstmt1.setString(8,time);
			int result1 = pstmt1.executeUpdate();
			if(result1 == 1)
				 logger.info("[INFO]========== insert invoiceInfo ok");
			else
				 logger.info("[INFO]========== insert invoiceInfo failed");

			/*String sql2 = "insert into ess_ugp_invoiceresult (userid,productname,specification,unit,account,unitprice,price,tax,taxmoney,TaxPayerNumber) values(?,?,?,?,?,?,?,?,?,?)";
			pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setString(1,username);
			pstmt2.setString(2,invoiceResult[0]);
			pstmt2.setString(3,invoiceResult[1]);
			pstmt2.setString(4,invoiceResult[2]);
			pstmt2.setString(5,invoiceResult[3]);
			pstmt2.setString(6,invoiceResult[4]);
			pstmt2.setString(7,invoiceResult[5]);
			pstmt2.setString(8,invoiceResult[6]);
			pstmt2.setString(9,invoiceResult[7]);
			pstmt2.setString(10,invoiceResult[8]);
			int result2 = pstmt2.executeUpdate();
			if(result2 == 1)
				 logger.info("[INFO]========== insert invoiceResult ok");
			else
				 logger.info("[INFO]========== insert invoiceResult failed");
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			JdbcUtil.releaseConn(connection, statement, resultSet);
		}*/
		logger.info("[INFO]========== finish verify");
    }

	
	/**
	 * @author xiuxian
	 * @param invoiceInfo 
	 * 将验证码发送给用户，并获取返回的输入值
	 * @throws Exception 
	 */
	private String[] postCheckInvoice(String[] invoiceAllInfo) throws Exception{
		verifyImage m = new verifyImage();
		String[] bArray = m.requestVercifationCodeHandler(invoiceAllInfo);
		String s = null;
		String[] ResultArray = null;
		String[] infosArray = null; 
		if(bArray != null)
		{
			//验证码字符流为bArray
			S.SendCAPTCHA(out,bArray[0]);
			S.SendCAPTCHA(out,bArray[1]);
			s = S.GetCAPTCHA(in);
			logger.info("[INFO]========== checkcode is: "+s);
			//验证发票真伪
			//
			//infosArray = m.commitHandler(invoiceAllInfo, s);
			m.commitHandler(invoiceAllInfo, s);
			//AnlynasResult result = new AnlynasResult();
			//ResultArray = result.handle(infosArray);
		}
		return ResultArray;
	}

	/**
	 * @param String invoicePicFilename
	 * @return String[] invoiceInfo
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private String[] doOCRInvoice(String invoicePicFilename) throws InterruptedException, IOException {
		String invoiceInfo[] = new String[20];
		
		//new SoundBinImage().releaseSound(invoicePicFilename,invoiceDir+"binPic"+invoicePicNum+".png",120);//png识别准确度更高
		
		//方案一：调用opencv库，动态定位发票信息
		System.loadLibrary("JavaInvokeCpp");
		JavaInvokeCpp mytest = new JavaInvokeCpp();
		//mytest.getInfoUsingOpencv(invoiceDir+"binPic"+invoicePicNum+".png");
		mytest.getInfoUsingOpencv(invoicePicFilename);
		
		//方案二：直接定位发票信息，用于测试
        //OperateImage oCode = new OperateImage(335,75,300,50);//发票代码
        //System.out.println("OperateImage 1 OK");
        /*try {
	        oCode.setSrcpath(invoicePicFilename);
	        oCode.setSubpath( invoiceDir+"code"+invoicePicNum+".jpg");
	        System.out.println("OperateImage 1.5 OK");
	        oCode.cut();
	        System.out.println("cut code 2 OK");
	        //OperateImage oNumber = new OperateImage(1550,120,200,30);//发票号码
	        OperateImage oNumber = new OperateImage(1350,70,200,50);//发票号码
	        oNumber.setSrcpath(invoicePicFilename);
	        oNumber.setSubpath(invoiceDir+"number"+invoicePicNum+".jpg");
	        System.out.println("OperateImage 2.5 OK");
	        oNumber.cut();
	        System.out.println("OperateImage 3 OK");
	        
	        OperateImage oCheckCode = new OperateImage(642,180,130,40); //发票校验码
	        oCheckCode.setSrcpath(invoicePicFilename);
	        oCheckCode.setSubpath(invoiceDir+"checkcode"+invoicePicNum+".jpg");
	        System.out.println("Checkcode ok");
	        oCheckCode.cut();
	        
	        OperateImage oDate = new OperateImage(1500,160,260,50); //开票日期
	        oDate.setSrcpath(invoicePicFilename);
	        oDate.setSubpath(invoiceDir+"date"+invoicePicNum+".jpg");
	        System.out.println("date ok");
	        oDate.cut();
        } catch (IOException e) {
			e.printStackTrace();
		} */
		//过滤背景色及图像二值化
        /*new SoundBinImage().releaseSound(invoiceDir+"code"+invoicePicNum+".jpg",invoiceDir+"bincode"+invoicePicNum+".png",120);
        new SoundBinImage().releaseSound(invoiceDir+"number"+invoicePicNum+".jpg",invoiceDir+"binnumber"+invoicePicNum+".png",150);//png识别准确度更高
        new SoundBinImage().releaseSound(invoiceDir+"checkcode"+invoicePicNum+".jpg",invoiceDir+"bincheckcode"+invoicePicNum+".png",200);//png识别准确度更高
        new SoundBinImage().releaseSound(invoiceDir+"date"+invoicePicNum+".jpg",invoiceDir+"bindate"+invoicePicNum+".png",220);//png识别准确度更高
        */
        new SoundBinImage().releaseSound(cutImageDir+"daima.jpg",invoiceDir+"bincode"+invoicePicNum+".png",120);
        new SoundBinImage().releaseSound(cutImageDir+"bianhao.jpg",invoiceDir+"binnumber"+invoicePicNum+".png",150);//png识别准确度更高
        new SoundBinImage().releaseSound(cutImageDir+"xiaoyanma4.jpg",invoiceDir+"bincheckcode1"+invoicePicNum+".png",215);//png识别准确度更高
        new SoundBinImage().releaseSound(cutImageDir+"xiaoyanma5.jpg",invoiceDir+"bincheckcode2"+invoicePicNum+".png",210);//png识别准确度更高
        new SoundBinImage().releaseSound(cutImageDir+"riqi.jpg",invoiceDir+"bindate"+invoicePicNum+".png",220);//png识别准确度更高
        
		//OCR识别
        String invoiceBinNumberFileName = invoiceDir+"binnumbertxt"+invoicePicNum;
        String invoiceBinCodeFileName = invoiceDir+"bincodetxt"+invoicePicNum;
        String invoiceBinCheckcodeFileName1 = invoiceDir+"bincheckcodetxt1"+invoicePicNum;
        String invoiceBinCheckcodeFileName2 = invoiceDir+"bincheckcodetxt2"+invoicePicNum;
        String invoiceBinDateFileName = invoiceDir+"bindatetxt"+invoicePicNum;
        
        Runtime run = Runtime.getRuntime();
        Process pr1 = run.exec("cmd.exe /c tesseract "+invoiceDir+"binnumber"+invoicePicNum+".png"+" "+invoiceBinNumberFileName+" -l eng");
        Process pr2 = run.exec("cmd.exe /c tesseract "+invoiceDir+"bincode"+invoicePicNum+".png"+" "+invoiceBinCodeFileName+" -l eng");
        Process pr3 = run.exec("cmd.exe /c tesseract "+invoiceDir+"bincheckcode1"+invoicePicNum+".png"+" "+invoiceBinCheckcodeFileName1+" -l eng");
        Process pr4 = run.exec("cmd.exe /c tesseract "+invoiceDir+"bincheckcode2"+invoicePicNum+".png"+" "+invoiceBinCheckcodeFileName2+" -l eng");
        Process pr5 = run.exec("cmd.exe /c tesseract "+invoiceDir+"bindate"+invoicePicNum+".png"+" "+invoiceBinDateFileName+" -l date");
        
        pr1.waitFor();//让调用线程阻塞，直到exec调用OCR完毕，否则会报错找不到txt文件
        pr2.waitFor();
        pr3.waitFor();
        pr4.waitFor();
        pr5.waitFor();
        String line;
        int i = 0;
        //注意这里生成txt是需要时间的，所有进程需要等待直到返回再继续执行，否则就会找不到文件
        FileReader frNum = new FileReader(invoiceBinNumberFileName+".txt");
        FileReader frCode = new FileReader(invoiceBinCodeFileName+".txt");
        FileReader frCheckcode1 = new FileReader(invoiceBinCheckcodeFileName1+".txt");
        FileReader frCheckcode2 = new FileReader(invoiceBinCheckcodeFileName2+".txt");
        FileReader frDate = new FileReader(invoiceBinDateFileName+".txt");
        BufferedReader brNum = new BufferedReader(frNum);
        while ((line = brNum.readLine()) != null)
        {
        	invoiceInfo[i++] = line;
        }
        BufferedReader brCode = new BufferedReader(frCode);
        //i--;
        while ((line = brCode.readLine()) != null)
        {
        	invoiceInfo[i++] = line;
        }
        
        BufferedReader brCheckcode1 = new BufferedReader(frCheckcode1);
        while ((line = brCheckcode1.readLine()) != null)
        {
        	invoiceInfo[i++] = line;
        }
        
        BufferedReader brCheckcode2 = new BufferedReader(frCheckcode2);
        while ((line = brCheckcode2.readLine()) != null)
        {
        	invoiceInfo[i++] = line;
        }
        
        BufferedReader brDate = new BufferedReader(frDate);
        while ((line = brDate.readLine()) != null)
        {
        	invoiceInfo[i++] = line;
        }
        
        brNum.close();
        brCode.close();
        brCheckcode1.close();
        brCheckcode2.close();
        brDate.close();
        frNum.close();
        frCode.close();
        frCheckcode1.close();
        frCheckcode2.close();
        frDate.close();
        logger.info("OCR识别结果："+invoiceInfo[0]+" "+invoiceInfo[2]+" "+invoiceInfo[4]+" "+invoiceInfo[6]+" "+invoiceInfo[8]);
		return invoiceInfo;
	}
	
	/**
	 * @param invoiceInfo
	 * 获取章上纳税人号码
	 */
	private void getIdentityNum(String invoicePicFilename) throws IOException {
		OperateImage oIdentityNum = new OperateImage(1310,940,240,40); //纳税人识别号
        oIdentityNum.setSrcpath(invoicePicFilename);
        oIdentityNum.setSubpath(invoiceDir+"identitynum"+invoicePicNum+".jpg");
        System.out.println("date ok");
        oIdentityNum.cut();
        
        new SoundBinImage().releaseSound(invoiceDir+"identitynum"+invoicePicNum+".jpg",invoiceDir+"binidentitynum"+invoicePicNum+".png",220);//png识别准确度更高
        
	}
	
	
}
