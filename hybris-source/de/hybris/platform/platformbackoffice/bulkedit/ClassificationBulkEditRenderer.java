package de.hybris.platform.platformbackoffice.bulkedit;

import com.google.common.collect.Maps;
import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.bulkedit.BulkEditForm;
import com.hybris.backoffice.bulkedit.renderer.BulkEditRenderer;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.ValidationHandler;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.platformbackoffice.bulkedit.renderer.ClassificationEditorBulkEditRenderer;
import de.hybris.platform.platformbackoffice.bulkedit.renderer.ClassificationSectionBulkEditRenderer;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;

public class ClassificationBulkEditRenderer extends BulkEditRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(ClassificationBulkEditRenderer.class);
    private static final String MODIFIED_FEATURES_MAP_MODEL_PARAM = "modifiedProductFeatures";
    private static final String MODIFIED_FEATURES_MODEL_PARAM_PREFIX = "modifiedProductFeatures.pknull";
    private static final String LABEL_GREY_AREA_CLASSIFICATION_TITLE = "bulkedit.grey.area.classification.title";
    private ObjectFacade objectFacade;
    private ClassificationSectionBulkEditRenderer classificationSectionBulkEditRenderer;
    private ClassificationEditorBulkEditRenderer classificationEditorBulkEditRenderer;


    protected boolean validateBulkEditForm(BulkEditForm form, Map<String, String> params)
    {
        if(form == null)
        {
            getNotificationService().notifyUser("bulkEdit", "missingForm", NotificationEvent.Level.FAILURE, new Object[0]);
            return false;
        }
        if(!StringUtils.equalsIgnoreCase(params.get("bulkEditRequiresSelectedAttributes"), "false") &&
                        !form.hasSelectedAttributes() && !hasClassificationAttributes(form))
        {
            getNotificationService().notifyUser("bulkEdit", "missingAttributes", NotificationEvent.Level.FAILURE, new Object[0]);
            return false;
        }
        return true;
    }


    private boolean hasClassificationAttributes(BulkEditForm form)
    {
        return ((Boolean)toProductBulkEditForm(form).<Boolean>map(ClassificationBulkEditForm::hasSelectedClassificationAttributes).orElse(Boolean.valueOf(false))).booleanValue();
    }


    protected void renderAttributes(ValidatableContainer validatableContainer, Map<String, String> params, DataType dataType, WidgetInstanceManager wim, BulkEditForm form, ValidationHandler proxyValidationHandler, Div attributesContainer)
    {
        initializeWidgetModelIfNotAlreadyInitialized(wim);
        List<Attribute> selectedAttributes = extractClassificationAttributes(form);
        Map<ClassificationClassModel, List<Attribute>> groupedAttributes = groupAttributesByClassificationClass(selectedAttributes);
        groupedAttributes.entrySet().stream().sorted(Comparator.comparing(entry -> ((ClassificationClassModel)entry.getKey()).getCode())).forEach(entry -> {
            Component section = getClassificationSectionBulkEditRenderer().render((Component)attributesContainer, (ClassificationClassModel)entry.getKey());
            ((List)entry.getValue()).forEach(());
        });
    }


    protected void renderGreyAreaIfNeeded(Component component, Map<String, String> params, BulkEditForm form)
    {
        if(form instanceof ClassificationBulkEditForm && !((ClassificationBulkEditForm)form).hasSelectedClassificationAttributes() &&
                        StringUtils.equalsIgnoreCase(params.get("showGrayArea"), "true"))
        {
            component.appendChild((Component)renderGreyArea("bulkedit.grey.area.classification.title"));
        }
    }


    protected Map<ClassificationClassModel, List<Attribute>> groupAttributesByClassificationClass(List<Attribute> selectedAttributes)
    {
        return (Map<ClassificationClassModel, List<Attribute>>)selectedAttributes.stream().filter(attribute -> (getClassAttributeAssignmentModel(attribute) != null))
                        .sorted(getAttributeComparator()).collect(
                                        Collectors.groupingBy(attribute -> getClassAttributeAssignmentModel(attribute).getClassificationClass()));
    }


    private Comparator<Attribute> getAttributeComparator()
    {
        return Comparator.comparing(attr -> getClassAttributeAssignmentModel(attr).getPosition(),
                        Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Attribute::getDisplayName);
    }


    private List<Attribute> extractClassificationAttributes(BulkEditForm form)
    {
        return toProductBulkEditForm(form).<List<Attribute>>map(productBulkEditForm -> extractClassificationAttributes(productBulkEditForm.getClassificationAttributesForm().getSelectedAttributes()))
                        .orElseGet(ArrayList::new);
    }


    private List<Attribute> extractClassificationAttributes(Set<Attribute> attributes)
    {
        List<Attribute> classificationAttributes = new ArrayList<>();
        attributes.forEach(attribute -> {
            if(attribute.hasSubAttributes() && !subAttributesContainIsoCode(attribute))
            {
                classificationAttributes.addAll(extractClassificationAttributes(attribute.getSubAttributes()));
            }
            else
            {
                classificationAttributes.add(attribute);
            }
        });
        return classificationAttributes;
    }


    private boolean subAttributesContainIsoCode(Attribute attribute)
    {
        return (((Attribute)attribute.getSubAttributes().iterator().next()).getIsoCode() != null);
    }


    private void initializeWidgetModelIfNotAlreadyInitialized(WidgetInstanceManager wim)
    {
        if(wim.getModel().getValue("modifiedProductFeatures", Map.class) == null)
        {
            wim.getModel().setValue("modifiedProductFeatures", Maps.newHashMap());
            wim.getModel().setValue("modifiedProductFeatures.pknull", Maps.newHashMap());
        }
    }


    private Optional<ClassificationBulkEditForm> toProductBulkEditForm(BulkEditForm form)
    {
        Objects.requireNonNull(ClassificationBulkEditForm.class);
        Objects.requireNonNull(ClassificationBulkEditForm.class);
        return Optional.<BulkEditForm>of(form).filter(ClassificationBulkEditForm.class::isInstance).map(ClassificationBulkEditForm.class::cast);
    }


    protected Editor createEditor(DataType genericType, WidgetInstanceManager wim, Attribute attribute, Map<String, String> params)
    {
        boolean isClassificationAttribute = extractClassificationAttributes(getBulkEditForm(wim, params)).contains(attribute);
        if(isClassificationAttribute)
        {
            return getClassificationEditorBulkEditRenderer().render(wim, attribute, params);
        }
        return super.createEditor(genericType, wim, attribute, params);
    }


    private ClassAttributeAssignmentModel getClassAttributeAssignmentModel(Attribute attribute)
    {
        try
        {
            return (ClassAttributeAssignmentModel)this.objectFacade.load(attribute.getQualifier());
        }
        catch(ObjectNotFoundException e)
        {
            LOG.error(String.format("Cannot load %s for attribute '%s'", new Object[] {ClassAttributeAssignmentModel.class.getName(), attribute
                            .getDisplayName()}), (Throwable)e);
            return null;
        }
    }


    protected Optional<Checkbox> createClearAttributeSwitch(DataType dataType, Attribute attribute, BulkEditForm form)
    {
        if(isClassificationAttribute(attribute, form))
        {
            boolean isMandatory = ((Boolean)Optional.<Attribute>ofNullable(attribute).map(this::getClassAttributeAssignmentModel).map(ClassAttributeAssignmentModel::getMandatory).orElse(Boolean.valueOf(false))).booleanValue();
            if(!isMandatory)
            {
                Checkbox clearAttributeSwitch = new Checkbox(Labels.getLabel("bulkedit.clear.value"));
                clearAttributeSwitch.setSclass(String.format("%s %s", new Object[] {"ye-switch-checkbox", "ye-switch-checkbox-delete"}));
                return Optional.of(clearAttributeSwitch);
            }
            return Optional.empty();
        }
        return createClearAttributeSwitch(dataType, attribute.getQualifier());
    }


    private boolean isClassificationAttribute(Attribute attribute, BulkEditForm form)
    {
        return extractClassificationAttributes(form).contains(attribute);
    }


    protected Optional<Checkbox> createMergeCheckBox(DataType dataType, Attribute attribute, BulkEditForm form)
    {
        if(isClassificationAttribute(attribute, form))
        {
            return createMergeCheckBoxForClassificationAttribute(attribute);
        }
        return createMergeCheckBox(dataType, attribute.getQualifier());
    }


    private Optional<Checkbox> createMergeCheckBoxForClassificationAttribute(Attribute attribute)
    {
        if(isClassificationAttributeMergable(attribute))
        {
            Checkbox overrideExisting = new Checkbox(Labels.getLabel("bulkedit.merge.existing"));
            overrideExisting.setSclass("ye-switch-checkbox");
            return Optional.of(overrideExisting);
        }
        return Optional.empty();
    }


    private boolean isClassificationAttributeMergable(Attribute attribute)
    {
        ClassAttributeAssignmentModel assignment = getClassAttributeAssignmentModel(attribute);
        return (assignment != null && BooleanUtils.isTrue(assignment.getMultiValued()));
    }


    protected void clearModelValue(WidgetInstanceManager wim, DataType dataType, Attribute attribute, String fullAttributeProperty, Map<String, String> params)
    {
        if(isClassificationAttribute(attribute, getBulkEditForm(wim, params)))
        {
            ClassificationInfo info = new ClassificationInfo(getClassAttributeAssignmentModel(attribute), new HashMap<>());
            wim.getModel().setValue(fullAttributeProperty, info);
        }
        else
        {
            clearModelValue(wim, dataType, attribute, fullAttributeProperty);
        }
    }


    public ClassificationSectionBulkEditRenderer getClassificationSectionBulkEditRenderer()
    {
        return this.classificationSectionBulkEditRenderer;
    }


    @Required
    public void setClassificationSectionBulkEditRenderer(ClassificationSectionBulkEditRenderer classificationSectionBulkEditRenderer)
    {
        this.classificationSectionBulkEditRenderer = classificationSectionBulkEditRenderer;
    }


    public ClassificationEditorBulkEditRenderer getClassificationEditorBulkEditRenderer()
    {
        return this.classificationEditorBulkEditRenderer;
    }


    @Required
    public void setClassificationEditorBulkEditRenderer(ClassificationEditorBulkEditRenderer classificationEditorBulkEditRenderer)
    {
        this.classificationEditorBulkEditRenderer = classificationEditorBulkEditRenderer;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }
}
