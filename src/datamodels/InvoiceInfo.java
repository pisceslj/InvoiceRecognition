package datamodels;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */


//由于意义不是很明确暂时 使用key来表示,以后可以refactor
public class InvoiceInfo {
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String key5;
    private String key6;
    private String key7;
    private String key8;
    private String key9;
    private String key10;
    private String rules;

    public InvoiceInfo(InvoiceRequestInfo invoiceRequestInfo) {
    }

    //由于意义不是很明确暂时 使用key来表示,以后可以refactor
    public String getKey1() {
        return key1;
    }

    public InvoiceInfo setKey1(String key1) {
        this.key1 = key1;
        return this;
    }

    public String getKey2() {
        return key2;
    }

    public InvoiceInfo setKey2(String key2) {
        this.key2 = key2;
        return this;
    }

    public String getKey3() {
        return key3;
    }

    public InvoiceInfo setKey3(String key3) {
        this.key3 = key3;
        return this;
    }

    public String getKey4() {
        return key4;
    }

    public InvoiceInfo setKey4(String key4) {
        this.key4 = key4;
        return this;
    }

    public String getKey5() {
        return key5;
    }

    public InvoiceInfo setKey5(String key5) {
        this.key5 = key5;
        return this;
    }

    public String getKey6() {
        return key6;
    }

    public InvoiceInfo setKey6(String key6) {
        this.key6 = key6;
        return this;
    }

    public String getKey7() {
        return key7;
    }

    public InvoiceInfo setKey7(String key7) {
        this.key7 = key7;
        return this;
    }

    public String getKey8() {
        return key8;
    }

    public InvoiceInfo setKey8(String key8) {
        this.key8 = key8;
        return this;
    }

    public String getKey9() {
        return key9;
    }

    public InvoiceInfo setKey9(String key9) {
        this.key9 = key9;
        return this;
    }

    public String getKey10() {
        return key10;
    }

    public InvoiceInfo setKey10(String key10) {
        this.key10 = key10;
        return this;
    }

    public String getRules() {
        return rules;
    }

    public InvoiceInfo setRules(String rules) {
        this.rules = rules;
        return this;
    }
}
