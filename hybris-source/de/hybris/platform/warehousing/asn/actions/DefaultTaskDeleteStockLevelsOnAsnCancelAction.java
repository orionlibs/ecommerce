package de.hybris.platform.warehousing.asn.actions;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.asn.service.AsnService;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.warehousing.taskassignment.actions.AbstractTaskAssignmentActions;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTaskDeleteStockLevelsOnAsnCancelAction extends AbstractTaskAssignmentActions
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskDeleteStockLevelsOnAsnCancelAction.class);
    private AsnService asnService;


    public WorkflowDecisionModel perform(WorkflowActionModel workflowActionModel)
    {
        if(getAttachedAsn(workflowActionModel).isPresent())
        {
            AdvancedShippingNoticeModel attachedAsn = getAttachedAsn(workflowActionModel).get();
            List<StockLevelModel> stockLevels = getAsnService().getStockLevelsForAsn(attachedAsn);
            if(CollectionUtils.isNotEmpty(stockLevels))
            {
                getModelService().removeAll(stockLevels);
                LOGGER.info("{} Stocklevels for product code: {} are being removed because ASN: {} got cancelled", new Object[] {Integer.valueOf(stockLevels.size()), ((StockLevelModel)stockLevels
                                .iterator().next()).getProductCode(), attachedAsn.getInternalId()});
            }
        }
        return workflowActionModel.getDecisions().isEmpty() ? null : workflowActionModel.getDecisions().iterator().next();
    }


    protected AsnService getAsnService()
    {
        return this.asnService;
    }


    @Required
    public void setAsnService(AsnService asnService)
    {
        this.asnService = asnService;
    }
}
