package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class ClassificationKeyword extends GeneratedClassificationKeyword
{
    private static final Logger log = Logger.getLogger(ClassificationKeyword.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("catalogVersion", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("catalogVersion", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
