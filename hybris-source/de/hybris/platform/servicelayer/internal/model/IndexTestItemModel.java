package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class IndexTestItemModel extends ItemModel
{
    public static final String _TYPECODE = "IndexTestItem";
    public static final String COLUMN1 = "column1";
    public static final String COLUMN2 = "column2";
    public static final String COLUMN3 = "column3";
    public static final String COLUMN4 = "column4";
    public static final String COLUMN5 = "column5";


    public IndexTestItemModel()
    {
    }


    public IndexTestItemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IndexTestItemModel(Short _column1, Short _column2, Short _column3, Short _column4, Short _column5)
    {
        setColumn1(_column1);
        setColumn2(_column2);
        setColumn3(_column3);
        setColumn4(_column4);
        setColumn5(_column5);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IndexTestItemModel(Short _column1, Short _column2, Short _column3, Short _column4, Short _column5, ItemModel _owner)
    {
        setColumn1(_column1);
        setColumn2(_column2);
        setColumn3(_column3);
        setColumn4(_column4);
        setColumn5(_column5);
        setOwner(_owner);
    }


    @Accessor(qualifier = "column1", type = Accessor.Type.GETTER)
    public Short getColumn1()
    {
        return (Short)getPersistenceContext().getPropertyValue("column1");
    }


    @Accessor(qualifier = "column2", type = Accessor.Type.GETTER)
    public Short getColumn2()
    {
        return (Short)getPersistenceContext().getPropertyValue("column2");
    }


    @Accessor(qualifier = "column3", type = Accessor.Type.GETTER)
    public Short getColumn3()
    {
        return (Short)getPersistenceContext().getPropertyValue("column3");
    }


    @Accessor(qualifier = "column4", type = Accessor.Type.GETTER)
    public Short getColumn4()
    {
        return (Short)getPersistenceContext().getPropertyValue("column4");
    }


    @Accessor(qualifier = "column5", type = Accessor.Type.GETTER)
    public Short getColumn5()
    {
        return (Short)getPersistenceContext().getPropertyValue("column5");
    }


    @Accessor(qualifier = "column1", type = Accessor.Type.SETTER)
    public void setColumn1(Short value)
    {
        getPersistenceContext().setPropertyValue("column1", value);
    }


    @Accessor(qualifier = "column2", type = Accessor.Type.SETTER)
    public void setColumn2(Short value)
    {
        getPersistenceContext().setPropertyValue("column2", value);
    }


    @Accessor(qualifier = "column3", type = Accessor.Type.SETTER)
    public void setColumn3(Short value)
    {
        getPersistenceContext().setPropertyValue("column3", value);
    }


    @Accessor(qualifier = "column4", type = Accessor.Type.SETTER)
    public void setColumn4(Short value)
    {
        getPersistenceContext().setPropertyValue("column4", value);
    }


    @Accessor(qualifier = "column5", type = Accessor.Type.SETTER)
    public void setColumn5(Short value)
    {
        getPersistenceContext().setPropertyValue("column5", value);
    }
}
