package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2005", forRemoval = true)
public class ExcelBaseProductValidator implements ExcelValidator
{
    protected static final String BASE_PRODUCT_PATTERN = "%s:%s:%s";
    protected static final String VALIDATION_BASE_PRODUCT_DOESNT_MATCH = "excel.import.validation.baseproduct.doesntmatch";
    protected static final String VALIDATION_BASE_PRODUCT_EMPTY = "excel.import.validation.baseproduct.empty";
    protected static final String VALIDATION_BASE_PRODUCT_DOESNT_EXIST = "excel.import.validation.baseproduct.doesntexists";
    private static final Logger LOG = LoggerFactory.getLogger(ExcelBaseProductValidator.class);
    private CatalogVersionService catalogVersionService;
    private ProductService productService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> ctx)
    {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        Map<String, String> parameters = importParameters.getSingleValueParameters();
        if(parameters.get("baseProduct") == null)
        {
            validationMessages.add(new ValidationMessage("excel.import.validation.baseproduct.empty"));
        }
        else
        {
            Optional<ProductModel> product = findValueInCache(parameters, ctx);
            if(!product.isPresent())
            {
                if(parameters.get("catalog") != null && parameters.get("version") != null)
                {
                    validationMessages.add(new ValidationMessage("excel.import.validation.baseproduct.doesntmatch", new Serializable[] {parameters
                                    .get("baseProduct"), parameters
                                    .get("version"), parameters.get("catalog")}));
                }
                else
                {
                    validationMessages.add(new ValidationMessage("excel.import.validation.baseproduct.doesntexists", new Serializable[] {parameters
                                    .get("baseProduct")}));
                }
            }
        }
        return new ExcelValidationResult(validationMessages);
    }


    protected Optional<ProductModel> findValueInCache(Map<String, String> parameters, Map<String, Object> ctx)
    {
        String formattedKey = getFormattedBaseProduct(parameters);
        if(!ctx.containsKey(formattedKey))
        {
            try
            {
                CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(parameters.get("catalog"), parameters.get("version"));
                ProductModel foundProduct = this.productService.getProductForCode(catalogVersion, parameters
                                .get("baseProduct"));
                Optional<ProductModel> result = Optional.ofNullable(foundProduct);
                ctx.put(formattedKey, result);
            }
            catch(UnknownIdentifierException | IllegalArgumentException ex)
            {
                ctx.put(formattedKey, Optional.empty());
                LOG.debug("Cannot find value in cache", ex);
            }
        }
        return (Optional<ProductModel>)ctx.get(formattedKey);
    }


    protected String getFormattedBaseProduct(Map<String, String> parameters)
    {
        return String.format("%s:%s:%s", new Object[] {parameters.get("catalog"), parameters
                        .get("version"), parameters.get("baseProduct")});
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && attributeDescriptor instanceof RelationDescriptorModel && attributeDescriptor
                        .getAttributeType() instanceof de.hybris.platform.core.model.type.ComposedTypeModel && "Product2VariantRelation"
                        .equals(((RelationDescriptorModel)attributeDescriptor).getRelationType().getCode()));
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }
}
