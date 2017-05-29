package invoiceImplement;

import datamodels.InvoiceInfo;
import datamodels.InvoiceResult;
import invoiceInterface.IGetRuleURL;
import invoiceInterface.IInvoiceResult;
import org.apache.http.client.ResponseHandler;
import util.HttpManager;

import java.net.URI;

import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */
public class InvoiceParase implements IInvoiceResult {
    public InvoiceParase() {
    }

    @Override
    public InvoiceResult getInvoiceResult(InvoiceInfo invoiceInfo) {
    	logger.info("[INFO]========== enter getInvoiceResult");
        String sign = "";
        sign = signResult(invoiceInfo);
        InvoiceResult result = new InvoiceResult(sign, invoiceInfo);

        //成功则进行纳税人识别号的获取.
        if (sign.equals("001")) {
        	logger.info("[INFO]========== 001 get success");
            String taxPayerCode = null;
            taxPayerCode = taxPayerCodePaser(invoiceInfo);
            result.setTaxpayerNumber(taxPayerCode);

            logger.info("[INFO]========== taxpayercode is " + taxPayerCode);
            return result;
        }else{
        	logger.info("[INFO]========== get taxpayercode failed");
        }

        return result;
    }


    private String[] getRule(InvoiceInfo invoiceInfo) {
    	String[] rule = null;
    	logger.info("[INFO]========== enter getRule ");
        //(1) use http manager
        HttpManager httpManager = HttpManager.getInstance();
        //(2)get URL
        IGetRuleURL ruleURL = new GetRuleURL();
        URI uri = ruleURL.getRuleURL(invoiceInfo);

        if (uri == null) {
            logger.warning("[warning]==========uri null");
            return null;
        }

        //(3)make response handler
        ResponseHandler<String[]> rs = new RuleHandler();

        //(4)exec http
        if(uri != null) {
        	rule = httpManager.httpProcess(uri, rs);
        }
        return rule;
    }

    private String taxPayerCodePaser(InvoiceInfo invoiceInfo) {
        String[] rule = getRule(invoiceInfo);
        if (rule == null) {
            return null;
        }

        String taxpayerCode = (invoiceInfo.getKey2().split(rule[0]))[4];

        taxpayerCode =  taxpayerCode.replace(rule[1].charAt(0), '%');
        taxpayerCode = taxpayerCode.replace(rule[1].charAt(2), '#');

        taxpayerCode = taxpayerCode.replace('#', rule[1].charAt(0));
        taxpayerCode = taxpayerCode.replace('%', rule[1].charAt(2));

        taxpayerCode = taxpayerCode.replace(rule[1].charAt(4), '%');
        taxpayerCode = taxpayerCode.replace(rule[1].charAt(6), '#');

        taxpayerCode = taxpayerCode.replace('#', rule[1].charAt(4));
        taxpayerCode = taxpayerCode.replace('%', rule[1].charAt(6));

        return taxpayerCode;
    }

    private String signResult(InvoiceInfo invoiceInfo) {
    	logger.info("[INFO]========== enter getKey1");
    	String sign = ""; 
    	sign = invoiceInfo.getKey1();
    	
        if(sign != null) {
        	if (sign.equals("001")) {
        		logger.info("[INFO]========== INVOICE SUCCESS");
        	} else if (sign.equals("002")) {
        		logger.info("[INFO]==========超过该张发票当日查验次数(请于次日再次查验)");
        	} else if (sign.equals("003")) {
        		logger.info("[INFO]==========发票查验请求太频繁，请稍后再试！");
        	} else if (sign.equals("004")) {
        		logger.info("[INFO]==========超过服务器最大请求数，请稍后访问!");
        	} else if (sign.equals("005")) {
        		logger.info("[INFO]==========请求不合法!");
        	} else if (sign.equals("020")) {
        		logger.info("[INFO]==========由于查验行为异常，涉嫌违规，当前无法使用查验服务！");
        	} else if (sign.equals("006")) {
        		logger.info("[INFO]==========不一致");
        	} else if (sign.equals("007")) {
        		logger.info("[INFO]==========验证码失效!");
        	} else if (sign.equals("008")) {
        		logger.info("[INFO]==========验证完成!");
        	} else if (sign.equals("009")) {
        		logger.info("[INFO]==========查无此票");
        	} else if (sign.equals("rqerr")) {
        		logger.info("[INFO]==========当日开具发票可于次日进行查验！");
        	} else if (sign.equals("010")) {
        		logger.info("[INFO]==========网络超时，请重试！");
        	} else if (sign.equals("010_")) {
        		logger.info("[INFO]==========网络超时，请重试！");
        	} else {
        		logger.info("[INFO]==========网络超时，请重试！");
        	}
        }else{
        	logger.info("[INFO]==========sign is null");
        }
        return sign;
    }
}
