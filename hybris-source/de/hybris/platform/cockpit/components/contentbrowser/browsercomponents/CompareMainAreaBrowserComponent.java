package de.hybris.platform.cockpit.components.contentbrowser.browsercomponents;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.PagerToolbarBrowserComponent;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.values.ObjectCompareService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractAdvancedBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Toolbarbutton;

public class CompareMainAreaBrowserComponent extends AbstractMainAreaBrowserComponent
{
    private boolean useEditorConfig = true;
    private CompareView cView = null;
    private ObjectCompareService objectCompareService = null;
    private static final Logger LOG = LoggerFactory.getLogger(CompareMainAreaBrowserComponent.class);
    private CockpitValidationService validationService;
    private final CompareView.CompareViewListener compViewListener = (CompareView.CompareViewListener)new Object(this);


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        update();
    }


    public CompareMainAreaBrowserComponent(AbstractAdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((AdvancedBrowserModel)model, contentBrowser);
    }


    public AbstractAdvancedBrowserModel getModel()
    {
        return (AbstractAdvancedBrowserModel)super.getModel();
    }


    protected void cleanup()
    {
    }


    protected EditorConfiguration getEditorConfigurationForSource()
    {
        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(getModel().getCompareModel().getReferenceObject());
        EditorConfiguration config = (EditorConfiguration)UISessionUtils.getCurrentSession().getUiConfigurationService().getComponentConfiguration(template, "editorArea", EditorConfiguration.class);
        if(config != null)
        {
            try
            {
                config = config.clone();
                EditorHelper.initializeSections(config, (ObjectType)template, getModel().getCompareModel().getReferenceObject(), getTypeService());
            }
            catch(CloneNotSupportedException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return config;
    }


    protected Div createToolbar()
    {
        Div toolbar = new Div();
        toolbar.setSclass("compareToolbar");
        Hbox toolbarBox = new Hbox();
        toolbarBox.setSclass("compareToolbarBox");
        toolbar.appendChild((Component)toolbarBox);
        Toolbarbutton toggleButton = new Toolbarbutton();
        toggleButton.setAttribute("checked", Boolean.valueOf(this.useEditorConfig));
        toggleButton.setImage(this.useEditorConfig ? "/cockpit/images/compare_diff.gif" : "/cockpit/images/compare_config.gif");
        toggleButton.setTooltiptext(this.useEditorConfig ? Labels.getLabel("browser.viewmode.compare.toggleview.tooltip.activated") :
                        Labels.getLabel("browser.viewmode.compare.toggleview.tooltip.deactivated"));
        toggleButton.addEventListener("onClick", (EventListener)new Object(this, toggleButton));
        toolbarBox.appendChild((Component)toggleButton);
        return toolbar;
    }


    protected Div createMainArea()
    {
        Div mainContainer = new Div();
        mainContainer.setSclass("compareAreaMainContainer");
        if(getContentBrowser().getToolbarComponent() instanceof PagerToolbarBrowserComponent)
        {
            Div additionalToolbarSlot = ((PagerToolbarBrowserComponent)getContentBrowser().getToolbarComponent()).getAdditionalToolbarSlot();
            UITools.detachChildren((Component)additionalToolbarSlot);
            additionalToolbarSlot.appendChild((Component)createToolbar());
            additionalToolbarSlot.setAttribute("viewmodeid", "compare");
        }
        Div scrollContainer = new Div();
        scrollContainer.setSclass("compareAreaScrollContainer");
        this
                        .cView = new CompareView(getModel().getCompareModel().getReferenceObject(), getModel().getCompareModel().getItems(), getObjectCompareService().getComparedAttributes(getModel().getCompareModel().getReferenceObject(),
                        getCompareObjects()), this.compViewListener);
        this.cView.setConfiguration(this.useEditorConfig ? getEditorConfigurationForSource() : null);
        this.cView.initialize();
        scrollContainer.appendChild((Component)this.cView);
        mainContainer.appendChild((Component)scrollContainer);
        return mainContainer;
    }


    protected List<TypedObject> getCompareObjects()
    {
        List<TypedObject> items = new ArrayList<>(getModel().getCompareModel().getItems());
        items.remove(getModel().getCompareModel().getReferenceObject());
        return items;
    }


    public boolean update()
    {
        this.cView.setItems(getModel().getCompareModel().getItems());
        this.cView.setComparedAttributes(getObjectCompareService().getComparedAttributes(
                        getModel().getCompareModel().getReferenceObject(), getCompareObjects()));
        if(this.cView.getReferenceObject() == null ||
                        !this.cView.getReferenceObject().equals(getModel().getCompareModel().getReferenceObject()) || this.cView
                        .getConfiguration() == null || !this.useEditorConfig)
        {
            this.cView.setConfiguration(this.useEditorConfig ? getEditorConfigurationForSource() : null);
        }
        this.cView.setReferenceObject(getModel().getCompareModel().getReferenceObject());
        this.cView.update();
        return true;
    }


    public ObjectCompareService getObjectCompareService()
    {
        if(this.objectCompareService == null)
        {
            this.objectCompareService = (ObjectCompareService)SpringUtil.getBean("objectCompareService");
        }
        return this.objectCompareService;
    }


    CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }


    protected UIItemView getCurrentItemView()
    {
        return null;
    }
}
