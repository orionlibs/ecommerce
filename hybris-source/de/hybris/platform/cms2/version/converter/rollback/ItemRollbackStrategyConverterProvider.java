package de.hybris.platform.cms2.version.converter.rollback;

import de.hybris.platform.core.model.ItemModel;
import java.util.Optional;

public interface ItemRollbackStrategyConverterProvider
{
    Optional<ItemRollbackConverter> getConverter(ItemModel paramItemModel);
}
