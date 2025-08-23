package com.hybris.backoffice.excel.translators;

import com.google.common.base.Joiner;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

@Deprecated(since = "2005", forRemoval = true)
public class ExcelProductSupercategoriesTypeTranslator extends AbstractCatalogVersionAwareTranslator<Collection<CategoryModel>>
{
    private static final String PATTERN = "%s:%s";
    public static final String CATEGORY_TOKEN = "category";


    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        return (attributeDescriptor instanceof RelationDescriptorModel && "CategoryProductRelation"
                        .equals(((RelationDescriptorModel)attributeDescriptor).getRelationType().getCode()) &&
                        getTypeService().isAssignableFrom("Product", attributeDescriptor.getEnclosingType().getCode()));
    }


    public Optional<Object> exportData(Collection<CategoryModel> objectToExport)
    {
        Objects.requireNonNull(Joiner.on(','));
        Objects.requireNonNull(Object.class);
        return CollectionUtils.emptyIfNull(objectToExport).stream().map(this::exportCategory).reduce((x$0, x$1) -> rec$.join(x$0, x$1, new Object[0])).map(Object.class::cast);
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        List<String> categories = new ArrayList<>();
        for(Map<String, String> params : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            String catalogVersion = catalogVersionData(params);
            if(StringUtils.isNotBlank(catalogVersion) && StringUtils.isNotBlank(params.get("category")))
            {
                String category = String.format("%s:%s", new Object[] {params.get("category"), catalogVersion});
                categories.add(category);
            }
        }
        ImpexHeaderValue categoryHeader = (new ImpexHeaderValue.Builder(String.format("%s(%s, %s)", new Object[] {"supercategories", "code", catalogVersionHeader("Category")}))).withQualifier(attributeDescriptor.getQualifier()).build();
        return new ImpexValue(String.join(",", (Iterable)categories), categoryHeader);
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return String.format("%s:%s", new Object[] {"category", referenceCatalogVersionFormat()});
    }


    protected String exportCategory(CategoryModel category)
    {
        CatalogVersionModel catalogVersion = category.getCatalogVersion();
        return String.format("%s:%s", new Object[] {category.getCode(), exportCatalogVersionData(catalogVersion)});
    }
}
