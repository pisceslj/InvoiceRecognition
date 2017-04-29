package datamodels;

/**
 * Created by mac on 2017/3/27.
 */
public class InvoiceInputInfo {
    private String invoiceCode;
    private String invoiceNumber;
    private String date;
    private String checkCode;

    public InvoiceInputInfo(String invoiceCode, String invoiceNumber, String date, String checkCode) {
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.checkCode = checkCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public InvoiceInputInfo setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
        return this;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public InvoiceInputInfo setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public String getDate() {
        return date;
    }

    public InvoiceInputInfo setDate(String date) {
        this.date = date;
        return this;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public InvoiceInputInfo setCheckCode(String checkCode) {
        this.checkCode = checkCode;
        return this;
    }
}
