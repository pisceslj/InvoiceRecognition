package invoiceImplement;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.LogRecord.logger;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-4-18.
 */
public class RuleHandler implements ResponseHandler<String[]> {

    @Override
    public String[] handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        logger.info("[INFO]========== START HANDLER RESPONSE");
        StatusLine statusLine = httpResponse.getStatusLine();

        if (statusLine.getStatusCode() < 300 && statusLine.getStatusCode() >= 200) {
            logger.info("[INFO]========== RESPONSE OK");
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                logger.warning("[warning]========== NO MORE ENTITY");
                throw new ClientProtocolException("Response contains no content");
            }

            //ContentType contentType = ContentType.getOrDefault(entity);
            //Charset charset = contentType.getCharset();

            Reader reader = null;
            try {
                reader = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.warning("[warning]========== RESPONSE ENTITY CONTENT ERROR");
                e.printStackTrace();
            }

            StringBuffer contentStr = new StringBuffer();
            char[] buffer = new char[20];
            int n = 0;
            while ((n = reader.read(buffer)) != -1) {
                if (n != buffer.length)
                    contentStr.append(buffer, 0, n);
                else
                    contentStr.append(buffer);
            }


            logger.info("[INFO]========== contentStr is " + contentStr);
            Pattern pattern = Pattern.compile(".*\"(.*)\".*");
            Matcher matcher = pattern.matcher(contentStr);

            if (!matcher.find()) {
                logger.warning("[warning]========== REG CONTENT NO MATCHER");
                return null;
            }

            String rules = matcher.group(1);
            logger.info("[INFO]========== rules is " + rules);
            String[] rs = rules.split("☺");
            //logger.info("[INFO]========== rules[] is " + rs[0] + " " + rs[1]);
            return rs;

        }
        return null;
    }
}
