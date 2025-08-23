package de.hybris.platform.adaptivesearchbackoffice.widgets.searchprofilecontext;

import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Required;

public class SearchProfileInfoModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String searchProfileLabel;
    private AsSearchConfigurationInfoData searchConfigurationInfo;


    public String getSearchProfileLabel()
    {
        return this.searchProfileLabel;
    }


    @Required
    public void setSearchProfileLabel(String searchProfileLabel)
    {
        this.searchProfileLabel = searchProfileLabel;
    }


    public AsSearchConfigurationInfoData getSearchConfigurationInfo()
    {
        return this.searchConfigurationInfo;
    }


    @Required
    public void setSearchConfigurationInfo(AsSearchConfigurationInfoData searchConfigurationInfo)
    {
        this.searchConfigurationInfo = searchConfigurationInfo;
    }
}
