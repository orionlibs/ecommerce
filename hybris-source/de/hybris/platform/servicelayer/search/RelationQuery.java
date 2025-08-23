package de.hybris.platform.servicelayer.search;

import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.List;

public class RelationQuery
{
    private String attribute;
    private int start;
    private final int count;
    private ItemModel model;
    private final List<OrderSpec> orderSpecs = new ArrayList<>();


    public RelationQuery(int start, int count) throws RelationQueryException
    {
        this.start = start;
        this.count = count;
    }


    public RelationQuery(ItemModel model, String attribute, int start, int count)
    {
        this(start, count);
        this.model = model;
        this.attribute = attribute;
    }


    public int getStart()
    {
        return this.start;
    }


    public void setStart(int start)
    {
        this.start = start;
    }


    public int getCount()
    {
        return this.count;
    }


    public ItemModel getModel()
    {
        return this.model;
    }


    public void setModel(ItemModel model)
    {
        this.model = model;
    }


    public String getAttribute()
    {
        return this.attribute;
    }


    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }


    public void addOrder(String attribute, ORDERING ascending) throws RelationQueryException
    {
        this.orderSpecs.add(new OrderSpec(this, attribute, ascending));
    }


    public OrderSpec getOrderSpec(int number)
    {
        return this.orderSpecs.get(number);
    }


    public int getOrderCount()
    {
        return this.orderSpecs.size();
    }
}
