package de.hybris.platform.servicelayer.session.impl;

import de.hybris.platform.servicelayer.session.Session;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSessionFactory implements FactoryBean
{
    private Class sessionClass;


    public Object getObject() throws Exception
    {
        try
        {
            return this.sessionClass.newInstance();
        }
        catch(InstantiationException e)
        {
            throw new IllegalArgumentException("cannot instantiate session class: ", e);
        }
        catch(IllegalAccessException e)
        {
            throw new IllegalArgumentException("cannot instantiate session class: ", e);
        }
    }


    public Class getObjectType()
    {
        return Session.class;
    }


    public boolean isSingleton()
    {
        return false;
    }


    @Required
    public void setSessionClass(String clazz)
    {
        try
        {
            this.sessionClass = Class.forName(clazz);
        }
        catch(ClassNotFoundException e)
        {
            throw new IllegalArgumentException("cannot instantiate session class '" + clazz + "' provided in session factory.", e);
        }
        if(!Session.class.isAssignableFrom(this.sessionClass))
        {
            throw new IllegalArgumentException("class '" + clazz + "' is not assignable from de.hybris.platform.servicelayer.session.Session");
        }
    }
}
