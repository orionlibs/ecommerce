package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import de.hybris.platform.cockpit.services.celum.impl.DefaultCockpitCelumDelegate;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
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
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;

public class CelumMediaEditorBase
{
    private static final Logger LOG = LoggerFactory.getLogger(CelumMediaEditorBase.class);
    public static final String CELUM_MEDIA_FORMAT_KEY = "celumMediaFormat";
    public static final String OPEN_CELUM_EDITOR_IMG = "cockpit/images/open_celum_editor.gif";
    public static final String OPEN_CELUM_EDITOR_NOSYNC_IMG = "cockpit/images/open_celum_editor_nosync.gif";
    public static final String EDITOR_CONTEXT_OBJECT = "parentObject";
    public static final String CONTEXT_PATH = "contextPath";
    public static final String BACK_TO_HYBRIS_SCRIPT = "backToHybrisScript";
    public static final String CATALOG_VERSION_PK = "catalogVersionPK";
    public static final String EVENTS_KEY = "events";
    private DefaultCockpitCelumDelegate celumDelegate = null;
    private final Map<String, Object> parameters = new HashMap<>();


    public CelumMediaEditorBase(Map<String, ? extends Object> parameters)
    {
        if(parameters != null)
        {
            this.parameters.putAll(parameters);
        }
    }


    protected String getLink(Map<String, ? extends Object> parameters, boolean isEditable) throws CelumNotAvailableException, IllegalStateException
    {
        String ret = "";
        if(isEditable)
        {
            ItemModel item = null;
            Object ctx = parameters.get("createContext");
            if(ctx instanceof CreateContext)
            {
                TypedObject sourceObj = ((CreateContext)ctx).getSourceObject();
                item = (sourceObj == null) ? null : (ItemModel)sourceObj.getObject();
            }
            String attFullQualif = (String)parameters.get("attributeQualifier");
            String celumAssetId = null;
            if(attFullQualif != null)
            {
                String attQualif = UISessionUtils.getCurrentSession().getTypeService().getAttributeCodeFromPropertyQualifier(attFullQualif);
                Object attribute = TypeTools.getModelService().getAttributeValue(item, attQualif);
                if(attribute instanceof ItemModel)
                {
                    Integer assetId = getCockpitCelumDelegate().getCelumAssetId((ItemModel)attribute);
                    celumAssetId = (assetId == null) ? null : assetId.toString();
                }
            }
            ret = getCockpitCelumDelegate().getCelumLink(item, celumAssetId, getCelumParameters(parameters));
        }
        return ret;
    }


    protected DefaultCockpitCelumDelegate getCockpitCelumDelegate()
    {
        if(this.celumDelegate == null)
        {
            this.celumDelegate = (DefaultCockpitCelumDelegate)Registry.getApplicationContext().getBean("cockpitCelumDelegate");
        }
        return this.celumDelegate;
    }


    public TypedObject getAssociatedItem()
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


    public String getCatalogVersionPK()
    {
        String catVerPk = null;
        TypedObject item = getAssociatedItem();
        if(item != null)
        {
            Object object = item.getObject();
            if(object instanceof ItemModel)
            {
                ItemModel itemModel = (ItemModel)object;
                Object source = UISessionUtils.getCurrentSession().getModelService().getSource(itemModel);
                if(source instanceof Item && CatalogManager.getInstance().isCatalogItem((Item)source))
                {
                    catVerPk = CatalogManager.getInstance().getCatalogVersion((Item)source).getPK().toString();
                }
            }
        }
        return catVerPk;
    }


    public Map<String, String> getCelumParameters(Map<String, ? extends Object> parameters) throws IllegalStateException, CelumNotAvailableException
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
        TypedObject item = null;
        Object object = parameters.get("createContext");
        if(object instanceof CreateContext)
        {
            item = ((CreateContext)object).getSourceObject();
        }
        else
        {
            item = getAssociatedItem();
        }
        if(item == null)
        {
            throw new IllegalStateException("No associated item could be resolved.");
        }
        Object itemObject = item.getObject();
        if(itemObject instanceof ItemModel)
        {
            itemPk = ((ItemModel)itemObject).getPk().toString();
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


    protected boolean isSynchronized(Object editorValue) throws CelumNotAvailableException
    {
        boolean synched = true;
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


    public Hbox renderCelumButton(HtmlBasedComponent superView, String link, boolean isCelumAlive, boolean isEditable)
    {
        Image btnImg = new Image("cockpit/images/open_celum_editor.gif");
        btnImg.setSclass("openCelumEditorImg");
        Hbox hbox = new Hbox();
        hbox.setAlign("top");
        hbox.setWidth("100%");
        hbox.setWidths("none, 20px");
        hbox.setStyle("table-layout: fixed");
        superView.setParent((Component)hbox);
        if(!isEditable)
        {
            btnImg.setTooltiptext(Labels.getLabel("security.permision_denied"));
            btnImg.setStyle("opacity:0.4;filter:alpha(opacity=40)");
            btnImg.setParent((Component)hbox);
        }
        else if(!isCelumAlive)
        {
            btnImg.setTooltiptext(Labels.getLabel("editor.button.celumeditor.celum_not_available"));
            btnImg.setStyle("opacity:0.4;filter:alpha(opacity=40)");
            btnImg.setParent((Component)hbox);
        }
        else
        {
            A celumBtn = new A();
            celumBtn.setParent((Component)hbox);
            celumBtn.setSclass("celumBtn");
            celumBtn.setDynamicProperty("href", link);
            btnImg.setTooltiptext(Labels.getLabel("editor.button.celumeditor.open.tooltip"));
            celumBtn.appendChild((Component)btnImg);
            celumBtn.setParent((Component)hbox);
        }
        return hbox;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }
}
