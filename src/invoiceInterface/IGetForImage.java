package invoiceInterface;

import datamodels.VerficationCodeInfo;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-28.
 */
public interface IGetForImage {
    byte[] getForImage(VerficationCodeInfo verficationCodeInfo);

    String getForImageSelectColor(VerficationCodeInfo verficationCodeInfo);
}
