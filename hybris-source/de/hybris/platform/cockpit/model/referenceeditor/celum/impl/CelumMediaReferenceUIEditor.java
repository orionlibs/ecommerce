package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultMediaReferenceUIEditor;
import de.hybris.platform.cockpit.services.celum.CockpitCelumService;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import de.hybris.platform.cockpit.services.celum.impl.DefaultCockpitCelumDelegate;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.A;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Toolbarbutton;

@Deprecated
public class CelumMediaReferenceUIEditor extends DefaultMediaReferenceUIEditor
{
    public static final String CELUM_MEDIA_FORMAT_KEY = "celumMediaFormat";
    protected static final String EDITOR_CONTEXT_OBJECT = "parentObject";
    protected static final String CONTEXT_PATH = "contextPath";
    protected static final String OPEN_CELUM_EDITOR_IMG = "cockpit/images/open_celum_editor.gif";
    protected static final String OPEN_CELUM_EDITOR_NOSYNC_IMG = "cockpit/images/open_celum_editor_nosync.gif";
    private static final Logger LOG = LoggerFactory.getLogger(CelumMediaReferenceUIEditor.class);
    protected CockpitCelumService celumService = null;
    private DefaultCockpitCelumDelegate celumDelegate = null;
    private final transient HtmlBasedComponent viewRootComponent = (HtmlBasedComponent)new Div();
    private final Map<String, Object> parameters = new HashMap<>();
    private EditorListener listener = null;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        if(parameters != null)
        {
            this.parameters.putAll(parameters);
        }
        this.listener = listener;
        this.viewRootComponent.setWidth("100%");
        this.viewRootComponent.setHeight("100%");
        renderEditorContent(this.viewRootComponent, initialValue, parameters, listener);
        return this.viewRootComponent;
    }


    protected void renderEditorContent(HtmlBasedComponent parent, Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        parent.getChildren().clear();
        HtmlBasedComponent view = null;
        HtmlBasedComponent superView = super.createViewComponent(initialValue, parameters, listener);
        if(getCockpitCelumDelegate().isCelumAvailable())
        {
            try
            {
                A celumBtn = new A();
                String link = "";
                link = getLink(parameters);
                celumBtn.setDynamicProperty("href", link);
                Image btnImg = new Image("cockpit/images/open_celum_editor.gif");
                celumBtn.appendChild((Component)btnImg);
                Hbox hbox = new Hbox();
                hbox.setAlign("top");
                hbox.setWidth("100%");
                hbox.setWidths("none, 20px");
                hbox.setStyle("table-layout: fixed");
                superView.setParent((Component)hbox);
                celumBtn.setParent((Component)hbox);
                btnImg.setTooltiptext(Labels.getLabel("editor.button.celumeditor.open.tooltip"));
                celumBtn.setSclass("celumBtn");
                if(!isSynchronized())
                {
                    Toolbarbutton synchBtn = new Toolbarbutton("", "cockpit/images/open_celum_editor_nosync.gif");
                    synchBtn.setTooltiptext(Labels.getLabel("editor.button.celumeditor.synch.tooltip"));
                    hbox.setWidths("none, 20px, 20px");
                    hbox.appendChild((Component)synchBtn);
                    Object editorValue = getModel().getValue();
                    if(editorValue instanceof TypedObject)
                    {
                        getCockpitCelumDelegate().createSynchPopup((Component)synchBtn, (TypedObject)editorValue, (EditorListener)new Object(this, listener));
                    }
                }
                Hbox hbox1 = hbox;
            }
            catch(CelumNotAvailableException cnae)
            {
                LOG.warn("Deactivating Celum functionality for property '" + parameters
                                .get("attributeQualifier") + "' since it will not work correctly. Reason: '" + cnae
                                .getMessage() + "'.");
                view = superView;
            }
            catch(IllegalStateException ise)
            {
                LOG.warn("Deactivating Celum functionality for property '" + parameters
                                .get("attributeQualifier") + "' since it will not work correctly. Reason: '" + ise
                                .getMessage() + "'.");
                view = superView;
            }
            catch(Exception e)
            {
                LOG.warn("Deactivating Celum functionality for property '" + parameters
                                .get("attributeQualifier") + "' since it will not work correctly.", e);
                view = superView;
            }
        }
        else
        {
            LOG.info("Deactivating Celum functionality for property '" + parameters.get("attributeQualifier") + "' since it will not work correctly. Reason: Celum not available.");
            view = superView;
        }
        if(view != null)
        {
            parent.appendChild((Component)view);
        }
    }


    protected boolean isSynchronized() throws CelumNotAvailableException
    {
        boolean synched = true;
        Object editorValue = getModel().getValue();
        if(editorValue instanceof TypedObject)
        {
            try
            {
                synched = getCockpitCelumDelegate().isSynchronized((TypedObject)editorValue);
            }
            catch(IllegalArgumentException iae)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Synchronization status could not be determined. Reason: '" + iae.getMessage() + "'.");
                }
            }
        }
        else
        {
            LOG.warn("Synchronization status could not be determined. Reason: Editor value not a TypedObject.");
        }
        return synched;
    }


    protected String getLink(Map<String, ? extends Object> parameters) throws CelumNotAvailableException, IllegalStateException
    {
        return getCockpitCelumDelegate().getCelumLink(null, getCelumParameters(parameters));
    }


    protected Map<String, String> getCelumParameters(Map<String, ? extends Object> parameters) throws IllegalStateException, CelumNotAvailableException
    {
        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("backToHybrisScript", "/mam/cockpitIntegrationCommit");
        String catVerPk = getCatalogVersionPK();
        if(StringUtils.isBlank(catVerPk))
        {
            throw new IllegalStateException("No valid catalog version could be resolved.");
        }
        additionalParams.put("catalogVersionPK", catVerPk);
        String contextPath = Executions.getCurrent().getContextPath();
        if(StringUtils.isBlank(contextPath))
        {
            LOG.warn("Transfering Celum asset back to hybris might not work. Reason: Context path could not be resolved.");
        }
        else
        {
            additionalParams.put("contextPath", StringUtils.remove(contextPath, '/'));
        }
        String itemPk = null;
        TypedObject item = getAssociatedItem();
        if(item == null)
        {
            throw new IllegalStateException("No associated item could be resolved.");
        }
        Object object = item.getObject();
        if(object instanceof ItemModel)
        {
            itemPk = ((ItemModel)object).getPk().toString();
        }
        if(StringUtils.isBlank(itemPk))
        {
            throw new IllegalStateException("No item PK could be resolved.");
        }
        additionalParams.put("cel-item", itemPk);
        additionalParams.put("cel-prop", parameters
                        .get("attributeQualifier").toString());
        Object ctx = parameters.get("createContext");
        if(ctx instanceof CreateContext)
        {
            additionalParams.put("cel-isoCode", ((CreateContext)ctx)
                            .getLangIso());
        }
        additionalParams.put("events", "celum");
        Object mediaFormatParam = parameters.get("celumMediaFormat");
        String mediaFormatCode = null;
        if(mediaFormatParam == null)
        {
            mediaFormatCode = getCockpitCelumDelegate().getOriginalMediaFormat();
            LOG.warn("No explicit media format in editor configuration! Original media format will be used instead (any modifications to the binary picture in celum system will not affect original media format)");
        }
        else
        {
            mediaFormatCode = mediaFormatParam.toString();
        }
        if(StringUtils.isNotBlank(mediaFormatCode))
        {
            additionalParams.put("cel-mf", mediaFormatCode);
        }
        return additionalParams;
    }


    protected String getCatalogVersionPK()
    {
        String catVerPk = null;
        TypedObject item = getAssociatedItem();
        if(item != null)
        {
            Object object = item.getObject();
            if(object instanceof ItemModel)
            {
                Object source = UISessionUtils.getCurrentSession().getModelService().getSource(object);
                if(source instanceof Item && CatalogManager.getInstance().isCatalogItem((Item)source))
                {
                    catVerPk = CatalogManager.getInstance().getCatalogVersion((Item)source).getPK().toString();
                }
            }
        }
        return catVerPk;
    }


    protected DefaultCockpitCelumDelegate getCockpitCelumDelegate()
    {
        if(this.celumDelegate == null)
        {
            this.celumDelegate = (DefaultCockpitCelumDelegate)Registry.getApplicationContext().getBean("cockpitCelumDelegate");
        }
        return this.celumDelegate;
    }


    protected TypedObject getAssociatedItem()
    {
        TypedObject item = null;
        if(this.parameters != null && this.parameters.containsKey("parentObject"))
        {
            try
            {
                item = (TypedObject)this.parameters.get("parentObject");
            }
            catch(ClassCastException cce)
            {
                LOG.warn("Item associated with editor is not a Typed Object. Please make sure editor parameters have been properly configured.", cce);
            }
        }
        if(item == null)
        {
            item = UISessionUtils.getCurrentSession().getCurrentPerspective().getActiveItem();
        }
        return item;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
    }
}
