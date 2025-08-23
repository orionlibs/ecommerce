package de.hybris.platform.platformbackoffice.bulkedit.renderer;

import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.YTestTools;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import java.util.Locale;
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
import org.zkoss.zk.ui.Component;

public class DefaultClassificationEditorBulkEditRenderer implements ClassificationEditorBulkEditRenderer
{
    private static final String MODIFIED_FEATURES_MAP_MODEL_PARAM = "modifiedProductFeatures";
    private static final String MODIFIED_FEATURES_MODEL_PARAM_PREFIX = "modifiedProductFeatures.pknull";
    private static final String FEATURE_EDITOR_CODE = "Feature";
    private static final String EDITED_PROPERTY_QUALIFIER_MODEL_KEY = "editedPropertyQualifier";
    protected static final String PARAM_BULK_EDIT_FORM_MODEL_KEY = "bulkEditFormModelKey";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultClassificationEditorBulkEditRenderer.class);
    private ObjectFacade objectFacade;


    public Editor render(WidgetInstanceManager wim, Attribute attribute, Map<String, String> params)
    {
        Set<Locale> selectedLocalesForAttribute = getSelectedLocalesForAttribute(attribute);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = getClassAttributeAssignmentModel(attribute);
        Feature feature = createFeature(classAttributeAssignmentModel);
        Object initialValue = createInitialValue(classAttributeAssignmentModel);
        String featureQualifier = createFeatureQualifier(classAttributeAssignmentModel);
        String encodeFeatureQualifier = createFeatureQualifierEncoded(featureQualifier);
        String templateObjectPath = getTemplateObjectPath(params).buildPath();
        String classificationQualifier = String.format("%s['%s']", new Object[] {templateObjectPath, encodeFeatureQualifier});
        putFeatureInModelIfNotAlreadyPresent(wim, encodeFeatureQualifier, feature);
        Editor editor = new Editor();
        editor.setReadableLocales(selectedLocalesForAttribute);
        editor.setWritableLocales(selectedLocalesForAttribute);
        editor.setWidgetInstanceManager(wim);
        editor.setType("Feature");
        Objects.requireNonNull(editor);
        Optional.<ClassificationInfo>ofNullable((ClassificationInfo)wim.getModel().getValue(classificationQualifier, ClassificationInfo.class)).filter(classificationInfo -> (classificationInfo.getValue() != null)).ifPresentOrElse(editor::setValue, () -> editor.setInitialValue(initialValue));
        editor.setOptional(BooleanUtils.isNotTrue(classAttributeAssignmentModel.getMandatory()));
        editor.setAtomic(true);
        editor.setProperty(classificationQualifier);
        editor.addParameter("editedPropertyQualifier", encodeFeatureQualifier);
        if(classAttributeAssignmentModel.getRange().booleanValue())
        {
            editor.addParameter("allowInfiniteEndpoints", Boolean.valueOf(true));
        }
        YTestTools.modifyYTestId((Component)editor, "editor_" + featureQualifier);
        editor.afterCompose();
        return editor;
    }


    protected Set<Locale> getSelectedLocalesForAttribute(Attribute attribute)
    {
        return (Set<Locale>)attribute.getSubAttributes().stream().map(Attribute::getIsoCode).filter(StringUtils::isNotBlank).sorted()
                        .map(Locale::forLanguageTag).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }


    protected ClassAttributeAssignmentModel getClassAttributeAssignmentModel(Attribute attribute)
    {
        try
        {
            return (ClassAttributeAssignmentModel)getObjectFacade().load(attribute.getQualifier());
        }
        catch(ObjectNotFoundException e)
        {
            LOG.error(String.format("Cannot load %s for attribute '%s'", new Object[] {ClassAttributeAssignmentModel.class.getName(), attribute
                            .getDisplayName()}), (Throwable)e);
            return null;
        }
    }


    protected Feature createFeature(ClassAttributeAssignmentModel classAttributeAssignmentModel)
    {
        if(BooleanUtils.isTrue(classAttributeAssignmentModel.getLocalized()))
        {
            return (Feature)new LocalizedFeature(classAttributeAssignmentModel, null, null);
        }
        return (Feature)new UnlocalizedFeature(classAttributeAssignmentModel, new de.hybris.platform.classification.features.FeatureValue[0]);
    }


    protected ObjectValuePath getTemplateObjectPath(Map<String, String> params)
    {
        String formPath = params.get("bulkEditFormModelKey");
        if(StringUtils.isBlank(formPath))
        {
            LOG.warn("Missing template item param");
            return ObjectValuePath.empty();
        }
        return ObjectValuePath.parse(formPath).append("templateObject");
    }


    protected Object createInitialValue(ClassAttributeAssignmentModel classAttributeAssignmentModel)
    {
        return BackofficeClassificationUtils.convertFeatureToClassificationInfo(createFeature(classAttributeAssignmentModel));
    }


    protected String createFeatureQualifier(ClassAttributeAssignmentModel classAttributeAssignmentModel)
    {
        return BackofficeClassificationUtils.getFeatureQualifier(classAttributeAssignmentModel);
    }


    protected String createFeatureQualifierEncoded(String featureQualifier)
    {
        return BackofficeClassificationUtils.getFeatureQualifierEncoded(featureQualifier);
    }


    protected void putFeatureInModelIfNotAlreadyPresent(WidgetInstanceManager wim, String encodeFeatureQualifier, Object initialValue)
    {
        String initialValueKeyInModel = String.format("%s.%s", new Object[] {"modifiedProductFeatures.pknull", encodeFeatureQualifier});
        if(wim.getModel().getValue(initialValueKeyInModel, Object.class) == null)
        {
            wim.getModel().setValue(initialValueKeyInModel, initialValue);
        }
    }


    public ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
