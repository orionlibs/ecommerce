package de.hybris.platform.cockpit.components.editorarea.export;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;

public class ExportEditorArea2PdfAction extends DefaultExportEditorAreaAction
{
    private static final String UNAVAILABE_IMAGE_URI = "/cockpit/images/icon_func_get_pdf_unavailable.png";


    protected String getFileName(TypedObject item)
    {
        if(getFileName() == null)
        {
            Object obj = item.getObject();
            if(obj instanceof ProductModel)
            {
                return ((ProductModel)obj).getCode() + ".pdf";
            }
            return TypeTools.getValueAsString(UISessionUtils.getCurrentSession().getLabelService(), item);
        }
        return getFileName();
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            context.setItem(selectedItems.iterator().next());
            ret = getEventListener(context);
        }
        return ret;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            return getImageURI(context);
        }
        return "/cockpit/images/icon_func_get_pdf_unavailable.png";
    }
}
