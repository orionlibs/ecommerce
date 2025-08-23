package com.hybris.classificationgroupsservices.jalo;

import com.hybris.classificationgroupsservices.constants.GeneratedClassificationgroupsservicesConstants;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedClassFeatureGroup extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String CLASSFEATUREGROUPASSIGNMENTS = "classFeatureGroupAssignments";
    public static final String CLASSIFICATIONCLASSPOS = "classificationClassPOS";
    public static final String CLASSIFICATIONCLASS = "classificationClass";
    protected static final OneToManyHandler<ClassFeatureGroupAssignment> CLASSFEATUREGROUPASSIGNMENTSHANDLER = new OneToManyHandler(GeneratedClassificationgroupsservicesConstants.TC.CLASSFEATUREGROUPASSIGNMENT, false, "classFeatureGroup", "classFeatureGroupPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedClassFeatureGroup> CLASSIFICATIONCLASSHANDLER = new BidirectionalOneToManyHandler(GeneratedClassificationgroupsservicesConstants.TC.CLASSFEATUREGROUP, false, "classificationClass", "classificationClassPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("classificationClassPOS", Item.AttributeMode.INITIAL);
        tmp.put("classificationClass", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<ClassFeatureGroupAssignment> getClassFeatureGroupAssignments(SessionContext ctx)
    {
        return (List<ClassFeatureGroupAssignment>)CLASSFEATUREGROUPASSIGNMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<ClassFeatureGroupAssignment> getClassFeatureGroupAssignments()
    {
        return getClassFeatureGroupAssignments(getSession().getSessionContext());
    }


    public void setClassFeatureGroupAssignments(SessionContext ctx, List<ClassFeatureGroupAssignment> value)
    {
        CLASSFEATUREGROUPASSIGNMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setClassFeatureGroupAssignments(List<ClassFeatureGroupAssignment> value)
    {
        setClassFeatureGroupAssignments(getSession().getSessionContext(), value);
    }


    public void addToClassFeatureGroupAssignments(SessionContext ctx, ClassFeatureGroupAssignment value)
    {
        CLASSFEATUREGROUPASSIGNMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToClassFeatureGroupAssignments(ClassFeatureGroupAssignment value)
    {
        addToClassFeatureGroupAssignments(getSession().getSessionContext(), value);
    }


    public void removeFromClassFeatureGroupAssignments(SessionContext ctx, ClassFeatureGroupAssignment value)
    {
        CLASSFEATUREGROUPASSIGNMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromClassFeatureGroupAssignments(ClassFeatureGroupAssignment value)
    {
        removeFromClassFeatureGroupAssignments(getSession().getSessionContext(), value);
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
        CLASSIFICATIONCLASSHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setClassificationClass(ClassificationClass value)
    {
        setClassificationClass(getSession().getSessionContext(), value);
    }


    Integer getClassificationClassPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "classificationClassPOS");
    }


    Integer getClassificationClassPOS()
    {
        return getClassificationClassPOS(getSession().getSessionContext());
    }


    int getClassificationClassPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getClassificationClassPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getClassificationClassPOSAsPrimitive()
    {
        return getClassificationClassPOSAsPrimitive(getSession().getSessionContext());
    }


    void setClassificationClassPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "classificationClassPOS", value);
    }


    void setClassificationClassPOS(Integer value)
    {
        setClassificationClassPOS(getSession().getSessionContext(), value);
    }


    void setClassificationClassPOS(SessionContext ctx, int value)
    {
        setClassificationClassPOS(ctx, Integer.valueOf(value));
    }


    void setClassificationClassPOS(int value)
    {
        setClassificationClassPOS(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CLASSIFICATIONCLASSHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedClassFeatureGroup.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedClassFeatureGroup.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }
}
