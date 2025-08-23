package de.hybris.platform.personalizationintegration.mapping.mapper.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.List;

public class CxTestSuffixMapper implements Populator<List<String>, MappingData>
{
    public void populate(List<String> data, MappingData target) throws ConversionException
    {
        data.stream().map(s -> s + "-test").forEach(s -> {
            SegmentMappingData segment = new SegmentMappingData();
            segment.setCode(s);
            target.getSegments().add(segment);
        });
    }
}
