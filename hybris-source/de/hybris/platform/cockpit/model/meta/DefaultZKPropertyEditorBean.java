package de.hybris.platform.cockpit.model.meta;

import de.hybris.platform.cockpit.model.meta.impl.DefaultPropertyEditorBean;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collections;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.impl.InputElement;

public class DefaultZKPropertyEditorBean extends DefaultPropertyEditorBean implements ZKPropertyEditorBean
{
    public static final String INVALID_SCLASS = "invalid";
    public static final String READONLY_SCLASS = "readOnly";
    private Component view;
    private AnnotateDataBinder binder;


    public DefaultZKPropertyEditorBean(DefaultPropertyEditorDescriptor descriptor)
    {
        super((PropertyEditorDescriptor)descriptor);
    }


    protected AnnotateDataBinder getBinder()
    {
        if(this.binder == null)
        {
            throw new IllegalStateException("binder not yet created - call getView() before");
        }
        return this.binder;
    }


    public final Component getOrCreateViewComponent(Page page)
    {
        Component oldView = this.view;
        this.view = createView(page);
        if(oldView != null)
        {
            ((HtmlBasedComponent)this.view).setSclass(((HtmlBasedComponent)oldView).getSclass());
        }
        return this.view;
    }


    protected Map<String, Object> getViewCreationArgs()
    {
        return Collections.singletonMap("editorBean", this);
    }


    protected Map<String, Object> getViewLocalVariables()
    {
        return Collections.singletonMap("editorBean", this);
    }


    protected void initView(Component view)
    {
    }


    protected Component createView(Page page)
    {
        Component rootComp = null;
        return rootComp;
    }


    protected void updateEditable(boolean newEditable)
    {
        super.updateEditable(newEditable);
        if(this.view instanceof HtmlBasedComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)this.view, "readOnly", !newEditable);
            if(this.view instanceof InputElement)
            {
                ((InputElement)this.view).setDisabled(!newEditable);
            }
        }
    }


    protected void updateInvalid(boolean newInvalid)
    {
        super.updateInvalid(newInvalid);
        if(this.view instanceof HtmlBasedComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)this.view, "invalid", newInvalid);
        }
    }
}
