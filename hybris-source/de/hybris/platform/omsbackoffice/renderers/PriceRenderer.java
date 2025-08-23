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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Listcell;

public class PriceRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(PriceRenderer.class);
    private static final int DEFAULT_NUMBER_OF_DIGITS = 2;
    private TypeFacade typeFacade;
    private PropertyValueService propertyValueService;
    private LabelService labelService;
    private PermissionFacade permissionFacade;
    private String myEntry;


    public void render(Listcell listcell, ListColumn columnConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        String qualifier = columnConfiguration.getQualifier();
        try
        {
            BigDecimal amountValue;
            DataType objectDataType = getTypeFacade().load(getMyEntry());
            if(objectDataType != null && !getPermissionFacade().canReadProperty(objectDataType.getCode(), qualifier))
            {
                return;
            }
            Object value = getPropertyValueService().readValue(object, qualifier);
            if(value == null)
            {
                return;
            }
            if(value instanceof Double)
            {
                amountValue = BigDecimal.valueOf(((Double)value).doubleValue());
            }
            else
            {
                amountValue = (BigDecimal)value;
            }
            BigDecimal entryAmount = amountValue.setScale(getDigitsNumber(object), 5);
            String price = getLabelService().getObjectLabel(entryAmount);
            if(StringUtils.isBlank(price))
            {
                price = value.toString();
            }
            listcell.setLabel(price);
        }
        catch(TypeNotFoundException e)
        {
            LOG.info("Could not render row.", (Throwable)e);
        }
    }


    protected int getDigitsNumber(Object object)
    {
        int digitsNumber = 2;
        if(object instanceof RefundEntryModel)
        {
            digitsNumber = ((RefundEntryModel)object).getOrderEntry().getOrder().getCurrency().getDigits().intValue();
        }
        else if(object instanceof AbstractOrderEntryModel)
        {
            digitsNumber = ((AbstractOrderEntryModel)object).getOrder().getCurrency().getDigits().intValue();
        }
        else if(object instanceof PaymentTransactionEntryModel)
        {
            digitsNumber = ((PaymentTransactionEntryModel)object).getPaymentTransaction().getOrder().getCurrency().getDigits().intValue();
        }
        return digitsNumber;
    }


    protected String getMyEntry()
    {
        return this.myEntry;
    }


    @Required
    public void setMyEntry(String myEntry)
    {
        this.myEntry = myEntry;
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
