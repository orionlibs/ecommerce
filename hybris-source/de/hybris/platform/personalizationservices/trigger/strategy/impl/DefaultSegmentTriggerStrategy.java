package de.hybris.platform.personalizationservices.trigger.strategy.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.trigger.dao.CxSegmentTriggerDao;
import de.hybris.platform.personalizationservices.trigger.strategy.CxTriggerStrategy;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSegmentTriggerStrategy implements CxTriggerStrategy
{
    private CxConfigurationService cxConfigurationService;
    private CxSegmentTriggerDao cxSegmentTriggerDao;
    private CxSegmentService cxSegmentService;


    public Collection<CxVariationModel> getVariations(UserModel user, CatalogVersionModel catalogVersion)
    {
        Collection<CxSegmentModel> segments = (Collection<CxSegmentModel>)this.cxSegmentService.getUserToSegmentForCalculation(user).stream().filter(this::affinityFilter).map(CxUserToSegmentModel::getSegment).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(segments))
        {
            return this.cxSegmentTriggerDao.findApplicableVariations(segments, catalogVersion);
        }
        return Collections.emptyList();
    }


    protected boolean affinityFilter(CxUserToSegmentModel u2s)
    {
        return (u2s.getAffinity().compareTo(getMinAffinity()) >= 0);
    }


    protected BigDecimal getMinAffinity()
    {
        return this.cxConfigurationService.getMinAffinity();
    }


    @Required
    public void setCxSegmentTriggerDao(CxSegmentTriggerDao cxSegmentTriggerDao)
    {
        this.cxSegmentTriggerDao = cxSegmentTriggerDao;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    protected CxSegmentTriggerDao getCxSegmentTriggerDao()
    {
        return this.cxSegmentTriggerDao;
    }


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }
}
