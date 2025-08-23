package de.hybris.platform.processengine.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface BusinessProcessServiceDao
{
    @Deprecated(since = "6.2.0", forRemoval = true)
    @Nullable
    BusinessProcessModel getProcess(@Nonnull String paramString);


    @Nullable
    BusinessProcessModel findProcessByName(@Nonnull String paramString);


    @Nonnull
    List<String> findBusinessProcessTaskActions(@Nonnull PK paramPK);
}
