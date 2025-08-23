package de.hybris.platform.cmscockpit.systemsetup;

import de.hybris.platform.cockpit.systemsetup.CockpitImportConfig;
import java.util.Map;

public class CMSCockpitImportConfig extends CockpitImportConfig
{
    private Map<String, String> ctxID2FactoryMappings = null;


    public void setCtxID2FactoryMappings(Map<String, String> ctxID2FactoryMappings)
    {
        this.ctxID2FactoryMappings = ctxID2FactoryMappings;
        super.setCtxID2FactoryMappings(ctxID2FactoryMappings);
    }


    public Map<String, String> getCtxID2FactoryMappings()
    {
        return this.ctxID2FactoryMappings;
    }
}
