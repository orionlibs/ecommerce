package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.couponwebservices.CouponNotFoundException;
import de.hybris.platform.couponwebservices.dto.CouponValidationResponseWsDTO;
import de.hybris.platform.couponwebservices.facades.GenericCouponWsFacades;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class DefaultGenericCouponWsFacades implements GenericCouponWsFacades
{
    private CouponService couponService;
    private UserService userService;
    private Converter<CouponResponse, CouponValidationResponseWsDTO> couponValidationResponseWsDTOConverter;


    public CouponValidationResponseWsDTO validateCoupon(String couponCode, String customerId)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "CouponCode cannot be empty");
        Optional<AbstractCouponModel> couponModel = getCouponService().getCouponForCode(couponCode);
        if(couponModel.isPresent())
        {
            UserModel customer = StringUtils.isNotEmpty(customerId) ? getUserService().getUserForUID(customerId) : null;
            CouponResponse couponResponse = getCouponService().validateCouponCode(couponCode, customer);
            CouponValidationResponseWsDTO couponValidationResponseWsDTO = (CouponValidationResponseWsDTO)getCouponValidationResponseWsDTOConverter().convert(couponResponse);
            couponValidationResponseWsDTO.setCouponId(((AbstractCouponModel)couponModel.get()).getCouponId());
            if(!couponCode.equals(((AbstractCouponModel)couponModel.get()).getCouponId()))
            {
                couponValidationResponseWsDTO.setGeneratedCouponCode(couponCode);
            }
            return couponValidationResponseWsDTO;
        }
        throw new CouponNotFoundException("No coupon found for coupon code [" + couponCode + "]");
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected Converter<CouponResponse, CouponValidationResponseWsDTO> getCouponValidationResponseWsDTOConverter()
    {
        return this.couponValidationResponseWsDTOConverter;
    }


    public void setCouponValidationResponseWsDTOConverter(Converter<CouponResponse, CouponValidationResponseWsDTO> couponValidationResponseWsDTOConverter)
    {
        this.couponValidationResponseWsDTOConverter = couponValidationResponseWsDTOConverter;
    }
}
