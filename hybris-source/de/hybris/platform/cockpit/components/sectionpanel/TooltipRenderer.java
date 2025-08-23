package de.hybris.platform.cockpit.components.sectionpanel;

import de.hybris.platform.core.model.ItemModel;
import org.zkoss.zul.Popup;

public interface TooltipRenderer
{
    Popup renderItemTooltip(ItemModel paramItemModel);
}
