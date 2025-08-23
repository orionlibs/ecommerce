package de.hybris.platform.persistence.links;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

public class DefaultNonNavigableRelationsDAO implements NonNavigableRelationsDAO
{
    private final AtomicReference<Multimap<String, RelationType>> typeCodesToNonNavigableRelations = new AtomicReference<>();


    @PostConstruct
    public void init()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new NonNavigableRelationsInvalidationListener(this, this.typeCodesToNonNavigableRelations));
    }


    public Collection<String> getNonNavigableRelationCodesForType(String typeCode)
    {
        return (Collection<String>)getNonNavigableRelationsForTypeCode(typeCode).stream().map(i -> i.getCode()).collect(Collectors.toList());
    }


    public Collection<RelationType> getNonNavigableRelationsForTypeCode(String typeCode)
    {
        Multimap<String, RelationType> typeCodeToRelations = this.typeCodesToNonNavigableRelations.get();
        if(typeCodeToRelations == null)
        {
            typeCodeToRelations = getNonNavigableRelations();
            this.typeCodesToNonNavigableRelations.compareAndSet(null, typeCodeToRelations);
        }
        return typeCodeToRelations.get(typeCode);
    }


    private Multimap<String, RelationType> getNonNavigableRelations()
    {
        RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        try
        {
            ComposedType relationType = TypeManager.getInstance().getComposedType(RelationType.class);
            SessionContext ctx = new SessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            String composedTypeQuery =
                            "SELECT {" + RelationType.PK + "} FROM {" + relationType.getCode() + "} WHERE {" + GeneratedCoreConstants.Attributes.RelationMetaType.SOURCENAVIGABLE + "} = ?navigable or {" + GeneratedCoreConstants.Attributes.RelationMetaType.TARGETNAVIGABLE + "} = ?navigable";
            List<RelationType> nonNavigableRelations = FlexibleSearch.getInstance().search(ctx, composedTypeQuery, (Map)ImmutableMap.of("navigable", Boolean.valueOf(false)), Collections.singletonList(RelationType.class), true, true, 0, -1).getResult();
            Multimap<String, RelationType> multimap = asTypeCodeToNonNavigableRelationMap(nonNavigableRelations);
            if(theUpdate != null)
            {
                theUpdate.close();
            }
            return multimap;
        }
        catch(Throwable throwable)
        {
            if(theUpdate != null)
            {
                try
                {
                    theUpdate.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    private Multimap<String, RelationType> asTypeCodeToNonNavigableRelationMap(List<RelationType> nonNavigableRelations)
    {
        ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
        for(RelationType nonNavigableRelation : nonNavigableRelations)
        {
            if(!nonNavigableRelation.isSourceNavigable())
            {
                arrayListMultimap.put(nonNavigableRelation.getSourceType().getCode(), nonNavigableRelation);
            }
            if(!nonNavigableRelation.isTargetNavigable())
            {
                arrayListMultimap.put(nonNavigableRelation.getTargetType().getCode(), nonNavigableRelation);
            }
        }
        return (Multimap<String, RelationType>)arrayListMultimap;
    }
}
