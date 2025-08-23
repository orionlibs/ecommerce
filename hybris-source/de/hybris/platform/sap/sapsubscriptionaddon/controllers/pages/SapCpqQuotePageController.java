/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapsubscriptionaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCartPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commerceservices.order.exceptions.IllegalQuoteStateException;
import de.hybris.platform.sap.saprevenuecloudorder.facade.quote.SapSbQuoteFacade;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator.CannotCloneException;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/quote")
public class SapCpqQuotePageController extends AbstractCartPageController
{
    private static final String REDIRECT_QUOTE_DETAILS_URL = REDIRECT_PREFIX + "/my-account/my-quotes/%s/";
    private static final String PROPOSAL_DOCUMENT_FILENAME = "proposaldocument.pdf";
    // localization properties
    private static final String QUOTE_PROPOSAL_DOCUMENT_NOT_PRESENT_ERROR = "quote.proposal.document.not.present.error";
    private static final Logger LOG = Logger.getLogger(SapCpqQuotePageController.class);
    @Resource(name = "sapSbQuoteFacade")
    private SapSbQuoteFacade sapSbQuoteFacade;


    @ResponseBody
    @RequestMapping(value = "{quoteCode}/cpqDownloadProposalDocument", method = RequestMethod.GET)
    public String downloadProposalDocument(@PathVariable("quoteCode") final String quoteCode, final RedirectAttributes redirectModel, final HttpServletRequest request,
                    final HttpServletResponse response)
    {
        try
        {
            final byte[] pdfData = (sapSbQuoteFacade.downloadQuoteProposalDocument(quoteCode));
            if(null != pdfData && pdfData.length > 0)
            {
                response.setHeader("Content-Disposition", "attachment;filename=" + PROPOSAL_DOCUMENT_FILENAME);
                response.setDateHeader("Expires", -1);
                response.setContentType("application/pdf");
                StreamUtils.copy(pdfData, response.getOutputStream());
                response.getOutputStream().close();
                response.getOutputStream().flush();
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return String.format(REDIRECT_QUOTE_DETAILS_URL, urlEncode(quoteCode));
        }
        catch(final IOException | IllegalQuoteStateException | CannotCloneException | IllegalArgumentException
                    | ModelSavingException e)
        {
            LOG.error("Unable to requote", e);
            GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
                            QUOTE_PROPOSAL_DOCUMENT_NOT_PRESENT_ERROR, null);
            return String.format(REDIRECT_QUOTE_DETAILS_URL, urlEncode(quoteCode));
        }
    }
}




