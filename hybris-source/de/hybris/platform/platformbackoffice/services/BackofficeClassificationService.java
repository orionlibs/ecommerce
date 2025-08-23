package de.hybris.platform.platformbackoffice.services;

import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BackofficeClassificationService
{
    Map<String, Map<FeatureDescriptor, ClassificationInfo>> getFeatureValues(ProductModel paramProductModel, List<ClassificationDescriptor> paramList);


    default Map<String, Map<FeatureDescriptor, ClassificationInfo>> getFeatureValuesFromFeatureList(List<ClassificationDescriptor> classificationDescriptors, FeatureList featureList)
    {
        return null;
    }


    List<ClassificationDescriptor> getClassificationDescriptors(Set<ProductModel> paramSet);
}
