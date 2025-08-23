package de.hybris.datasupplier.facades.impl;

import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import de.hybris.datasupplier.data.DSSendResult;
import de.hybris.datasupplier.exceptions.DSContentGenerationException;
import de.hybris.datasupplier.exceptions.DSSenderException;
import de.hybris.datasupplier.facades.DataSupplierFacade;
import de.hybris.datasupplier.services.DSConfigurationService;
import de.hybris.datasupplier.services.DSContentGeneratorService;
import de.hybris.datasupplier.services.DSSenderService;
import org.apache.log4j.Logger;

public class DefaultDataSupplierFacade implements DataSupplierFacade
{
    private static final Logger LOG = Logger.getLogger(DefaultDataSupplierFacade.class);
    private final DSConfigurationService configurationService;
    private final DSContentGeneratorService contentGeneratorService;
    private final DSSenderService dsSenderService;


    public DefaultDataSupplierFacade(DSConfigurationService configurationService, DSSenderService dsSenderService, DSContentGeneratorService contentGeneratorService)
    {
        this.configurationService = configurationService;
        this.dsSenderService = dsSenderService;
        this.contentGeneratorService = contentGeneratorService;
    }


    public DSSendResult supplyData()
    {
        SLDSupplierConfiguration configuration = this.configurationService.getSLDSldSupplierConfiguration();
        if(configuration != null && !configuration.getDisabled())
        {
            DSSendResult.SupplyStatus supplyStatus = DSSendResult.SupplyStatus.SUCCESS;
            String sldContent = null;
            try
            {
                sldContent = this.contentGeneratorService.generateContent();
                boolean status = this.dsSenderService.send(sldContent);
                if(!status)
                {
                    supplyStatus = DSSendResult.SupplyStatus.ERROR;
                }
            }
            catch(DSContentGenerationException generationExcetion)
            {
                LOG.error("An error occured while generating SLD content", (Throwable)generationExcetion);
                supplyStatus = DSSendResult.SupplyStatus.ERROR;
            }
            catch(DSSenderException senderException)
            {
                LOG.error(senderException.getMessage(), (Throwable)senderException);
                supplyStatus = DSSendResult.SupplyStatus.ERROR;
            }
            return new DSSendResult(supplyStatus, sldContent);
        }
        return new DSSendResult(DSSendResult.SupplyStatus.DISABLED);
    }
}
