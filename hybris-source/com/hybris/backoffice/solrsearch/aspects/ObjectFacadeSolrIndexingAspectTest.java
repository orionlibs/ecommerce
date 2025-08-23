package com.hybris.backoffice.solrsearch.aspects;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.backoffice.solrsearch.events.SolrIndexSynchronizationStrategy;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.configuration.Configuration;
import org.aspectj.lang.JoinPoint;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectFacadeSolrIndexingAspectTest
{
    private static final String PRODUCT_TYPE = "Product";
    private static final PK CHANGED_PRODUCT_PK = PK.fromLong(1L);
    private static final PK FAILED_PRODUCT_PK = PK.fromLong(2L);
    @Spy
    @InjectMocks
    private ObjectFacadeSolrIndexingAspect solrIndexingAspect;
    @Mock
    private SolrIndexSynchronizationStrategy synchronizationStrategy;
    @Mock
    private ItemModificationHistoryService itemModificationHistoryService;
    @Mock
    private ModelService modelService;
    @Mock
    private ProductModel changedProduct;
    @Mock
    private ProductModel failedProduct;
    @Mock
    private ConfigurationService configurationService;


    @Before
    public void setUp()
    {
        ((ProductModel)Mockito.doReturn(CHANGED_PRODUCT_PK).when(this.changedProduct)).getPk();
        ((ModelService)Mockito.doReturn("Product").when(this.modelService)).getModelType(this.changedProduct);
        ((ObjectFacadeSolrIndexingAspect)Mockito.doReturn(Boolean.valueOf(false)).when(this.solrIndexingAspect)).isReverseCategoryIndexLookupEnabled();
        ((ConfigurationService)Mockito.doReturn(Mockito.mock(Configuration.class)).when(this.configurationService)).getConfiguration();
    }


    @Test
    public void shouldUpdateIndexForChangedModel()
    {
        this.solrIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {this.changedProduct}, ), null);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }


    @Test
    public void shouldCleanIndexForRemovedModel()
    {
        this.solrIndexingAspect.updateRemoved((JoinPoint)new JoinPointStub(new Object[] {this.changedProduct}, ), null);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).removeItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }


    @Test
    public void shouldUpdateIndexForChangedModels()
    {
        ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        result.addFailedObject(this.failedProduct, new ObjectAccessException("", new RuntimeException()));
        this.solrIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {Collections.singletonList(this.changedProduct)}, ), result);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }


    @Test
    public void shouldUpdateIndexForAddedProductToCategory()
    {
        ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        CategoryModel csm = (CategoryModel)Mockito.mock(CategoryModel.class);
        List<SavedValuesModel> savedValuesModelList = createSavedValueModel(true);
        Mockito.when(this.itemModificationHistoryService.getSavedValues((ItemModel)csm)).thenReturn(savedValuesModelList);
        Mockito.when(this.modelService.getModelType(csm)).thenReturn("Category");
        this.solrIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {csm}, ), result);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems("Product", Lists.newArrayList((Object[])new PK[] {CHANGED_PRODUCT_PK}));
    }


    @Test
    public void shouldUpdateIndexForRemovedProductFromCategory()
    {
        ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        CategoryModel categoryModel = (CategoryModel)Mockito.mock(CategoryModel.class);
        List<SavedValuesModel> savedValuesModelList = createSavedValueModel(false);
        Mockito.when(this.itemModificationHistoryService.getSavedValues((ItemModel)categoryModel)).thenReturn(savedValuesModelList);
        Mockito.when(this.modelService.getModelType(categoryModel)).thenReturn("Category");
        this.solrIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {categoryModel}, ), result);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems("Product", Lists.newArrayList((Object[])new PK[] {CHANGED_PRODUCT_PK}));
    }


    @Test
    public void shouldNotUpdateIndexFoProductsInCategoryWhenProductsHaveNotChanged()
    {
        ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        CategoryModel categoryModel = (CategoryModel)Mockito.mock(CategoryModel.class);
        List<SavedValuesModel> savedValuesModelList = createSavedValueModelWithoutProductsModification();
        Mockito.when(this.itemModificationHistoryService.getSavedValues((ItemModel)categoryModel)).thenReturn(savedValuesModelList);
        Mockito.when(this.modelService.getModelType(categoryModel)).thenReturn("Category");
        this.solrIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {categoryModel}, ), result);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems("Product", Lists.emptyList());
    }


    private List<SavedValuesModel> createSavedValueModelWithoutProductsModification()
    {
        SavedValueEntryModel savedValueEntry = (SavedValueEntryModel)Mockito.mock(SavedValueEntryModel.class);
        Mockito.when(savedValueEntry.getModifiedAttribute()).thenReturn("name");
        SavedValuesModel savedValue = (SavedValuesModel)Mockito.mock(SavedValuesModel.class);
        Mockito.when(savedValue.getSavedValuesEntries()).thenReturn(Sets.newSet((Object[])new SavedValueEntryModel[] {savedValueEntry}));
        Mockito.when(savedValue.getCreationtime()).thenReturn(new Date());
        return Lists.newArrayList((Object[])new SavedValuesModel[] {savedValue});
    }


    private List<SavedValuesModel> createSavedValueModel(boolean addProduct)
    {
        SavedValueEntryModel savedValueEntry = (SavedValueEntryModel)Mockito.mock(SavedValueEntryModel.class);
        if(addProduct)
        {
            mockAddProduct(savedValueEntry);
        }
        else
        {
            mockRemoveProduct(savedValueEntry);
        }
        Mockito.when(savedValueEntry.getModifiedAttribute()).thenReturn("products");
        Mockito.when(this.changedProduct.getModifiedtime()).thenReturn(new Date());
        SavedValuesModel savedValue = (SavedValuesModel)Mockito.mock(SavedValuesModel.class);
        Mockito.when(savedValue.getCreationtime()).thenReturn(new Date());
        Mockito.when(savedValue.getSavedValuesEntries()).thenReturn(Sets.newSet((Object[])new SavedValueEntryModel[] {savedValueEntry}));
        return Lists.newArrayList((Object[])new SavedValuesModel[] {savedValue});
    }


    private void mockAddProduct(SavedValueEntryModel savedValueEntry)
    {
        Mockito.when(savedValueEntry.getOldValue()).thenReturn(Lists.emptyList());
        Mockito.when(savedValueEntry.getNewValue()).thenReturn(Lists.newArrayList((Object[])new ProductModel[] {this.changedProduct}));
    }


    private void mockRemoveProduct(SavedValueEntryModel savedValueEntry)
    {
        Mockito.when(savedValueEntry.getOldValue()).thenReturn(Lists.newArrayList((Object[])new ProductModel[] {this.changedProduct}));
        Mockito.when(savedValueEntry.getNewValue()).thenReturn(Lists.emptyList());
    }


    @Test
    public void includeRequiredProductsShouldKeepPreAddedProducts()
    {
        ((ObjectFacadeSolrIndexingAspect)Mockito.doReturn(Boolean.valueOf(true)).when(this.solrIndexingAspect)).isReverseCategoryIndexLookupEnabled();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        PK originalPK = PK.fromLong(1L);
        JoinPointStub joinPointStub = new JoinPointStub(new Object[] {category});
        Map<String, List<PK>> data = new HashMap<>();
        data.put("Category", Collections.singletonList(originalPK));
        PK pk = PK.fromLong(1L);
        ((ObjectFacadeSolrIndexingAspect)Mockito.doReturn(Optional.of(Collections.singleton(pk))).when(this.solrIndexingAspect)).extractProductsFromModifiedCategories(Matchers.any());
        this.solrIndexingAspect.includeRequiredProducts((JoinPoint)joinPointStub, data);
        Assertions.assertThat(data.get("Product")).contains((Object[])new PK[] {pk, originalPK});
    }


    @Test
    public void extractProductsFromModifiedCategoriesShouldFetchProductsFromPassedCategories()
    {
        CategoryModel category1 = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel category2 = (CategoryModel)Mockito.mock(CategoryModel.class);
        PK p1 = PK.fromLong(1L);
        PK p2 = PK.fromLong(2L);
        PK p3 = PK.fromLong(3L);
        ((ObjectFacadeSolrIndexingAspect)Mockito.doReturn(Set.of(p1)).when(this.solrIndexingAspect)).findAllProductsInSubTree(category1);
        ((ObjectFacadeSolrIndexingAspect)Mockito.doReturn(Set.of(p2, p3)).when(this.solrIndexingAspect)).findAllProductsInSubTree(category2);
        Optional<Set<PK>> result = this.solrIndexingAspect.extractProductsFromModifiedCategories(List.of(category1, category2, "Irrelevant data"));
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).contains(Set.of(p1, p2, p3));
    }


    @Test
    public void extractProductsFromModifiedCategoriesShouldFetchProductsFromPassedSingleCategory()
    {
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        PK p1 = PK.fromLong(1L);
        PK p2 = PK.fromLong(2L);
        ((ObjectFacadeSolrIndexingAspect)Mockito.doReturn(Set.of(p1, p2)).when(this.solrIndexingAspect)).findAllProductsInSubTree(category);
        Optional<Set<PK>> result = this.solrIndexingAspect.extractProductsFromModifiedCategories(category);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).contains(Set.of(p1, p2));
    }


    @Test
    public void extractProductsFromModifiedCategoriesShouldReturnEmptyOnEmptyData()
    {
        Optional<Set<PK>> result = this.solrIndexingAspect.extractProductsFromModifiedCategories(null);
        Assertions.assertThat(result).isEmpty();
    }
}
