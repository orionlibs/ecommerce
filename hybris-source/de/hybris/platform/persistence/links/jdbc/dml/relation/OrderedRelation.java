package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModification;

public class OrderedRelation extends BaseRelation
{
    public OrderedRelation(RelationId relationId, Iterable<ExistingLinkToChild> existingLinks, boolean markAsModified)
    {
        super(relationId, existingLinks, markAsModified);
    }


    public Iterable<RelationModification> getModificationsForInsertion(Iterable<Long> childPKsToInsert, int position)
    {
        Preconditions.checkNotNull(childPKsToInsert, "childPKsToInsert can't be null");
        if(Iterables.isEmpty(childPKsToInsert))
        {
            return noModifications();
        }
        if(position < 0)
        {
            return append(childPKsToInsert);
        }
        ImmutableSet.Builder<RelationModification> resultBuilder = ImmutableSet.builder();
        ExistingLinkToChild linkBeforeFirstInsertedChild = (position == 0) ? null : (ExistingLinkToChild)Iterables.get(getExistingLinks(), position - 1, null);
        int childPosition = (linkBeforeFirstInsertedChild == null) ? 0 : (linkBeforeFirstInsertedChild.getPosition() + 1);
        for(Long childPK : childPKsToInsert)
        {
            resultBuilder.addAll(addNewLink(childPK.longValue(), childPosition));
            childPosition++;
        }
        Iterable<ExistingLinkToChild> linksToShift = Iterables.skip(getExistingLinks(), position);
        for(ExistingLinkToChild link : linksToShift)
        {
            if(link.getPosition() >= childPosition)
            {
                break;
            }
            resultBuilder.addAll(shiftExistingLink(link, childPosition));
            childPosition++;
        }
        if(!Iterables.isEmpty(childPKsToInsert) && isMarkAsModified())
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
        int position = 0;
        boolean anythingChanged = false;
        for(Long childPK : childPKsToSet)
        {
            ExistingLinkToChild bestMatch = reusableLinks.takeBestMatchingLink(childPK, position);
            if(bestMatch == null)
            {
                resultBuilder.addAll(addNewLink(childPK.longValue(), position));
                anythingChanged = true;
            }
            else if(position > bestMatch.getPosition())
            {
                resultBuilder.addAll(shiftExistingLink(bestMatch, position));
                anythingChanged = true;
            }
            else
            {
                position = bestMatch.getPosition();
            }
            position++;
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


    private Iterable<RelationModification> append(Iterable<Long> childPKs)
    {
        Preconditions.checkNotNull(childPKs, "childPKs can't be null");
        if(Iterables.isEmpty(childPKs))
        {
            return noModifications();
        }
        ImmutableSet.Builder<RelationModification> resultBuilder = ImmutableSet.builder();
        ExistingLinkToChild lastExisting = (ExistingLinkToChild)Iterables.getLast(getExistingLinks(), null);
        int position = (lastExisting == null) ? 0 : (lastExisting.getPosition() + 1);
        for(Long childPK : childPKs)
        {
            resultBuilder.addAll(addNewLink(childPK.longValue(), position));
            position++;
        }
        if(!Iterables.isEmpty(childPKs) && isMarkAsModified())
        {
            resultBuilder.addAll(touchParent());
        }
        return (Iterable<RelationModification>)resultBuilder.build();
    }


    public static BaseRelation.Builder builder(RelationId id)
    {
        return (BaseRelation.Builder)new OrderedRelationBuilder(id);
    }
}
