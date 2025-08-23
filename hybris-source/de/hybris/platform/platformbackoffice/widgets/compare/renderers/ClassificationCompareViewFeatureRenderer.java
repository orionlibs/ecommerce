package de.hybris.platform.platformbackoffice.widgets.compare.renderers;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.compare.renderer.AbstractCompareViewElementRenderer;
import com.hybris.cockpitng.widgets.compare.renderer.CompareViewElementAttributeValueCache;
import com.hybris.cockpitng.widgets.compare.renderer.DefaultCompareViewLayout;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import de.hybris.platform.platformbackoffice.widgets.compare.model.BackofficeComparisonResult;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationValuesContainer;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.impl.BackofficePartialRendererData;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;

public class ClassificationCompareViewFeatureRenderer extends AbstractCompareViewElementRenderer<FeatureDescriptor, BackofficePartialRendererData<Collection>>
{
    private static final String MODEL_SELECTED_OBJECT = "currentObject";
    private ClassificationService classificationService;


    protected boolean hasPermissionsToRead(FeatureDescriptor configuration, BackofficePartialRendererData<Collection> data, Object item, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(!getPermissionFacade().canReadType("ClassificationAttributeValue"))
        {
            ClassificationInfo classificationInfo = (ClassificationInfo)((Map)((Map)data.getFeatureValues().get(widgetInstanceManager.getModel().getValue("currentObject", Object.class))).get(configuration.getClassificationCode())).get(configuration);
            return (hasPermissionsToRead(configuration, item) &&
                            !classificationInfo.getAssignment().getAttributeType().equals(ClassificationAttributeTypeEnum.ENUM));
        }
        return hasPermissionsToRead(configuration, item);
    }


    protected boolean hasPermissionsToWrite(FeatureDescriptor configuration, BackofficePartialRendererData<Collection> data, Object item, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(!getPermissionFacade().canChangeType("ClassificationAttributeValue"))
        {
            ClassificationInfo classificationInfo = (ClassificationInfo)((Map)((Map)data.getFeatureValues().get(widgetInstanceManager.getModel().getValue("currentObject", Object.class))).get(configuration.getClassificationCode())).get(configuration);
            return (hasPermissionsToWrite(configuration, item) &&
                            !classificationInfo.getAssignment().getAttributeType().equals(ClassificationAttributeTypeEnum.ENUM));
        }
        return hasPermissionsToWrite(configuration, item);
    }


    protected String getQualifier(FeatureDescriptor configuration)
    {
        return configuration.getCode();
    }


    protected String getAttributeName(FeatureDescriptor configuration, BackofficePartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        return (configuration.getName() != null) ? configuration.getName() : configuration.getClassificationAttributeCode();
    }


    protected AbstractCompareViewElementRenderer.CompareViewElementRendererEventListener<Event> createCompareViewElementOnDropEvent(TableCell attributeValue, FeatureDescriptor configuration, BackofficePartialRendererData<Collection> data, Object item, DataType dataType,
                    WidgetInstanceManager widgetInstanceManager)
    {
        return (AbstractCompareViewElementRenderer.CompareViewElementRendererEventListener<Event>)new Object(this, configuration, item, data, attributeValue, widgetInstanceManager);
    }


    private void updateProductFeatures(FeatureDescriptor configuration, ProductModel item, CompareViewElementAttributeValueCache draggedAttributeValueCache, WidgetInstanceManager widgetInstanceManager)
    {
        FeatureList featureList;
        Map<ProductModel, FeatureList> compareObjectFeatureListMap = (Map<ProductModel, FeatureList>)widgetInstanceManager.getModel().getValue("comparison-object-featureList-map", Map.class);
        if(compareObjectFeatureListMap.containsKey(item))
        {
            featureList = compareObjectFeatureListMap.get(item);
        }
        else
        {
            featureList = getClassificationService().getFeatures(item);
        }
        List<Feature> features = featureList.getFeatures();
        for(Feature feature : features)
        {
            if(feature.getCode().equals(configuration.getCode()))
            {
                updateFeatureValue(feature, draggedAttributeValueCache);
            }
        }
        compareObjectFeatureListMap.put(item, new FeatureList(features));
    }


    private void updateFeatureValue(Feature feature, CompareViewElementAttributeValueCache draggedAttributeValueCache)
    {
        if(feature instanceof LocalizedFeature)
        {
            updateLocalizedFeatureValue((LocalizedFeature)feature, draggedAttributeValueCache);
        }
        else
        {
            feature.removeAllValues();
            if(draggedAttributeValueCache.getValue() instanceof FeatureValue)
            {
                feature.setValues(List.of((FeatureValue)draggedAttributeValueCache.getValue()));
            }
            else if(draggedAttributeValueCache.getValue() instanceof List)
            {
                feature.setValues((List)draggedAttributeValueCache.getValue());
            }
        }
    }


    private void updateLocalizedFeatureValue(LocalizedFeature localizedFeature, CompareViewElementAttributeValueCache draggedAttributeValueCache)
    {
        Map<Locale, List<FeatureValue>> featureValueForLocale = localizedFeature.getValuesForAllLocales();
        for(Locale locale : featureValueForLocale.keySet())
        {
            localizedFeature.removeAllValues(locale);
        }
        if(draggedAttributeValueCache.getValue() instanceof Map)
        {
            Map<Locale, FeatureValue> classificationValue = (Map<Locale, FeatureValue>)draggedAttributeValueCache.getValue();
            for(Locale locale : classificationValue.keySet())
            {
                if(classificationValue.get(locale) instanceof List)
                {
                    localizedFeature.setValues((List)classificationValue.get(locale), locale);
                    continue;
                }
                localizedFeature.addValue(classificationValue.get(locale), locale);
            }
        }
    }


    protected String getTooltipText(FeatureDescriptor configuration)
    {
        return configuration.getCode();
    }


    protected Optional<Locale> getLocaleForAttribute(FeatureDescriptor configuration)
    {
        return Optional.empty();
    }


    protected Object getValueOfAttribute(FeatureDescriptor configuration, Object item, BackofficePartialRendererData<Collection> data)
    {
        Map<FeatureDescriptor, ClassificationInfo> featureValues = getFeatureValues(configuration, data, item);
        if(featureValues == null)
        {
            return null;
        }
        ClassificationInfo classificationInfo = featureValues.get(configuration);
        return (classificationInfo != null) ? classificationInfo.getValue() : null;
    }


    private void setValueOfAttribute(FeatureDescriptor configuration, Object item, BackofficePartialRendererData<Collection> data, Object value)
    {
        Map<FeatureDescriptor, ClassificationInfo> featureValues = getFeatureValues(configuration, data, item);
        if(featureValues == null || featureValues.get(configuration) == null)
        {
            return;
        }
        ClassificationInfo classificationInfo = featureValues.get(configuration);
        if(classificationInfo.isLocalized() && value instanceof Map && classificationInfo.getValue() instanceof Map)
        {
            Map valueOfAttributeMap = (Map)classificationInfo.getValue();
            if(!valueOfAttributeMap.isEmpty())
            {
                valueOfAttributeMap.clear();
            }
            valueOfAttributeMap.putAll((Map)value);
        }
        else if(value instanceof FeatureValue && classificationInfo.getValue() instanceof FeatureValue)
        {
            ((FeatureValue)classificationInfo.getValue()).setValue(((FeatureValue)value).getValue());
        }
    }


    protected Map<FeatureDescriptor, ClassificationInfo> getFeatureValues(FeatureDescriptor featureDescription, BackofficePartialRendererData<Collection> data, Object item)
    {
        Map<String, Map<FeatureDescriptor, ClassificationInfo>> featureValuesByClassificationCode = (Map<String, Map<FeatureDescriptor, ClassificationInfo>>)data.getFeatureValues().get(item);
        return MapUtils.isEmpty(featureValuesByClassificationCode) ? null :
                        featureValuesByClassificationCode.get(featureDescription.getClassificationCode());
    }


    protected void updateLocalizationDifferentiatorDifferentMark(Locale locale, FeatureDescriptor configuration, HtmlBasedComponent component, BackofficePartialRendererData<Collection> data)
    {
        if(isNotEqual((BackofficeComparisonResult)data.getComparisonResult(), configuration))
        {
            DefaultCompareViewLayout.markAsNotEqual(component);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(component);
        }
    }


    protected boolean isValueIncorrect(FeatureDescriptor configuration, DataType dataType, BackofficePartialRendererData<Collection> data, Object item)
    {
        Map<FeatureDescriptor, ClassificationInfo> featureValues = getFeatureValues(configuration, data, item);
        if(featureValues == null)
        {
            return true;
        }
        return (featureValues.isEmpty() || featureValues.get(configuration) == null);
    }


    protected boolean hasPermissionsToRead(FeatureDescriptor configuration, Object item)
    {
        return configuration.canRead();
    }


    protected boolean hasPermissionsToWrite(FeatureDescriptor configuration, Object item)
    {
        return configuration.canWrite();
    }


    protected boolean isLocaleAvailableForItemInstance(FeatureDescriptor configuration, Object item)
    {
        ObjectValuePath valuePath = ObjectValuePath.parse(configuration.getCode());
        if(valuePath.isLocalized())
        {
            Locale requiredLocale = LocaleUtils.toLocale(valuePath.getLocale());
            Set<Locale> availableLocalesForItem = getPermissionFacade().getReadableLocalesForInstance(item);
            return availableLocalesForItem.stream().anyMatch(locale -> locale.equals(requiredLocale));
        }
        return true;
    }


    protected boolean isFittingEditor(Editor editor, FeatureDescriptor configuration, BackofficePartialRendererData<Collection> data, Object item, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        ClassificationInfo classificationInfo = (ClassificationInfo)((Map)((Map)data.getFeatureValues().get(item)).get(configuration.getClassificationCode())).get(configuration);
        return StringUtils.equals(editor.getType(), BackofficeClassificationUtils.getType(classificationInfo));
    }


    protected Editor createAttributeEditor(FeatureDescriptor configuration, BackofficePartialRendererData<Collection> data, Object item, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        EditorBuilder wrappedEditor = getEditorWrappingUtils().createWrappedEditor(Collections.emptySet(), null, widgetInstanceManager);
        wrappedEditor.setReadOnly(true);
        ClassificationInfo classificationInfo = (ClassificationInfo)((Map)((Map)data.getFeatureValues().get(item)).get(configuration.getClassificationCode())).get(configuration);
        if(classificationInfo == null)
        {
            return null;
        }
        wrappedEditor.setValueType(EditorUtils.getFeatureEditorType());
        wrappedEditor.setValue(classificationInfo);
        return wrappedEditor.build();
    }


    protected void updateAttributeValueDifferentMark(FeatureDescriptor configuration, Object item, BackofficePartialRendererData<Collection> data, TableCell attributeValue)
    {
        if(isNotEqual((BackofficeComparisonResult)data.getComparisonResult(), configuration, item))
        {
            DefaultCompareViewLayout.markAsNotEqual((HtmlBasedComponent)attributeValue);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual((HtmlBasedComponent)attributeValue);
        }
    }


    protected boolean isNotEqual(BackofficeComparisonResult result, FeatureDescriptor attributeDescriptor)
    {
        Collection<ClassificationValuesContainer> differentObjects = (Collection<ClassificationValuesContainer>)result.getDifferentObjectsForFeatures().get(attributeDescriptor);
        return !CollectionUtils.isEmpty(differentObjects);
    }


    protected boolean isNotEqual(BackofficeComparisonResult result, FeatureDescriptor attributeDescriptor, Object compareObject)
    {
        if(result == null)
        {
            return false;
        }
        Collection<ClassificationValuesContainer> differentObjects = (Collection<ClassificationValuesContainer>)result.getDifferentObjectsForFeatures().get(attributeDescriptor);
        if(differentObjects == null)
        {
            return false;
        }
        return ((Set)differentObjects.stream().map(ObjectAttributesValueContainer::getObjectId).collect(Collectors.toSet()))
                        .contains(getObjectFacade().getObjectId(compareObject));
    }


    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    public ClassificationService getClassificationService()
    {
        return this.classificationService;
    }
}
