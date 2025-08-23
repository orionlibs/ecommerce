package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;

@RunWith(MockitoJUnitRunner.class)
public class CatalogVersionFacetChartFiltersRendererTest
{
    @Spy
    @InjectMocks
    private CatalogVersionFacetChartFiltersRenderer catalogVersionFacetChartFiltersRenderer;
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private UserService userService;
    @Mock
    private CatalogVersionService catalogVersionService;
    @Mock
    private LabelService labelService;
    @Spy
    private Div filterContainer = new Div();
    @Mock
    private BiConsumer<String, Set<String>> facetSelectionListener;
    @Mock
    private CatalogVersionModel catalogVersionModel;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
        Mockito.when(this.labelService.getObjectLabel(Matchers.any())).thenReturn("Label");
        List<CatalogVersionModel> allReadableCatalog = new ArrayList<>();
        allReadableCatalog.add(this.catalogVersionModel);
        UserModel user = (UserModel)Mockito.mock(UserModel.class);
        Mockito.when(this.userService.getCurrentUser()).thenReturn(user);
        Mockito.when(this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)user)).thenReturn(allReadableCatalog);
    }


    @Test
    public void shouldFireEventForOneSearchCondition()
    {
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        Set<String> selected = Collections.singleton("some catalog");
        this.catalogVersionFacetChartFiltersRenderer.onSelectCatalogVersion(selected);
        ArgumentCaptor<String> facetNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Set> facetSelectionArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ((BiConsumer<String, Set>)Mockito.verify(this.facetSelectionListener)).accept((String)facetNameArgumentCaptor.capture(), (Set)facetSelectionArgumentCaptor.capture());
        Assertions.assertThat((Iterable)facetSelectionArgumentCaptor.getValue()).hasSize(1);
    }


    @Test
    public void shouldFireEventForEmptyConditionList()
    {
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        this.catalogVersionFacetChartFiltersRenderer.onSelectCatalogVersion(Collections.emptySet());
        ArgumentCaptor<String> facetNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Set> facetSelectionArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ((BiConsumer<String, Set>)Mockito.verify(this.facetSelectionListener)).accept((String)facetNameArgumentCaptor.capture(), (Set)facetSelectionArgumentCaptor.capture());
        Assertions.assertThat((Iterable)facetSelectionArgumentCaptor.getValue()).isEmpty();
    }


    @Test
    public void shouldStoreIndexAndValueOnSelectingCategory()
    {
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        Set<String> selected = Collections.singleton("some catalog");
        this.catalogVersionFacetChartFiltersRenderer.onSelectCatalogVersion(selected);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer)).storeSelectedValues((Collection)Matchers.any());
    }


    @Test
    public void shouldStoreIndexAndValueOnSelectingEmptyCategory()
    {
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        Set<String> selected = Collections.emptySet();
        this.catalogVersionFacetChartFiltersRenderer.onSelectCatalogVersion(selected);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer)).storeSelectedValues((Collection)Matchers.any());
    }


    @Test
    public void shouldSelectItemInTheChosenBox()
    {
        ListModelList<String> listModel = new ListModelList();
        listModel.addAll(Arrays.asList(new String[] {"oneNotSelected", "twoSelected", "threeNotSelected"}));
        Collection<String> selected = Collections.singleton("twoSelected");
        Set<String> selectedObjects = this.catalogVersionFacetChartFiltersRenderer.getSelectedCatalogVersions(listModel, selected);
        Assertions.assertThat(selectedObjects.size()).isEqualTo(selected.size());
        Assertions.assertThat(selectedObjects).containsAll(selected);
    }


    @Test
    public void shouldClearSelectionAndSearchIfValueIsNotFoundInTheChosenBox()
    {
        Collection<String> selected = Collections.singleton("name4");
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.doReturn(selected).when(this.catalogVersionFacetChartFiltersRenderer)).readSelectedValues();
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer)).storeSelectedValues((Collection)Matchers.any());
        ArgumentCaptor<ListModelList> argumentCaptor = ArgumentCaptor.forClass(ListModelList.class);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer)).getSelectedCatalogVersions((ListModelList)argumentCaptor.capture(), (Collection)Matchers.any());
        ListModelList listModel = (ListModelList)argumentCaptor.getValue();
        Assertions.assertThat(listModel.getSelection()).isEmpty();
    }


    @Test
    public void shouldNotTriggerClearAndSearchOnEmptySelection()
    {
        ListModelList<String> listModel = new ListModelList();
        listModel.addAll(Arrays.asList(new String[] {"one", "two"}));
        this.catalogVersionFacetChartFiltersRenderer.getSelectedCatalogVersions(listModel, Collections.emptySet());
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer, Mockito.never())).storeSelectedValues((Collection)Matchers.any());
    }


    @Test
    public void shouldApplySelectedCategories()
    {
        Collection<String> selected = Arrays.asList(new String[] {"Label"});
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.doReturn(selected).when(this.catalogVersionFacetChartFiltersRenderer)).readSelectedValues();
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer, Mockito.never())).storeSelectedValues((Collection)Matchers.any());
        ArgumentCaptor<ListModelList> argumentCaptor = ArgumentCaptor.forClass(ListModelList.class);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer)).getSelectedCatalogVersions((ListModelList)argumentCaptor.capture(), (Collection)Matchers.any());
        ListModelList listModel = (ListModelList)argumentCaptor.getValue();
        Assertions.assertThat(listModel.getSelection()).containsAll(selected);
    }


    @Test
    public void shouldRefreshAfterRemoveFilters()
    {
        this.catalogVersionFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        this.catalogVersionFacetChartFiltersRenderer.removeFilters();
        ArgumentCaptor<Collection> collectionArgumentCaptor = ArgumentCaptor.forClass(Collection.class);
        ((CatalogVersionFacetChartFiltersRenderer)Mockito.verify(this.catalogVersionFacetChartFiltersRenderer)).storeSelectedValues((Collection)collectionArgumentCaptor.capture());
        Assertions.assertThat((Iterable)collectionArgumentCaptor.getValue()).isEmpty();
        ArgumentCaptor<Set> setArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ((BiConsumer<String, Set>)Mockito.verify(this.facetSelectionListener)).accept((String)Matchers.any(), (Set)setArgumentCaptor.capture());
        Assertions.assertThat((Iterable)setArgumentCaptor.getValue()).isEmpty();
    }
}
