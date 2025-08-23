package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cmscockpit.components.contentbrowser.message.StatusPanelComponent;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cmscockpit.session.impl.ContentEditorBrowserSectionModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserSectionComponent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.ViewUpdateUtils;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class CMSContentEditorSectionComponent extends AbstractBrowserSectionComponent
{
    private static final Logger LOG = Logger.getLogger(CMSContentEditorSectionComponent.class);
    protected static final String STRUCTURE_EDIT_SECTION = "structureViewEditorSection";
    protected static final String SPLITTER_PARENT_SCLASS = "splitterParent";
    protected static final String SPLITTER_SCLASS = "splitter";
    protected static final String RESIZE_USER_EVENT = "resize";
    protected static final String EMPTY_TEXT = "Nothing to display.";
    private boolean initialized = false;
    private ObjectTemplate lastRootItemType;
    private TypedObject lastRootItem;
    private VelocityContext lastVelocityCtx;
    protected transient Div sectionGroupBox;
    protected transient Div groupBoxContent;
    protected transient StatusPanelComponent statusPanel = new StatusPanelComponent();
    private ContentEditorRenderer editorRenderer;
    private ContentEditorConfiguration configuration;
    private UIConfigurationService uiConfigurationService;


    public CMSContentEditorSectionComponent(ContentEditorBrowserSectionModel sectionModel)
    {
        super((BrowserSectionModel)sectionModel);
    }


    public ContentEditorBrowserSectionModel getSectionModel()
    {
        return (ContentEditorBrowserSectionModel)super.getSectionModel();
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getSectionModel() != null)
        {
            UITools.detachChildren((Component)this);
            setWidth("100%");
            setVisible(getSectionModel().isVisible());
            setSclass("splitterParent");
            Div editorContentCnt = new Div();
            editorContentCnt.setSclass("structureViewEditorSection");
            appendChild((Component)editorContentCnt);
            this.sectionGroupBox = createSectionView();
            if(this.sectionGroupBox == null)
            {
                throw new IllegalStateException("Section group box was not created successfully.");
            }
            editorContentCnt.appendChild((Component)this.sectionGroupBox);
            this.initialized = true;
        }
        return this.initialized;
    }


    protected Div createSectionView()
    {
        Div container = new Div();
        this.groupBoxContent = new Div();
        this.groupBoxContent.setSclass("browserSectionContent");
        container.appendChild((Component)this.groupBoxContent);
        if(getSectionModel().getRootItem() instanceof TypedObject)
        {
            renderEditor((HtmlBasedComponent)this.groupBoxContent);
        }
        else
        {
            this.groupBoxContent.appendChild((Component)new Label("Nothing to display."));
        }
        return container;
    }


    public void setActiveItem(TypedObject activeItem)
    {
    }


    protected void renderEditor(HtmlBasedComponent parent)
    {
        if(getSectionModel().getRootItem() instanceof TypedObject)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate((TypedObject)getSectionModel().getRootItem());
            if(isReloadNeeded(objectTemplate))
            {
                this.lastRootItemType = objectTemplate;
                this.configuration = getContentEditorConfiguration(objectTemplate);
                if(this.configuration == null)
                {
                    LOG.error("Aborting rendering of content element editor. Reason: Configuration could not be loaded.");
                    return;
                }
                this.editorRenderer = (ContentEditorRenderer)getEditorRenderer(this.configuration);
            }
            if(this.editorRenderer == null)
            {
                LOG.warn("Aborting rendering of content element editor. Reason: Content element editor renderer could not be loaded.");
            }
            else if(this.configuration == null)
            {
                LOG.warn("Aborting rendering of content element editor. Reason: Configuration not available.");
            }
            else
            {
                try
                {
                    String templateString = getParsedVelocityTemplateString(this.configuration.getCockpitTemplate());
                    if(StringUtils.isBlank(templateString))
                    {
                        LOG.warn("Aborting rendering of content element editor. Reason: No cockpit template could be loaded.");
                    }
                    else
                    {
                        this.editorRenderer.renderContentEditor((TypedObject)getSectionModel().getRootItem(), templateString,
                                        getSectionModel().getValueContainer(), parent,
                                        Collections.singletonMap("locationInfo", getSectionModel()), getSectionModel().isReadOnly());
                        this.lastRootItem = (TypedObject)getSectionModel().getRootItem();
                        this.lastVelocityCtx = ComponentInjectorHelper.createVelocityContext(this.lastRootItem);
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
        return (this.lastRootItemType == null || !this.lastRootItemType.equals(objectTemplate) || this.editorRenderer == null || this.configuration == null);
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            setVisible(getSectionModel().isVisible());
            if(this.lastRootItem == null || !this.lastRootItem.equals(getSectionModel().getRootItem()) ||
                            ComponentInjectorHelper.checkVelocityParamsChanged(getSectionModel().getRootItem(), this.lastVelocityCtx) ||
                            getSectionModel().hasReadOnlyChanged())
            {
                this.groupBoxContent.getChildren().clear();
                this.statusPanel.getChildren().clear();
                this.groupBoxContent.appendChild((Component)this.statusPanel);
                if(getSectionModel().isVisible())
                {
                    if(getSectionModel().getRootItem() instanceof TypedObject)
                    {
                        renderEditor((HtmlBasedComponent)this.groupBoxContent);
                    }
                    else
                    {
                        UITools.detachChildren((Component)this);
                        appendChild((Component)new Label("Nothing to display."));
                    }
                }
            }
            else
            {
                ViewUpdateUtils.performViewUpdate((Component)this,
                                ViewUpdateUtils.createItemPropertyContext(this.lastRootItem, Collections.EMPTY_LIST), true);
            }
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
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


    protected String getParsedVelocityTemplateString(String velocityTemplate)
    {
        return ComponentInjectorHelper.getParsedVelocityTemplateString(getSectionModel().getRootItem(), velocityTemplate);
    }


    public void updateActiveItems()
    {
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        if(getSectionModel() != null)
        {
            ViewUpdateUtils.performViewUpdate((Component)this, ViewUpdateUtils.createItemPropertyContext(item, modifiedProperties), true);
        }
    }


    public void updateSelectedItems()
    {
    }


    public DefaultContentEditorRenderer getEditorRenderer(ContentEditorConfiguration configuration)
    {
        return new DefaultContentEditorRenderer(configuration);
    }
}
