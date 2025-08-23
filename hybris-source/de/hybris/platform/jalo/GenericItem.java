package de.hybris.platform.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.GenericItemEJBImpl;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.RelationsInfo;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@ForceJALO(reason = "something else")
public class GenericItem extends LocalizableItem
{
    public GenericItemImpl getImplementation()
    {
        return (GenericItemImpl)super.getImplementation();
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.adjustTypeIfNeeded(type);
        JaloSession session = type.getSession();
        try
        {
            SessionContext ctxToUse;
            if(ctx != null)
            {
                ctxToUse = session.createLocalSessionContext(ctx);
            }
            else
            {
                ctxToUse = session.createLocalSessionContext();
                ctxToUse.setLanguage(null);
            }
            GenericItem genericItem = PolyglotPersistenceGenericItemSupport.tryToCreateGenericItemUsingPolyglotPersistence(getTenant(), ctxToUse, allAttributes);
            if(genericItem != null)
            {
                return (Item)genericItem;
            }
            JaloPropertyContainer props = getInitialProperties(session, allAttributes);
            Date creationTime = (Date)allAttributes.get(CREATION_TIME);
            Item owner = (Item)allAttributes.get(OWNER);
            if(creationTime != null || owner != null)
            {
                if(props == null)
                {
                    props = session.createPropertyContainer();
                }
                if(creationTime != null)
                {
                    props.setProperty(CREATION_TIME, creationTime);
                }
                if(owner != null)
                {
                    props.setProperty(OWNER, owner);
                }
            }
            allAttributes.checkConsistency();
            return (Item)GenericItemEJBImpl.createGenericItem(getTenant(), (PK)allAttributes.get(Item.PK), type, props);
        }
        finally
        {
            session.removeLocalSessionContext();
        }
    }


    protected JaloPropertyContainer getInitialProperties(JaloSession jaloSession, Item.ItemAttributeMap allAttributes)
    {
        JaloPropertyContainer props = jaloSession.createPropertyContainer();
        SessionContext ctx = null;
        Boolean singleLanguage = null;
        for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)allAttributes.getAllInitialValues().entrySet())
        {
            String qualifier = e.getKey();
            Object value = e.getValue();
            if(allAttributes.isLocalized(qualifier))
            {
                if(singleLanguage == null)
                {
                    ctx = jaloSession.getSessionContext();
                    singleLanguage = Boolean.valueOf((ctx != null && ctx.getLanguage() != null));
                }
                if(singleLanguage.booleanValue())
                {
                    props.setLocalizedProperty(ctx, qualifier, value);
                }
                else if(value != null)
                {
                    props.setAllLocalizedProperties(qualifier, (Map)value);
                }
            }
            else
            {
                props.setProperty(qualifier, value);
            }
            allAttributes.markInitialPropertyFetch();
        }
        return props;
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap map = super.getNonInitialAttributes(ctx, allAttributes);
        map.remove(CREATION_TIME);
        map.remove(OWNER);
        if(!map.isEmpty() && map.isInitialPropertyFetch())
        {
            for(String initialQ : allAttributes.getAllInitial())
            {
                map.remove(initialQ);
            }
        }
        return map;
    }


    public <T extends Item> Collection<T> getRelatedItems(String relationQualifier)
    {
        return this.isJaloOnly ? null : getImplementation().getRelatedItems(relationQualifier);
    }


    public <T extends Item> Collection<T> getRelatedItems(RelationsInfo relationsInfo)
    {
        return this.isJaloOnly ? null : getImplementation().getRelatedItems(relationsInfo);
    }


    public <T extends Item> boolean setRelatedItems(String relationQualifier, Collection<T> values)
    {
        return (!this.isJaloOnly && getImplementation().setRelatedItems(relationQualifier, values));
    }
}
