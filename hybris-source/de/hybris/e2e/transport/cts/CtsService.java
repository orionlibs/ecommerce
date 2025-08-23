package de.hybris.e2e.transport.cts;

import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestFilter;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTraResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTransportEntity;
import com.sap.document.sap.soap.functions.mc_style.CtsWsUploadResponse;

public interface CtsService
{
    CtsWsRequestResponse getDefaultRequest(String paramString1, String paramString2, CtsWsRequestFilter paramCtsWsRequestFilter);


    CtsWsUploadResponse appendBytesIntoFile(byte[] paramArrayOfbyte, String paramString);


    CtsWsUploadResponse uploadBytesIntoFile(byte[] paramArrayOfbyte, String paramString);


    CtsWsTraResponse attachToRequest(CtsWsTransportEntity paramCtsWsTransportEntity, String paramString);


    CtsWsTraResponse submitRequest(String paramString);
}
