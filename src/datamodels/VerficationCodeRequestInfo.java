package datamodels;

/**
 * Created by mac on 2017/3/27.
 */
public class VerficationCodeRequestInfo {
    private String callback;
    private String invoiceCode;
    private String rand;
    private long timestamps;

    public VerficationCodeRequestInfo(String invoiceCode) {
        this.invoiceCode = invoiceCode;
        setTimestamps();
        setRand();
    }

    public String getCallback() {
        return callback;
    }

    public VerficationCodeRequestInfo setCallback() {
        this.callback = "callback" + timestamps;
        return this;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public VerficationCodeRequestInfo setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
        return this;
    }

    public String getRand() {
        return rand;
    }

    public VerficationCodeRequestInfo setRand() {
        rand = Double.toString(Math.random());
        return this;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public VerficationCodeRequestInfo setTimestamps() {
        timestamps=System.currentTimeMillis();
        setCallback();
        return this;
    }
}
