package invoiceInterface;


import datamodels.InvoiceInfo;

import java.net.URI;


/**
 * Created by SPREADTRUM\jiannan.liu on 17-4-18.
 */
public interface IGetRuleURL {
    //https://inv-veri.chinatax.gov.cn/js/582ae.js?
    final String HOST = "https://inv-veri.chinatax.gov.cn/js/";

    URI getRuleURL(InvoiceInfo invoiceInfo);

}
