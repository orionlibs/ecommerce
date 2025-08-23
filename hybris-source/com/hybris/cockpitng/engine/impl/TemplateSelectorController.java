/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class TemplateSelectorController extends ViewAnnotationAwareComposer
{
    public static final String ON_TEMPLATE_SELECTED = "onTemplateSelected";
    private static final long serialVersionUID = 1L;
    private transient CockpitComponentDefinitionService componentDefinitionService;
    private Component mainComponent;
    private Listbox templateSelectorListbox;
    private final transient ListitemRenderer<Widget> itemRenderer = new ListitemRenderer<Widget>()
    {
        public void render(final Listitem item, final Widget data, final int index) throws Exception
        {
            final Listcell cell = new Listcell();
            cell.setSclass("widgetCell");
            item.appendChild(cell);
            final Div cntDiv = new Div();
            cntDiv.setSclass("z-panel-header");
            cell.appendChild(cntDiv);
            final Div nameDiv = new Div();
            cntDiv.appendChild(nameDiv);
            nameDiv.setSclass("widgetName");
            final AbstractCockpitComponentDefinition definition = componentDefinitionService.getComponentDefinitionForCode(data
                            .getWidgetDefinitionId());
            nameDiv.appendChild(new Label(definition.getName()));
            cell.setTooltiptext(definition.getDescription());
            item.setValue(data);
        }
    };


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        mainComponent = comp;
        super.doAfterCompose(comp);
        final Widget parentWidget = getParentWidget(comp);
        if(parentWidget != null)
        {
            templateSelectorListbox.setItemRenderer(itemRenderer);
            final Widgetchildren widgetchildren = getParentWidgetchildrenComponent(comp);
            final String slotId = (widgetchildren == null ? null : widgetchildren.getSlotID());
            templateSelectorListbox.setModel(new SimpleListModel<>(getTemplates(parentWidget, slotId)));
        }
    }


    private Widget getParentWidget(final Component comp)
    {
        return WidgetTreeUIUtils.getParentWidgetslot(comp).getWidgetInstance().getWidget();
    }


    private Widgetchildren getParentWidgetchildrenComponent(final Component comp)
    {
        return WidgetTreeUIUtils.getParentWidgetchildren(comp);
    }


    private List<Widget> getTemplates(final Widget widget, final String slotId)
    {
        final List<Widget> result = new ArrayList<>();
        for(final Widget child : widget.getChildren())
        {
            if(child.isTemplate() && Objects.equals(slotId, child.getSlotId()))
            {
                result.add(child);
            }
        }
        return result;
    }


    @ViewEvent(componentID = "templateSelectorListbox", eventName = Events.ON_SELECT)
    public void doOnTemplateSelected()
    {
        Events.postEvent(ON_TEMPLATE_SELECTED, mainComponent, templateSelectorListbox.getSelectedItem().getValue());
        templateSelectorListbox.clearSelection();
    }
}
