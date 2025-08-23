package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
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
public class CatalogVersionFacetChartFilterAdapterTest
{
    @Spy
    @InjectMocks
    private CatalogVersionFacetChartFilterAdapter adapter = new CatalogVersionFacetChartFilterAdapter();
    @Mock
    private LabelService labelService;
    @Mock
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private Executable onDeletedCallback;


    @Test
    public void shouldDeleteFilter()
    {
        Mockito.when(this.adapter.getNamesOfDeletedFilters((CockpitEvent)Matchers.any(CockpitEvent.class))).thenReturn(Collections.singletonList("selectedFilter"));
        this.adapter.deleteFilter((CockpitEvent)Mockito.mock(CockpitEvent.class), "selectedFilterValue", this.widgetInstanceManager, this.onDeletedCallback);
        ((CatalogVersionFacetChartFilterAdapter)Mockito.verify(this.adapter)).deleteFilterFromModel(Matchers.anyString(), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class), (Executable)Matchers.any(Executable.class));
    }


    @Test
    public void shouldDeleteMultipleFilters()
    {
        List<String> filters = Arrays.asList(new String[] {"selectedFilterOne", "selectedFilterTwo", "selectedFilterThree"});
        Mockito.when(this.adapter.getNamesOfDeletedFilters((CockpitEvent)Matchers.any(CockpitEvent.class))).thenReturn(filters);
        this.adapter.deleteFilter((CockpitEvent)Mockito.mock(CockpitEvent.class), "selectedFilterValue", this.widgetInstanceManager, this.onDeletedCallback);
        ((CatalogVersionFacetChartFilterAdapter)Mockito.verify(this.adapter, Mockito.times(filters.size()))).deleteFilterFromModel(Matchers.anyString(), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class),
                        (Executable)Matchers.any(Executable.class));
    }


    @Test
    public void shouldNotDeleteFilter()
    {
        Mockito.when(this.adapter.getNamesOfDeletedFilters((CockpitEvent)Matchers.any(CockpitEvent.class))).thenReturn(Collections.emptyList());
        this.adapter.deleteFilter((CockpitEvent)Mockito.mock(CockpitEvent.class), "selectedValue", this.widgetInstanceManager, this.onDeletedCallback);
        ((CatalogVersionFacetChartFilterAdapter)Mockito.verify(this.adapter, Mockito.times(0))).deleteFilterFromModel(Matchers.anyString(), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class),
                        (Executable)Matchers.any(Executable.class));
    }


    @Test
    public void shouldGetNameOfDeletedFilter()
    {
        String expectedVersionName = "testVersion";
        CockpitEvent event = createEventStub("testVersion");
        List<String> namesOfDeletedFilters = this.adapter.getNamesOfDeletedFilters(event);
        Assertions.assertThat(namesOfDeletedFilters.isEmpty()).isFalse();
        Assertions.assertThat(namesOfDeletedFilters).contains(new Object[] {"testVersion"});
    }


    @Test
    public void shouldNotGetNameOfDeletedFilterWhenVersionIsEmpty()
    {
        CockpitEvent event = createEventStub("");
        List<String> namesOfDeletedFilters = this.adapter.getNamesOfDeletedFilters(event);
        Assertions.assertThat(namesOfDeletedFilters.isEmpty()).isTrue();
    }


    @Test
    public void shouldNotGetNameOfDeletedFilterWhenVersionIsNull()
    {
        CockpitEvent event = createEventStub(null);
        List<String> namesOfDeletedFilters = this.adapter.getNamesOfDeletedFilters(event);
        Assertions.assertThat(namesOfDeletedFilters.isEmpty()).isTrue();
    }


    @Test
    public void shouldGetCatalogVersion()
    {
        String expectedVersionName = "testVersion";
        ClassificationSystemVersionModel model = createClassificationSystemVersionModelStub("testVersion");
        Optional<String> catalogVersion = this.adapter.getCatalogVersion(model);
        Assertions.assertThat(catalogVersion.isPresent()).isTrue();
        catalogVersion.ifPresent(receivedVersionName -> Assertions.assertThat(receivedVersionName).isEqualTo("testVersion"));
    }


    @Test
    public void shouldNotGetCatalogVersionWhenVersionIsEmpty()
    {
        ClassificationSystemVersionModel model = createClassificationSystemVersionModelStub("");
        Optional<String> catalogVersion = this.adapter.getCatalogVersion(model);
        Assertions.assertThat(catalogVersion.isPresent()).isFalse();
    }


    @Test
    public void shouldNotGetCatalogVersionWhenVersionIsNull()
    {
        ClassificationSystemVersionModel model = createClassificationSystemVersionModelStub(null);
        Optional<String> catalogVersion = this.adapter.getCatalogVersion(model);
        Assertions.assertThat(catalogVersion.isPresent()).isFalse();
    }


    @Test
    public void shouldBeAbleToDeleteFilter()
    {
        String version = "testVersion";
        ClassificationSystemVersionModel model = createClassificationSystemVersionModelStub("testVersion");
        boolean isAbleToDeleteFilter = this.adapter.canDelete(model, "testVersion");
        Assertions.assertThat(isAbleToDeleteFilter).isTrue();
    }


    @Test
    public void shouldNotBeAbleToDeleteFilterWhenVersionIsDifferentThanNameOfDeletedItem()
    {
        String version = "testVersion";
        ClassificationSystemVersionModel model = createClassificationSystemVersionModelStub("testVersion");
        boolean isAbleToDeleteFilter = this.adapter.canDelete(model, "anotherVersion");
        Assertions.assertThat(isAbleToDeleteFilter).isFalse();
    }


    @Test
    public void shouldNotBeAbleToDeleteFilterWhenTypeIsDifferentThanExpected()
    {
        String version = "testVersion";
        Object model = new Object();
        boolean isAbleToDeleteFilter = this.adapter.canDelete(model, "testVersion");
        Assertions.assertThat(isAbleToDeleteFilter).isFalse();
    }


    private CockpitEvent createEventStub(String version)
    {
        ClassificationSystemVersionModel model = createClassificationSystemVersionModelStub(version);
        List<ClassificationSystemVersionModel> data = Collections.singletonList(model);
        return (CockpitEvent)new DefaultCockpitEvent("testEventStub", data, new Object());
    }


    private ClassificationSystemVersionModel createClassificationSystemVersionModelStub(String version)
    {
        ClassificationSystemModel classificationSystemModel = new ClassificationSystemModel();
        ClassificationSystemVersionModel model = new ClassificationSystemVersionModel();
        model.setCatalog((CatalogModel)classificationSystemModel);
        model.setVersion(version);
        return model;
    }
}
