package de.hybris.platform.cockpit.components.editorarea.export;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.product.ProductModel;

public class ExportEditorArea2XlsAction extends DefaultExportEditorAreaAction
{
    protected String getFileName(TypedObject item)
    {
        if(getFileName() == null)
        {
            Object obj = item.getObject();
            if(obj instanceof ProductModel)
            {
                return ((ProductModel)obj).getCode() + ".xls";
            }
            return TypeTools.getValueAsString(UISessionUtils.getCurrentSession().getLabelService(), item);
        }
        return getFileName();
    }
}
