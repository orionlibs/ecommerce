package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.exp.HeaderLibrary;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

public interface ScriptProcessingWizard
{
    void reloadScript(SessionContext paramSessionContext, ImpExMedia paramImpExMedia);


    String getMediaCode(String paramString);


    ImpExMedia getJobMedia();


    EnumerationValue getMode();


    String getImpExScript();


    void setJobMedia(ImpExMedia paramImpExMedia);


    void setHeaderLibrary(SessionContext paramSessionContext, HeaderLibrary paramHeaderLibrary);
}
