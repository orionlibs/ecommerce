package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetType;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.chart.Point;

@RunWith(MockitoJUnitRunner.class)
public class FacetChartDataExtractorTest
{
    private final String VALID_FACET_NAME = "facetName";
    private final String ANOTHER_FACET_NAME = "anotherName";
    @InjectMocks
    private FacetChartDataExtractor facetChartDataExtractor;
    @Mock
    private FullTextSearchData fullTextSearchData;


    @Test
    public void shouldCreateChartPointsFromFacets()
    {
        Mockito.when(this.fullTextSearchData.getFacets()).thenReturn(prepareFacetsData());
        List<Point> points = this.facetChartDataExtractor.getPoints(this.fullTextSearchData, "facetName");
        Assertions.assertThat(points).hasSize(5);
        for(int i = 0; i < 5; i++)
        {
            Assertions.assertThat(((Point)points.get(i)).getName()).isEqualTo(createDataValueName("facetName", i));
            Assertions.assertThat(((Point)points.get(i)).getY().intValue()).isEqualTo(i);
        }
    }


    private List<FacetData> prepareFacetsData()
    {
        List<FacetValueData> facedDataValues = (List<FacetValueData>)IntStream.range(0, 5).mapToObj(i -> new FacetValueData(createDataValueName("facetName", i), i, false)).collect(Collectors.toList());
        FacetData facedData = new FacetData("facetName", "facetName", FacetType.REFINE, facedDataValues);
        List<FacetValueData> anotherFacedDataValues = (List<FacetValueData>)IntStream.range(0, 5).mapToObj(i -> new FacetValueData(createDataValueName("anotherName", i), i, false)).collect(Collectors.toList());
        FacetData anotherFacetData = new FacetData("anotherName", "anotherName", FacetType.REFINE, anotherFacedDataValues);
        return Lists.newArrayList((Object[])new FacetData[] {facedData, anotherFacetData});
    }


    private String createDataValueName(String facetName, int facetDataNumber)
    {
        return String.format("%s - data value %d", new Object[] {facetName, Integer.valueOf(facetDataNumber)});
    }


    @Test
    public void shouldCompareRangeLabels()
    {
        Comparator<FacetValueData> rangeLabelsComparator = FacetChartDataExtractor.getRangeLabelsComparator();
        Assertions.assertThat(rangeLabelsComparator.compare(new FacetValueData("20-39%", 1L, true), new FacetValueData("0-19%", 1L, true)))
                        .isGreaterThan(0);
        Assertions.assertThat(rangeLabelsComparator.compare(new FacetValueData("100%", 1L, true), new FacetValueData("80-99%", 1L, true)))
                        .isGreaterThan(0);
        Assert.assertEquals(0L, rangeLabelsComparator.compare(new FacetValueData("12suffix", 1L, true), new FacetValueData("12", 1L, true)));
        Assertions.assertThat(rangeLabelsComparator.compare(new FacetValueData("not-a-number", 1L, true), new FacetValueData("7", 1L, true)))
                        .isLessThan(0);
    }
}
