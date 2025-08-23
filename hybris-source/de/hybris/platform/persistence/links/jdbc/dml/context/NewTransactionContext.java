package de.hybris.platform.persistence.links.jdbc.dml.context;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.cache.AdditionalInvalidationData;
import de.hybris.platform.cache.RelationsCache;
import de.hybris.platform.cache.relation.RelationAttributes;
import de.hybris.platform.cache.relation.TypeId;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.persistence.links.jdbc.JdbcLinksSupport;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModification;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModifictionContext;
import de.hybris.platform.persistence.links.jdbc.dml.RelationTypeHelper;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

class NewTransactionContext implements RelationModifictionContext
{
    private final ImmutableList.Builder<EntityRecord> recordsToPersist;
    private final WritePersistenceGateway writePersistenceGateway;
    private final PK relationTypePk;
    protected final String relationTypeCode;
    protected final String parentAttributeName;
    protected final String childAttributeName;
    protected final String positionAttributeName;
    protected final String reversePositionAttributeName;
    protected final String languageAttributeName;
    protected final int relationItemTypeCode;
    protected final String qualifierAttributeName;
    protected final String relationCode;
    protected final Date now;
    private final RelationAttributes attributesToCache;
    private final Map<PK, AdditionalInvalidationData> relationInvalidationData = new HashMap<>();


    public NewTransactionContext(String relationCode, WritePersistenceGateway writePersistenceGateway, boolean parentIsSource, Date now)
    {
        Preconditions.checkNotNull(relationCode, "relationCode can't be null");
        Preconditions.checkNotNull(writePersistenceGateway, "writePersistenceGateway can't be null");
        this.relationCode = relationCode;
        this.writePersistenceGateway = writePersistenceGateway;
        this.recordsToPersist = ImmutableList.builder();
        TypeInfoMap relationTypeInfo = RelationTypeHelper.getTypeInfoMap(relationCode);
        this.relationTypePk = relationTypeInfo.getTypePK();
        this.relationTypeCode = relationTypeInfo.getCode();
        this.parentAttributeName = parentIsSource ? "source" : "target";
        this.childAttributeName = parentIsSource ? "target" : "source";
        this.positionAttributeName = parentIsSource ? "sequenceNumber" : "reverseSequenceNumber";
        this.reversePositionAttributeName = parentIsSource ? "reverseSequenceNumber" : "sequenceNumber";
        this.languageAttributeName = "language";
        this.qualifierAttributeName = "qualifier";
        this.relationItemTypeCode = relationTypeInfo.getItemTypeCode();
        this.now = (now == null) ? new Date() : now;
        this.attributesToCache = RelationsCache.getDefaultInstance().getSingleCacheableAttributes(TypeId.fromTypePk(this.relationTypePk));
    }


    public void consume(Iterable<RelationModification> modifications)
    {
        Preconditions.checkNotNull(modifications, "modifications can't be null");
        for(RelationModification modification : modifications)
        {
            modification.contribute(this);
        }
    }


    public void addNewLinkToChild(long parentPK, long langPK, long childPK, int position, int reversePosition)
    {
        ItemPropertyValue languagePK = (JdbcLinksSupport.NONE_LANGUAGE_PK_VALUE.longValue() == langPK) ? null : getItemPropertyHolderFrom(langPK);
        ItemPropertyValue parentRef = getItemPropertyHolderFrom(parentPK);
        ItemPropertyValue childRef = getItemPropertyHolderFrom(childPK);
        Set<PropertyHolder> changes = changes(new Object[] {
                        this.parentAttributeName, parentRef, this.childAttributeName, childRef, this.languageAttributeName, languagePK, this.positionAttributeName,
                        Integer.valueOf(position), this.reversePositionAttributeName,
                        Integer.valueOf(reversePosition),
                        this.qualifierAttributeName, this.relationCode});
        PK linkPk = createNewPK();
        this.recordsToPersist.add(new InsertRecord(linkPk, this.relationTypeCode, changes));
        recordAdditionalInvalidationDataIfNeeded(linkPk, childRef.getPK(), parentRef.getPK());
    }


    public void shiftExistingLink(long linkPK, int newPosition, long version)
    {
        shiftExistingLink(linkPK, newPosition, version, 0L, null);
    }


    public void shiftExistingLink(long linkPK, int newPosition, long version, long parentPK, Long childPK)
    {
        Set<PropertyHolder> changes = changes(new Object[] {this.positionAttributeName, Integer.valueOf(newPosition)});
        PK pk = getPKFrom(linkPK);
        this.recordsToPersist.add(new UpdateRecord(pk, this.relationTypeCode, version, changes));
        recordAdditionalInvalidationDataIfNeeded(pk, (childPK == null) ? PK.NULL_PK : getPKFrom(childPK.longValue()), getPKFrom(parentPK));
    }


    public void removeExistingLink(long linkPK, long version)
    {
        removeExistingLink(linkPK, version, 0L, null);
    }


    public void removeExistingLink(long linkPK, long version, long parentPK, Long childPK)
    {
        PK pk = getPKFrom(linkPK);
        this.recordsToPersist.add(new DeleteRecord(getPKFrom(linkPK), this.relationTypeCode, version));
        recordAdditionalInvalidationDataIfNeeded(pk, (childPK == null) ? PK.NULL_PK : getPKFrom(childPK.longValue()), getPKFrom(parentPK));
    }


    public void touchExistingItem(long itemPK)
    {
        this.recordsToPersist.add(getTouchRecordForItem(getPKFrom(itemPK)));
    }


    public void flush()
    {
        ImmutableList<EntityRecord> records = this.recordsToPersist.build();
        if(CollectionUtils.isNotEmpty((Collection)records))
        {
            EntityRecordsToChangeSetAdapter entityRecordsToChangeSetAdapter = new EntityRecordsToChangeSetAdapter((Collection)records, this.relationInvalidationData);
            this.writePersistenceGateway.persist((ChangeSet)entityRecordsToChangeSetAdapter);
        }
    }


    protected Date now()
    {
        return this.now;
    }


    private ItemPropertyValue getItemPropertyHolderFrom(long pkValue)
    {
        return new ItemPropertyValue(getPKFrom(pkValue));
    }


    protected PK getPKFrom(long pkValue)
    {
        return PK.fromLong(pkValue);
    }


    protected Item getItemByPK(PK pk)
    {
        return JaloSession.getCurrentSession().getItem(pk);
    }


    private PK createNewPK()
    {
        return PK.createCounterPK(this.relationItemTypeCode);
    }


    private EntityRecord getTouchRecordForItem(PK itemPK)
    {
        Item item = getItemByPK(itemPK);
        long version = item.getPersistenceVersion();
        String typeCode = item.getComposedType().getCode();
        return (EntityRecord)new UpdateRecord(itemPK, typeCode, version, changes(new Object[0]));
    }


    private boolean isRelationCacheEnabled()
    {
        return this.attributesToCache.containsAnyAttribute();
    }


    private void recordAdditionalInvalidationDataIfNeeded(PK linkPk, PK childPk, PK parentPk)
    {
        if(isRelationCacheEnabled() && parentPk != null && !parentPk.equals(PK.NULL_PK))
        {
            this.relationInvalidationData.put(linkPk,
                            AdditionalInvalidationData.builder()
                                            .addForeignKey(Item.TYPE, this.relationTypePk)
                                            .addForeignKey(this.childAttributeName, childPk)
                                            .addForeignKey(this.parentAttributeName, parentPk)
                                            .build());
        }
    }


    private static Set<PropertyHolder> changes(Object... attributes)
    {
        Preconditions.checkArgument(((attributes.length & 0x1) == 0), "Number of attributes must be even.");
        ImmutableSet.Builder<PropertyHolder> resultBuilder = ImmutableSet.builder();
        for(int i = 0; i < attributes.length; i += 2)
        {
            String attributeName = (String)attributes[i];
            Object attributeValue = attributes[i + 1];
            resultBuilder.add(new PropertyHolder(attributeName, attributeValue));
        }
        return (Set<PropertyHolder>)resultBuilder.build();
    }
}
