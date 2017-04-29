package com.crypto;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;



public class SM02KeyPair {


private  ECPoint publicKey;
private BigInteger privateKey;

SM02KeyPair(ECPoint publicKey, BigInteger privateKey) {
this.publicKey = publicKey;
this.privateKey = privateKey;
}

public ECPoint getPublicKey() {
return publicKey;
}

public BigInteger getPrivateKey() {
return privateKey;
}

}
