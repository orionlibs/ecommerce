package de.hybris.platform.commercefacades.coupon.data;

import java.io.Serializable;
import java.util.Date;

public class CouponData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String couponCode;
    private String couponId;
    private String name;
    private boolean active;
    private Date startDate;
    private Date endDate;


    public void setCouponCode(String couponCode)
    {
        this.couponCode = couponCode;
    }


    public String getCouponCode()
    {
        return this.couponCode;
    }


    public void setCouponId(String couponId)
    {
        this.couponId = couponId;
    }


    public String getCouponId()
    {
        return this.couponId;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    public Date getStartDate()
    {
        return this.startDate;
    }


    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    public Date getEndDate()
    {
        return this.endDate;
    }
}
