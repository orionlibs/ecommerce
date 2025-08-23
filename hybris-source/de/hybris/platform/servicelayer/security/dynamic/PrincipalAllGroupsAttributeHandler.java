package de.hybris.platform.servicelayer.security.dynamic;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

public class PrincipalAllGroupsAttributeHandler implements DynamicAttributeHandler<Set<PrincipalGroupModel>, PrincipalModel>
{
    public Set<PrincipalGroupModel> get(PrincipalModel model)
    {
        Set<PrincipalGroupModel> ret = new HashSet<>();
        Set<PrincipalGroupModel> groups = model.getGroups();
        while(CollectionUtils.isNotEmpty(groups))
        {
            Set<PrincipalGroupModel> nextGroups = new HashSet<>();
            for(PrincipalGroupModel group : groups)
            {
                ret.add(group);
                nextGroups.addAll(CollectionUtils.emptyIfNull(group.getGroups()));
            }
            nextGroups.removeAll(ret);
            groups = nextGroups;
        }
        return ret;
    }


    public void set(PrincipalModel model, Set<PrincipalGroupModel> principalGroupModels)
    {
        throw new UnsupportedOperationException();
    }
}
