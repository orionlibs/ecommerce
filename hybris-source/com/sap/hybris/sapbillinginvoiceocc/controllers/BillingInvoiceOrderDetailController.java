/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapbillinginvoiceocc.controllers;

import com.sap.hybris.sapbillinginvoicefacades.document.data.ExternalSystemBillingDocumentData;
import com.sap.hybris.sapbillinginvoicefacades.facade.impl.SapBillingInvoiceFacadeImpl;
import com.sap.hybris.sapbillinginvoiceocc.security.SecuredAccessConstants;
import com.sap.hybris.sapbillinginvoiceservices.exception.SapBillingInvoiceUserException;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Api(tags = "Billing")
public class BillingInvoiceOrderDetailController
{
    private static final Logger LOG = LoggerFactory.getLogger(BillingInvoiceOrderDetailController.class);
    @Resource(name = "orderFacade")
    private OrderFacade orderFacade;
    @Resource(name = "sapBillingInvoiceOrderFacade")
    private SapBillingInvoiceFacadeImpl sapBillingInvoiceOrderFacade;


    @Secured({SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
                    SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/orders/{code}/billingDocuments/{billingDocId}.pdf", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(nickname = "getBillingDocument", value = "Get billing document of an order", notes = "Get billing document of an order in encoded byte array", consumes = javax.ws.rs.core.MediaType.APPLICATION_JSON, produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    @ApiResponses(value = {
                    @ApiResponse(code = 200, message = "Returns PDF")})
    @ApiBaseSiteIdAndUserIdParam
    public ResponseEntity<byte[]> getBillingDocument(
                    @ApiParam(value = "Order code", required = true) @PathVariable final String code,
                    @ApiParam(value = "Billing Document ID", required = true) @PathVariable final String billingDocId,
                    @ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
    {
        validateUserForOrder(code);
        byte[] pdfData;
        try
        {
            final ExternalSystemBillingDocumentData document = getSapOrderCodeForBillingDocument(code, billingDocId);
            if(document == null)
            {
                throw new NotFoundException("Billing document not found");
            }
            pdfData = sapBillingInvoiceOrderFacade.getPDFData(document.getSapOrderCode(), document.getBillingDocumentId());
        }
        catch(final SapBillingInvoiceUserException ex)
        {
            LOG.warn("Billing document not found for the user for the given billing document id", ex);
            throw new NotFoundException("Billing document not found");
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "octet-stream"));
        headers.setContentDispositionFormData("attachment", billingDocId + ".pdf");
        headers.setContentLength(pdfData.length);
        return new ResponseEntity<byte[]>(pdfData, headers, HttpStatus.OK);
    }


    protected ExternalSystemBillingDocumentData getSapOrderCodeForBillingDocument(final String orderCode,
                    final String documentId)
    {
        final List<ExternalSystemBillingDocumentData> documentList = sapBillingInvoiceOrderFacade
                        .getBillingDocumentsForOrder(orderCode);
        return documentList.stream().filter(Objects::nonNull)
                        .filter(entry -> Objects.equals(documentId, entry.getBillingDocumentId())).findFirst().orElse(null);
    }


    /**
     * Validates if the current user has access to the order
     *
     * @param code
     *            the order code
     * @throws NotFoundException
     *             if current user has no access to the order
     */
    protected void validateUserForOrder(final String code)
    {
        try
        {
            orderFacade.getOrderDetailsForCode(code);
        }
        catch(final UnknownIdentifierException ex)
        {
            LOG.warn("Order not found for the current user in current BaseStore", ex);
            throw new NotFoundException("Order Resource not found");
        }
    }
}
