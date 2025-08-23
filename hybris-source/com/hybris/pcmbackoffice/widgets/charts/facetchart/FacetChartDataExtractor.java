package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.zkoss.chart.Point;

public class FacetChartDataExtractor
{
    private static final Comparator<FacetValueData> rangeLabelsComparator = Comparator.comparing(FacetValueData::getName,
                    Comparator.comparing(FacetChartDataExtractor::findFirstInteger, Comparator.nullsFirst(Comparator.naturalOrder())));


    private static Point facetValeDataToPointMapper(FacetValueData facetValueData)
    {
        Point point = new Point(facetValueData.getDisplayName(), Long.valueOf(facetValueData.getCount()));
        point.setId(facetValueData.getName());
        return point;
    }


    protected static Comparator<FacetValueData> getRangeLabelsComparator()
    {
        return rangeLabelsComparator;
    }


    private static Integer findFirstInteger(String inputString)
    {
        Matcher integerMatcher = Pattern.compile("\\d+").matcher(inputString);
        boolean continsInteger = integerMatcher.find();
        if(!continsInteger)
        {
            return null;
        }
        String number = integerMatcher.group();
        return Integer.valueOf(Integer.parseInt(number));
    }


    public List<Point> getPoints(FullTextSearchData fullTextSearchData, String facetCode)
    {
        List<Point> points = (List<Point>)((Collection<FacetValueData>)fullTextSearchData.getFacets().stream().filter(facet -> facetCode.equals(facet.getName())).findFirst().map(FacetData::getFacetValues).orElseGet(Collections::emptyList)).stream().sorted(rangeLabelsComparator)
                        .map(FacetChartDataExtractor::facetValeDataToPointMapper).collect(Collectors.toList());
        AtomicInteger nIndex = new AtomicInteger(0);
        points.forEach(point -> point.setX(Integer.valueOf(nIndex.getAndIncrement())));
        return points;
    }
}
