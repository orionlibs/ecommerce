package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultMediaReferenceCollectionUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.impl.MediaReferenceCollectionEditor;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

@Deprecated
public class CelumMediaReferenceCollectionUIEditor extends DefaultMediaReferenceCollectionUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(CelumMediaReferenceCollectionUIEditor.class);
    private CelumMediaEditorBase celumMediaEditorBase;
    private EditorListener listener;
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


    public CelumMediaEditorBase getCelumMediaEditorBase()
    {
        return this.celumMediaEditorBase;
    }


    protected MediaReferenceCollectionEditor getCollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        return (MediaReferenceCollectionEditor)new CelumMediaReferenceCollectionEditor(editorListener, additionalListener, this.celumMediaEditorBase);
    }


    protected void renderEditorContent(HtmlBasedComponent parent, Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Hbox hbox;
        parent.getChildren().clear();
        HtmlBasedComponent view = null;
        HtmlBasedComponent superView = super.createViewComponent(initialValue, parameters, listener);
        if(getCelumMediaEditorBase().getCockpitCelumDelegate().isCelumAvailable())
        {
            try
            {
                String link = "";
                link = getCelumMediaEditorBase().getLink(parameters, isEditable());
                Hbox hbox1 = getCelumMediaEditorBase().renderCelumButton(superView, link, true, isEditable());
                hbox1.setAlign("bottom");
                hbox = hbox1;
            }
            catch(CelumNotAvailableException cnae)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Deactivating Celum functionality for property '" + parameters.get("attributeQualifier") + "' since it will not work correctly. Reason: '" + cnae
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
}
