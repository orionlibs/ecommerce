package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractProductSnIndexerValueProvider<T extends ItemModel, D> extends AbstractSnIndexerValueProvider<T, D>
{
    public static final String PRODUCT_SELECTOR_VALUE_CURRENT = "CURRENT";
    public static final String PRODUCT_SELECTOR_VALUE_CURRENT_PARENT = "CURRENT_PARENT";
    public static final String PRODUCT_SELECTOR_VALUE_BASE = "BASE";
    public static final String PRODUCT_SELECTOR_PARAM = "productSelector";
    public static final String PRODUCT_SELECTOR_PARAM_DEFAULT_VALUE = "CURRENT";


    protected Map<String, Set<ProductModel>> collectProducts(Collection<SnIndexerFieldWrapper> fieldWrappers, ProductModel product) throws SnIndexerException
    {
        Map<String, Set<ProductModel>> products = new HashMap<>();
        for(SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
        {
            String productSelector = resolveProductSelector(fieldWrapper);
            switch(productSelector)
            {
                case "CURRENT":
                    products.computeIfAbsent("CURRENT", key -> collectCurrentProduct(product));
                    continue;
                case "CURRENT_PARENT":
                    products.computeIfAbsent("CURRENT_PARENT", key -> collectCurrentParentProducts(product));
                    continue;
                case "BASE":
                    products.computeIfAbsent("BASE", key -> collectBaseProduct(product));
                    continue;
            }
            throw new SnIndexerException("Invalid product selector: " + productSelector);
        }
        return products;
    }


    protected Set<ProductModel> collectCurrentProduct(ProductModel product)
    {
        return Set.of(product);
    }


    protected Set<ProductModel> collectCurrentParentProducts(ProductModel product)
    {
        if(product instanceof VariantProductModel)
        {
            Set<ProductModel> products = new LinkedHashSet<>();
            ProductModel currentProduct = product;
            while(currentProduct instanceof VariantProductModel)
            {
                products.add(currentProduct);
                currentProduct = ((VariantProductModel)currentProduct).getBaseProduct();
            }
            products.add(currentProduct);
            return products;
        }
        return Set.of(product);
    }


    protected Set<ProductModel> collectBaseProduct(ProductModel product)
    {
        ProductModel currentProduct = product;
        while(currentProduct instanceof VariantProductModel)
        {
            currentProduct = ((VariantProductModel)currentProduct).getBaseProduct();
        }
        return Set.of(currentProduct);
    }


    protected Set<ProductModel> mergeProducts(Map<String, Set<ProductModel>> source)
    {
        Set<ProductModel> target = source.get("CURRENT_PARENT");
        if(target != null)
        {
            return target;
        }
        target = new LinkedHashSet<>();
        target.addAll(source.getOrDefault("CURRENT", Collections.emptySet()));
        target.addAll(source.getOrDefault("BASE", Collections.emptySet()));
        return target;
    }


    protected String resolveProductSelector(SnIndexerFieldWrapper fieldWrapper)
    {
        return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), "productSelector", "CURRENT");
    }
}
