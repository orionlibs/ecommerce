package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Listcell;

public class PaymentTransactionAmountRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(PaymentTransactionAmountRenderer.class);
    protected static final String PAYMENT_TRANSCTION = "PaymentTransaction";
    private TypeFacade typeFacade;
    private PropertyValueService propertyValueService;
    private LabelService labelService;
    private PermissionFacade permissionFacade;


    public void render(Listcell listcell, ListColumn columnConfiguration, Object object, DataType unused, WidgetInstanceManager widgetInstanceManager)
    {
        String qualifier = columnConfiguration.getQualifier();
        try
        {
            DataType dataType = getTypeFacade().load("PaymentTransaction");
            if(dataType == null || getPermissionFacade().canReadProperty(dataType.getCode(), qualifier))
            {
                Object value = getPropertyValueService().readValue(object, qualifier);
                BigDecimal paymentTransactionAmount = ((BigDecimal)value).setScale(((PaymentTransactionEntryModel)((PaymentTransactionModel)object)
                                .getEntries().get(0)).getCurrency().getDigits().intValue(), 5);
                String amount = getLabelService().getObjectLabel(paymentTransactionAmount);
                if(StringUtils.isBlank(amount))
                {
                    amount = value.toString();
                }
                listcell.setLabel(amount);
            }
        }
        catch(TypeNotFoundException e)
        {
            LOG.error("Could not render row......", (Throwable)e);
        }
    }


    protected PropertyValueService getPropertyValueService()
    {
        return this.propertyValueService;
    }


    @Required
    public void setPropertyValueService(PropertyValueService propertyValueService)
    {
        this.propertyValueService = propertyValueService;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
