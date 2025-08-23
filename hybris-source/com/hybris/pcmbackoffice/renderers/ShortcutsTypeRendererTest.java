package com.hybris.pcmbackoffice.renderers;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;
import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

@RunWith(MockitoJUnitRunner.class)
public class ShortcutsTypeRendererTest
{
    @Spy
    @InjectMocks
    private final ShortcutsTypeRenderer shortcutsTypeRenderer = new ShortcutsTypeRenderer();
    @Mock
    protected ShortcutsService shortcutsService;
    @Mock
    private DataType dataType;
    @Mock
    private ProductModel productModel;
    @Mock
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private final Component parentComponent = (Component)Mockito.mock(Div.class);
    @Mock
    private final GridView configuration = (GridView)Mockito.mock(GridView.class);
    private static final String BLOCKED_LIST = "blockedlist";
    private static final String QUICK_LIST = "quicklist";
    private final BackofficeObjectSpecialCollectionModel quickLiskCollectionModel = new BackofficeObjectSpecialCollectionModel();
    private final BackofficeObjectSpecialCollectionModel blockedLiskCollectionModel = new BackofficeObjectSpecialCollectionModel();


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
        this.quickLiskCollectionModel.setCollectionType(BackofficeSpecialCollectionType.QUICKLIST);
        this.blockedLiskCollectionModel.setCollectionType(BackofficeSpecialCollectionType.BLOCKEDLIST);
    }


    @Test
    public void shouldNotRenderAnyIconWhenCollectionContainsNeitherBlockedOrQuickItems()
    {
        BDDMockito.given(this.shortcutsService.initCollection("blockedlist")).willReturn(this.blockedLiskCollectionModel);
        BDDMockito.given(this.shortcutsService.initCollection("quicklist")).willReturn(this.quickLiskCollectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsService.collectionContainsItem(this.productModel, this.blockedLiskCollectionModel))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(Boolean.valueOf(this.shortcutsService.collectionContainsItem(this.productModel, this.quickLiskCollectionModel))).willReturn(Boolean.valueOf(false));
        this.shortcutsTypeRenderer.render(this.parentComponent, this.configuration, this.productModel, this.dataType, this.widgetInstanceManager);
        ((Component)Mockito.verify(this.parentComponent, Mockito.never())).appendChild((Component)Matchers.any());
        ((ShortcutsTypeRenderer)Mockito.verify(this.shortcutsTypeRenderer, Mockito.never())).createIcon((String)Matchers.any(), (String)Matchers.any());
        ((ShortcutsTypeRenderer)Mockito.verify(this.shortcutsTypeRenderer)).fireComponentRendered(this.parentComponent, this.configuration, this.productModel);
    }


    @Test
    public void shouldRenderBlockedListIconWhenBelongsToBlockedList()
    {
        BDDMockito.given(this.shortcutsService.initCollection("blockedlist")).willReturn(this.blockedLiskCollectionModel);
        BDDMockito.given(this.shortcutsService.initCollection("quicklist")).willReturn(this.quickLiskCollectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsService.collectionContainsItem(this.productModel, this.blockedLiskCollectionModel))).willReturn(Boolean.valueOf(true));
        this.shortcutsTypeRenderer.render(this.parentComponent, this.configuration, this.productModel, this.dataType, this.widgetInstanceManager);
        ((ShortcutsTypeRenderer)Mockito.verify(this.shortcutsTypeRenderer)).createIcon((String)Matchers.eq("blockedlist"), (String)ArgumentMatchers.nullable(String.class));
        ((ShortcutsTypeRenderer)Mockito.verify(this.shortcutsTypeRenderer)).fireComponentRendered(this.parentComponent, this.configuration, this.productModel);
    }


    @Test
    public void shouldRenderQuickListIconWhenBelongsToQuickList()
    {
        BDDMockito.given(this.shortcutsService.initCollection("blockedlist")).willReturn(this.blockedLiskCollectionModel);
        BDDMockito.given(this.shortcutsService.initCollection("quicklist")).willReturn(this.quickLiskCollectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsService.collectionContainsItem(this.productModel, this.blockedLiskCollectionModel))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(Boolean.valueOf(this.shortcutsService.collectionContainsItem(this.productModel, this.quickLiskCollectionModel))).willReturn(Boolean.valueOf(true));
        this.shortcutsTypeRenderer.render(this.parentComponent, this.configuration, this.productModel, this.dataType, this.widgetInstanceManager);
        ((ShortcutsTypeRenderer)Mockito.verify(this.shortcutsTypeRenderer)).createIcon((String)Matchers.eq("quicklist"), (String)ArgumentMatchers.nullable(String.class));
        ((ShortcutsTypeRenderer)Mockito.verify(this.shortcutsTypeRenderer)).fireComponentRendered(this.parentComponent, this.configuration, this.productModel);
    }
}
