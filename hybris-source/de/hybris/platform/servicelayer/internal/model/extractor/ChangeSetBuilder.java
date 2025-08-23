package de.hybris.platform.servicelayer.internal.model.extractor;

import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;

public interface ChangeSetBuilder
{
    ChangeSet build(Collection<ModelWrapper> paramCollection);


    ChangeSet buildForModification(Collection<ModelWrapper> paramCollection);


    ChangeSet buildForDelete(Collection<ModelWrapper> paramCollection);
}
