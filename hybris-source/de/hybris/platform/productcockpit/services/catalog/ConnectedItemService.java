package de.hybris.platform.productcockpit.services.catalog;

import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import java.util.List;

public interface ConnectedItemService
{
    List<MacFinderTreeNode> getAllKindConnectedItems();
}
