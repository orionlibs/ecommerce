package de.hybris.platform.personalizationservices.action.property;

import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;

public class ActionTargetProvider<T extends CxAbstractActionModel> implements ActionPropertyProvider<T>
{
    private Class<T> supportedClass;
    private String target;


    public Class<T> supports()
    {
        return this.supportedClass;
    }


    public void provideValues(CxAbstractActionModel action)
    {
        action.setTarget(this.target);
    }


    protected Class<T> getSupportedClass()
    {
        return this.supportedClass;
    }


    public void setSupportedClass(Class<T> supportedClass)
    {
        this.supportedClass = supportedClass;
    }


    public void setTarget(String target)
    {
        this.target = target;
    }


    protected String getTarget()
    {
        return this.target;
    }
}
