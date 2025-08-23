package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.commerceservices.organization.utils.OrgUtils;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrgRolesEditorSearchFacade implements ReferenceEditorSearchFacade
{
    private static final int DEFAULT_PAGE_SIZE = 5;
    private UserService userService;


    public Pageable search(SearchQueryData searchQueryData)
    {
        List<UserGroupModel> orgRoles = new ArrayList<>();
        for(String uid : OrgUtils.getRoleUids())
        {
            orgRoles.add(getUserService().getUserGroupForUID(uid));
        }
        return (Pageable)new PageableList(orgRoles, 5);
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
