package de.hybris.platform.adaptivesearchbackoffice.editors.facets;

import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class FacetValueModel
{
    private boolean sticky;
    private AsFacetValueData data;
    private AbstractAsFacetValueConfigurationModel model;


    public boolean isSticky()
    {
        return this.sticky;
    }


    public void setSticky(boolean sticky)
    {
        this.sticky = sticky;
    }


    public AsFacetValueData getData()
    {
        return this.data;
    }


    public void setData(AsFacetValueData data)
    {
        this.data = data;
    }


    public AbstractAsFacetValueConfigurationModel getModel()
    {
        return this.model;
    }


    public void setModel(AbstractAsFacetValueConfigurationModel model)
    {
        this.model = model;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        FacetValueModel that = (FacetValueModel)obj;
        return (new EqualsBuilder())
                        .append(this.data.getValue(), that.data.getValue())
                        .append(this.sticky, that.sticky)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {Boolean.valueOf(this.sticky), this.data.getValue()});
    }
}
