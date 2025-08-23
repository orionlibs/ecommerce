package de.hybris.platform.servicelayer.impex.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

public class CmdImpExService
{
    private static final Logger LOG = Logger.getLogger(CmdImpExService.class);
    private ImportService importService;


    public void importFromFile(String importResourceURI)
    {
        ImpExResource impExResource = getImpexResourceByURI(importResourceURI);
        ImportConfig importConfig = new ImportConfig();
        importConfig.setScript(impExResource);
        importConfig.setEnableCodeExecution(Boolean.TRUE);
        ImportResult importResult = this.importService.importData(importConfig);
        printResult(importResult);
    }


    private ImpExResource getImpexResourceByURI(String importResourceURI)
    {
        return (ImpExResource)new StreamBasedImpExResource(getStreamFromResource(importResourceURI), StandardCharsets.UTF_8.name());
    }


    private InputStream getStreamFromResource(String importResourceURI)
    {
        try
        {
            Resource resource = getResourceByURI(importResourceURI);
            Preconditions.checkState(resource.exists(), "Resource " + importResourceURI + " does not exist. You must use absolute path.");
            return resource.getInputStream();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }


    private Resource getResourceByURI(String importResourceURI) throws MalformedURLException
    {
        FileSystemResource fileSystemResource;
        if(ResourceUtils.isUrl(importResourceURI))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Getting resource from remote file system by protocol: " + importResourceURI);
            }
            UrlResource urlResource = new UrlResource(importResourceURI);
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Getting resource from local file system by shortcut: " + importResourceURI);
            }
            fileSystemResource = new FileSystemResource(importResourceURI);
        }
        return (Resource)fileSystemResource;
    }


    private void printResult(ImportResult importResult)
    {
        LOG.info("Import result: ");
        LOG.info(" -> Result: " + (importResult.isSuccessful() ? "Finished successfully" : "Finished with errors"));
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }
}
