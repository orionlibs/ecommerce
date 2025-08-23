/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.ComparisonState;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.util.EditorWrappingUtils;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class AbstractCompareViewElementRenderer<ATTRIBUTE, DATA extends PartialRendererData<Collection>>
                extends AbstractWidgetComponentRenderer<TableRow, ATTRIBUTE, DATA>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCompareViewElementRenderer.class);
    private static final String ATTRIBUTE_QUALIFIER = "attribute-qualifier";
    protected static final String ATTRIBUTE_VALUE_CACHE = "attribute-value-cache";
    private static final String ATTRIBUTE_LANG_DIFFERENTIATOR = "locale-differentiator";
    private static final String ATTRIBUTE_DATA_TYPE = "attribute-data-type";
    private static final String SCLASS_ATTRIBUTE_NAME = "yw-compareview-attribute-name";
    private static final String SCLASS_ATTRIBUTE_NAME_LABEL = "yw-compareview-attribute-name-label";
    private static final String SCLASS_ATTRIBUTE_VALUE = "yw-compareview-attribute-value";
    private static final String SCLASS_ATTRIBUTE_VALUE_ERROR = "yw-compareview-attribute-value-error";
    private static final String SCLASS_LANGUAGE_DIFFERENTIATOR = "yw-compareview-language-differentiator";
    private static final String SCLASS_LOCEDITOR = "y-icon-loceditor";
    private static final String SCLASS_LOCEDITOR_CLOSED = "yw-loceditor-fixed-closed";
    private static final String SCLASS_Y_ICON = "y-icon";
    private static final String LABEL_KEY_NOT_EXISTING = "data.not.existing";
    private static final String YATFTESTID_PREFIX_ATTRIBUTE_VALUE = "attributeValue_";
    private static final String YATFTESTID_PREFIX_ATTRIBUTE_NAME = "attributeName_";
    private static final Set<DataType> ATOMIC_EDITABLE_EDITOR_TYPES = new HashSet<>(
                    Arrays.asList(DataType.BOOLEAN, DataType.DATE, DataType.BIG_DECIMAL, DataType.INTEGER, DataType.LONG, DataType.STRING,
                                    DataType.BYTE, DataType.CHARACTER, DataType.SHORT, DataType.FLOAT, DataType.DOUBLE));
    private CockpitLocaleService cockpitLocaleService;
    private PermissionFacade permissionFacade;
    private LabelService labelService;
    private ObjectValueService objectValueService;
    private EditorWrappingUtils editorWrappingUtils;
    private WidgetRenderingUtils widgetRenderingUtils;
    private ObjectFacade objectFacade;
    private ItemComparisonFacade itemComparisonFacade;
    private TypeFacade typeFacade;


    protected abstract String getQualifier(final ATTRIBUTE configuration);


    protected boolean isReadonly(final ATTRIBUTE configuration)
    {
        return false;
    }


    @Override
    public void render(final TableRow parent, final ATTRIBUTE configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final TableComponentIterator<TableCell> cells = parent.cellsIterator();
        final TableCell attributeName = cells.request();
        if(requiresAttributeNameRendering(attributeName, configuration, data, dataType, widgetInstanceManager))
        {
            renderAttributeName(parent, attributeName, configuration, data, dataType, widgetInstanceManager);
            modifyYatfTestIdForAttributeName(attributeName, configuration);
        }
        attributeNameRendered(attributeName, configuration, data, dataType, widgetInstanceManager);
        fireComponentRendered(attributeName, parent, configuration, data);
        final AtomicInteger index = new AtomicInteger();
        data.getData().forEach(item -> {
            final TableCell attributeValue = cells.request();
            if(requiresAttributeValueRendering(attributeValue, configuration, data, item, dataType, widgetInstanceManager))
            {
                renderAttributeValue(attributeValue, configuration, data, item, dataType, widgetInstanceManager);
            }
            index.getAndIncrement();
            attributeValueRendered(attributeValue, configuration, data, item, dataType, widgetInstanceManager);
            modifyYatfTestIdForAttributeValue(attributeValue, configuration, index.get());
            fireComponentRendered(attributeValue, parent, configuration, data);
        });
        cells.removeRemaining();
        fireComponentRendered(parent, configuration, data);
    }


    protected boolean requiresAttributeNameRendering(final TableCell attributeNameCell, final ATTRIBUTE configuration,
                    final DATA data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return attributeNameCell.getChildren().isEmpty()
                        || ObjectUtils.notEqual(getQualifier(configuration), attributeNameCell.getAttribute(ATTRIBUTE_QUALIFIER))
                        || ObjectUtils.notEqual(dataType, attributeNameCell.getAttribute(ATTRIBUTE_DATA_TYPE));
    }


    /**
     * @deprecated since 1808, please use renderAttributeName(TableRow, TableCell, ATTRIBUTE, DATA, DataType,
     *             WidgetInstanceManager)
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void renderAttributeName(final TableCell attributeName, final ATTRIBUTE configuration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        renderAttributeName(null, attributeName, configuration, data, dataType, widgetInstanceManager);
    }


    protected void renderAttributeName(final TableRow parent, final TableCell attributeName, final ATTRIBUTE configuration,
                    final DATA data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        attributeName.getChildren().clear();
        final Div container = new Div();
        DefaultCompareViewLayout.markAsContentsContainer(container);
        container.appendChild(
                        createAttributeNameContents(parent, attributeName, configuration, data, dataType, widgetInstanceManager));
        attributeName.appendChild(container);
    }


    /**
     * @deprecated since 1808, please use createAttributeNameContents(TableRow, TableCell, ATTRIBUTE, DATA, DataType,
     *             WidgetInstanceManager)
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected Component createAttributeNameContents(final ATTRIBUTE configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return createAttributeNameContents(null, null, configuration, data, dataType, widgetInstanceManager);
    }


    protected Component createAttributeNameContents(final TableRow row, final TableCell parent, final ATTRIBUTE configuration,
                    final DATA data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div labelContainer = new Div();
        final Optional<Locale> locale = getLocaleForAttribute(configuration);
        if(locale.isPresent())
        {
            if(locale.get().equals(getCurrentLocale()))
            {
                final Component attributeName = createLocalizedAttributeNameLabel(row, parent, configuration, data, dataType,
                                widgetInstanceManager);
                labelContainer.appendChild(attributeName);
            }
            final HtmlBasedComponent languageDifferentiatorLabel = createLocalizationDifferentiator(locale.get(), row, parent,
                            configuration, data, dataType, widgetInstanceManager);
            parent.setAttribute(ATTRIBUTE_LANG_DIFFERENTIATOR, languageDifferentiatorLabel);
            labelContainer.appendChild(languageDifferentiatorLabel);
        }
        else
        {
            labelContainer.appendChild(createAttributeNameLabel(parent, configuration, data, dataType, widgetInstanceManager));
        }
        labelContainer.setTooltiptext(getTooltipText(configuration));
        return labelContainer;
    }


    protected Component createAttributeNameLabel(final Component parent, final ATTRIBUTE configuration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final String attributeName = getAttributeName(configuration, data, dataType, widgetInstanceManager);
        final Label attributeNameLabel = new Label(attributeName);
        UITools.addSClass(attributeNameLabel, SCLASS_ATTRIBUTE_NAME_LABEL);
        return attributeNameLabel;
    }


    protected Component createLocalizedAttributeNameLabel(final TableRow row, final TableCell parent,
                    final ATTRIBUTE configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Div attributeNameContainer = new Div();
        attributeNameContainer.appendChild(createAttributeNameLabel(parent, configuration, data, dataType, widgetInstanceManager));
        final Button expandTrigger = new Button();
        UITools.addSClass(expandTrigger, SCLASS_LOCEDITOR);
        UITools.addSClass(expandTrigger, SCLASS_Y_ICON);
        UITools.addSClass(attributeNameContainer, SCLASS_LOCEDITOR_CLOSED);
        getWidgetRenderingUtils().markComponent(row, expandTrigger,
                        DefaultCompareViewSectionRenderer.MARK_LOCALIZED_ATTRIBUTE_EXPAND_TRIGGER);
        row.setAttribute(DefaultCompareViewSectionRenderer.ATTRIBUTE_LOCALIZED_ATTRIBUTE_EXPAND_STATE_HOLDER,
                        attributeNameContainer);
        attributeNameContainer.appendChild(expandTrigger);
        return attributeNameContainer;
    }


    protected abstract String getAttributeName(final ATTRIBUTE configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager);


    protected abstract CompareViewElementRendererEventListener<Event> createCompareViewElementOnDropEvent(
                    final TableCell attributeValue, final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    /**
     * Marker interface for events created by compareView element renderer.
     */
    protected interface CompareViewElementRendererEventListener<T extends Event> extends EventListener<T>
    {
        // marker interface
    }


    protected HtmlBasedComponent createLocalizationDifferentiator(final Locale locale, final TableRow row, final TableCell parent,
                    final ATTRIBUTE configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Label languageDifferentiatorLabel = new Label(getLocalizationDifferentiator(locale.toString()));
        UITools.addSClass(languageDifferentiatorLabel, SCLASS_LANGUAGE_DIFFERENTIATOR);
        return languageDifferentiatorLabel;
    }


    protected String getLocalizationDifferentiator(final String languageDescriptor)
    {
        return "[" + languageDescriptor + "]";
    }


    protected abstract String getTooltipText(final ATTRIBUTE configuration);


    /**
     * @deprecated since 2205. Please use {@link #getCompareViewElementAttributeValueCache(Object, PartialRendererData, Object, DataType, WidgetInstanceManager)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Object getAttributeValueCache(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return new AttributeValueCache(getValueOfAttribute(configuration, item, data),
                        getQualifier(configuration), dataType);
    }


    protected Object getCompareViewElementAttributeValueCache(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return new CompareViewElementAttributeValueCache(getValueOfAttribute(configuration, item, data),
                        getQualifier(configuration), dataType);
    }


    protected abstract Optional<Locale> getLocaleForAttribute(final ATTRIBUTE configuration);


    protected abstract Object getValueOfAttribute(final ATTRIBUTE configuration, final Object item, final DATA data);


    protected Locale getCurrentLocale()
    {
        return getCockpitLocaleService().getCurrentLocale();
    }


    protected void attributeNameRendered(final TableCell component, final ATTRIBUTE configuration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        UITools.addSClass(component, SCLASS_ATTRIBUTE_NAME);
        if(ComparisonState.Status.FINISHED != data.getComparisonState().getStatus())
        {
            DefaultCompareViewLayout.markAsDuringCalculation(component);
        }
        else
        {
            DefaultCompareViewLayout.markAsCalculated(component);
        }
        final Object langDifferentiator = component.getAttribute(ATTRIBUTE_LANG_DIFFERENTIATOR);
        final Optional<Locale> locale = getLocaleForAttribute(configuration);
        if(locale.isPresent() && (langDifferentiator instanceof HtmlBasedComponent))
        {
            updateLocalizationDifferentiatorDifferentMark(locale.get(), configuration, (HtmlBasedComponent)langDifferentiator,
                            data);
        }
        component.setSticky(true);
        component.setAttribute(ATTRIBUTE_QUALIFIER, getQualifier(configuration));
        component.setAttribute(ATTRIBUTE_DATA_TYPE, dataType);
    }


    protected abstract void updateLocalizationDifferentiatorDifferentMark(final Locale locale, final ATTRIBUTE configuration,
                    final HtmlBasedComponent component, final DATA data);


    protected void renderAttributeValue(final TableCell attributeValue, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div container = getValueContainer(attributeValue);
        DefaultCompareViewLayout.markAsContentsContainer(container);
        renderAttributeValueContents(container, configuration, data, item, dataType, widgetInstanceManager);
    }


    protected Div getValueContainer(final TableCell cell)
    {
        final Div container;
        if(cell.getChildren().isEmpty() || !(cell.getChildren().get(0) instanceof Div))
        {
            cell.getChildren().clear();
            container = new Div();
            cell.appendChild(container);
        }
        else
        {
            final Iterator<Component> children = cell.getChildren().iterator();
            container = (Div)children.next();
            children.forEachRemaining(child -> children.remove());
        }
        return container;
    }


    protected boolean requiresAttributeValueRendering(final TableCell attributeValue, final ATTRIBUTE configuration,
                    final DATA data, final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Object cache = getCompareViewElementAttributeValueCache(configuration, data, item, dataType, widgetInstanceManager);
        return attributeValue.getChildren().isEmpty()
                        || ObjectUtils.notEqual(cache, attributeValue.getAttribute(ATTRIBUTE_VALUE_CACHE));
    }


    protected void renderAttributeValueContents(final HtmlBasedComponent parent, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final String itemDataTypeCode = getTypeFacade().getType(item);
        try
        {
            final DataType itemDataType = getTypeFacade().load(itemDataTypeCode);
            if(isValueIncorrect(configuration, itemDataType, data, item))
            {
                handleIncorrectAttributeValue(parent, configuration, data, item, dataType, widgetInstanceManager,
                                this::createAttributeNotExistingLabel);
            }
            else if(!isLocaleAvailableForItemInstance(configuration, item))
            {
                handleIncorrectAttributeValue(parent, configuration, data, item, dataType, widgetInstanceManager,
                                this::createDisabledForLanguageLabel);
            }
            else if(!hasPermissionsToRead(configuration, data, item, dataType, widgetInstanceManager))
            {
                handleIncorrectAttributeValue(parent, configuration, data, item, dataType, widgetInstanceManager,
                                this::createAccessDeniedLabel);
            }
            else
            {
                renderAttributeEditor(parent, configuration, data, item, itemDataType, widgetInstanceManager);
            }
        }
        catch(final TypeNotFoundException ex)
        {
            LOG.warn("Type [{}] not found. {}", itemDataTypeCode, ex);
            handleIncorrectAttributeValue(parent, configuration, data, item, dataType, widgetInstanceManager,
                            this::createAttributeNotExistingLabel);
        }
    }


    protected abstract boolean isValueIncorrect(final ATTRIBUTE configuration, final DataType dataType, final DATA data,
                    final Object item);


    protected void handleIncorrectAttributeValue(final HtmlBasedComponent parent, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager,
                    final IncorrectValueLabelProvider<ATTRIBUTE, DATA> incorrectValueLabelProvider)
    {
        parent.getChildren().clear();
        final HtmlBasedComponent component = incorrectValueLabelProvider.provideLabel(configuration, data, item, dataType,
                        widgetInstanceManager);
        UITools.addSClass(component, SCLASS_ATTRIBUTE_VALUE_ERROR);
        parent.appendChild(component);
    }


    protected boolean hasPermissionsToRead(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return hasPermissionsToRead(configuration, item);
    }


    protected abstract boolean hasPermissionsToRead(final ATTRIBUTE configuration, final Object item);


    protected boolean hasPermissionsToWrite(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return hasPermissionsToWrite(configuration, item);
    }


    protected abstract boolean hasPermissionsToWrite(final ATTRIBUTE configuration, final Object item);


    protected abstract boolean isLocaleAvailableForItemInstance(final ATTRIBUTE configuration, final Object item);


    protected HtmlBasedComponent createAttributeNotExistingLabel(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return new Label(widgetInstanceManager.getLabel(LABEL_KEY_NOT_EXISTING));
    }


    protected HtmlBasedComponent createAccessDeniedLabel(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return new Label(getLabelService().getAccessDeniedLabel(item));
    }


    protected HtmlBasedComponent createDisabledForLanguageLabel(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return new Label(getLabelService().getLanguageDisabledLabel(item));
    }


    protected void renderAttributeEditor(final HtmlBasedComponent container, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        Editor editor;
        if(container.getChildren().isEmpty() || !(container.getChildren().get(0) instanceof Editor))
        {
            container.getChildren().clear();
            editor = createAttributeEditor(configuration, data, item, dataType, widgetInstanceManager);
            container.appendChild(editor);
        }
        else
        {
            final Iterator<Component> children = container.getChildren().iterator();
            editor = (Editor)children.next();
            children.forEachRemaining(child -> children.remove());
            if(!isFittingEditor(editor, configuration, data, item, dataType, widgetInstanceManager))
            {
                container.getChildren().clear();
                editor = createAttributeEditor(configuration, data, item, dataType, widgetInstanceManager);
                container.appendChild(editor);
            }
            else
            {
                final Object value = getValueOfAttribute(configuration, item, data);
                editor.setInitialValue(value);
            }
        }
    }


    protected abstract boolean isFittingEditor(final Editor editor, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    protected abstract Editor createAttributeEditor(final ATTRIBUTE configuration, final DATA data, final Object item,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    protected Editor buildEditor(final EditorBuilder editorBuilder, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataAttribute dataAttribute, final WidgetInstanceManager widgetInstanceManager)
    {
        return editorBuilder.build();
    }


    protected void attributeValueRendered(final TableCell attributeValue, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        String dragAndDropId;
        final String qualify = getQualifier(configuration);
        String notLocalizedQualify;
        if(ObjectValuePath.isLocalizedPath(qualify))
        {
            notLocalizedQualify = ObjectValuePath.getNotLocalizedPath(qualify);
        }
        else
        {
            notLocalizedQualify = qualify;
        }
        if(dataType.getAttribute(notLocalizedQualify) != null)
        {
            final DataType editorType = dataType.getAttribute(notLocalizedQualify).getDefinedType();
            if(ATOMIC_EDITABLE_EDITOR_TYPES.contains(editorType))
            {
                dragAndDropId = editorType.getCode();
            }
            else
            {
                dragAndDropId = editorType.getCode() + "_" + notLocalizedQualify;
            }
        }
        else
        {
            dragAndDropId = notLocalizedQualify;
        }
        dragAndDropId = dragAndDropId.replace(",", "/");
        final String itemDataTypeCode = getTypeFacade().getType(item);
        try
        {
            setDraggableAndDroppable(attributeValue, configuration, data, item, dataType, widgetInstanceManager, dragAndDropId);
        }
        catch(final TypeNotFoundException ex)
        {
            LOG.warn("Type [{}] not found. {}", itemDataTypeCode, ex);
            handleIncorrectAttributeValue(attributeValue, configuration, data, item, dataType, widgetInstanceManager,
                            this::createAttributeNotExistingLabel);
        }
        removeEventListeners(attributeValue, Events.ON_DROP, CompareViewElementRendererEventListener.class);
        attributeValue.addEventListener(Events.ON_DROP,
                        createCompareViewElementOnDropEvent(attributeValue, configuration, data, item, dataType, widgetInstanceManager));
        UITools.addSClass(attributeValue, SCLASS_ATTRIBUTE_VALUE);
        updateAttributeValueDifferentMark(configuration, item, data, attributeValue);
        final boolean inProgress = !(ComparisonState.Status.FINISHED == data.getComparisonState().getStatus()
                        || data.getComparisonState().getComparedObjects().contains(item));
        if(inProgress)
        {
            DefaultCompareViewLayout.markAsDuringCalculation(attributeValue);
        }
        else
        {
            DefaultCompareViewLayout.markAsCalculated(attributeValue);
        }
        final Object cachedValue = getCompareViewElementAttributeValueCache(configuration, data, item, dataType, widgetInstanceManager);
        attributeValue.setAttribute(ATTRIBUTE_VALUE_CACHE, cachedValue);
        attributeValue.setSticky(getItemComparisonFacade().isSameItem(data.getComparisonState().getReference(), item));
    }


    private void setDraggableAndDroppable(final TableCell attributeValue, final ATTRIBUTE configuration, final DATA data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager,
                    final String dragAndDropId) throws TypeNotFoundException
    {
        final String qualify = getQualifier(configuration);
        String notLocalizedQualify;
        if(ObjectValuePath.isLocalizedPath(qualify))
        {
            notLocalizedQualify = ObjectValuePath.getNotLocalizedPath(qualify);
        }
        else
        {
            notLocalizedQualify = qualify;
        }
        final String itemDataTypeCode = getTypeFacade().getType(item);
        final DataType itemDataType = getTypeFacade().load(itemDataTypeCode);
        final String disableDragAndDrop = "false";
        if(isValueIncorrect(configuration, itemDataType, data, item) || !isLocaleAvailableForItemInstance(configuration, item)
                        || !hasPermissionsToRead(configuration, data, item, dataType, widgetInstanceManager))
        {
            attributeValue.setDraggable(disableDragAndDrop);
            attributeValue.setDroppable(disableDragAndDrop);
        }
        else if(isDisableDroppable(configuration, data, item, dataType, widgetInstanceManager, notLocalizedQualify))
        {
            attributeValue.setDraggable(dragAndDropId);
            attributeValue.setDroppable(disableDragAndDrop);
        }
        else
        {
            attributeValue.setDraggable(dragAndDropId);
            attributeValue.setDroppable(dragAndDropId);
            attributeValue.setTooltiptext(qualify);
        }
    }


    private boolean isDisableDroppable(final ATTRIBUTE configuration, final DATA data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final String notLocalizedQualify)
    {
        final boolean noWriteAccess = !hasPermissionsToWrite(configuration, data, item, dataType, widgetInstanceManager)
                        || (dataType.getAttribute(notLocalizedQualify) != null && !dataType.getAttribute(notLocalizedQualify).isWritable());
        return noWriteAccess || isReadonly(configuration)
                        || (dataType.getAttribute(notLocalizedQualify) != null && dataType.getAttribute(notLocalizedQualify).isPartOf());
    }


    private void removeEventListeners(final TableCell container, final String eventName, final Class type)
    {
        final Iterator<EventListener<? extends Event>> overIterator = container.getEventListeners(eventName).iterator();
        while(overIterator.hasNext())
        {
            if(type.isAssignableFrom(overIterator.next().getClass()))
            {
                overIterator.remove();
            }
        }
    }


    protected void modifyYatfTestIdForAttributeValue(final TableCell attributeValue, final ATTRIBUTE configuration,
                    final int index)
    {
        YTestTools.modifyYTestId(attributeValue, YATFTESTID_PREFIX_ATTRIBUTE_VALUE + getQualifier(configuration) + "_" + index);
    }


    protected void modifyYatfTestIdForAttributeName(final TableCell attributeName, final ATTRIBUTE configuration)
    {
        YTestTools.modifyYTestId(attributeName, YATFTESTID_PREFIX_ATTRIBUTE_NAME + getQualifier(configuration));
    }


    protected abstract void updateAttributeValueDifferentMark(final ATTRIBUTE configuration, final Object item, final DATA data,
                    final TableCell attributeValue);


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
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


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    protected EditorWrappingUtils getEditorWrappingUtils()
    {
        return editorWrappingUtils;
    }


    @Required
    public void setEditorWrappingUtils(final EditorWrappingUtils editorWrappingUtils)
    {
        this.editorWrappingUtils = editorWrappingUtils;
    }


    protected WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Resource
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
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


    protected ItemComparisonFacade getItemComparisonFacade()
    {
        return itemComparisonFacade;
    }


    @Required
    public void setItemComparisonFacade(final ItemComparisonFacade itemComparisonFacade)
    {
        this.itemComparisonFacade = itemComparisonFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    /**
     * @deprecated since 2205. Please use {@link CompareViewElementAttributeValueCache} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected static class AttributeValueCache
    {
        private final Object value;
        private final String qualifier;
        private DataType dataType;


        /**
         * @deprecated since 1811, not used anymore, please use {@link #AttributeValueCache(Object, String, DataType)}
         */
        @Deprecated(since = "1811", forRemoval = true)
        public AttributeValueCache(final Object value, final String qualifier)
        {
            this.value = value;
            this.qualifier = qualifier;
        }


        public AttributeValueCache(final Object value, final String qualifier, final DataType dataType)
        {
            this(value, qualifier);
            this.dataType = dataType;
        }


        public Object getValue()
        {
            return value;
        }


        public String getQualifier()
        {
            return qualifier;
        }


        public DataType getDataType()
        {
            return dataType;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(!DefaultCompareViewAttributeRenderer.AttributeValueCache.class.isInstance(o))
            {
                return false;
            }
            final AbstractCompareViewElementRenderer.AttributeValueCache that = (AbstractCompareViewElementRenderer.AttributeValueCache)o;
            return value == that.value && qualifier.equals(that.qualifier) && dataType.equals(that.dataType);
        }


        @Override
        public int hashCode()
        {
            int result = value.hashCode();
            result = 31 * result + qualifier.hashCode();
            result = 31 * result + dataType.hashCode();
            return result;
        }
    }
}
