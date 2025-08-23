/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ext.Selectable;

/**
 * Renders list entries using {@link com.hybris.cockpitng.labels.LabelService}.
 */
public class DefaultListViewRenderer extends AbstractWidgetComponentRenderer<Listitem, ListView, Object>
{
    protected static final String SCLASS_LISTVIEW_CELL = "yw-listview-cell";
    protected static final String SCLASS_LISTVIEW_CELL_FILL = "yw-listview-cell-fill";
    protected static final String SCLASS_LISTVIEW_SEL_ROW = "yw-listview-row-sel";
    private static final String SCLASS_LISTVIEW_READ_RESTRICTED = "yw-listview-cell-restricted";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListViewRenderer.class);
    private PermissionFacade permissionFacade;
    private TypeFacade typeFacade;
    private ObjectValueService objectValueService;
    private LabelService labelService;
    private WidgetComponentRenderer<Listcell, ListColumn, Object> defaultCellRenderer;


    @Override
    public void render(final Listitem row, final ListView listConfig, final Object entry, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        updateRowSelectedSclass(row, entry);
        try
        {
            final List<ListColumn> properties = listConfig != null ? listConfig.getColumn() : null;
            if(CollectionUtils.isNotEmpty(properties))
            {
                for(final ListColumn column : properties)
                {
                    renderCell(row, listConfig, entry, dataType, widgetInstanceManager, column);
                }
                addFillColumnIfNecessary(row, listConfig, entry);
                updateReadRestrictedSclass(row, entry);
            }
            fireComponentRendered(row, row, listConfig, entry);
        }
        catch(final Exception e)
        {
            LOG.error("Could not render row.", e);
        }
    }


    private boolean canReadInstance(final Object entry)
    {
        return getPermissionFacade().canReadInstance(entry);
    }


    private void renderCell(final Listitem row, final ListView listConfig, final Object entry, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final ListColumn column)
    {
        final Listcell cell = new Listcell();
        cell.setValue(entry);
        cell.setSclass(SCLASS_LISTVIEW_CELL);
        if(canReadInstance(entry))
        {
            final WidgetComponentRenderer<Listcell, ListColumn, Object> renderer = getColumnCellRenderer(column,
                            widgetInstanceManager, dataType);
            if(BooleanUtils.isTrue(column.isAutoExtract()) && StringUtils.isNotBlank(column.getQualifier()))
            {
                renderExtractedValue(entry, widgetInstanceManager, row, listConfig, column, cell, renderer);
            }
            else
            {
                new ProxyRenderer<>(this, row, listConfig, entry).render(renderer, cell, column, entry, dataType,
                                widgetInstanceManager);
            }
            row.setValue(entry);
        }
        else
        {
            cell.setLabel(getLabelService().getAccessDeniedLabel(entry));
        }
        row.appendChild(cell);
        fireComponentRendered(cell, row, listConfig, entry);
    }


    private void updateReadRestrictedSclass(final Listitem row, final Object entry)
    {
        if(!canReadInstance(entry))
        {
            UITools.modifySClass(row, SCLASS_LISTVIEW_READ_RESTRICTED, true);
        }
    }


    private void updateRowSelectedSclass(final Listitem row, final Object entry)
    {
        final ListModel<Object> model = row.getListbox().getModel();
        if((model instanceof Selectable) && ((Selectable)model).isSelected(entry))
        {
            UITools.modifySClass(row, SCLASS_LISTVIEW_SEL_ROW, true);
        }
    }


    private void addFillColumnIfNecessary(final Listitem row, final ListView listConfig, final Object entry)
    {
        if(allColumnsHaveHflexMin(listConfig.getColumn()))
        {
            final Listcell fill = new Listcell();
            fill.setValue(entry);
            fill.setSclass(String.format("%s %s", SCLASS_LISTVIEW_CELL, SCLASS_LISTVIEW_CELL_FILL));
            row.appendChild(fill);
            fireComponentRendered(fill, row, listConfig, entry);
        }
    }


    protected boolean allColumnsHaveHflexMin(final List<ListColumn> columns)
    {
        return columns.stream().allMatch(column -> StringUtils.isBlank(column.getHflex()) || "min".equals(column.getHflex()));
    }


    private void renderExtractedValue(final Object entry, final WidgetInstanceManager widgetInstanceManager, final Listitem item,
                    final ListView config, final ListColumn column, final Listcell cell,
                    final WidgetComponentRenderer<Listcell, ListColumn, Object> renderer)
    {
        final Object value = getObjectValueService().getValue(column.getQualifier(), entry);
        DataType type = null;
        try
        {
            type = value == null ? DataType.NULL : getTypeFacade().load(getTypeFacade().getType(value));
        }
        catch(final TypeNotFoundException e)
        {
            LOG.warn("Could not load type information", e);
        }
        new ProxyRenderer<>(this, item, config, entry).render(renderer, cell, column, value, type, widgetInstanceManager);
    }


    protected WidgetComponentRenderer<Listcell, ListColumn, Object> getColumnCellRenderer(final ListColumn column,
                    final WidgetInstanceManager widgetInstanceManager, final DataType dataType)
    {
        final String rendererKey = composeRendererKey(column);
        WidgetComponentRenderer<Listcell, ListColumn, Object> result = getWidgetModelValue(widgetInstanceManager, rendererKey,
                        WidgetComponentRenderer.class);
        if(result == null)
        {
            if(StringUtils.isNotBlank(column.getClazz()))
            {
                result = BackofficeSpringUtil.createClassInstance(column.getClazz(), WidgetComponentRenderer.class);
                if(StringUtils.isNotBlank(column.getSpringBean()))
                {
                    LOG.warn("Defined both class[{}] and bean[{}] component renderer. Using class declaration.", column.getClazz(),
                                    column.getSpringBean());
                }
            }
            else if(StringUtils.isNotBlank(column.getSpringBean()))
            {
                result = BackofficeSpringUtil.getBean(column.getSpringBean(), WidgetComponentRenderer.class);
            }
            else
            {
                result = getDefaultCellRenderer();
            }
            widgetInstanceManager.getModel().setValue(rendererKey, result);
        }
        return result;
    }


    private String composeRendererKey(final ListColumn column)
    {
        return String.format("%s_%s", column.getClazz(), column.getSpringBean());
    }


    protected WidgetComponentRenderer<Listcell, ListColumn, Object> getDefaultCellRenderer()
    {
        return defaultCellRenderer;
    }


    @Required
    public void setDefaultCellRenderer(final WidgetComponentRenderer defaultCellRenderer)
    {
        this.defaultCellRenderer = defaultCellRenderer;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected <E> E getWidgetModelValue(final WidgetInstanceManager widgetInstanceManager, final String key, final Class<E> clazz)
    {
        return widgetInstanceManager.getModel().getValue(key, clazz);
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
