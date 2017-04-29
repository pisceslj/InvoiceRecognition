package com.crypto;

public class Message {
    public byte[] buffer;
    public int C1_length = 0;
    public int C2_length = 0;
    public int C3_length = 0;
    public int ms_length = 0;
    public void Set(byte[] temp,int a,int b,int c,int ms)
    {
        buffer = temp.clone();
        C1_length = a;
        C2_length = b;
        C3_length = c;
        ms_length = ms;
    }
}
