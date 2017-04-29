package invoiceImplement;


import datamodels.VerficationCodeInfo;
import datamodels.VerficationCodeRequestInfo;
import invoiceInterface.IVerficationCodeInfo;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-28.
 */
public class VerficationCodeHandler implements ResponseHandler<VerficationCodeInfo>, IVerficationCodeInfo {

    public static final String REGEX = "^.*(\\{.*\\}).*$";
    private VerficationCodeRequestInfo verficationCodeRequestInfo;

    public VerficationCodeHandler(VerficationCodeRequestInfo verficationCodeRequestInfo) {
        this.verficationCodeRequestInfo = verficationCodeRequestInfo;
    }

    @Override
    public VerficationCodeInfo handleResponse(HttpResponse httpResponse) throws IOException {

        logger.info("[INFO]========== START HANDLER RESPONSE");

        StatusLine statusLine = httpResponse.getStatusLine();

        if (statusLine.getStatusCode() < 300 && statusLine.getStatusCode() >= 200) {
            logger.info("[INFO]========== RESPONSE OK");
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                logger.warning("[warning]========== NO MORE ENTITY");
                throw new ClientProtocolException("Response contains no content");
            }


            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();

            Reader reader = null;
            try {
                reader = new InputStreamReader(entity.getContent(), charset);
            } catch (IOException e) {
                logger.info("[warning]========== RESPONSE ENTITY CONTENT ERROR");
                e.printStackTrace();
            }


            logger.info("[INFO]========== GET ENTITY CONTENT");


            StringBuffer contentStr = new StringBuffer();
            char[] buffer = new char[512];
            int n = 0;
            while ((n = reader.read(buffer)) != -1) {
                if (n != buffer.length)
                    contentStr.append(buffer, 0, n);
                else
                    contentStr.append(buffer);
            }


            logger.info("[warning]========== entity " + contentStr);

            Pattern pattern = Pattern.compile(".*(\\{.*\\}).*");
            Matcher matcher = pattern.matcher(contentStr);

            if (!matcher.find()) {
                logger.info("[warning]========== REG CONTENT NO MATCHER");
                return null;
            }

            String jsonText = matcher.group(1);

            logger.info("[INFO]==========GET JSON TEXT" + jsonText);
            if (jsonText == null)
                return null;

            JSONObject object = JSONObject.fromObject(jsonText);

            VerficationCodeInfo verficationCodeInfo = new VerficationCodeInfo(verficationCodeRequestInfo);


            logger.info("[INFO]==========CREATE JSON READER");

            verficationCodeInfo.setImageBase64(object.getString("key1"))
                    .setTime(object.getString("key2"))
                    .setIndex(object.getString("key3"))
                    .setSign(object.getString("key4"));
            return verficationCodeInfo;
        }

        return null;
    }

    @Override
    public void setVerficationCodeInfoConvert(VerficationCodeRequestInfo verficationCodeRequestInfo) {
        this.verficationCodeRequestInfo = verficationCodeRequestInfo;
        return;
    }
}


