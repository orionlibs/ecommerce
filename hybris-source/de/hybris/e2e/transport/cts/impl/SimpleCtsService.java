package de.hybris.e2e.transport.cts.impl;

import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestFilter;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTraResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTransportEntity;
import com.sap.document.sap.soap.functions.mc_style.CtsWsUploadResponse;
import com.sap.document.sap.soap.functions.mc_style.EXPORTCTSWS;
import com.sap.document.sap.soap.functions.mc_style.ExportService;
import de.hybris.e2e.transport.cts.CtsService;
import de.hybris.e2e.transport.utils.ConsolePrinter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleCtsService implements CtsService
{
    private static final Logger LOG = Logger.getLogger(SimpleCtsService.class.getName());
    private EXPORTCTSWS binding = null;


    public SimpleCtsService(String url, String wsName, String bindingName)
    {
        try
        {
            ExportService service = new ExportService(new URL(url), wsName, bindingName);
            this.binding = service.getExportServiceBinding1();
        }
        catch(Exception e)
        {
            ConsolePrinter.println("Problem with connection to CTS export web service: '" + e.getMessage() + "'");
            ConsolePrinter.println("Please check your configuration and network settings.");
            LOG.log(Level.SEVERE, e, () -> "Problem with connection to CTS export web service: '" + e.getMessage() + "'");
            LOG.log(Level.SEVERE, "Please check your configuration and network settings.");
        }
    }


    public CtsWsRequestResponse getDefaultRequest(String description, String refreshMode, CtsWsRequestFilter filter)
    {
        return this.binding.ctsGetDefaultRequest(description, refreshMode, filter);
    }


    public CtsWsUploadResponse uploadBytesIntoFile(byte[] bytes, String fileName)
    {
        return this.binding.ctsUploadBytesIntoFile(bytes, fileName);
    }


    public CtsWsUploadResponse appendBytesIntoFile(byte[] bytes, String fileHandle)
    {
        return this.binding.ctsAppendBytesIntoFile(bytes, fileHandle);
    }


    public CtsWsTraResponse attachToRequest(CtsWsTransportEntity entity, String requestId)
    {
        return this.binding.ctsAttachToRequest(entity, requestId);
    }


    public CtsWsTraResponse submitRequest(String requestId)
    {
        return this.binding.ctsSubmitRequest(requestId);
    }
}
