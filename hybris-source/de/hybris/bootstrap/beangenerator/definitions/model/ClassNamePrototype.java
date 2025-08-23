package de.hybris.bootstrap.beangenerator.definitions.model;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ClassNamePrototype
{
    private static final String EMPTY_STRING = "".intern();
    private final String className;
    private final List<ClassNamePrototype> prototypes;


    public ClassNamePrototype(String className, List<ClassNamePrototype> prototypes)
    {
        Preconditions.checkNotNull(className);
        this.className = className;
        this.prototypes = prototypes;
    }


    public ClassNamePrototype(String className, ClassNamePrototype... prototypes)
    {
        this(className, Arrays.asList(prototypes));
    }


    public List<ClassNamePrototype> getPrototypes()
    {
        return Collections.unmodifiableList(this.prototypes);
    }


    public String getPrototypeAsString()
    {
        if(this.prototypes.isEmpty())
        {
            return EMPTY_STRING;
        }
        StringBuilder buf = new StringBuilder(100);
        if(!this.prototypes.isEmpty())
        {
            buf.append('<');
            for(Iterator<ClassNamePrototype> sub = this.prototypes.iterator(); sub.hasNext(); )
            {
                buf.append(sub.next());
                if(sub.hasNext())
                {
                    buf.append(',');
                }
            }
            buf.append('>');
        }
        return buf.toString();
    }


    public String getBaseClass()
    {
        return this.className;
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof ClassNamePrototype)
        {
            ClassNamePrototype other = (ClassNamePrototype)obj;
            if(!this.className.equals(other.getBaseClass()))
            {
                return false;
            }
            if(this.prototypes.size() != other.getPrototypes().size())
            {
                return false;
            }
            for(ClassNamePrototype prototype : this.prototypes)
            {
                if(!other.getPrototypes().contains(prototype))
                {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }


    public String toString()
    {
        StringBuilder buf = new StringBuilder(100);
        buf.append(this.className);
        buf.append(getPrototypeAsString());
        return buf.toString();
    }


    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this, new String[] {"className", "prototype"});
    }
}
