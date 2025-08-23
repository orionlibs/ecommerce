package de.hybris.platform.oauth2;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;

@SystemSetup(extension = "oauth2")
public class CleanupAccessToken
{
    private static final Logger log = LoggerFactory.getLogger(CleanupAccessToken.class);
    private TypeService typeService;
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    @Value("${oauth2.cleanupAccessToken.maxRows:100}")
    private int maxRows = 100;


    @SystemSetup(extension = "oauth2", type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void clearAccessTokensFrom60Version()
    {
        try
        {
            this.typeService.getAttributeDescriptor("OAuthAccessToken", "clientId");
        }
        catch(UnknownIdentifierException e)
        {
            log.info("already >=6.1 schema version for oauth2, no need to cleanup access tokens");
            return;
        }
        List<Object> result = null;
        log.debug("maxRows:{}", Integer.valueOf(this.maxRows));
        do
        {
            FlexibleSearchQuery query = new FlexibleSearchQuery(String.format("select {pk} from {%s} where {%s} is not null", new Object[] {"OAuthAccessToken", "clientId"}));
            query.setCount(this.maxRows);
            result = this.flexibleSearchService.search(query).getResult();
            this.modelService.removeAll(result);
            log.info("removed {} access tokens", Integer.valueOf(result.size()));
        }
        while(result.size() == this.maxRows);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setMaxRows(int maxRows)
    {
        this.maxRows = maxRows;
    }
}
