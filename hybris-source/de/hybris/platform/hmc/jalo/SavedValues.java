package de.hybris.platform.hmc.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class SavedValues extends GeneratedSavedValues
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("modifiedItemType", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("timestamp", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("user", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("modificationType", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new  " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("modifiedItemType", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("modifiedItem", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("timestamp", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("timestamp", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("modificationType", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public static final int getMaxSize()
    {
        return (ConfigConstants.getInstance()).STORING_MODIFIEDVALUES_SIZE;
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getChangedAttributes(SessionContext ctx)
    {
        int index = 1;
        StringBuilder changedAttributes = new StringBuilder();
        for(SavedValueEntry entry : getSavedValuesEntries(ctx))
        {
            if(index > 1)
            {
                changedAttributes.append(",");
            }
            changedAttributes.append(entry.getModifiedAttribute());
            if(index >= 3)
            {
                changedAttributes.append(",...");
                break;
            }
            index++;
        }
        return changedAttributes.toString();
    }


    @ForceJALO(reason = "abstract method implementation")
    public Integer getNumberOfChangedAttributes(SessionContext ctx)
    {
        return Integer.valueOf(getSavedValuesEntries().size());
    }
}
