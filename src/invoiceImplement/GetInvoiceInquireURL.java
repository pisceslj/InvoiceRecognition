package invoiceImplement;

import datamodels.InvoiceRequestInfo;
import invoiceInterface.IProvinceURL;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */
public class GetInvoiceInquireURL implements IProvinceURL {
    /*
    callback=jQuery1102001507891467208089_1490923413069&
    fpdm=4403161320&
    fphm=17151216&
    kprq=20161212&
    fpje=685135&
    fplx=04&
    yzm=5&
    yzmSj=2017-03-31+09%3A45%3A36&
    index=fd3daf10defbc76a7433ebca2c4537f7
    &iv=3a4d808e53b0032bb40b3d2b684fc613
    &salt=7656b499148cc215b3e8422df46595a4
    &_=1490923413076
    */
    final String PATH = "/WebQuery/query";
    final String CALLBACK = "callback";
    final String FPDM = "fpdm";
    final String FPHA = "fphm";
    final String KPRQ = "kprq";
    final String FPJE = "fpje";
    final String FPLX = "fplx";
    final String YZM = "yzm";
    final String YZMSJ = "yzmSj";
    final String INDEX = "index";
    final String IV = "iv";
    final String SALT = "salt";
    final String TIMESTAMP = "_";

    private InvoiceRequestInfo invoiceRequestInfo;

    public GetInvoiceInquireURL(InvoiceRequestInfo invoiceRequestInfo) {
        this.invoiceRequestInfo = invoiceRequestInfo;
    }

    @Override
    public URI getProvinceURL() {
        String url = getHostAddress(invoiceRequestInfo.getInvoiceInputInfo().getInvoiceCode());
        URI uri = null;
        try {
            uri = new URIBuilder(url.toString())
                    .setPath(PATH)
                    .setParameter(CALLBACK, invoiceRequestInfo.getCallback())
                    .setParameter(FPDM, invoiceRequestInfo.getInvoiceInputInfo().getInvoiceCode())
                    .setParameter(FPHA, invoiceRequestInfo.getInvoiceInputInfo().getInvoiceNumber())
                    .setParameter(KPRQ, invoiceRequestInfo.getInvoiceInputInfo().getDate())
                    .setParameter(FPJE, invoiceRequestInfo.getInvoiceInputInfo().getCheckCode())
                    .setParameter(FPLX, invoiceRequestInfo.getInvoiceType())
                    .setParameter(YZM, invoiceRequestInfo.getVerficationCodeResult().getResult())
                    .setParameter(YZMSJ, invoiceRequestInfo.getVerficationCodeResult().getVerficationCodeInfo().getTime())
                    .setParameter(INDEX, invoiceRequestInfo.getVerficationCodeResult().getVerficationCodeInfo().getIndex())
                    .setParameter(IV, invoiceRequestInfo.getIv())
                    .setParameter(SALT, invoiceRequestInfo.getSalt())
                    .setParameter(TIMESTAMP, String.valueOf(invoiceRequestInfo.getTimestamps()))
                    .build();

        } catch (URISyntaxException e) {
            logger.warning("[warning]==========URL SYNTAX FAILED");
            uri = null;
            e.printStackTrace();
        }
        logger.info("[INFO]==========URL IS " + uri.toString());
        return uri;

    }

    public InvoiceRequestInfo getInvoiceRequestInfo() {
        return invoiceRequestInfo;
    }

    public void setInvoiceRequestInfo(InvoiceRequestInfo invoiceRequestInfo) {
        this.invoiceRequestInfo = invoiceRequestInfo;
    }
}
