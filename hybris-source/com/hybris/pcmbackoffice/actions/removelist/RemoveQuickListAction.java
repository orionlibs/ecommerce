package com.hybris.pcmbackoffice.actions.removelist;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;

public class RemoveQuickListAction extends AbstractRemoveShortcutsListAction
{
    private static final String NOTIFICATION_SOURCE_SHORTCUTS_QUICK_LIST_REMOVE_SUCCESS = "shortcutsQuickListProductRemoveSuccess";


    protected String getQualifer()
    {
        return BackofficeSpecialCollectionType.QUICKLIST.getCode();
    }


    protected String getSuccessMessage()
    {
        return "shortcutsQuickListProductRemoveSuccess";
    }
}
