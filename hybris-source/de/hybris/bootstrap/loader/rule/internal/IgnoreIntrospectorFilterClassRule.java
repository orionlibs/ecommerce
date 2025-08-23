package de.hybris.bootstrap.loader.rule.internal;

import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingRule;

public class IgnoreIntrospectorFilterClassRule implements IgnoreClassLoadingRule
{
    private static final String CUSTOMIZER_SUFIX = "Customizer";
    private static final String BEANINFO_SUFFIX = "BeanInfo";


    public void initialize(String param)
    {
    }


    public IgnoreClassLoadingRule.IgnoredStatus isIgnored(String name)
    {
        if(checkIfIgnoreCustomizerClassesInIntrospectionMechanism(name))
        {
            return IgnoreClassLoadingRule.IgnoredStatus.IGNORED;
        }
        if(checkIfIgnoreBeanInfoClassesInIntrospectionMechanism(name))
        {
            return IgnoreClassLoadingRule.IgnoredStatus.IGNORED;
        }
        return IgnoreClassLoadingRule.IgnoredStatus.UNDEFINED;
    }


    boolean checkIfIgnoreBeanInfoClassesInIntrospectionMechanism(String className)
    {
        return (className.endsWith("BeanInfo") && checkIfClassIsInStackTrace("java.beans.Introspector", "getBeanInfo"));
    }


    boolean checkIfIgnoreCustomizerClassesInIntrospectionMechanism(String className)
    {
        return (className.endsWith("Customizer") && checkIfClassIsInStackTrace("java.beans.Introspector", "findCustomizerClass"));
    }


    public boolean checkIfClassIsInStackTrace(String classToVerify, String methodToFind)
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for(StackTraceElement element : stackTrace)
        {
            if(element.getClassName().equals(classToVerify) && element.getMethodName().equals(methodToFind))
            {
                return true;
            }
        }
        return false;
    }
}
