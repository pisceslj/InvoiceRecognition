package invoiceImplement;

import datamodels.VerficationCodeRequestInfo;
import invoiceInterface.IProvinceURL;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-28.
 */
public class GetVerficationCodeURL implements IProvinceURL {

    final String PATH = "/WebQuery/yzmQuery";
    final String CALLBACK = "callback";
    final String FPDM = "fpdm";
    final String RAND = "r";
    final String TIMESTAMP = "_";
    private VerficationCodeRequestInfo verficationCodeRequestInfo;

    public GetVerficationCodeURL(VerficationCodeRequestInfo verficationCodeRequestInfo) {
        this.verficationCodeRequestInfo = verficationCodeRequestInfo;
    }

    public VerficationCodeRequestInfo getVerficationCodeRequestInfo() {
        return verficationCodeRequestInfo;
    }

    public void setVerficationCodeRequestInfo(VerficationCodeRequestInfo verficationCodeRequestInfo) {
        this.verficationCodeRequestInfo = verficationCodeRequestInfo;
    }

    @Override
    public URI getProvinceURL() {
        String url = getHostAddress(verficationCodeRequestInfo.getInvoiceCode());
        URI uri = null;
        try {
            uri = new URIBuilder(url.toString())
                    .setPath(PATH)
                    .setParameter(CALLBACK, verficationCodeRequestInfo.getCallback())
                    .setParameter(FPDM, verficationCodeRequestInfo.getInvoiceCode())
                    .setParameter(RAND, verficationCodeRequestInfo.getRand())
                    .setParameter(TIMESTAMP, String.valueOf(verficationCodeRequestInfo.getTimestamps()))
                    .build();

        } catch (URISyntaxException e) {
            logger.warning("[warning]==========URL SYNTAX FAILED");
            uri = null;
            e.printStackTrace();
        }
        logger.info("[INFO]==========URL IS " + uri.toString());
        return uri;
    }
}
