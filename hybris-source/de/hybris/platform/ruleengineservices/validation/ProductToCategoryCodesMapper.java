package de.hybris.platform.ruleengineservices.validation;

import com.google.common.base.Preconditions;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductToCategoryCodesMapper implements Function<ProductModel, Set<String>>
{
    public Set<String> apply(ProductModel product)
    {
        Preconditions.checkArgument((product != null), "Product is required to perform this operation, null given");
        return (Set<String>)product.getSupercategories().stream().map(CategoryModel::getCode).collect(Collectors.toSet());
    }
}
