package de.hybris.platform.personalizationintegration.mapping.mapper.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Required;

public class CxFixedSegmentsProfileMapper<T> implements Populator<T, MappingData>
{
    private static final String SEGMENTS_PROPERTY = "personalizationintegration.mappers.cxfixedsegmentsprofilemapper.segments";
    private ConfigurationService configurationService;


    public void populate(T source, MappingData target)
    {
        String[] segments = this.configurationService.getConfiguration().getStringArray("personalizationintegration.mappers.cxfixedsegmentsprofilemapper.segments");
        for(String segment : segments)
        {
            SegmentMappingData segmentData = new SegmentMappingData();
            segmentData.setCode(segment);
            target.getSegments().add(segmentData);
        }
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
