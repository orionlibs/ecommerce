package de.hybris.platform.adaptivesearchbackoffice.editors.facets;

import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import java.util.List;
import org.zkoss.zul.SimpleListModel;

public class FacetValuesListModel extends SimpleListModel<AsFacetValueData>
{
    private final int stickyValuesSize;


    public FacetValuesListModel(List<? extends AsFacetValueData> values, int stickyValuesSize)
    {
        super(values);
        this.stickyValuesSize = stickyValuesSize;
    }


    public int getStickyValuesSize()
    {
        return this.stickyValuesSize;
    }
}
