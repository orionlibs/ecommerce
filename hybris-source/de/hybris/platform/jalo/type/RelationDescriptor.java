package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.JaloInvalidParameterException;
import org.znerd.xmlenc.XMLOutputter;

public class RelationDescriptor extends AttributeDescriptor
{
    public static final String RELATION_TYPE = "relationType";
    public static final String IS_SOURCE = "isSource";
    public static final String RELATION_NAME = "relationName";
    public static final String ORDERED = "ordered";


    protected void checkSearchableChange(boolean newSearchable) throws JaloInvalidParameterException
    {
        if(newSearchable && getRelationType().isOneToMany() && !isProperty() && getPersistenceQualifier() == null)
        {
            throw new JaloInvalidParameterException("1-n relations are not searchable", 0);
        }
    }


    public boolean isSource()
    {
        return ((Boolean)getProperty("isSource")).booleanValue();
    }


    public boolean isOrdered()
    {
        return Boolean.TRUE.equals(getProperty("ordered"));
    }


    public String getRelationName()
    {
        return (String)getProperty("relationName");
    }


    public RelationType getRelationType()
    {
        return (RelationType)getProperty("relationType");
    }


    public String getXMLDefinition()
    {
        return exportXMLDefinition(null);
    }


    public String exportXMLDefinition(XMLOutputter xout)
    {
        return "";
    }
}
