package getVerify;

import datamodels.*;
import invoiceImplement.*;
import invoiceInterface.IGetForInvoiceType;
import invoiceInterface.IProvinceURL;

import org.apache.http.client.ResponseHandler;
import util.HttpManager;
import java.io.ByteArrayInputStream;
import java.net.URI;
import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-29.
 */
public class verifyImage implements IGetForInvoiceType {
    private String invoiceType;
    private VerficationCodeResult verficationCodeResult;
    private VerficationCodeInfo verficationCodeInfo;

    //进行发票真伪验证
	public String[] commitHandler(String[] invoiceAllInfo, String verifyCode) {
        // check not null
        if (verficationCodeInfo == null){
        	return null;
        }
        if (verifyCode == null) {
        	return null;
        }
        if (invoiceAllInfo[0] == null) {
        	return null;
        }
        if (invoiceAllInfo[2] == null){
        	return null;
        }
        if (invoiceAllInfo[4] == null){
        	return null;
        }
        if (invoiceAllInfo[8] == null){
        	return null;
        }
        if (invoiceType == null)
            invoiceType = getForInvoiceType(invoiceAllInfo[2]);

        //1.data
        verficationCodeResult = new VerficationCodeResult(verficationCodeInfo, verifyCode);
        InvoiceInputInfo invoiceInputInfo = new InvoiceInputInfo(invoiceAllInfo[2]
                , invoiceAllInfo[0]
                , invoiceAllInfo[8]
                , invoiceAllInfo[4]);
        InvoiceRequestInfo invoiceRequestInfo = new InvoiceRequestInfo(invoiceType, invoiceInputInfo, verficationCodeResult);

        //2.HTTP GET
        //(1) use http manager
        HttpManager httpManager = HttpManager.getInstance();
        //(2)get URL
        IProvinceURL provinceURL = new GetInvoiceInquireURL(invoiceRequestInfo);
        URI uri = provinceURL.getProvinceURL();

        if (uri == null)
        {
        	return null;
        }
        logger.info("[INFO]==========got url ="+uri);
        //(3)make response handler
        ResponseHandler<InvoiceInfo> rs = new InvoiceHandler();
        logger.info("[INFO]==========rs = "+rs);
        //(4)exec http
        InvoiceInfo invoiceInfos = httpManager.httpProcess(uri, rs);
        logger.info("[INFO]========== exec http ok");
        //(5)InvoiceResult
        InvoiceResult invoiceResult = new InvoiceParase().getInvoiceResult(invoiceInfos);

        logger.info("[INFO]========== RESULT");
        logger.info("[INFO]========== invoiceInfos1 = "+invoiceInfos.getKey1());
        logger.info("[INFO]========== invoiceInfos2 = "+invoiceInfos.getKey2());
        logger.info("[INFO]========== invoiceInfos3 = "+invoiceInfos.getKey3());
        logger.info("[INFO]========== invoiceInfos4 = "+invoiceInfos.getKey4());
        logger.info("[INFO]========== invoiceInfos5 = "+invoiceInfos.getKey5());
        logger.info("[INFO]========== invoiceInfos6 = "+invoiceInfos.getKey6());
        logger.info("[INFO]========== invoiceInfos7 = "+invoiceInfos.getKey7());
        logger.info("[INFO]========== invoiceInfos8 = "+invoiceInfos.getKey8());
        logger.info("[INFO]========== invoiceInfos8 = "+invoiceInfos.getKey9());
        logger.info("[INFO]========== invoiceInfos8 = "+invoiceInfos.getKey10());
        logger.info("getTaxpayerNumber = "+invoiceResult.getTaxpayerNumber());
        
        String[] invoiceInfoArray = new String[3];
        invoiceInfoArray[0] = invoiceInfos.getKey2();
        invoiceInfoArray[1] = invoiceInfos.getKey3();
        invoiceInfoArray[2] = invoiceResult.getTaxpayerNumber();

        //此处我觉得需要将结果返回客户端
        logger.info("[INFO]========== COMMIT FINISHED");
        
        return invoiceInfoArray;
    }

    //获取验证码图片
	public String[] requestVercifationCodeHandler(String[] Infos) {
        //启动httpmanager
        HttpManager httpManager = HttpManager.getInstance();
        
        //获取对应省份税务局的url
        VerficationCodeRequestInfo vcq = new VerficationCodeRequestInfo(Infos[2]);
        IProvinceURL provinceURL = new GetVerficationCodeURL(vcq);
        URI uri = provinceURL.getProvinceURL();
        if(uri == null)
            return null;

        //创建response handler
        ResponseHandler<VerficationCodeInfo> rs = new VerficationCodeHandler(vcq);

        //执行http请求
		VerficationCodeInfo vci = httpManager.httpProcess(uri, rs);
        this.verficationCodeInfo = vci;

        //获取验证码图像
        VerficationCodeParse recognition = new VerficationCodeParse();        
        ByteArrayInputStream bArray = new ByteArrayInputStream(recognition.getForImage(vci));
 
        //用户输入hint
        //verficationCodeLab.setText(recognition.getForImageSelectColor(vci));
        logger.info("[INFO]==========VERFICATION CODE FINISHED");
        
        //需要将验证码发往客户端
        //return bArray;
        String[] Img_Hint = new String[2];
        Img_Hint[0] = recognition.getForImageSelectColor(vci);
        Img_Hint[1] = recognition.getForImage2(vci);
        return Img_Hint;
    }
	
    //analyze InvoiceType
    @Override
    public String getForInvoiceType(String code) {
        logger.info("[INFO]==========INVOICE CODE " + code);
        String invoiceType = "99";
        char tempstr;
        if (code.length() == 12) {
            if (invoiceType.equals("99")) {
                //增加判断，判断是否为新版电子票
                if (code.charAt(0) == '0' && code.substring(10, 12).equals("11")) {
                    invoiceType = "10";
                }
                if (code.charAt(0) == '0' && (code.substring(10, 12).equals("06") || code.substring(10, 12).equals("07"))) {
                    //判断是否为卷式发票  第1位为0且第11-12位为06或07
                    invoiceType = "11";
                }
            }
            if (invoiceType.equals("99")) { //如果还是99，且第8位是2，则是机动车发票
                if (code.charAt(7) == '2' && code.charAt(0) != '0') {
                    invoiceType = "03";
                }
            }
        } else if (code.length() == 10) {
            tempstr = code.charAt(7);
            if (tempstr == '1' || tempstr == '5') {
                invoiceType = "01";
            } else if (tempstr == '6' || tempstr == '3') {
                invoiceType = "04";
            } else if (tempstr == '7' || tempstr == '2') {
                invoiceType = "02";
            }
        } else {
            logger.warning("[warning]==========INVOICE TYPE ERROR");
        }
        logger.info("[INFO]==========INVOICE TYPE " + invoiceType);
        return invoiceType;
    }
}
