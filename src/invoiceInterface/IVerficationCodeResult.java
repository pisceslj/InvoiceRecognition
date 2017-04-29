package invoiceInterface;

import datamodels.VerficationCodeInfo;
import datamodels.VerficationCodeResult;

/**
 * Created by mac on 2017/3/27.
 */
public interface IVerficationCodeResult {
    VerficationCodeResult VerficationCodeResultConvert(VerficationCodeInfo verficationCodeInfo, String result);
}
