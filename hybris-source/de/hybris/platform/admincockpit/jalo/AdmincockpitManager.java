package de.hybris.platform.admincockpit.jalo;

import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.validation.constants.GeneratedValidationConstants;
import java.io.InputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdmincockpitManager extends GeneratedAdmincockpitManager
{
    private static final Logger LOG = LoggerFactory.getLogger(AdmincockpitManager.class.getName());


    public static final AdmincockpitManager getInstance()
    {
        return (AdmincockpitManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("admincockpit");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Creating NumberSeries for " + GeneratedValidationConstants.TC.ABSTRACTCONSTRAINT);
            }
            NumberSeriesManager.getInstance().createNumberSeries(GeneratedValidationConstants.TC.ABSTRACTCONSTRAINT, "0", 1, 10);
        }
        catch(JaloInvalidParameterException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("NumberSeries " + GeneratedValidationConstants.TC.ABSTRACTCONSTRAINT + " is already created!");
            }
        }
    }


    public void createProjectData(Map<String, String> params, JspContext jspc) throws Exception
    {
    }


    private void importCSVFromResources(String csv) throws Exception
    {
        importCSVFromResources(csv, "UTF-8", ';', '"', true);
    }


    private void importCSVFromResources(String csv, String encoding, char fieldseparator, char quotecharacter, boolean codeExecution) throws Exception
    {
        LOG.info("importing resource " + csv);
        InputStream inputstream = CockpitManager.class.getResourceAsStream(csv);
        if(inputstream == null)
        {
            LOG.warn("Import resource '" + csv + "' not found!");
            return;
        }
        ImpExManager.getInstance().importData(inputstream, encoding, fieldseparator, quotecharacter, codeExecution);
    }
}
