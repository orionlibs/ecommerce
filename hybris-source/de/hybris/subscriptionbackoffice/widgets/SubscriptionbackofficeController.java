package de.hybris.subscriptionbackoffice.widgets;

import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.subscriptionbackoffice.services.SubscriptionbackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;

public class SubscriptionbackofficeController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private Label label;
    @WireVariable
    private SubscriptionbackofficeService subscriptionbackofficeService;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        this.label.setValue(this.subscriptionbackofficeService.getHello() + " SubscriptionbackofficeController");
    }
}
