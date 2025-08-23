package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.editorarea.BasePopupEditorComponent;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.notifier.NotifierZKComponent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.LayoutRegion;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

public class BaseUICockpitPerspective extends AbstractUIPerspective
{
    private static final String ON_OPEN_LATER = "onOpenLater";
    public static final String INITIAL_ATTRIBUTES = "initialAttributes";
    public static final String PROPERTY_DESCRIPTOR = "propertyDescriptor";
    private static final Logger LOG = LoggerFactory.getLogger(BaseUICockpitPerspective.class);
    public static final String DRAG_DROP_ID = "PerspectiveDND";
    private String customCsaURI;
    private NotifierZKComponent notifier = null;
    private UINavigationArea navigationArea;
    private UIBrowserArea browserArea;
    private UIEditorArea editorArea = null;
    private UIEditorArea popupEditorArea = null;
    private BrowserAreaListener browserAreaListener = null;
    private EditorAreaListener editorAreaListener = null;
    private NavigationAreaListener navAreaListener = null;
    private Set<ObjectType> editorAreaTypes = null;
    private Window popupEditorWindow;
    private LayoutRegion editorAreaComponent;
    private LayoutRegion navigationAreaComponent;
    private boolean forceOpenInEditorArea = false;
    private final List<CockpitEventAcceptor> cockpitEventAcceptors = new LinkedList<>();
    private UIConfigurationService uiConfigurationService = null;


    @Required
    public void setUiConfigurationService(UIConfigurationService uiConfigurationService)
    {
        this.uiConfigurationService = uiConfigurationService;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        return this.uiConfigurationService;
    }


    public void setEditorAreaTypes(Set<EditorAreaTypeRule> typeRules)
    {
        this.editorAreaTypes = new HashSet<>();
        Set<ObjectType> toRemoveSet = new HashSet<>();
        if(typeRules != null)
        {
            for(EditorAreaTypeRule rule : typeRules)
            {
                ObjectType type = getTypeService().getObjectType(rule.getTypeCode());
                Set<ObjectType> tmpSet = new HashSet<>();
                tmpSet.add(type);
                if(Boolean.TRUE.equals(rule.getRecursive()))
                {
                    tmpSet.addAll(getTypeService().getAllSubtypes(type));
                }
                if(Boolean.TRUE.equals(rule.getAllowed()))
                {
                    this.editorAreaTypes.addAll(tmpSet);
                    continue;
                }
                toRemoveSet.addAll(tmpSet);
            }
        }
        this.editorAreaTypes.removeAll(toRemoveSet);
    }


    public void onHide()
    {
    }


    public void onShow()
    {
    }


    public void resetOpenBrowserContainer()
    {
    }


    public void createItemInPopupEditor(ObjectType type, Map<String, ? extends Object> initialValues, CreateContext context)
    {
        createItemInPopupEditor(type, initialValues, null, context, true, false);
    }


    public void createItemInPopupEditor(ObjectType type, Map<String, ? extends Object> initialValues, CreateContext context, boolean loadDefaultValues)
    {
        createItemInPopupEditor(type, initialValues, null, context, loadDefaultValues, false);
    }


    @Deprecated
    public void createItemInPopupEditor(ObjectType type, Map<String, ? extends Object> initialValues, BrowserModel browser)
    {
        createItemInPopupEditor(type, initialValues, browser, true);
    }


    @Deprecated
    public void createItemInPopupEditor(ObjectType type, Map<String, ? extends Object> initialValues, BrowserModel browser, boolean loadDefaultValues)
    {
        createItemInPopupEditor(type, initialValues, browser, loadDefaultValues, false);
    }


    @Deprecated
    public void createItemInPopupEditor(ObjectType type, Map<String, ? extends Object> initialValues, BrowserModel browser, boolean loadDefaultValues, boolean initialValidation)
    {
        createItemInPopupEditor(type, initialValues, browser, null, loadDefaultValues, initialValidation);
    }


    protected void createItemInPopupEditor(ObjectType type, Map<String, ? extends Object> initialValues, BrowserModel browser, CreateContext ctx, boolean loadDefaultValues, boolean initialValidation)
    {
        getPopupEditorArea().reset();
        EditorAreaController eac = getPopupEditorArea().getEditorAreaController();
        if(eac instanceof PopupEditorAreaController)
        {
            ((PopupEditorAreaController)eac).setContextEditorBrowser(browser);
            ((PopupEditorAreaController)eac).getAttributesMap().put("createContext", ctx);
            ((PopupEditorAreaController)eac).getAttributesMap().put("initialValidation", Boolean.valueOf(initialValidation));
        }
        eac.setCreateFromTemplate(type, initialValues, loadDefaultValues);
        eac.resetSectionPanelModel();
        this.popupEditorWindow.setTitle(Labels.getLabel("perspective.popupeditor.create", (Object[])new String[] {type
                        .getCode()}));
        this.popupEditorWindow.doHighlighted();
        this.popupEditorWindow.setShadow(false);
    }


    public boolean isPopupEditorOpen()
    {
        return this.popupEditorWindow.isVisible();
    }


    public void closePopupEditor()
    {
        this.popupEditorWindow.setVisible(false);
    }


    public boolean isActivatable(TypedObject item)
    {
        return (this.editorAreaTypes == null) ? true : this.editorAreaTypes.contains(item.getType());
    }


    public void activateItemInPopupEditor(TypedObject activeItem)
    {
        if(activeItem != null)
        {
            if(getTypeService().checkItemAlive(activeItem))
            {
                try
                {
                    if(!isPopupEditorOpen() && getPopupEditorArea() instanceof EditorArea)
                    {
                        ((EditorArea)getPopupEditorArea()).resetStack();
                        this.popupEditorWindow.doHighlighted();
                        this.popupEditorWindow.setClosable(true);
                    }
                    getPopupEditorArea().reset();
                    getPopupEditorArea().setCurrentObject(activeItem);
                    getPopupEditorArea().getEditorAreaController().resetSectionPanelModel();
                    List<AbstractComponent> children = this.popupEditorWindow.getChildren();
                    if(!children.isEmpty())
                    {
                        BasePopupEditorComponent popUpEditor = null;
                        for(AbstractComponent component : children)
                        {
                            if(component instanceof BasePopupEditorComponent)
                            {
                                popUpEditor = (BasePopupEditorComponent)component;
                            }
                        }
                        if(popUpEditor != null)
                        {
                            popUpEditor.setBackPanelVisible(false);
                            if(((EditorArea)this.popupEditorArea).getItemStackSize() > 1)
                            {
                                popUpEditor.setBackPanelVisible(true);
                            }
                        }
                    }
                    this.popupEditorWindow.setTitle(Labels.getLabel("perspective.popupeditor.edit", (Object[])new String[] {activeItem
                                    .getType().getCode()}));
                    this.popupEditorWindow.invalidate();
                }
                catch(IllegalArgumentException iae)
                {
                    LOG.error("Item could not be activated.", iae);
                }
            }
            else
            {
                getPopupEditorArea().setCurrentObjectType(null);
                getPopupEditorArea().setCurrentObject(null);
            }
        }
    }


    protected void activateItemInEditorArea(TypedObject activeItem)
    {
        if(activeItem != null && getTypeService().checkItemAlive(activeItem) && !activeItem.equals(getActiveItem()))
        {
            setActiveItem(activeItem);
            try
            {
                if(!this.editorAreaComponent.isOpen())
                {
                    this.editorAreaComponent.addEventListener("onOpenLater", (EventListener)new Object(this));
                    Events.echoEvent("onOpenLater", (Component)this.editorAreaComponent, null);
                }
                getEditorArea().setCurrentObject(activeItem);
                for(BrowserModel browserModel : getBrowserArea().getVisibleBrowsers())
                {
                    getBrowserArea().updateActivation(browserModel);
                }
            }
            catch(IllegalArgumentException iae)
            {
                LOG.error("Item could not be activated.", iae);
                for(BrowserModel b : getBrowserArea().getVisibleBrowsers())
                {
                    b.updateItems();
                }
                setActiveItem(null);
            }
        }
        else if(activeItem != null && !getTypeService().checkItemAlive(activeItem))
        {
            for(BrowserModel b : getBrowserArea().getVisibleBrowsers())
            {
                b.updateItems();
            }
            setActiveItem(null);
            getEditorArea().setCurrentObjectType(null);
            getEditorArea().setCurrentObject(null);
        }
        else if(activeItem == null)
        {
            getEditorArea().setCurrentObjectType(null);
            getEditorArea().setCurrentObject(null);
            setActiveItem(null);
        }
        else if(activeItem.equals(getActiveItem()) && !this.editorAreaComponent.isOpen())
        {
            this.editorAreaComponent.addEventListener("onOpenLater", (EventListener)new Object(this));
            Events.echoEvent("onOpenLater", (Component)this.editorAreaComponent, null);
        }
    }


    public final void activateItemInEditor(TypedObject activeItem)
    {
        if(isActivatable(activeItem))
        {
            activateItemInEditorArea(activeItem);
        }
        else
        {
            activateItemInPopupEditor(activeItem);
        }
    }


    public UINavigationArea getNavigationArea()
    {
        return this.navigationArea;
    }


    public void setNavigationArea(UINavigationArea navigationArea)
    {
        if((this.navigationArea == null && navigationArea != null) || !this.navigationArea.equals(navigationArea))
        {
            if(getNavigationArea() != null)
            {
                getNavigationArea().setPerspective(null);
                getNavigationArea().removeAreaListener(getNavigationAreaListener());
                removeCockpitEventAcceptor((CockpitEventAcceptor)getNavigationArea());
            }
            this.navigationArea = navigationArea;
            if(getNavigationArea() != null)
            {
                getNavigationArea().setPerspective((UICockpitPerspective)this);
                getNavigationArea().addAreaListener(getNavigationAreaListener());
                addCockpitEventAcceptor((CockpitEventAcceptor)getNavigationArea());
            }
        }
    }


    public UIBrowserArea getBrowserArea()
    {
        return this.browserArea;
    }


    public void setBrowserArea(UIBrowserArea browserArea)
    {
        if((this.browserArea == null && browserArea != null) || !this.browserArea.equals(browserArea))
        {
            if(getBrowserArea() != null)
            {
                getBrowserArea().setPerspective(null);
                getBrowserArea().removeBrowserAreaListener(getBrowserAreaListener());
                removeCockpitEventAcceptor((CockpitEventAcceptor)getBrowserArea());
            }
            this.browserArea = browserArea;
            if(getBrowserArea() != null)
            {
                getBrowserArea().setPerspective((UICockpitPerspective)this);
                getBrowserArea().addBrowserAreaListener(getBrowserAreaListener());
                addCockpitEventAcceptor((CockpitEventAcceptor)getBrowserArea());
            }
        }
    }


    public UIEditorArea getEditorArea()
    {
        return this.editorArea;
    }


    public void setEditorArea(UIEditorArea editorArea)
    {
        if((this.editorArea == null && editorArea != null) || !this.editorArea.equals(editorArea))
        {
            if(getEditorArea() != null)
            {
                getEditorArea().setPerspective(null);
                getEditorArea().removeEditorAreaListener(getEditorAreaListener());
                removeCockpitEventAcceptor((CockpitEventAcceptor)getEditorArea());
            }
            this.editorArea = editorArea;
            if(getEditorArea() != null)
            {
                getEditorArea().setPerspective((UICockpitPerspective)this);
                getEditorArea().addEditorAreaListener(getEditorAreaListener());
                addCockpitEventAcceptor((CockpitEventAcceptor)getEditorArea());
            }
        }
    }


    public void setCustomCsaURI(String customCsaURI)
    {
        this.customCsaURI = customCsaURI;
    }


    public String getCustomCsaURI()
    {
        return this.customCsaURI;
    }


    public NotifierZKComponent getNotifier()
    {
        return this.notifier;
    }


    public void setNotifier(NotifierZKComponent notifier)
    {
        this.notifier = notifier;
    }


    public void setPopupEditorWindow(Window popupEditorWindow)
    {
        this.popupEditorWindow = popupEditorWindow;
    }


    public void initialize(Map<String, Object> params)
    {
        if(getNavigationArea() != null)
        {
            getNavigationArea().initialize(params);
        }
        if(getBrowserArea() != null)
        {
            getBrowserArea().initialize(params);
        }
        if(getEditorArea() != null)
        {
            getEditorArea().initialize(params);
            if(getEditorArea().getEditorAreaController() != null)
            {
                if(getEditorArea().getEditorAreaController().getModel() == null ||
                                !getEditorArea().getEditorAreaController().getModel().equals(getEditorArea()))
                {
                    if(getEditorArea().getEditorAreaController().getModel() != null)
                    {
                        getEditorArea().getEditorAreaController().getModel()
                                        .removeEditorAreaListener(getEditorAreaListener());
                    }
                    getEditorArea().getEditorAreaController().setModel(getEditorArea());
                }
                getEditorArea().getEditorAreaController().initialize();
            }
        }
        if(getPopupEditorArea() != null)
        {
            getPopupEditorArea().initialize(params);
            if(getPopupEditorArea().getEditorAreaController() != null)
            {
                if(getPopupEditorArea().getEditorAreaController().getModel() == null ||
                                !getPopupEditorArea().getEditorAreaController().getModel().equals(getPopupEditorArea()))
                {
                    getPopupEditorArea().getEditorAreaController().setModel(getPopupEditorArea());
                }
                getPopupEditorArea().getEditorAreaController().initialize();
            }
        }
    }


    public void openReferenceCollectionInBrowserContext(Collection<TypedObject> objects, ObjectTemplate template, TypedObject rootItem, Map<String, ? extends Object> parameters)
    {
        BrowserModel browser = getBrowserArea().getFocusedBrowser();
        if(browser == null)
        {
            browser = getBrowserArea().getVisibleBrowsers().get(0);
        }
        if(browser instanceof AdvancedBrowserModel)
        {
            AdvancedBrowserModel advBrowser = (AdvancedBrowserModel)browser;
            PropertyDescriptor descriptor = (PropertyDescriptor)parameters.get("propertyDescriptor");
            advBrowser.setContextRootTypePropertyDescriptor(descriptor);
            if(objects != null)
            {
                if(advBrowser.isContextVisible())
                {
                    if(template == null)
                    {
                        advBrowser.setContextItems(rootItem, objects);
                    }
                    else
                    {
                        advBrowser.setContextItems(rootItem, objects, template);
                    }
                }
                else
                {
                    advBrowser.setContextItemsDirectly(rootItem, objects);
                    if(template != null)
                    {
                        advBrowser.setContextRootType(template);
                    }
                    advBrowser.setContextVisible(true);
                }
                if(objects.isEmpty())
                {
                    advBrowser.setContextRootType(template);
                }
            }
        }
    }


    public void handleItemRemoved(TypedObject object)
    {
        for(BrowserModel b : getBrowserArea().getVisibleBrowsers())
        {
            b.updateItems();
        }
        getBrowserArea().update();
        if(getActiveItem() != null && (getActiveItem().equals(object) || !getTypeService().checkItemAlive(getActiveItem())))
        {
            setActiveItem(null);
            getEditorArea().setCurrentObjectType(null);
            getEditorArea().setCurrentObject(null);
        }
        Notification notification = new Notification(Labels.getLabel("perspective.itemremoved"));
        if(getNotifier() != null)
        {
            getNotifier().setNotification(notification);
        }
    }


    public void setBrowserAreaListener(BrowserAreaListener browserAreaListener)
    {
        if(this.browserAreaListener != null)
        {
            getBrowserArea().removeBrowserAreaListener(this.browserAreaListener);
        }
        this.browserAreaListener = browserAreaListener;
        if(this.browserAreaListener != null)
        {
            getBrowserArea().addBrowserAreaListener(this.browserAreaListener);
        }
    }


    protected BrowserAreaListener getBrowserAreaListener()
    {
        if(this.browserAreaListener == null)
        {
            this.browserAreaListener = (BrowserAreaListener)new DefaultBrowserAreaListener(this);
        }
        return this.browserAreaListener;
    }


    public void setEditorAreaListener(EditorAreaListener editorAreaListener)
    {
        if(this.editorAreaListener != null)
        {
            getEditorArea().removeEditorAreaListener(this.editorAreaListener);
        }
        this.editorAreaListener = editorAreaListener;
        if(this.editorAreaListener != null)
        {
            getEditorArea().addEditorAreaListener(this.editorAreaListener);
        }
    }


    protected EditorAreaListener getEditorAreaListener()
    {
        if(this.editorAreaListener == null)
        {
            this.editorAreaListener = (EditorAreaListener)new DefaultEditorAreaListener(this);
        }
        return this.editorAreaListener;
    }


    public void setNavigationAreaListener(NavigationAreaListener navAreaListener)
    {
        if(this.navAreaListener != null)
        {
            getNavigationArea().removeAreaListener(this.navAreaListener);
        }
        this.navAreaListener = navAreaListener;
        if(this.navAreaListener != null)
        {
            getNavigationArea().addAreaListener(this.navAreaListener);
        }
    }


    protected NavigationAreaListener getNavigationAreaListener()
    {
        if(this.navAreaListener == null)
        {
            this.navAreaListener = (NavigationAreaListener)new DefaultNavigationAreaListener(this);
        }
        return this.navAreaListener;
    }


    public void setPopupEditorArea(UIEditorArea popupEditorArea)
    {
        if((this.popupEditorArea == null && popupEditorArea != null) || (this.popupEditorArea != null &&
                        !this.popupEditorArea.equals(popupEditorArea)))
        {
            if(getPopupEditorArea() != null)
            {
                getPopupEditorArea().setPerspective(null);
            }
            this.popupEditorArea = popupEditorArea;
            if(getPopupEditorArea() != null)
            {
                getPopupEditorArea().setPerspective((UICockpitPerspective)this);
                getPopupEditorArea().getEditorAreaController().setModel(getPopupEditorArea());
                addCockpitEventAcceptor((CockpitEventAcceptor)getPopupEditorArea());
            }
        }
    }


    public UIEditorArea getPopupEditorArea()
    {
        return this.popupEditorArea;
    }


    public void setEditorAreaComponent(LayoutRegion editorAreaComponent)
    {
        this.editorAreaComponent = editorAreaComponent;
    }


    public LayoutRegion getEditorAreaComponent()
    {
        return this.editorAreaComponent;
    }


    public void setNavigationAreaComponent(LayoutRegion navigationAreaComponent)
    {
        this.navigationAreaComponent = navigationAreaComponent;
    }


    public LayoutRegion getNavigationAreaComponent()
    {
        return this.navigationAreaComponent;
    }


    public void expandEditorArea()
    {
        expandEditorArea(false);
    }


    public void toggleNavAndEditArea()
    {
        boolean bothClosed = true;
        if((this.editorAreaComponent != null && this.editorAreaComponent.isOpen()) || (this.navigationAreaComponent != null && this.navigationAreaComponent
                        .isOpen()))
        {
            bothClosed = false;
        }
        if(bothClosed)
        {
            expandEditorArea();
            expandNavigationArea();
        }
        else
        {
            collapseNavigationArea();
            collapseEditorArea();
        }
    }


    public void expandEditorArea(boolean invalidate)
    {
        if(this.editorAreaComponent != null && !UITools.isFromOtherDesktop((Component)this.editorAreaComponent) && !this.editorAreaComponent.isOpen())
        {
            this.editorAreaComponent.setOpen(true);
            if(invalidate)
            {
                this.editorAreaComponent.invalidate();
            }
        }
    }


    public void expandNavigationArea()
    {
        if(this.navigationAreaComponent != null && !this.navigationAreaComponent.isOpen())
        {
            this.navigationAreaComponent.setOpen(true);
        }
    }


    public void collapseNavigationArea()
    {
        if(this.navigationAreaComponent != null && this.navigationAreaComponent.isOpen())
        {
            this.navigationAreaComponent.setOpen(false);
        }
    }


    public void collapseEditorArea()
    {
        if(this.editorAreaComponent != null && this.editorAreaComponent.isOpen())
        {
            this.editorAreaComponent.setOpen(false);
        }
    }


    public void update()
    {
        if(getNavigationArea() != null)
        {
            getNavigationArea().update();
        }
        if(getBrowserArea() != null)
        {
            getBrowserArea().update();
        }
        if(getEditorArea() != null)
        {
            getEditorArea().update();
        }
    }


    public void addCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        if(!this.cockpitEventAcceptors.contains(acceptor))
        {
            this.cockpitEventAcceptors.add(acceptor);
        }
    }


    public void removeCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.remove(acceptor);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(equals(UISessionUtils.getCurrentSession().getCurrentPerspective()) ||
                        UISessionUtils.getCurrentSession().isCachePerspectivesEnabled())
        {
            List<CockpitEventAcceptor> acceptors = new LinkedList<>(this.cockpitEventAcceptors);
            for(CockpitEventAcceptor acceptor : acceptors)
            {
                try
                {
                    acceptor.onCockpitEvent(event);
                }
                catch(Exception e)
                {
                    LOG.error("Could not dispatch cockpit event '" + event + "'.", e);
                }
            }
        }
    }


    public void createNewItem(ObjectTemplate template)
    {
        createNewItem(template, Collections.EMPTY_MAP);
    }


    public void createNewItem(ObjectTemplate template, Map<String, Object> initValues)
    {
        createNewItem(template, initValues, true);
    }


    public void createNewItem(ObjectTemplate template, Map<String, Object> initValues, boolean loadDefaultValues)
    {
        getEditorArea().reset();
        setActiveItem(null);
        getEditorArea().setCurrentObject(getActiveItem());
        for(BrowserModel b : getBrowserArea().getVisibleBrowsers())
        {
            getBrowserArea().updateActivation(b);
        }
        EditorAreaController eac = getEditorArea().getEditorAreaController();
        eac.setCreateFromTemplate((ObjectType)template, initValues, loadDefaultValues);
        eac.resetSectionPanelModel();
    }


    public void createNewItem(ObjectTemplate template, CreateContext createContext, Map<String, Object> initValues, boolean loadDefaultValues, boolean expandEditorArea, boolean popup)
    {
        TypedObject sourceItem = (createContext == null) ? null : createContext.getSourceObject();
        if(expandEditorArea)
        {
            expandEditorArea();
        }
        Map<String, Object> initialValues = new HashMap<>();
        if(sourceItem != null)
        {
            try
            {
                initialValues.putAll(TypeTools.getInitialValues((ObjectType)template, sourceItem, getTypeService(), getUIConfigurationService()));
            }
            catch(Exception e)
            {
                LOG.error("Could not get initial values, reason: ", e);
            }
        }
        if(initValues != null)
        {
            initialValues.putAll(initValues);
        }
        if(popup)
        {
            createItemInPopupEditor((ObjectType)template, initialValues, createContext, loadDefaultValues);
        }
        else
        {
            createNewItem(template, initialValues, loadDefaultValues);
        }
    }


    protected void createTemplateList(String baseType)
    {
        getTemplateList().clear();
        getTemplateList().addAll(getTemplateList(baseType));
    }


    protected List<TemplateListEntry> getTemplateList(String baseType)
    {
        List<TemplateListEntry> ret = new ArrayList<>();
        List<Map<ObjectTemplate, Integer>> list = TypeTools.getTemplatesForCreationWithDepth(getTypeService(),
                        getTypeService().getBaseType(baseType), 0, true);
        int index = 0;
        for(Map<ObjectTemplate, Integer> ot : list)
        {
            Map.Entry<ObjectTemplate, Integer> entry = ot.entrySet().iterator().next();
            ret.add(new TemplateListEntry(entry.getKey(), index, ((Integer)entry.getValue()).intValue()));
            index++;
        }
        return ret;
    }


    private String getTemplateName(ObjectTemplate template)
    {
        if(template.getCode().equals(template.getBaseType().getCode()))
        {
            return TypeTools.getTypeName(template.getBaseType(), getTypeService());
        }
        return template.getCode();
    }


    public boolean generateCreateMenuitems(Menupopup menupopup, boolean popup)
    {
        return generateCreateMenuitems(menupopup, false, null, popup, Collections.EMPTY_MAP, true);
    }


    public boolean generateCreateMenuitems(Menupopup menupopup, boolean expandEditorArea, CreateContext createContext, boolean popup, Map<String, Object> initValues, boolean loadDefaultValues)
    {
        List<TemplateListEntry> entries = (createContext == null) ? getTemplateList() : getTemplateList(createContext
                        .getBaseType().getCode());
        Set<ObjectType> excludedTypes = (createContext == null) ? Collections.EMPTY_SET : createContext.getExcludedTypes();
        Set<ObjectType> allowedTypes = (createContext == null) ? Collections.EMPTY_SET : createContext.getAllowedTypes();
        for(TemplateListEntry entry : entries)
        {
            if(!canCreate(entry) || (
                            !allowedTypes.isEmpty() && !allowedTypes.contains(entry.getTemplate())) || excludedTypes.contains(entry
                            .getTemplate()))
            {
                continue;
            }
            Menuitem menuitem = new Menuitem();
            menuitem.setParent((Component)menupopup);
            String entryLabel = getTemplateName(entry.getTemplate());
            menuitem.setLabel((entryLabel == null) ? entry.getLabel() : entryLabel);
            menuitem.setStyle("padding-left:" + entry.getDepth() + "em");
            if(entry.isAbstract())
            {
                menuitem.setDisabled(true);
                continue;
            }
            menuitem.addEventListener("onClick", (EventListener)new Object(this, entry, createContext, initValues, loadDefaultValues, expandEditorArea, popup));
        }
        return (menupopup.getChildren() != null && menupopup.getChildren().size() > 1);
    }


    public List<TemplateListEntry> generateTemplateEntryList(CreateContext createContext)
    {
        List<TemplateListEntry> ret = new ArrayList<>();
        Set<ObjectType> allowedTypes = (createContext == null) ? Collections.EMPTY_SET : createContext.getAllowedTypes();
        Set<ObjectType> excludedTypes = (createContext == null) ? Collections.EMPTY_SET : createContext.getExcludedTypes();
        List<TemplateListEntry> entries = new ArrayList<>();
        if(allowedTypes.isEmpty())
        {
            entries = (createContext == null || createContext.getBaseType() == null) ? getTemplateList() : getTemplateList((createContext.getBaseType() instanceof ObjectTemplate) ? (
                            (ObjectTemplate)createContext.getBaseType()).getBaseType().getCode() : createContext.getBaseType().getCode());
        }
        else
        {
            int index = 0;
            for(ObjectType te : allowedTypes)
            {
                entries.add(new TemplateListEntry(
                                UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(te.getCode()), index, 0));
                index++;
            }
        }
        for(TemplateListEntry entry : entries)
        {
            if((!allowedTypes.isEmpty() && !allowedTypes.contains(entry.getTemplate())) || excludedTypes.contains(entry
                            .getTemplate()))
            {
                continue;
            }
            ret.add(entry);
        }
        return ret;
    }


    public void closeAreasIfOverlapped()
    {
        LayoutRegion navigationAreaComp = getNavigationAreaComponent();
        if(navigationAreaComp != null && navigationAreaComp.isCollapsible() && !navigationAreaComp.isOpen())
        {
            navigationAreaComp.invalidate();
        }
    }


    public boolean canCreate(TemplateListEntry entry)
    {
        return (entry.isAbstract() ||
                        UISessionUtils.getCurrentSession().getSystemService()
                                        .checkPermissionOn(entry.getTemplate().getBaseType().getCode(), "create"));
    }


    public void setForceOpenInEditorArea(boolean forceOpenInEditorArea)
    {
        this.forceOpenInEditorArea = forceOpenInEditorArea;
    }


    public boolean isForceOpenInEditorArea()
    {
        return this.forceOpenInEditorArea;
    }
}
