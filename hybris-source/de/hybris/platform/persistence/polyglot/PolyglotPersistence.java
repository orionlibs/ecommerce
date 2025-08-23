package de.hybris.platform.persistence.polyglot;

import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.polyglot.config.ItemStateRepositoryFactory;
import de.hybris.platform.persistence.polyglot.config.RepositoryConfig;
import de.hybris.platform.persistence.polyglot.config.RepositoryResult;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.PolyglotModelFactory;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SerializableValue;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.model.UnknownIdentity;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public final class PolyglotPersistence
{
    public static final String PROPERTY_ALLOW_CHANGING_ENITY_PARENT = "polyglot.allow.changing.entity.parent";


    public static RepositoryResult getRepository(TypeInfo typeInfo)
    {
        Objects.requireNonNull(typeInfo);
        return getFactory().getRepository(typeInfo);
    }


    public static boolean isKeyUsedInConfig(TypeInfo typeInfo, String qualifier)
    {
        return getFactory().isKeyUsedInConfig(typeInfo, qualifier);
    }


    private static ItemStateRepositoryFactory getFactory()
    {
        return (ItemStateRepositoryFactory)Registry.getCoreApplicationContext().getBean("itemStateRepositoryFactory", ItemStateRepositoryFactory.class);
    }


    public static Identity unknownIdentity()
    {
        return UnknownIdentity.instance();
    }


    public static SingleAttributeKey getNonlocalizedKey(String qualifier)
    {
        return PolyglotModelFactory.getNonlocalizedKey(qualifier);
    }


    public static <T extends Key> T as(Class<T> clazz, Key key)
    {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(clazz);
        return (T)Optional.<Key>ofNullable(key).filter(clazz::isInstance).map(clazz::cast).orElse(null);
    }


    public static Identity identityFromLong(long id)
    {
        return PolyglotModelFactory.identityFromLong(id);
    }


    public static Reference getReferenceTo(Identity id)
    {
        return PolyglotModelFactory.getReferenceTo(id);
    }


    public static SerializableValue getSerializableValue(Serializable serializableObject)
    {
        return PolyglotModelFactory.getSerializableValue(serializableObject);
    }


    public static SingleAttributeKey getLocalizedKey(String qualifier, String langCode)
    {
        return PolyglotModelFactory.getLocalizedKey(qualifier, langCode);
    }


    public static PolyglotFeature handlingRelatedItemsFeature(ItemState state, Key attributeKey, boolean write)
    {
        return PolyglotModelFactory.handlingRelatedItemsFeature(state, attributeKey, write);
    }


    public static PolyglotFeature handlingRelatedItemsFeature(ItemState state, Key attributeKey)
    {
        return handlingRelatedItemsFeature(state, attributeKey, false);
    }


    public static Optional<RepositoryConfig> getRepositoryConfig(ItemStateRepository repo)
    {
        return getFactory().getRepositoryConfig(Objects.<ItemStateRepository>requireNonNull(repo));
    }
}
