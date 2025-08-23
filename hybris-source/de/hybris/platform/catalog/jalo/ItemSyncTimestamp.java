package de.hybris.platform.catalog.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;

public class ItemSyncTimestamp extends GeneratedItemSyncTimestamp
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("sourceItem") == null)
        {
            throw new JaloInvalidParameterException("missing source item", -1);
        }
        if(!allAttributes.containsKey("syncJob"))
        {
            if(allAttributes.get("sourceVersion") == null || allAttributes.get("targetVersion") == null)
            {
                throw new JaloInvalidParameterException("missing source or target verison (sync job was null)", -1);
            }
            allAttributes.put("syncJob", PK.NULL_PK);
        }
        else
        {
            if(!allAttributes.containsKey("sourceVersion"))
            {
                SyncItemJob syncItemJob = (SyncItemJob)allAttributes.get("syncJob");
                allAttributes.put("sourceVersion", syncItemJob.getSourceVersion());
            }
            if(!allAttributes.containsKey("targetVersion"))
            {
                SyncItemJob syncItemJob = (SyncItemJob)allAttributes.get("syncJob");
                allAttributes.put("targetVersion", syncItemJob.getTargetVersion());
            }
        }
        if(allAttributes.get("targetItem") == null)
        {
            throw new JaloInvalidParameterException("missing target item", -1);
        }
        allAttributes.setAttributeMode("sourceItem", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("syncJob", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("targetItem", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("lastSyncSourceModifiedTime", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("lastSyncTime", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("outdated", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("pendingAttributeQualifiers", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("pendingAttributesOwnerJob", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("pendingAttributesScheduledTurn", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("sourceVersion", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("targetVersion", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    protected void removeLinks()
    {
    }


    @ForceJALO(reason = "something else")
    protected void removePartOfItems(SessionContext ctx) throws ConsistencyCheckException
    {
    }


    @ForceJALO(reason = "something else")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx)
    {
    }


    @ForceJALO(reason = "abstract method implementation")
    public Boolean isOutdated(SessionContext ctx)
    {
        Item srcItem = getSourceItem(ctx);
        Date srcModifiedTS = (srcItem != null) ? srcItem.getModificationTime() : null;
        Date lastSyncModTS = getLastSyncSourceModifiedTime(ctx);
        return (srcItem == null || lastSyncModTS == null || srcModifiedTS == null || srcModifiedTS.after(lastSyncModTS)) ?
                        Boolean.TRUE : Boolean.FALSE;
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<AttributeDescriptor> getPendingAttributes(SessionContext ctx)
    {
        String qualifiers = getPendingAttributeQualifiers(ctx);
        if(qualifiers == null || qualifiers.length() == 0)
        {
            return Collections.EMPTY_SET;
        }
        ComposedType targetType = getTargetItem().getComposedType();
        Collection<AttributeDescriptor> ret = new LinkedHashSet<>();
        for(String q : qualifiers.split(","))
        {
            if(q != null && q.length() > 0)
            {
                try
                {
                    AttributeDescriptor attributeDescriptor = targetType.getAttributeDescriptorIncludingPrivate(q);
                    if(attributeDescriptor != null)
                    {
                        ret.add(attributeDescriptor);
                    }
                }
                catch(JaloItemNotFoundException jaloItemNotFoundException)
                {
                }
            }
        }
        return ret;
    }


    public PK getTargetItemPK()
    {
        return (PK)(new Object(this, "tgtItemPK"))
                        .get(null);
    }


    @ForceJALO(reason = "something else")
    public SyncItemJob getSyncJob(SessionContext ctx)
    {
        Object syncJob = getProperty(ctx, "syncJob");
        return PK.NULL_PK.equals(syncJob) ? null : (SyncItemJob)syncJob;
    }
}
