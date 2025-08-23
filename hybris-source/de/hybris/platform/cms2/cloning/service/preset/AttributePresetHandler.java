package de.hybris.platform.cms2.cloning.service.preset;

import de.hybris.platform.core.model.ItemModel;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public interface AttributePresetHandler<T> extends BiPredicate<ItemModel, String>, Supplier<T>
{
    default T get(Object originalValue)
    {
        return (T)get();
    }
}
