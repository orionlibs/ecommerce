package de.hybris.bootstrap.loader.rule.internal;

import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingEvent;

public class ClassNotFoundRuleEvent implements IgnoreClassLoadingEvent
{
    private final ClassNotFoundException exception;
    private final String className;


    public ClassNotFoundRuleEvent(ClassNotFoundException exception, String className)
    {
        this.exception = exception;
        this.className = className;
    }


    public ClassNotFoundException getException()
    {
        return this.exception;
    }


    public String getClassName()
    {
        return this.className;
    }


    public String toString()
    {
        return "ClassNotFoundRuleEvent [exception=" + this.exception + ", className=" + this.className + "]";
    }
}
