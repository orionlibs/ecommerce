package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleMediaReferenceUIEditor;
import de.hybris.platform.cockpit.services.celum.CockpitCelumService;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import de.hybris.platform.cockpit.services.celum.impl.DefaultCockpitCelumDelegate;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Toolbarbutton;

public class CelumSimpleMediaReferenceUIEditor extends DefaultSimpleMediaReferenceUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(CelumSimpleMediaReferenceUIEditor.class);
    @Deprecated
    public static final String CELUM_MEDIA_FORMAT_KEY = "celumMediaFormat";
    @Deprecated
    protected static final String CONTEXT_PATH = "contextPath";
    @Deprecated
    protected static final String EDITOR_CONTEXT_OBJECT = "parentObject";
    @Deprecated
    protected static final String OPEN_CELUM_EDITOR_IMG = "cockpit/images/open_celum_editor.gif";
    @Deprecated
    protected static final String OPEN_CELUM_EDITOR_NOSYNC_IMG = "cockpit/images/open_celum_editor_nosync.gif";
    private static final String CANCEL_BUTTON_CONTAINER = "cancelButtonContainer";
    protected CockpitCelumService celumService = null;
    private CelumMediaEditorBase celumMediaEditorBase;
    private EditorListener listener = null;
    private final transient HtmlBasedComponent viewRootComponent = (HtmlBasedComponent)new Div();


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        this.celumMediaEditorBase = new CelumMediaEditorBase(parameters);
        this.listener = listener;
        this.viewRootComponent.setWidth("100%");
        this.viewRootComponent.setHeight("100%");
        renderEditorContent(this.viewRootComponent, initialValue, parameters, listener);
        return this.viewRootComponent;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        try
        {
            AbstractUIEditor.CancelButtonContainer cancelContainer = (AbstractUIEditor.CancelButtonContainer)rootEditorComponent.getAttribute("cancelButtonContainer");
            if(isEditable())
            {
                cancelContainer.showButton(true);
            }
            SimpleReferenceSelector element = (SimpleReferenceSelector)cancelContainer.getContent();
            if(this.initialInputString != null)
            {
                this.simpleSelector.setInitString(this.initialInputString);
            }
            element.setFocus(true);
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.error("Could not set focus: ", e);
            }
        }
    }


    @Deprecated
    protected TypedObject getAssociatedItem()
    {
        return getCelumMediaEditorBase().getAssociatedItem();
    }


    @Deprecated
    protected String getCatalogVersionPK()
    {
        return getCelumMediaEditorBase().getCatalogVersionPK();
    }


    @Deprecated
    protected Map<String, String> getCelumParameters(Map<String, ? extends Object> parameters) throws IllegalStateException, CelumNotAvailableException
    {
        return getCelumMediaEditorBase().getCelumParameters(parameters);
    }


    @Deprecated
    protected DefaultCockpitCelumDelegate getCockpitCelumDelegate()
    {
        return getCelumMediaEditorBase().getCockpitCelumDelegate();
    }


    @Deprecated
    protected String getLink(Map<String, ? extends Object> parameters) throws CelumNotAvailableException, IllegalStateException
    {
        return getCelumMediaEditorBase().getLink(parameters, isEditable());
    }


    @Deprecated
    protected boolean isSynchronized() throws CelumNotAvailableException
    {
        Object editorValue = getModel().getValue();
        return getCelumMediaEditorBase().isSynchronized(editorValue);
    }


    protected void renderEditorContent(HtmlBasedComponent parent, Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Hbox hbox;
        parent.getChildren().clear();
        HtmlBasedComponent view = null;
        HtmlBasedComponent superView = super.createViewComponent(initialValue, parameters, listener);
        if(superView instanceof AbstractUIEditor.CancelButtonContainer)
        {
            this.viewRootComponent.setAttribute("cancelButtonContainer", superView);
        }
        if(getCelumMediaEditorBase().getCockpitCelumDelegate().isCelumAvailable())
        {
            try
            {
                String link = "";
                link = getCelumMediaEditorBase().getLink(parameters, isEditable());
                Hbox hbox1 = getCelumMediaEditorBase().renderCelumButton(superView, link, true, isEditable());
                if(!getCelumMediaEditorBase().isSynchronized(getModel().getValue()))
                {
                    Toolbarbutton synchBtn = new Toolbarbutton("", "cockpit/images/open_celum_editor_nosync.gif");
                    synchBtn.setTooltiptext(Labels.getLabel("editor.button.celumeditor.synch.tooltip"));
                    hbox1.setWidths("none, 20px, 20px");
                    hbox1.appendChild((Component)synchBtn);
                    Object editorValue = getModel().getValue();
                    if(editorValue instanceof TypedObject)
                    {
                        getCelumMediaEditorBase().getCockpitCelumDelegate().createSynchPopup((Component)synchBtn, (TypedObject)editorValue, (EditorListener)new Object(this, listener));
                    }
                }
                hbox = hbox1;
            }
            catch(CelumNotAvailableException cnae)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Deactivating Celum functionality for property '" + parameters
                                    .get("attributeQualifier") + "' since it will not work correctly. Reason: '" + cnae
                                    .getMessage() + "'.");
                }
                hbox = getCelumMediaEditorBase().renderCelumButton(superView, null, false, isEditable());
            }
            catch(IllegalStateException ise)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Deactivating Celum functionality for property '" + parameters
                                    .get("attributeQualifier") + "' since it will not work correctly. Reason: '" + ise
                                    .getMessage() + "'.");
                }
                view = superView;
            }
            catch(Exception e)
            {
                LOG.error("Deactivating Celum functionality for property '" + parameters
                                .get("attributeQualifier") + "' since it will not work correctly.", e);
                view = superView;
            }
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.info("Deactivating Celum functionality for property '" + parameters
                                .get("attributeQualifier") + "' since it will not work correctly. Reason: Celum not available.");
            }
            hbox = getCelumMediaEditorBase().renderCelumButton(superView, null, false, isEditable());
        }
        if(hbox != null)
        {
            parent.appendChild((Component)hbox);
        }
    }


    private CelumMediaEditorBase getCelumMediaEditorBase()
    {
        return this.celumMediaEditorBase;
    }
}
