package datamodels;

import util.EncryptUtils;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */
public class InvoiceRequestInfo {
    private String InvoiceType;
    private String callback;
    private long timestamps;

    private InvoiceInputInfo invoiceInputInfo;
    private VerficationCodeResult verficationCodeResult;

    private String iv;
    private String salt;


    public InvoiceRequestInfo(String invoiceType, InvoiceInputInfo invoiceInputInfo, VerficationCodeResult verficationCodeResult) {
        InvoiceType = invoiceType;
        this.invoiceInputInfo = invoiceInputInfo;
        this.verficationCodeResult = verficationCodeResult;
        setTimestamps();
        setIv();
        setSalt();
    }

    public String getCallback() {
        return callback;
    }

    public InvoiceRequestInfo setCallback() {
        this.callback = "jQuery" + timestamps;
        return this;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public InvoiceRequestInfo setTimestamps() {
        timestamps = System.currentTimeMillis();
        setCallback();
        return this;
    }

    public InvoiceRequestInfo setIv() {
        this.iv = EncryptUtils.getIV();
        return this;
    }

    public InvoiceRequestInfo setSalt() {
        this.salt = EncryptUtils.getSalt();
        return this;
    }

    public InvoiceInputInfo getInvoiceInputInfo() {
        return invoiceInputInfo;
    }

    public InvoiceRequestInfo setInvoiceInputInfo(InvoiceInputInfo invoiceInputInfo) {
        this.invoiceInputInfo = invoiceInputInfo;
        return this;
    }

    public String getInvoiceType() {
        return InvoiceType;
    }

    public InvoiceRequestInfo setInvoiceType(String invoiceType) {
        InvoiceType = invoiceType;
        return this;
    }

    public VerficationCodeResult getVerficationCodeResult() {
        return verficationCodeResult;
    }

    public InvoiceRequestInfo setVerficationCodeResult(VerficationCodeResult verficationCodeResult) {
        this.verficationCodeResult = verficationCodeResult;
        return this;
    }

    public String getIv() {
        return iv;
    }

    public String getSalt() {
        return salt;
    }


//
//            'fpdm': fpdm,
//            'fphm': fphm,
//            'kprq': kprq,
//            'fpje': kjje,
//
//            'fplx': fplx,
//
//            'yzm': yzm,
//            'yzmSj': yzmSj,
//            'index': jmmy,
//
//            'iv': iv,
//            'salt': salt
}
