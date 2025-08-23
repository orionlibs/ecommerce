package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

public class ClassificationClass extends GeneratedClassificationClass
{
    private static final Logger LOG = Logger.getLogger(ClassificationClass.class.getName());
    private static final Map<String, NumberFormat> NUMBER_FORMAT_CACHE = Collections.synchronizedMap((Map<String, NumberFormat>)new LRUMap(50));
    public static final String CATALOGVERSION = CatalogConstants.Attributes.Category.CATALOGVERSION;


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute(CATALOGVERSION, allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        if(!(allAttributes.get(CATALOGVERSION) instanceof ClassificationSystemVersion))
        {
            throw new JaloInvalidParameterException("attribute '" + CATALOGVERSION + "' is no instance of ClassificationSystemVersion, can not create a new " + type
                            .getCode(), 0);
        }
        allAttributes.setAttributeMode(CATALOGVERSION, Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        for(Iterator<ClassAttributeAssignment> iter = getDeclaredClassificationAttributeAssignments(ctx).iterator(); iter.hasNext(); )
        {
            ((ClassAttributeAssignment)iter.next()).remove(ctx);
        }
        super.remove(ctx);
    }


    public ClassificationSystemVersion getSystemVersion()
    {
        return (ClassificationSystemVersion)CatalogManager.getInstance().getCatalogVersion((Category)this);
    }


    public void setSuperClass(ClassificationClass superClass)
    {
        setSupercategories(new Category[] {(superClass != null) ? (Category)superClass : null});
    }


    public Set<ClassificationClass> getSubClasses()
    {
        Set<ClassificationClass> ret = new LinkedHashSet(getSubcategories());
        for(Iterator<Category> iter = ret.iterator(); iter.hasNext(); )
        {
            Category cat = iter.next();
            if(!(cat instanceof ClassificationClass))
            {
                iter.remove();
            }
        }
        return ret;
    }


    public Set<ClassificationClass> getSuperClasses()
    {
        Set<ClassificationClass> ret = new LinkedHashSet<>();
        for(Category cat : getSupercategories())
        {
            if(cat instanceof ClassificationClass)
            {
                ret.add((ClassificationClass)cat);
            }
        }
        return ret;
    }


    public Collection<ClassificationClass> getAllSuperClasses()
    {
        Collection<ClassificationClass> ret = new LinkedHashSet<>();
        Set<ClassificationClass> current = getSuperClasses();
        do
        {
            Set<ClassificationClass> nextLevel = null;
            for(ClassificationClass cl : current)
            {
                if(ret.add(cl))
                {
                    Set<ClassificationClass> superClasses = cl.getSuperClasses();
                    if(!superClasses.isEmpty())
                    {
                        if(nextLevel == null)
                        {
                            nextLevel = new LinkedHashSet<>();
                        }
                        nextLevel.addAll(superClasses);
                    }
                }
            }
            current = nextLevel;
        }
        while(current != null && !current.isEmpty());
        return ret;
    }


    public ClassAttributeAssignment getAttributeAssignment(ClassificationAttribute attr) throws JaloItemNotFoundException
    {
        Collection<ClassificationClass> classes = Collections.singleton(this);
        StringBuilder stringBuilder = new StringBuilder();
        Map<Object, Object> values = new HashMap<>();
        stringBuilder.append("SELECT {")
                        .append(PK)
                        .append("},{")
                        .append("classificationClass")
                        .append("} ");
        stringBuilder.append("FROM {").append(GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT).append("} ");
        stringBuilder.append("WHERE {").append("classificationAttribute").append("}= ?attrPK ");
        stringBuilder.append("AND {").append("classificationClass").append("} ");
        values.put("attrPK", attr.getPK());
        String queryBase = stringBuilder.toString();
        List<ClassAttributeAssignment> rows = null;
        do
        {
            StringBuilder sb2 = new StringBuilder();
            if(classes.size() == 1)
            {
                sb2.append("= ?class ");
                values.put("class", ((ClassificationClass)classes.iterator().next()).getPK());
            }
            else
            {
                sb2.append(" IN ( ?classes ) ");
                values.put("classes", classes);
            }
            rows = FlexibleSearch.getInstance().search(queryBase + queryBase, values, Collections.singletonList(ClassAttributeAssignment.class), true, true, 0, -1).getResult();
            if(!rows.isEmpty())
            {
                continue;
            }
            Collection<ClassificationClass> tmp = null;
            for(ClassificationClass cl : classes)
            {
                Collection<ClassificationClass> superCl = cl.getSuperClasses();
                if(!superCl.isEmpty())
                {
                    if(tmp == null)
                    {
                        tmp = new HashSet<>();
                    }
                    tmp.addAll(superCl);
                }
            }
            classes = tmp;
        }
        while(rows.isEmpty() && classes != null && !classes.isEmpty());
        if(rows.isEmpty())
        {
            throw new JaloItemNotFoundException("attribute " + attr + " is not assigned to class " + getCode() + " or any of its super classes", 0);
        }
        return rows.get(0);
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassificationAttribute> getClassificationAttributes(SessionContext ctx)
    {
        List<ClassificationAttribute> ret = new ArrayList<>();
        for(ClassAttributeAssignment asgnmt : getClassificationAttributeAssignments(ctx))
        {
            ret.add(asgnmt.getClassificationAttribute(ctx));
        }
        return ret;
    }


    public ClassificationAttribute getClassificationAttribute(String code)
    {
        for(Iterator<ClassificationAttribute> iter = getClassificationAttributes().iterator(); iter.hasNext(); )
        {
            ClassificationAttribute attr = iter.next();
            if(code.equals(attr.getCode()))
            {
                return attr;
            }
        }
        return null;
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassificationAttribute> getInheritedClassificationAttributes(SessionContext ctx)
    {
        List<ClassificationAttribute> ret = new ArrayList<>();
        for(Iterator<ClassificationClass> iter = getAllSupercategories(ctx).iterator(); iter.hasNext(); )
        {
            ClassificationClass classificationClass = null;
            try
            {
                classificationClass = iter.next();
            }
            catch(ClassCastException e)
            {
                continue;
            }
            ret.addAll(classificationClass.getDeclaredClassificationAttributes(ctx));
        }
        return ret;
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassificationAttribute> getDeclaredClassificationAttributes(SessionContext ctx)
    {
        List<ClassificationAttribute> ret = new ArrayList<>();
        for(ClassAttributeAssignment asgn : getDeclaredClassificationAttributeAssignments(ctx))
        {
            ret.add(asgn.getClassificationAttribute());
        }
        return ret;
    }


    protected int getMaxPosition()
    {
        List<Integer> rows = FlexibleSearch.getInstance().search("SELECT MAX( {position} ) FROM {" + GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT + "} WHERE {classificationClass}= ?me ", Collections.singletonMap("me", getPK()), Collections.singletonList(Integer.class), true, true, 0, -1)
                        .getResult();
        return (rows.isEmpty() || rows.get(0) == null) ? -1 : ((Integer)rows.get(0)).intValue();
    }


    public ClassAttributeAssignment assignAttribute(ClassificationAttribute attribute)
    {
        return assignToMe(attribute, null, null, null, getMaxPosition() + 1);
    }


    public ClassAttributeAssignment assignAttribute(ClassificationAttribute attribute, int position)
    {
        return assignToMe(attribute, null, null, null, position);
    }


    public ClassAttributeAssignment assignAttribute(ClassificationAttribute attribute, String typeCode, ClassificationAttributeUnit unit, Collection values, int position) throws JaloInvalidParameterException, JaloItemNotFoundException
    {
        return assignToMe(attribute,
                        EnumerationManager.getInstance()
                                        .getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, typeCode), unit, values, position);
    }


    public ClassAttributeAssignment assignAttribute(ClassificationAttribute attribute, EnumerationValue type, ClassificationAttributeUnit unit, Collection values, int position)
    {
        return assignToMe(attribute, type, unit, values, position);
    }


    protected ClassAttributeAssignment assignToMe(ClassificationAttribute attribute, EnumerationValue type, ClassificationAttributeUnit unit, Collection values, int position)
    {
        if(!getSystemVersion().equals(attribute.getSystemVersion()))
        {
            throw new JaloInvalidParameterException("attribute " + attribute + " belongs to different system version, expected " +
                            getSystemVersion() + " but got " + attribute.getSystemVersion(), 0);
        }
        try
        {
            ClassAttributeAssignment rel = getAttributeAssignment(attribute);
            rel.setPosition(position);
            return rel;
        }
        catch(JaloItemNotFoundException e)
        {
            Map<Object, Object> attributes = new HashMap<>();
            attributes.put("classificationClass", this);
            attributes.put("classificationAttribute", attribute);
            attributes.put("position", Integer.valueOf(position));
            if(type != null)
            {
                attributes.put("attributeType", type);
            }
            if(unit != null)
            {
                attributes.put("unit", unit);
            }
            if(values != null && !values.isEmpty())
            {
                attributes.put("attributeValues", values);
            }
            return CatalogManager.getInstance().createClassAttributeAssignment(attributes);
        }
    }


    public ClassificationAttributeUnit getAttributeUnit(ClassificationAttribute attribute)
    {
        return getAttributeAssignment(attribute).getUnit();
    }


    public void setAttributeUnit(ClassificationAttribute attribute, ClassificationAttributeUnit unit)
    {
        getAttributeAssignment(attribute).setUnit(unit);
    }


    public Collection<ClassificationAttributeValue> getAttributeValues(ClassificationAttribute attribute)
    {
        return getAttributeValues(getAttributeAssignment(attribute));
    }


    public Collection<ClassificationAttributeValue> getAttributeValues(ClassAttributeAssignment asgnmt)
    {
        Collection<ClassificationAttributeValue> ret = asgnmt.getAttributeValues();
        if(ret == null || ret.isEmpty())
        {
            ret = asgnmt.getClassificationAttribute().getDefaultAttributeValues(null);
        }
        return ret;
    }


    public void setAttributeValues(ClassificationAttribute attribute, List<ClassificationAttributeValue> values)
    {
        getAttributeAssignment(attribute).setAttributeValues(values);
    }


    public Map getAttributeValueMap()
    {
        Map<Object, Object> ret = new HashMap<>();
        for(Iterator<ClassificationAttribute> iter = getClassificationAttributes().iterator(); iter.hasNext(); )
        {
            ClassificationAttribute atr = iter.next();
            ret.put(atr, getAttributeValues(atr));
        }
        return ret;
    }


    public void setAttributeValueMap(Map<?, ?> attributeValueMappings)
    {
        Map toSet = (attributeValueMappings != null) ? new HashMap<>(attributeValueMappings) : Collections.EMPTY_MAP;
        for(Iterator<ClassificationAttribute> iter = getClassificationAttributes().iterator(); iter.hasNext(); )
        {
            ClassificationAttribute atr = iter.next();
            Collection<? extends ClassificationAttributeValue> values = (Collection)toSet.get(atr);
            setAttributeValues(atr, (values != null) ? new ArrayList<>(values) : Collections.EMPTY_LIST);
        }
    }


    public EnumerationValue getVisibility(ClassificationAttribute attribute)
    {
        return getAttributeAssignment(attribute).getVisibility();
    }


    public void setVisibility(ClassificationAttribute attribute, EnumerationValue vsbl)
    {
        getAttributeAssignment(attribute).setVisibility(vsbl);
    }


    public EnumerationValue getAttributeType(ClassificationAttribute attribute)
    {
        return getAttributeAssignment(attribute).getAttributeType();
    }


    public void setAttributeType(ClassificationAttribute attribute, EnumerationValue valueType)
    {
        getAttributeAssignment(attribute).setAttributeType(valueType);
    }


    public boolean isMandatory(ClassificationAttribute attribute)
    {
        return getAttributeAssignment(attribute).isMandatoryAsPrimitive();
    }


    public void setMandatory(ClassificationAttribute attribute, boolean mandatory)
    {
        getAttributeAssignment(attribute).setMandatory(mandatory);
    }


    public boolean isLocalized(ClassificationAttribute atr)
    {
        return getAttributeAssignment(atr).isLocalizedAsPrimitive();
    }


    public void setLocalized(ClassificationAttribute atr, boolean loc)
    {
        getAttributeAssignment(atr).setLocalized(loc);
    }


    public boolean isRange(ClassificationAttribute atr)
    {
        return getAttributeAssignment(atr).isRangeAsPrimitive();
    }


    public void setRange(ClassificationAttribute atr, boolean range)
    {
        getAttributeAssignment(atr).setRange(range);
    }


    public boolean isMultiValued(ClassificationAttribute atr)
    {
        return getAttributeAssignment(atr).isMultiValuedAsPrimitive();
    }


    public void setMultiValued(ClassificationAttribute atr, boolean multi)
    {
        getAttributeAssignment(atr).setMultiValued(multi);
    }


    public Collection<ClassAttributeAssignment> getListableAttributeAssignments()
    {
        return getListableAttributeAssignments(getSession().getSessionContext());
    }


    public Collection<ClassAttributeAssignment> getListableAttributeAssignments(SessionContext ctx)
    {
        Collection<ClassAttributeAssignment> ret = new ArrayList<>();
        for(ClassAttributeAssignment assignment : getClassificationAttributeAssignments(ctx))
        {
            if(assignment.isListableAsPrimitive(ctx))
            {
                ret.add(assignment);
            }
        }
        return ret;
    }


    public Collection<ClassAttributeAssignment> getComparableAttributeAssignments()
    {
        return getComparableAttributeAssignments(getSession().getSessionContext());
    }


    public Collection<ClassAttributeAssignment> getComparableAttributeAssignments(SessionContext ctx)
    {
        Collection<ClassAttributeAssignment> ret = new ArrayList<>();
        for(ClassAttributeAssignment assignment : getClassificationAttributeAssignments(ctx))
        {
            if(assignment.isComparableAsPrimitive(ctx))
            {
                ret.add(assignment);
            }
        }
        return ret;
    }


    public Collection<ClassAttributeAssignment> getSearchableAttributeAssignments()
    {
        return getSearchableAttributeAssignments(getSession().getSessionContext());
    }


    public Collection<ClassAttributeAssignment> getSearchableAttributeAssignments(SessionContext ctx)
    {
        Collection<ClassAttributeAssignment> ret = new ArrayList<>();
        for(ClassAttributeAssignment assignment : getClassificationAttributeAssignments(ctx))
        {
            if(assignment.isSearchableAsPrimitive(ctx))
            {
                ret.add(assignment);
            }
        }
        return ret;
    }


    public Collection<ClassAttributeAssignment> getClassificationAttributeAssignments()
    {
        return getClassificationAttributeAssignments(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ClassAttributeAssignment> getClassificationAttributeAssignments(SessionContext ctx)
    {
        List<ClassAttributeAssignment> assignments = new ArrayList<>(getDeclaredClassificationAttributeAssignments(ctx));
        Set<ClassificationAttribute> attributes = new HashSet<>();
        for(ClassAttributeAssignment asgnmt : assignments)
        {
            attributes.add(asgnmt.getClassificationAttribute(ctx));
        }
        Set<ClassificationClass> superClasses = new LinkedHashSet<>(getSuperClasses());
        while(superClasses != null && !superClasses.isEmpty())
        {
            Set<ClassificationClass> nextLevel = null;
            Set<ClassAttributeAssignment> toAdd = null;
            for(ClassificationClass superClass : superClasses)
            {
                Collection<ClassAttributeAssignment> clAsgnmt = superClass.getDeclaredClassificationAttributeAssignments(ctx);
                if(!clAsgnmt.isEmpty())
                {
                    if(toAdd == null)
                    {
                        toAdd = new LinkedHashSet<>();
                    }
                    toAdd.addAll(clAsgnmt);
                }
                Collection<ClassificationClass> nextSuperClasses = superClass.getSuperClasses();
                if(!nextSuperClasses.isEmpty())
                {
                    if(nextLevel == null)
                    {
                        nextLevel = new LinkedHashSet<>();
                    }
                    nextLevel.addAll(nextSuperClasses);
                }
            }
            if(toAdd != null)
            {
                for(ClassAttributeAssignment asgnmt : toAdd)
                {
                    if(attributes.add(asgnmt.getClassificationAttribute(ctx)))
                    {
                        assignments.add(asgnmt);
                    }
                }
            }
            superClasses = nextLevel;
        }
        return assignments;
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassAttributeAssignment> getDeclaredClassificationAttributeAssignments(SessionContext ctx)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + ClassAttributeAssignment.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT + "} WHERE {classificationClass}= ?me ORDER BY {position} ASC, {" + Item.CREATION_TIME + "} ASC",
                                                        Collections.singletonMap("me", getPK()), Collections.singletonList(ClassAttributeAssignment.class), true, true, 0, -1)
                                        .getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setDeclaredClassificationAttributeAssignments(SessionContext ctx, List<ClassAttributeAssignment> assignments)
    {
        Collection<ClassAttributeAssignment> toRemove = new HashSet<>(getDeclaredClassificationAttributeAssignments(ctx));
        if(assignments != null)
        {
            toRemove.removeAll(assignments);
        }
        for(Iterator<ClassAttributeAssignment> iter = toRemove.iterator(); iter.hasNext(); )
        {
            try
            {
                ((ClassAttributeAssignment)iter.next()).remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
        if(CollectionUtils.isEmpty(assignments))
        {
            return;
        }
        int index = 0;
        for(Iterator<ClassAttributeAssignment> iterator1 = assignments.iterator(); iterator1.hasNext(); index++)
        {
            ClassAttributeAssignment rel = iterator1.next();
            if(!equals(rel.getClassificationClass(ctx)))
            {
                ClassAttributeAssignment copy = null;
                try
                {
                    copy = getAttributeAssignment(rel.getClassificationAttribute());
                }
                catch(JaloItemNotFoundException e)
                {
                    copy = assignToMe(rel.getClassificationAttribute(ctx), null, null, null, index);
                }
                copy.setUnit(rel.getUnit());
                copy.setLocalized(rel.isLocalizedAsPrimitive());
                copy.setMandatory(rel.isMandatoryAsPrimitive());
                copy.setRange(rel.isRangeAsPrimitive());
                copy.setMultiValued(rel.isMultiValuedAsPrimitive());
                copy.setAttributeType(rel.getAttributeType());
                copy.setVisibility(rel.getVisibility());
                copy.setAttributeValues(rel.getAttributeValues());
            }
            else
            {
                rel.setPosition(index);
            }
        }
    }


    protected Language getContextLanguage(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("missing session language", 0);
        }
        return ctx.getLanguage();
    }


    protected Collection translateValue(SessionContext ctx, ClassAttributeAssignment asgnmt, Object value)
    {
        Collection toTranslate = (value instanceof Collection) ? (Collection)value : Collections.<Object>singletonList(value);
        Collection<ClassificationAttributeValue> presetValues = getAttributeValues(asgnmt);
        if(presetValues == null || presetValues.isEmpty())
        {
            return toTranslate;
        }
        boolean localized = asgnmt.isLocalizedAsPrimitive();
        Collection<Object> ret = new LinkedList();
        for(Iterator iter = toTranslate.iterator(); iter.hasNext(); )
        {
            Object object = iter.next();
            if(object instanceof String)
            {
                ClassificationAttributeValue found = null;
                for(Iterator<ClassificationAttributeValue> iterator = presetValues.iterator(); iterator.hasNext(); )
                {
                    ClassificationAttributeValue attrVal = iterator.next();
                    if(object.equals(attrVal.getCode()) || (localized && object.equals(attrVal.getName(ctx))))
                    {
                        found = attrVal;
                        break;
                    }
                }
                ret.add((found != null) ? found : object);
                continue;
            }
            ret.add(object);
        }
        return ret;
    }


    public boolean isClassifying(Product product)
    {
        Collection<ClassificationClass> valid = CatalogManager.getInstance().getClassificationClasses(product);
        if(valid.contains(this))
        {
            return true;
        }
        for(ClassificationClass ccl : valid)
        {
            if(ccl.getAllSuperClasses().contains(this))
            {
                return true;
            }
        }
        return false;
    }


    protected NumberFormat getNumberFormat(SessionContext ctx, String formatDefinition)
    {
        if(formatDefinition == null)
        {
            return Utilities.getNumberInstance();
        }
        NumberFormat ret = null;
        ret = NUMBER_FORMAT_CACHE.get(formatDefinition);
        if(ret == null)
        {
            NUMBER_FORMAT_CACHE.put(formatDefinition, ret = Utilities.getDecimalFormat(formatDefinition));
        }
        return ret;
    }
}
