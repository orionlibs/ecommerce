package de.hybris.y2ysync.model;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.y2ysync.impex.typesystem.ImpexHeaderBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class Y2YColumnDefinitionPrepareInterceptor implements PrepareInterceptor<Y2YColumnDefinitionModel>
{
    private ImpexHeaderBuilder impexHeaderBuilder;
    private CommonI18NService commonI18NService;


    public void onPrepare(Y2YColumnDefinitionModel model, InterceptorContext ctx) throws InterceptorException
    {
        AttributeDescriptorModel attributeDescriptor = model.getAttributeDescriptor();
        if(attributeDescriptor == null)
        {
            return;
        }
        if(isLocalized(attributeDescriptor) && model.getLanguage() == null)
        {
            model.setLanguage(this.commonI18NService.getCurrentLanguage());
        }
        if(StringUtils.isBlank(model.getColumnName()))
        {
            model.setColumnName(getDefaultColumnName(attributeDescriptor, model.getLanguage()));
        }
        if(StringUtils.isBlank(model.getImpexHeader()))
        {
            if(isLocalized(attributeDescriptor))
            {
                model.setImpexHeader(this.impexHeaderBuilder.getHeaderFor(attributeDescriptor, model.getLanguage().getIsocode()));
            }
            else
            {
                model.setImpexHeader(this.impexHeaderBuilder.getHeaderFor(attributeDescriptor));
            }
        }
    }


    private boolean isLocalized(AttributeDescriptorModel attributeDescriptor)
    {
        return attributeDescriptor.getLocalized().booleanValue();
    }


    private String getDefaultColumnName(AttributeDescriptorModel attributeDescriptor, LanguageModel language)
    {
        if(language == null)
        {
            return attributeDescriptor.getQualifier();
        }
        return attributeDescriptor.getQualifier() + "_" + attributeDescriptor.getQualifier();
    }


    @Required
    public void setImpexHeaderBuilder(ImpexHeaderBuilder impexHeaderBuilder)
    {
        this.impexHeaderBuilder = impexHeaderBuilder;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
