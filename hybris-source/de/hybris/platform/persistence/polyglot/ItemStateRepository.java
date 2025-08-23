package de.hybris.platform.persistence.polyglot;

import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;

public interface ItemStateRepository
{
    ItemState get(Identity paramIdentity);


    ChangeSet beginCreation(Identity paramIdentity);


    void store(ChangeSet paramChangeSet);


    void remove(ItemState paramItemState);


    FindResult find(Criteria paramCriteria);


    default boolean isSupported(PolyglotFeature feature)
    {
        return false;
    }
}
