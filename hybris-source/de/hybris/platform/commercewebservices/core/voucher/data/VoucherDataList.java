package de.hybris.platform.commercewebservices.core.voucher.data;

import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import java.io.Serializable;
import java.util.List;

public class VoucherDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<VoucherData> vouchers;


    public void setVouchers(List<VoucherData> vouchers)
    {
        this.vouchers = vouchers;
    }


    public List<VoucherData> getVouchers()
    {
        return this.vouchers;
    }
}
