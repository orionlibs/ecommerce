package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.samlsinglesignon.model.SamlUserGroupModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSamlUserGroupDAO implements SamlUserGroupDAO
{
    protected static final String PARAM_NAME = "samlUserGroup";
    protected static final String QUERY_STRING = "select {PK} from {SamlUserGroup} where {samlUserGroup} = ?samlUserGroup";
    private FlexibleSearchService flexibleSearchService;


    public Optional<SamlUserGroupModel> findSamlUserGroup(String samlUserGroup)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {PK} from {SamlUserGroup} where {samlUserGroup} = ?samlUserGroup");
        query.addQueryParameter("samlUserGroup", samlUserGroup);
        List<SamlUserGroupModel> searchResult = this.flexibleSearchService.search(query).getResult();
        if(CollectionUtils.isNotEmpty(searchResult))
        {
            return Optional.of(searchResult.get(0));
        }
        return Optional.empty();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
