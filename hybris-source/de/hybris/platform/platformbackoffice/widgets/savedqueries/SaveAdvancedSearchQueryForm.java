package de.hybris.platform.platformbackoffice.widgets.savedqueries;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import de.hybris.platform.core.model.user.UserGroupModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.Locales;

public class SaveAdvancedSearchQueryForm
{
    private Map<Locale, String> queryName;
    private List<UserGroupModel> userGroups;
    private AdvancedSearchData advancedSearchData;


    public Map<Locale, String> getQueryName()
    {
        return this.queryName;
    }


    public void setQueryName(Map<Locale, String> queryName)
    {
        this.queryName = queryName;
    }


    public List<UserGroupModel> getUserGroups()
    {
        return this.userGroups;
    }


    public void setUserGroups(List<UserGroupModel> userGroups)
    {
        this.userGroups = userGroups;
    }


    public AdvancedSearchData getAdvancedSearchData()
    {
        return this.advancedSearchData;
    }


    public void setAdvancedSearchData(AdvancedSearchData advancedSearchData)
    {
        this.advancedSearchData = advancedSearchData;
    }


    public boolean hasQueryNameInAnyLocale()
    {
        if(this.queryName != null)
        {
            return StringUtils.isNotBlank(this.queryName.get(Locales.getCurrent()));
        }
        return false;
    }
}
