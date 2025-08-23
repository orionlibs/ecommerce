package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.document.context.AbstractDocumentContext;
import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.NumberTool;

public class CommonPrintLabelContext extends AbstractDocumentContext<ConsignmentProcessModel>
{
    private OrderModel order;
    private ConsignmentModel consignment;
    private DateTool date;
    private NumberTool number;
    private EscapeTool escapeTool;


    public void init(ConsignmentProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        super.init((BusinessProcessModel)businessProcessModel, documentPageModel);
        ConsignmentModel currentConsignment = businessProcessModel.getConsignment();
        this.order = (OrderModel)currentConsignment.getOrder();
        this.consignment = currentConsignment;
        this.date = new DateTool();
        this.number = new NumberTool();
        this.escapeTool = new EscapeTool();
    }


    public String escapeHtml(String stringToEscape)
    {
        return this.escapeTool.html(stringToEscape);
    }


    protected BaseSiteModel getSite(ConsignmentProcessModel consignmentProcessModel)
    {
        return consignmentProcessModel.getConsignment().getOrder().getSite();
    }


    protected LanguageModel getDocumentLanguage(ConsignmentProcessModel businessProcessModel)
    {
        return businessProcessModel.getConsignment().getOrder().getSite().getDefaultLanguage();
    }


    public DateTool getDate()
    {
        return this.date;
    }


    public OrderModel getOrder()
    {
        return this.order;
    }


    public ConsignmentModel getConsignment()
    {
        return this.consignment;
    }


    public NumberTool getNumber()
    {
        return this.number;
    }
}
