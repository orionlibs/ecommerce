package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ExcelCategoryValidator implements ExcelValidator
{
    protected static final String CATEGORY_PATTERN = "%s:%s:%s";
    protected static final String VALIDATION_CATEGORY_DOESNT_MATCH = "excel.import.validation.category.doesntmatch";
    protected static final String VALIDATION_CATEGORY_EMPTY = "excel.import.validation.category.empty";
    private static final Logger LOG = LoggerFactory.getLogger(ExcelCategoryValidator.class);
    private CatalogVersionService catalogVersionService;
    private CategoryService categoryService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> ctx)
    {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for(Map<String, String> parameters : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            Objects.requireNonNull(validationMessages);
            validateSingleValue(ctx, parameters).ifPresent(validationMessages::add);
        }
        return new ExcelValidationResult(validationMessages);
    }


    protected Optional<ValidationMessage> validateSingleValue(Map<String, Object> ctx, Map<String, String> parameters)
    {
        if(parameters.get("category") == null)
        {
            return Optional.of(new ValidationMessage("excel.import.validation.category.empty"));
        }
        Optional<CategoryModel> categoryFromCache = findValueInCache(ctx, parameters);
        if(!categoryFromCache.isPresent() && parameters.get("catalog") != null && parameters
                        .get("version") != null)
        {
            return Optional.of(new ValidationMessage("excel.import.validation.category.doesntmatch", new Serializable[] {parameters
                            .get("category"), parameters
                            .get("version"), parameters.get("catalog")}));
        }
        return Optional.empty();
    }


    protected Optional<CategoryModel> findValueInCache(Map<String, Object> ctx, Map<String, String> parameters)
    {
        String formattedCategory = getFormattedCategory(parameters);
        if(ctx.containsKey(formattedCategory))
        {
            return (Optional<CategoryModel>)ctx.get(formattedCategory);
        }
        try
        {
            CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(parameters.get("catalog"), parameters.get("version"));
            CategoryModel categoryModel = getCategoryService().getCategoryForCode(catalogVersion, parameters
                            .get("category"));
            Optional<CategoryModel> result = Optional.ofNullable(categoryModel);
            ctx.put(formattedCategory, result);
            return result;
        }
        catch(UnknownIdentifierException | IllegalArgumentException ex)
        {
            LOG.debug("Value not found in cache", ex);
            ctx.put(formattedCategory, Optional.empty());
            return Optional.empty();
        }
    }


    protected String getFormattedCategory(Map<String, String> parameters)
    {
        return String.format("%s:%s:%s", new Object[] {parameters.get("catalog"), parameters
                        .get("version"), parameters
                        .get("category")});
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && attributeDescriptor instanceof RelationDescriptorModel && "CategoryProductRelation"
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


    public CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    @Required
    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }
}
