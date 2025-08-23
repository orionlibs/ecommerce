package de.hybris.platform.tx;

import java.util.Collection;

public interface AfterSaveListener
{
    void afterSave(Collection<AfterSaveEvent> paramCollection);
}
