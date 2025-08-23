/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.locking;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.locking.ItemLockingService;
import java.util.Collection;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;

public class ToggleItemLockActionRenderer extends DefaultActionRenderer<Object, Object>
{
    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected static final String ICON_ACTION_UNLOCK_ITEM_HOVER_PNG = "icon_action_unlock_item_hover.png";
    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected static final String ICON_ACTION_LOCK_ITEM_HOVER_PNG = "icon_action_lock_item_hover.png";
    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected static final String ICON_ACTION_UNLOCK_ITEM_DEFAULT_PNG = "icon_action_unlock_item_default.png";
    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected static final String ICON_ACTION_LOCK_ITEM_DEFAULT_PNG = "icon_action_lock_item_default.png";
    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected static final String ICON_ACTION_UNLOCK_ITEM_DISABLED_PNG = "icon_action_unlock_item_disabled.png";
    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected static final String ICON_ACTION_LOCK_ITEM_DISABLED_PNG = "icon_action_lock_item_disabled.png";
    protected static final String I18N_UNLOCK_ACTION_TOOLTIP = "perform.unlock.tooltip";
    protected static final String I18N_LOCK_ACTION_TOOLTIP = "perform.lock.tooltip";
    private static final String ICON_ACTION_UNLOCK_ITEM_DEFAULT_FONT_NAME = "unlocked";
    private static final String ICON_ACTION_LOCK_ITEM_DEFAULT_FONT_NAME = "locked";
    @Resource
    private ItemLockingService itemLockingService;


    @Override
    protected String getIconUri(final ActionContext<Object> context, final boolean canPerform)
    {
        String iconName = ICON_ACTION_UNLOCK_ITEM_DEFAULT_FONT_NAME;
        if(isLocked(context.getData()))
        {
            iconName = ICON_ACTION_LOCK_ITEM_DEFAULT_FONT_NAME;
        }
        context.setParameter("iconUri", iconName);
        return iconName;
    }


    /**
     * @deprecated in 2205
     */
    @Deprecated(since = "2205")
    protected String getLockedIconUri(final Object data, final String lockedFileName, final String unlockedFileName)
    {
        return "";
    }


    @Override
    protected String getLocalizedName(final ActionContext<?> context)
    {
        final String result;
        if(isLocked(context.getData()))
        {
            result = context.getLabel(I18N_UNLOCK_ACTION_TOOLTIP);
        }
        else
        {
            result = context.getLabel(I18N_LOCK_ACTION_TOOLTIP);
        }
        return StringUtils.defaultIfBlank(result, super.getLocalizedName(context));
    }


    protected boolean isLocked(final Object data)
    {
        final ItemLockingService lockingService = getItemLockingService();
        if(data instanceof ItemModel)
        {
            return lockingService.isLocked((ItemModel)data);
        }
        else if(data instanceof Collection && !((Collection)data).isEmpty())
        {
            return ((Collection<?>)data).stream().allMatch(this::isLocked);
        }
        return false;
    }


    protected ItemLockingService getItemLockingService()
    {
        return itemLockingService;
    }
}
