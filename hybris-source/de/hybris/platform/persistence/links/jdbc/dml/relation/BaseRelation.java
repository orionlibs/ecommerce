package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.platform.persistence.links.jdbc.dml.Relation;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModification;

abstract class BaseRelation implements Relation
{
    private static final String CHILD_PK_STRING = "childPK";
    private static final String RELATION_PK_STRING = "relationPK";
    private static final String VERSION_STRING = "version";
    private static final String NEW_POSITION_STRING = "newPosition";
    private static final String POSITION_STRING = "position";
    private final RelationId relationId;
    private final Iterable<ExistingLinkToChild> existingLinks;
    private final boolean markAsModified;


    public BaseRelation(RelationId relationId, Iterable<ExistingLinkToChild> existingLinks, boolean markAsModified)
    {
        Preconditions.checkNotNull(relationId, "relationId can't be null");
        Preconditions.checkNotNull(existingLinks, "existingLinks can't be null");
        this.relationId = relationId;
        this.existingLinks = existingLinks;
        this.markAsModified = markAsModified;
    }


    public long getLanguagePK()
    {
        return this.relationId.getLanguagePK();
    }


    public Iterable<RelationModification> getModificationsForRemoval(Iterable<Long> childPKsToRemove)
    {
        Preconditions.checkNotNull(childPKsToRemove, "childPKsToRemove can't be null");
        if(Iterables.isEmpty(childPKsToRemove))
        {
            return noModifications();
        }
        ImmutableSet.Builder<RelationModification> resultBuilder = ImmutableSet.builder();
        ImmutableSet<Long> toRemove = ImmutableSet.copyOf(childPKsToRemove);
        boolean anyLinkRemoved = false;
        for(ExistingLinkToChild link : getExistingLinks())
        {
            if(toRemove.contains(link.getChildPK()))
            {
                resultBuilder.addAll(removeExistingLink(link));
                anyLinkRemoved = true;
            }
        }
        if(anyLinkRemoved && isMarkAsModified())
        {
            resultBuilder.addAll(touchParent());
        }
        return (Iterable<RelationModification>)resultBuilder.build();
    }


    public Iterable<ExistingLinkToChild> getExistingLinks()
    {
        return this.existingLinks;
    }


    public RelationId getRelationId()
    {
        return this.relationId;
    }


    public long getParentPK()
    {
        return this.relationId.getParentPK();
    }


    protected boolean isMarkAsModified()
    {
        return this.markAsModified;
    }


    protected int getReversePosition()
    {
        return (int)(System.currentTimeMillis() >> 11L & 0x7FFFFFFFL);
    }


    protected Iterable<RelationModification> noModifications()
    {
        return (Iterable<RelationModification>)ImmutableSet.of();
    }


    protected Iterable<RelationModification> touchParent()
    {
        return (Iterable<RelationModification>)ImmutableSet.of(new Touch(getParentPK()));
    }


    protected Iterable<RelationModification> removeExistingLink(ExistingLinkToChild link)
    {
        Remove remove = new Remove(this, link.getLinkPK(), link.getVersion(), link.getChildPK());
        if(isMarkAsModified())
        {
            Touch touch = new Touch(link.getChildPK().longValue());
            return (Iterable<RelationModification>)ImmutableSet.of(remove, touch);
        }
        return (Iterable<RelationModification>)ImmutableSet.of(remove);
    }


    protected Iterable<RelationModification> addNewLink(long childPK, int position)
    {
        Add add = new Add(this, childPK, position);
        if(isMarkAsModified())
        {
            Touch touch = new Touch(childPK);
            return (Iterable<RelationModification>)ImmutableSet.of(add, touch);
        }
        return (Iterable<RelationModification>)ImmutableSet.of(add);
    }


    protected Iterable<RelationModification> shiftExistingLink(ExistingLinkToChild link, int newPosition)
    {
        Shift shift = new Shift(this, link.getLinkPK(), link.getVersion(), newPosition, link.getChildPK());
        return (Iterable<RelationModification>)ImmutableSet.of(shift);
    }


    protected Iterable<RelationModification> clear()
    {
        ImmutableSet.Builder<RelationModification> resultBuilder = ImmutableSet.builder();
        for(ExistingLinkToChild link : getExistingLinks())
        {
            resultBuilder.addAll(removeExistingLink(link));
        }
        if(!Iterables.isEmpty(getExistingLinks()) && isMarkAsModified())
        {
            resultBuilder.addAll(touchParent());
        }
        return (Iterable<RelationModification>)resultBuilder.build();
    }
}
