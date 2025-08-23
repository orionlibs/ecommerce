package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.couponwebservices.CouponNotFoundException;
import de.hybris.platform.couponwebservices.dto.SingleCodeCouponWsDTO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.paginated.dao.PaginatedGenericDao;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSingleCodeCouponWsFacades extends AbstractCouponWsFacades<SingleCodeCouponWsDTO, SingleCodeCouponModel>
{
    private PaginatedGenericDao<SingleCodeCouponModel> singleCodeCouponPaginatedGenericDao;
    private Converter<SingleCodeCouponModel, SingleCodeCouponWsDTO> singleCodeCouponWsDTOConverter;


    protected Optional<SingleCodeCouponWsDTO> convert(AbstractCouponModel couponModel)
    {
        Optional<SingleCodeCouponWsDTO> couponWsDTO = Optional.empty();
        if(couponModel instanceof SingleCodeCouponModel)
        {
            couponWsDTO = Optional.ofNullable((SingleCodeCouponWsDTO)getSingleCodeCouponWsDTOConverter().convert(couponModel));
        }
        return couponWsDTO;
    }


    protected SingleCodeCouponModel createCouponModel(SingleCodeCouponWsDTO couponDto)
    {
        SingleCodeCouponModel couponModel = (SingleCodeCouponModel)getModelService().create(SingleCodeCouponModel.class);
        couponModel.setCouponId(couponDto.getCouponId());
        Objects.requireNonNull(couponModel);
        Optional.<String>ofNullable(couponDto.getStartDate()).map(getCouponWsUtils().getStringToDateMapper()).ifPresent(couponModel::setStartDate);
        Objects.requireNonNull(couponModel);
        Optional.<String>ofNullable(couponDto.getEndDate()).map(getCouponWsUtils().getStringToDateMapper()).ifPresent(couponModel::setEndDate);
        Objects.requireNonNull(couponModel);
        Optional.<Boolean>ofNullable(couponDto.getActive()).ifPresent(couponModel::setActive);
        couponModel.setName(couponDto.getName());
        couponModel.setMaxRedemptionsPerCustomer(couponDto.getMaxRedemptionsPerCustomer());
        couponModel.setMaxTotalRedemptions(couponDto.getMaxTotalRedemptions());
        return couponModel;
    }


    protected SingleCodeCouponModel updateCouponModel(SingleCodeCouponWsDTO couponDto)
    {
        AbstractCouponModel couponModel = null;
        try
        {
            couponModel = getCouponDao().findCouponById(couponDto.getCouponId());
        }
        catch(ModelNotFoundException ex)
        {
            throw new CouponNotFoundException("No single code coupon found for couponId [" + couponDto.getCouponId() + "]", "invalid", "couponId");
        }
        assertCouponNotActive(couponModel, "Can't update active coupon");
        assertCouponModelType(couponModel, couponDto.getCouponId());
        SingleCodeCouponModel singleCodeCouponModel = (SingleCodeCouponModel)couponModel;
        singleCodeCouponModel
                        .setStartDate(Optional.<String>ofNullable(couponDto.getStartDate()).map(getCouponWsUtils().getStringToDateMapper()).orElse(null));
        singleCodeCouponModel
                        .setEndDate(Optional.<String>ofNullable(couponDto.getEndDate()).map(getCouponWsUtils().getStringToDateMapper()).orElse(null));
        singleCodeCouponModel.setName(couponDto.getName());
        singleCodeCouponModel.setMaxRedemptionsPerCustomer(couponDto.getMaxRedemptionsPerCustomer());
        singleCodeCouponModel.setMaxTotalRedemptions(couponDto.getMaxTotalRedemptions());
        return singleCodeCouponModel;
    }


    protected void assertCouponModelType(AbstractCouponModel couponModel, String couponId)
    {
        getCouponWsUtils().assertValidSingleCodeCoupon(couponModel, couponId);
    }


    protected String getCouponId(String couponCode)
    {
        return couponCode;
    }


    protected Converter<SingleCodeCouponModel, SingleCodeCouponWsDTO> getSingleCodeCouponWsDTOConverter()
    {
        return this.singleCodeCouponWsDTOConverter;
    }


    @Required
    public void setSingleCodeCouponWsDTOConverter(Converter<SingleCodeCouponModel, SingleCodeCouponWsDTO> singleCodeCouponWsDTOConverter)
    {
        this.singleCodeCouponWsDTOConverter = singleCodeCouponWsDTOConverter;
    }


    protected PaginatedGenericDao<SingleCodeCouponModel> getSingleCodeCouponPaginatedGenericDao()
    {
        return this.singleCodeCouponPaginatedGenericDao;
    }


    @Required
    public void setSingleCodeCouponPaginatedGenericDao(PaginatedGenericDao<SingleCodeCouponModel> singleCodeCouponPaginatedGenericDao)
    {
        this.singleCodeCouponPaginatedGenericDao = singleCodeCouponPaginatedGenericDao;
    }


    protected PaginatedGenericDao<SingleCodeCouponModel> getCouponPaginatedGenericDao()
    {
        return getSingleCodeCouponPaginatedGenericDao();
    }
}
