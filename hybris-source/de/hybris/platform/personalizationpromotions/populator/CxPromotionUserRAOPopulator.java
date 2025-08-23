package de.hybris.platform.personalizationpromotions.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationpromotions.data.CxPromotionActionResult;
import de.hybris.platform.personalizationpromotions.rao.CxPromotionActionResultRAO;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class CxPromotionUserRAOPopulator implements Populator<UserModel, UserRAO>
{
    private CxService cxService;
    private Converter<CxPromotionActionResult, CxPromotionActionResultRAO> cxPromotionActionResultRAOConverter;


    public void populate(UserModel source, UserRAO target)
    {
        List<CxPromotionActionResult> promotionActionResults = (List<CxPromotionActionResult>)this.cxService.getActionResultsFromSession(source).stream().filter(ar -> ar instanceof CxPromotionActionResult).map(ar -> (CxPromotionActionResult)ar).collect(Collectors.toList());
        target.setCxPromotionActionResults(Converters.convertAll(promotionActionResults, this.cxPromotionActionResultRAOConverter));
    }


    protected CxService getCxService()
    {
        return this.cxService;
    }


    protected Converter<CxPromotionActionResult, CxPromotionActionResultRAO> getCxPromotionActionResultRAOConverter()
    {
        return this.cxPromotionActionResultRAOConverter;
    }


    @Required
    public void setCxService(CxService cxService)
    {
        this.cxService = cxService;
    }


    @Required
    public void setCxPromotionActionResultRAOConverter(Converter<CxPromotionActionResult, CxPromotionActionResultRAO> cxPromotionActionResultRAOConverter)
    {
        this.cxPromotionActionResultRAOConverter = cxPromotionActionResultRAOConverter;
    }
}
