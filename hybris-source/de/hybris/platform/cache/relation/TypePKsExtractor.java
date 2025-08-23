package de.hybris.platform.cache.relation;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TypePKsExtractor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TypePKsExtractor.class);
    private final Set<Integer> excludedTypCodes;


    public TypePKsExtractor(Set<Integer> excludedTypCodes)
    {
        this.excludedTypCodes = Set.copyOf(excludedTypCodes);
    }


    public Collection<PK> extractTypePKs(TypeId typeId)
    {
        return typeId.getTypePk().map(Set::of)
                        .or(() -> typeId.getTypeName().map(this::getTypePKs))
                        .or(() -> typeId.getTypeCode().map(this::getTypePKs))
                        .orElseThrow();
    }


    private Set<PK> getTypePKs(String typeName)
    {
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType(typeName);
            return Set.of(type.getPK());
        }
        catch(JaloSystemException e)
        {
            logPKExtractionFailure(typeName, e);
            return Set.of();
        }
    }


    private Set<PK> getTypePKs(int typeCode)
    {
        if(this.excludedTypCodes.contains(Integer.valueOf(typeCode)))
        {
            LOGGER.debug("Ignoring excluded type code `{}`.", Integer.valueOf(typeCode));
            return Set.of();
        }
        try
        {
            HashSet<PK> pks = new HashSet<>();
            ComposedType root = TypeManager.getInstance().getRootComposedType(typeCode);
            pks.add(root.getPK());
            Objects.requireNonNull(pks);
            root.getAllSubTypes().stream().filter(ct -> (ct.getItemTypeCode() == typeCode)).map(Item::getPK).forEach(pks::add);
            return Set.copyOf(pks);
        }
        catch(JaloSystemException e)
        {
            logPKExtractionFailure(Integer.valueOf(typeCode), e);
            return Set.of();
        }
    }


    private void logPKExtractionFailure(Object source, JaloSystemException failure)
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Failed to obtain PKs for `{}`.", source, failure);
        }
        else
        {
            LOGGER.warn("Failed to obtain PKs for `{}`. {}", source, failure.getMessage());
        }
    }
}
