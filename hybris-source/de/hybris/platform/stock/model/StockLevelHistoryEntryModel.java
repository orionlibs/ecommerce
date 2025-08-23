package de.hybris.platform.stock.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.StockLevelUpdateType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class StockLevelHistoryEntryModel extends ItemModel
{
    public static final String _TYPECODE = "StockLevelHistoryEntry";
    public static final String _STOCKLEVELSTOCKLEVELHISTORYENTRYRELATION = "StockLevelStockLevelHistoryEntryRelation";
    public static final String UPDATEDATE = "updateDate";
    public static final String ACTUAL = "actual";
    public static final String RESERVED = "reserved";
    public static final String UPDATETYPE = "updateType";
    public static final String COMMENT = "comment";
    public static final String STOCKLEVELPOS = "stockLevelPOS";
    public static final String STOCKLEVEL = "stockLevel";


    public StockLevelHistoryEntryModel()
    {
    }


    public StockLevelHistoryEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StockLevelHistoryEntryModel(int _actual, int _reserved, StockLevelModel _stockLevel, Date _updateDate)
    {
        setActual(_actual);
        setReserved(_reserved);
        setStockLevel(_stockLevel);
        setUpdateDate(_updateDate);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StockLevelHistoryEntryModel(int _actual, ItemModel _owner, int _reserved, StockLevelModel _stockLevel, Date _updateDate)
    {
        setActual(_actual);
        setOwner(_owner);
        setReserved(_reserved);
        setStockLevel(_stockLevel);
        setUpdateDate(_updateDate);
    }


    @Accessor(qualifier = "actual", type = Accessor.Type.GETTER)
    public int getActual()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("actual"));
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public String getComment()
    {
        return (String)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "reserved", type = Accessor.Type.GETTER)
    public int getReserved()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("reserved"));
    }


    @Accessor(qualifier = "stockLevel", type = Accessor.Type.GETTER)
    public StockLevelModel getStockLevel()
    {
        return (StockLevelModel)getPersistenceContext().getPropertyValue("stockLevel");
    }


    @Accessor(qualifier = "updateDate", type = Accessor.Type.GETTER)
    public Date getUpdateDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("updateDate");
    }


    @Accessor(qualifier = "updateType", type = Accessor.Type.GETTER)
    public StockLevelUpdateType getUpdateType()
    {
        return (StockLevelUpdateType)getPersistenceContext().getPropertyValue("updateType");
    }


    @Accessor(qualifier = "actual", type = Accessor.Type.SETTER)
    public void setActual(int value)
    {
        getPersistenceContext().setPropertyValue("actual", toObject(value));
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(String value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "reserved", type = Accessor.Type.SETTER)
    public void setReserved(int value)
    {
        getPersistenceContext().setPropertyValue("reserved", toObject(value));
    }


    @Accessor(qualifier = "stockLevel", type = Accessor.Type.SETTER)
    public void setStockLevel(StockLevelModel value)
    {
        getPersistenceContext().setPropertyValue("stockLevel", value);
    }


    @Accessor(qualifier = "updateDate", type = Accessor.Type.SETTER)
    public void setUpdateDate(Date value)
    {
        getPersistenceContext().setPropertyValue("updateDate", value);
    }


    @Accessor(qualifier = "updateType", type = Accessor.Type.SETTER)
    public void setUpdateType(StockLevelUpdateType value)
    {
        getPersistenceContext().setPropertyValue("updateType", value);
    }
}
