package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.productcockpit.dao.AbstractDao;

public class LeafNode extends MacFinderTreeNodeAbstract
{
    private AbstractDao categoryDao;


    public AbstractDao getDao()
    {
        return this.categoryDao;
    }
}
