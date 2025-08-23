package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import de.hybris.platform.personalizationservices.enums.CxGroupingOperator;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxSegmentTriggerModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class SegmentTriggerReversePopulator implements Populator<SegmentTriggerData, CxSegmentTriggerModel>
{
    private EnumerationService enumerationService;
    private CxSegmentService cxSegmentService;


    public void populate(SegmentTriggerData source, CxSegmentTriggerModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setGroupBy((CxGroupingOperator)this.enumerationService.getEnumerationValue(CxGroupingOperator.class, source.getGroupBy()));
        setSegments(source, target);
    }


    protected void setSegments(SegmentTriggerData source, CxSegmentTriggerModel target)
    {
        if(CollectionUtils.isNotEmpty(source.getSegments()))
        {
            Set<CxSegmentModel> segmentList = (Set<CxSegmentModel>)source.getSegments().stream().filter(s -> (s != null)).map(SegmentData::getCode).map(this::getSegment).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
            target.setSegments(segmentList);
        }
        else
        {
            target.setSegments(Collections.emptyList());
        }
    }


    protected CxSegmentModel getSegment(String code)
    {
        return (CxSegmentModel)this.cxSegmentService.getSegment(code)
                        .orElseThrow(() -> new UnknownIdentifierException("Segment with code " + code + " does not exists"));
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }
}
