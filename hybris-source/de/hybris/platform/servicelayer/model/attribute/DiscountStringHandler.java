package de.hybris.platform.servicelayer.model.attribute;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.AbstractDiscountRowModel;
import de.hybris.platform.servicelayer.DiscountValueConverter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.DiscountValue;
import java.text.NumberFormat;
import org.springframework.beans.factory.annotation.Required;

public class DiscountStringHandler implements DynamicAttributeHandler<String, AbstractDiscountRowModel>
{
    private FormatFactory formatFactory;
    private CommonI18NService commonI18NService;
    private SessionService sessionService;
    private final DiscountValueConverter discountConverter = new DiscountValueConverter();


    public String get(AbstractDiscountRowModel model)
    {
        DiscountValue discountValue = this.discountConverter.discountToValue(model);
        if(discountValue.isAbsolute())
        {
            CurrencyModel currency = this.commonI18NService.getCurrency(discountValue.getCurrencyIsoCode());
            NumberFormat currencyFormat = (NumberFormat)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, currency));
            return currencyFormat.format(discountValue.getValue());
        }
        return this.formatFactory.createPercentFormat().format(discountValue.getValue() / 100.0D);
    }


    public void set(AbstractDiscountRowModel model, String aBoolean)
    {
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setFormatFactory(FormatFactory formatFactory)
    {
        this.formatFactory = formatFactory;
    }
}
