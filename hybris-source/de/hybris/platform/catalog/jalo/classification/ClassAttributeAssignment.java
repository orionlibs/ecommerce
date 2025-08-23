package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.Localization;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ClassAttributeAssignment extends GeneratedClassAttributeAssignment
{
    private static final Logger LOG = Logger.getLogger(ClassAttributeAssignment.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("classificationClass", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("classificationAttribute", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        ClassificationClass classificationClass = (ClassificationClass)allAttributes.get("classificationClass");
        ClassificationAttribute atr = (ClassificationAttribute)allAttributes.get("classificationAttribute");
        ClassificationSystemVersion ver = (ClassificationSystemVersion)allAttributes.get("systemVersion");
        if(!classificationClass.getSystemVersion().equals(atr.getSystemVersion()))
        {
            throw new JaloInvalidParameterException("class " + classificationClass.getCode() + " and attribute " + atr.getCode() + " belong to different systems:" + classificationClass
                            .getSystemVersion()
                            .getFullVersionName() + " <> " + atr
                            .getSystemVersion().getFullVersionName(), 0);
        }
        if(ver != null && !ver.equals(classificationClass.getSystemVersion()))
        {
            throw new JaloInvalidParameterException("Different system versions specified for assignment  and class " + classificationClass
                            .getCode() + " :" + ver.getFullVersionName() + " <> " + classificationClass
                            .getSystemVersion().getFullVersionName(), 0);
        }
        checkCreationConstrains(classificationClass, atr);
        if(ver == null)
        {
            Item.ItemAttributeMap myMap = new Item.ItemAttributeMap((Map)allAttributes);
            myMap.put("systemVersion", classificationClass.getSystemVersion());
            myMap.setAttributeMode("systemVersion", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("classificationAttribute", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("classificationClass", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("classificationClass", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("position", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
            return super.createItem(ctx, type, myMap);
        }
        allAttributes.setAttributeMode("systemVersion", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("classificationAttribute", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("classificationClass", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("classificationClass", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("position", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        for(Iterator<AttributeValueAssignment> iter = getAllValueAssignments().iterator(); iter.hasNext(); )
        {
            ((AttributeValueAssignment)iter.next()).remove(ctx);
        }
        JaloSession jSession = getSession();
        for(PK featPK : getFeaturesValuePKs(ctx))
        {
            ((ProductFeature)jSession.getItem(featPK)).untype();
        }
        super.remove(ctx);
    }


    protected void checkCreationConstrains(ClassificationClass classificationClass, ClassificationAttribute attr) throws ConsistencyCheckException
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("cl", classificationClass);
        params.put("attr", attr);
        int count = ((Integer)FlexibleSearch.getInstance().search("SELECT COUNT({" + Item.PK + "}) FROM {" + GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT + "} WHERE {classificationClass}=?cl AND {classificationAttribute}=?attr", params, Integer.class).getResult().get(0)).intValue();
        if(count > 0)
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("exception.classification.alreadyassigned", new Object[] {classificationClass
                            .getCode(), attr.getCode()}), 0);
        }
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getClassificationClass().getCode() + "->" + getClassificationClass().getCode() + "(" + getClassificationAttribute().getCode() + ")";
    }


    protected Collection<ProductFeature> getFeaturesValues(int start, int count)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.PRODUCTFEATURE + "} WHERE {classificationAttributeAssignment}= ?me ORDER BY {" + Item.PK + "} ASC ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(ProductFeature.class), true, true, start, count).getResult();
    }


    private Collection<PK> getFeaturesValuePKs(SessionContext ctx)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.PRODUCTFEATURE + "} WHERE {classificationAttributeAssignment}=?me ORDER BY {" + Item.PK + "} ASC ",
                                                        Collections.singletonMap("me", this), Collections.singletonList(PK.class), true, true, 0, -1).getResult();
    }


    @ForceJALO(reason = "something else")
    public void setUnit(SessionContext ctx, ClassificationAttributeUnit classificationAttributeUnit)
    {
        if(classificationAttributeUnit != null &&
                        !getClassificationClass().getSystemVersion().equals(classificationAttributeUnit.getSystemVersion()))
        {
            throw new JaloInvalidParameterException("classification unit belongs to different system: expected " +
                            getClassificationClass().getSystemVersion()
                                            .getFullVersionName() + " but got " + classificationAttributeUnit
                            .getSystemVersion().getFullVersionName(), 0);
        }
        super.setUnit(ctx, classificationAttributeUnit);
    }


    protected Collection<AttributeValueAssignment> getAllValueAssignments()
    {
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT + "} WHERE {attributeAssignment}= ?me ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(AttributeValueAssignment.class), true, true, 0, -1).getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassificationAttributeValue> getAttributeValues(SessionContext ctx)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {value} FROM {" + GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT + "} WHERE {attributeAssignment}= ?me ORDER BY {position} ASC, {" + Item.CREATION_TIME + "} ASC",
                                                        Collections.singletonMap("me", this), Collections.singletonList(ClassificationAttributeValue.class), true, true, 0, -1)
                                        .getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setAttributeValues(SessionContext ctx, List<ClassificationAttributeValue> values)
    {
        List<List> rows = FlexibleSearch.getInstance()
                        .search("SELECT {value}, {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT + "} WHERE {attributeAssignment}= ?me ", Collections.singletonMap("me", this), Arrays.asList((Class<?>[][])new Class[] {PK.class, AttributeValueAssignment.class}, ),
                                        true, true, 0, -1).getResult();
        Map<PK, List<AttributeValueAssignment>> valueAssignmentMap = new HashMap<>();
        for(List<PK> row : rows)
        {
            PK valuePK = row.get(0);
            AttributeValueAssignment asgnmt = (AttributeValueAssignment)row.get(1);
            List<AttributeValueAssignment> assignments = valueAssignmentMap.get(valuePK);
            if(assignments == null)
            {
                valueAssignmentMap.put(valuePK, assignments = new ArrayList<>(3));
            }
            assignments.add(asgnmt);
        }
        if(values != null && !values.isEmpty())
        {
            int index = 0;
            for(ClassificationAttributeValue v : new LinkedHashSet(values))
            {
                PK valuePK = v.getPK();
                List<AttributeValueAssignment> attributeValueAssignmentList = valueAssignmentMap.get(valuePK);
                AttributeValueAssignment rel = null;
                if(attributeValueAssignmentList != null && !attributeValueAssignmentList.isEmpty())
                {
                    rel = attributeValueAssignmentList.get(0);
                    attributeValueAssignmentList.remove(0);
                }
                if(rel != null)
                {
                    if(rel.getPositionAsPrimitive() != index)
                    {
                        rel.setPosition(index);
                    }
                }
                else
                {
                    assignToMe(v, index);
                }
                index++;
            }
        }
        for(Map.Entry<PK, List<AttributeValueAssignment>> e : valueAssignmentMap.entrySet())
        {
            for(AttributeValueAssignment rel : e.getValue())
            {
                try
                {
                    rel.remove();
                }
                catch(ConsistencyCheckException e1)
                {
                    throw new JaloSystemException(e1);
                }
            }
        }
    }


    protected int getMaxValuePosition()
    {
        return (
                        (Integer)FlexibleSearch.getInstance()
                                        .search("SELECT MAX( {position}) FROM {" + GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT + "} WHERE {attributeAssignment} = ?me ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    public void assignValue(ClassificationAttributeValue classificationAttributeValue)
    {
        assignToMe(classificationAttributeValue, getMaxValuePosition() + 1);
    }


    public void assignValue(ClassificationAttributeValue classificationAttributeValue, int position)
    {
        assignToMe(classificationAttributeValue, position);
    }


    protected AttributeValueAssignment assignToMe(ClassificationAttributeValue classificationAttributeValue, int position)
    {
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put("attributeAssignment", this);
        attributes.put("value", classificationAttributeValue);
        attributes.put("position", Integer.valueOf(position));
        return CatalogManager.getInstance().createAttributeValueAssignment(attributes);
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getAttributeValueDisplayString(SessionContext ctx)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<ClassificationAttributeValue> iter = getClassificationClass().getAttributeValues(getClassificationAttribute()).iterator();
        while(iter.hasNext())
        {
            ClassificationAttributeValue val = iter.next();
            String name = val.getName(ctx);
            stringBuilder.append((name != null) ? name : val.getCode());
            if(iter.hasNext())
            {
                stringBuilder.append(" ; ");
            }
        }
        return stringBuilder.toString();
    }


    @ForceJALO(reason = "abstract method implementation")
    public Map<Language, String> getAllAttributeValueDisplayString(SessionContext ctx)
    {
        Map<Object, Object> ret = new HashMap<>();
        SessionContext myCtx = new SessionContext(ctx);
        for(Iterator<Language> iter = C2LManager.getInstance().getAllLanguages().iterator(); iter.hasNext(); )
        {
            Language language = iter.next();
            myCtx.setLanguage(language);
            ret.put(language, getAttributeValueDisplayString(myCtx));
        }
        return (Map)ret;
    }


    @ForceJALO(reason = "something else")
    public void setFormatDefinition(SessionContext ctx, String format)
    {
        if(format != null)
        {
            EnumerationValue aType = getAttributeType();
            if(aType != null && GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER.equals(aType.getCode()))
            {
                try
                {
                    Locale locale = (ctx.getLanguage() != null) ? ctx.getLanguage().getLocale() : JaloSession.getCurrentSession().getSessionContext().getLocale();
                    Utilities.getDecimalFormat(format, locale);
                }
                catch(IllegalArgumentException e)
                {
                    throw new JaloInvalidParameterException("invalid format decimal format '" + format + "'", 0);
                }
            }
            else if(aType != null && GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.DATE.equals(aType.getCode()))
            {
                try
                {
                    Locale locale = (ctx.getLanguage() != null) ? ctx.getLanguage().getLocale() : JaloSession.getCurrentSession().getSessionContext().getLocale();
                    Utilities.getSimpleDateFormat(format, locale);
                }
                catch(IllegalArgumentException e)
                {
                    throw new JaloInvalidParameterException("invalid format decimal format '" + format + "'", 0);
                }
            }
        }
        super.setFormatDefinition(ctx, format);
    }


    public NumberFormat getNumberFormat(SessionContext ctx)
    {
        Locale locale = (ctx.getLanguage() != null) ? ctx.getLanguage().getLocale() : JaloSession.getCurrentSession().getSessionContext().getLocale();
        String format = getFormatDefinition(ctx);
        if(format != null)
        {
            try
            {
                return Utilities.getDecimalFormat(format, locale);
            }
            catch(IllegalArgumentException e)
            {
                throw new JaloInvalidParameterException("invalid decimal format '" + format + "'", 0);
            }
        }
        return Utilities.getNumberInstance(locale);
    }


    public DateFormat getDateFormat(SessionContext ctx)
    {
        Locale locale = (ctx.getLanguage() != null) ? ctx.getLanguage().getLocale() : JaloSession.getCurrentSession().getSessionContext().getLocale();
        String format = getFormatDefinition(ctx);
        if(format != null)
        {
            try
            {
                return Utilities.getSimpleDateFormat(format, locale);
            }
            catch(IllegalArgumentException e)
            {
                throw new JaloInvalidParameterException("invalid date format '" + format + "'", 0);
            }
        }
        return Utilities.getDateTimeInstance(2, 2, locale);
    }


    @ForceJALO(reason = "something else")
    public void setClassificationClass(ClassificationClass value)
    {
        boolean exist = false;
        try
        {
            for(ClassificationAttribute ca : value.getClassificationAttributes())
            {
                if(getClassificationAttribute().equals(ca))
                {
                    exist = true;
                    break;
                }
            }
        }
        catch(JaloInvalidParameterException e)
        {
            e.printStackTrace();
        }
        if(!exist)
        {
            super.setClassificationClass(value);
        }
        else
        {
            throw new JaloInvalidParameterException("attribute already exists!", 0);
        }
    }
}
