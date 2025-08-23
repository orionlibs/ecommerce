package de.hybris.platform.warehousing.process;

import de.hybris.platform.processengine.BusinessProcessService;

public interface WarehousingBusinessProcessService<T extends de.hybris.platform.core.model.ItemModel> extends BusinessProcessService
{
    void triggerSimpleEvent(T paramT, String paramString);


    void triggerChoiceEvent(T paramT, String paramString1, String paramString2) throws BusinessProcessException;


    String getProcessCode(T paramT) throws BusinessProcessException;
}
