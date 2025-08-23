package de.hybris.platform.cache.relation;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import java.util.Objects;

public class RelationCacheKey implements CacheKey
{
    public static final String RELATION = "Relation";
    public static final int KEY_POSITION_RELATION = 0;
    public static final int KEY_POSITION_RELATION_ID = 1;
    public static final int KEY_POSITION_RELATION_SIDE_ID = 2;
    public static final int KEY_POSITION_PARENT_PK = 3;
    private static final int KEY_FOR_RELATION_LENGTH = 2;
    private final String tenantId;
    private final String relationId;
    private final String relationSideId;
    private final PK parentPk;
    private final String fullyQualifiedRelationSideId;
    private final String invalidationIdentifier;


    public RelationCacheKey(String tenantId, String relationId, String relationSideId, PK parentPk)
    {
        this.tenantId = tenantId;
        this.relationId = relationId;
        this.relationSideId = relationSideId;
        this.parentPk = parentPk;
        this.fullyQualifiedRelationSideId = assembleFullyQualifiedRelationSideId(relationId, relationSideId);
        this.invalidationIdentifier = assembleInvalidationIdentifier(relationId);
    }


    public RelationCacheKey(String tenantId, String relationId)
    {
        this.tenantId = tenantId;
        this.relationId = relationId;
        this.relationSideId = null;
        this.parentPk = null;
        this.fullyQualifiedRelationSideId = relationId;
        this.invalidationIdentifier = assembleInvalidationIdentifier(relationId);
    }


    public static boolean isRelationKey(Object[] key)
    {
        return "Relation".equals(key[0]);
    }


    public static RelationCacheKey createKey(Object[] key)
    {
        if(!"Relation".equals(key[0]))
        {
            throw new UnsupportedOperationException();
        }
        if(key.length == 2)
        {
            return new RelationCacheKey(Registry.getCurrentTenant().getTenantID(), (String)key[1]);
        }
        return new RelationCacheKey(Registry.getCurrentTenant().getTenantID(), (String)key[1], (String)key[2], (PK)key[3]);
    }


    public Object[] toKey()
    {
        if(isForRelation())
        {
            return new Object[] {"Relation", getRelationId()};
        }
        return new Object[] {"Relation", getRelationId(), getRelationSideId(), getParentPk()};
    }


    public static Object[] createInvalidationKey(String relationId, String relationSideId, PK parentPK)
    {
        return new Object[] {"Relation", relationId, relationSideId, parentPK};
    }


    public static Object[] createInvalidationKeyForRelation(String relationId)
    {
        return new Object[] {"Relation", relationId};
    }


    private String assembleInvalidationIdentifier(String relationId)
    {
        return relationId + ".RELATION";
    }


    private String assembleFullyQualifiedRelationSideId(String relationId, String relationSideId)
    {
        return relationId + "." + relationId;
    }


    public CacheUnitValueType getCacheValueType()
    {
        return CacheUnitValueType.NON_SERIALIZABLE;
    }


    public Object getTypeCode()
    {
        return "Relation";
    }


    public String getRelationId()
    {
        return this.relationId;
    }


    public String getRelationSideId()
    {
        return this.relationSideId;
    }


    public PK getParentPk()
    {
        return this.parentPk;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public String getFullyQualifiedRelationSideId()
    {
        return this.fullyQualifiedRelationSideId;
    }


    public String getInvalidationIdentifier()
    {
        return this.invalidationIdentifier;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        RelationCacheKey that = (RelationCacheKey)o;
        return (Objects.equals(this.tenantId, that.tenantId) &&
                        Objects.equals(this.relationId, that.relationId) &&
                        Objects.equals(this.relationSideId, that.relationSideId) &&
                        Objects.equals(this.parentPk, that.parentPk));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.tenantId, this.relationId, this.relationSideId, this.parentPk});
    }


    public boolean isForRelation()
    {
        return (getRelationSideId() == null && getParentPk() == null);
    }


    public String toString()
    {
        return "RelationCacheKey{tenantId='" + this.tenantId + "', relationId='" + this.relationId + "', relationSideId='" + this.relationSideId + "', parentPk=" + this.parentPk + "', fullyQualifiedRelationSideId=" + this.fullyQualifiedRelationSideId + "', invalidationIdentifier="
                        + this.invalidationIdentifier + "}";
    }
}
