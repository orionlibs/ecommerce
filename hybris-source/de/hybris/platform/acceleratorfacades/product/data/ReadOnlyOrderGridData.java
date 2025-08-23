package de.hybris.platform.acceleratorfacades.product.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class ReadOnlyOrderGridData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, String> dimensionHeaderMap;
    private Set<LeafDimensionData> leafDimensionDataSet;
    private ProductData product;


    public void setDimensionHeaderMap(Map<String, String> dimensionHeaderMap)
    {
        this.dimensionHeaderMap = dimensionHeaderMap;
    }


    public Map<String, String> getDimensionHeaderMap()
    {
        return this.dimensionHeaderMap;
    }


    public void setLeafDimensionDataSet(Set<LeafDimensionData> leafDimensionDataSet)
    {
        this.leafDimensionDataSet = leafDimensionDataSet;
    }


    public Set<LeafDimensionData> getLeafDimensionDataSet()
    {
        return this.leafDimensionDataSet;
    }


    public void setProduct(ProductData product)
    {
        this.product = product;
    }


    public ProductData getProduct()
    {
        return this.product;
    }
}
