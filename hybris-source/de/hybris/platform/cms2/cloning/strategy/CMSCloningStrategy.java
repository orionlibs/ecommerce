package de.hybris.platform.cms2.cloning.strategy;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import java.util.Map;
import java.util.Optional;

public interface CMSCloningStrategy<T extends de.hybris.platform.cms2.model.contents.CMSItemModel>
{
    T clone(T paramT, Optional<T> paramOptional, Optional<Map<String, Object>> paramOptional1) throws CMSItemNotFoundException, IllegalArgumentException;
}
