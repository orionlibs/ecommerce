package de.hybris.deltadetection;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.List;

public interface ChangeDetectionService
{
    ItemChangeDTO getChangeForExistingItem(ItemModel paramItemModel, String paramString);


    ItemChangeDTO getChangeForRemovedItem(PK paramPK, String paramString);


    List<ItemChangeDTO> getChangesForRemovedItems(String paramString);


    void collectChangesForType(ComposedTypeModel paramComposedTypeModel, String paramString, ChangesCollector paramChangesCollector);


    void collectChangesForType(ComposedTypeModel paramComposedTypeModel, StreamConfiguration paramStreamConfiguration, ChangesCollector paramChangesCollector);


    void consumeChanges(List<ItemChangeDTO> paramList);


    void deleteItemVersionMarkersForStream(String paramString);


    void resetStream(String paramString);
}
