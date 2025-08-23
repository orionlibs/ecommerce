package com.hybris.pcmbackoffice.actions.removelist;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;

public class RemoveBlockedListAction extends AbstractRemoveShortcutsListAction
{
    private static final String NOTIFICATION_SOURCE_SHORTCUTS_BLOCKED_LIST_REMOVE_SUCCESS = "shortcutsBlockedListProductRemoveSuccess";


    protected String getQualifer()
    {
        return BackofficeSpecialCollectionType.BLOCKEDLIST.getCode();
    }


    protected String getSuccessMessage()
    {
        return "shortcutsBlockedListProductRemoveSuccess";
    }
}
