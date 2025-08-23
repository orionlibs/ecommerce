package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.data.model.BaseConfigObject;
import javax.management.ObjectName;

public abstract class BaseTomcatConfigObject extends BaseConfigObject
{
    private static final long serialVersionUID = -8581305771198372217L;
    private ObjectName objectName;


    public ObjectName getObjectName()
    {
        return this.objectName;
    }


    public void setObjectName(ObjectName objectName)
    {
        this.objectName = objectName;
    }


    public String getDisplayName()
    {
        return toString();
    }


    public String toString()
    {
        if(this.objectName == null)
        {
            return "";
        }
        return this.objectName.getCanonicalName();
    }


    public int hashCode()
    {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + ((this.objectName == null) ? 0 : this.objectName.hashCode());
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        BaseTomcatConfigObject other = (BaseTomcatConfigObject)obj;
        if(this.objectName == null)
        {
            if(other.objectName != null)
            {
                return false;
            }
        }
        else if(!this.objectName.equals(other.objectName))
        {
            return false;
        }
        return true;
    }


    protected Object getAttribute(String key)
    {
        return getAttribute("attribute", key);
    }


    protected void setAttribute(String key, Object value)
    {
        setAttribute("attribute", key, value);
    }
}
