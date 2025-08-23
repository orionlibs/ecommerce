package de.hybris.platform.jalo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

public class JaloSessionFactory implements FactoryBean<JaloSession>
{
    private static final Logger LOG = Logger.getLogger(JaloSessionFactory.class);
    private static Class<JaloSession> DEFAULT_CLASS = JaloSession.class;
    private Class<JaloSession> targetClass = DEFAULT_CLASS;


    public static JaloSession createWithSessionClass(Class<JaloSession> sessionClass) throws Exception
    {
        Class<JaloSession> clazz = DEFAULT_CLASS;
        if(sessionClass != null)
        {
            if(DEFAULT_CLASS.isAssignableFrom(sessionClass))
            {
                clazz = sessionClass;
            }
            else
            {
                LOG.error("Session class " + sessionClass + " is no instance of " + DEFAULT_CLASS.getName() + ", will use default class " + DEFAULT_CLASS
                                .getName() + ".");
            }
        }
        return clazz.newInstance();
    }


    public JaloSession getObject() throws Exception
    {
        return createInstance();
    }


    protected JaloSession createInstance() throws Exception
    {
        return getTargetClass().newInstance();
    }


    public Class getObjectType()
    {
        return this.targetClass;
    }


    public boolean isSingleton()
    {
        return false;
    }


    public void setTargetClassName(String className)
    {
        try
        {
            Class<?> newClass = Class.forName(className);
            if(DEFAULT_CLASS.isAssignableFrom(newClass))
            {
                this.targetClass = (Class)newClass;
            }
            else
            {
                LOG.error("Configured class " + className + " is no instance of " + DEFAULT_CLASS.getName() + ", will use default class " + DEFAULT_CLASS
                                .getName() + ".");
            }
        }
        catch(ClassNotFoundException e)
        {
            LOG.error("Can not find class " + className + ", will use default class " + DEFAULT_CLASS.getName() + ".");
        }
    }


    public Class<JaloSession> getTargetClass()
    {
        return this.targetClass;
    }
}
