package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.impex.distributed.batch.BatchingImpExCRUDHandler;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import org.springframework.beans.factory.annotation.Required;

public class CRUDHandlerFactory
{
    private BatchingImpExCRUDHandler insertHandler;
    private BatchingImpExCRUDHandler updateHandler;
    private BatchingImpExCRUDHandler deleteHandler;
    private BatchingImpExCRUDHandler insertUpdateHandler;


    public BatchingImpExCRUDHandler getHandlerFor(ProcessMode processMode)
    {
        switch(null.$SwitchMap$de$hybris$platform$servicelayer$impex$ProcessMode[processMode.ordinal()])
        {
            case 1:
                return this.insertHandler;
            case 2:
                return this.insertUpdateHandler;
            case 3:
                return this.updateHandler;
            case 4:
                return this.deleteHandler;
        }
        throw new IllegalStateException("BatchingImpExCRUDHandler not found for current processMode: " + processMode);
    }


    @Required
    public void setInsertHandler(BatchingImpExCRUDHandler insertHandler)
    {
        this.insertHandler = insertHandler;
    }


    @Required
    public void setUpdateHandler(BatchingImpExCRUDHandler updateHandler)
    {
        this.updateHandler = updateHandler;
    }


    @Required
    public void setDeleteHandler(BatchingImpExCRUDHandler deleteHandler)
    {
        this.deleteHandler = deleteHandler;
    }


    @Required
    public void setInsertUpdateHandler(BatchingImpExCRUDHandler insertUpdateHandler)
    {
        this.insertUpdateHandler = insertUpdateHandler;
    }
}
