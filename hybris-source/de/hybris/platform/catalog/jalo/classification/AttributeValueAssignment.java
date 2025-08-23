package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class AttributeValueAssignment extends GeneratedAttributeValueAssignment
{
    private static final Logger log = Logger.getLogger(AttributeValueAssignment.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("value", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new " + type.getCode(), 0);
        }
        ClassAttributeAssignment assignment = (ClassAttributeAssignment)allAttributes.get("attributeAssignment");
        ClassificationAttribute attr = (ClassificationAttribute)allAttributes.get("attribute");
        ClassificationSystemVersion ver = (ClassificationSystemVersion)allAttributes.get("systemVersion");
        if(assignment == null && attr == null)
        {
            throw new JaloInvalidParameterException("need at least one of attributeAssignment and attribute for creating a new " + type
                            .getCode(), 0);
        }
        if(assignment != null && attr != null && !attr.equals(assignment.getClassificationAttribute()))
        {
            throw new JaloInvalidParameterException("wrong attribute " + attr.getPK().toString() + " for assignment " + assignment
                            .getPK().toString(), 0);
        }
        if(ver != null && ((attr != null &&
                        !ver.equals(attr.getSystemVersion())) || (assignment != null && !ver.equals(assignment
                        .getSystemVersion()))))
        {
            throw new JaloInvalidParameterException("classification system version " + ver.getPK().toString() + " doesnt match attribute " + (
                            (attr != null) ?
                                            attr.getPK().toString() : "null") + " and/or assignment " + (
                            (assignment != null) ? assignment.getPK().toString() : "null"), 0);
        }
        if(attr == null || ver == null)
        {
            Item.ItemAttributeMap myMap = new Item.ItemAttributeMap((Map)allAttributes);
            if(attr == null)
            {
                myMap.put("attribute", assignment.getClassificationAttribute());
            }
            if(ver == null)
            {
                myMap.put("systemVersion", (assignment != null) ? assignment.getSystemVersion() : attr.getSystemVersion());
            }
            myMap.setAttributeMode("systemVersion", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("attributeAssignment", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("attribute", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("value", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("position", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
            return super.createItem(ctx, type, myMap);
        }
        allAttributes.setAttributeMode("systemVersion", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("attributeAssignment", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("attribute", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("value", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("position", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
