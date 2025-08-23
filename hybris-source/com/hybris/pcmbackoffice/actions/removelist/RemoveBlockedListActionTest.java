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

public class RemoveBlockedListActionTest extends AbstractActionUnitTest<RemoveBlockedListAction>
{
    @InjectMocks
    @Spy
    private RemoveBlockedListAction removeBlockedListAction;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ShortcutsService shortcutsService;
    @Mock
    private ShortcutsUtilService shortcutsUtilService;
    @Mock
    private List<ItemModel> listItems;
    @Mock
    private ActionContext<Object> actionContext;
    @Mock
    private BackofficeObjectSpecialCollectionModel collectionModel;
    private static final String collectionCode = BackofficeSpecialCollectionType.BLOCKEDLIST.getCode();
    private static final String NOTIFICATION_SOURCE_SHORTCUTS_BLOCKED_LIST_REMOVE_SUCCESS = "shortcutsBlockedListProductRemoveSuccess";


    public RemoveBlockedListAction getActionInstance()
    {
        return this.removeBlockedListAction;
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
    public void shouldNotPerformRemoveWhenGetNullObject()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(null);
        boolean result = this.removeBlockedListAction.canPerform(this.actionContext);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotPerformRemoveWhenCollectionIsEmpty()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(new ArrayList());
        boolean result = this.removeBlockedListAction.canPerform(this.actionContext);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldPerformRemoveWhenGetNotNullItemList()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(this.listItems);
        boolean result = this.removeBlockedListAction.canPerform(this.actionContext);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotPerformRemoveWhenItemAlreadyRemoved()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(this.listItems);
        BDDMockito.given(this.shortcutsService.initCollection(collectionCode)).willReturn(this.collectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsUtilService.isItemsAlreadyDeleted(this.listItems))).willReturn(Boolean.valueOf(true));
        ActionResult result = this.removeBlockedListAction.perform(this.actionContext);
        Assertions.assertThat(result.getResultCode()).isSameAs("error");
    }


    @Test
    public void shouldRemoveSuccessWhenItemsAlreadyExist()
    {
        BDDMockito.given(this.actionContext.getData()).willReturn(this.listItems);
        BDDMockito.given(this.shortcutsService.initCollection(collectionCode)).willReturn(this.collectionModel);
        BDDMockito.given(Boolean.valueOf(this.shortcutsUtilService.isItemsAlreadyDeleted(this.listItems))).willReturn(Boolean.valueOf(false));
        ActionResult result = this.removeBlockedListAction.perform(this.actionContext);
        Assertions.assertThat(result.getResultCode()).isSameAs("success");
        ((NotificationService)Mockito.verify(this.notificationService)).notifyUser("shortcutsBlockedListProductRemoveSuccess", "General", NotificationEvent.Level.SUCCESS, new Object[0]);
    }
}
