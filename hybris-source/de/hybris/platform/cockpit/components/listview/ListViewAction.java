package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public interface ListViewAction
{
    public static final String AFFECTED_ITEMS_KEY = "affectedItems";


    Context createContext(TableModel paramTableModel, TypedObject paramTypedObject, ColumnDescriptor paramColumnDescriptor);


    Context createContext(ListComponentModel paramListComponentModel, TypedObject paramTypedObject);


    String getImageURI(Context paramContext);


    EventListener getEventListener(Context paramContext);


    Menupopup getPopup(Context paramContext);


    Menupopup getContextPopup(Context paramContext);


    String getTooltip(Context paramContext);


    String getMultiSelectImageURI(Context paramContext);


    EventListener getMultiSelectEventListener(Context paramContext);


    Menupopup getMultiSelectPopup(Context paramContext);


    String getStatusCode(Context paramContext);
}
