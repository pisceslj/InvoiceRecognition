package com.crypto;


import java.math.BigInteger;

import org.bouncycastle.util.encoders.Hex;  


import java.security.SecureRandom;
import java.util.Arrays;


import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;


import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.digests.ShortenedDigest;

import org.bouncycastle.crypto.digests.SHA256Digest;
    
import org.bouncycastle.crypto.generators.KDF1BytesGenerator;
import org.bouncycastle.crypto.params.ISO18033KDFParameters;

public class SM3Digest  
{  
    /** SM3鍊肩殑闀垮害 */  
    private static final int BYTE_LENGTH = 32;  
      
    /** SM3鍒嗙粍闀垮害 */  
    private static final int BLOCK_LENGTH = 64;  
      
    /** 缂撳啿鍖洪暱搴� */  
    private static final int BUFFER_LENGTH = BLOCK_LENGTH * 1;  
      
    /** 缂撳啿鍖� */  
    private byte[] xBuf = new byte[BUFFER_LENGTH];  
      
    /** 缂撳啿鍖哄亸绉婚噺 */  
    private int xBufOff;  
      
    /** 鍒濆鍚戦噺 */  
    private byte[] V = SM3.iv.clone();  
      
    private int cntBlock = 0;  
  
    public SM3Digest() {  
    }  
  
    public SM3Digest(SM3Digest t)  
    {  
        System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);  
        this.xBufOff = t.xBufOff;  
        System.arraycopy(t.V, 0, this.V, 0, t.V.length);  
    }  
      
    /** 
     * SM3缁撴灉杈撳嚭 
     *  
     * @param out 淇濆瓨SM3缁撴瀯鐨勭紦鍐插尯 
     * @param outOff 缂撳啿鍖哄亸绉婚噺 
     * @return 
     */  
    public int doFinal(byte[] out, int outOff)   
    {  
        byte[] tmp = doFinal();  
        System.arraycopy(tmp, 0, out, 0, tmp.length);  
        return BYTE_LENGTH;  
    }  
  
    public void reset()   
    {  
        xBufOff = 0;  
        cntBlock = 0;  
        V = SM3.iv.clone();  
    }  
  
    /** 
     * 鏄庢枃杈撳叆 
     *  
     * @param in 
     *            鏄庢枃杈撳叆缂撳啿鍖� 
     * @param inOff 
     *            缂撳啿鍖哄亸绉婚噺 
     * @param len 
     *            鏄庢枃闀垮害 
     */  
    public void update(byte[] in, int inOff, int len)  
    {  
        int partLen = BUFFER_LENGTH - xBufOff;  
        int inputLen = len;  
        int dPos = inOff;  
        if (partLen < inputLen)   
        {  
            System.arraycopy(in, dPos, xBuf, xBufOff, partLen);  
            inputLen -= partLen;  
            dPos += partLen;  
            doUpdate();  
            while (inputLen > BUFFER_LENGTH)   
            {  
                System.arraycopy(in, dPos, xBuf, 0, BUFFER_LENGTH);  
                inputLen -= BUFFER_LENGTH;  
                dPos += BUFFER_LENGTH;  
                doUpdate();  
            }  
        }  
  
        System.arraycopy(in, dPos, xBuf, xBufOff, inputLen);  
        xBufOff += inputLen;  
    }  
  
    private void doUpdate()   
    {  
        byte[] B = new byte[BLOCK_LENGTH];  
        for (int i = 0; i < BUFFER_LENGTH; i += BLOCK_LENGTH)   
        {  
            System.arraycopy(xBuf, i, B, 0, B.length);  
            doHash(B);  
        }  
        xBufOff = 0;  
    }  
  
    private void doHash(byte[] B)  
    {  
        byte[] tmp = SM3.CF(V, B);  
        System.arraycopy(tmp, 0, V, 0, V.length);  
        cntBlock++;  
    }  
  
    private byte[] doFinal()   
    {  
        byte[] B = new byte[BLOCK_LENGTH];  
        byte[] buffer = new byte[xBufOff];  
        System.arraycopy(xBuf, 0, buffer, 0, buffer.length);  
        byte[] tmp = SM3.padding(buffer, cntBlock);  
        for (int i = 0; i < tmp.length; i += BLOCK_LENGTH)  
        {  
            System.arraycopy(tmp, i, B, 0, B.length);  
            doHash(B);  
        }  
        return V;  
    }  
  
    public void update(byte in)   
    {  
        byte[] buffer = new byte[] { in };  
        update(buffer, 0, 1);  
    }  
      
    public int getDigestSize()   
    {  
        return BYTE_LENGTH;  
    }  
      
    
}  