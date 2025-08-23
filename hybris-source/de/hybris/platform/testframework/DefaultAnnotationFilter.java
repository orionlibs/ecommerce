package de.hybris.platform.testframework;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.bootstrap.annotations.UnitTest;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

public class DefaultAnnotationFilter extends Filter
{
    private static final Logger LOG = Logger.getLogger(DefaultAnnotationFilter.class);
    private static Class[] annotations = new Class[] {PerformanceTest.class, DemoTest.class, IntegrationTest.class, ManualTest.class, UnitTest.class};
    private final Collection<Class> included;
    private final Collection<Class> excluded;
    private final boolean defaultIncluded;
    private final Filter existingFilter;


    public DefaultAnnotationFilter(Filter existingFilter)
    {
        this.existingFilter = existingFilter;
        String property = System.getProperty("hybris.junit4.annotations.defaultIncluded");
        this
                        .defaultIncluded = (property == null || (property.charAt(0) != '0' && !property.toLowerCase(Locale.getDefault()).startsWith("false")));
        this.included = parseString(System.getProperty("hybris.junit4.annotations.included"));
        this.excluded = parseString(System.getProperty("hybris.junit4.annotations.excluded"));
    }


    public DefaultAnnotationFilter(String includedList, String excludedList, boolean defaultAllowed)
    {
        this.existingFilter = null;
        this.defaultIncluded = defaultAllowed;
        this.included = parseString(includedList);
        this.excluded = parseString(excludedList);
    }


    private boolean isDefaultIncluded()
    {
        return this.defaultIncluded;
    }


    public Collection<Class> getExcludedAnnotations()
    {
        return this.excluded;
    }


    public Collection<Class> getIncludedAnnotations()
    {
        return this.included;
    }


    private static Collection<Class> parseString(String listStr)
    {
        Collection<Class<?>> list = new ArrayList<>();
        if(listStr != null)
        {
            String[] classes = listStr.split(",");
            for(String classStr : classes)
            {
                for(Class<?> annotation : annotations)
                {
                    if(annotation.getSimpleName().equals(classStr))
                    {
                        list.add(annotation);
                    }
                }
            }
        }
        return list;
    }


    private boolean filter(Annotation[] annotations, boolean defaultIncluded)
    {
        for(Class clazz : getExcludedAnnotations())
        {
            for(Annotation annotation : annotations)
            {
                if(annotation.annotationType().equals(clazz))
                {
                    return false;
                }
            }
        }
        boolean allow = defaultIncluded;
        if(!allow)
        {
            for(Class clazz : getIncludedAnnotations())
            {
                for(Annotation annotation : annotations)
                {
                    if(annotation.annotationType().equals(clazz))
                    {
                        allow = true;
                        break;
                    }
                }
            }
        }
        return allow;
    }


    private boolean filter(Collection<Annotation> annotations, boolean defaultIncluded)
    {
        return filter(annotations.<Annotation>toArray(new Annotation[0]), defaultIncluded);
    }


    public boolean shouldRun(Description description)
    {
        if(this.existingFilter != null && !this.existingFilter.shouldRun(description))
        {
            return false;
        }
        if(description != null)
        {
            return filter(description.getAnnotations(), isDefaultIncluded());
        }
        return isDefaultIncluded();
    }


    public String describe()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DefaultAnnotationFilter(included=");
        stringBuilder.append(getIncludedAnnotations());
        stringBuilder.append(", excluded=");
        stringBuilder.append(getExcludedAnnotations());
        stringBuilder.append(", defaultIncluded=");
        stringBuilder.append(isDefaultIncluded());
        if(this.existingFilter != null)
        {
            stringBuilder.append(", delagateFilter=").append(this.existingFilter.describe());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
