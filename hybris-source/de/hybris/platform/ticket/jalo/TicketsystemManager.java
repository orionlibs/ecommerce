package de.hybris.platform.ticket.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExManager;
import java.io.InputStream;
import org.apache.log4j.Logger;

public class TicketsystemManager extends GeneratedTicketsystemManager
{
    private static final Logger LOG = Logger.getLogger(TicketsystemManager.class.getName());


    public TicketsystemManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of TicketsystemManager called.");
        }
    }


    public static TicketsystemManager getInstance()
    {
        return (TicketsystemManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("ticketsystem");
    }


    protected void importCSVFromResources(String csv)
    {
        LOG.info("Importing [" + csv + "]");
        importCSVFromResources(csv, "UTF-8", ';', '"', true);
        LOG.info("DONE importing [" + csv + "]");
    }


    protected void importCSVFromResources(String csv, String encoding, char fieldseparator, char quotecharacter, boolean codeExecution)
    {
        LOG.info("importing resource " + csv);
        InputStream input = TicketsystemManager.class.getResourceAsStream(csv);
        if(input == null)
        {
            LOG.warn("Import resource '" + csv + "' not found!");
            return;
        }
        ImpExManager.getInstance().importData(input, encoding, fieldseparator, quotecharacter, codeExecution);
    }
}
