package de.hybris.bootstrap.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractValueObject
{
    private int startLineNumber = -1;
    private int endLineNumber = -1;
    private String xml = null;


    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append(getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1) + ":\n");
        output.append("-----------------------------------------\n");
        Object[] param = new Object[0];
        for(int i = 0; i < (getClass().getMethods()).length; i++)
        {
            Method method = getClass().getMethods()[i];
            String name = method.getName();
            if(name.startsWith("get") && !name.equalsIgnoreCase("getLogger") && !name.equalsIgnoreCase("getClass"))
            {
                name = name.substring(3);
                Object value = null;
                try
                {
                    value = method.invoke(this, param);
                }
                catch(InvocationTargetException invocationTargetException)
                {
                }
                catch(IllegalAccessException illegalAccessException)
                {
                }
                if(value != null)
                {
                    output.append("\t- " + name + ": " + value + "\n");
                }
            }
        }
        output.append("-----------------------------------------\n");
        return output.toString();
    }


    public int getEndLineNumber()
    {
        return this.endLineNumber;
    }


    public void setEndLineNumber(int endLineNumber)
    {
        this.endLineNumber = endLineNumber;
    }


    public int getStartLineNumber()
    {
        return this.startLineNumber;
    }


    public void setStartLineNumber(int startLineNumber)
    {
        this.startLineNumber = startLineNumber;
    }


    public String getXML()
    {
        return this.xml;
    }


    public void setXML(String xml)
    {
        this.xml = xml;
    }
}
