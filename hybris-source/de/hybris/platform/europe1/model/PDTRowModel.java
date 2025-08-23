package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;

public class PDTRowModel extends ItemModel
{
    public static final String _TYPECODE = "PDTRow";
    public static final String PRODUCT = "product";
    public static final String PG = "pg";
    public static final String PRODUCTMATCHQUALIFIER = "productMatchQualifier";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String DATERANGE = "dateRange";
    public static final String USER = "user";
    public static final String UG = "ug";
    public static final String USERMATCHQUALIFIER = "userMatchQualifier";
    public static final String PRODUCTID = "productId";


    public PDTRowModel()
    {
    }


    public PDTRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PDTRowModel(ItemModel _owner, HybrisEnumValue _pg, ProductModel _product, String _productId)
    {
        setOwner(_owner);
        setPg(_pg);
        setProduct(_product);
        setProductId(_productId);
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.GETTER)
    public StandardDateRange getDateRange()
    {
        return (StandardDateRange)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "dateRange");
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
    public Date getEndTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("endTime");
    }


    @Accessor(qualifier = "pg", type = Accessor.Type.GETTER)
    public HybrisEnumValue getPg()
    {
        return (HybrisEnumValue)getPersistenceContext().getPropertyValue("pg");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "productId", type = Accessor.Type.GETTER)
    public String getProductId()
    {
        return (String)getPersistenceContext().getPropertyValue("productId");
    }


    @Accessor(qualifier = "productMatchQualifier", type = Accessor.Type.GETTER)
    public Long getProductMatchQualifier()
    {
        return (Long)getPersistenceContext().getPropertyValue("productMatchQualifier");
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
    public Date getStartTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("startTime");
    }


    @Accessor(qualifier = "ug", type = Accessor.Type.GETTER)
    public HybrisEnumValue getUg()
    {
        return (HybrisEnumValue)getPersistenceContext().getPropertyValue("ug");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "userMatchQualifier", type = Accessor.Type.GETTER)
    public Long getUserMatchQualifier()
    {
        return (Long)getPersistenceContext().getPropertyValue("userMatchQualifier");
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.SETTER)
    public void setDateRange(StandardDateRange value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "dateRange", value);
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
    public void setEndTime(Date value)
    {
        getPersistenceContext().setPropertyValue("endTime", value);
    }


    @Accessor(qualifier = "pg", type = Accessor.Type.SETTER)
    public void setPg(HybrisEnumValue value)
    {
        getPersistenceContext().setPropertyValue("pg", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "productId", type = Accessor.Type.SETTER)
    public void setProductId(String value)
    {
        getPersistenceContext().setPropertyValue("productId", value);
    }


    @Accessor(qualifier = "productMatchQualifier", type = Accessor.Type.SETTER)
    public void setProductMatchQualifier(Long value)
    {
        getPersistenceContext().setPropertyValue("productMatchQualifier", value);
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
    public void setStartTime(Date value)
    {
        getPersistenceContext().setPropertyValue("startTime", value);
    }


    @Accessor(qualifier = "ug", type = Accessor.Type.SETTER)
    public void setUg(HybrisEnumValue value)
    {
        getPersistenceContext().setPropertyValue("ug", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }


    @Accessor(qualifier = "userMatchQualifier", type = Accessor.Type.SETTER)
    public void setUserMatchQualifier(Long value)
    {
        getPersistenceContext().setPropertyValue("userMatchQualifier", value);
    }
}
