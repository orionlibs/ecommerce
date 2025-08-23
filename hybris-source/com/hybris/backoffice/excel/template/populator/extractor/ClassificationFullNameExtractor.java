package com.hybris.backoffice.excel.template.populator.extractor;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;

public class ClassificationFullNameExtractor
{
    private static final String LOCALIZED_PATTERN = "${owner}.${attribute}[${locale}] - ${systemId}/${version}";
    private static final String NON_LOCALIZED_PATTERN = "${owner}.${attribute} - ${systemId}/${version}";


    public String extract(ExcelClassificationAttribute excelAttribute)
    {
        Map<String, String> params = new HashMap<>();
        params.put("systemId", getSystemId((ExcelAttribute)excelAttribute));
        params.put("version", getSystemVersion((ExcelAttribute)excelAttribute));
        params.put("owner", getClassificationClassName((ExcelAttribute)excelAttribute));
        params.put("attribute", getClassificationAttributeName((ExcelAttribute)excelAttribute));
        if(StringUtils.isNotBlank(excelAttribute.getIsoCode()))
        {
            params.put("locale", excelAttribute.getIsoCode());
            return (new StrSubstitutor(params)).replace("${owner}.${attribute}[${locale}] - ${systemId}/${version}");
        }
        return (new StrSubstitutor(params)).replace("${owner}.${attribute} - ${systemId}/${version}");
    }


    private static String getSystemId(ExcelAttribute excelAttribute)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return Optional.<ExcelAttribute>of(excelAttribute).filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast)
                        .map(ExcelClassificationAttribute::getAttributeAssignment)
                        .map(ClassAttributeAssignmentModel::getSystemVersion)
                        .map(ClassificationSystemVersionModel::getCatalog)
                        .map(CatalogModel::getId)
                        .orElse("");
    }


    private static String getClassificationClassName(ExcelAttribute excelAttribute)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return Optional.<ExcelAttribute>of(excelAttribute).filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast)
                        .map(ExcelClassificationAttribute::getAttributeAssignment)
                        .map(ClassAttributeAssignmentModel::getClassificationClass)
                        .map(CategoryModel::getCode)
                        .orElse("");
    }


    private static String getSystemVersion(ExcelAttribute excelAttribute)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return Optional.<ExcelAttribute>of(excelAttribute).filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast)
                        .map(ExcelClassificationAttribute::getAttributeAssignment)
                        .map(ClassAttributeAssignmentModel::getSystemVersion)
                        .map(CatalogVersionModel::getVersion)
                        .orElse("");
    }


    private static String getClassificationAttributeName(ExcelAttribute excelAttribute)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return Optional.<ExcelAttribute>of(excelAttribute).filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast)
                        .map(ExcelClassificationAttribute::getAttributeAssignment)
                        .map(ClassAttributeAssignmentModel::getClassificationAttribute)
                        .map(ClassificationAttributeModel::getCode)
                        .orElse("");
    }
}
