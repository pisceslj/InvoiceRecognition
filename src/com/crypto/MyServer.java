package com.crypto;

import java.io.BufferedReader;
import java.io.FileOutputStream;  
import java.io.PrintWriter;

import org.bouncycastle.util.encoders.Hex;  

public class MyServer {  
	public int ClientStatus = 0;
	public void SendCAPTCHA(PrintWriter out,String str) throws Exception
	{
		System.out.println(str);
		SendToClient(out,str);
	}
	public String GetCAPTCHA(BufferedReader in)throws Exception
	{
		String str = in.readLine();
		return str;
	}
	
	public void SendMessage(PrintWriter out,String str)throws Exception
	{
		System.out.println(str);
		SendToClient(out,str);
	}
	
	public void  GetM(BufferedReader in,PrintWriter out,String fileName) throws Exception
	{
		String one = nego(in,out);
		String two = nego(in,out);
		FileOutputStream file = new FileOutputStream(fileName, false);	
		while(true)
		{
	
			String M1 = in.readLine();
			if(M1.equals("END") == true) 
			{
			System.out.println("M1: "+M1);
			break;
			
			}

		    System.out.println("M1: "+M1);
			String M2 = in.readLine();
			String M3 = in.readLine();
			String M4 = in.readLine();
			String M5 = in.readLine();
		
			Message M = new Message();
			M.buffer = Hex.decode(M5);
			M.C1_length =  Integer.parseInt(M1);
			M.C2_length =  Integer.parseInt(M2);
			M.C3_length =  Integer.parseInt(M3);
			M.ms_length =  Integer.parseInt(M4);

			SM02 sm02 = new SM02();
			SM02KeyPair keyPair = sm02.generateKeyPair(one+two);
			byte[] text = sm02.decrypt(M.buffer, keyPair.getPrivateKey(),M.C1_length, M.C2_length,M.C3_length,M.ms_length);
			//SM02.printHexString(text);
			//System.out.println(Integer.parseInt(M7)-Integer.parseInt(M6));
			file.write(text, 0 ,text.length);
		}
		file.close();
	}
    public String nego(BufferedReader in,PrintWriter out) throws Exception {  
        String rstr = "83A2C9C8B96E5AF70BD480B472409A9A327257F1EBB73F5B073354B248668563";
        String dstr = "6FCBA2EF9AE0AB902BC3BDE3FF915D44BA4CC78F88E2F8E7F8996D3B8CCEEDEE";
        
        Negotiator A = new Negotiator();
        A.Set_Pam(0, rstr,dstr);
        A.Step1_5();
        ExChange_Point(out,A,in);
        A.Step6_8(); 	
        Exchange_S(out,A,in); 
        return A.KKK;
    } 
    static void ExChange_Point(PrintWriter out,Negotiator N ,BufferedReader in) throws Exception
    {
    	String[] str = new String[2];
    	SendToClient(out,N.RA.getXCoord().toString());
    	str[0] = in.readLine(); 
     	SendToClient(out,N.RA.getYCoord().toString());
     	str[1] = in.readLine();
     	N.ECPointEncode(str[0], str[1]);
    }
    static void Exchange_S(PrintWriter out,Negotiator N ,BufferedReader in) throws Exception
    {
    	int Flag = 0;
    	if(N.Status == 0)
    	{
    		SendToClient(out,N.StepCheck9_10(1));
    		String str = in.readLine();
    		if(str.equals(N.StepCheck9_10(0)))
    			Flag = 1;
    	}
    	else 
    	{
    		SendToClient(out,N.StepCheck9_10(0));
    		String str = in.readLine();
    		if(str.equals(N.StepCheck9_10(1)))
    			Flag = 1;
    	}
    	if(Flag == 1)
    		System.out.println("Successful negotiation");
    	else 
    		System.out.println("Failure negotiation");
    }
    static void SendToClient(PrintWriter out,String str)
    {
        out.println(str);  
        out.flush();  
    }
}  