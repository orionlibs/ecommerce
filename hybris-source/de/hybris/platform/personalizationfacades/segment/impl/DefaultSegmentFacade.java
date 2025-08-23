package de.hybris.platform.personalizationfacades.segment.impl;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.AbstractBaseFacade;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.enums.SegmentConversionOptions;
import de.hybris.platform.personalizationfacades.segment.SegmentFacade;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultSegmentFacade extends AbstractBaseFacade implements SegmentFacade
{
    private static final String SEGMENT = "Segment";
    private CxSegmentService segmentService;
    private ConfigurableConverter<CxSegmentModel, SegmentData, SegmentConversionOptions> segmentConverter;
    private Converter<SegmentData, CxSegmentModel> segmentReverseConverter;


    public SearchPageData<SegmentData> getSegments(Map<String, String> filters, SearchPageData<?> pagination)
    {
        SearchPageData<CxSegmentModel> segments = this.segmentService.getSegments(filters, pagination);
        return convertSearchPage(segments, s -> this.segmentConverter.convertAll(s, (Object[])new SegmentConversionOptions[] {SegmentConversionOptions.FULL}));
    }


    public SegmentData getSegment(String segmentCode)
    {
        validateCode("Segment", segmentCode);
        Optional<CxSegmentModel> segmentModel = this.segmentService.getSegment(segmentCode);
        CxSegmentModel segment = segmentModel.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Segment", segmentCode));
        return (SegmentData)this.segmentConverter.convert(segment, Collections.singletonList(SegmentConversionOptions.FULL));
    }


    public SegmentData createSegment(SegmentData segment)
    {
        Assert.notNull(segment, "Segment data cannot be null");
        validateCode("Segment", segment.getCode());
        Optional<CxSegmentModel> existingSegment = this.segmentService.getSegment(segment.getCode());
        if(existingSegment.isPresent())
        {
            throwAlreadyExists("Segment", segment.getCode());
        }
        CxSegmentModel model = (CxSegmentModel)this.segmentReverseConverter.convert(segment);
        getModelService().save(model);
        return (SegmentData)this.segmentConverter.convert(model);
    }


    public SegmentData updateSegment(String segmentCode, SegmentData segment)
    {
        validateCode("Segment", segmentCode);
        Assert.notNull(segment, "Segment data cannot be null");
        segment.setCode(segmentCode);
        Optional<CxSegmentModel> existingSegmentModel = this.segmentService.getSegment(segmentCode);
        CxSegmentModel existingSegment = existingSegmentModel.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Segment", segmentCode));
        CxSegmentModel model = (CxSegmentModel)this.segmentReverseConverter.convert(segment, existingSegment);
        getModelService().save(model);
        return (SegmentData)this.segmentConverter.convert(model);
    }


    public void deleteSegment(String segmentCode)
    {
        validateCode("Segment", segmentCode);
        Optional<CxSegmentModel> segment = this.segmentService.getSegment(segmentCode);
        getModelService().remove(segment.orElseThrow(() -> createUnknownIdentifierException("Segment", segmentCode)));
    }


    @Required
    public void setSegmentConverter(ConfigurableConverter<CxSegmentModel, SegmentData, SegmentConversionOptions> segmentConverter)
    {
        this.segmentConverter = segmentConverter;
    }


    protected ConfigurableConverter<CxSegmentModel, SegmentData, SegmentConversionOptions> getSegmentConverter()
    {
        return this.segmentConverter;
    }


    @Required
    public void setSegmentReverseConverter(Converter<SegmentData, CxSegmentModel> segmentReverseConverter)
    {
        this.segmentReverseConverter = segmentReverseConverter;
    }


    protected Converter<SegmentData, CxSegmentModel> getSegmentReverseConverter()
    {
        return this.segmentReverseConverter;
    }


    @Required
    public void setSegmentService(CxSegmentService segmentService)
    {
        this.segmentService = segmentService;
    }


    protected CxSegmentService getSegmentService()
    {
        return this.segmentService;
    }
}
