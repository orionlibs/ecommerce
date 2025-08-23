/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import static com.hybris.cockpitng.widgets.compare.renderer.DefaultCompareViewSectionRenderer.MARK_EXPAND_TRIGGER;

import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.compare.impl.DefaultItemComparisonFacade;
import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.config.compareview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class AbstractCompareViewSectionHeaderRenderer<CONFIG>
                extends AbstractWidgetComponentRenderer<TableRow, CONFIG, PartialRendererData<Collection>>
{
    private static final String ATTRIBUTE_ITEM = "item";
    private static final String ATTRIBUTE_SECTION_NAME = "section-name";
    private static final String SCLASS_SECTION_HEADER = "yw-compareview-section-header";
    private static final String SCLASS_SECTION_EXPAND_TRIGGER = "yw-compareview-section-header-expand-trigger";
    private static final String SCLASS_SECTION_HEADER_MARK = "yw-compareview-mark-icon";
    private static final String SCLASS_SECTION_NAME_LABEL = "yw-compareview-section-name-label";
    private static final String SCLASS_SECTION_NAME_CELL = "yw-compareview-section-name";
    private WidgetRenderingUtils widgetRenderingUtils;
    private ItemComparisonFacade itemComparisonFacade;
    private ObjectFacade objectFacade;


    protected abstract String getSectionName(final CONFIG configuration);


    protected String getTranslatedSectionName(final CONFIG configuration, final WidgetInstanceManager widgetInstanceManager)
    {
        final String localizedLabelByWidget = widgetInstanceManager.getLabel(getSectionName(configuration));
        return StringUtils.isNotBlank(localizedLabelByWidget) ? localizedLabelByWidget
                        : Labels.getLabel(getSectionName(configuration), getSectionName(configuration));
    }


    @Override
    public void render(final TableRow parent, final CONFIG configuration, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final TableComponentIterator<TableCell> cells = parent.cellsIterator();
        final TableCell title = cells.request();
        if(requiresSectionHeaderTitleRendering(title, configuration, data, dataType, widgetInstanceManager))
        {
            renderSectionHeaderTitle(title, configuration, data, dataType, widgetInstanceManager);
        }
        sectionHeaderTitleRendered(title, configuration, data, dataType, widgetInstanceManager);
        fireComponentRendered(title, parent, configuration, data);
        data.getData().forEach(item -> {
            final TableCell cell = cells.request();
            if(requiresSectionHeaderRendering(cell, configuration, data, item, dataType, widgetInstanceManager))
            {
                renderSectionHeader(cell, configuration, data, item, dataType, widgetInstanceManager);
                getWidgetRenderingUtils().moveMarkedComponents(cell, parent);
            }
            sectionHeaderRendered(cell, configuration, data, item, dataType, widgetInstanceManager);
            fireComponentRendered(cell, parent, configuration, data);
        });
        cells.removeRemaining();
        getWidgetRenderingUtils().moveMarkedComponents(title, parent);
        fireComponentRendered(parent, configuration, data);
    }


    protected boolean requiresSectionHeaderTitleRendering(final TableCell title, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return title.getChildren().isEmpty()
                        || ObjectUtils.notEqual(getSectionName(configuration), title.getAttribute(ATTRIBUTE_SECTION_NAME));
    }


    protected void renderSectionHeaderTitle(final TableCell parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        parent.getChildren().clear();
        parent.appendChild(createSectionHeaderTitle(parent, configuration, data, dataType, widgetInstanceManager));
    }


    protected Component createSectionHeaderTitle(final Component parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div container = new Div();
        DefaultCompareViewLayout.markAsContentsContainer(container);
        UITools.addSClass(container, SCLASS_SECTION_HEADER);
        final Component sectionHeaderLabel = createSectionHeaderTitleLabel(parent, configuration, data, dataType,
                        widgetInstanceManager);
        container.appendChild(sectionHeaderLabel);
        createSectionHeaderTitleSuffixLabel(parent, configuration, data, dataType, widgetInstanceManager).ifPresent(container::appendChild);
        container.appendChild(createSectionHeaderTitleExpandTrigger(parent, configuration, data, dataType, widgetInstanceManager));
        return container;
    }


    protected Component createSectionHeaderTitleLabel(final Component parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final String sectionHeaderLabelText = getTranslatedSectionName(configuration, widgetInstanceManager);
        final Label sectionHeaderLabel = new Label(sectionHeaderLabelText);
        final String tooltipText = getTooltipText(configuration);
        sectionHeaderLabel.setTooltiptext(tooltipText != null ? createTooltipText(tooltipText) : sectionHeaderLabelText);
        UITools.addSClass(sectionHeaderLabel, SCLASS_SECTION_NAME_LABEL);
        return sectionHeaderLabel;
    }


    protected Optional<Component> createSectionHeaderTitleSuffixLabel(final Component parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return Optional.empty();
    }


    protected Component createSectionHeaderTitleExpandTrigger(final Component parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Button expandTrigger = new Button();
        UITools.addSClass(expandTrigger, SCLASS_SECTION_EXPAND_TRIGGER);
        getWidgetRenderingUtils().markComponent(parent, expandTrigger, MARK_EXPAND_TRIGGER, null);
        return expandTrigger;
    }


    protected abstract String getTooltipText(final CONFIG configuration);


    protected String createTooltipText(final String labelKey)
    {
        if(StringUtils.isBlank(labelKey))
        {
            return "";
        }
        final String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }


    //@SuppressWarnings("unused")
    protected void sectionHeaderTitleRendered(final TableCell title, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        UITools.addSClass(title, SCLASS_SECTION_NAME_CELL);
        if(isNotEqual(data.getComparisonResult(), configuration))
        {
            DefaultCompareViewLayout.markAsNotEqual(title);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(title);
        }
        title.setAttribute(ATTRIBUTE_SECTION_NAME, getSectionName(configuration));
        title.setSticky(true);
    }


    protected void renderSectionHeader(final TableCell parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        parent.getChildren().clear();
        final Component sectionHeader = createSectionHeader(configuration, data, item, dataType, widgetInstanceManager);
        parent.appendChild(sectionHeader);
        getWidgetRenderingUtils().moveMarkedComponents(sectionHeader, parent);
    }


    protected boolean requiresSectionHeaderRendering(final TableCell cell, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return cell.getChildren().isEmpty() || ObjectUtils.notEqual(item, cell.getAttribute(ATTRIBUTE_ITEM));
    }


    protected Component createSectionHeader(final CONFIG configuration, final PartialRendererData<Collection> data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div sectionHeader = new Div();
        DefaultCompareViewLayout.markAsContentsContainer(sectionHeader);
        final Div sectionHeaderDifferenceMarker = new Div();
        sectionHeaderDifferenceMarker.setSclass(SCLASS_SECTION_HEADER_MARK);
        sectionHeader.appendChild(sectionHeaderDifferenceMarker);
        return sectionHeader;
    }


    protected void sectionHeaderRendered(final TableCell cell, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Object id = getObjectFacade().getObjectId(item);
        if(isNotEqual(data.getComparisonResult(), configuration, id))
        {
            DefaultCompareViewLayout.markAsNotEqual(cell);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(cell);
        }
        cell.setAttribute(ATTRIBUTE_ITEM, item);
        final boolean isReferenceItem = getItemComparisonFacade().isSameItem(data.getComparisonState().getReference(), item);
        cell.setSticky(isReferenceItem);
    }


    /**
     * Checks if there is any difference in a group for reference object
     *
     * @param result
     *           ComparisonResult loaded from
     *           engine{@link DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param configuration
     *           group configuration
     * @return true if reference object has any difference in context of group, false otherwise
     */
    protected abstract boolean isNotEqual(final ComparisonResult result, final CONFIG configuration);


    /**
     * Checks if there is any difference in a group for reference object
     *
     * @param result
     *           ComparisonResult loaded from
     *           engine{@link DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param section
     *           tested section for contains differences to reference object
     * @param itemId
     *           ID of the item which is compared to reference object
     * @return true if any of attributes from section of item has any difference in context of group(section) and reference
     *         object, false otherwise
     */
    protected abstract boolean isNotEqual(final ComparisonResult result, final CONFIG section, final Object itemId);


    protected boolean isItemAttributeDifferentThanCorrespondingReferenceItemAttribute(final ComparisonResult result,
                    final Object itemId, final Attribute attribute)
    {
        return result.getObjectsIdWithDifferences(new CompareAttributeDescriptor(attribute.getQualifier())).stream()
                        .map(ObjectAttributesValueContainer::getObjectId).anyMatch(o -> o.equals(itemId));
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


    protected ItemComparisonFacade getItemComparisonFacade()
    {
        return itemComparisonFacade;
    }


    @Required
    public void setItemComparisonFacade(final ItemComparisonFacade itemComparisonFacade)
    {
        this.itemComparisonFacade = itemComparisonFacade;
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
