package de.hybris.platform.processengine.interceptors;

import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.XMLProcessDefinitionsReader;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.io.StringReader;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.InputSource;

public class DynamicProcessDefinitionPrepareInterceptor implements PrepareInterceptor<DynamicProcessDefinitionModel>
{
    private XMLProcessDefinitionsReader xmlProcessDefinitionsReader;


    @Required
    public void setXmlProcessDefinitionsReader(XMLProcessDefinitionsReader xmlProcessDefinitionsReader)
    {
        this.xmlProcessDefinitionsReader = xmlProcessDefinitionsReader;
    }


    public void onPrepare(DynamicProcessDefinitionModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(model.getContent() == null)
        {
            return;
        }
        if(newProcessDefinitionIsBeingCreated(model, ctx))
        {
            setCodeFromContent(model);
        }
    }


    private boolean newProcessDefinitionIsBeingCreated(DynamicProcessDefinitionModel model, InterceptorContext ctx)
    {
        return (ctx.isNew(model) && (model.getActive() == null || model.getActive().booleanValue()));
    }


    private void setCodeFromContent(DynamicProcessDefinitionModel model)
    {
        try
        {
            ProcessDefinition definition = this.xmlProcessDefinitionsReader.from(new InputSource(new StringReader(model
                            .getContent())));
            model.setCode(definition.getName());
        }
        catch(Exception ex)
        {
            model.setCode(null);
        }
    }
}
