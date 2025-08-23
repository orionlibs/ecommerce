package de.hybris.platform.warehousing.process.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.warehousing.process.AbstractWarehousingBusinessProcessService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import org.apache.commons.collections.CollectionUtils;

public class DefaultConsignmentProcessService extends AbstractWarehousingBusinessProcessService<ConsignmentModel>
{
    public String getProcessCode(ConsignmentModel consignment)
    {
        BusinessProcessModel consignmentProcess = getConsignmentProcess(consignment);
        return consignmentProcess.getCode();
    }


    public BusinessProcessModel getConsignmentProcess(ConsignmentModel consignmentModel)
    {
        if(CollectionUtils.isEmpty(consignmentModel.getConsignmentProcesses()))
        {
            throw new BusinessProcessException("Unable to process event for consignment [" + consignmentModel.getCode() + "]. No processes associated to the consignment.");
        }
        String expectedCode = consignmentModel.getCode() + "_ordermanagement";
        return (BusinessProcessModel)consignmentModel.getConsignmentProcesses().stream().filter(process -> expectedCode.equals(process.getCode()))
                        .findFirst().orElseThrow(() -> new BusinessProcessException("No business process found for consignment [" + consignmentModel.getCode() + "]."));
    }
}
