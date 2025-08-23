package de.hybris.platform.embeddedserver.tomcat;

import org.springframework.beans.factory.FactoryBean;

public class EmbeddedTomcatServerBuilderFactoryBean implements FactoryBean<EmbeddedTomcatServerBuilder>
{
    public static final String TOMCAT_CLASS_NAME = "org.apache.catalina.connector.Connector";
    public static final String TOMCAT_EMBEDDED_SERVER_BUILDER_CLASS_NAME = "de.hybris.platform.embeddedserver.tomcat.EmbeddedTomcatServerBuilderImpl";
    private static final EmbeddedTomcatServerBuilder NULL_BUILDER = (EmbeddedTomcatServerBuilder)new NullEmbeddedTomcatServerBuilder();


    public EmbeddedTomcatServerBuilder getObject() throws Exception
    {
        if(checkIfClassExists("org.apache.catalina.connector.Connector"))
        {
            Class<?> etb = Class.forName("de.hybris.platform.embeddedserver.tomcat.EmbeddedTomcatServerBuilderImpl");
            return (EmbeddedTomcatServerBuilder)etb.newInstance();
        }
        return NULL_BUILDER;
    }


    public Class<?> getObjectType()
    {
        return EmbeddedTomcatServerBuilder.class;
    }


    public boolean isSingleton()
    {
        return false;
    }


    public boolean checkIfClassExists(String className)
    {
        try
        {
            Class.forName(className);
            return true;
        }
        catch(ClassNotFoundException | NoClassDefFoundError e)
        {
            return false;
        }
    }
}
