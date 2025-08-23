package de.hybris.platform.personalizationservices.process.strategies.impl;

import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.strategies.CxProcessParameterStrategy;
import de.hybris.platform.personalizationservices.process.strategies.CxProcessParameterType;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCxProcessParameterStrategy implements CxProcessParameterStrategy
{
    private ProcessParameterHelper processParameterHelper;
    private Set<CxProcessParameterType> supportedParameters = EnumSet.noneOf(CxProcessParameterType.class);


    public boolean supports(CxProcessParameterType parameterType)
    {
        return this.supportedParameters.contains(parameterType);
    }


    protected <T> void consumeProcessParameter(CxPersonalizationProcessModel process, String parameterName, Consumer<T> parmeterValueConsumer)
    {
        if(this.processParameterHelper.containsParameter((BusinessProcessModel)process, parameterName))
        {
            T value = (T)getProcessParameterHelper().getProcessParameterByName((BusinessProcessModel)process, parameterName).getValue();
            parmeterValueConsumer.accept(value);
        }
    }


    @Required
    public void setProcessParameterHelper(ProcessParameterHelper processParameterHelper)
    {
        this.processParameterHelper = processParameterHelper;
    }


    protected ProcessParameterHelper getProcessParameterHelper()
    {
        return this.processParameterHelper;
    }


    public void setSupportedParameters(Set<CxProcessParameterType> supportedParameters)
    {
        this.supportedParameters = EnumSet.copyOf(supportedParameters);
    }


    protected Set<CxProcessParameterType> getSupportedParameters()
    {
        return this.supportedParameters;
    }
}
