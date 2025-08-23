package de.hybris.platform.commercefacades.user.data;

import java.util.List;

public class CustomerListData extends UserGroupData
{
    private List<String> additionalColumnsKeys;
    private boolean searchBoxEnabled;


    public void setAdditionalColumnsKeys(List<String> additionalColumnsKeys)
    {
        this.additionalColumnsKeys = additionalColumnsKeys;
    }


    public List<String> getAdditionalColumnsKeys()
    {
        return this.additionalColumnsKeys;
    }


    public void setSearchBoxEnabled(boolean searchBoxEnabled)
    {
        this.searchBoxEnabled = searchBoxEnabled;
    }


    public boolean isSearchBoxEnabled()
    {
        return this.searchBoxEnabled;
    }
}
