package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import com.hybris.backoffice.excel.validators.ExcelGenericReferenceValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class ExcelClassificationGenericReferenceValidator extends AbstractSingleClassificationFieldValidator
{
    private ExcelGenericReferenceValidator excelGenericReferenceValidator;
    private RequiredAttributesFactory requiredAttributesFactory;
    private Collection<String> blacklistedTypes;
    private TypeService typeService;


    public boolean canHandleSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (ClassificationAttributeTypeEnum.REFERENCE == excelAttribute.getAttributeAssignment().getAttributeType() && !isBlacklisted(excelAttribute));
    }


    protected boolean isBlacklisted(ExcelClassificationAttribute attribute)
    {
        for(String blacklistedType : this.blacklistedTypes)
        {
            if(this.typeService.isAssignableFrom(blacklistedType, attribute.getAttributeAssignment().getReferenceType().getCode()))
            {
                return true;
            }
        }
        return false;
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        ComposedTypeModel referenceType = excelAttribute.getAttributeAssignment().getReferenceType();
        RequiredAttribute requiredAttribute = this.requiredAttributesFactory.create(referenceType);
        return this.excelGenericReferenceValidator.validateRequiredAttribute(requiredAttribute, importParameters, context);
    }


    @Required
    public void setExcelGenericReferenceValidator(ExcelGenericReferenceValidator excelGenericReferenceValidator)
    {
        this.excelGenericReferenceValidator = excelGenericReferenceValidator;
    }


    @Required
    public void setRequiredAttributesFactory(RequiredAttributesFactory requiredAttributesFactory)
    {
        this.requiredAttributesFactory = requiredAttributesFactory;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setBlacklistedTypes(Collection<String> blacklistedTypes)
    {
        this.blacklistedTypes = blacklistedTypes;
    }
}
