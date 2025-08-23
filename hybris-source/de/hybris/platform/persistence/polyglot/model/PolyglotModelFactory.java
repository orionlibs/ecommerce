package de.hybris.platform.persistence.polyglot.model;

import de.hybris.platform.persistence.polyglot.PolyglotFeature;
import java.io.Serializable;
import java.util.Objects;

public class PolyglotModelFactory
{
    public static SingleAttributeKey getNonlocalizedKey(String qualifier)
    {
        Objects.requireNonNull(qualifier);
        NonlocalizedKey wellKnown = WellKnownKey.fromQualifier(qualifier);
        return (wellKnown != null) ? (SingleAttributeKey)wellKnown : (SingleAttributeKey)new NonlocalizedKey(qualifier);
    }


    public static PolyglotFeature handlingRelatedItemsFeature(ItemState state, Key attributeKey, boolean write)
    {
        return HandlingRelatedItemsFeature.create((Reference)state.get((Key)WellKnownKey.TYPE_REF.key), attributeKey, write);
    }


    public static Identity identityFromLong(long id)
    {
        return (Identity)new LongIdentity(id);
    }


    public static Reference getReferenceTo(Identity id)
    {
        return new Reference(id);
    }


    public static SerializableValue getSerializableValue(Serializable serializableObject)
    {
        return new SerializableValue(serializableObject);
    }


    public static SingleAttributeKey getLocalizedKey(String qualifier, String langCode)
    {
        return (SingleAttributeKey)new LocalizedKey(qualifier, langCode);
    }


    public static SingleAttributeKey pk()
    {
        return (SingleAttributeKey)WellKnownKey.ITEM_PK.key;
    }


    public static SingleAttributeKey version()
    {
        return (SingleAttributeKey)WellKnownKey.VERSION.key;
    }


    public static SingleAttributeKey type()
    {
        return (SingleAttributeKey)WellKnownKey.TYPE_REF.key;
    }
}
