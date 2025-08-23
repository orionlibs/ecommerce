/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapserviceorder.billlinginvoice.service.impl;

import com.sap.hybris.sapbillinginvoiceservices.exception.SapBillingInvoiceUserException;
import com.sap.hybris.sapbillinginvoiceservices.service.SapBillingInvoiceService;
import de.hybris.platform.sap.sapmodel.model.SAPOrderModel;
import de.hybris.platform.sap.sapserviceorder.billlinginvoice.service.SapServiceOrderBillingInvoiceService;
import java.util.Map;

/**
 *  Service layer implementation to retrieve service order billing documents and invoices
 */
public class DefaultSapServiceOrderBillingInvoiceService implements SapServiceOrderBillingInvoiceService
{
    private SapBillingInvoiceService sapBillingInvoiceService;


    @Override
    public Map<String, Object> callS4forBillingDocuments(SAPOrderModel sapOrder, String targetSuffixUrl)
    {
        return getSapBillingInvoiceService().callS4forBillingDocuments(sapOrder, targetSuffixUrl);
    }


    @Override
    public byte[] getPDFData(SAPOrderModel sapOrder, String billingDocumentId) throws SapBillingInvoiceUserException
    {
        return getSapBillingInvoiceService().getPDFData(sapOrder, billingDocumentId);
    }


    public SapBillingInvoiceService getSapBillingInvoiceService()
    {
        return sapBillingInvoiceService;
    }


    public void setSapBillingInvoiceService(SapBillingInvoiceService sapBillingInvoiceService)
    {
        this.sapBillingInvoiceService = sapBillingInvoiceService;
    }
}
