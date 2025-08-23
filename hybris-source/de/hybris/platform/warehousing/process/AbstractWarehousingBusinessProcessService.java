package de.hybris.platform.warehousing.process;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.impl.DefaultBusinessProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWarehousingBusinessProcessService<T extends ItemModel> extends DefaultBusinessProcessService implements WarehousingBusinessProcessService<T>
{
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractWarehousingBusinessProcessService.class);


    public abstract String getProcessCode(T paramT) throws BusinessProcessException;


    public void triggerSimpleEvent(T item, String eventName)
    {
        String processCode = getProcessCode(item);
        LOGGER.info("Process: {} triggered event {}", processCode, eventName);
        triggerEvent(processCode + "_" + processCode);
    }


    public void triggerChoiceEvent(T item, String eventName, String choice) throws BusinessProcessException
    {
        String processCode = getProcessCode(item);
        LOGGER.info("Process: {} triggered event {}", processCode, eventName);
        BusinessProcessEvent businessProcessEvent = BusinessProcessEvent.builder(processCode + "_" + processCode).withChoice(choice).build();
        boolean result = Boolean.TRUE.booleanValue();
        try
        {
            result = triggerEvent(businessProcessEvent);
        }
        catch(IllegalStateException e)
        {
            result = Boolean.FALSE.booleanValue();
        }
        if(!result)
        {
            throw new BusinessProcessException("Unable to process action. The process with code [" + processCode + "] is not currently waiting for action.");
        }
    }
}
