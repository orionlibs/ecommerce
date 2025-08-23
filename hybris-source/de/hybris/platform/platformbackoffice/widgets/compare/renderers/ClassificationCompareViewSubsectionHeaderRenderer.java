package de.hybris.platform.platformbackoffice.widgets.compare.renderers;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.compare.renderer.AbstractCompareViewSectionHeaderRenderer;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.platformbackoffice.widgets.compare.model.BackofficeComparisonResult;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;

public class ClassificationCompareViewSubsectionHeaderRenderer extends AbstractCompareViewSectionHeaderRenderer<ClassificationDescriptor>
{
    private static final String SCLASS_SUBSECTION_NAME_NO_READ_ACCESS_LABEL = "yw-compareview-subsection-name-label-no-read-access";
    private static final String LABEL_NO_READ_ACCESS_YTEST_ID = "compareview-classification-no-read-access";
    private LabelService labelService;
    private ClassificationSystemService classificationSystemService;
    private PermissionFacade permissionFacade;


    protected Optional<Component> createSectionHeaderTitleSuffixLabel(Component parent, ClassificationDescriptor configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(!getPermissionFacade().canReadType("ClassificationAttribute"))
        {
            Div container = new Div();
            Separator separator = new Separator();
            Label noReadAccessLabel = new Label(String.format("[%s]", new Object[] {getLabelService().getAccessDeniedLabel(null)}));
            noReadAccessLabel.addSclass("yw-compareview-subsection-name-label-no-read-access");
            YTestTools.modifyYTestId((Component)noReadAccessLabel, "compareview-classification-no-read-access");
            container.appendChild((Component)separator);
            container.appendChild((Component)noReadAccessLabel);
            return (Optional)Optional.of(container);
        }
        return Optional.empty();
    }


    protected String getSectionName(ClassificationDescriptor configuration)
    {
        return configuration.getName();
    }


    protected String getTranslatedSectionName(ClassificationDescriptor configuration, WidgetInstanceManager widgetInstanceManager)
    {
        String localizedLabelByWidget = this.labelService.getShortObjectLabel(getClassificationClass(configuration));
        return StringUtils.isNotBlank(localizedLabelByWidget) ? localizedLabelByWidget :
                        Labels.getLabel(getSectionName(configuration), getSectionName(configuration));
    }


    protected ClassificationClassModel getClassificationClass(ClassificationDescriptor configuration)
    {
        ClassificationSystemVersionModel classificationSystemVersion = this.classificationSystemService.getSystemVersion(configuration.getCatalogDescriptor().getId(), configuration.getCatalogDescriptor().getVersion());
        return this.classificationSystemService.getClassForCode(classificationSystemVersion, configuration.getCode());
    }


    protected String getTooltipText(ClassificationDescriptor configuration)
    {
        return this.labelService.getObjectLabel(getClassificationClass(configuration));
    }


    protected boolean isNotEqual(ComparisonResult result, ClassificationDescriptor configuration)
    {
        if(result instanceof BackofficeComparisonResult)
        {
            BackofficeComparisonResult backofficeComparisonResult = (BackofficeComparisonResult)result;
            return backofficeComparisonResult.getDifferentClassifications().stream()
                            .anyMatch(classificationDescriptor -> classificationDescriptor.getCode().equals(configuration.getCode()));
        }
        return false;
    }


    protected boolean isNotEqual(ComparisonResult result, ClassificationDescriptor section, Object item)
    {
        return section.getFeatures().stream().anyMatch(featureDescriptor -> isItemFeatureDifferentThanCorrespondingReferenceItemFeature(result, item, featureDescriptor));
    }


    protected boolean isItemFeatureDifferentThanCorrespondingReferenceItemFeature(ComparisonResult result, Object item, FeatureDescriptor featureDescriptor)
    {
        if(result instanceof BackofficeComparisonResult)
        {
            BackofficeComparisonResult backofficeComparisonResult = (BackofficeComparisonResult)result;
            return ((Collection)backofficeComparisonResult.getDifferentObjectsForFeatures()
                            .getOrDefault(featureDescriptor, Collections.emptySet())).stream().map(ObjectAttributesValueContainer::getObjectId)
                            .anyMatch(o -> o.equals(item));
        }
        return false;
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


    protected ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    @Required
    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
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
}
