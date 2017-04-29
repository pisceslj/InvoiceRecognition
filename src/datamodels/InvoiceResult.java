package datamodels;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */
public class InvoiceResult {
    private InvoiceInfo invoiceInfo;
    private String sign;

    //纳税人识别号
    private String taxpayerNumber;

    public InvoiceResult(String sign, InvoiceInfo invoiceInfo) {
        this.sign = sign;
        this.invoiceInfo = invoiceInfo;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public InvoiceInfo getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(InvoiceInfo invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
