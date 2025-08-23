package de.hybris.platform.platformbackoffice.widgets.compare.model.impl;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.widgets.compare.model.ComparisonState;
import com.hybris.cockpitng.widgets.compare.model.impl.DefaultPartialRendererData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import java.util.HashMap;
import java.util.Map;

public class BackofficePartialRendererData<D> extends DefaultPartialRendererData<D>
{
    private Map<ProductModel, Map<String, Map<FeatureDescriptor, ClassificationInfo>>> featureValues;


    public BackofficePartialRendererData(ComparisonResult comparisonResult, D data, ComparisonState comparisonState)
    {
        super(comparisonResult, data, comparisonState);
        this.featureValues = new HashMap<>();
    }


    public BackofficePartialRendererData(ComparisonResult comparisonResult, D data, ComparisonState comparisonState, Map<ProductModel, Map<String, Map<FeatureDescriptor, ClassificationInfo>>> featureValues)
    {
        super(comparisonResult, data, comparisonState);
        this.featureValues = featureValues;
    }


    public Map<ProductModel, Map<String, Map<FeatureDescriptor, ClassificationInfo>>> getFeatureValues()
    {
        return this.featureValues;
    }
}
