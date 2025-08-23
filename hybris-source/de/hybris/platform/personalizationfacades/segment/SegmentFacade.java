package de.hybris.platform.personalizationfacades.segment;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import java.util.Map;

public interface SegmentFacade
{
    SearchPageData<SegmentData> getSegments(Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    SegmentData getSegment(String paramString);


    SegmentData createSegment(SegmentData paramSegmentData);


    SegmentData updateSegment(String paramString, SegmentData paramSegmentData);


    void deleteSegment(String paramString);
}
