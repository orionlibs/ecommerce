package de.hybris.platform.spring;

import de.hybris.platform.core.Registry;
import groovy.servlet.GroovyServlet;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;

public class SpringAwareGroovyServlet extends GroovyServlet
{
    protected GroovyScriptEngine createGroovyScriptEngine()
    {
        return (GroovyScriptEngine)new SpringAwareGroovySriptEngine((ResourceConnector)this, Registry.getApplicationContext());
    }
}
