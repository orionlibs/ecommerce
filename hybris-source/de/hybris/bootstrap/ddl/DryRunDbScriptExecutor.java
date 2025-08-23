package de.hybris.bootstrap.ddl;

import java.io.Reader;
import java.nio.file.Path;
import org.apache.log4j.Logger;

public class DryRunDbScriptExecutor implements HybrisDbScriptsExecutor
{
    private static final Logger LOG = Logger.getLogger(DryRunDbScriptExecutor.class);


    public void executeDDl(Path scriptPath)
    {
        LOG.info("Dry run mode - DDL script execution disabled");
    }


    public void executeDDl(Reader scriptReader)
    {
        LOG.info("Dry run mode - DDL script execution disabled");
    }


    public void executeDropDdl(Path scriptPath)
    {
        LOG.info("Dry run mode - DDL Drop script execution disabled");
    }


    public void executeDropDdl(Reader scriptReader)
    {
        LOG.info("Dry run mode - DDL Drop script execution disabled");
    }


    public void executeDml(Path scriptPath)
    {
        LOG.info("Dry run mode - DML script execution disabled");
    }


    public void executeDml(Reader scriptReader)
    {
        LOG.info("Dry run mode - DML script execution disabled");
    }
}
