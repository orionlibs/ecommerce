/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.compare.impl.DefaultItemComparisonFacade;
import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.CompareLocalizedAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.TableRowsGroup;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.config.compareview.jaxb.Attribute;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.impl.IdentifiableMarkEventListener;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.LocalizedAttributeInfo;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.summaryview.CustomRendererClassUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

public class DefaultCompareViewSectionRenderer extends AbstractCompareViewSectionRenderer<Section, Attribute>
{
    public static final String ATTRIBUTE_LOCALIZED_ATTRIBUTE_EXPAND_STATE_HOLDER = "localized-attribute-expand-state-holder";
    private static final String LISTENER_EXPAND_LOCALIZED_ATTRIBUTE = "expand-localized-attribute-listener";
    private static final String SCLASS_LOCALIZED_ROW_CURRENT = "yw-compareview-localized-row-current";
    private static final String SCLASS_LOCALIZED_ROW_TRANSLATION = "yw-compareview-localized-row-translation";
    private static final String SCLASS_LOCALIZED_ROW_INDIFFERENT = "yw-compareview-localized-row-indifferent";
    private static final String SCLASS_LOCEDITOR_OPENED = "yw-loceditor-fixed-open";
    private static final String SCLASS_LOCEDITOR_CLOSED = "yw-loceditor-fixed-closed";


    @Override
    protected String getConfiguredSectionIdentifier(final Section configuration)
    {
        return configuration.getName();
    }


    @Override
    protected boolean isSectionContentRendered(final TableRowsGroup parent, final Section configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return configuration.getAttribute().isEmpty() || parent.rowsIterator().hasNext();
    }


    @Override
    protected void renderSection(final TableRowsGroup parent, final TableRow headerRow, final Section configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final TableComponentIterator<TableRowsGroup> rowsGroups = parent.groupsIterator();
        final TableComponentIterator<TableRow> rows = parent.rowsIterator();
        configuration.getAttribute().forEach(
                        attribute -> {
                            if(isAttributeLocalized(attribute.getQualifier(), dataType))
                            {
                                final LocalizedAttributeInfo localizedAttributeInfo = new LocalizedAttributeInfo(parent, headerRow, rowsGroups
                                                .request(), configuration, attribute);
                                renderLocalizedAttributeGroup(localizedAttributeInfo, data, dataType, widgetInstanceManager);
                            }
                            else
                            {
                                final TableRow row = rows.request();
                                renderAttribute(parent, row, attribute, data, dataType, widgetInstanceManager);
                                fireComponentRendered(row, parent, configuration, data);
                            }
                        });
        rowsGroups.removeRemaining();
        rows.removeRemaining();
    }


    protected boolean isAttributeLocalized(final String qualifier, final DataType dataType)
    {
        final DataAttribute attribute = dataType.getAttribute(qualifier);
        return attribute != null && attribute.isLocalized();
    }


    protected void renderLocalizedAttributeGroup(final LocalizedAttributeInfo localizedAttributeInfo,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final TableRowsGroup localizedAttributeGroup = localizedAttributeInfo.getLocalizedAttributeGroup();
        final TableComponentIterator<TableRow> localizedRows = localizedAttributeGroup.rowsIterator();
        final Attribute attribute = localizedAttributeInfo.getAttributeConfiguration();
        final Section section = localizedAttributeInfo.getSectionConfiguration();
        getAllLocales().forEach(
                        locale -> {
                            final TableRow localizedRow = localizedRows.request();
                            if(getCurrentLocale().equals(locale))
                            {
                                localizedAttributeInfo.setCurrentLocaleRow(localizedRow);
                            }
                            renderLocalizedAttributeLocalization(localizedAttributeGroup, data, dataType, widgetInstanceManager, localizedRow,
                                            attribute, locale);
                            fireComponentRendered(localizedRow, localizedAttributeGroup, section, data);
                        });
        localizedRows.removeRemaining();
        registerCollapsibleLocalizedGroupListeners(localizedAttributeInfo, data, dataType, widgetInstanceManager);
        setCollapsed(localizedAttributeGroup, isCollapsed(localizedAttributeGroup));
        fireComponentRendered(localizedAttributeGroup, localizedAttributeInfo.getParentSection(), section, data);
    }


    protected void registerCollapsibleLocalizedGroupListeners(final LocalizedAttributeInfo localizedAttributeInfo,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final TableRowsGroup localizedAttributeGroup = localizedAttributeInfo.getLocalizedAttributeGroup();
        final Section sectionConfiguration = localizedAttributeInfo.getSectionConfiguration();
        getWidgetRenderingUtils().registerMarkedComponentsListener(localizedAttributeGroup,
                        MARK_LOCALIZED_ATTRIBUTE_EXPAND_TRIGGER, Events.ON_CLICK,
                        new IdentifiableMarkEventListener(LISTENER_EXPAND_LOCALIZED_ATTRIBUTE)
                        {
                            @Override
                            public void onEvent(final Event event, final Object markData)
                            {
                                handleLocalizedAttributeExpandStateChangeRequested(localizedAttributeGroup,
                                                localizedAttributeInfo.getCurrentLocaleRow(), sectionConfiguration, data, dataType, widgetInstanceManager);
                            }
                        });
    }


    protected void handleLocalizedAttributeExpandStateChangeRequested(final TableRowsGroup group, final TableRow currentLocaleRow,
                    final Section configuration, final PartialRendererData<Collection> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final boolean newCollapsedState = !isCollapsed(group);
        setCollapsed(group, newCollapsedState);
        handleLocalizedAttributeExpandStateChangeRequested(newCollapsedState, currentLocaleRow, configuration, data, dataType,
                        widgetInstanceManager);
    }


    protected void handleLocalizedAttributeExpandStateChangeRequested(final boolean newCollapsedState, final TableRow row,
                    final Section configuration, final PartialRendererData<Collection> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Object labelContainer = Optional.ofNullable(row.getAttribute(ATTRIBUTE_LOCALIZED_ATTRIBUTE_EXPAND_STATE_HOLDER))
                        .orElse(row);
        if(labelContainer instanceof HtmlBasedComponent)
        {
            final String classToAdd = newCollapsedState ? SCLASS_LOCEDITOR_CLOSED : SCLASS_LOCEDITOR_OPENED;
            final String classToRemove = newCollapsedState ? SCLASS_LOCEDITOR_OPENED : SCLASS_LOCEDITOR_CLOSED;
            UITools.addSClass((HtmlBasedComponent)labelContainer, classToAdd);
            UITools.removeSClass((HtmlBasedComponent)labelContainer, classToRemove);
        }
    }


    protected void renderLocalizedAttributeLocalization(final TableRowsGroup parent, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager, final TableRow row,
                    final Attribute attribute, final Locale locale)
    {
        final String sClass = isCurrentLocale(locale) ? SCLASS_LOCALIZED_ROW_CURRENT : SCLASS_LOCALIZED_ROW_TRANSLATION;
        UITools.addSClass(row, sClass);
        renderAttribute(parent, row, buildAttributeWithLocalizedQualifierPath(attribute, locale), data, dataType,
                        widgetInstanceManager);
    }


    protected Attribute buildAttributeWithLocalizedQualifierPath(final Attribute attribute, final Locale locale)
    {
        final String localizedQualifier = ObjectValuePath.getLocalePath(attribute.getQualifier(), locale.toString());
        final Attribute localizedAttribute = new Attribute();
        localizedAttribute.setQualifier(localizedQualifier);
        localizedAttribute.setPosition(attribute.getPosition());
        localizedAttribute.setMergeMode(attribute.getMergeMode());
        localizedAttribute.setLabel(attribute.getLabel());
        localizedAttribute.setRenderer(attribute.getRenderer());
        return localizedAttribute;
    }


    protected List<Locale> getAllLocales()
    {
        final LinkedHashSet<Locale> locales = new LinkedHashSet<>();
        locales.add(getCurrentLocale());
        locales.addAll(getCockpitLocaleService().getEnabledDataLocales(getCockpitUserService().getCurrentUser()));
        return new ArrayList<>(locales);
    }


    protected Locale getCurrentLocale()
    {
        return getCockpitLocaleService().getCurrentLocale();
    }


    protected boolean isCurrentLocale(final Locale locale)
    {
        return getCurrentLocale().equals(locale);
    }


    @Override
    protected void updateTableRowDifferentMark(final TableRowsGroup parent, final TableRow tableRow, final Attribute attribute,
                    final PartialRendererData<Collection> data)
    {
        final String localeName = ObjectValuePath.getLocaleFromPath(attribute.getQualifier());
        final Optional<Locale> locale = Optional.ofNullable(LocaleUtils.toLocale(localeName));
        if(locale.isPresent() && getCurrentLocale().equals(locale.get()))
        {
            final String path = ObjectValuePath.getNotLocalizedPath(attribute.getQualifier());
            final boolean hasDifferences = data.getComparisonResult().getAttributesWithDifferences().stream()
                            .filter(attr -> org.apache.commons.lang.StringUtils.equals(path, attr.getQualifier()))
                            .anyMatch(attr -> isNotEqual(data.getComparisonResult(), attr));
            if(hasDifferences)
            {
                DefaultCompareViewLayout.markAsNotEqual(tableRow);
            }
            else
            {
                DefaultCompareViewLayout.markAsEqual(tableRow);
            }
            UITools.modifySClass(tableRow, SCLASS_LOCALIZED_ROW_INDIFFERENT,
                            !isNotEqual(data.getComparisonResult(), new CompareLocalizedAttributeDescriptor(path, locale.get())));
        }
        else
        {
            final CompareAttributeDescriptor compareQualifierDescriptor = locale
                            .<CompareAttributeDescriptor>map(
                                            loc -> new CompareLocalizedAttributeDescriptor(
                                                            ObjectValuePath.getNotLocalizedPath(attribute.getQualifier()), loc)).orElseGet(
                                            () -> new CompareAttributeDescriptor(attribute.getQualifier()));
            if(isNotEqual(data.getComparisonResult(), compareQualifierDescriptor))
            {
                DefaultCompareViewLayout.markAsNotEqual(tableRow);
            }
            else
            {
                DefaultCompareViewLayout.markAsEqual(tableRow);
            }
        }
    }


    /**
     * Checks if there is any difference for an attribute <code>attributeDescriptor</code>.
     *
     * @param result
     *           ComparisonResult loaded from engine
     *           {@link DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param attributeDescriptor
     *           object's attribute descriptor
     * @return true if compareObject is different than reference object in context of attribute, false otherwise
     */
    protected boolean isNotEqual(final ComparisonResult result, final CompareAttributeDescriptor attributeDescriptor)
    {
        final Collection<ObjectAttributesValueContainer> differentObjects = result.getObjectsIdWithDifferences(attributeDescriptor);
        return !CollectionUtils.isEmpty(differentObjects);
    }


    @Override
    protected WidgetComponentRenderer<TableRow, Attribute, PartialRendererData<Collection>> getAttributeRenderer(
                    final Attribute attributeConfig)
    {
        if(StringUtils.isNotEmpty(attributeConfig.getRenderer()))
        {
            return CustomRendererClassUtil.createRenderer(attributeConfig.getRenderer());
        }
        else
        {
            return getAttributeRenderer();
        }
    }
}
