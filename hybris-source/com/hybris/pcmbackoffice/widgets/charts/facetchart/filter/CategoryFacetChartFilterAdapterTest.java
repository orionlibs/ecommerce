package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CategoryFacetChartFilterAdapterTest
{
    @Spy
    @InjectMocks
    private CategoryFacetChartFilterAdapter adapter = new CategoryFacetChartFilterAdapter();
    @Mock
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private Executable onDeletedCallback;


    @Test
    public void shouldDeleteFilter()
    {
        Mockito.when(this.adapter.getNamesOfDeletedFilters((CockpitEvent)Matchers.any(CockpitEvent.class))).thenReturn(Collections.singletonList("selectedFilter"));
        this.adapter.deleteFilter((CockpitEvent)Mockito.mock(CockpitEvent.class), "selectedFilterValue", this.widgetInstanceManager, this.onDeletedCallback);
        ((CategoryFacetChartFilterAdapter)Mockito.verify(this.adapter)).deleteFilterFromModel(Matchers.anyString(), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class), (Executable)Matchers.any(Executable.class));
    }


    @Test
    public void shouldDeleteMultipleFilters()
    {
        List<String> filters = Arrays.asList(new String[] {"selectedFilterOne", "selectedFilterTwo", "selectedFilterThree"});
        Mockito.when(this.adapter.getNamesOfDeletedFilters((CockpitEvent)Matchers.any(CockpitEvent.class))).thenReturn(filters);
        this.adapter.deleteFilter((CockpitEvent)Mockito.mock(CockpitEvent.class), "selectedFilterValue", this.widgetInstanceManager, this.onDeletedCallback);
        ((CategoryFacetChartFilterAdapter)Mockito.verify(this.adapter, Mockito.times(filters.size()))).deleteFilterFromModel(Matchers.anyString(), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class),
                        (Executable)Matchers.any(Executable.class));
    }


    @Test
    public void shouldNotDeleteFilter()
    {
        Mockito.when(this.adapter.getNamesOfDeletedFilters((CockpitEvent)Matchers.any(CockpitEvent.class))).thenReturn(Collections.emptyList());
        this.adapter.deleteFilter((CockpitEvent)Mockito.mock(CockpitEvent.class), "selectedValue", this.widgetInstanceManager, this.onDeletedCallback);
        ((CategoryFacetChartFilterAdapter)Mockito.verify(this.adapter, Mockito.times(0))).deleteFilterFromModel(Matchers.anyString(), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class),
                        (Executable)Matchers.any(Executable.class));
    }


    @Test
    public void shouldGetNameOfDeletedFilter()
    {
        String expectedCode = "testCode";
        CockpitEvent event = createEventStub("testCode");
        List<String> namesOfDeletedFilters = this.adapter.getNamesOfDeletedFilters(event);
        Assertions.assertThat(namesOfDeletedFilters.isEmpty()).isFalse();
        Assertions.assertThat(namesOfDeletedFilters).contains(new Object[] {"testCode"});
    }


    @Test
    public void shouldNotGetNameOfDeletedFilterWhenCodeIsNull()
    {
        CockpitEvent event = createEventStub(null);
        List<String> namesOfDeletedFilter = this.adapter.getNamesOfDeletedFilters(event);
        Assertions.assertThat(namesOfDeletedFilter.isEmpty()).isTrue();
    }


    @Test
    public void shouldGetCategoryCode()
    {
        String code = "testCode";
        ClassificationClassModel model = createClassificationClassModelStub("testCode");
        Optional<String> categoryCode = this.adapter.getCategoryCode(model);
        Assertions.assertThat(categoryCode.isPresent()).isTrue();
        categoryCode.ifPresent(receivedCode -> Assertions.assertThat(receivedCode).isEqualTo("testCode"));
    }


    @Test
    public void shouldNotGetCategoryCodeWhenCodeIsEmpty()
    {
        ClassificationClassModel model = createClassificationClassModelStub("");
        Optional<String> categoryCode = this.adapter.getCategoryCode(model);
        Assertions.assertThat(categoryCode.isPresent()).isFalse();
    }


    @Test
    public void shouldNotGetCategoryCodeWhenCodeIsNull()
    {
        ClassificationClassModel model = createClassificationClassModelStub(null);
        Optional<String> categoryCode = this.adapter.getCategoryCode(model);
        Assertions.assertThat(categoryCode.isPresent()).isFalse();
    }


    @Test
    public void shouldBeAbleToDeleteFilter()
    {
        String code = "testCode";
        ClassificationClassModel model = createClassificationClassModelStub("testCode");
        boolean isAbleToDeleteFilter = this.adapter.canDelete(model, "testCode");
        Assertions.assertThat(isAbleToDeleteFilter).isTrue();
    }


    @Test
    public void shouldNotBeAbleToDeleteFilterWheCodeIsDifferentThanNameOfDeletedItem()
    {
        String code = "testCode";
        ClassificationClassModel model = createClassificationClassModelStub("testCode");
        boolean isAbleToDeleteFilter = this.adapter.canDelete(model, "anotherCode");
        Assertions.assertThat(isAbleToDeleteFilter).isFalse();
    }


    @Test
    public void shouldNotBeAbleToDeleteFilterWhenTypeIsDifferentThanExpected()
    {
        String code = "testCode";
        Object model = new Object();
        boolean isAbleToDeleteFilter = this.adapter.canDelete(model, "testCode");
        Assertions.assertThat(isAbleToDeleteFilter).isFalse();
    }


    private CockpitEvent createEventStub(String code)
    {
        ClassificationClassModel model = createClassificationClassModelStub(code);
        List<ClassificationClassModel> data = Collections.singletonList(model);
        return (CockpitEvent)new DefaultCockpitEvent("testEventStub", data, new Object());
    }


    private ClassificationClassModel createClassificationClassModelStub(String code)
    {
        ClassificationClassModel model = new ClassificationClassModel();
        model.setCode(code);
        return model;
    }
}
