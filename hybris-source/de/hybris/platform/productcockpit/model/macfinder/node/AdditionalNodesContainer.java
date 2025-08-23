package de.hybris.platform.productcockpit.model.macfinder.node;

import java.util.List;

public class AdditionalNodesContainer
{
    private List<MacFinderTreeNodeAbstract> availableConnectedItems;


    public List<MacFinderTreeNodeAbstract> getAvailableConnectedItems()
    {
        return this.availableConnectedItems;
    }


    public void setAvailableConnectedItems(List<MacFinderTreeNodeAbstract> availableConnectedItems)
    {
        this.availableConnectedItems = availableConnectedItems;
    }
}
