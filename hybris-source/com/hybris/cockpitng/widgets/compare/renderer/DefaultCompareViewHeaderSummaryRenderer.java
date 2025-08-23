/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.CompareViewController;
import com.hybris.cockpitng.widgets.compare.model.ComparisonState;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultCompareViewHeaderSummaryRenderer
                extends AbstractWidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Collection>>
{
    private static final String ATTRIBUTE_ITEMS_COUNT = "items-count";
    private static final String SCLASS_SELECTION_LABEL = "yw-compareview-header-summary-label";
    private static final String SCLASS_ADD_ITEMS_LABEL = "yw-compareview-header-summary-add";
    private static final String SCLASS_CELL_SUMMARY = "yw-compareview-header-summary-item";
    private static final String SCLASS_CELL_EMPTY = "yw-compareview-header-summary-empty";
    private static final String SCLASS_TEXT_BUTTON = "y-btn";
    private static final String SCLASS_CHECKBOX_SWITCH = "ye-switch-checkbox";
    private static final String SCLASS_CHECKBOX_DIFF_ONLY = "yw-compareview-diff-only-checkbox";
    private static final String LABEL_ITEM_SELECTED = "item.selected";
    private static final String LABEL_ITEMS_SELECTED = "items.selected";
    private static final String LABEL_ADD_ITEMS = "items.add";
    private static final String LABEL_DIFF_ONLY = "show.diff.only";
    private static final String YATFTESTID_DIFF_ONLY = "diffOnlySwitch";
    private WidgetRenderingUtils widgetRenderingUtils;


    @Override
    public void render(final TableCell parent, final CompareView configuration, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        if(requiresSummaryRendering(parent, configuration, data, dataType, widgetInstanceManager))
        {
            renderSummary(parent, configuration, data, dataType, widgetInstanceManager);
        }
        summaryRendered(parent, configuration, data, dataType, widgetInstanceManager);
        fireComponentRendered(parent, configuration, data);
    }


    //@SuppressWarnings("unused")
    protected boolean requiresSummaryRendering(final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return ObjectUtils.notEqual(Integer.valueOf(data.getData().size()), parent.getAttribute(ATTRIBUTE_ITEMS_COUNT));
    }


    protected void renderSummary(final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        parent.getChildren().clear();
        final Div container = new Div();
        DefaultCompareViewLayout.markAsContentsContainer(container);
        fillSummaryContainer(container, parent, configuration, data, dataType, widgetInstanceManager);
        parent.appendChild(container);
    }


    protected void fillSummaryContainer(final Component container, final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        container.appendChild(createSummaryContent(configuration, data, dataType, widgetInstanceManager));
        container.appendChild(createAddItemContent(parent, configuration, data, dataType, widgetInstanceManager));
        container.appendChild(createDiffOnlySwitch(parent, widgetInstanceManager, data));
    }


    protected Component createSummaryContent(final CompareView configuration, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final int itemsCount = data.getData().size();
        final String labelKey = itemsCount == 1 ? LABEL_ITEM_SELECTED : LABEL_ITEMS_SELECTED;
        final String labelValue = String.valueOf(itemsCount) + " " + widgetInstanceManager.getLabel(labelKey);
        final Label label = new Label(labelValue);
        label.setSclass(SCLASS_SELECTION_LABEL);
        label.setAttribute(ATTRIBUTE_ITEMS_COUNT, Integer.valueOf(itemsCount));
        return label;
    }


    protected Checkbox createDiffOnlySwitch(final Component parent, final WidgetInstanceManager widgetInstanceManager,
                    final PartialRendererData<Collection> data)
    {
        final Checkbox diffOnlySwitch = new Checkbox(widgetInstanceManager.getLabel(LABEL_DIFF_ONLY));
        diffOnlySwitch.setChecked(data.isDiffOnly());
        UITools.addSClass(diffOnlySwitch, SCLASS_CHECKBOX_SWITCH);
        UITools.addSClass(diffOnlySwitch, SCLASS_CHECKBOX_DIFF_ONLY);
        YTestTools.modifyYTestId(diffOnlySwitch, YATFTESTID_DIFF_ONLY);
        getWidgetRenderingUtils().markComponent(parent, diffOnlySwitch, CompareViewController.MARK_NAME_DIFF_ONLY, null);
        return diffOnlySwitch;
    }


    protected Component createAddItemContent(final Component parent, final CompareView configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Button addItemButton = new Button(widgetInstanceManager.getLabel(LABEL_ADD_ITEMS));
        addItemButton.setAutodisable("self");
        assignSClassToAddItemButton(addItemButton);
        getWidgetRenderingUtils().markComponent(parent, addItemButton, CompareViewController.MARK_NAME_ADD_ITEMS, null);
        return addItemButton;
    }


    protected void assignSClassToAddItemButton(final HtmlBasedComponent htmlBasedComponent)
    {
        UITools.addSClass(htmlBasedComponent, SCLASS_ADD_ITEMS_LABEL);
        UITools.addSClass(htmlBasedComponent, SCLASS_TEXT_BUTTON);
    }


    protected void summaryRendered(final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        UITools.addSClass(parent, SCLASS_CELL_SUMMARY);
        UITools.modifySClass(parent, SCLASS_CELL_EMPTY, CollectionUtils.isEmpty(data.getData()));
        if(ComparisonState.Status.FINISHED != data.getComparisonState().getStatus())
        {
            DefaultCompareViewLayout.markAsDuringCalculation(parent);
        }
        else
        {
            DefaultCompareViewLayout.markAsCalculated(parent);
        }
        parent.setSticky(true);
        parent.setAttribute(ATTRIBUTE_ITEMS_COUNT, Integer.valueOf(data.getData().size()));
    }


    protected WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Required
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }
}
