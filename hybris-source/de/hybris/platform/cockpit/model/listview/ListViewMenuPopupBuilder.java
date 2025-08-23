package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.components.ColumnLayoutComponent;
import org.zkoss.zul.Menupopup;

public interface ListViewMenuPopupBuilder
{
    Menupopup buildStandardColumnMenuPopup(UIListView paramUIListView, ColumnDescriptor paramColumnDescriptor, int paramInt);


    Menupopup buildBackgroundColumnMenuPopup(UIListView paramUIListView, ColumnLayoutComponent paramColumnLayoutComponent);
}
