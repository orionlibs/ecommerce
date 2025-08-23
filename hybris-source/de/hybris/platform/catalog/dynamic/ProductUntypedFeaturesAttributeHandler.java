package de.hybris.platform.catalog.dynamic;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

public class ProductUntypedFeaturesAttributeHandler implements DynamicAttributeHandler<List<ProductFeatureModel>, ProductModel>
{
    public List<ProductFeatureModel> get(ProductModel model)
    {
        return getUntypedFeatures(model);
    }


    public void set(ProductModel model, List<ProductFeatureModel> incomingUntypedFeatures)
    {
        validateUntypedProductFeatures(model, incomingUntypedFeatures);
        List<ProductFeatureModel> allProductFeatures = model.getFeatures();
        List<ProductFeatureModel> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(allProductFeatures))
        {
            result.addAll((Collection<? extends ProductFeatureModel>)allProductFeatures.stream().filter(e -> (e.getClassificationAttributeAssignment() != null))
                            .collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(incomingUntypedFeatures))
        {
            result.addAll(incomingUntypedFeatures);
        }
        model.setFeatures(result);
    }


    private void validateUntypedProductFeatures(ProductModel model, List<ProductFeatureModel> untypedFeatures)
    {
        if(CollectionUtils.isNotEmpty(untypedFeatures))
        {
            for(ProductFeatureModel untypedFeature : untypedFeatures)
            {
                if(!model.equals(untypedFeature.getProduct()))
                {
                    throw new UnsupportedOperationException("feature " + untypedFeature + " of " + untypedFeatures + " does not belong to product " + model);
                }
                if(untypedFeature.getClassificationAttributeAssignment() != null)
                {
                    throw new UnsupportedOperationException("feature " + untypedFeature + " of " + untypedFeatures + " is not untyped by belongs to attribute " + untypedFeature
                                    .getClassificationAttributeAssignment());
                }
            }
        }
    }


    private List<ProductFeatureModel> getUntypedFeatures(ProductModel model)
    {
        return (List<ProductFeatureModel>)ListUtils.emptyIfNull(model.getFeatures()).stream().filter(e -> (e.getClassificationAttributeAssignment() == null))
                        .collect(Collectors.toList());
    }
}
