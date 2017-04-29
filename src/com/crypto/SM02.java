package com.crypto;
import java.math.BigInteger;



import java.security.SecureRandom;
import java.util.Arrays;


import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.digests.ShortenedDigest;


import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.KDF1BytesGenerator;
import org.bouncycastle.crypto.params.ISO18033KDFParameters;


public class SM02 {


private static BigInteger n = new BigInteger("8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7", 16);
private static BigInteger p = new BigInteger("8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3", 16);
private static BigInteger a = new BigInteger("787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498", 16);
private static BigInteger b = new BigInteger("63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A", 16);
private static BigInteger gx = new BigInteger("421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D", 16);
private static BigInteger gy = new BigInteger("0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2", 16);
/*
private static BigInteger n = new BigInteger("FFFFFFFE" + "FFFFFFFF"
+ "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409"
+ "39D54123", 16);
private static BigInteger p = new BigInteger("FFFFFFFE" + "FFFFFFFF"
+ "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF"
+ "FFFFFFFF", 16);
private static BigInteger a = new BigInteger("FFFFFFFE" + "FFFFFFFF"
+ "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF"
+ "FFFFFFFC", 16);
private static BigInteger b = new BigInteger("28E9FA9E" + "9D9F5E34"
+ "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41"
+ "4D940E93", 16);
private static BigInteger gx = new BigInteger("32C4AE2C" + "1F198119"
+ "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589"
+ "334C74C7", 16);
private static BigInteger gy = new BigInteger("BC3736A2" + "F4F6779C"
+ "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5"
+ "2139F0A0", 16);*/


private static SecureRandom random = new SecureRandom();
public ECCurve.Fp curve;
public ECPoint G;


public static void printHexString(byte[] b) {
for (int i = 0; i < b.length; i++) {
String hex = Integer.toHexString(b[i] & 0xFF);
if (hex.length() == 1) {
hex = '0' + hex;
}
System.out.print(hex.toUpperCase());
}
System.out.println();
}


public BigInteger random(BigInteger max) {


BigInteger r = new BigInteger(256, random);
// int count = 1;


while (r.compareTo(max) >= 0) {
r = new BigInteger(128, random);
// count++;
}


// System.out.println("count: " + count);
return r;
}


private boolean allZero(byte[] buffer) {
for (int i = 0; i < buffer.length; i++) {
if (buffer[i] != 0)
return false;
}
return true;
}


public byte[] encrypt(String input, ECPoint publicKey) {

//System.out.println(input.length());
byte[] inputBuffer = input.getBytes();
printHexString(inputBuffer);


/* 1 娴溠呮晸闂呭繑婧�閺佺櫤閿涘鐏炵偘绨琜1, n-1] */
//BigInteger k = random(n);
BigInteger k = new BigInteger("4C62EEFD6ECFC2B95B92FD6C3D9575148AFA17425546D49018E5388D49DD7B4F",16);
System.out.print("k: ");
printHexString(k.toByteArray());


/* 2 鐠侊紕鐣诲顓炴妇閺囪尙鍤庨悙绗�1 = [k]G = (x1, y1) */
ECPoint C1 = G.multiply(k);
byte[] C1Buffer = C1.getEncoded(false);
System.out.print("C1: ");
printHexString(C1Buffer);


/*
* 3 鐠侊紕鐣诲顓炴妇閺囪尙鍤庨悙锟� S = [h]Pb * curve濞屸剝婀侀幐鍥х暰娴ｆ瑥娲滅�涙劧绱漢娑撹櫣鈹�
* 
* BigInteger h = curve.getCofactor(); System.out.print("h: ");
* printHexString(h.toByteArray()); if (publicKey != null) { ECPoint
* result = publicKey.multiply(h); if (!result.isInfinity()) {
* System.out.println("pass"); } else {
* System.err.println("鐠侊紕鐣诲顓炴妇閺囪尙鍤庨悙锟� S = [h]Pb婢惰精瑙�"); return null; } }
*/


/* 4 鐠侊紕鐣� [k]PB = (x2, y2) */
ECPoint kpb = publicKey.multiply(k).normalize();
byte[] kpbBuffer = kpb.getEncoded(false);
System.out.print("kpb: ");
printHexString(kpbBuffer);

/* 5 鐠侊紕鐣� t = KDF(x2||y2, klen) */
/*byte[] kpbBytes = kpb.getEncoded(false);
DerivationFunction kdf = new KDF1BytesGenerator(new ShortenedDigest(
new SHA256Digest(), 20));

*///KDF
byte[] KBarray =new BigInteger(kpb.getXCoord().toString()+kpb.getYCoord().toString(), 16).toByteArray(); 
byte[] t = KDF(KBarray,input.length());
System.out.print("KDF: ");
String KDF = new String(Hex.encode(t));
System.out.println(KDF);                          //KB
if (allZero(t)) {
System.err.println("all zero");
}


/* 6 鐠侊紕鐣籆2=M^t */
byte[] C2 = new byte[inputBuffer.length];
for (int i = 0; i < inputBuffer.length; i++) {
C2[i] = (byte) (inputBuffer[i] ^ t[i]);
}
System.out.print("C2: ");
String C2String = new String(Hex.encode(C2));
System.out.println(C2String);         

/* 7 鐠侊紕鐣籆3 = Hash(x2 || M || y2) */
String[] C3ALL = {kpb.getXCoord().toString(),new String(Hex.encode(inputBuffer)),kpb.getYCoord().toString()};
String C3String = SM02.My_HashZ(C3ALL,3);
/*byte[] C3 = calculateHash(kpb.getXCoord().toBigInteger(), inputBuffer,
kpb.getYCoord().toBigInteger());
*/
byte[] C3 = Hex.decode(C3String);
//System.out.println(C2String);       
System.out.print("C3: ");
System.out.println(C3String);   
/* 8 鏉堟挸鍤�靛棙鏋� C=C1 || C2 || C3 */


byte[] encryptResult = new byte[C1Buffer.length + C2.length + C3.length];
System.out.println("lll"+C1Buffer.length+","+C2.length+","+C3.length);

System.arraycopy(C1Buffer, 0, encryptResult, 0, C1Buffer.length);
System.arraycopy(C2, 0, encryptResult, C1Buffer.length, C2.length);
System.arraycopy(C3, 0, encryptResult, C1Buffer.length + C2.length,
C3.length);


System.out.print("鐎靛棙鏋�: ");
printHexString(encryptResult);


return encryptResult;
}


public byte[] decrypt(byte[] encryptData, BigInteger privateKey,int a,int b,int c,int d) {


System.out.println("encryptData length: " + encryptData.length);


byte[] C1Byte = new byte[a];
System.arraycopy(encryptData, 0, C1Byte, 0, C1Byte.length);


ECPoint C1 = curve.decodePoint(C1Byte).normalize();


/* 鐠侊紕鐣籟dB]C1 = (x2, y2) */
ECPoint dBC1 = C1.multiply(privateKey).normalize();
byte[] kpbBuffer = dBC1.getEncoded(false);
System.out.print("dBC1: ");
//printHexString(kpbBuffer);


int klen = d;
//int klen = encryptData.length - 65 - 20;
byte[] KBarray =new BigInteger(dBC1.getXCoord().toString()+dBC1.getYCoord().toString(), 16).toByteArray(); 
byte[] t = KDF(KBarray,klen);  //!!!!!!!!!!!!!!!!
//System.out.print("KDF: ");
String KDF = new String(Hex.encode(t));
//System.out.println(KDF);                          //KB
if (allZero(t)) {
System.err.println("all zero");
}


/* 5 鐠侊紕鐣籑'=C2^t */
byte[] M = new byte[klen];
for (int i = 0; i < M.length; i++) {
M[i] = (byte) (encryptData[C1Byte.length + i] ^ t[i]);
}
//printHexString(M);


/* 6 鐠侊紕鐣� u = Hash(x2 || M' || y2) 閸掋倖鏌� u == C3閺勵垰鎯侀幋鎰彌 */
byte[] C3 = new byte[32];


//System.out.println("M = " + new String(M));


System.out.println("el = " + encryptData.length);
System.arraycopy(encryptData, encryptData.length - c, C3, 0, c);

String[] U3ALL = {dBC1.getXCoord().toString(),new String(Hex.encode(M)),dBC1.getYCoord().toString()};
String UString = SM02.My_HashZ(U3ALL,3);
/*byte[] C3 = calculateHash(kpb.getXCoord().toBigInteger(), inputBuffer,
kpb.getYCoord().toBigInteger());
*/
byte[] u = Hex.decode(UString);    
//System.out.print("U: ");
//System.out.println(UString);   
if (Arrays.equals(u, C3)) {
System.out.println("OK");
} else {
System.out.print("u = ");
printHexString(u);
System.out.print("C3 = ");
printHexString(C3);
System.err.println("Wrong");
}
return M;

}


private byte[] calculateHash(BigInteger x2, byte[] M, BigInteger y2) {
ShortenedDigest digest = new ShortenedDigest(new SHA256Digest(), 20);
byte[] buf = x2.toByteArray();
digest.update(buf, 0, buf.length);
digest.update(M, 0, M.length);
buf = y2.toByteArray();
digest.update(buf, 0, buf.length);


buf = new byte[20];
digest.doFinal(buf, 0);


return buf;
}


private boolean between(BigInteger param, BigInteger min, BigInteger max) {
if (param.compareTo(min) >= 0 && param.compareTo(max) < 0) {
return true;
} else {
return false;
}
}


private boolean checkPublicKey(ECPoint publicKey) {


if (!publicKey.isInfinity()) {


BigInteger x = publicKey.getXCoord().toBigInteger();
BigInteger y = publicKey.getYCoord().toBigInteger();


if (between(x, new BigInteger("0"), p)
&& between(y, new BigInteger("0"), p)) {


BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);


//System.out.println("xResult: " + xResult.toString());


BigInteger yResult = y.pow(2).mod(p);


//System.out.println("yResult: " + yResult.toString());


if (yResult.equals(xResult)
&& publicKey.multiply(n).isInfinity()) {
return true;
}
}
return false;
} else {
return false;
}
}


public SM02KeyPair generateKeyPair(String str) {


//BigInteger d = random(n.subtract(new BigInteger("1")));

BigInteger d = new BigInteger(str,	16);
//BigInteger d = new BigInteger("1649AB77A00637BD5E2EFE283FBF353534AA7F7CB89463F208DDBC2920BB0DA0",16);


SM02KeyPair keyPair = new SM02KeyPair(G.multiply(d).normalize(), d);


if (checkPublicKey(keyPair.getPublicKey())) {
//System.out.println("generate key successfully");
return keyPair;
} else {
//System.err.println("generate key failed");
return null;
}
}


public SM02() {
curve = new ECCurve.Fp(p, // q
a, // a
b); // b
G = curve.createPoint(gx, gy);
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

public static String My_HashZ(String s[],int index)
{
	String StrTemp = "";
    for(int i = 0;i < index;i++){
    	//System.out.println(s[i]);
    	StrTemp += s[i];
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
/*public static void main(String[] args) {

	String content = "encryption standard";
	
	System.out.print("閸樼喎顫愰弫鐗堝祦閿涳拷 ");
	System.out.println(content);
	SM02 sm02 = new SM02();
	SM02KeyPair keyPair = sm02.generateKeyPair("1649AB77A00637BD5E2EFE283FBF353534AA7F7CB89463F208DDBC2920BB0DA0");
	
	System.out.println("閸旂姴鐦�");
	byte[] data = sm02.encrypt(content,keyPair.getPublicKey());
	System.out.println("鐟欙絽鐦�");
	String text = sm02.decrypt(data, keyPair.getPrivateKey());
	System.out.println(text);
	}*/
}