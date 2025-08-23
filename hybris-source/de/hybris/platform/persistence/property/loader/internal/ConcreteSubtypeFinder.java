package de.hybris.platform.persistence.property.loader.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.hybris.platform.persistence.property.loader.internal.dto.ComposedTypeDTO;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConcreteSubtypeFinder
{
    private final Map<Long, ComposedTypeDTO> pkToComposedTypeDTO;
    private final Multimap<Long, Long> typePkToChildrenPKs;


    private ConcreteSubtypeFinder(List<ComposedTypeDTO> composedTypes)
    {
        this.pkToComposedTypeDTO = buildPkToComposedTypeDTOMap(composedTypes);
        this.typePkToChildrenPKs = buildTypePkToChildrenPKsMap(composedTypes);
    }


    public static ConcreteSubtypeFinder buildForComposedTypes(List<ComposedTypeDTO> composedTypes)
    {
        return new ConcreteSubtypeFinder(composedTypes);
    }


    public boolean hasConcreteSubtypeForDeployment(Long typePK, int tc)
    {
        for(Long pk : this.typePkToChildrenPKs.get(typePK))
        {
            ComposedTypeDTO subType = this.pkToComposedTypeDTO.get(pk);
            if(sameDeployment(tc, subType) && notAbstractOrHasConcreteSubtype(tc, subType))
            {
                return true;
            }
        }
        return false;
    }


    private boolean sameDeployment(int tc, ComposedTypeDTO subType)
    {
        return (subType.getItemTypeCode() == tc);
    }


    private boolean notAbstractOrHasConcreteSubtype(int tc, ComposedTypeDTO subType)
    {
        return (!subType.isAbstractTypeCodeFlag() || hasConcreteSubtypeForDeployment(Long.valueOf(subType.getPk()), tc));
    }


    private Multimap<Long, Long> buildTypePkToChildrenPKsMap(List<ComposedTypeDTO> composedTypes)
    {
        ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
        for(ComposedTypeDTO composed : composedTypes)
        {
            if(composed.getSuperTypePk() != 0L)
            {
                arrayListMultimap.put(Long.valueOf(composed.getSuperTypePk()), Long.valueOf(composed.getPk()));
            }
        }
        return (Multimap<Long, Long>)arrayListMultimap;
    }


    private Map<Long, ComposedTypeDTO> buildPkToComposedTypeDTOMap(List<ComposedTypeDTO> composedTypes)
    {
        return (Map<Long, ComposedTypeDTO>)composedTypes.stream().collect(Collectors.toMap(ComposedTypeDTO::getPk, Function.identity()));
    }
}
