package de.hybris.platform.platformbackoffice.classification;

import com.google.common.collect.Lists;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class FeaturePeristanceHandler
{
    protected ClassificationService classificationService;


    protected void saveFeatures(ProductModel productModel, Map<String, Feature> productFeatures)
    {
        FeatureList finalFeatureList;
        List<Feature> features = Lists.newArrayList(productFeatures.values());
        FeatureList currentFeatureList = this.classificationService.getFeatures(productModel);
        for(Feature persistentFeature : currentFeatureList.getFeatures())
        {
            String wrappedPersistentFeature = BackofficeClassificationUtils.getFeatureQualifierEncoded(persistentFeature
                            .getClassAttributeAssignment());
            for(Feature modifiedFeature : features)
            {
                String wrappedModifiedFeature = BackofficeClassificationUtils.getFeatureQualifierEncoded(modifiedFeature
                                .getClassAttributeAssignment());
                if(StringUtils.equals(wrappedPersistentFeature, wrappedModifiedFeature))
                {
                    if(persistentFeature instanceof LocalizedFeature)
                    {
                        for(Locale locale : ((LocalizedFeature)modifiedFeature).getValuesForAllLocales().keySet())
                        {
                            ((LocalizedFeature)persistentFeature).removeAllValues(locale);
                            for(FeatureValue fValue : ((LocalizedFeature)modifiedFeature).getValuesForAllLocales().get(locale))
                            {
                                ((LocalizedFeature)persistentFeature).addValue(fValue, locale);
                            }
                        }
                        continue;
                    }
                    persistentFeature.setValues(modifiedFeature.getValues());
                }
            }
        }
        if(CollectionUtils.isEmpty(currentFeatureList.getFeatures()))
        {
            finalFeatureList = new FeatureList(Lists.newArrayList(features));
        }
        else
        {
            finalFeatureList = new FeatureList(currentFeatureList.getFeatures());
        }
        this.classificationService.replaceFeatures(productModel, finalFeatureList);
        productFeatures.clear();
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }
}
