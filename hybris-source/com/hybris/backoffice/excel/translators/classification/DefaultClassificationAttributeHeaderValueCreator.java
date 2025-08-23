package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Required;

public class DefaultClassificationAttributeHeaderValueCreator implements ClassificationAttributeHeaderValueCreator
{
    private static final String PATTERN = "@${columnId}${references}[class='${classId}',system='${systemId}',version='${systemVersion}',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]";
    private RequiredAttributesFactory requiredAttributesFactory;


    public String create(@Nonnull ExcelClassificationAttribute attribute, @Nonnull ExcelImportContext ignored)
    {
        Map<String, String> params = new HashMap<>();
        params.put("columnId", getColumnId(attribute));
        params.put("classId", getClassId(attribute));
        params.put("systemId", getSystemId(attribute));
        params.put("systemVersion", getSystemVersion(attribute));
        params.put("references", getReferences(attribute).orElse(""));
        return (new StrSubstitutor(params)).replace("@${columnId}${references}[class='${classId}',system='${systemId}',version='${systemVersion}',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
    }


    private static String getColumnId(ExcelClassificationAttribute attribute)
    {
        return attribute.getQualifier();
    }


    private static String getSystemId(ExcelClassificationAttribute attribute)
    {
        return attribute.getAttributeAssignment().getSystemVersion().getCatalog().getId();
    }


    private static String getClassId(ExcelClassificationAttribute attribute)
    {
        return attribute.getAttributeAssignment().getClassificationClass().getCode();
    }


    private static String getSystemVersion(ExcelClassificationAttribute attribute)
    {
        return attribute.getAttributeAssignment().getSystemVersion().getVersion();
    }


    private Optional<String> getReferences(ExcelClassificationAttribute attribute)
    {
        Objects.requireNonNull(this.requiredAttributesFactory);
        return Optional.<ComposedTypeModel>ofNullable(attribute.getAttributeAssignment().getReferenceType()).map(this.requiredAttributesFactory::create)
                        .map(this::createReferencesHeader);
    }


    private String createReferencesHeader(RequiredAttribute requiredAttribute)
    {
        String joinedChildrenValues = requiredAttribute.getChildren().stream().map(this::createReferencesHeader).collect(Collectors.joining(","));
        if(!requiredAttribute.getChildren().isEmpty())
        {
            joinedChildrenValues = String.format("(%s)", new Object[] {joinedChildrenValues});
        }
        return requiredAttribute.getQualifier() + requiredAttribute.getQualifier();
    }


    @Required
    public void setRequiredAttributesFactory(RequiredAttributesFactory requiredAttributesFactory)
    {
        this.requiredAttributesFactory = requiredAttributesFactory;
    }
}
