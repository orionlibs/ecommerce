package de.hybris.e2e.transport.cts;

import com.sap.document.sap.soap.functions.mc_style.BasicAuthenticator;
import com.sap.document.sap.soap.functions.mc_style.CtsWsReply;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequest;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestFilter;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTraResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTransportEntity;
import com.sap.document.sap.soap.functions.mc_style.CtsWsUploadResponse;
import de.hybris.e2e.transport.cts.impl.SimpleCtsClient;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Authenticator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class CtsClientUnitTest
{
    private static final String RESPONSE_NOT_NULL = "Response cannot be null";
    private CtsClient client;
    private CtsService service;


    @Before
    public void prepareTest()
    {
        TestConfigurationHolder testConfigurationHolder = new TestConfigurationHolder();
        Authenticator.setDefault((Authenticator)new BasicAuthenticator((ConfigurationHolder)testConfigurationHolder));
        this.service = (CtsService)Mockito.mock(CtsService.class);
        File testFile = new File("testChange.zip");
        ZipEntry ze = new ZipEntry("testfile.txt");
        try
        {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(testFile));
            try
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Test");
                out.putNextEntry(ze);
                byte[] data = sb.toString().getBytes();
                out.write(data, 0, data.length);
                out.closeEntry();
                out.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    out.close();
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
            Assert.fail("Test file cannot be created");
        }
        this.client = (CtsClient)new SimpleCtsClient((ConfigurationHolder)testConfigurationHolder, this.service, testFile.getAbsolutePath());
    }


    @Test
    public void testUploadFile()
    {
        testGetDefaltRequest();
        testUploadBytesIntoFile();
    }


    @Ignore("ignored")
    @Test
    public void testSequenceCall()
    {
        prepareContractForGetDefaultRequest();
        prepareContractForUploadBytesIntoFile();
        prepareContractForAttachToRequest();
        prepareContractForSubmitRequest();
        this.client.uploadFile();
        InOrder inOrder = Mockito.inOrder(new Object[] {this.service});
        ((CtsService)inOrder.verify(this.service)).getDefaultRequest(Matchers.anyString(), Matchers.anyString(), (CtsWsRequestFilter)Matchers.any(CtsWsRequestFilter.class));
        ((CtsService)inOrder.verify(this.service)).uploadBytesIntoFile((byte[])Matchers.any(byte[].class), Matchers.anyString());
        ((CtsService)inOrder.verify(this.service)).appendBytesIntoFile((byte[])Matchers.any(byte[].class), Matchers.anyString());
        ((CtsService)inOrder.verify(this.service)).attachToRequest((CtsWsTransportEntity)Matchers.any(CtsWsTransportEntity.class), Matchers.anyString());
        ((CtsService)inOrder.verify(this.service)).submitRequest(Matchers.anyString());
    }


    public void testGetDefaltRequest()
    {
        prepareContractForGetDefaultRequest();
        CtsWsRequestResponse response = this.client.getDefaultRequest();
        this.client.setRequestResponse(response);
        Assert.assertNotNull("Response cannot be null", response);
    }


    private void prepareContractForGetDefaultRequest()
    {
        CtsWsRequestResponse ctsWsRequestResponse = new CtsWsRequestResponse();
        ctsWsRequestResponse.setRequest(new CtsWsRequest());
        CtsWsResponse wsResponse = new CtsWsResponse();
        wsResponse.setReply(new CtsWsReply());
        ctsWsRequestResponse.setTraResponse(wsResponse);
        Mockito.when(this.service.getDefaultRequest(Matchers.anyString(), Matchers.anyString(), (CtsWsRequestFilter)Matchers.any(CtsWsRequestFilter.class))).thenReturn(ctsWsRequestResponse);
    }


    public void testUploadBytesIntoFile()
    {
        prepareContractForUploadBytesIntoFile();
        CtsWsUploadResponse uploadResponse = this.client.internalUploadFile();
        this.client.setUploadResponse(uploadResponse);
        Assert.assertNotNull("Response cannot be null", uploadResponse);
    }


    private void prepareContractForUploadBytesIntoFile()
    {
        CtsWsUploadResponse response = new CtsWsUploadResponse();
        response.setReply(new CtsWsReply());
        Mockito.when(this.service.uploadBytesIntoFile((byte[])Matchers.any(byte[].class), Matchers.anyString())).thenReturn(response);
        Mockito.when(this.service.appendBytesIntoFile((byte[])Matchers.any(byte[].class), Matchers.anyString())).thenReturn(response);
    }


    public void testAttachToRequest()
    {
        prepareContractForAttachToRequest();
        CtsWsTraResponse traResponse = this.client.attachToRequest();
        Assert.assertNotNull("Response cannot be null", traResponse);
    }


    private void prepareContractForAttachToRequest()
    {
        CtsWsTraResponse response = new CtsWsTraResponse();
        response.setReply(new CtsWsReply());
        Mockito.when(this.service.attachToRequest((CtsWsTransportEntity)Matchers.any(CtsWsTransportEntity.class), Matchers.anyString())).thenReturn(response);
    }


    public void testSubmitRequest()
    {
        prepareContractForSubmitRequest();
        CtsWsTraResponse traResponse = this.client.submitRequest();
        Assert.assertNotNull("Response cannot be null", traResponse);
    }


    private void prepareContractForSubmitRequest()
    {
        CtsWsTraResponse response = new CtsWsTraResponse();
        response.setReply(new CtsWsReply());
        Mockito.when(this.service.submitRequest(Matchers.anyString())).thenReturn(response);
    }
}
