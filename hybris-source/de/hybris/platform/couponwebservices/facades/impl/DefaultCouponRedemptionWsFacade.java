package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.couponwebservices.CouponNotFoundException;
import de.hybris.platform.couponwebservices.dto.CouponRedemptionWsDTO;
import de.hybris.platform.couponwebservices.facades.CouponRedemptionWsFacade;
import de.hybris.platform.ruleengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultCouponRedemptionWsFacade implements CouponRedemptionWsFacade
{
    private CouponDao couponDao;
    private CouponRedemptionDao couponRedemptionDao;
    private UserService userService;
    private Converter<SingleCodeCouponModel, CouponRedemptionWsDTO> couponRedemptionWsDTOConverter;
    private ExtendedOrderDao extendedOrderDao;
    private CouponService couponService;
    private ModelService modelService;


    public CouponRedemptionWsDTO getSingleCodeCouponRedemption(String couponId, String customerId)
    {
        AbstractCouponModel abstractCouponModel = null;
        try
        {
            abstractCouponModel = getCouponDao().findCouponById(couponId);
        }
        catch(ModelNotFoundException ex)
        {
            throw new CouponNotFoundException("No single code coupon found for invalid couponId [" + couponId + "]");
        }
        assertSingleCodeCoupon(abstractCouponModel, couponId);
        CouponRedemptionWsDTO couponRedemptionWsDTO = (CouponRedemptionWsDTO)getCouponRedemptionWsDTOConverter().convert(abstractCouponModel);
        if(StringUtils.isNotEmpty(customerId))
        {
            UserModel customer = getUserService().getUserForUID(customerId);
            List<CouponRedemptionModel> couponRedemptionList = getCouponRedemptionDao().findCouponRedemptionsByCodeAndUser(couponId, customer);
            couponRedemptionWsDTO.setCustomerId(customerId);
            couponRedemptionWsDTO.setRedemptionsPerCustomer(Integer.valueOf(couponRedemptionList.size()));
        }
        return couponRedemptionWsDTO;
    }


    public CouponRedemptionWsDTO createCouponRedemption(CouponRedemptionWsDTO couponRedemptionWsDTO)
    {
        if(Objects.isNull(couponRedemptionWsDTO.getOrderCode()) || couponRedemptionWsDTO.getOrderCode().isEmpty())
        {
            Optional<AbstractCouponModel> couponModel = getCouponService().getValidatedCouponForCode(couponRedemptionWsDTO
                            .getCouponCode());
            if(couponModel.isPresent())
            {
                CouponRedemptionModel couponRedemption = (CouponRedemptionModel)getModelService().create(CouponRedemptionModel.class);
                couponRedemption.setCoupon(couponModel.get());
                couponRedemption.setCouponCode(couponRedemptionWsDTO.getCouponCode());
                getModelService().save(couponRedemption);
                return populateCouponRedemptionWsDTO(couponRedemption);
            }
            throw new CouponNotFoundException("No valid coupon found for coupon code [" + couponRedemptionWsDTO
                            .getCouponCode() + "]");
        }
        OrderModel orderModel = (OrderModel)getExtendedOrderDao().findOrderByCode(couponRedemptionWsDTO.getOrderCode());
        CouponResponse couponResponse = getCouponService().redeemCoupon(couponRedemptionWsDTO.getCouponCode(), orderModel);
        if(BooleanUtils.isTrue(couponResponse.getSuccess()))
        {
            List<CouponRedemptionModel> couponRedemptions = getCouponRedemptionDao().findCouponRedemptionsByCodeOrderAndUser(couponRedemptionWsDTO
                            .getCouponCode(), (AbstractOrderModel)orderModel, orderModel.getUser());
            List<CouponRedemptionModel> sortedList = (List<CouponRedemptionModel>)couponRedemptions.stream().sorted(Comparator.comparing(ItemModel::getCreationtime, Comparator.reverseOrder())).collect(
                            Collectors.toList());
            return populateCouponRedemptionWsDTO(sortedList.get(0));
        }
        throw new CouponServiceException(couponResponse.getMessage());
    }


    protected CouponRedemptionWsDTO populateCouponRedemptionWsDTO(CouponRedemptionModel redemptionModel)
    {
        CouponRedemptionWsDTO responseBody = new CouponRedemptionWsDTO();
        responseBody.setCouponCode(redemptionModel.getCouponCode());
        responseBody.setCouponId(redemptionModel.getCoupon().getCouponId());
        responseBody.setOrderCode((redemptionModel.getOrder() == null) ? null : redemptionModel.getOrder().getCode());
        responseBody.setCustomerId((redemptionModel.getUser() == null) ? null : redemptionModel.getUser().getUid());
        return responseBody;
    }


    protected void assertSingleCodeCoupon(AbstractCouponModel couponModel, String couponId)
    {
        if(Objects.isNull(couponModel) || !(couponModel instanceof SingleCodeCouponModel))
        {
            throw new CouponNotFoundException("No single code coupon was found for code [" + couponId + "]");
        }
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }


    protected Converter<SingleCodeCouponModel, CouponRedemptionWsDTO> getCouponRedemptionWsDTOConverter()
    {
        return this.couponRedemptionWsDTOConverter;
    }


    public void setCouponRedemptionWsDTOConverter(Converter<SingleCodeCouponModel, CouponRedemptionWsDTO> couponRedemptionWsDTOConverter)
    {
        this.couponRedemptionWsDTOConverter = couponRedemptionWsDTOConverter;
    }


    protected CouponRedemptionDao getCouponRedemptionDao()
    {
        return this.couponRedemptionDao;
    }


    public void setCouponRedemptionDao(CouponRedemptionDao couponRedemptionDao)
    {
        this.couponRedemptionDao = couponRedemptionDao;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }


    protected ExtendedOrderDao getExtendedOrderDao()
    {
        return this.extendedOrderDao;
    }


    public void setExtendedOrderDao(ExtendedOrderDao extendedOrderDao)
    {
        this.extendedOrderDao = extendedOrderDao;
    }
}
