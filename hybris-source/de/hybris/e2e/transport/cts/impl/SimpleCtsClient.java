package de.hybris.e2e.transport.cts.impl;

import com.sap.document.sap.soap.functions.mc_style.CtsWsObjectListEntries;
import com.sap.document.sap.soap.functions.mc_style.CtsWsObjectListEntry;
import com.sap.document.sap.soap.functions.mc_style.CtsWsReply;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestFilter;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTraResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTransportEntity;
import com.sap.document.sap.soap.functions.mc_style.CtsWsUploadResponse;
import de.hybris.e2e.transport.cts.ConfigurationHolder;
import de.hybris.e2e.transport.cts.CtsClient;
import de.hybris.e2e.transport.cts.CtsService;
import de.hybris.e2e.transport.utils.ConsolePrinter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SimpleCtsClient implements CtsClient
{
    private final ConfigurationHolder holder;
    private final CtsService ctsService;
    private CtsWsUploadResponse uploadResponse;
    private CtsWsRequestResponse requestResponse;
    private final String fileName;
    private final String shortName;
    private static final Logger LOGGER = Logger.getLogger(SimpleCtsClient.class.getName());


    public SimpleCtsClient(ConfigurationHolder holder, CtsService service, String fileName)
    {
        this.holder = holder;
        this.ctsService = service;
        this.fileName = fileName;
        Path path = Paths.get(fileName, new String[0]);
        this.shortName = path.getFileName().toString();
    }


    public final void uploadFile()
    {
        setRequestResponse(getDefaultRequest());
        verifyReply(this.requestResponse.getTraResponse().getReply(), "'get default request'");
        setUploadResponse(internalUploadFile());
        if(this.uploadResponse != null)
        {
            verifyReply(this.uploadResponse.getReply(), "'upload file'");
            CtsWsTraResponse attachResponse = attachToRequest();
            verifyReply(attachResponse.getReply(), "'attach to request'");
            CtsWsTraResponse submitResponse = submitRequest();
            verifyReply(submitResponse.getReply(), "'submit request'");
        }
        else
        {
            LOGGER.log(Level.WARNING, "Request cannot be completed due to problem with a file upload");
        }
    }


    public void setUploadResponse(CtsWsUploadResponse response)
    {
        this.uploadResponse = response;
    }


    public void setRequestResponse(CtsWsRequestResponse response)
    {
        this.requestResponse = response;
    }


    public CtsWsRequestFilter configureRequestFilter()
    {
        CtsWsRequestFilter ctsWsRequestFilter = new CtsWsRequestFilter();
        ctsWsRequestFilter.setSid(this.holder.getSid());
        ctsWsRequestFilter.setApplicationType(this.holder.getApplicationType());
        ctsWsRequestFilter.setUserName(this.holder.getUser());
        return ctsWsRequestFilter;
    }


    protected void verifyReply(CtsWsReply reply, String phaseName)
    {
        String message = String.format("Return code from phase %s is: %d", new Object[] {phaseName, Integer.valueOf(reply.getReturnCode())});
        ConsolePrinter.println(message);
        LOGGER.log(Level.INFO, message);
        message = String.format("Message from phase %s  is: %s", new Object[] {phaseName, reply.getMessage()});
        ConsolePrinter.println(message);
        LOGGER.log(Level.INFO, message);
    }


    public CtsWsRequestResponse getDefaultRequest()
    {
        String name = (new File(this.fileName)).getName();
        return this.ctsService.getDefaultRequest(name.substring(0, name.lastIndexOf('.')), "F", configureRequestFilter());
    }


    public CtsWsUploadResponse internalUploadFile()
    {
        CtsWsUploadResponse response = null;
        boolean notUploaded = true;
        int bytesRead = 0;
        try
        {
            InputStream in = new FileInputStream(this.fileName);
            try
            {
                int length = this.holder.getPackageSize() * 1024 * 1024;
                byte[] bytes = new byte[length];
                while((bytesRead = in.read(bytes, 0, bytes.length)) != -1)
                {
                    if(bytesRead < length)
                    {
                        bytes = handleBytesLeft(bytesRead, bytes);
                    }
                    if(notUploaded)
                    {
                        response = this.ctsService.uploadBytesIntoFile(bytes, this.shortName);
                        notUploaded = false;
                        LOGGER.log(Level.INFO, String.format("File uploaded with response code: %s and message: %s", new Object[] {Integer.valueOf(response.getReply().getReturnCode()), response
                                        .getReply().getMessage()}));
                        continue;
                    }
                    CtsWsUploadResponse appendResponse = this.ctsService.appendBytesIntoFile(bytes, response.getFileHandle());
                    LOGGER.log(Level.INFO, String.format("File uploaded with response code: %s and message: %s", new Object[] {Integer.valueOf(appendResponse.getReply().getReturnCode()), appendResponse
                                    .getReply().getMessage()}));
                }
                in.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    in.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            ConsolePrinter.println(e.getLocalizedMessage());
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return response;
    }


    protected static byte[] handleBytesLeft(int bytesRead, byte[] bytes)
    {
        byte[] shorterPart = new byte[bytesRead];
        System.arraycopy(bytes, 0, shorterPart, 0, bytesRead);
        return shorterPart;
    }


    public CtsWsTraResponse attachToRequest()
    {
        CtsWsTransportEntity entity = new CtsWsTransportEntity();
        CtsWsObjectListEntries objectList = new CtsWsObjectListEntries();
        entity.setApplicationType(this.holder.getApplicationType());
        entity.setFileHandle(getFileHandle());
        entity.setAttachUser(this.holder.getUser());
        entity.setObjectList(objectList);
        try
        {
            ZipFile zipFile = new ZipFile(this.fileName);
            try
            {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                int i = 0;
                while(entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    CtsWsObjectListEntry listEntry = new CtsWsObjectListEntry();
                    listEntry.setId(Integer.toString(i++));
                    listEntry.setName(entry.getName());
                    String subFileName = entry.getName();
                    listEntry.setType(subFileName.substring(subFileName.lastIndexOf('.') + 1));
                    listEntry.setIsComplete("T");
                    objectList.getItem().add(listEntry);
                }
                zipFile.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    zipFile.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            ConsolePrinter.println("Cannot read:" + this.fileName);
            LOGGER.log(Level.SEVERE, e, () -> "Cannot read:" + this.fileName);
        }
        return this.ctsService.attachToRequest(entity, getRequestId());
    }


    public CtsWsTraResponse submitRequest()
    {
        CtsWsTraResponse internalSubmitRequest = this.ctsService.submitRequest(getRequestId());
        ConsolePrinter.println("Please find a link to a request here: " + internalSubmitRequest.getUrl());
        LOGGER.log(Level.INFO, String.format("Please find a link to a request here: %s", new Object[] {internalSubmitRequest.getUrl()}));
        return internalSubmitRequest;
    }


    public String getRequestId()
    {
        return this.requestResponse.getRequest().getRequestId();
    }


    public String getFileHandle()
    {
        return this.uploadResponse.getFileHandle();
    }
}
