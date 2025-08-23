package de.hybris.platform.productcockpit.services.catalog.impl;

import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.core.Registry;
import de.hybris.platform.productcockpit.model.macfinder.node.AdditionalNodesContainer;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.services.catalog.ConnectedItemService;
import java.util.ArrayList;
import java.util.List;

public class ConnectedItemServiceImpl extends AbstractServiceImpl implements ConnectedItemService
{
    public List<MacFinderTreeNode> getAllKindConnectedItems()
    {
        return new ArrayList<>((
                        (AdditionalNodesContainer)Registry.getApplicationContext().getBean("additionalNodesContainer"))
                        .getAvailableConnectedItems());
    }
}
