package de.hybris.platform.personalizationfacades.customersegmentation.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.AbstractBaseFacade;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.customersegmentation.CustomerSegmentationFacade;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.enums.CustomerSegmentationConversionOptions;
import de.hybris.platform.personalizationfacades.enums.SegmentConversionOptions;
import de.hybris.platform.personalizationfacades.segmentation.SegmentationHelper;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultCustomerSegmentationFacade extends AbstractBaseFacade implements CustomerSegmentationFacade
{
    private static final String SEGMENTATION = "CustomerSegmentation";
    private static final Logger LOG = Logger.getLogger(DefaultCustomerSegmentationFacade.class);
    private CxSegmentService segmentService;
    private CxConfigurationService cxConfigurationService;
    private UserService userService;
    private BaseSiteService baseSiteService;
    private ConfigurableConverter<CxUserToSegmentModel, CustomerSegmentationData, CustomerSegmentationConversionOptions> segmentationConverter;
    private Converter<CustomerSegmentationData, CxUserToSegmentModel> segmentationReverseConverter;
    private SegmentationHelper segmentationHelper;
    private ConfigurableConverter<CxSegmentModel, SegmentData, SegmentConversionOptions> segmentConverter;


    public List<SegmentData> getSegmentsForCurrentUser()
    {
        UserModel user = this.userService.getCurrentUser();
        Collection<CxUserToSegmentModel> userToSegment = this.segmentService.getUserToSegmentForCalculation(user);
        Objects.requireNonNull(this.segmentConverter);
        List<SegmentData> result = (List<SegmentData>)userToSegment.stream().filter(this::affinityFilter).map(CxUserToSegmentModel::getSegment).map(this.segmentConverter::convert).collect(Collectors.toList());
        return result;
    }


    protected boolean affinityFilter(CxUserToSegmentModel u2s)
    {
        return (u2s.getAffinity().compareTo(getMinAffinity()) >= 0);
    }


    protected BigDecimal getMinAffinity()
    {
        return this.cxConfigurationService.getMinAffinity();
    }


    public CustomerSegmentationData getCustomerSegmentation(String id)
    {
        CxUserToSegmentModel segmentation = getUserToSegment(id).<Throwable>orElseThrow(() -> createUnknownIdentifierException("CustomerSegmentation", id));
        return createCustomizationSegmentationData(segmentation, new CustomerSegmentationConversionOptions[] {CustomerSegmentationConversionOptions.FULL});
    }


    public void deleteCustomerSegmentation(String id)
    {
        Optional<CxUserToSegmentModel> segmentation = getUserToSegment(id);
        Objects.requireNonNull(getModelService());
        segmentation.ifPresent(getModelService()::remove);
        segmentation.orElseThrow(() -> createUnknownIdentifierException("CustomerSegmentation", id));
    }


    protected Optional<CxUserToSegmentModel> getUserToSegment(String id)
    {
        String[] codes = this.segmentationHelper.splitCustomerSegmentationCode(id);
        CxSegmentModel segment = (CxSegmentModel)this.segmentService.getSegment(codes[0]).orElseThrow(() -> createUnknownIdentifierException("CustomerSegmentation", id));
        UserModel user = (UserModel)Optional.<UserModel>ofNullable(getUser(codes[1])).orElseThrow(() -> createUnknownIdentifierException("CustomerSegmentation", id));
        String baseSiteId = codes[2];
        BaseSiteModel baseSite = null;
        if(baseSiteId != null)
        {
            baseSite = (BaseSiteModel)Optional.<BaseSiteModel>ofNullable(getBaseSite(baseSiteId)).orElseThrow(() -> createUnknownIdentifierException("CustomerSegmentation", id));
        }
        String provider = codes[3];
        return getUserToSegmentExact(user, segment, baseSite, provider);
    }


    protected Optional<CxUserToSegmentModel> getUserToSegmentExact(UserModel user, CxSegmentModel segment, BaseSiteModel baseSite, String provider)
    {
        int pageSize = 100;
        int page = 0;
        boolean nextPage = true;
        while(true)
        {
            SearchPageData<Object> pagination = getSearchPage(page, 100);
            SearchPageData<CxUserToSegmentModel> searchPage = this.segmentService.getUserToSegmentModel(user, segment, baseSite, pagination);
            Optional<CxUserToSegmentModel> any = searchPage.getResults().stream().filter(s -> (Objects.equals(s.getBaseSite(), baseSite) && Objects.equals(s.getProvider(), provider))).findAny();
            if(any.isPresent())
            {
                return any;
            }
            page++;
            nextPage = (searchPage.getPagination().getNumberOfPages() > page);
            if(!nextPage)
            {
                return Optional.empty();
            }
        }
    }


    protected <T> SearchPageData<T> getSearchPage(int page, int pageSize)
    {
        SearchPageData<T> result = getEmptySearchData();
        result.getPagination().setPageSize(pageSize);
        result.getPagination().setCurrentPage(page);
        result.getPagination().setNeedsTotal(true);
        return result;
    }


    public SearchPageData<CustomerSegmentationData> getCustomerSegmentations(String customerId, String segmentId, String baseSiteId, SearchPageData<?> pagination)
    {
        if(customerId == null && segmentId == null)
        {
            throw new IllegalArgumentException("at least one parameter has to have not null value");
        }
        CxSegmentModel segment = getSegment(segmentId);
        if(segment == null && StringUtils.isNotEmpty(segmentId))
        {
            return getEmptySearchData();
        }
        UserModel user = getUser(customerId);
        if(user == null && StringUtils.isNotEmpty(customerId))
        {
            return getEmptySearchData();
        }
        BaseSiteModel baseSite = getBaseSite(baseSiteId);
        if(baseSite == null && StringUtils.isNotEmpty(baseSiteId))
        {
            return getEmptySearchData();
        }
        SearchPageData<CxUserToSegmentModel> segmentations = this.segmentService.getUserToSegmentModel(user, segment, baseSite, pagination);
        return convertCustomerSegmentationList(customerId, segmentId, segmentations);
    }


    protected SearchPageData<CustomerSegmentationData> convertCustomerSegmentationList(String customerId, String segmentId, SearchPageData<CxUserToSegmentModel> segmentations)
    {
        if(customerId != null && segmentId != null)
        {
            return convertSearchPage(segmentations, s -> this.segmentationConverter.convertAll(s, (Object[])new CustomerSegmentationConversionOptions[] {CustomerSegmentationConversionOptions.FULL}));
        }
        if(customerId != null)
        {
            return convertSearchPage(segmentations, s -> this.segmentationConverter.convertAll(s, (Object[])new CustomerSegmentationConversionOptions[] {CustomerSegmentationConversionOptions.FOR_CUSTOMER}));
        }
        return convertSearchPage(segmentations, s -> this.segmentationConverter.convertAll(s, (Object[])new CustomerSegmentationConversionOptions[] {CustomerSegmentationConversionOptions.FOR_SEGMENT}));
    }


    public CustomerSegmentationData createCustomerSegmentation(CustomerSegmentationData segmentation)
    {
        BaseSiteModel baseSite;
        Assert.notNull(segmentation.getCustomer(), "customer cannot be null");
        Assert.notNull(segmentation.getSegment(), "segment cannot be null");
        String customerCode = segmentation.getCustomer().getUid();
        String segmentCode = segmentation.getSegment().getCode();
        String baseSiteId = segmentation.getBaseSite();
        String provider = segmentation.getProvider();
        CxSegmentModel segment = (CxSegmentModel)this.segmentService.getSegment(segmentCode).orElseThrow(() -> createUnknownIdentifierException("Segment", segmentCode));
        UserModel user = (UserModel)Optional.<UserModel>ofNullable(getUser(customerCode)).orElseThrow(() -> createUnknownIdentifierException("Customer", customerCode));
        if(baseSiteId != null)
        {
            baseSite = (BaseSiteModel)Optional.<BaseSiteModel>ofNullable(getBaseSite(baseSiteId)).orElseThrow(() -> createUnknownIdentifierException("BaseSite", baseSiteId));
        }
        else
        {
            baseSite = null;
        }
        getUserToSegmentExact(user, segment, baseSite, provider).ifPresent(e -> throwAlreadyExists("CustomerSegmentation", this.segmentationHelper.getSegmentationCode(new String[] {customerCode, segmentCode, baseSiteId, provider})));
        CxUserToSegmentModel model = (CxUserToSegmentModel)this.segmentationReverseConverter.convert(segmentation);
        model.setUser(user);
        model.setSegment(segment);
        model.setBaseSite(baseSite);
        model.setProvider(provider);
        getModelService().save(model);
        getModelService().refresh(user);
        getModelService().refresh(segment);
        return createCustomizationSegmentationData(model, new CustomerSegmentationConversionOptions[] {CustomerSegmentationConversionOptions.BASE});
    }


    public CustomerSegmentationData updateCustomerSegmentation(CustomerSegmentationData segmentation)
    {
        String code = segmentation.getCode();
        CxUserToSegmentModel model = getUserToSegment(code).<Throwable>orElseThrow(() -> createUnknownIdentifierException("CustomerSegmentation", code));
        this.segmentationReverseConverter.convert(segmentation, model);
        getModelService().save(model);
        return createCustomizationSegmentationData(model, new CustomerSegmentationConversionOptions[] {CustomerSegmentationConversionOptions.BASE});
    }


    protected CustomerSegmentationData createCustomizationSegmentationData(CxUserToSegmentModel model, CustomerSegmentationConversionOptions... options)
    {
        return (CustomerSegmentationData)this.segmentationConverter.convert(model, Arrays.asList(options));
    }


    protected BaseSiteModel getBaseSite(String baseSite)
    {
        if(baseSite != null)
        {
            return this.baseSiteService.getBaseSiteForUID(baseSite);
        }
        return null;
    }


    protected CxSegmentModel getSegment(String id)
    {
        Objects.requireNonNull(this.segmentService);
        return Optional.<String>ofNullable(id).flatMap(this.segmentService::getSegment).orElse(null);
    }


    protected UserModel getUser(String id)
    {
        if(id == null)
        {
            return null;
        }
        try
        {
            return this.userService.getUserForUID(id);
        }
        catch(UnknownIdentifierException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException e)
        {
            LOG.error("Invalid user id", (Throwable)e);
            return null;
        }
    }


    protected SegmentationHelper getSegmentationHelper()
    {
        return this.segmentationHelper;
    }


    @Required
    public void setSegmentationHelper(SegmentationHelper segmentationHelper)
    {
        this.segmentationHelper = segmentationHelper;
    }


    protected CxSegmentService getSegmentService()
    {
        return this.segmentService;
    }


    @Required
    public void setSegmentService(CxSegmentService segmentService)
    {
        this.segmentService = segmentService;
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


    protected ConfigurableConverter<CxUserToSegmentModel, CustomerSegmentationData, CustomerSegmentationConversionOptions> getSegmentationConverter()
    {
        return this.segmentationConverter;
    }


    @Required
    public void setSegmentationConverter(ConfigurableConverter<CxUserToSegmentModel, CustomerSegmentationData, CustomerSegmentationConversionOptions> segmentationConverter)
    {
        this.segmentationConverter = segmentationConverter;
    }


    protected Converter<CustomerSegmentationData, CxUserToSegmentModel> getSegmentationReverseConverter()
    {
        return this.segmentationReverseConverter;
    }


    @Required
    public void setSegmentationReverseConverter(Converter<CustomerSegmentationData, CxUserToSegmentModel> segmentationReverseConverter)
    {
        this.segmentationReverseConverter = segmentationReverseConverter;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected ConfigurableConverter<CxSegmentModel, SegmentData, SegmentConversionOptions> getSegmentConverter()
    {
        return this.segmentConverter;
    }


    @Required
    public void setSegmentConverter(ConfigurableConverter<CxSegmentModel, SegmentData, SegmentConversionOptions> segmentConverter)
    {
        this.segmentConverter = segmentConverter;
    }


    public CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }
}
