/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import static com.hybris.cockpitng.widgets.compare.CompareViewController.MODEL_VALUE_CHANGED_MAP;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.CompareLocalizedAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.config.compareview.jaxb.Attribute;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.compare.CompareViewController;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;

public class DefaultCompareViewAttributeRenderer
                extends AbstractCompareViewElementRenderer<Attribute, PartialRendererData<Collection>>
{
    @Override
    protected String getQualifier(final Attribute configuration)
    {
        return configuration.getQualifier();
    }


    @Override
    protected boolean isReadonly(final Attribute configuration)
    {
        return configuration.isReadonly();
    }


    @Override
    protected String getAttributeName(final Attribute configuration, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        if(StringUtils.isNotBlank(configuration.getLabel()))
        {
            return Optional.ofNullable(widgetInstanceManager.getLabel(configuration.getLabel())).orElse(configuration.getLabel());
        }
        else
        {
            final String nonLocalizedPath = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
            return getLabelService().getObjectLabel(ObjectValuePath.getPath(dataType.getCode(), nonLocalizedPath));
        }
    }


    @Override
    protected CompareViewElementRendererEventListener<Event> createCompareViewElementOnDropEvent(final TableCell attributeValue,
                    final Attribute configuration, final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return new CompareViewElementRendererEventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                final Object draggedValue = ((DropEvent)event).getDragged().getAttribute(ATTRIBUTE_VALUE_CACHE);
                final CompareViewElementAttributeValueCache draggedAttributeValueCache = (CompareViewElementAttributeValueCache)draggedValue;
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
                    if(ObjectValuePath.isLocalizedPath(qualify))
                    {
                        final String localeCode = ObjectValuePath.getLocaleFromPath(qualify);
                        final Locale locale = getLocale(localeCode);
                        final Map<Locale, Object> currentValue = getObjectValueService().getValue(notLocalizedQualify, item);
                        currentValue.put(locale, draggedAttributeValueCache.getValue());
                        getObjectValueService().setValue(notLocalizedQualify, item, currentValue);
                    }
                    else
                    {
                        getObjectValueService().setValue(qualify, item, draggedAttributeValueCache.getValue());
                    }
                    final Editor editor = (Editor)attributeValue.getChildren().get(0).getChildren().get(0);
                    editor.setValue(draggedAttributeValueCache.getValue());
                    final Object targetValue = attributeValue.getAttribute(ATTRIBUTE_VALUE_CACHE);
                    final CompareViewElementAttributeValueCache targetAttributeValueCache = (CompareViewElementAttributeValueCache)targetValue;
                    attributeValue.setAttribute(ATTRIBUTE_VALUE_CACHE,
                                    new CompareViewElementAttributeValueCache(draggedAttributeValueCache.getValue(),
                                                    targetAttributeValueCache.getQualifier(), targetAttributeValueCache.getDataType()));
                    final Map<Object, Boolean> modelValueChangedMap = widgetInstanceManager.getModel()
                                    .getValue(MODEL_VALUE_CHANGED_MAP, Map.class);
                    modelValueChangedMap.put(item, true);
                    widgetInstanceManager.getModel().setValue(MODEL_VALUE_CHANGED_MAP, modelValueChangedMap);
                }
            }
        };
    }


    private Locale getLocale(final String isoCode)
    {
        final String[] splitCode = isoCode.split("_");
        final int twoLength = 2;
        if(splitCode.length == twoLength)
        {
            return new Locale(splitCode[0], splitCode[1]);
        }
        return new Locale(isoCode);
    }


    @Override
    protected String getTooltipText(final Attribute configuration)
    {
        return configuration.getQualifier();
    }


    @Override
    protected Optional<Locale> getLocaleForAttribute(final Attribute configuration)
    {
        final String locale = ObjectValuePath.getLocaleFromPath(configuration.getQualifier());
        return Optional.ofNullable(LocaleUtils.toLocale(locale));
    }


    @Override
    protected Object getValueOfAttribute(final Attribute configuration, final Object item,
                    final PartialRendererData<Collection> data)
    {
        return getValueOfAttribute(configuration, item);
    }


    /**
     * @deprecated, since 1811, please use {@link #getValueOfAttribute(Attribute, Object, PartialRendererData)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Object getValueOfAttribute(final Attribute configuration, final Object item)
    {
        final String qualifier = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        final Optional<Locale> localeForAttribute = getLocaleForAttribute(configuration);
        Object value = getObjectValueService().getValue(qualifier, item);
        if(localeForAttribute.isPresent() && value instanceof Map)
        {
            final Map<Locale, Object> localizedValue = (Map)value;
            value = localeForAttribute.map(localizedValue::get).orElse(null);
        }
        return value;
    }


    @Override
    protected void updateLocalizationDifferentiatorDifferentMark(final Locale locale, final Attribute configuration,
                    final HtmlBasedComponent component, final PartialRendererData<Collection> data)
    {
        final String path = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        if(isNotEqual(data.getComparisonResult(), new CompareLocalizedAttributeDescriptor(path, locale)))
        {
            DefaultCompareViewLayout.markAsNotEqual(component);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(component);
        }
    }


    @Override
    protected boolean isValueIncorrect(final Attribute configuration, final DataType dataType,
                    final PartialRendererData<Collection> data, final Object item)
    {
        final String qualifier = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        return dataType.getAttribute(qualifier) == null;
    }


    @Override
    protected boolean isLocaleAvailableForItemInstance(final Attribute configuration, final Object item)
    {
        final ObjectValuePath valuePath = ObjectValuePath.parse(configuration.getQualifier());
        if(valuePath.isLocalized())
        {
            final Locale requiredLocale = LocaleUtils.toLocale(valuePath.getLocale());
            final Set<Locale> availableLocalesForItem = getPermissionFacade().getReadableLocalesForInstance(item);
            return availableLocalesForItem.stream().anyMatch(locale -> locale.equals(requiredLocale));
        }
        return true;
    }


    @Override
    protected boolean hasPermissionsToRead(final Attribute configuration, final Object item)
    {
        return getPermissionFacade().canReadInstanceProperty(item,
                        ObjectValuePath.getNotLocalizedPath(configuration.getQualifier()));
    }


    @Override
    protected boolean hasPermissionsToWrite(final Attribute configuration, final Object item)
    {
        return getPermissionFacade().canChangeInstanceProperty(item,
                        ObjectValuePath.getNotLocalizedPath(configuration.getQualifier()));
    }


    @Override
    protected void renderAttributeEditor(final HtmlBasedComponent container, final Attribute configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final String qualifier = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        final DataAttribute dataAttribute = dataType.getAttribute(qualifier);
        renderAttributeEditor(container, configuration, data, item, dataAttribute, widgetInstanceManager);
        super.renderAttributeEditor(container, configuration, data, item, dataType, widgetInstanceManager);
    }


    /**
     * @deprecated, since 1811, please use
     * {@link #renderAttributeEditor(HtmlBasedComponent, Attribute, PartialRendererData, Object, DataType, WidgetInstanceManager)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected void renderAttributeEditor(final HtmlBasedComponent container, final Attribute configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataAttribute dataAttribute,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        //do nothing
    }


    /**
     * @deprecated, since 1811, please use
     * {@link #isFittingEditor(Editor, Attribute, PartialRendererData, Object, DataType, WidgetInstanceManager)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected boolean isFittingEditor(final Editor editor, final Attribute configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataAttribute dataAttribute,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final String editorType = EditorUtils.getEditorType(dataAttribute.getDefinedType());
        return StringUtils.equals(editor.getType(), editorType);
    }


    @Override
    protected boolean isFittingEditor(final Editor editor, final Attribute configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final String qualifier = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        final DataAttribute dataAttribute = dataType.getAttribute(qualifier);
        return isFittingEditor(editor, configuration, data, item, dataAttribute, widgetInstanceManager);
    }


    /**
     * @deprecated, since 1811, please use
     * {@link #createAttributeEditor(Attribute, PartialRendererData, Object, DataType, WidgetInstanceManager)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Editor createAttributeEditor(final Attribute configuration, final PartialRendererData<Collection> data,
                    final Object item, final DataAttribute dataAttribute, final WidgetInstanceManager widgetInstanceManager)
    {
        final Object value = getValueOfAttribute(configuration, item);
        final String qualifier = getQualifier(configuration);
        final EditorBuilder wrappedEditor = getEditorWrappingUtils().createWrappedEditor(configuration.getParameter(),
                        dataAttribute.getDefinedType(), configuration.getEditor(), widgetInstanceManager);
        wrappedEditor.attach(item, ObjectValuePath.getNotLocalizedPath(qualifier));
        wrappedEditor.setValue(value);
        wrappedEditor.setGroup(Integer.toHexString(configuration.hashCode()) + "_"
                        + widgetInstanceManager.getModel().getValue(CompareViewController.MODEL_COMPARISON_REQUEST_ID, UUID.class));
        wrappedEditor.setReadOnly(true);
        return buildEditor(wrappedEditor, configuration, data, item, dataAttribute, widgetInstanceManager);
    }


    @Override
    protected Editor createAttributeEditor(final Attribute configuration, final PartialRendererData<Collection> data,
                    final Object item, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final String qualifier = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        final DataAttribute dataAttribute = dataType.getAttribute(qualifier);
        return createAttributeEditor(configuration, data, item, dataAttribute, widgetInstanceManager);
    }


    @Override
    protected void updateAttributeValueDifferentMark(final Attribute configuration, final Object item,
                    final PartialRendererData<Collection> data, final TableCell attributeValue)
    {
        final CompareAttributeDescriptor compareViewAttribute;
        final ObjectValuePath valuePath = ObjectValuePath.parse(configuration.getQualifier());
        final String qualifier = ObjectValuePath.getNotLocalizedPath(configuration.getQualifier());
        if(!valuePath.isLocalized())
        {
            compareViewAttribute = new CompareAttributeDescriptor(qualifier);
        }
        else
        {
            final Locale locale = LocaleUtils.toLocale(valuePath.getLocale());
            compareViewAttribute = new CompareLocalizedAttributeDescriptor(qualifier, locale);
        }
        if(isNotEqual(data.getComparisonResult(), compareViewAttribute, item))
        {
            DefaultCompareViewLayout.markAsNotEqual(attributeValue);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(attributeValue);
        }
    }


    /**
     * Checks if there is any difference for an attribute <code>attributeDescriptor</code>.
     *
     * @param result
     *           ComparisonResult loaded from engine
     *           {@link com.hybris.cockpitng.compare.impl.DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param attributeDescriptor
     *           object's attribute descriptor
     * @return true if compareObject is different than reference object in context of attribute, false otherwise
     */
    protected boolean isNotEqual(final ComparisonResult result, final CompareAttributeDescriptor attributeDescriptor)
    {
        final Collection<ObjectAttributesValueContainer> differentObjects = result.getObjectsIdWithDifferences(attributeDescriptor);
        return !CollectionUtils.isEmpty(differentObjects);
    }


    /**
     * Checks if there is any difference for a <code>compareObject</code> and attribute <code>attributeDescriptor</code> in
     * comparing with reference object.
     *
     * @param result
     *           ComparisonResult loaded from engine
     *           {@link com.hybris.cockpitng.compare.impl.DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param attributeDescriptor
     *           attribute's descriptor to compare between <code>referenceObject</code> and * <code>compareObject</code>
     * @param compareObject
     *           object compares with a referenceObjectId {@link ComparisonResult#getReferenceObjectId()}
     * @return <code>true</code> if <code>compareObject</code> is different than reference object
     */
    protected boolean isNotEqual(final ComparisonResult result, final CompareAttributeDescriptor attributeDescriptor,
                    final Object compareObject)
    {
        if(result != null)
        {
            final Object compareObjectId = getObjectFacade().getObjectId(compareObject);
            final Collection<ObjectAttributesValueContainer> differentObjects = result
                            .getObjectsIdWithDifferences(attributeDescriptor);
            return differentObjects.stream().map(ObjectAttributesValueContainer::getObjectId).collect(Collectors.toSet())
                            .contains(compareObjectId);
        }
        return false;
    }


    /**
     * @deprecated, since 1811, please use {@link com.hybris.cockpitng.widgets.compare.renderer.IncorrectValueLabelProvider}
     */
    @FunctionalInterface
    @Deprecated(since = "1811", forRemoval = true)
    public interface IncorrectValueLabelProvider extends
                    com.hybris.cockpitng.widgets.compare.renderer.IncorrectValueLabelProvider<Attribute, PartialRendererData<Collection>>
    {
    }
}
