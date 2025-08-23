package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionEditor;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionUIEditorMedia;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

public class CelumMediaCollectionUIEditor extends CollectionUIEditorMedia
{
    private static final Logger LOG = LoggerFactory.getLogger(CelumMediaCollectionUIEditor.class);
    private CelumMediaEditorBase celumMediaEditorBase;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        this.celumMediaEditorBase = new CelumMediaEditorBase(parameters);
        Div viewRootComponent = new Div();
        viewRootComponent.setWidth("100%");
        viewRootComponent.setHeight("100%");
        renderEditorContent((HtmlBasedComponent)viewRootComponent, initialValue, parameters, listener);
        return (HtmlBasedComponent)viewRootComponent;
    }


    public CelumMediaEditorBase getCelumMediaEditorBase()
    {
        return this.celumMediaEditorBase;
    }


    public CollectionEditor createCollectionEditor(EditorListener listener, AdditionalReferenceEditorListener additionalListener)
    {
        return (CollectionEditor)new Object(this, listener, additionalListener);
    }


    protected void renderEditorContent(HtmlBasedComponent parent, Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Hbox hbox;
        parent.getChildren().clear();
        HtmlBasedComponent superView = super.createViewComponent(initialValue, parameters, listener);
        if(getCelumMediaEditorBase().getCockpitCelumDelegate().isCelumAvailable())
        {
            try
            {
                String link = getCelumMediaEditorBase().getLink(parameters, isEditable());
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
                HtmlBasedComponent view = superView;
            }
            catch(Exception e)
            {
                LOG.error("Deactivating Celum functionality for property '" + parameters
                                .get("attributeQualifier") + "' since it will not work correctly.", e);
                HtmlBasedComponent view = superView;
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
