package de.hybris.platform.warehousing.process.impl;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.warehousing.process.AbstractWarehousingBusinessProcessService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultReturnProcessService extends AbstractWarehousingBusinessProcessService<ReturnRequestModel>
{
    private transient BaseStoreService baseStoreService;


    public String getProcessCode(ReturnRequestModel returnRequest)
    {
        if(CollectionUtils.isEmpty(returnRequest.getReturnProcess()))
        {
            throw new BusinessProcessException("Unable to process event for return [" + returnRequest.getCode() + "]. No processes associated to the return.");
        }
        BaseStoreModel store = (BaseStoreModel)Optional.<BaseStoreModel>ofNullable(Optional.<BaseStoreModel>ofNullable(returnRequest.getOrder().getStore()).orElseGet(() -> getBaseStoreService().getCurrentBaseStore()))
                        .orElseThrow(() -> new BusinessProcessException("Unable to process event for return [" + returnRequest.getCode() + "]. No base store associated to the return."));
        String returnProcessDefinitionName = (String)Optional.<String>ofNullable(store.getCreateReturnProcessCode()).orElseThrow(() -> new BusinessProcessException("Unable to process event for return [" + returnRequest.getCode() + "]. No return process definition for base store."));
        String expectedCodePrefix = returnProcessDefinitionName + "-" + returnProcessDefinitionName;
        Collection<String> codes = (Collection<String>)returnRequest.getReturnProcess().stream().map(BusinessProcessModel::getCode).filter(code -> code.startsWith(expectedCodePrefix)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(codes))
        {
            throw new BusinessProcessException("Unable to process event for return [" + returnRequest.getCode() + "]. No return processes associated to the return with prefix [" + returnProcessDefinitionName + "].");
        }
        if(codes.size() > 1)
        {
            throw new BusinessProcessException("Unable to process event for return [" + returnRequest.getCode() + "]. Expected only 1 process with prefix [" + returnProcessDefinitionName + "] but there were " + codes
                            .size() + ".");
        }
        return codes.iterator().next();
    }


    public BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }
}
