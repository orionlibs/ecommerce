package de.hybris.platform.personalizationservices.segment.converters;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CxUserSegmentConversionHelper
{
    private BaseSiteService baseSiteService;
    private CxSegmentDao cxSegmentDao;
    private Converter<CxUserToSegmentModel, UserToSegmentData> cxUserToSegmentConverter;
    private Converter<UserToSegmentData, CxUserToSegmentModel> cxUserToSegmentReverseConverter;


    public Collection<UserToSegmentData> convertToData(Collection<CxUserToSegmentModel> userToSegments)
    {
        return this.cxUserToSegmentConverter.convertAll(userToSegments);
    }


    public Collection<CxUserToSegmentModel> convertToModel(UserModel user, Collection<? extends UserToSegmentData> userToSegments)
    {
        return convertToModel(user, userToSegments, this.baseSiteService.getCurrentBaseSite());
    }


    public Collection<CxUserToSegmentModel> convertToModel(UserModel user, Collection<? extends UserToSegmentData> userToSegments, String baseSiteId)
    {
        return convertToModel(user, userToSegments, this.baseSiteService.getBaseSiteForUID(baseSiteId));
    }


    public Collection<CxUserToSegmentModel> convertToModel(UserModel user, Collection<? extends UserToSegmentData> userToSegments, BaseSiteModel baseSite)
    {
        Map<String, CxSegmentModel> segmentsModelMap = createSegmentModelMap(userToSegments);
        return (Collection<CxUserToSegmentModel>)userToSegments.stream()
                        .map(s -> convertDataToModel(user, s, segmentsModelMap, baseSite))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }


    protected Map<String, CxSegmentModel> createSegmentModelMap(Collection<? extends UserToSegmentData> userSegmentsData)
    {
        if(CollectionUtils.isNotEmpty(userSegmentsData))
        {
            Collection<CxSegmentModel> segmentsModel = this.cxSegmentDao.findSegmentsByCodes((Collection)userSegmentsData.stream().map(BaseSegmentData::getCode).collect(Collectors.toList()));
            return createValueMap(segmentsModel, CxSegmentModel::getCode, (a, b) -> b);
        }
        return Collections.emptyMap();
    }


    private static <K, V> Map<K, V> createValueMap(Collection<V> valueCollection, Function<? super V, ? extends K> keyMapper, BinaryOperator<V> mergeFunction)
    {
        return (Map<K, V>)valueCollection.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(keyMapper,
                                        (Function)Function.identity(), mergeFunction, java.util.HashMap::new));
    }


    protected CxUserToSegmentModel convertDataToModel(UserModel user, UserToSegmentData data, Map<String, CxSegmentModel> segmentsModelMap, BaseSiteModel baseSite)
    {
        if(segmentsModelMap.containsKey(data.getCode()))
        {
            CxUserToSegmentModel model = new CxUserToSegmentModel();
            model.setSegment(segmentsModelMap.get(data.getCode()));
            if(model.getSegment() != null)
            {
                model.getSegment().setDescription(data.getDescription());
            }
            model.setProvider(data.getProvider());
            model.setUser(user);
            model.setBaseSite(baseSite);
            this.cxUserToSegmentReverseConverter.convert(data, model);
            return model;
        }
        return null;
    }


    protected CxSegmentDao getCxSegmentDao()
    {
        return this.cxSegmentDao;
    }


    @Required
    public void setCxSegmentDao(CxSegmentDao cxSegmentDao)
    {
        this.cxSegmentDao = cxSegmentDao;
    }


    protected Converter<CxUserToSegmentModel, UserToSegmentData> getCxUserToSegmentConverter()
    {
        return this.cxUserToSegmentConverter;
    }


    @Required
    public void setCxUserToSegmentConverter(Converter<CxUserToSegmentModel, UserToSegmentData> cxUserToSegmentConverter)
    {
        this.cxUserToSegmentConverter = cxUserToSegmentConverter;
    }


    protected Converter<UserToSegmentData, CxUserToSegmentModel> getCxUserToSegmentReverseConverter()
    {
        return this.cxUserToSegmentReverseConverter;
    }


    @Required
    public void setCxUserToSegmentReverseConverter(Converter<UserToSegmentData, CxUserToSegmentModel> cxUserToSegmentReverseConverter)
    {
        this.cxUserToSegmentReverseConverter = cxUserToSegmentReverseConverter;
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
}
