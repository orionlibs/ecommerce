package com.hybris.pcmbackoffice.widgets.shortcuts;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;

@RunWith(MockitoJUnitRunner.class)
public class ShortcutsUtilServiceTest
{
    @Mock
    private ModelService modelService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private AdvancedSearchOperatorService advancedSearchOperatorService;
    @Mock
    private TypeFacade typeFacade;
    @Mock
    private ShortcutsService shortcutsService;
    @Mock
    private LabelService labelService;
    @Spy
    @InjectMocks
    private ShortcutsUtilService shortcutsUtilService;


    @Test
    public void testReturnEmptyConditions()
    {
        List<PK> list = new ArrayList<>();
        Assertions.assertThat(this.shortcutsUtilService.getConditions(ValueComparisonOperator.IN, list)).isEqualTo(list);
    }


    @Test
    public void testReturnConditions()
    {
        List<PK> list = createPKList();
        this.shortcutsUtilService.getConditions(ValueComparisonOperator.IN, list);
        ((ShortcutsUtilService)Mockito.verify(this.shortcutsUtilService)).addFilterConditionOnSearchData(ValueComparisonOperator.IN, list);
    }


    @Test
    public void testHandleDropMultiSelection()
    {
        List<ItemModel> result = createItemModelList();
        DropEvent event = createDropEvent(result, null);
        Assertions.assertThat(this.shortcutsUtilService.getSelectedData((Event)event)).isEqualTo(result);
    }


    @Test
    public void testHandleDropSingleSelection()
    {
        List<ItemModel> result = createItemModelList();
        DropEvent event = createDropEvent(null, result.get(0));
        Assertions.assertThat(this.shortcutsUtilService.getSelectedData((Event)event)).isEqualTo(result);
    }


    @Test
    public void shouldReturnEmptyWhenListIsEmpty()
    {
        List<PK> list = new ArrayList<>();
        Assertions.assertThat(this.shortcutsUtilService.addFilterConditionOnSearchData(ValueComparisonOperator.IN, list))
                        .isEqualTo(Lists.newArrayList());
    }


    @Test
    public void shouldReturnConditionsWhenListIsNotEmpty()
    {
        List<PK> list = createPKList();
        Assertions.assertThat(this.shortcutsUtilService.addFilterConditionOnSearchData(ValueComparisonOperator.IN, list).size()).isEqualTo(1);
        Assertions.assertThat(((SearchConditionData)this.shortcutsUtilService.addFilterConditionOnSearchData(ValueComparisonOperator.IN, list).get(0)).getValue())
                        .isEqualTo(list);
        Assertions.assertThat((Comparable)((SearchConditionData)this.shortcutsUtilService.addFilterConditionOnSearchData(ValueComparisonOperator.IN, list).get(0)).getOperator())
                        .isEqualTo(ValueComparisonOperator.IN);
    }


    @Test
    public void shouldReturnPKEmptyCondition()
    {
        SearchConditionData condition = this.shortcutsUtilService.getPKEmptyCondition();
        Assertions.assertThat(condition.getValue()).isEqualTo(null);
        Assertions.assertThat((Comparable)condition.getOperator()).isEqualTo(ValueComparisonOperator.IS_EMPTY);
        Assertions.assertThat(condition.getFieldType().getName()).isEqualTo("pk");
    }


    @Test
    public void testIsItemsAlreadyDeletedReturnFalse()
    {
        List<ItemModel> list = createItemModelList();
        Mockito.when(Boolean.valueOf(this.modelService.isRemoved(list.get(0)))).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.shortcutsUtilService.isItemsAlreadyDeleted(list)).isFalse();
    }


    @Test
    public void testIsItemsAlreadyDeletedWhenListIsEmpty()
    {
        List<ItemModel> list = new ArrayList<>();
        Assertions.assertThat(this.shortcutsUtilService.isItemsAlreadyDeleted(list)).isFalse();
    }


    @Test
    public void testIsItemsAlreadyDeletedReturnTrue()
    {
        List<ItemModel> list = createItemModelList();
        Mockito.when(Boolean.valueOf(this.modelService.isRemoved(list.get(0)))).thenReturn(Boolean.valueOf(true));
        Assertions.assertThat(this.shortcutsUtilService.isItemsAlreadyDeleted(list)).isTrue();
    }


    @Test
    public void testIsLimitationExceededReturnFalse()
    {
        List<ItemModel> list = createItemModelList();
        Shortcutsitem shortcutsitem = (Shortcutsitem)Mockito.mock(Shortcutsitem.class);
        prepareDataForTestIsLimitationExceeded(list, shortcutsitem, 10);
        Assertions.assertThat(this.shortcutsUtilService.isLimitationExceeded(list, shortcutsitem)).isFalse();
    }


    @Test
    public void testIsLimitationExceededReturnTrue()
    {
        List<ItemModel> list = createItemModelList();
        List<ItemModel> list2 = createItemModelList();
        list2.addAll(list);
        Shortcutsitem shortcutsitem = (Shortcutsitem)Mockito.mock(Shortcutsitem.class);
        prepareDataForTestIsLimitationExceeded(list2, shortcutsitem, 1);
        Assertions.assertThat(this.shortcutsUtilService.isLimitationExceeded(list2, shortcutsitem)).isTrue();
    }


    @Test
    public void shouldReturnAlreadyExistItems()
    {
        List<ItemModel> list = createItemModelList();
        Mockito.when(Boolean.valueOf(this.shortcutsService.collectionContainsItem((ProductModel)Matchers.any(), (BackofficeObjectSpecialCollectionModel)Matchers.any()))).thenReturn(Boolean.valueOf(true));
        Assertions.assertThat(this.shortcutsUtilService.getAlreadyExistItems(list, (BackofficeObjectSpecialCollectionModel)Mockito.mock(BackofficeObjectSpecialCollectionModel.class)).get(0))
                        .isEqualTo(list.get(0));
        Assertions.assertThat(this.shortcutsUtilService.getAlreadyExistItems(list, (BackofficeObjectSpecialCollectionModel)Mockito.mock(BackofficeObjectSpecialCollectionModel.class)).size())
                        .isEqualTo(1);
    }


    @Test
    public void shouldReturnEmptyListWhenNoItemsExist()
    {
        List<ItemModel> list = createItemModelList();
        Mockito.when(Boolean.valueOf(this.shortcutsService.collectionContainsItem((ProductModel)Matchers.any(), (BackofficeObjectSpecialCollectionModel)Matchers.any()))).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.shortcutsUtilService.getAlreadyExistItems(list, (BackofficeObjectSpecialCollectionModel)Mockito.mock(BackofficeObjectSpecialCollectionModel.class)).size())
                        .isZero();
    }


    @Test
    public void shouldNotExistItems()
    {
        List<ItemModel> list = createItemModelList();
        Mockito.when(Boolean.valueOf(this.shortcutsService.collectionContainsItem((ProductModel)Matchers.any(), (BackofficeObjectSpecialCollectionModel)Matchers.any()))).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.shortcutsUtilService.getNotExistItems(list, (BackofficeObjectSpecialCollectionModel)Mockito.mock(BackofficeObjectSpecialCollectionModel.class)).get(0))
                        .isEqualTo(list.get(0));
        Assertions.assertThat(this.shortcutsUtilService.getNotExistItems(list, (BackofficeObjectSpecialCollectionModel)Mockito.mock(BackofficeObjectSpecialCollectionModel.class)).size())
                        .isEqualTo(1);
    }


    @Test
    public void shouldReturnEmptyListWhenAllItemsExist()
    {
        List<ItemModel> list = createItemModelList();
        Mockito.when(Boolean.valueOf(this.shortcutsService.collectionContainsItem((ProductModel)Matchers.any(), (BackofficeObjectSpecialCollectionModel)Matchers.any()))).thenReturn(Boolean.valueOf(true));
        Assertions.assertThat(this.shortcutsUtilService.getNotExistItems(list, (BackofficeObjectSpecialCollectionModel)Mockito.mock(BackofficeObjectSpecialCollectionModel.class)).size())
                        .isZero();
    }


    private static DropEvent createDropEvent(List multiSelection, ItemModel item)
    {
        Div draggedDiv = (Div)Mockito.mock(Div.class);
        Mockito.when(draggedDiv.getAttribute("dndData", true)).thenReturn(item);
        Div targetDiv = (Div)Mockito.mock(Div.class);
        if(multiSelection != null)
        {
            Mockito.when(draggedDiv.getAttribute("selectionSupplier", true))
                            .thenReturn(() -> multiSelection);
        }
        return new DropEvent("event", (Component)targetDiv, (Component)draggedDiv, 0, 0, 0, 0, 0);
    }


    private static List<PK> createPKList()
    {
        List<PK> list = new ArrayList<>();
        PK pk = PK.createCounterPK(1);
        list.add(pk);
        return list;
    }


    private static List<ItemModel> createItemModelList()
    {
        List<ItemModel> list = new ArrayList<>();
        ProductModel item = (ProductModel)Mockito.mock(ProductModel.class);
        list.add(item);
        return list;
    }


    private static List<ProductModel> createProductModelList()
    {
        List<ProductModel> list = new ArrayList<>();
        ProductModel item = (ProductModel)Mockito.mock(ProductModel.class);
        list.add(item);
        return list;
    }


    private void prepareDataForTestIsLimitationExceeded(List<ItemModel> list, Shortcutsitem shortcutsitem, int maxSize)
    {
        ((ShortcutsUtilService)Mockito.doReturn(Integer.valueOf(maxSize)).when(this.shortcutsUtilService)).getMaxSize((Shortcutsitem)Matchers.any());
    }
}
