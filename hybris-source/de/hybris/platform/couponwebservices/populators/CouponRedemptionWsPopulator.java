package de.hybris.platform.couponwebservices.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.couponwebservices.dto.CouponRedemptionWsDTO;
import java.util.List;
import org.springframework.util.Assert;

public class CouponRedemptionWsPopulator implements Populator<SingleCodeCouponModel, CouponRedemptionWsDTO>
{
    private CouponRedemptionDao couponRedemptionDao;


    public void populate(SingleCodeCouponModel source, CouponRedemptionWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCouponId(source.getCouponId());
        target.setMaxRedemptionsLimitPerCustomer(source.getMaxRedemptionsPerCustomer());
        target.setMaxTotalRedemptionsLimit(source.getMaxTotalRedemptions());
        List<CouponRedemptionModel> totalCouponRedemptions = getCouponRedemptionDao().findCouponRedemptionsByCode(source.getCouponId());
        target.setTotalRedemptions(Integer.valueOf(totalCouponRedemptions.size()));
    }


    protected CouponRedemptionDao getCouponRedemptionDao()
    {
        return this.couponRedemptionDao;
    }


    public void setCouponRedemptionDao(CouponRedemptionDao couponRedemptionDao)
    {
        this.couponRedemptionDao = couponRedemptionDao;
    }
}
