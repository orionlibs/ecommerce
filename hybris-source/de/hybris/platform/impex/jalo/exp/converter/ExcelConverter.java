package de.hybris.platform.impex.jalo.exp.converter;

import de.hybris.platform.impex.jalo.exp.Report;
import de.hybris.platform.jalo.JaloBusinessException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class ExcelConverter extends AbstractExcelConverter
{
    private final Report report = null;
    private static final Logger log = Logger.getLogger(ExcelConverter.class.getName());


    public Report getReport()
    {
        return this.report;
    }


    public void start()
    {
        log.info("start....");
        log.info("using export: " + getExport());
        try
        {
            dump();
        }
        catch(JaloBusinessException e)
        {
            log.error("Error while converting to Excel", (Throwable)e);
        }
        catch(IOException e)
        {
            log.error("Error while converting to Excel", e);
        }
    }
}
