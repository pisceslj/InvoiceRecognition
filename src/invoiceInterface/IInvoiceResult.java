package invoiceInterface;

import datamodels.InvoiceInfo;
import datamodels.InvoiceResult;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */
public interface IInvoiceResult {
    InvoiceResult getInvoiceResult(InvoiceInfo invoiceInfo);
}
