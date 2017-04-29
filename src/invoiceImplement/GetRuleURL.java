package invoiceImplement;

import datamodels.InvoiceInfo;
import invoiceInterface.IGetRuleURL;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static util.LogRecord.logger;


/**
 * Created by SPREADTRUM\jiannan.liu on 17-4-18.
 */
public class GetRuleURL implements IGetRuleURL {
    @Override
    public URI getRuleURL(InvoiceInfo invoiceInfo) {
        String url = HOST + invoiceInfo.getRules() + ".js";
        URI uri = null;
        try {
            uri = new URIBuilder(url).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.warning("[warning]==========URL SYNTAX FAILED");
        }
        logger.info("[INFO]==========URL IS " + uri.toString());
        return uri;
    }
}
