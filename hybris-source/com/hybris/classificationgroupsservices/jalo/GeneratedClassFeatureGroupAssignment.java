package com.hybris.classificationgroupsservices.jalo;

import com.hybris.classificationgroupsservices.constants.GeneratedClassificationgroupsservicesConstants;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedClassFeatureGroupAssignment extends GenericItem
{
    public static final String CLASSATTRIBUTEASSIGNMENT = "classAttributeAssignment";
    public static final String CLASSIFICATIONCLASS = "classificationClass";
    public static final String CLASSFEATUREGROUPPOS = "classFeatureGroupPOS";
    public static final String CLASSFEATUREGROUP = "classFeatureGroup";
    protected static final BidirectionalOneToManyHandler<GeneratedClassFeatureGroupAssignment> CLASSFEATUREGROUPHANDLER = new BidirectionalOneToManyHandler(GeneratedClassificationgroupsservicesConstants.TC.CLASSFEATUREGROUPASSIGNMENT, false, "classFeatureGroup", "classFeatureGroupPOS", true, true,
                    2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("classAttributeAssignment", Item.AttributeMode.INITIAL);
        tmp.put("classificationClass", Item.AttributeMode.INITIAL);
        tmp.put("classFeatureGroupPOS", Item.AttributeMode.INITIAL);
        tmp.put("classFeatureGroup", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ClassAttributeAssignment getClassAttributeAssignment(SessionContext ctx)
    {
        return (ClassAttributeAssignment)getProperty(ctx, "classAttributeAssignment");
    }


    public ClassAttributeAssignment getClassAttributeAssignment()
    {
        return getClassAttributeAssignment(getSession().getSessionContext());
    }


    public void setClassAttributeAssignment(SessionContext ctx, ClassAttributeAssignment value)
    {
        setProperty(ctx, "classAttributeAssignment", value);
    }


    public void setClassAttributeAssignment(ClassAttributeAssignment value)
    {
        setClassAttributeAssignment(getSession().getSessionContext(), value);
    }


    public ClassFeatureGroup getClassFeatureGroup(SessionContext ctx)
    {
        return (ClassFeatureGroup)getProperty(ctx, "classFeatureGroup");
    }


    public ClassFeatureGroup getClassFeatureGroup()
    {
        return getClassFeatureGroup(getSession().getSessionContext());
    }


    public void setClassFeatureGroup(SessionContext ctx, ClassFeatureGroup value)
    {
        CLASSFEATUREGROUPHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setClassFeatureGroup(ClassFeatureGroup value)
    {
        setClassFeatureGroup(getSession().getSessionContext(), value);
    }


    Integer getClassFeatureGroupPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "classFeatureGroupPOS");
    }


    Integer getClassFeatureGroupPOS()
    {
        return getClassFeatureGroupPOS(getSession().getSessionContext());
    }


    int getClassFeatureGroupPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getClassFeatureGroupPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getClassFeatureGroupPOSAsPrimitive()
    {
        return getClassFeatureGroupPOSAsPrimitive(getSession().getSessionContext());
    }


    void setClassFeatureGroupPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "classFeatureGroupPOS", value);
    }


    void setClassFeatureGroupPOS(Integer value)
    {
        setClassFeatureGroupPOS(getSession().getSessionContext(), value);
    }


    void setClassFeatureGroupPOS(SessionContext ctx, int value)
    {
        setClassFeatureGroupPOS(ctx, Integer.valueOf(value));
    }


    void setClassFeatureGroupPOS(int value)
    {
        setClassFeatureGroupPOS(getSession().getSessionContext(), value);
    }


    public ClassificationClass getClassificationClass(SessionContext ctx)
    {
        return (ClassificationClass)getProperty(ctx, "classificationClass");
    }


    public ClassificationClass getClassificationClass()
    {
        return getClassificationClass(getSession().getSessionContext());
    }


    public void setClassificationClass(SessionContext ctx, ClassificationClass value)
    {
        setProperty(ctx, "classificationClass", value);
    }


    public void setClassificationClass(ClassificationClass value)
    {
        setClassificationClass(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CLASSFEATUREGROUPHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }
}
