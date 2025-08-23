/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.renderer;

import com.hybris.cockpitng.admin.model.tree.MatchingSocketsTreeModelInternalNode;
import com.hybris.cockpitng.admin.strategy.socket.WidgetSocketMatchStrategy;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.impl.DefaultSocketConnectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

public class ConnectionWindowListItemRenderer implements ListitemRenderer<Object>,
                TreeitemRenderer<MatchingSocketsTreeModelInternalNode>
{
    private DefaultSocketConnectionService socketConnectionService;
    private CockpitComponentDefinitionService cockpitComponentDefinitionService;


    @Override
    public void render(final Listitem item, final Object data, final int index)
    {
        if(data instanceof WidgetSocket)
        {
            item.setValue(data);
            final Listcell cell = new Listcell();
            final Object attribute = item.getListbox().getAttribute("item");
            prepareSocketDiv((WidgetSocket)data, ((WidgetSocket)data).getId(), extractTypeLabel(((WidgetSocket)data), attribute),
                            cell);
            item.appendChild(cell);
        }
        else if(data instanceof String || data == null)
        {
            renderString(item, (String)data);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported type passed: " + data);
        }
    }


    @Override
    public void render(final Treeitem item, final MatchingSocketsTreeModelInternalNode data, final int index)
    {
        item.setValue(data);
        final Object value = data.getElement();
        if(value instanceof WidgetSocketMatchStrategy)
        {
            renderString(item, ((WidgetSocketMatchStrategy)value).getStrategyCode());
        }
        else if(value instanceof WidgetSocket)
        {
            final Treerow row = new Treerow();
            final Treecell cell = new Treecell();
            prepareSocketDiv((WidgetSocket)value, ((WidgetSocket)value).getId(), extractTypeLabel(((WidgetSocket)value), null),
                            cell);
            row.appendChild(cell);
            item.appendChild(row);
        }
        else if(value instanceof Widget)
        {
            final String widgetId = ((Widget)value).getId();
            final AbstractCockpitComponentDefinition def = cockpitComponentDefinitionService
                            .getComponentDefinitionForCode(((Widget)value).getWidgetDefinitionId());
            if(def == null)
            {
                renderString(item, widgetId);
            }
            else
            {
                renderString(item, widgetId + " : " + def.getName());
            }
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }


    private void renderString(final Listitem item, final String data)
    {
        item.setLabel(StringUtils.defaultIfBlank(StringUtils.trim(data), StringUtils.EMPTY));
    }


    private void renderString(final Treeitem item, final String data)
    {
        item.setLabel(StringUtils.defaultIfBlank(StringUtils.trim(data), StringUtils.EMPTY));
    }


    private String extractTypeLabel(final WidgetSocket socket, final Object attribute)
    {
        String typeLabel = socket.getDataType();
        final String realType = socketConnectionService.resolveGenericType(socket, attribute instanceof Widget ? (Widget)attribute
                        : null);
        if(StringUtils.isNotBlank(realType) && realType.charAt(0) != '#')
        {
            typeLabel = typeLabel.replace(">", "=" + realType + ">");
        }
        final WidgetSocket.Multiplicity dataMultiplicity = socket.getDataMultiplicity();
        if(dataMultiplicity != null)
        {
            typeLabel = StringUtils.capitalize(dataMultiplicity.getCode()) + "<" + typeLabel + ">";
        }
        return typeLabel;
    }


    private void prepareSocketDiv(final WidgetSocket data, final String label, final String typeLabel,
                    final HtmlBasedComponent parent)
    {
        parent.setSclass("widgetCell");
        final Div cntDiv = new Div();
        cntDiv.setSclass("z-panel-header");
        parent.appendChild(cntDiv);
        final Div nameDiv = new Div();
        cntDiv.appendChild(nameDiv);
        nameDiv.setSclass("widgetName");
        nameDiv.appendChild(new Label(label));
        final Label inOut = new Label(data.isInput() ? "in" : "out");
        inOut.setSclass("inOutLabel");
        nameDiv.appendChild(inOut);
        final Label typeLabelComponent = new Label("Type: " + typeLabel);
        typeLabelComponent.setSclass("typeLabelComponent");
        cntDiv.appendChild(typeLabelComponent);
    }


    @Required
    public void setSocketConnectionService(final DefaultSocketConnectionService socketConnectionService)
    {
        this.socketConnectionService = socketConnectionService;
    }


    @Required
    public void setCockpitComponentDefinitionService(final CockpitComponentDefinitionService cockpitComponentDefinitionService)
    {
        this.cockpitComponentDefinitionService = cockpitComponentDefinitionService;
    }
}
