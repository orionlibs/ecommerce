package de.hybris.platform.oauth2;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.oauth2.model.OAuthAuthorizationCodeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

public class AuthorizationCodeService extends RandomValueAuthorizationCodeServices
{
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;


    protected void store(String code, OAuth2Authentication authentication)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(code), "code must not be empty");
        Preconditions.checkArgument((authentication != null), "authentication must not be null");
        OAuthAuthorizationCodeModel model = (OAuthAuthorizationCodeModel)this.modelService.create(OAuthAuthorizationCodeModel.class);
        model.setCode(code);
        model.setAuthentication(SerializationUtils.serialize(authentication));
        this.modelService.save(model);
    }


    protected OAuth2Authentication remove(String code)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(code), "code must not be empty");
        OAuthAuthorizationCodeModel model = (OAuthAuthorizationCodeModel)this.flexibleSearchService.searchUnique(new FlexibleSearchQuery(
                        String.format("select {pk} from {%s} where {%s}= ?code", new Object[] {"OAuthAuthorizationCode", "code"}), (Map)ImmutableMap.of("code", code)));
        OAuth2Authentication authentication = (OAuth2Authentication)SerializationUtils.deserialize((byte[])model.getAuthentication());
        this.modelService.remove(model);
        return authentication;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
