package de.hybris.platform.directpersistence;

import java.lang.reflect.Method;

public class UnsafeMethodInfo implements Comparable<UnsafeMethodInfo>
{
    private final String attribute;
    private final Method method;
    private final boolean read;
    private final boolean markedAsKnowProblem;


    public UnsafeMethodInfo(String attribute, Method method, boolean read, boolean markedAsKnownProblem)
    {
        this.attribute = attribute;
        this.method = method;
        this.read = read;
        this.markedAsKnowProblem = markedAsKnownProblem;
    }


    public String getAttribute()
    {
        return this.attribute;
    }


    public Method getMethod()
    {
        return this.method;
    }


    public boolean isMarkedAsKnowProblem()
    {
        return this.markedAsKnowProblem;
    }


    public boolean isUnknownProblem()
    {
        return !this.markedAsKnowProblem;
    }


    public boolean isRead()
    {
        return this.read;
    }


    public int compareTo(UnsafeMethodInfo o)
    {
        if(o == null)
        {
            return 1;
        }
        if(isMarkedAsKnowProblem() && !o.isMarkedAsKnowProblem())
        {
            return -1;
        }
        if(!isMarkedAsKnowProblem() && o.isMarkedAsKnowProblem())
        {
            return 1;
        }
        return getMethod().getName().compareTo(o.getMethod().getName());
    }


    public int hashCode()
    {
        return this.attribute.hashCode() ^ this.method.hashCode();
    }


    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(super.equals(obj))
        {
            return true;
        }
        if(obj instanceof UnsafeMethodInfo)
        {
            UnsafeMethodInfo other = (UnsafeMethodInfo)obj;
            return (this.method.equals(other.method) && this.attribute.equals(other.attribute));
        }
        return false;
    }
}
