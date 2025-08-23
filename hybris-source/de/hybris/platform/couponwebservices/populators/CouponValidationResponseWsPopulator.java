package de.hybris.platform.couponwebservices.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponwebservices.dto.CouponValidationResponseWsDTO;
import de.hybris.platform.util.localization.Localization;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

public class CouponValidationResponseWsPopulator implements Populator<CouponResponse, CouponValidationResponseWsDTO>
{
    public void populate(CouponResponse source, CouponValidationResponseWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCouponId(source.getCouponId());
        target.setValid(source.getSuccess());
        if(StringUtils.isNotEmpty(source.getMessage()))
        {
            target.setMessage(Localization.getLocalizedString(source.getMessage()));
        }
    }
}
