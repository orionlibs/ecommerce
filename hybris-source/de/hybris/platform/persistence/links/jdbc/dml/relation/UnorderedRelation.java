package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModification;

class UnorderedRelation extends BaseRelation
{
    protected static final int POSITION_FOR_UNORDERED_LINKS = 0;


    public UnorderedRelation(RelationId relationId, Iterable<ExistingLinkToChild> existingLinks, boolean markAsModified)
    {
        super(relationId, existingLinks, markAsModified);
    }


    public Iterable<RelationModification> getModificationsForInsertion(Iterable<Long> childPKs, int position)
    {
        ImmutableSet.Builder<RelationModification> resultBuilder = ImmutableSet.builder();
        for(Long childPK : childPKs)
        {
            resultBuilder.addAll(addNewLink(childPK.longValue(), 0));
        }
        if(!Iterables.isEmpty(childPKs) && isMarkAsModified())
        {
            resultBuilder.addAll(touchParent());
        }
        return (Iterable<RelationModification>)resultBuilder.build();
    }


    public Iterable<RelationModification> getModificationsForSetting(Iterable<Long> childPKsToSet)
    {
        Preconditions.checkNotNull(childPKsToSet, "childPKsToSet can't be null");
        if(Iterables.isEmpty(childPKsToSet))
        {
            return clear();
        }
        ImmutableSet.Builder<RelationModification> resultBuilder = ImmutableSet.builder();
        BaseRelation.ReusableLinks reusableLinks = new BaseRelation.ReusableLinks(getExistingLinks());
        boolean anythingChanged = false;
        for(Long childPK : childPKsToSet)
        {
            if(reusableLinks.takeBestMatchingLink(childPK) == null)
            {
                resultBuilder.addAll(addNewLink(childPK.longValue(), 0));
                anythingChanged = true;
            }
        }
        for(ExistingLinkToChild unusedLink : reusableLinks.getUnusedLinks())
        {
            resultBuilder.addAll(removeExistingLink(unusedLink));
            anythingChanged = true;
        }
        if(anythingChanged && isMarkAsModified())
        {
            resultBuilder.addAll(touchParent());
        }
        return (Iterable<RelationModification>)resultBuilder.build();
    }


    public static BaseRelation.Builder builder(RelationId id)
    {
        return (BaseRelation.Builder)new UnorderedRelationBuilder(id);
    }
}
