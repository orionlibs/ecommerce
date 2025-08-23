package de.hybris.platform.cache.relation;

import de.hybris.platform.cache.AdditionalInvalidationData;
import de.hybris.platform.cache.InvalidationKey;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RelationInvalidationListener implements InvalidationListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationInvalidationListener.class);
    private final Function<TypeId, Collection<RelationAttributes>> typeIdToRelationAttributes;


    RelationInvalidationListener(Function<TypeId, Collection<RelationAttributes>> typeIdToRelationAttributes)
    {
        this.typeIdToRelationAttributes = Objects.<Function<TypeId, Collection<RelationAttributes>>>requireNonNull(typeIdToRelationAttributes);
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        Optional<InvalidationKey.PkWithAdditionalData> possiblePkWithAdditionalData = InvalidationKey.extractPkWithAdditionalData(key);
        if(possiblePkWithAdditionalData.isEmpty())
        {
            LOGGER.warn("Received unexpected invalidation key: `{}`.", key);
            return;
        }
        InvalidationKey.PkWithAdditionalData pkWithAdditionalData = possiblePkWithAdditionalData.get();
        Optional<AdditionalInvalidationData> possibleAdditionalData = pkWithAdditionalData.getData();
        if(possibleAdditionalData.isEmpty() || !tryToInvalidateByForeignKeys(possibleAdditionalData.get(), target))
        {
            invalidateByTypeCode(pkWithAdditionalData.getTypeCode(), target);
        }
    }


    private boolean tryToInvalidateByForeignKeys(AdditionalInvalidationData data, InvalidationTarget target)
    {
        if(data.isEmpty())
        {
            return false;
        }
        Set<PK> typePks = data.getForeignKeyValues(Item.TYPE);
        if(typePks.isEmpty())
        {
            return false;
        }
        Objects.requireNonNull(data);
        invalidateByTypeIds(typePks.stream().map(TypeId::fromTypePk), target, data::getForeignKeyValues);
        return true;
    }


    private void invalidateByTypeCode(int typeCode, InvalidationTarget target)
    {
        invalidateByTypeIds(Stream.of(TypeId.fromTypeCode(typeCode)), target, fk -> Set.of());
    }


    private void invalidateByTypeIds(Stream<TypeId> typeIds, InvalidationTarget target, Function<String, Collection<PK>> fkValuesProvider)
    {
        typeIds.map(this::getCacheableAttributes)
                        .flatMap(Collection::stream)
                        .map(r -> r.getRelationInvalidations(fkValuesProvider))
                        .flatMap(Collection::stream)
                        .forEach(inv -> invalidate(inv, target));
    }


    private void invalidate(Object[] key, InvalidationTarget target)
    {
        target.invalidate(key, 1);
    }


    private Collection<RelationAttributes> getCacheableAttributes(TypeId typeId)
    {
        Collection<RelationAttributes> attributes = this.typeIdToRelationAttributes.apply(typeId);
        if(attributes == null)
        {
            return List.of();
        }
        return attributes;
    }
}
