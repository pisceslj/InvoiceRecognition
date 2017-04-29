package datamodels;

import java.time.LocalDateTime;


/**
 * Created by mac on 2017/3/27.
 */
public class VerficationCodeInfo {
    private VerficationCodeRequestInfo verficationCodeRequestInfo;

    private String imageBase64;
    private LocalDateTime localDateTime;
    private String time;
    private String index;
    private String sign;

    public VerficationCodeInfo(VerficationCodeRequestInfo verficationCodeRequestInfo) {
        this.verficationCodeRequestInfo = verficationCodeRequestInfo;
    }

    public String getTime() {
        return time;
    }

    public VerficationCodeInfo setTime(String time) {
        this.time = time;
        return this;
    }

    public VerficationCodeRequestInfo getVerficationCodeRequestInfo() {
        return verficationCodeRequestInfo;
    }

    public VerficationCodeInfo setVerficationCodeRequestInfo(VerficationCodeRequestInfo verficationCodeRequestInfo) {
        this.verficationCodeRequestInfo = verficationCodeRequestInfo;
        return this;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public VerficationCodeInfo setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
        return this;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public VerficationCodeInfo setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public VerficationCodeInfo setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public VerficationCodeInfo setSign(String sign) {
        this.sign = sign;
        return this;
    }

}
