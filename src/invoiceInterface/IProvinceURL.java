package invoiceInterface;

import datamodels.CityInfo;

import java.net.URI;

import static util.LogRecord.logger;

/**
 * Created by mac on 2017/3/27.
 */
public interface IProvinceURL {
    CityInfo[] CITY_INFOS = {
            new CityInfo("1100", "北京", "https://zjfpcyweb.bjsat.gov.cn:443"),
            new CityInfo("1200", "天津", "https://fpcy.tjsat.gov.cn:443"),
            new CityInfo("1300", "河北", "https://fpcy.he-n-tax.gov.cn:82"),
            new CityInfo("1400", "山西", "https://fpcy.tax.sx.cn:443"),
            new CityInfo("1500", "内蒙古", "https://fpcy.nm-n-tax.gov.cn:443"),
            new CityInfo("2100", "辽宁", "https://fpcy.tax.ln.cn:443"),
            new CityInfo("2102", "大连", "https://fpcy.dlntax.gov.cn:443"),
            new CityInfo("2200", "吉林", "https://fpcy.jl-n-tax.gov.cn:4432"),
            new CityInfo("2300", "黑龙江", "https://fpcy.hl-n-tax.gov.cn:443"),
            new CityInfo("3100", "上海", "https://fpcyweb.tax.sh.gov.cn:1001"),
            new CityInfo("3200", "江苏", "https://fpdk.jsgs.gov.cn:80"),
            new CityInfo("3300", "浙江", "https://fpcyweb.zjtax.gov.cn:443"),
            new CityInfo("3302", "宁波", "https://fpcy.nb-n-tax.gov.cn:443"),
            new CityInfo("3400", "安徽", "https://fpcy.ah-n-tax.gov.cn:443"),
            new CityInfo("3500", "福建", "https://fpcyweb.fj-n-tax.gov.cn:443"),
            new CityInfo("3502", "厦门", "https://fpcy.xm-n-tax.gov.cn"),
            new CityInfo("3600", "江西", "https://fpcy.jxgs.gov.cn:82"),
            new CityInfo("3700", "山东", "https://fpcy.sd-n-tax.gov.cn:443"),
            new CityInfo("3702", "青岛", "https://fpcy.qd-n-tax.gov.cn:443"),
            new CityInfo("4100", "河南", "https://fpcy.ha-n-tax.gov.cn"),
            new CityInfo("4200", "湖北", "https://fpcy.hb-n-tax.gov.cn:443"),
            new CityInfo("4300", "湖南", "https://fpcy.hntax.gov.cn:8083"),
            new CityInfo("4400", "广东", "https://fpcy.gd-n-tax.gov.cn:443"),
            new CityInfo("4403", "深圳", "https://fpcy.szgs.gov.cn:443"),
            new CityInfo("4500", "广西", "https://fpcy.gxgs.gov.cn:8200"),
            new CityInfo("4600", "海南", "https://fpcy.hitax.gov.cn:443"),
            new CityInfo("5000", "重庆", "https://fpcy.cqsw.gov.cn:80"),
            new CityInfo("5100", "四川", "https://fpcy.sc-n-tax.gov.cn:443"),
            new CityInfo("5200", "贵州", "https://fpcy.gz-n-tax.gov.cn:80"),
            new CityInfo("5300", "云南", "https://fpcy.yngs.gov.cn:443"),
            new CityInfo("5400", "西藏", "https://fpcy.xztax.gov.cn:81"),
            new CityInfo("6100", "陕西", "https://fpcyweb.sn-n-tax.gov.cn:443"),
            new CityInfo("6200", "甘肃", "https://fpcy.gs-n-tax.gov.cn:443"),
            new CityInfo("6300", "青海", "https://fpcy.qh-n-tax.gov.cn:443"),
            new CityInfo("6400", "宁夏", "https://fpcy.nxgs.gov.cn:443"),
            new CityInfo("6500", "新疆", "https://fpcy.xj-n-tax.gov.cn:443")
    };

    URI getProvinceURL();

    default String getHostAddress(String code) {
        String provinceCode = "";
        if (code.length() == 12) {
            provinceCode = code.substring(1, 5);
        } else {
            provinceCode = code.substring(0, 4);
        }
        if (!provinceCode.equals("2102") && !provinceCode.equals("3302") && !provinceCode.equals("3502") && !provinceCode.equals("3702") && !provinceCode.equals("4403")) {
            provinceCode = provinceCode.substring(0, 2) + "00";
        }
        logger.info("[INFO]==========city code is " + provinceCode);
        for (CityInfo cityinfo :
                CITY_INFOS) {
            if (cityinfo.getCode().equals(provinceCode)) {
                return cityinfo.getUrl();
            }
        }
        logger.warning("[warning]==========no city's code match " + provinceCode);
        return "";
    }
}
