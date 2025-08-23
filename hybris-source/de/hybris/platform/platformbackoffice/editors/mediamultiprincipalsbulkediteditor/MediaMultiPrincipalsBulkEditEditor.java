package de.hybris.platform.platformbackoffice.editors.mediamultiprincipalsbulkediteditor;

import com.hybris.backoffice.bulkedit.BulkEditHandler;
import com.hybris.backoffice.bulkedit.DynamicAttributeBulkEditHandler;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.platformbackoffice.editors.mediamultiprincipalseditor.MediaMultiPrincipalsEditor;
import java.util.Collection;
import org.zkoss.zk.ui.Component;

public class MediaMultiPrincipalsBulkEditEditor<T> extends MediaMultiPrincipalsEditor<T> implements DynamicAttributeBulkEditHandler
{
    protected static final String BULK_EDIT_MODEL_PREFIX = "bulkEditForm.templateObject";
    protected static final String BULK_EDIT_HANDLER_BEAN_ID = "defaultBulkEditHandler";


    public void render(Component parent, EditorContext<Collection<T>> context, EditorListener<Collection<T>> listener)
    {
        super.render(parent, context, listener);
        registerBulkEditHandler(context);
    }


    public boolean canHandle(String qualifier)
    {
        if(getEditorContext() != null)
        {
            return getEditorContext().getParameter("editorProperty")
                            .equals(String.format("%s.%s", new Object[] {"bulkEditForm.templateObject", qualifier}));
        }
        return false;
    }


    public Collection getSelectedItems()
    {
        return this.selectedItems;
    }


    protected void registerBulkEditHandler(EditorContext<Collection<T>> context)
    {
        if(context.getParameter("editorModelPrefix").equals("bulkEditForm.templateObject"))
        {
            BulkEditHandler bulkEditHandler = (BulkEditHandler)BackofficeSpringUtil.getBean("defaultBulkEditHandler");
            if(bulkEditHandler != null)
            {
                bulkEditHandler.registerDynamicAttributeBulkEditHandler(this);
            }
        }
    }
}
