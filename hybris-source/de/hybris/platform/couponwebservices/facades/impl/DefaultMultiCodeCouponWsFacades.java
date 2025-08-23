package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.couponservices.dao.CodeGenerationConfigurationDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.couponwebservices.CodeGenerationConfigurationNotFoundException;
import de.hybris.platform.couponwebservices.CouponNotFoundException;
import de.hybris.platform.couponwebservices.CouponRequestWsError;
import de.hybris.platform.couponwebservices.InvalidCouponStateException;
import de.hybris.platform.couponwebservices.dto.MultiCodeCouponWsDTO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.paginated.dao.PaginatedGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMultiCodeCouponWsFacades extends AbstractCouponWsFacades<MultiCodeCouponWsDTO, MultiCodeCouponModel>
{
    private CodeGenerationConfigurationDao codeGenerationConfigurationDao;
    private PaginatedGenericDao<MultiCodeCouponModel> multiCodeCouponPaginatedGenericDao;
    private Converter<MultiCodeCouponModel, MultiCodeCouponWsDTO> multiCodeCouponWsDTOConverter;
    private CouponCodeGenerationService couponCodeGenerationService;
    private static final String REASONINVALID = "invalid";


    protected Optional<MultiCodeCouponWsDTO> convert(AbstractCouponModel couponModel)
    {
        Optional<MultiCodeCouponWsDTO> couponWsDTO = Optional.empty();
        if(couponModel instanceof MultiCodeCouponModel)
        {
            couponWsDTO = Optional.ofNullable((MultiCodeCouponWsDTO)getMultiCodeCouponWsDTOConverter().convert(couponModel));
        }
        return couponWsDTO;
    }


    protected MultiCodeCouponModel createCouponModel(MultiCodeCouponWsDTO couponDto)
    {
        MultiCodeCouponModel couponModel = (MultiCodeCouponModel)getModelService().create(MultiCodeCouponModel.class);
        couponModel.setCouponId(couponDto.getCouponId());
        Objects.requireNonNull(couponModel);
        Optional.<String>ofNullable(couponDto.getStartDate()).map(getCouponWsUtils().getStringToDateMapper()).ifPresent(couponModel::setStartDate);
        Objects.requireNonNull(couponModel);
        Optional.<String>ofNullable(couponDto.getEndDate()).map(getCouponWsUtils().getStringToDateMapper()).ifPresent(couponModel::setEndDate);
        couponModel.setName(couponDto.getName());
        Optional.<String>ofNullable(couponDto.getCodeGenerationConfiguration()).ifPresent(n -> {
            Objects.requireNonNull(couponModel);
            getCodeGenerationConfigurationDao().findCodeGenerationConfigurationByName(n).ifPresent(couponModel::setCodeGenerationConfiguration);
        });
        Objects.requireNonNull(couponModel);
        Optional.<Boolean>ofNullable(couponDto.getActive()).ifPresent(couponModel::setActive);
        return couponModel;
    }


    protected MultiCodeCouponModel updateCouponModel(MultiCodeCouponWsDTO couponDto)
    {
        ServicesUtil.validateParameterNotNull(couponDto.getCodeGenerationConfiguration(), "Code Generation Configuration cannot be empty");
        AbstractCouponModel couponModel = null;
        try
        {
            couponModel = getCouponDao().findCouponById(couponDto.getCouponId());
        }
        catch(ModelNotFoundException ex)
        {
            throw new CouponNotFoundException("No multi code coupon found for couponId [" + couponDto.getCouponId() + "]", "invalid", "couponId");
        }
        assertCouponNotActive(couponModel, "Can't update active coupon");
        getCouponWsUtils().assertValidMultiCodeCoupon(couponModel, couponDto.getCouponId());
        MultiCodeCouponModel multiCodeCouponModel = (MultiCodeCouponModel)couponModel;
        multiCodeCouponModel
                        .setStartDate(Optional.<String>ofNullable(couponDto.getStartDate()).map(getCouponWsUtils().getStringToDateMapper()).orElse(null));
        multiCodeCouponModel
                        .setEndDate(Optional.<String>ofNullable(couponDto.getEndDate()).map(getCouponWsUtils().getStringToDateMapper()).orElse(null));
        multiCodeCouponModel.setName(couponDto.getName());
        if(CollectionUtils.isNotEmpty(multiCodeCouponModel.getGeneratedCodes()))
        {
            throw new InvalidCouponStateException("Multi code coupon already has generated codes for current Code Generation Configuration", "invalid", "generatedCodes");
        }
        CodeGenerationConfigurationModel codeGenerationConfiguration = (CodeGenerationConfigurationModel)this.codeGenerationConfigurationDao.findCodeGenerationConfigurationByName(couponDto.getCodeGenerationConfiguration())
                        .orElseThrow(() -> new CodeGenerationConfigurationNotFoundException("No Code Generation Configuration found for name [" + couponDto.getCodeGenerationConfiguration() + "]", "invalid", "codeGenerationConfiguration"));
        multiCodeCouponModel.setCodeGenerationConfiguration(codeGenerationConfiguration);
        return multiCodeCouponModel;
    }


    protected void assertCouponModelType(AbstractCouponModel couponModel, String couponId)
    {
        getCouponWsUtils().assertValidMultiCodeCoupon(couponModel, couponId);
    }


    protected String getCouponId(String couponCode)
    {
        String couponId = getCouponCodeGenerationService().extractCouponPrefix(couponCode);
        if(StringUtils.isEmpty(couponId))
        {
            throw new CouponRequestWsError("The generated multi code coupon provided for validation is not valid[" + couponCode + "]", "invalid", "Invalid arguments in the request");
        }
        return couponId;
    }


    protected CodeGenerationConfigurationDao getCodeGenerationConfigurationDao()
    {
        return this.codeGenerationConfigurationDao;
    }


    @Required
    public void setCodeGenerationConfigurationDao(CodeGenerationConfigurationDao codeGenerationConfigurationDao)
    {
        this.codeGenerationConfigurationDao = codeGenerationConfigurationDao;
    }


    protected Converter<MultiCodeCouponModel, MultiCodeCouponWsDTO> getMultiCodeCouponWsDTOConverter()
    {
        return this.multiCodeCouponWsDTOConverter;
    }


    @Required
    public void setMultiCodeCouponWsDTOConverter(Converter<MultiCodeCouponModel, MultiCodeCouponWsDTO> multiCodeCouponWsDTOConverter)
    {
        this.multiCodeCouponWsDTOConverter = multiCodeCouponWsDTOConverter;
    }


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
    }


    protected PaginatedGenericDao<MultiCodeCouponModel> getMultiCodeCouponPaginatedGenericDao()
    {
        return this.multiCodeCouponPaginatedGenericDao;
    }


    @Required
    public void setMultiCodeCouponPaginatedGenericDao(PaginatedGenericDao<MultiCodeCouponModel> multiCodeCouponPaginatedGenericDao)
    {
        this.multiCodeCouponPaginatedGenericDao = multiCodeCouponPaginatedGenericDao;
    }


    protected PaginatedGenericDao<MultiCodeCouponModel> getCouponPaginatedGenericDao()
    {
        return getMultiCodeCouponPaginatedGenericDao();
    }
}
