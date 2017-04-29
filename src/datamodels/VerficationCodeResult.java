package datamodels;

/**
 * Created by mac on 2017/3/27.
 */
public class VerficationCodeResult {
    private VerficationCodeInfo verficationCodeInfo;
    private String result;

    public VerficationCodeResult(VerficationCodeInfo verficationCodeInfo, String result) {
        this.verficationCodeInfo = verficationCodeInfo;
        this.result = result;
    }

    public VerficationCodeInfo getVerficationCodeInfo() {
        return verficationCodeInfo;
    }

    public void setVerficationCodeInfo(VerficationCodeInfo verficationCodeInfo) {
        this.verficationCodeInfo = verficationCodeInfo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
