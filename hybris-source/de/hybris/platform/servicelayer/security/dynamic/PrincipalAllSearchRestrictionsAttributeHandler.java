package de.hybris.platform.servicelayer.security.dynamic;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

public class PrincipalAllSearchRestrictionsAttributeHandler extends AbstractDynamicAttributeHandler<Collection<SearchRestrictionModel>, PrincipalModel>
{
    public Collection<SearchRestrictionModel> get(PrincipalModel principal)
    {
        Collection<SearchRestrictionModel> principalRestrictions = CollectionUtils.emptyIfNull(principal
                        .getSearchRestrictions());
        Set<SearchRestrictionModel> searchRestrictions = Sets.newHashSet(principalRestrictions);
        Collection<PrincipalGroupModel> allGroups = CollectionUtils.emptyIfNull(principal.getAllGroups());
        for(PrincipalModel group : allGroups)
        {
            searchRestrictions.addAll(CollectionUtils.emptyIfNull(group.getSearchRestrictions()));
        }
        return searchRestrictions;
    }
}
