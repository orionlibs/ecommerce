package de.hybris.platform.persistence;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.polyglot.ChangeParentOfEntityFeature;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotFeature;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.PolyglotPersistenceException;
import de.hybris.platform.persistence.polyglot.config.RepositoryConfig;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.RelationsInfo;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GenericItemImplBackedByThePolyglotPersistence extends GenericItemImplAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericItemImplBackedByThePolyglotPersistence.class);
    private final transient ItemStateRepository repo;
    private final transient Identity id;
    private final transient Tenant tenant;
    private transient PolyglotPersistenceGenericItemSupport.EncryptedAttributesAccessor encryptedAttributesAccessor;


    public GenericItemImplBackedByThePolyglotPersistence(Tenant tenant, ItemStateRepository repo, Identity id)
    {
        this.tenant = Objects.<Tenant>requireNonNull(tenant);
        this.repo = Objects.<ItemStateRepository>requireNonNull(repo);
        this.id = Objects.<Identity>requireNonNull(id);
    }


    public boolean isLocalCachingSupported()
    {
        return false;
    }


    public PK getTypeKey()
    {
        return PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(getTypeRef().getIdentity());
    }


    public Item getOwner(SessionContext ctx)
    {
        return getUsingPolyglotPersistence(PolyglotPersistenceGenericItemSupport.OWNER);
    }


    public Class getJaloObjectClass()
    {
        return getComposedType().getJaloClass();
    }


    public PK getPK()
    {
        return PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(this.id);
    }


    public ComposedType getComposedType()
    {
        return (ComposedType)PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(this.tenant, getTypeRef());
    }


    public Object setProperty(SessionContext ctx, String qualifier, Object jaloValue)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getNonlocalizedPropertyKey(qualifier);
        return setUsingPolyglotPersistence(key, jaloValue);
    }


    public Object getProperty(SessionContext ctx, String qualifier)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getNonlocalizedPropertyKey(qualifier);
        return getUsingPolyglotPersistence(key);
    }


    public Object setLocalizedProperty(SessionContext ctx, String qualifier, Object jaloValue)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getLocalizedPropertyKey(qualifier, ctx);
        return setUsingPolyglotPersistence(key, jaloValue);
    }


    public Object getLocalizedProperty(SessionContext ctx, String qualifier)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getLocalizedPropertyKey(qualifier, ctx);
        return getUsingPolyglotPersistence(key);
    }


    public Object removeLocalizedProperty(SessionContext ctx, String name)
    {
        return setLocalizedProperty(ctx, name, null);
    }


    public Object removeProperty(SessionContext ctx, String name)
    {
        return setProperty(ctx, name, null);
    }


    public Date getModificationTime()
    {
        return getUsingPolyglotPersistence(PolyglotPersistenceGenericItemSupport.MODIFICATION_TIME_KEY);
    }


    public void setModificationTime(Date date)
    {
        setUsingPolyglotPersistence(PolyglotPersistenceGenericItemSupport.MODIFICATION_TIME_KEY, date);
    }


    public Date getCreationTime()
    {
        return getUsingPolyglotPersistence(PolyglotPersistenceGenericItemSupport.CREATION_TIME_KEY);
    }


    public void setCreationTime(Date date)
    {
        setUsingPolyglotPersistence(PolyglotPersistenceGenericItemSupport.CREATION_TIME_KEY, date);
    }


    public long getHJMPTS()
    {
        return ((Long)getUsingPolyglotPersistence(PolyglotPersistenceGenericItemSupport.VERSION_KEY)).longValue();
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        ItemState state = getCurrentState();
        this.repo.remove(state);
        PK pk = getPK();
        Object[] key = {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, PlatformStringUtils.valueOf(pk.getTypeCode()), pk};
        InvalidationTopic invalidationTopic = this.tenant.getInvalidationManager().getInvalidationTopic(key, 3);
        invalidationTopic.invalidateLocally(key, 2, null);
    }


    public void invalidateLocalCaches()
    {
    }


    public <T extends Item> Collection<T> getRelatedItems(String relationQualifier)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getNonlocalizedPropertyKey(relationQualifier);
        ItemState state = getCurrentState();
        if(!this.repo.isSupported(PolyglotPersistence.handlingRelatedItemsFeature(state, (Key)key)))
        {
            return null;
        }
        Object polyglotValue = state.get((Key)key);
        return (Collection<T>)PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(this.tenant, polyglotValue);
    }


    public <T extends Item> Collection<T> getRelatedItems(RelationsInfo relationsInfo)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getNonlocalizedPropertyKey(relationsInfo
                        .getRelationQualifier());
        ItemState state = getCurrentState();
        if(!this.repo.isSupported(PolyglotPersistence.handlingRelatedItemsFeature(state, (Key)key)))
        {
            return null;
        }
        Object polyglotValue = state.get((Key)key);
        if(Objects.isNull(polyglotValue))
        {
            polyglotValue = state.get(relationsInfo);
        }
        return (Collection<T>)PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(this.tenant, polyglotValue);
    }


    public <T extends Item> boolean setRelatedItems(String relationQualifier, Collection<T> jaloItems)
    {
        SingleAttributeKey key = PolyglotPersistenceGenericItemSupport.getNonlocalizedPropertyKey(relationQualifier);
        ItemState state = getCurrentState();
        if(!this.repo.isSupported(PolyglotPersistence.handlingRelatedItemsFeature(state, (Key)key, true)))
        {
            return false;
        }
        Object polyglotValue = PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toPolyglotLayer(jaloItems);
        verifyChangeAllowed(key, state, polyglotValue);
        ChangeSet changeSet = state.beginModification();
        changeSet.set(key, polyglotValue);
        this.repo.store(changeSet);
        return true;
    }


    protected void verifyChangeAllowed(SingleAttributeKey key, ItemState state, Object newValue)
    {
        if(hasValueChanged(key, state, newValue) && !isChangingAttributeAllowed(this.repo, key))
        {
            handleInvalidAttributeChange(key);
        }
    }


    private void handleInvalidAttributeChange(SingleAttributeKey key)
    {
        boolean shouldThrowException = !Config.getBoolean("polyglot.allow.changing.entity.parent", false);
        PolyglotPersistenceException ex = new PolyglotPersistenceException("Changing value of attribute " + key + " for item " + this.id + " by polyglot repo (" + this.repo.toString() + ") is not supported");
        if(shouldThrowException)
        {
            throw ex;
        }
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.warn(ex.getMessage(), (Throwable)ex);
        }
        else
        {
            LOGGER.warn("{}. A stacktrace can be seen if DEBUG log level is enabled", ex.getMessage());
        }
    }


    private boolean hasValueChanged(SingleAttributeKey key, ItemState state, Object newValue)
    {
        Object oldValue = state.get((Key)key);
        return (oldValue != null && !oldValue.equals(newValue));
    }


    private boolean isChangingAttributeAllowed(ItemStateRepository repo, SingleAttributeKey key)
    {
        Optional<RepositoryConfig> repoConfig = PolyglotPersistence.getRepositoryConfig(repo);
        if(repoConfig.isEmpty())
        {
            LOGGER.warn("could not find polyglot repository config for item {}", this.id);
            return false;
        }
        Identity type = getTypeRef().getIdentity();
        return (!isParentReferenceAttribute(key, repoConfig.get(), type) || repo
                        .isSupported((PolyglotFeature)new ChangeParentOfEntityFeature(type, key)));
    }


    private boolean isParentReferenceAttribute(SingleAttributeKey key, RepositoryConfig repositoryConfig, Identity type)
    {
        return repositoryConfig.getConditions().values().stream().flatMap(Collection::stream)
                        .filter(moreSpecificCondition -> moreSpecificCondition.getSourceIdentity().equals(type))
                        .anyMatch(moreSpecificCondition -> moreSpecificCondition.getQualifier().equalsIgnoreCase(key.getQualifier()));
    }


    private <T> T setUsingPolyglotPersistence(SingleAttributeKey key, Object newJaloValue)
    {
        ItemState state = getCurrentState();
        PolyglotPersistenceGenericItemSupport.EncryptedAttributesAccessor eaa = getEncryptedAttributesAccessor();
        Object oldJaloValue = eaa.decryptIfNecessary(key,
                        PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(this.tenant, state.get((Key)key)));
        Object newValue = eaa.encryptIfNecessary(key, PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toPolyglotLayer(newJaloValue));
        verifyChangeAllowed(key, state, newValue);
        ChangeSet changeSet = state.beginModification();
        changeSet.set(key, newValue);
        this.repo.store(changeSet);
        return (T)oldJaloValue;
    }


    private <T> T getUsingPolyglotPersistence(SingleAttributeKey key)
    {
        ItemState state = getCurrentState();
        Object value = state.get((Key)key);
        PolyglotPersistenceGenericItemSupport.EncryptedAttributesAccessor eaa = getEncryptedAttributesAccessor();
        return (T)eaa.decryptIfNecessary(key, PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(this.tenant, value));
    }


    private PolyglotPersistenceGenericItemSupport.EncryptedAttributesAccessor getEncryptedAttributesAccessor()
    {
        if(this.encryptedAttributesAccessor != null)
        {
            return this.encryptedAttributesAccessor;
        }
        this.encryptedAttributesAccessor = PolyglotPersistenceGenericItemSupport.getEncryptedAttributesAccessor(this.tenant, getTypeKey());
        return this.encryptedAttributesAccessor;
    }


    private Reference getTypeRef()
    {
        ItemState currentState = getCurrentState();
        return (Reference)currentState.get((Key)PolyglotPersistenceGenericItemSupport.COMPOSED_TYPE_KEY);
    }


    private ItemState getCurrentState()
    {
        ItemState state = this.repo.get(this.id);
        if(state == null)
        {
            PK pk = getPK();
            throw new JaloObjectNoLongerValidException(pk, null, "Item (" + pk + ") no longer exists.", 666);
        }
        return state;
    }
}
