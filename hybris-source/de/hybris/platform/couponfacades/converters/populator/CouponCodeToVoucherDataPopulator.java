package de.hybris.platform.couponfacades.converters.populator;

import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class CouponCodeToVoucherDataPopulator implements Populator<String, VoucherData>
{
    public void populate(String source, VoucherData target)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("source", source);
        ServicesUtil.validateParameterNotNullStandardMessage("target", target);
        target.setCode(source);
    }
}
