package de.hybris.platform.ruleengineservices.validation;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductToProductCodesMapper implements Function<ProductModel, Set<String>>
{
    public Set<String> apply(ProductModel product)
    {
        Preconditions.checkArgument((product != null), "Product is required to perform this operation, null given");
        Set<String> codes = (Set<String>)flatten(product).map(ProductModel::getCode).collect(Collectors.toSet());
        codes.add(product.getCode());
        return codes;
    }


    protected Stream<ProductModel> flatten(ProductModel product)
    {
        return Stream.concat(Stream.of(product), (product.getVariants() == null) ? Stream.<ProductModel>empty() :
                        product.getVariants().stream().flatMap(this::flatten));
    }
}
