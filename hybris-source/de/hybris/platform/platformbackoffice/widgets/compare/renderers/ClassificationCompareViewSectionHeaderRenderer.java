package de.hybris.platform.platformbackoffice.widgets.compare.renderers;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.compare.renderer.DefaultCompareViewSectionHeaderRenderer;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.widgets.compare.model.BackofficeComparisonResult;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;

public class ClassificationCompareViewSectionHeaderRenderer extends DefaultCompareViewSectionHeaderRenderer
{
    private static final String SCLASS_SECTION_NAME_NO_READ_ACCESS_LABEL = "yw-compareview-section-name-label-no-read-access";
    private static final String LABEL_NO_READ_ACCESS_YTEST_ID = "compareview-classification-no-read-access";
    private static final String COMPARE_VIEW_FEATURES_BEFORE_SAVE_LISTENER_PARAM = "compareViewFeaturesBeforeSaveListener";
    private PermissionFacade permissionFacade;
    private LabelService labelService;
    private ClassificationService classificationService;


    public void render(TableRow parent, Section configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        super.render(parent, configuration, data, dataType, widgetInstanceManager);
        registerBeforeSaveListener(widgetInstanceManager, data);
    }


    private void registerBeforeSaveListener(WidgetInstanceManager wim, PartialRendererData<Collection> data)
    {
        WidgetModel widgetModel = wim.getModel();
        Map<String, EventListener<Event>> value = (Map<String, EventListener<Event>>)widgetModel.getValue("_compareViewBeforeSaveListenersMap", Map.class);
        if(value == null)
        {
            value = new ConcurrentHashMap<>();
            widgetModel.put("_compareViewBeforeSaveListenersMap", value);
        }
        value.put("compareViewFeaturesBeforeSaveListener", createBeforeSaveListener(wim, data));
    }


    private EventListener<Event> createBeforeSaveListener(WidgetInstanceManager wim, PartialRendererData<Collection> data)
    {
        return event -> {
            List<Object> allObjects = new ArrayList(data.getComparisonState().getAllObjects());
            for(Object item : allObjects)
            {
                if(item instanceof ProductModel)
                {
                    saveFeatureForProduct(wim, (ProductModel)item);
                }
            }
        };
    }


    private void saveFeatureForProduct(WidgetInstanceManager wim, ProductModel product)
    {
        FeatureList featureList = getClassificationService().getFeatures(product);
        Map<ProductModel, FeatureList> compareObjectFeatureListMap = (Map<ProductModel, FeatureList>)wim.getModel().getValue("comparison-object-featureList-map", Map.class);
        if(featureList != null && compareObjectFeatureListMap != null && compareObjectFeatureListMap.containsKey(product))
        {
            FeatureList updatedFeatureList = compareObjectFeatureListMap.remove(product);
            getClassificationService().replaceFeatures(product, updatedFeatureList);
        }
    }


    protected Optional<Component> createSectionHeaderTitleSuffixLabel(Component parent, Section configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(!isPermitted())
        {
            Div container = new Div();
            Separator separator = new Separator();
            Label noReadAccessLabel = new Label(String.format("[%s]", new Object[] {getLabelService().getAccessDeniedLabel(null)}));
            noReadAccessLabel.addSclass("yw-compareview-section-name-label-no-read-access");
            YTestTools.modifyYTestId((Component)noReadAccessLabel, "compareview-classification-no-read-access");
            container.appendChild((Component)separator);
            container.appendChild((Component)noReadAccessLabel);
            return (Optional)Optional.of(container);
        }
        return Optional.empty();
    }


    protected boolean requiresSectionHeaderTitleRendering(TableCell title, Section configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        return (super.requiresSectionHeaderTitleRendering(title, configuration, data, dataType, widgetInstanceManager) ||
                        !isPermitted());
    }


    protected boolean isPermitted()
    {
        return (getPermissionFacade().canReadType("ClassificationClass") &&
                        getPermissionFacade().canReadType("ClassAttributeAssignment"));
    }


    protected boolean isNotEqual(ComparisonResult result, String groupName)
    {
        return result.getGroupsWithDifferences().contains(groupName);
    }


    protected boolean isNotEqual(ComparisonResult result, Section section, Object item)
    {
        boolean hasDifferentAttribute = super.isNotEqual(result, section, item);
        if(result instanceof BackofficeComparisonResult)
        {
            BackofficeComparisonResult backofficeComparisonResult = (BackofficeComparisonResult)result;
            boolean hasDifferentClassification = false;
            if(backofficeComparisonResult.getClassificationsByGroupName().get(section.getName()) != null)
            {
                hasDifferentClassification = ((List)backofficeComparisonResult.getClassificationsByGroupName().get(section.getName())).stream().anyMatch(classificationDescriptor -> classificationDescriptor.getFeatures().stream().anyMatch(()));
            }
            return (hasDifferentAttribute && hasDifferentClassification);
        }
        return hasDifferentAttribute;
    }


    protected boolean isItemFeatureDifferentThanCorrespondingReferenceItemFeature(BackofficeComparisonResult result, Object item, FeatureDescriptor feature)
    {
        return ((Collection)result.getDifferentObjectsForFeatures().getOrDefault(feature, Collections.emptyList())).stream()
                        .map(ObjectAttributesValueContainer::getObjectId).anyMatch(o -> o.equals(item));
    }


    protected PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
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
