package de.hybris.e2e.transport.cts;

import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestFilter;
import com.sap.document.sap.soap.functions.mc_style.CtsWsRequestResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsTraResponse;
import com.sap.document.sap.soap.functions.mc_style.CtsWsUploadResponse;

public interface CtsClient
{
    void uploadFile();


    CtsWsRequestFilter configureRequestFilter();


    CtsWsRequestResponse getDefaultRequest();


    CtsWsUploadResponse internalUploadFile();


    CtsWsTraResponse attachToRequest();


    CtsWsTraResponse submitRequest();


    void setUploadResponse(CtsWsUploadResponse paramCtsWsUploadResponse);


    void setRequestResponse(CtsWsRequestResponse paramCtsWsRequestResponse);
}
