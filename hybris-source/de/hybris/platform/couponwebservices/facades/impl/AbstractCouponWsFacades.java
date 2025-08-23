package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.couponwebservices.CouponNotFoundException;
import de.hybris.platform.couponwebservices.InvalidCouponStateException;
import de.hybris.platform.couponwebservices.dto.AbstractCouponWsDTO;
import de.hybris.platform.couponwebservices.dto.CouponValidationResponseWsDTO;
import de.hybris.platform.couponwebservices.facades.CouponWsFacades;
import de.hybris.platform.couponwebservices.util.CouponWsUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.paginated.dao.PaginatedGenericDao;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.Cacheable;

public abstract class AbstractCouponWsFacades<D extends AbstractCouponWsDTO, M extends AbstractCouponModel> implements CouponWsFacades<D>
{
    private CouponDao couponDao;
    private CouponService couponService;
    private ModelService modelService;
    private UserService userService;
    private CouponWsUtils couponWsUtils;
    private Converter<CouponResponse, CouponValidationResponseWsDTO> couponValidationResponseWsDTOConverter;


    @Cacheable(value = {"couponWsCache"}, key = "T(de.hybris.platform.webservicescommons.cache.CacheKeyGenerator).generateKey(false,false,'getCouponWsDTO',#value)")
    public D getCouponWsDTO(String couponId)
    {
        AbstractCouponModel couponModel = getCouponDao().findCouponById(couponId);
        return convert(couponModel)
                        .<Throwable>orElseThrow(() -> new CouponNotFoundException("No coupon found for couponId [" + couponId + "]"));
    }


    public D createCoupon(D coupon)
    {
        ServicesUtil.validateParameterNotNull(coupon, "coupon data object cannot be empty");
        M couponModel = createCouponModel(coupon);
        getModelService().save(couponModel);
        return convert((AbstractCouponModel)couponModel).orElse(null);
    }


    public void updateCoupon(D coupon)
    {
        ServicesUtil.validateParameterNotNull(coupon, "coupon data object cannot be empty");
        M couponModel = updateCouponModel(coupon);
        getModelService().save(couponModel);
    }


    public void updateCouponStatus(String couponId, Boolean active)
    {
        ServicesUtil.validateParameterNotNull(couponId, "couponId cannot be empty");
        ServicesUtil.validateParameterNotNull(active, "active cannot be empty");
        AbstractCouponModel couponModel = getCouponWsUtils().getCouponById(couponId);
        assertCouponModelType(couponModel, couponId);
        if(!active.equals(couponModel.getActive()))
        {
            couponModel.setActive(active);
            getModelService().save(couponModel);
        }
    }


    public CouponValidationResponseWsDTO validateCoupon(String couponCode)
    {
        return validateCoupon(couponCode, "");
    }


    public CouponValidationResponseWsDTO validateCoupon(String couponCode, String customerId)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "couponId cannot be empty");
        String couponId = getCouponId(couponCode);
        AbstractCouponModel couponModel = getCouponWsUtils().getCouponById(couponId);
        assertCouponModelType(couponModel, couponId);
        UserModel customer = StringUtils.isNotEmpty(customerId) ? getUserService().getUserForUID(customerId) : null;
        CouponResponse couponResponse = getCouponService().validateCouponCode(couponCode, customer);
        CouponValidationResponseWsDTO couponValidationResponseWsDTO = (CouponValidationResponseWsDTO)getCouponValidationResponseWsDTOConverter().convert(couponResponse);
        couponValidationResponseWsDTO.setCouponId(couponId);
        if(!couponId.equals(couponCode))
        {
            couponValidationResponseWsDTO.setGeneratedCouponCode(couponCode);
        }
        return couponValidationResponseWsDTO;
    }


    protected <S extends AbstractCouponModel> SearchPageData<D> convertSearchPageData(SearchPageData<S> source)
    {
        SearchPageData<D> result = new SearchPageData();
        result.setPagination(source.getPagination());
        result.setSorts(source.getSorts());
        result.setResults((List)source.getResults().stream().map(this::convert).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()));
        return result;
    }


    protected <S extends AbstractCouponModel> SearchPageData<D> convertSearchPageData(SearchPageData<S> source)
    {
        SearchPageData<D> result = buildSearchPageData(source.getPagination(), source
                        .getSorts());
        result.setResults((List)source.getResults().stream().map(this::convert).map(Optional::get).collect(Collectors.toList()));
        return result;
    }


    protected void assertCouponNotActive(AbstractCouponModel couponModel, String message)
    {
        if(BooleanUtils.isTrue(couponModel.getActive()))
        {
            throw new InvalidCouponStateException(message, "active", "active");
        }
    }


    public SearchPageData getCoupons(PaginationData pagination, List<SortData> sorts)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("pagination", pagination);
        ServicesUtil.validateParameterNotNullStandardMessage("sorts", sorts);
        SearchPageData searchPageData = buildSearchPageData(pagination, sorts);
        SearchPageData<M> result = getCouponPaginatedGenericDao().find(searchPageData);
        return convertSearchPageData(result);
    }


    protected SearchPageData buildSearchPageData(PaginationData pagination, List<SortData> sorts)
    {
        SearchPageData searchPageData = new SearchPageData();
        searchPageData.setPagination(pagination);
        searchPageData.setSorts(sorts);
        return searchPageData;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
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


    protected CouponWsUtils getCouponWsUtils()
    {
        return this.couponWsUtils;
    }


    @Required
    public void setCouponWsUtils(CouponWsUtils couponWsUtils)
    {
        this.couponWsUtils = couponWsUtils;
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    @Required
    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected Converter<CouponResponse, CouponValidationResponseWsDTO> getCouponValidationResponseWsDTOConverter()
    {
        return this.couponValidationResponseWsDTOConverter;
    }


    @Required
    public void setCouponValidationResponseWsDTOConverter(Converter<CouponResponse, CouponValidationResponseWsDTO> couponValidationResponseWsDTOConverter)
    {
        this.couponValidationResponseWsDTOConverter = couponValidationResponseWsDTOConverter;
    }


    protected abstract void assertCouponModelType(AbstractCouponModel paramAbstractCouponModel, String paramString);


    protected abstract M updateCouponModel(D paramD);


    protected abstract Optional<D> convert(AbstractCouponModel paramAbstractCouponModel);


    protected abstract M createCouponModel(D paramD);


    protected abstract PaginatedGenericDao<M> getCouponPaginatedGenericDao();


    protected abstract String getCouponId(String paramString);
}
