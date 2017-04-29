package com.crypto;

import java.math.BigInteger;
import java.util.Arrays;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

public class Negotiator{
  	public static String ENTLA = "0090";
	public static String IDA = "414C494345313233405941484F4F2E434F4D";
  	public static String ENTLB = "0088";
	public static String IDB = "42494C4C343536405941484F4F2E434F4D";
	public static String p = "8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3";
	public static String a = "787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498";
	public static String b = "63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A";
	public static String xG = "421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D";
	public static String yG = "0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2";
	public static ECPoint G;
	public int h = 1;
	public static String n = "8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7";
	public String d;
	public static String xA = "3099093BF3C137D8FCBBCDF4A2AE50F3B0F216C3122D79425FE03A45DBFE1655";
	public static String yA = "3DF79E8DAC1CF0ECBAA2F2B49D51A4B387F2EFAF482339086A27A8E05BAED98B";
	public static String xB = "245493D446C38D8CC0F118374690E7DF633A8A4BFB3329B5ECE604B2B4F37F43";
	public static String yB = "53C0869F4B9E17773DE68FEC45E14904E0DEA45BF6CECF9918C85EA047C60A4C";
	public String r;
    public byte[] Buffer;
    public int Status = 0;
    public String Z= "";
    public String ZA = "";
    public String ZB = "";
    public BigInteger x_int;
    public BigInteger x1_int;
    public BigInteger x2_int;
    public BigInteger t;
    ECPoint R;
    ECPoint RA;
    ECPoint RB;
    ECPoint U_V;
    public String KKK = ""; 
	//@SuppressWarnings("deprecation")
	public Negotiator()   
    {
			

    }
	public static String Check(String s)
	{
		int index = 0;
		index = 64 - s.length();
		for(int i = 0;i < index;i++)
		{
			s = "0" + s;
		}
		return s;
	}
	public static String My_HashZ(String s[],int index)
	{
		String StrTemp = "";
	    for(int i = 0;i < index;i++){
	    	StrTemp += s[i];
	     }
	    return H_256(StrTemp);
	}		
	public static String My_Hash(String s[],int index)
	{
		String StrTemp = "";
	    for(int i = 0;i < index;i++){	
	    	StrTemp += Check(s[i]);
	     }
	    
	    return H_256(StrTemp);
	}
	public static String H_256(String s)
	{
		String Temp = s;
		byte[] md = new byte[32];  
	    byte[] msg1 =new BigInteger(Temp, 16).toByteArray(); 
	    SM3Digest sm3 = new SM3Digest();  
	    sm3.update(msg1, 0, msg1.length);  
	    sm3.doFinal(md, 0);  
	    String re = new String(Hex.encode(md)); 
	    //System.out.println(re.toUpperCase());
	    return re;
	}
	public static ECPoint  ECMutiply(ECPoint ECP,String integer)
	{
	    BigInteger k =new BigInteger(integer, 16);
	    ECPoint ECP_k = ECP.multiply(k).normalize();
	    byte[] Buffer = ECP_k.getXCoord().getEncoded();
	    //SM02.printHexString(Buffer);
	    Buffer = ECP_k.getYCoord().getEncoded();
	    //SM02.printHexString(Buffer);
	    return ECP_k;
	    
	}
	public static BigInteger GetInt(BigInteger x,int w)
	{
	    
    	BigInteger one = new BigInteger("1",16);
    	BigInteger one_127 = one.shiftLeft(w);
    	BigInteger one_127_sub1 = one_127.subtract(one); 
        BigInteger x_int =(x.and(one_127_sub1)).add(one_127);
    	String p = new String(Hex.encode(x_int.toByteArray()));
    	//System.out.println(p);
    	return x_int;
	}
	public static ECPoint GetECPoint(BigInteger x,BigInteger y)
	{
		ECCurve.Fp curve = new ECCurve.Fp(new BigInteger(p,16),new BigInteger(a,16),new BigInteger(b,16));
		return curve.createPoint(x,y);
	}

	public final static byte[] KDF(byte[] Z, int kLen) {
	    int ct = 1;
	    int n = (kLen + 32) / 32;
	    byte[] buffer = new byte[kLen + 32];
	 
	    SM3Digest sm3Digest = new SM3Digest();
	    for (int i = 0; i < n; i++) {
	        sm3Digest.reset();
	        sm3Digest.update(Z, 0, Z.length);
	        sm3Digest.update(intToByteArray(ct++), 0, 4);
	        sm3Digest.doFinal(buffer, 32 * i);
	    }
	    return Arrays.copyOf(buffer, kLen);
	}
	private final static byte[] intToByteArray(int k) {
	    return new byte[] { (byte) ((k >> 24) & 0xff),
	            (byte) ((k >> 16) & 0xff), (byte) ((k >> 8) & 0xff),
	            (byte) (k & 0xff) };
	}
	public void Step1_5()
	{
		String TempZ = ENTLA+IDA+a+b+xG+yG+xA+yA;    //hash ->Z
		//System.out.print("ZA: ");
		ZA = H_256(TempZ);
		
		TempZ = ENTLB+IDB+a+b+xG+yG+xB+yB;    //hash ->Z
		//System.out.print("ZB: ");
		ZB = H_256(TempZ);
		
		SM02 sm02 = new SM02();
		r = sm02.random(new BigInteger(n,16).subtract(new BigInteger("1"))).toString();
		//System.out.print("(x,y): ");
		String rstr = r.toString();
		R = ECMutiply(sm02.G,rstr);     //R=[r]G
		if(Status == 0)RA = R;
		else RB = R;
		if(Status == 0)
		{
			//System.out.print("x1_int:");
			x1_int = GetInt(RA.getAffineXCoord().toBigInteger(),127); //x1 ->int
			t = (x1_int.multiply(new BigInteger(r,16)).add(new BigInteger(d,16))).mod(new BigInteger(n,16));
			String tstr = new String(Hex.encode(t.toByteArray()));           //t
			//System.out.println("t:"+tstr);
		}
		else 
		{
			//System.out.print("x2_int:");
			x2_int = GetInt(RB.getAffineXCoord().toBigInteger(),127); //x1 ->int
			t = (x2_int.multiply(new BigInteger(r,16)).add(new BigInteger(d,16))).mod(new BigInteger(n,16));
			String tstr = new String(Hex.encode(t.toByteArray()));           //t
			//System.out.println("t:"+tstr);
		}
		
	}
	public void Step6_8()
	{

		if(Status != 0)
		{
			//System.out.print("x1_int:");
			x1_int = GetInt(RA.getAffineXCoord().toBigInteger(),127); //x1 ->int
		}
		else 
		{
			//System.out.print("x2_int:");
			x2_int = GetInt(RB.getAffineXCoord().toBigInteger(),127); //x1 ->int
		}
		
		//System.out.print("[x_int]R:");
		ECPoint x_int_R; 
		if(Status == 0) x_int_R= RB.multiply(x2_int).normalize();
		else  x_int_R= RA.multiply(x1_int).normalize();
	    Buffer = x_int_R.getEncoded(false);
	    //SM02.printHexString(Buffer);                //[x1_int]R
		
		//System.out.print("P+[x_int]R:");
		ECPoint tempECP;
		if (Status == 0)tempECP = GetECPoint(new BigInteger(xB,16),new BigInteger(yB,16));
		else tempECP = GetECPoint(new BigInteger(xA,16),new BigInteger(yA,16));
	    ECPoint x_int_R_P =  tempECP.add(x_int_R);
	    Buffer = x_int_R_P.add(x_int_R).getEncoded(false);
	    //SM02.printHexString(Buffer);                //P+[x_int]R      /////!!
	    
		//System.out.print("U_V:");
		U_V = x_int_R_P.multiply(t).normalize(); 
	    Buffer = U_V.getEncoded(false);
	    //SM02.printHexString(Buffer);                //V = [h.t](P+[x_int]R)
		
		//System.out.print("ux||uy||ZA||ZB:");
	    String tempS = U_V.getXCoord().toString() + U_V.getYCoord().toString() + ZA + ZB;
		//System.out.println(tempS);
		System.out.print("KB:");
		byte[] Karray =new BigInteger(tempS, 16).toByteArray(); 
	    byte[] tempbyte = KDF(Karray,16);
		String Kstr = new String(Hex.encode(tempbyte));
		System.out.println(Kstr);                          //K
		KKK = Kstr;
	}
	public String StepCheck9_10(int s)
	{
		String[] StrArray = {U_V.getXCoord().toString(),ZA,ZB,RA.getXCoord().toString(),RA.getYCoord().toString(),RB.getXCoord().toString(),RB.getYCoord().toString()};
		String SCheck_T = My_Hash(StrArray,7);
		if(s == 0)StrArray[0] = "02";
		else StrArray[0] = "03";
		StrArray[1] = U_V.getYCoord().toString();
		StrArray[2] = SCheck_T;                       
		String S = My_HashZ(StrArray,3);
		System.out.print("S:");
		System.out.println(S);
		return S;
	}
	public void ECPointEncode(String ECx , String ECy)
	{
		if(Status == 0)
			RB = GetECPoint(new BigInteger(ECx,16),new BigInteger(ECy,16));
		else 
			RA = GetECPoint(new BigInteger(ECx,16),new BigInteger(ECy,16));
	}
	public void Set_Pam(int s, String rstr,String dstr)
	{
		//r = rstr;
		d = dstr;
		Status = s;
	}
}
