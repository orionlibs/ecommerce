package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class SyncAttributeDescriptorConfig extends GeneratedSyncAttributeDescriptorConfig
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("syncJob", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("attributeDescriptor", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("includedInSync", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new " + type.getCode(), 0);
        }
        SyncItemJob job = (SyncItemJob)allAttributes.get("syncJob");
        AttributeDescriptor attributeDescriptor = (AttributeDescriptor)allAttributes.get("attributeDescriptor");
        if(attributeDescriptor.isInherited())
        {
            throw new JaloInvalidParameterException("attribute " + attributeDescriptor + " is inherited - use the declared one", 0);
        }
        if(job.configAlreadyExists(attributeDescriptor))
        {
            throw new ConsistencyCheckException(type.getCode() + " for job " + type.getCode() + " and attribute " + job + " already exists", 0);
        }
        allAttributes.setAttributeMode("syncJob", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("attributeDescriptor", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("copyByValue", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("includedInSync", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("untranslatable", Item.AttributeMode.INITIAL);
        allAttributes.put("copyByValue", adjustCopyByValue(ctx, attributeDescriptor, (Boolean)allAttributes.get("copyByValue")));
        allAttributes.put("untranslatable", Boolean.TRUE.equals(allAttributes.get("untranslatable")) ? Boolean.TRUE : Boolean.FALSE);
        return super.createItem(ctx, type, allAttributes);
    }


    protected Boolean adjustCopyByValue(SessionContext ctx, AttributeDescriptor attributeDescriptor, Boolean toSet)
    {
        return (attributeDescriptor.isPartOf() || Boolean.TRUE.equals(toSet)) ? Boolean.TRUE : Boolean.FALSE;
    }


    @ForceJALO(reason = "something else")
    public void setCopyByValue(SessionContext ctx, Boolean param)
    {
        super.setCopyByValue(ctx, adjustCopyByValue(ctx, getAttributeDescriptor(ctx), param));
    }


    @ForceJALO(reason = "something else")
    public Boolean isCopyByValue(SessionContext ctx)
    {
        Boolean ret = super.isCopyByValue(ctx);
        return (ret != null) ? ret : adjustCopyByValue(ctx, getAttributeDescriptor(ctx), null);
    }
}
