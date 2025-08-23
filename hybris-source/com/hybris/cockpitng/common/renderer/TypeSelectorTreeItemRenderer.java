/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.renderer;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

public class TypeSelectorTreeItemRenderer implements TreeitemRenderer<DataType>
{
    public static final int DEFAULT_ELLIPSIS_LENGTH = 30;
    public static final String TYPE_SELECTOR_ELLIPSIS_LENGTH_SETTING = "typeSelectorEllipsisLength";
    protected static final String ALL_ARGUMENTS_ARE_MANDATORY_MSG = "All arguments are mandatory";
    private static final Logger LOG = LoggerFactory.getLogger(TypeSelectorTreeItemRenderer.class);
    protected final LabelService labelService;
    protected final int ellipsisLength;
    private PermissionFacade permissionFacade;


    public TypeSelectorTreeItemRenderer(final LabelService labelService, final WidgetInstanceManager widgetInstanceManager)
    {
        Validate.notNull(ALL_ARGUMENTS_ARE_MANDATORY_MSG, labelService, widgetInstanceManager);
        this.labelService = labelService;
        this.ellipsisLength = widgetInstanceManager == null ? DEFAULT_ELLIPSIS_LENGTH
                        : TypeSelectorTreeItemRenderer.this.getEllipsisLength(widgetInstanceManager);
    }


    public TypeSelectorTreeItemRenderer(final LabelService labelService, final ActionContext<?> context)
    {
        Validate.notNull(ALL_ARGUMENTS_ARE_MANDATORY_MSG, labelService, context);
        this.labelService = labelService;
        this.ellipsisLength = context == null ? DEFAULT_ELLIPSIS_LENGTH
                        : TypeSelectorTreeItemRenderer.this.getEllipsisLength(context);
    }


    public TypeSelectorTreeItemRenderer(final LabelService labelService, final EditorContext<?> context)
    {
        Validate.notNull(ALL_ARGUMENTS_ARE_MANDATORY_MSG, labelService, context);
        this.labelService = labelService;
        this.ellipsisLength = context == null ? DEFAULT_ELLIPSIS_LENGTH
                        : TypeSelectorTreeItemRenderer.this.getEllipsisLength(context);
    }


    protected int getEllipsisLength(final WidgetInstanceManager widgetInstanceManager)
    {
        if(widgetInstanceManager.getWidgetSettings().containsKey(TYPE_SELECTOR_ELLIPSIS_LENGTH_SETTING))
        {
            return widgetInstanceManager.getWidgetSettings().getInt(TYPE_SELECTOR_ELLIPSIS_LENGTH_SETTING);
        }
        return DEFAULT_ELLIPSIS_LENGTH;
    }


    protected int getEllipsisLength(final ActionContext context)
    {
        return extractIntValueFromParameter(context.getParameter(TYPE_SELECTOR_ELLIPSIS_LENGTH_SETTING));
    }


    protected int getEllipsisLength(final EditorContext context)
    {
        return extractIntValueFromParameter(context.getParameter(TYPE_SELECTOR_ELLIPSIS_LENGTH_SETTING));
    }


    protected int extractIntValueFromParameter(final Object parameter)
    {
        if(parameter instanceof String)
        {
            try
            {
                return Integer.parseInt((String)parameter);
            }
            catch(final NumberFormatException nfe)
            {
                LOG.warn(nfe.getMessage(), nfe);
            }
        }
        else if(parameter instanceof Number)
        {
            return ((Number)parameter).intValue();
        }
        return DEFAULT_ELLIPSIS_LENGTH;
    }


    @Override
    public void render(final Treeitem item, final DataType type, final int index)
    {
        final String tmpLabel = labelService.getObjectLabel(type.getCode());
        final String label = StringUtils.isNotBlank(tmpLabel) ? tmpLabel : type.getCode();
        if(ellipsisLength > 0 && label.length() > ellipsisLength)
        {
            item.setLabel(UITools.truncateText(label, ellipsisLength));
        }
        else
        {
            item.setLabel(label);
        }
        item.setTooltiptext(label);
        item.setValue(type);
        item.setDisabled(isDisabled(type));
        UITools.modifySClass(item.getTreerow(), getSClass(item), true);
    }


    protected String getSClass(final Treeitem item)
    {
        return "ye-create-type-selector-lvl-" + item.getLevel();
    }


    protected boolean isDisabled(final DataType type)
    {
        return !type.isSearchable() || !permissionFacade().canReadType(type.getCode());
    }


    protected PermissionFacade permissionFacade()
    {
        return BackofficeSpringUtil.getBean("permissionFacade", PermissionFacade.class);
    }
}
