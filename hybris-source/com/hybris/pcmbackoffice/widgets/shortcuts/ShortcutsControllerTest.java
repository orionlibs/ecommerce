package com.hybris.pcmbackoffice.widgets.shortcuts;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;
import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.DeclaredInputs;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

@DeclaredInputs({@DeclaredInput(value = "searchInit", socketType = String.class), @DeclaredInput("refresh"), @DeclaredInput(value = "updateContext", socketType = AdvancedSearchInitContext.class), @DeclaredInput("reset"), @DeclaredInput("refreshAssortmentFullTextSearch")})
@DeclaredGlobalCockpitEvent(eventName = "objectsDeleted", scope = "session")
public class ShortcutsControllerTest extends AbstractWidgetUnitTest<ShortcutsController>
{
    @Spy
    @InjectMocks
    private ShortcutsController shortcutsController;
    @Mock
    private ShortcutsService shortcutsService;
    @Mock
    private ShortcutsUtilService shortcutsUtilService;
    @Mock
    private AdvancedSearchOperatorService advancedSearchOperatorService;
    @Mock
    private Listbox shortcuts;
    @Mock
    private Component component;
    @Mock
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private TypeFacade typeFacade;
    @Mock
    private List<Shortcutsitem> shortcutsList;
    @Mock
    private AdvancedSearchInitContext context;
    @Mock
    private Shortcutsitem shortcutsitem;
    private final BackofficeObjectSpecialCollectionModel blockedListCollectionModel = new BackofficeObjectSpecialCollectionModel();
    private final BackofficeObjectSpecialCollectionModel quickListCollectionModel = new BackofficeObjectSpecialCollectionModel();
    private static final String typeCode = "testCode";
    private final CockpitEvent event = (CockpitEvent)Mockito.mock(CockpitEvent.class);


    protected ShortcutsController getWidgetController()
    {
        return this.shortcutsController;
    }


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
        Component component = (Component)Mockito.mock(Component.class);
        Mockito.when(this.shortcutsService.initCollection((String)Matchers.any())).thenReturn(Mockito.mock(BackofficeObjectSpecialCollectionModel.class));
        Mockito.when(this.shortcutsUtilService.getDroppables()).thenReturn("");
        this.shortcutsController.initialize(component);
        this.blockedListCollectionModel.setCollectionType(BackofficeSpecialCollectionType.BLOCKEDLIST);
        this.quickListCollectionModel.setCollectionType(BackofficeSpecialCollectionType.QUICKLIST);
    }


    @Test
    public void shouldSendOutputUpdateAssortmentFullTextSearchWhenRefreshWithBlockedListItem()
    {
        List<PK> pkList = new ArrayList<>();
        pkList.add(PK.createCounterPK(1));
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(pkList);
        this.shortcutsController.refresh(new Object());
        assertSocketOutput("updateFullTextSearch", new ArrayList());
    }


    @Test
    public void shouldInitSearchWithInputCodeWhenInitSearch()
    {
        this.shortcutsController.initializeFullTextSearch("testCode");
        assertSocketOutput("initSearch", code -> (code == "testCode"));
    }


    @Test
    public void shouldSendOutputUpdateAssortmentFullTextSearchWhenInitSearchWithBlockedListItem()
    {
        List<PK> pkList = new ArrayList<>();
        pkList.add(PK.createCounterPK(1));
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(pkList);
        this.shortcutsController.initializeFullTextSearch("testCode");
        assertSocketOutput("updateFullTextSearch", new ArrayList());
    }


    @Test
    public void shouldSendOutResetFullTextSearchWithConditionsWhenUpdateContextWithBlockedListIsNotEmpty()
    {
        List<PK> pkList = new ArrayList<>();
        List<SearchConditionData> conditions = Arrays.asList(new SearchConditionData[] {(SearchConditionData)Mockito.mock(SearchConditionData.class)});
        pkList.add(PK.createCounterPK(1));
        this.context = (AdvancedSearchInitContext)Mockito.mock(AdvancedSearchInitContext.class);
        Map<String, Object> finalData = new HashMap<>();
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(pkList);
        BDDMockito.given(this.shortcutsUtilService.getConditions(ValueComparisonOperator.NOT_IN, pkList)).willReturn(conditions);
        finalData.put("conditions", conditions);
        finalData.put("context", this.context);
        this.shortcutsController.updateContext(this.context);
        assertSocketOutput("resetFullTextSearch", data -> data.equals(finalData));
    }


    @Test
    public void shouldSendOutResetFullTextSearchWithoutConditionsWhenUpdateContextWithBlockedListIsEmpty()
    {
        this.context = (AdvancedSearchInitContext)Mockito.mock(AdvancedSearchInitContext.class);
        Map<String, Object> finalData = new HashMap<>();
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(new ArrayList());
        BDDMockito.given(this.shortcutsUtilService.getConditions(ValueComparisonOperator.NOT_IN, new ArrayList())).willReturn(null);
        finalData.put("conditions", null);
        finalData.put("context", this.context);
        this.shortcutsController.updateContext(this.context);
        assertSocketOutput("resetFullTextSearch", data -> data.equals(finalData));
    }


    @Test
    public void shouldSendOutputUpdateAssortmentFullTextSearchWhenHandleDeleteEventWithBlockedListItem()
    {
        List<PK> pkList = new ArrayList<>();
        pkList.add(PK.createCounterPK(1));
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(pkList);
        Collection<Object> deletedObjects = new ArrayList();
        deletedObjects.add(Mockito.mock(ProductModel.class));
        BDDMockito.given(this.event.getDataAsCollection()).willReturn(deletedObjects);
        this.shortcutsController.handleObjectDeleteEvent(this.event);
        assertSocketOutput("updateFullTextSearch", new ArrayList());
    }


    @Test
    public void shouldUpdateAssortmentViewFullTextSearchWhenRefresh()
    {
        List<PK> blockedList = new ArrayList<>();
        List<SearchConditionData> conditions = Arrays.asList(new SearchConditionData[] {(SearchConditionData)Mockito.mock(SearchConditionData.class)});
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(blockedList);
        BDDMockito.given(this.shortcutsUtilService.getConditions(ValueComparisonOperator.NOT_IN, blockedList)).willReturn(conditions);
        this.shortcutsController.refreshAssortmentViewFullTextSearch();
        assertSocketOutput("updateAssortmentFullTextSearch", conditions);
    }


    @Test
    public void shouldSendOutputWithPKIsEmptyConditionWhenAllBlockItemsDeleted()
    {
        SearchConditionData condition = (SearchConditionData)Mockito.mock(SearchConditionData.class);
        this.shortcutsController.setCurrentLabel(ShortcutsFlagEnum.BLOCKED_LIST);
        BDDMockito.given(this.shortcutsService.initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode())).willReturn(this.blockedListCollectionModel);
        BDDMockito.given(this.shortcutsService.getAllCollectionList(this.blockedListCollectionModel)).willReturn(Collections.emptyList());
        BDDMockito.given(this.shortcutsUtilService.getPKEmptyCondition()).willReturn(condition);
        BDDMockito.given(this.event.getDataAsCollection()).willReturn(Arrays.asList(new Object[] {Mockito.mock(ProductModel.class)}));
        this.shortcutsController.handleObjectDeleteEvent(this.event);
        assertSocketOutput("updateFullTextSearch", conditions -> (conditions.size() == 1 && ((SearchConditionData)conditions.get(0)).equals(condition)));
    }
}
