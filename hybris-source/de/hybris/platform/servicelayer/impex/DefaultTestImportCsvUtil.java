package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.util.CSVReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class DefaultTestImportCsvUtil implements TestImportCsvUtil
{
    private static final Logger LOG = Logger.getLogger(DefaultTestImportCsvUtil.class);


    public void importCsv(String csvFile, String encoding)
    {
        LOG.info("importing resource " + csvFile);
        Assert.assertNotNull("Given file is null", csvFile);
        InputStream inputStream = ServicelayerTest.class.getResourceAsStream(csvFile);
        Assert.assertNotNull("Given file " + csvFile + "can not be found in classpath", inputStream);
        importStream(inputStream, encoding, csvFile);
    }


    private void importStream(InputStream inputStream, String encoding, String resourceName)
    {
        CSVReader reader = null;
        try
        {
            reader = new CSVReader(inputStream, encoding);
        }
        catch(UnsupportedEncodingException e)
        {
            Assert.fail("Given encoding " + encoding + " is not supported");
        }
        MediaDataTranslator.setMediaDataHandler((MediaDataHandler)new DefaultMediaDataHandler());
        Importer importer = null;
        ImpExException error = null;
        try
        {
            importer = new Importer(reader);
            importer.getReader().enableCodeExecution(true);
            importer.setMaxPass(-1);
            importer.setDumpHandler((DumpHandler)new FirstLinesDumpReader());
            importer.importAll();
        }
        catch(ImpExException e)
        {
            error = e;
        }
        finally
        {
            MediaDataTranslator.unsetMediaDataHandler();
            ((ModelContext)Registry.getCoreApplicationContext().getBean("modelContext", ModelContext.class)).clear();
        }
        if(importer.hasUnresolvedLines())
        {
            Assert.fail("Import has " + importer.getDumpedLineCountPerPass() + "+unresolved lines, first lines are:\n" + importer
                            .getDumpHandler().getDumpAsString());
        }
        Assert.assertNull("Import of resource " + resourceName + " failed" + ((error == null) ? "" : error.getMessage()), error);
        Assert.assertFalse("Import of resource " + resourceName + " failed", importer.hadError());
    }
}
