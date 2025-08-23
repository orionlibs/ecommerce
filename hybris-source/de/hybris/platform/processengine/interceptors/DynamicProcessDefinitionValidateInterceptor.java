package de.hybris.platform.processengine.interceptors;

import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.XMLProcessDefinitionsReader;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.io.StringReader;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.InputSource;

public class DynamicProcessDefinitionValidateInterceptor implements ValidateInterceptor<DynamicProcessDefinitionModel>
{
    private XMLProcessDefinitionsReader xmlProcessDefinitionsReader;


    @Required
    public void setXmlProcessDefinitionsReader(XMLProcessDefinitionsReader xmlProcessDefinitionsReader)
    {
        this.xmlProcessDefinitionsReader = xmlProcessDefinitionsReader;
    }


    public void onValidate(DynamicProcessDefinitionModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(canBeValidated(model))
        {
            validate(model);
        }
    }


    private boolean canBeValidated(DynamicProcessDefinitionModel model)
    {
        return (model.getContent() != null && model.getCode() != null);
    }


    private void validate(DynamicProcessDefinitionModel model) throws InterceptorException
    {
        ProcessDefinition definition;
        try
        {
            definition = this.xmlProcessDefinitionsReader.from(new InputSource(new StringReader(model.getContent())));
        }
        catch(Exception ex)
        {
            throw new InterceptorException("Given content is not a valid process definition.", ex, this);
        }
        if(definition.getName() == null || !definition.getName().equals(model.getCode()))
        {
            throw new InterceptorException("Process definition's name must be the same as the code.");
        }
    }
}
