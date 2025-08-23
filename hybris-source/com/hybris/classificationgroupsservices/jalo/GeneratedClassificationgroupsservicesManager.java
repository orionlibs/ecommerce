package com.hybris.classificationgroupsservices.jalo;

import com.hybris.classificationgroupsservices.constants.GeneratedClassificationgroupsservicesConstants;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedClassificationgroupsservicesManager extends Extension
{
    protected static final OneToManyHandler<ClassFeatureGroup> CLASSIFICATIONCLASS2CLASSFEATUREGROUPRELATIONCLASSFEATUREGROUPSHANDLER = new OneToManyHandler(GeneratedClassificationgroupsservicesConstants.TC.CLASSFEATUREGROUP, true, "classificationClass", "classificationClassPOS", true, true, 2);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public List<ClassFeatureGroup> getClassFeatureGroups(SessionContext ctx, ClassificationClass item)
    {
        return (List<ClassFeatureGroup>)CLASSIFICATIONCLASS2CLASSFEATUREGROUPRELATIONCLASSFEATUREGROUPSHANDLER.getValues(ctx, (Item)item);
    }


    public List<ClassFeatureGroup> getClassFeatureGroups(ClassificationClass item)
    {
        return getClassFeatureGroups(getSession().getSessionContext(), item);
    }


    public void setClassFeatureGroups(SessionContext ctx, ClassificationClass item, List<ClassFeatureGroup> value)
    {
        CLASSIFICATIONCLASS2CLASSFEATUREGROUPRELATIONCLASSFEATUREGROUPSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setClassFeatureGroups(ClassificationClass item, List<ClassFeatureGroup> value)
    {
        setClassFeatureGroups(getSession().getSessionContext(), item, value);
    }


    public void addToClassFeatureGroups(SessionContext ctx, ClassificationClass item, ClassFeatureGroup value)
    {
        CLASSIFICATIONCLASS2CLASSFEATUREGROUPRELATIONCLASSFEATUREGROUPSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToClassFeatureGroups(ClassificationClass item, ClassFeatureGroup value)
    {
        addToClassFeatureGroups(getSession().getSessionContext(), item, value);
    }


    public void removeFromClassFeatureGroups(SessionContext ctx, ClassificationClass item, ClassFeatureGroup value)
    {
        CLASSIFICATIONCLASS2CLASSFEATUREGROUPRELATIONCLASSFEATUREGROUPSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromClassFeatureGroups(ClassificationClass item, ClassFeatureGroup value)
    {
        removeFromClassFeatureGroups(getSession().getSessionContext(), item, value);
    }


    public ClassFeatureGroup createClassFeatureGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedClassificationgroupsservicesConstants.TC.CLASSFEATUREGROUP);
            return (ClassFeatureGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassFeatureGroup : " + e.getMessage(), 0);
        }
    }


    public ClassFeatureGroup createClassFeatureGroup(Map attributeValues)
    {
        return createClassFeatureGroup(getSession().getSessionContext(), attributeValues);
    }


    public ClassFeatureGroupAssignment createClassFeatureGroupAssignment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedClassificationgroupsservicesConstants.TC.CLASSFEATUREGROUPASSIGNMENT);
            return (ClassFeatureGroupAssignment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassFeatureGroupAssignment : " + e.getMessage(), 0);
        }
    }


    public ClassFeatureGroupAssignment createClassFeatureGroupAssignment(Map attributeValues)
    {
        return createClassFeatureGroupAssignment(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "classificationgroupsservices";
    }
}
