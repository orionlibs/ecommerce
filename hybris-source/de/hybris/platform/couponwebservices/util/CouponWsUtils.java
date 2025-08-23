package de.hybris.platform.couponwebservices.util;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponwebservices.CouponNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class CouponWsUtils
{
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private CouponDao couponDao;


    public Function<String, Date> getStringToDateMapper()
    {
        return s -> Date.from(LocalDateTime.parse(s, DATE_TIME_FORMATTER).atZone(UTC_ZONE_ID).toInstant());
    }


    public Function<Date, String> getDateToStringMapper()
    {
        return d -> LocalDateTime.ofInstant(d.toInstant(), UTC_ZONE_ID).format(DATE_TIME_FORMATTER);
    }


    public void assertValidSingleCodeCoupon(AbstractCouponModel couponModel, String couponId)
    {
        if(Objects.isNull(couponModel) || !(couponModel instanceof de.hybris.platform.couponservices.model.SingleCodeCouponModel))
        {
            throw new CouponNotFoundException("No single code coupon was found for code [" + couponId + "]");
        }
    }


    public void assertValidMultiCodeCoupon(AbstractCouponModel couponModel, String couponId)
    {
        if(Objects.isNull(couponModel) || !(couponModel instanceof MultiCodeCouponModel))
        {
            throw new CouponNotFoundException("No multi-code coupon was found for code [" + couponId + "]");
        }
    }


    public MultiCodeCouponModel getValidMultiCodeCoupon(String couponId)
    {
        AbstractCouponModel abstractCouponModel = getCouponById(couponId);
        assertValidMultiCodeCoupon(abstractCouponModel, couponId);
        return (MultiCodeCouponModel)abstractCouponModel;
    }


    public AbstractCouponModel getCouponById(String couponId)
    {
        AbstractCouponModel abstractCouponModel = null;
        try
        {
            abstractCouponModel = getCouponDao().findCouponById(couponId);
        }
        catch(ModelNotFoundException ex)
        {
            throw new CouponNotFoundException("No coupon found for couponId [" + couponId + "]", "invalid", "couponId");
        }
        return abstractCouponModel;
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    @Required
    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }
}
