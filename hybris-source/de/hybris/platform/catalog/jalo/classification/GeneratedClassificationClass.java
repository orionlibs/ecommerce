package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedClassificationClass extends Category
{
    public static final String EXTERNALID = "externalID";
    public static final String REVISION = "revision";
    public static final String SHOWEMPTYATTRIBUTES = "showEmptyAttributes";
    public static final String DECLAREDCLASSIFICATIONATTRIBUTES = "declaredClassificationAttributes";
    public static final String INHERITEDCLASSIFICATIONATTRIBUTES = "inheritedClassificationAttributes";
    public static final String CLASSIFICATIONATTRIBUTES = "classificationAttributes";
    public static final String DECLAREDCLASSIFICATIONATTRIBUTEASSIGNMENTS = "declaredClassificationAttributeAssignments";
    public static final String HMCXML = "hmcXML";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Category.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("externalID", Item.AttributeMode.INITIAL);
        tmp.put("revision", Item.AttributeMode.INITIAL);
        tmp.put("showEmptyAttributes", Item.AttributeMode.INITIAL);
        tmp.put("hmcXML", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<ClassificationAttribute> getClassificationAttributes()
    {
        return getClassificationAttributes(getSession().getSessionContext());
    }


    public List<ClassAttributeAssignment> getDeclaredClassificationAttributeAssignments()
    {
        return getDeclaredClassificationAttributeAssignments(getSession().getSessionContext());
    }


    public void setDeclaredClassificationAttributeAssignments(List<ClassAttributeAssignment> value)
    {
        setDeclaredClassificationAttributeAssignments(getSession().getSessionContext(), value);
    }


    public List<ClassificationAttribute> getDeclaredClassificationAttributes()
    {
        return getDeclaredClassificationAttributes(getSession().getSessionContext());
    }


    public String getExternalID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalID");
    }


    public String getExternalID()
    {
        return getExternalID(getSession().getSessionContext());
    }


    public void setExternalID(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalID", value);
    }


    public void setExternalID(String value)
    {
        setExternalID(getSession().getSessionContext(), value);
    }


    public String getHmcXML(SessionContext ctx)
    {
        return (String)getProperty(ctx, "hmcXML");
    }


    public String getHmcXML()
    {
        return getHmcXML(getSession().getSessionContext());
    }


    public void setHmcXML(SessionContext ctx, String value)
    {
        setProperty(ctx, "hmcXML", value);
    }


    public void setHmcXML(String value)
    {
        setHmcXML(getSession().getSessionContext(), value);
    }


    public List<ClassificationAttribute> getInheritedClassificationAttributes()
    {
        return getInheritedClassificationAttributes(getSession().getSessionContext());
    }


    public String getRevision(SessionContext ctx)
    {
        return (String)getProperty(ctx, "revision");
    }


    public String getRevision()
    {
        return getRevision(getSession().getSessionContext());
    }


    public void setRevision(SessionContext ctx, String value)
    {
        setProperty(ctx, "revision", value);
    }


    public void setRevision(String value)
    {
        setRevision(getSession().getSessionContext(), value);
    }


    public Boolean isShowEmptyAttributes(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "showEmptyAttributes");
    }


    public Boolean isShowEmptyAttributes()
    {
        return isShowEmptyAttributes(getSession().getSessionContext());
    }


    public boolean isShowEmptyAttributesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isShowEmptyAttributes(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isShowEmptyAttributesAsPrimitive()
    {
        return isShowEmptyAttributesAsPrimitive(getSession().getSessionContext());
    }


    public void setShowEmptyAttributes(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "showEmptyAttributes", value);
    }


    public void setShowEmptyAttributes(Boolean value)
    {
        setShowEmptyAttributes(getSession().getSessionContext(), value);
    }


    public void setShowEmptyAttributes(SessionContext ctx, boolean value)
    {
        setShowEmptyAttributes(ctx, Boolean.valueOf(value));
    }


    public void setShowEmptyAttributes(boolean value)
    {
        setShowEmptyAttributes(getSession().getSessionContext(), value);
    }


    public abstract List<ClassificationAttribute> getClassificationAttributes(SessionContext paramSessionContext);


    public abstract List<ClassAttributeAssignment> getDeclaredClassificationAttributeAssignments(SessionContext paramSessionContext);


    public abstract void setDeclaredClassificationAttributeAssignments(SessionContext paramSessionContext, List<ClassAttributeAssignment> paramList);


    public abstract List<ClassificationAttribute> getDeclaredClassificationAttributes(SessionContext paramSessionContext);


    public abstract List<ClassificationAttribute> getInheritedClassificationAttributes(SessionContext paramSessionContext);
}
