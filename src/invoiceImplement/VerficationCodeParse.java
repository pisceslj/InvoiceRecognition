package invoiceImplement;

import datamodels.VerficationCodeInfo;
import datamodels.VerficationCodeResult;
import invoiceInterface.IGetForImage;
import invoiceInterface.IVerficationCodeResult;
import sun.misc.BASE64Decoder;

import java.util.Objects;

import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-28.
 */
public class VerficationCodeParse implements IVerficationCodeResult, IGetForImage {
    @Override
    public VerficationCodeResult VerficationCodeResultConvert(VerficationCodeInfo verficationCodeInfo, String result) {
        if (result != null)
            return new VerficationCodeResult(verficationCodeInfo, result);
        return null;
    }
    @Override
    public byte[] getForImage(VerficationCodeInfo verficationCodeInfo) {

        if (errorHandler(verficationCodeInfo)) {
            return null;
        }
        // selectDisplay(verficationCodeInfo);

        return imageDecode(verficationCodeInfo);
    }

    @Override
    public String getForImageSelectColor(VerficationCodeInfo verficationCodeInfo) {
        String display;
        String sign = verficationCodeInfo.getSign();
        if (Objects.equals(sign, "00")) {
            display = "CODE ALL";
        } else if (Objects.equals(sign, "01")) {
            display = "CODE RED";
        } else if (Objects.equals(sign, "02")) {
            display = "CODE YELLOW";
        } else if (Objects.equals(sign, "03")) {
            display = "CODE BLUE";
        } else {
            display = "";
            logger.warning("[warning]==========" + "NULL COLOR");
        }
        logger.info("[INFO]==========" + display);
        return display;
    }

    private boolean errorHandler(VerficationCodeInfo verficationCodeInfo) {
        String errorCode = verficationCodeInfo.getImageBase64();
        if (Objects.equals(errorCode, "003")) {
            logger.warning("[warning]==========验证码请求次数过于频繁，请1分钟后再试！");
        } else if (Objects.equals(errorCode, "005")) {
            logger.warning("[warning]==========非法请求!");
        } else if (Objects.equals(errorCode, "010")) {
            logger.warning("[warning]==========网络超时，请重试！(01)");
        } else if (Objects.equals(errorCode, "fpdmerr")) {
            logger.warning("[warning]==========请输入合法发票代码!");
        } else if (Objects.equals(errorCode, "024")) {
            logger.warning("[warning]==========24小时内验证码请求太频繁，请稍后再试！");
        } else if (Objects.equals(errorCode, "016")) {
            logger.warning("[warning]==========服务器接收的请求太频繁，请稍后再试！");
        } else if (Objects.equals(errorCode, "020")) {
            logger.warning("[warning]==========由于查验行为异常，涉嫌违规，当前无法使用查验服务！");
        } else if (Objects.equals(errorCode, "")) {

        } else {
            logger.info("[INFO]==========" + "GET IMAGE SUCCESS");
            return false; //ok
        }

        return true;
    }

    private byte[] imageDecode(VerficationCodeInfo verficationCodeInfo) {
        String imgStr = verficationCodeInfo.getImageBase64();
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            logger.info("[INFO]==========" + "IMAGE DECODE SUCCESS");
//            String imgFilePath = "/home/local/SPREADTRUM/jiannan.liu/Invoice/InvoiceProject/out/222.png";//新生成的图片
//            OutputStream out = new FileOutputStream(imgFilePath);
//            out.write(b);
//            out.flush();
//            out.close();
            return b;
        } catch (Exception e) {
            logger.warning("[warning]==========" + "IMAGE DECODE FAILED");
            e.printStackTrace();
            return null;
        }

    }
    public String getForImage2(VerficationCodeInfo verficationCodeInfo) {

        if (errorHandler(verficationCodeInfo)) {
            return null;
        }
        // selectDisplay(verficationCodeInfo);
        
        return verficationCodeInfo.getImageBase64();
    }
}
