package de.hybris.platform.cmscockpit.components.liveedit.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cmscockpit.components.contentbrowser.ContentEditorRenderer;
import de.hybris.platform.cmscockpit.components.contentbrowser.DefaultContentEditorRenderer;
import de.hybris.platform.cmscockpit.components.contentbrowser.message.StatusPanelComponent;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.commons.jalo.CommonsManager;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class LiveEditPopupEditDialog<T extends LiveEditView> extends Window
{
    private static final Logger LOG = Logger.getLogger(LiveEditPopupEditDialog.class);
    protected static final String CONTENT_EDITOR_CONF = "contentEditor";
    protected static final String LIVE_POPUP_MAIN_PANEL = "livePopupMainPanel";
    protected static final String LIVE_POPUP_CONTAINER = "livePopupContainer";
    private T liveEditView = null;
    private TypedObject currentContentElement = null;
    private ObjectTemplate lastRootItem = null;
    private ObjectValueContainer valueContainer = null;
    private Collection<CatalogVersionModel> catalogVersions = null;
    private Map<String, Object> currentAttributes = null;
    private ContentEditorRenderer editorRenderer = null;
    private CMSComponentService cmsComponentService = null;
    private UIConfigurationService uiConfigurationService = null;
    private ContentEditorConfiguration configuration = null;
    private transient Div scrollableContainer = null;
    private final transient StatusPanelComponent statusComponent = new StatusPanelComponent();


    public LiveEditPopupEditDialog(Map<String, Object> currentAttributes, Collection<CatalogVersionModel> catalogVersions, T liveEditView) throws InterruptedException
    {
        this.currentAttributes = currentAttributes;
        this.catalogVersions = catalogVersions;
        this.liveEditView = liveEditView;
        initialize();
    }


    protected void initialize() throws InterruptedException
    {
        setSizable(false);
        setClosable(true);
        setSclass("livePopupContainer");
        setBorder("none");
        setPosition("center");
        setMode("modal");
        setTitle(Labels.getLabel("cmscocpit.livedit.popup.title"));
        Div mainContainer = new Div();
        appendChild((Component)mainContainer);
        String componentId = (String)this.currentAttributes.get("cmp_id");
        AbstractCMSComponentModel currentComponent = null;
        try
        {
            if(StringUtils.isNotEmpty(componentId))
            {
                currentComponent = getComponentService().getAbstractCMSComponent(componentId, this.catalogVersions);
            }
        }
        catch(CMSItemNotFoundException e)
        {
            LOG.error(e);
        }
        if(currentComponent != null)
        {
            this.currentContentElement = UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentComponent);
            mainContainer.setWidth("100%");
            this.scrollableContainer = new Div();
            this.scrollableContainer.setStyle("overflow-y:auto");
            this.scrollableContainer.setSclass("livePopupMainPanel");
            this.scrollableContainer.setHeight("500px");
            this.scrollableContainer.appendChild((Component)this.statusComponent);
            renderEditor((HtmlBasedComponent)this.scrollableContainer, this.currentContentElement);
            mainContainer.appendChild((Component)this.scrollableContainer);
            addEventListener("onClose", (EventListener)new Object(this));
        }
        else
        {
            setWidth("400px");
            mainContainer.setWidth("100%");
            this.scrollableContainer = new Div();
            this.scrollableContainer.setStyle("overflow-y:auto");
            this.scrollableContainer.setHeight("250px");
            this.scrollableContainer.appendChild((Component)new Label(Labels.getLabel("liveedit.popup.element.notfound", (Object[])new String[] {componentId})));
            mainContainer.appendChild((Component)this.scrollableContainer);
        }
        Div buttonDiv = new Div();
        buttonDiv.setStyle("margin-top:15px;text-align: right;");
        Button okButton = new Button(Labels.getLabel("general.ok"));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "LiveEditPopupOKBtn_EA_";
            UITools.applyTestID((Component)okButton, "LiveEditPopupOKBtn_EA_");
        }
        okButton.setParent((Component)buttonDiv);
        okButton.setSclass("btnblue");
        okButton.setDisabled(false);
        okButton.setTooltiptext(Labels.getLabel("general.ok"));
        okButton.addEventListener("onClick", (EventListener)new Object(this));
        mainContainer.appendChild((Component)buttonDiv);
    }


    public void update()
    {
        UITools.detachChildren((Component)this.scrollableContainer);
        UITools.detachChildren((Component)this.statusComponent);
        this.scrollableContainer.appendChild((Component)this.statusComponent);
        renderEditor((HtmlBasedComponent)this.scrollableContainer, this.currentContentElement);
    }


    protected void renderEditor(HtmlBasedComponent parent, TypedObject rootItem)
    {
        if(rootItem != null)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(rootItem.getType().getCode());
            if(isReloadNeeded(objectTemplate))
            {
                this.lastRootItem = objectTemplate;
                this.configuration = getContentEditorConfiguration(objectTemplate);
                if(this.configuration == null)
                {
                    LOG.error("Aborting rendering of content element editor. Reason: Configuration could not be loaded.");
                    return;
                }
                this.editorRenderer = (ContentEditorRenderer)new DefaultContentEditorRenderer(this.configuration, true);
            }
            if(this.editorRenderer == null)
            {
                LOG.warn("Aborting rendering of content element editor. Content element editor renderer could not be loaded.");
            }
            else if(this.configuration == null)
            {
                LOG.warn("Aborting rendering of content element editor. Reason: Configuration not available.");
            }
            else
            {
                try
                {
                    String templateString = getParsedVelocityTemplateString(this.configuration.getCockpitTemplate(), rootItem);
                    if(StringUtils.isBlank(templateString))
                    {
                        LOG.warn("Aborting rendering of content element editor. Reason: No cockpit template could be loaded.");
                    }
                    else
                    {
                        this.editorRenderer.renderContentEditor(rootItem, templateString, getValueContainer(rootItem), parent,
                                        Collections.singletonMap("locationInfo", this));
                    }
                }
                catch(IllegalArgumentException iae)
                {
                    LOG.error("Rendering of editor failed. Reason: '" + iae.getMessage() + "'.", iae);
                }
            }
        }
        else
        {
            LOG.warn("Rendering of editor aborted. Reason: Root item of section model not a 'TypedObject'.");
        }
    }


    protected boolean isReloadNeeded(ObjectTemplate objectTemplate)
    {
        return (this.lastRootItem == null || !this.lastRootItem.equals(objectTemplate) || this.editorRenderer == null || this.configuration == null);
    }


    protected String getParsedVelocityTemplateString(String velocityTemplate, TypedObject rootItem)
    {
        String templateString = null;
        try
        {
            StringWriter writer = new StringWriter();
            Properties velocityEngineProperties = new Properties();
            VelocityEngine velocityEngine = CommonsManager.getInstance().getVelocityEngine(velocityEngineProperties);
            VelocityContext velocityContext = new VelocityContext();
            String label = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(rootItem);
            velocityContext.put("label", StringEscapeUtils.escapeXml(label));
            velocityEngine.evaluate((Context)velocityContext, writer, "CMS Content editor section component", velocityTemplate);
            templateString = writer.toString();
        }
        catch(Exception e)
        {
            LOG.error("Velocity template MELTDOWN!!!", e);
        }
        return templateString;
    }


    protected ObjectValueContainer getValueContainer(TypedObject rootItem)
    {
        if(this.valueContainer == null)
        {
            if(rootItem != null)
            {
                this.valueContainer = TypeTools.createValueContainer(rootItem, rootItem.getType().getPropertyDescriptors(),
                                UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos(), true);
            }
            else
            {
                LOG.warn("No value container and no valid root item available.");
            }
        }
        return this.valueContainer;
    }


    protected void clearValueContainer()
    {
        this.valueContainer = null;
    }


    protected ContentEditorConfiguration getContentEditorConfiguration(ObjectTemplate objectTemplate)
    {
        return (ContentEditorConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "contentEditor", ContentEditorConfiguration.class);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected CMSComponentService getComponentService()
    {
        if(this.cmsComponentService == null)
        {
            this.cmsComponentService = (CMSComponentService)SpringUtil.getBean("cmsComponentService");
        }
        return this.cmsComponentService;
    }
}
