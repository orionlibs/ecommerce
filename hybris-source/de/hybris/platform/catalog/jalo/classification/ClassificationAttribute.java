package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ClassificationAttribute extends GeneratedClassificationAttribute
{
    private static final Logger LOG = Logger.getLogger(ClassificationAttribute.class.getName());
    public static final String TYPE_STRING = GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING;
    public static final String TYPE_BOOLEAN = GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN;
    public static final String TYPE_NUMBER = GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER;
    public static final String TYPE_ENUM = GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.ENUM;
    public static final String TYPE_DATE = GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.DATE;


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("code", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("systemVersion", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("systemVersion", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        for(ClassAttributeAssignment asgn : getClassAssignments())
        {
            asgn.remove(ctx);
        }
        super.remove(ctx);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getCode() + "(" + getCode() + ")";
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassificationAttributeValue> getDefaultAttributeValues(SessionContext ctx)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("attr", getPK());
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {value} FROM {" + GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT + "} WHERE {attributeAssignment} IS NULL AND {attribute}= ?attr ORDER BY {position} ASC, {" + Item.PK + "} ASC", params,
                                                        Collections.singletonList(ClassificationClass.class), true, true, 0, -1)
                                        .getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setDefaultAttributeValues(SessionContext ctx, List<ClassificationAttributeValue> _values)
    {
        List<List<?>> rows = FlexibleSearch.getInstance().search("SELECT {value}, {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT + "} WHERE {attributeAssignment} IS NULL AND {attribute}= ?me", Collections.singletonMap("me", this),
                        Arrays.asList((Class<?>[][])new Class[] {PK.class, AttributeValueAssignment.class}, ), true, true, 0, -1).getResult();
        Map<PK, List<AttributeValueAssignment>> valueAssignmentMap = new HashMap<>(rows.size());
        for(List<PK> row : rows)
        {
            PK valuePK = row.get(0);
            List<AttributeValueAssignment> assignments = valueAssignmentMap.get(valuePK);
            if(assignments == null)
            {
                valueAssignmentMap.put(valuePK, assignments = new ArrayList<>());
            }
            assignments.add((AttributeValueAssignment)row.get(1));
        }
        boolean changed = false;
        if(_values != null && !_values.isEmpty())
        {
            Set<ClassificationAttributeValue> values = new LinkedHashSet<>(_values);
            int index = 0;
            for(ClassificationAttributeValue v : values)
            {
                PK valuePK = v.getPK();
                List<AttributeValueAssignment> currentAssignments = valueAssignmentMap.get(valuePK);
                AttributeValueAssignment rel = (currentAssignments != null && !currentAssignments.isEmpty()) ? currentAssignments.remove(0) : null;
                if(rel != null)
                {
                    if(rel.getPositionAsPrimitive() != index)
                    {
                        rel.setPosition(index);
                        changed = true;
                    }
                }
                else
                {
                    assignToMe(v, index);
                    changed = true;
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
                    changed = true;
                }
                catch(ConsistencyCheckException e1)
                {
                    throw new JaloSystemException(e1);
                }
            }
        }
        if(changed)
        {
            setModificationTime(new Date());
        }
    }


    protected AttributeValueAssignment assignToMe(ClassificationAttributeValue classificationAttributeValue, int position)
    {
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put("attribute", this);
        attributes.put("value", classificationAttributeValue);
        attributes.put("position", Integer.valueOf(position));
        return CatalogManager.getInstance().createAttributeValueAssignment(attributes);
    }


    public Collection<ClassAttributeAssignment> getClassAssignments()
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("attr", getPK());
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT + "} WHERE {classificationAttribute}= ?attr ORDER BY {" + PK + "} ASC", params,
                                                        Collections.singletonList(ClassAttributeAssignment.class), true, true, 0, -1).getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<ClassificationClass> getClasses(SessionContext ctx)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("attr", getPK());
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {classificationClass} FROM {" + GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT + "} WHERE {classificationAttribute}= ?attr ORDER BY {" + Item.CREATION_TIME + "} ASC, {" + Item.PK + "} ASC", params,
                                                        Collections.singletonList(ClassificationClass.class), true, true, 0, -1)
                                        .getResult();
    }
}
