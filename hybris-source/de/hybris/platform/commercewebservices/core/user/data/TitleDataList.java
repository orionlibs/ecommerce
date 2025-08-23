package de.hybris.platform.commercewebservices.core.user.data;

import de.hybris.platform.commercefacades.user.data.TitleData;
import java.io.Serializable;
import java.util.List;

public class TitleDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<TitleData> titles;


    public void setTitles(List<TitleData> titles)
    {
        this.titles = titles;
    }


    public List<TitleData> getTitles()
    {
        return this.titles;
    }
}
