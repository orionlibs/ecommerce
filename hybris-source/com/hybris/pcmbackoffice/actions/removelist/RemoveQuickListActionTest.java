package com.hybris.pcmbackoffice.actions.removelist;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;
import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.pcmbackoffice.widgets.shortcuts.ShortcutsUtilService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

public class RemoveQuickListActionTest extends AbstractActionUnitTest<RemoveQuickListAction>
{
    @InjectMocks
    @Spy
    private RemoveQuickListAction removeQuickListAction;
    @Mock
    protected NotificationService notificationService;
    @Mock
    protected ShortcutsService shortcutsService;
    @Mock
    protected ShortcutsUtilService shortcutsUtilService;
    @Mock
    private List<ItemModel> listItems;
    @Mock
    private ActionContext<Object> actionContext;
    @Mock
    private BackofficeObjectSpecialCollectionModel collectionModel;
    private static final String collectionCode = BackofficeSpecialCollectionType.QUICKLIST.getCode();
    private static final String NOTIFICATION_SOURCE_SHORTCUTS_QUICK_LIST_REMOVE_SUCCESS = "shortcutsQuickListProductRemoveSuccess";


    public RemoveQuickListAction getActionInstance()
    {
        return this.removeQuickListAction;
    }


    @Before
    public void setup()
    {
        ItemModel item1 = new ItemModel();
        ItemModel item2 = new ItemModel();
        ItemModel item3 = new ItemModel();
        this.listItems = Arrays.asList(new ItemModel[] {item1, item2, item3});
    }


    @Test
    public void canNotPerformRemoveWhenGetNullObject()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(null);
        boolean result = this.removeQuickListAction.canPerform(this.actionContext);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void canNotPerformRemoveWhenCollectionIsEmpty()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(new ArrayList());
        boolean result = this.removeQuickListAction.canPerform(this.actionContext);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void canPerformRemoveWhenGetNotNullItemList()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(this.listItems);
        boolean result = this.removeQuickListAction.canPerform(this.actionContext);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotPerformRemoveWhenItemAlreadyRemoved()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(this.listItems);
        BDDMockito.given(this.shortcutsService.initCollection(collectionCode)).willReturn(this.collectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsUtilService.isItemsAlreadyDeleted(this.listItems))).willReturn(Boolean.valueOf(true));
        ActionResult result = this.removeQuickListAction.perform(this.actionContext);
        Assertions.assertThat(result.getResultCode()).isSameAs("error");
    }


    @Test
    public void shouldRemoveSuccessWhenItemsAlradyExixt()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(this.listItems);
        BDDMockito.given(this.shortcutsService.initCollection(collectionCode)).willReturn(this.collectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsUtilService.isItemsAlreadyDeleted(this.listItems))).willReturn(Boolean.valueOf(false));
        ActionResult result = this.removeQuickListAction.perform(this.actionContext);
        Assertions.assertThat(result.getResultCode()).isSameAs("success");
        ((NotificationService)Mockito.verify(this.notificationService)).notifyUser("shortcutsQuickListProductRemoveSuccess", "General", NotificationEvent.Level.SUCCESS, new Object[0]);
    }
}
