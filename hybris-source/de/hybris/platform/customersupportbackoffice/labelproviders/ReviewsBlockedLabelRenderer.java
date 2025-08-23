package de.hybris.platform.customersupportbackoffice.labelproviders;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public class ReviewsBlockedLabelRenderer implements WidgetComponentRenderer<Listcell, ListColumn, CustomerReviewModel>
{
    protected static final String TRUE_STRING = "True";
    protected static final String FALSE_STRING = "False";


    public void render(Listcell parent, ListColumn configuration, CustomerReviewModel customerReview, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Label reviewBlockedLabel = null;
        if(customerReview.getBlocked().booleanValue())
        {
            reviewBlockedLabel = new Label("True");
        }
        else
        {
            reviewBlockedLabel = new Label("False");
        }
        reviewBlockedLabel.setVisible(true);
        reviewBlockedLabel.setParent((Component)parent);
    }
}
