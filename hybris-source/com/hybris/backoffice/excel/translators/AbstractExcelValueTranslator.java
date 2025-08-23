package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractExcelValueTranslator<T> extends AbstractValidationAwareTranslator<T>
{
    private TypeService typeService;
    protected int order = 0;


    public Impex importData(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        ImpexValue singleValue = importValue(attributeDescriptor, importParameters);
        Impex impex = new Impex();
        ImpexForType impexForType = impex.findUpdates(importParameters.getTypeCode());
        if(singleValue != null)
        {
            impexForType.putValue(Integer.valueOf(0), singleValue.getHeaderValue(), singleValue.getValue());
        }
        return impex;
    }


    public abstract ImpexValue importValue(AttributeDescriptorModel paramAttributeDescriptorModel, ImportParameters paramImportParameters);


    protected boolean isLocalizedOfType(AttributeDescriptorModel attributeDescriptorModel, String typeCode)
    {
        return (attributeDescriptorModel.getAttributeType() instanceof MapTypeModel && this.typeService.isAssignableFrom(typeCode, ((MapTypeModel)attributeDescriptorModel
                        .getAttributeType()).getReturntype().getCode()));
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
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
}
