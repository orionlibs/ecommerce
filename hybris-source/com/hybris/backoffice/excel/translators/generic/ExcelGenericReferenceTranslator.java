package com.hybris.backoffice.excel.translators.generic;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.AbstractValidationAwareTranslator;
import com.hybris.backoffice.excel.translators.generic.factory.ExportDataFactory;
import com.hybris.backoffice.excel.translators.generic.factory.ImportImpexFactory;
import com.hybris.backoffice.excel.translators.generic.factory.ReferenceFormatFactory;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.exceptions.ModelTypeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ExcelGenericReferenceTranslator extends AbstractValidationAwareTranslator
{
    private static final Logger LOG = LoggerFactory.getLogger(ExcelGenericReferenceTranslator.class);
    private int order = 2147483547;
    private RequiredAttributesFactory requiredAttributesFactory;
    private ReferenceFormatFactory referenceFormatFactory;
    private ExportDataFactory exportDataFactory;
    private ImportImpexFactory importImpexFactory;
    private TypeService typeService;
    private List<String> excludedFields;


    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        TypeModel attributeType = attributeDescriptor.getAttributeType();
        boolean isNotPartOf = BooleanUtils.isNotTrue(attributeDescriptor.getPartOf());
        boolean isRelation = attributeDescriptor instanceof de.hybris.platform.core.model.type.RelationDescriptorModel;
        boolean isCollection = attributeType instanceof de.hybris.platform.core.model.type.CollectionTypeModel;
        boolean isReference = (attributeType instanceof de.hybris.platform.core.model.type.ComposedTypeModel && !"ComposedType".equals(attributeType.getCode()));
        boolean isTypeAllowed = (isRelation || isCollection || isReference);
        return (isNotPartOf && isTypeAllowed && !isAttributeExcluded(attributeDescriptor) &&
                        hasAtLeastOneUniqueAttribute(attributeDescriptor));
    }


    private boolean hasAtLeastOneUniqueAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        RequiredAttribute rootUniqueAttribute = getRequiredAttributes(attributeDescriptor);
        return CollectionUtils.isNotEmpty(rootUniqueAttribute.getChildren());
    }


    private boolean isAttributeExcluded(AttributeDescriptorModel attributeDescriptor)
    {
        if(CollectionUtils.isEmpty(getExcludedFields()))
        {
            return false;
        }
        String enclosingType = attributeDescriptor.getEnclosingType().getCode();
        String expectedQualifier = attributeDescriptor.getQualifier();
        for(String excludedField : getExcludedFields())
        {
            String[] splitValue = excludedField.split("\\.");
            if(splitValue.length != 2)
            {
                LOG.warn("%s has incorrect format. Expected format is 'type.attribute'", excludedField);
                continue;
            }
            if(expectedQualifier.equals(splitValue[1]))
            {
                boolean assignableFrom = tryToCheckIfIsAssignableFrom(splitValue[0], enclosingType);
                if(assignableFrom)
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean tryToCheckIfIsAssignableFrom(String superModelTypeCode, String enclosingType)
    {
        try
        {
            return getTypeService().isAssignableFrom(superModelTypeCode, enclosingType);
        }
        catch(UnknownIdentifierException ex)
        {
            LOG.warn("Unknown type: {}", superModelTypeCode);
            return false;
        }
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        RequiredAttribute rootUniqueAttribute = getRequiredAttributes(attributeDescriptor);
        return getReferenceFormatFactory().create(rootUniqueAttribute);
    }


    public Optional<Object> exportData(Object objectToExport)
    {
        return Optional.empty();
    }


    public Optional<?> exportData(AttributeDescriptorModel attributeDescriptor, Object objectToExport)
    {
        RequiredAttribute rootUniqueAttribute = getRequiredAttributes(attributeDescriptor);
        try
        {
            return getExportDataFactory().create(rootUniqueAttribute, objectToExport);
        }
        catch(ModelTypeNotSupportedException e)
        {
            LOG.error(String.format("Cannot export given object %s", new Object[] {objectToExport}), (Throwable)e);
            return Optional.empty();
        }
    }


    public Impex importData(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        RequiredAttribute rootUniqueAttribute = getRequiredAttributes(attributeDescriptor);
        return getImportImpexFactory().create(rootUniqueAttribute, importParameters);
    }


    private RequiredAttribute getRequiredAttributes(AttributeDescriptorModel attributeDescriptorModel)
    {
        return getRequiredAttributesFactory().create(attributeDescriptorModel);
    }


    public RequiredAttributesFactory getRequiredAttributesFactory()
    {
        return this.requiredAttributesFactory;
    }


    @Required
    public void setRequiredAttributesFactory(RequiredAttributesFactory requiredAttributesFactory)
    {
        this.requiredAttributesFactory = requiredAttributesFactory;
    }


    public ReferenceFormatFactory getReferenceFormatFactory()
    {
        return this.referenceFormatFactory;
    }


    @Required
    public void setReferenceFormatFactory(ReferenceFormatFactory referenceFormatFactory)
    {
        this.referenceFormatFactory = referenceFormatFactory;
    }


    public ExportDataFactory getExportDataFactory()
    {
        return this.exportDataFactory;
    }


    @Required
    public void setExportDataFactory(ExportDataFactory exportDataFactory)
    {
        this.exportDataFactory = exportDataFactory;
    }


    public ImportImpexFactory getImportImpexFactory()
    {
        return this.importImpexFactory;
    }


    @Required
    public void setImportImpexFactory(ImportImpexFactory importImpexFactory)
    {
        this.importImpexFactory = importImpexFactory;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public List<String> getExcludedFields()
    {
        return this.excludedFields;
    }


    @Required
    public void setExcludedFields(List<String> excludedFields)
    {
        this.excludedFields = excludedFields;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public int getOrder()
    {
        return this.order;
    }
}
