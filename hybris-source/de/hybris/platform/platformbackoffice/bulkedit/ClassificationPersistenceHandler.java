package de.hybris.platform.platformbackoffice.bulkedit;

import com.google.common.base.Strings;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.bulkedit.dto.ClassificationChangeDto;
import de.hybris.platform.platformbackoffice.classification.FeaturePeristanceHandler;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationPersistenceHandler extends FeaturePeristanceHandler
{
    private ModelService modelService;
    private CockpitGlobalEventPublisher cockpitGlobalEventPublisher;


    public void saveChanges(ProductModel product, Collection<ClassificationChangeDto> changes)
    {
        Map<String, Feature> featuresToSave = new HashMap<>();
        for(ClassificationChangeDto change : changes)
        {
            Feature feature = featuresToSave.containsKey(change.getEncodedQualifier()) ? featuresToSave.get(change.getEncodedQualifier()) : this.classificationService.getFeature(product, change.getFeature().getClassAttributeAssignment());
            if(feature == null || hasEmptyValueWithoutClearCheckbox(change, change.getFeature()))
            {
                continue;
            }
            if(change.isClear())
            {
                storeValues(feature, new ArrayList<>(), change.getIsoCode());
            }
            else if(change.isMerge())
            {
                Feature changeValue = change.getFeature();
                List<FeatureValue> mergedValues = new ArrayList<>();
                mergedValues.addAll(getValues(feature, change.getIsoCode()));
                mergedValues.addAll(getValues(changeValue, change.getIsoCode()));
                storeValues(feature, mergedValues, change.getIsoCode());
            }
            else
            {
                storeValues(feature, getValues(change.getFeature(), change.getIsoCode()), change.getIsoCode());
            }
            featuresToSave.put(change.getEncodedQualifier(), feature);
        }
        saveFeatures(product, featuresToSave);
        this.modelService.refresh(product);
        this.cockpitGlobalEventPublisher.publish("objectsUpdated", product, (new DefaultContext.Builder()).build());
    }


    protected void saveFeatures(ProductModel productModel, Map<String, Feature> productFeatures)
    {
        super.saveFeatures(productModel, productFeatures);
    }


    protected boolean hasEmptyValueWithoutClearCheckbox(ClassificationChangeDto change, Feature feature)
    {
        return (getValues(feature, change.getIsoCode()).isEmpty() && !change.isClear());
    }


    protected void storeValues(Feature feature, List<FeatureValue> values, String isoCode)
    {
        if(feature instanceof LocalizedFeature)
        {
            ((LocalizedFeature)feature).setValues(values, Locale.forLanguageTag(isoCode));
        }
        else
        {
            feature.setValues(values);
        }
    }


    protected List<FeatureValue> getValues(Feature feature, String isoCode)
    {
        if(feature instanceof LocalizedFeature)
        {
            return ((LocalizedFeature)feature).getValues(Locale.forLanguageTag(Strings.nullToEmpty(isoCode)));
        }
        return feature.getValues();
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCockpitGlobalEventPublisher(CockpitGlobalEventPublisher cockpitGlobalEventPublisher)
    {
        this.cockpitGlobalEventPublisher = cockpitGlobalEventPublisher;
    }
}
