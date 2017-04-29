package datamodels;

import org.apache.http.client.utils.URIUtils;

/**
 * Created by mac on 2017/3/27.
 */
public final class CityInfo {
    private String code;
    private String name;
    private String url;

    public CityInfo(String code, String name, String url) {
        this.code = code;
        this.name = name;
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
