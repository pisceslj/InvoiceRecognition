package util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static util.LogRecord.logger;

/**
 * Created by mac on 2017/3/27.
 */
public class HttpManager {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static HttpManager ourInstance = new HttpManager();
    private static CloseableHttpClient httpClient = null;
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;


    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }

            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        return ourInstance;
    }

    public static void clientCreate() {
        if (httpClient == null) {
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
        }
        logger.info("[INFO]==========CREATE HTTP CLIENT");
    }

    public static void shutdown() throws IOException {
        if (httpClient != null) {
            httpClient.close();
            httpClient = null;
        }
        logger.info("[INFO]==========SHUTDOWN HTTP CLIENT");
    }

    public static <T> T httpProcess(URI uri, ResponseHandler<T> responseHandler) {
        T verficationCodeInfo = null;
        clientCreate();


        logger.info("[INFO]==========START EXEC HTTP");
        try {
            verficationCodeInfo = clientExec(getRequestCreate(uri), responseHandler);
        } catch (IOException e) {
            logger.warning("[warning]==========exec GET method IO exception");
            e.printStackTrace();
        }

        logger.info("[INFO]==========END EXEC HTTP");
        try {
            shutdown();
        } catch (IOException e) {
            logger.warning("[warning]==========close httpclient IO exception");
            e.printStackTrace();
        }

        return verficationCodeInfo;

    }

    private static HttpGet getRequestCreate(URI uri) {
        HttpGet httpget = new HttpGet(uri);
        return httpget;
    }

    private static HttpResponse clientExec(HttpGet httpGet) throws IOException {
        if(httpClient==null){
            clientCreate();
        }
        HttpResponse httpResponse=httpClient.execute(httpGet);
        return httpResponse;
    }

    private static <T> T clientExec(HttpGet httpGet, ResponseHandler<T> responseHandler) throws IOException {
        if(httpClient==null){
            clientCreate();
        }
        logger.info("[INFO]========== EXECING HTTP");
        T httpResponse=httpClient.execute(httpGet,responseHandler);
        return httpResponse;
    }
}
