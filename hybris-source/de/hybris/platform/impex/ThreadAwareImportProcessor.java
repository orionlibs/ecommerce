package de.hybris.platform.impex;

import de.hybris.platform.impex.jalo.imp.ImportProcessor;

public interface ThreadAwareImportProcessor extends ImportProcessor
{
    void setMaxThreads(int paramInt);
}
