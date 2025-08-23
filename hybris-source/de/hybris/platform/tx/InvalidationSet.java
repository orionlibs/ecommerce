package de.hybris.platform.tx;

import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.cache.relation.RelationCacheKey;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

@NotThreadSafe
public class InvalidationSet
{
    private static final Logger LOG = Logger.getLogger(InvalidationSet.class.getName());
    public static InvalidationSet EMPTY_SET = (InvalidationSet)new Object(null);


    public static Invalidation createInvalidation(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        return new Invalidation(key, invalidationType, invalidationTopicDepth);
    }


    public static void invalidateGlobally(Invalidation invalidation)
    {
        invalidateGlobally(Collections.singletonList(invalidation));
    }


    public static void invalidateGlobally(Collection<Invalidation> invalidations)
    {
        if(CollectionUtils.isNotEmpty(invalidations))
        {
            InvalidationManagerProcessor processor = new InvalidationManagerProcessor();
            for(Invalidation inv : invalidations)
            {
                processor.invalidateGlobally(inv);
            }
        }
    }


    public static void invalidateLocally(Collection<Invalidation> invalidations)
    {
        if(CollectionUtils.isNotEmpty(invalidations))
        {
            InvalidationManagerProcessor processor = new InvalidationManagerProcessor();
            InvalidationTarget realTarget = processor.im.getInvalidationTarget();
            for(Invalidation inv : invalidations)
            {
                processor.invalidateLocally(inv, realTarget);
            }
        }
    }


    private Map<Object, Invalidation> _recordedInvalidations = null;


    private Map<Object, Invalidation> recordedInvalidations(boolean forUpdate)
    {
        if(this._recordedInvalidations == null)
        {
            if(forUpdate)
            {
                this._recordedInvalidations = new LinkedHashMap<>();
            }
            else
            {
                return Collections.emptyMap();
            }
        }
        return this._recordedInvalidations;
    }


    Collection<Invalidation> recordedInvalidations()
    {
        return recordedInvalidations(false).values();
    }


    private Map<Object, Invalidation> _recordedRollbackInvalidations = null;
    private Map<Object, Object> _effectiveInvalidations;


    private Map<Object, Invalidation> recordedRollbackInvalidations(boolean forUpdate)
    {
        if(this._recordedRollbackInvalidations == null)
        {
            if(forUpdate)
            {
                this._recordedRollbackInvalidations = new LinkedHashMap<>();
            }
            else
            {
                return Collections.emptyMap();
            }
        }
        return this._recordedRollbackInvalidations;
    }


    Collection<Invalidation> recordedRollbackInvalidations()
    {
        return recordedRollbackInvalidations(false).values();
    }


    private final Map<String, RelationInvalidationsContainer> invalidationsForRelations = new HashMap<>();


    Map<Object, Object> effectiveInvalidations(boolean forUpdate)
    {
        if(this._effectiveInvalidations == null)
        {
            if(forUpdate)
            {
                this._effectiveInvalidations = new HashMap<>();
            }
            else
            {
                return Collections.EMPTY_MAP;
            }
        }
        return this._effectiveInvalidations;
    }


    private final InvalidationTarget fakeInvalidationTarget = (InvalidationTarget)new Object(this);
    private final InvalidationProcessor processor;


    public InvalidationSet()
    {
        this((InvalidationProcessor)new InvalidationManagerProcessor());
    }


    public InvalidationSet(InvalidationProcessor processor)
    {
        this.processor = processor;
    }


    int recordedInvalidationsSize()
    {
        return recordedInvalidations().size();
    }


    int effectiveInvalidationsSize()
    {
        return getTreeSize(effectiveInvalidations(false));
    }


    private int getTreeSize(Map<Object, Object> nodes)
    {
        int ret = nodes.size();
        for(Map.Entry<Object, Object> e : nodes.entrySet())
        {
            Object value = e.getValue();
            if(END_NODE != value)
            {
                ret += getTreeSize((Map<Object, Object>)value);
            }
        }
        return ret;
    }


    public void executeDelayedInvalidationsGlobally()
    {
        for(Invalidation invalidation : recordedInvalidations())
        {
            this.processor.invalidateGlobally(invalidation);
        }
    }


    protected InvalidationTarget getRealInvalidationTarget()
    {
        return InvalidationManager.getInstance().getInvalidationTarget();
    }


    public void executeDelayedInvalidationsLocally()
    {
        executeDelayedInvalidationsLocally(recordedInvalidations());
    }


    public void executeDelayedRollbackInvalidationsLocally()
    {
        executeDelayedInvalidationsLocally(recordedRollbackInvalidations());
    }


    private void executeDelayedInvalidationsLocally(Collection<Invalidation> invalidations)
    {
        if(!invalidations.isEmpty())
        {
            InvalidationTarget realTarget = getRealInvalidationTarget();
            for(Invalidation invalidation : invalidations)
            {
                this.processor.invalidateLocally(invalidation, realTarget);
            }
        }
    }


    public Invalidation delayInvalidation(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        Invalidation inv = addInvalidation(key, invalidationTopicDepth, invalidationType);
        this.processor.invalidateLocally(inv, this.fakeInvalidationTarget);
        return inv;
    }


    public Invalidation addInvalidation(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        return addInvalidation(
                        recordedInvalidations(true),
                        createInvalidation(key, invalidationTopicDepth, invalidationType));
    }


    public Invalidation delayRollbackInvalidation(Object[] key, int invalidationTopicDepth, int invalidationType)
    {
        return addInvalidation(
                        recordedRollbackInvalidations(true),
                        createInvalidation(key, invalidationTopicDepth, invalidationType));
    }


    private Invalidation addInvalidation(Map<Object, Invalidation> invalidations, Invalidation invalidation)
    {
        Object mergingKey = invalidation.getMergingKey();
        Invalidation existing = invalidations.get(mergingKey);
        if(existing == null)
        {
            invalidations.put(mergingKey, invalidation);
            return invalidation;
        }
        Invalidation merged = existing.merge(invalidation);
        invalidations.put(mergingKey, merged);
        return merged;
    }


    private boolean recordInvalidation(Object[] key)
    {
        if(RelationCacheKey.isRelationKey(key))
        {
            return addSingleRelationalKey(key);
        }
        boolean added = false;
        for(Object[] singleKey : DefaultCache.toSingleKey(key))
        {
            added |= addSingleKey(singleKey);
        }
        return added;
    }


    private boolean addSingleRelationalKey(Object[] key)
    {
        return addSingleRelationalKey(RelationCacheKey.createKey(key));
    }


    private boolean addSingleRelationalKey(RelationCacheKey key)
    {
        getContainer(key).invalidate(key);
        singleKeyHasBeenInvalidated(key.toKey());
        return true;
    }


    private static final Object END_NODE = "END_NODE";


    private boolean addSingleKey(Object[] key)
    {
        boolean added = false;
        boolean singleKeyHasBeenInvalidated = false;
        Map<Object, Object> nodeMap = effectiveInvalidations(true);
        for(int i = 0; i < key.length; i++)
        {
            Object keyElement = key[i];
            Object node = nodeMap.get(keyElement);
            boolean isLastKeyElement = (i + 1 == key.length);
            if(node == null)
            {
                if(isLastKeyElement)
                {
                    singleKeyHasBeenInvalidated = true;
                    nodeMap.put(keyElement, END_NODE);
                }
                else
                {
                    nodeMap.put(keyElement, nodeMap = new HashMap<>());
                }
                added = true;
            }
            else
            {
                if(END_NODE == node)
                {
                    singleKeyHasBeenInvalidated = true;
                    break;
                }
                if(isLastKeyElement)
                {
                    singleKeyHasBeenInvalidated = true;
                    ((Map<Object, Object>)node).put(keyElement, END_NODE);
                    added = true;
                }
                else
                {
                    nodeMap = (Map<Object, Object>)node;
                }
            }
        }
        if(singleKeyHasBeenInvalidated)
        {
            singleKeyHasBeenInvalidated(key);
        }
        return added;
    }


    protected void singleKeyHasBeenInvalidated(Object[] key)
    {
    }


    public boolean isEmpty()
    {
        return effectiveInvalidations(false).isEmpty();
    }


    public boolean isInvalidated(Object[] key)
    {
        if(RelationCacheKey.isRelationKey(key))
        {
            return isRelationInvalidated(key);
        }
        Map<Object, Object> rootMap = effectiveInvalidations(false);
        if(!rootMap.isEmpty())
        {
            for(Object[] singleKey : DefaultCache.toSingleKey(key))
            {
                if(containsSingleKey(rootMap, singleKey))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isRelationInvalidated(Object[] key)
    {
        return isRelationInvalidated(RelationCacheKey.createKey(key));
    }


    private boolean isRelationInvalidated(RelationCacheKey key)
    {
        return getContainer(key).isInvalidated(key);
    }


    private RelationInvalidationsContainer getContainer(RelationCacheKey key)
    {
        return this.invalidationsForRelations
                        .computeIfAbsent(key.getRelationId(), relation -> new RelationInvalidationsContainer(key.getRelationId()));
    }


    private boolean containsSingleKey(Map<Object, Object> rootMap, Object[] key)
    {
        Map<Object, Object> nodeMap = rootMap;
        for(int i = 0; i < key.length; i++)
        {
            Object node = nodeMap.get(key[i]);
            if(node == null)
            {
                return false;
            }
            if(END_NODE == node)
            {
                return true;
            }
            nodeMap = (Map<Object, Object>)node;
        }
        return false;
    }
}
