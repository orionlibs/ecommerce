package de.hybris.platform.persistence;

import de.hybris.platform.core.ItemImplCreationResult;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotFeature;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.RepositoryResult;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.model.TypeId;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PolyglotPersistenceGenericItemSupport
{
    static final SingleAttributeKey COMPOSED_TYPE_KEY = PolyglotPersistence.getNonlocalizedKey(Item.TYPE);
    static final SingleAttributeKey MODIFICATION_TIME_KEY = PolyglotPersistence.getNonlocalizedKey(Item.MODIFIED_TIME);
    static final SingleAttributeKey VERSION_KEY = PolyglotPersistence.getNonlocalizedKey(Item.HJMPTS);
    static final SingleAttributeKey CREATION_TIME_KEY = PolyglotPersistence.getNonlocalizedKey(Item.CREATION_TIME);
    public static final SingleAttributeKey OWNER = PolyglotPersistence.getNonlocalizedKey(Item.OWNER);


    public static boolean isFullyBackedByTheEJBPersistence(Tenant tenant, TypeInfo typeInfo)
    {
        return PolyglotPersistence.getRepository(typeInfo).isNotSupported();
    }


    public static boolean isBackedByTransactionalRepository(PK pk)
    {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(pk);
        RepositoryResult repositoryResult = PolyglotPersistence.getRepository(typeInfo);
        for(ItemStateRepository repository : repositoryResult.getRepositories())
        {
            ItemState itemState = repository.get(PolyglotPersistence.identityFromLong(pk.getLongValue()));
            if(itemState != null)
            {
                return repository.isSupported((PolyglotFeature)new TransactionalRepository());
            }
        }
        return false;
    }


    public static boolean isFullyBackedByThePolyglotPersistence(Tenant tenant, TypeInfo typeInfo)
    {
        return PolyglotPersistence.getRepository(typeInfo).isFullySupported();
    }


    public static GenericItem tryToCreateGenericItemUsingPolyglotPersistence(Tenant tenant, SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        if(mustBeCreatedByThePolyglotPersistence(tenant, allAttributes))
        {
            return createGenericItemUsingPolyglotPersistence(tenant, ctx, allAttributes);
        }
        return null;
    }


    public static ItemImplCreationResult tryToObtainGenericItemImplUsingPolyglotPersistence(Tenant tenant, PK pk)
    {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(pk);
        RepositoryResult repositories = PolyglotPersistence.getRepository(typeInfo);
        if(repositories.isNotSupported())
        {
            return null;
        }
        Identity id = PolyglotJaloConverter.toPolyglotLayer(pk);
        ItemState state = null;
        ItemStateRepository repo = null;
        for(ItemStateRepository repository : repositories.getRepositories())
        {
            ItemState result = repository.get(id);
            if(result != null)
            {
                state = result;
                repo = repository;
                break;
            }
        }
        if(state == null)
        {
            return repositories.isFullySupported() ? ItemImplCreationResult.missing(pk) : null;
        }
        GenericItemImplBackedByThePolyglotPersistence genericItemImplBackedByThePolyglotPersistence = new GenericItemImplBackedByThePolyglotPersistence(tenant, repo, id);
        return ItemImplCreationResult.existing((Item.ItemImpl)genericItemImplBackedByThePolyglotPersistence);
    }


    public static Set<Integer> getTypeCodesForPolyglotCriteria(Criteria criteria)
    {
        TypeId typeId = criteria.getTypeId();
        int typeCode = typeId.getTypeCode();
        Set<Integer> typeCodes = new HashSet<>();
        typeCodes.add(Integer.valueOf(typeCode));
        typeCodes.addAll((Collection<? extends Integer>)criteria.getSubtypeIds().stream().map(TypeId::getTypeCode).collect(Collectors.toSet()));
        return typeCodes;
    }


    static SingleAttributeKey getLocalizedPropertyKey(String qualifier, SessionContext ctx)
    {
        String langCode = ctx.getLanguage().getIsocode();
        return PolyglotPersistence.getLocalizedKey(qualifier, langCode);
    }


    static SingleAttributeKey getNonlocalizedPropertyKey(String qualifier)
    {
        return PolyglotPersistence.getNonlocalizedKey(qualifier);
    }


    private static GenericItem createGenericItemUsingPolyglotPersistence(Tenant tenant, SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(allAttributes);
        ItemStateRepository repo = PolyglotPersistence.getRepository(typeInfo).requireSingleRepositoryOnly();
        PK pk = allAttributes.getPK();
        ChangeSet creation = repo.beginCreation(PolyglotPersistence.identityFromLong(pk.getLongValue()));
        creation.set(COMPOSED_TYPE_KEY, PolyglotJaloConverter.toPolyglotLayer((Item)allAttributes.getComposedType()));
        EncryptedAttributesAccessor eaa = getEncryptedAttributesAccessor(tenant, allAttributes.getComposedType().getPK());
        allAttributes.forEach((qualifier, jaloValue) -> {
            SingleAttributeKey key = allAttributes.isLocalized(qualifier) ? getLocalizedPropertyKey(qualifier, ctx) : getNonlocalizedPropertyKey(qualifier);
            Object value = eaa.encryptIfNecessary(key, PolyglotJaloConverter.toPolyglotLayer(jaloValue));
            creation.set(key, value);
        });
        repo.store(creation);
        return (GenericItem)JaloSession.lookupItem(tenant, pk);
    }


    static EncryptedAttributesAccessor getEncryptedAttributesAccessor(Tenant tenant, PK typePK)
    {
        Objects.requireNonNull(tenant, "tenant mustn't be null.");
        Objects.requireNonNull(typePK, "typePK mustn't be null.");
        return new EncryptedAttributesAccessor(tenant.getPersistenceManager().getPersistenceInfo(typePK));
    }


    private static boolean mustBeCreatedByThePolyglotPersistence(Tenant tenant, Item.ItemAttributeMap allAttributes)
    {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(allAttributes);
        RepositoryResult repositoryResult = PolyglotPersistence.getRepository(typeInfo);
        return repositoryResult.isFullySupported();
    }
}
