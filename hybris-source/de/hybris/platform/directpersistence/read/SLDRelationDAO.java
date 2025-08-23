package de.hybris.platform.directpersistence.read;

import java.util.Collection;

public interface SLDRelationDAO
{
    <T extends de.hybris.platform.core.model.ItemModel> Collection<T> getRelatedModels(RelationInformation paramRelationInformation);


    <T> Collection<T> getRelatedObjects(RelationInformation paramRelationInformation, Class<T> paramClass);
}
