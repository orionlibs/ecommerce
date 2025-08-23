package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.NavigationPanelSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.daos.TypeDefinitionDao;
import de.hybris.platform.cockpit.model.browser.BrowserModelFactory;
import de.hybris.platform.cockpit.model.browser.impl.DefaultExtendedSearchBrowserModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TreeUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.ValuePair;
import de.hybris.platform.core.PK;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

public class TypeSelectorSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final String EXTENDED_SEARCH_BROWSER_MODEL = "extendedSearchBrowserModel";
    private static final Logger LOG = LoggerFactory.getLogger(TypeSelectorSectionRenderer.class);
    private BrowserModelFactory browserModelFactory;
    private TypeDefinitionDao typeDefinitionDao;
    private Map<PK, List<ValuePair<String, String>>> typeCacheMap = null;
    private final Map<String, PK> typeCode2PKMap = new HashMap<>();
    private String[] typeCodes;
    private String[] inactiveTypeCodes;
    private boolean includeSubtypes = false;
    private Type displayType = Type.LIST;
    private TypeService cockpitTypeService;
    private int pageSize = 0;
    private boolean searchable = false;
    private boolean allowDuplicates = false;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(Type.LIST.equals(this.displayType))
        {
            renderList(panel, parent, captionComponent, section);
        }
        else if(Type.TREE.equals(this.displayType))
        {
            renderTree(panel, parent, captionComponent, section);
        }
    }


    private void createTypeMap()
    {
        Date start = new Date();
        this.typeCacheMap = new HashMap<>();
        List<TypeDefinitionDao.TypeDefinition> allTypeDefinitions = this.typeDefinitionDao.findAllTypeDefinitions();
        for(TypeDefinitionDao.TypeDefinition typeDefinition : allTypeDefinitions)
        {
            this.typeCode2PKMap.put(typeDefinition.getCode(), typeDefinition.getPk());
            if(typeDefinition.getSupertypePk() != null)
            {
                List<ValuePair<String, String>> list = this.typeCacheMap.get(typeDefinition.getSupertypePk());
                if(list == null)
                {
                    list = new ArrayList<>();
                    this.typeCacheMap.put(typeDefinition.getSupertypePk(), list);
                }
                list.add(new ValuePair(typeDefinition.getCode(), typeDefinition.getName()));
            }
            if(!this.typeCacheMap.containsKey(typeDefinition.getPk()))
            {
                this.typeCacheMap.put(typeDefinition.getPk(), new ArrayList<>());
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating typemap took: " + (new Date()).getTime() - start.getTime() + "ms");
            LOG.debug("No. of types: " + allTypeDefinitions.size());
        }
    }


    private List<ValuePair<String, String>> getSubTypeCodes(String type)
    {
        if(this.typeCacheMap == null)
        {
            createTypeMap();
        }
        return this.typeCacheMap.get(this.typeCode2PKMap.get(type));
    }


    protected void renderTree(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Div componentContainer = new Div();
        componentContainer.setSclass("typeSelectorTree");
        Tree tree = new Tree();
        tree.setVflex(false);
        tree.setWidth("auto");
        tree.setZclass("z-dottree");
        if(this.pageSize > 0)
        {
            tree.setMold("paging");
            tree.setRows(this.pageSize);
            tree.setPageSize(this.pageSize);
            componentContainer.setSclass("typeSelectorTree pageableTree");
        }
        else
        {
            componentContainer.setSclass("typeSelectorTree");
        }
        Object rootDummy = new Object();
        Object object1 = getTreeModel(section);
        if(object1 == null)
        {
            object1 = new Object(this, rootDummy, rootDummy);
        }
        setTreeModel(tree, (TreeModel)object1, section);
        Object object2 = (object1 instanceof TreeUtils.FilteredTreeModel) ? ((TreeUtils.FilteredTreeModel)object1).getOriginalModel() : object1;
        tree.addEventListener("onSelect", (EventListener)new Object(this, tree));
        tree.setTreeitemRenderer((TreeitemRenderer)new Object(this, section, tree));
        if(this.searchable)
        {
            Div searchCnt = new Div();
            searchCnt.setSclass("searchBar");
            Textbox searchBox = new Textbox();
            Object object = new Object(this, searchBox, (TreeModel)object2, section, tree, componentContainer);
            searchBox.addEventListener("onOK", (EventListener)object);
            searchBox.addEventListener("onChanging", (EventListener)object);
            searchBox.addEventListener("onSearchLater", (EventListener)new Object(this, tree, componentContainer));
            Menuitem nameItem = new Menuitem(Labels.getLabel("navigationarea.typetree.menu.showname"));
            Menuitem codeItem = new Menuitem(Labels.getLabel("navigationarea.typetree.menu.showcode"));
            searchBox.setCtrlKeys("#down@c");
            searchBox.addEventListener("onCtrlKey", (EventListener)new Object(this, tree, nameItem, codeItem, section));
            searchCnt.appendChild((Component)searchBox);
            Button searchBtn = new Button(Labels.getLabel(""));
            searchBtn.setSclass("advSearchBtn");
            searchBtn.setTooltiptext(Labels.getLabel("general.search"));
            searchBtn.setImage("/cockpit/images/BUTTON_search.png");
            searchBtn.addEventListener("onClick", (EventListener)object);
            searchCnt.appendChild((Component)searchBtn);
            Button optionsBtn = new Button();
            optionsBtn.setSclass("optionsBtn");
            optionsBtn.setImage("/cockpit/css/images/panel_button_down.png");
            optionsBtn.setTooltiptext(Labels.getLabel("navigationarea.search.options"));
            Menupopup optionsMP = new Menupopup();
            nameItem.setChecked(!isShowTypeCode(section));
            nameItem.addEventListener("onClick", (EventListener)new Object(this, nameItem, codeItem, section, tree));
            codeItem.setChecked(isShowTypeCode(section));
            codeItem.addEventListener("onClick", (EventListener)new Object(this, nameItem, codeItem, section, tree));
            optionsMP.appendChild((Component)nameItem);
            optionsMP.appendChild((Component)codeItem);
            searchCnt.appendChild((Component)optionsMP);
            optionsBtn.setPopup((Popup)optionsMP);
            searchCnt.appendChild((Component)optionsBtn);
            if(object1 instanceof TreeUtils.FilteredTreeModel)
            {
                searchBox.setText(((TreeUtils.FilteredTreeModel)object1).getFilterString());
                for(Object item : new ArrayList(tree.getItems()))
                {
                    if(item instanceof Treeitem)
                    {
                        ((Treeitem)item).setOpen(true);
                    }
                }
                tree.setAttribute("searchMode", Boolean.TRUE);
                tree.setModel(tree.getModel());
            }
            Image img = new Image();
            img.setSclass("searchIndicator");
            searchCnt.appendChild((Component)img);
            componentContainer.appendChild((Component)searchCnt);
        }
        componentContainer.appendChild((Component)tree);
        parent.appendChild((Component)componentContainer);
    }


    private void updateTypeCodeSelection(Menuitem nameItem, Menuitem codeItem, Section section, Tree tree, boolean showCode)
    {
        showTypeCode(showCode, section);
        nameItem.setChecked(!showCode);
        codeItem.setChecked(showCode);
        tree.setModel(tree.getModel());
    }


    protected void setTreeModel(Tree tree, TreeModel model, Section section)
    {
        tree.setModel(model);
        if(section instanceof NavigationPanelSection)
        {
            ((NavigationPanelSection)section).setAttribute("treeModel", model);
        }
    }


    protected TreeModel getTreeModel(Section section)
    {
        if(section instanceof NavigationPanelSection)
        {
            return (TreeModel)((NavigationPanelSection)section).getAttribute("treeModel");
        }
        return null;
    }


    protected boolean isShowTypeCode(Section section)
    {
        if(section instanceof NavigationPanelSection)
        {
            return Boolean.TRUE.equals(((NavigationPanelSection)section).getAttribute("showTypeCode"));
        }
        return false;
    }


    protected void showTypeCode(boolean value, Section section)
    {
        if(section instanceof NavigationPanelSection)
        {
            ((NavigationPanelSection)section).setAttribute("showTypeCode", Boolean.valueOf(value));
        }
    }


    protected TreeUtils.TreeState getTreeState(Section section)
    {
        if(section instanceof NavigationPanelSection)
        {
            Object treeStateAttribute = ((NavigationPanelSection)section).getAttribute("treeState");
            if(!(treeStateAttribute instanceof TreeUtils.TreeState))
            {
                treeStateAttribute = new TreeUtils.TreeState();
                ((NavigationPanelSection)section).setAttribute("treeState", treeStateAttribute);
            }
            return (TreeUtils.TreeState)treeStateAttribute;
        }
        return null;
    }


    protected void renderList(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Listbox typeListbox = new Listbox();
        typeListbox.setParent(parent);
        typeListbox.setSclass("typeListbox");
        if(this.pageSize > 0)
        {
            typeListbox.setMold("paging");
            typeListbox.setPageSize(this.pageSize);
            typeListbox.setRows(this.pageSize);
        }
        if(this.typeCodes != null)
        {
            Collection<String> types = new LinkedHashSet<>(Arrays.asList(this.typeCodes));
            if(this.includeSubtypes)
            {
                types = addSubtypes(types);
            }
            for(String typeCode : types)
            {
                if(canRead(typeCode))
                {
                    typeListbox.appendChild((Component)createOpenBrowserComponent(typeCode));
                }
            }
        }
    }


    protected boolean isRootType(String typeCode)
    {
        return ArrayUtils.contains((Object[])this.typeCodes, typeCode);
    }


    protected Collection<String> addSubtypes(Collection<String> typeCodes)
    {
        Collection<ObjectType> typeSet = new LinkedHashSet<>();
        for(String typeCode : typeCodes)
        {
            typeSet.add(getCockpitTypeService().getObjectType(typeCode));
            typeSet.addAll(getCockpitTypeService().getAllSubtypes(getCockpitTypeService().getObjectType(typeCode)));
        }
        Collection<String> ret = new LinkedHashSet<>();
        for(ObjectType type : typeSet)
        {
            ret.add(type.getCode());
        }
        return ret;
    }


    protected Listitem createOpenBrowserComponent(String typeCode)
    {
        Listitem typeRow = new Listitem(typeCode);
        UITools.addBusyListener((Component)typeRow, "onClick", (EventListener)new Object(this, typeCode), null, null);
        return typeRow;
    }


    protected void openSearchBrowser(String rootTypeCode)
    {
        if(!canRead(rootTypeCode))
        {
            return;
        }
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        ObjectTemplate rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(rootTypeCode);
        BrowserModel createdBrowserModel = (this.browserModelFactory == null) ? (BrowserModel)new DefaultExtendedSearchBrowserModel() : this.browserModelFactory.createBrowserModel("extendedSearchBrowserModel");
        if(!(createdBrowserModel instanceof DefaultExtendedSearchBrowserModel))
        {
            LOG.error("Bean with id 'extendedSearchBrowserModel' not an instance of 'DefaultExtendedSearchBrowserModel'");
            return;
        }
        DefaultExtendedSearchBrowserModel browserModel = (DefaultExtendedSearchBrowserModel)createdBrowserModel;
        browserModel.setRootType(rootType);
        browserModel.setLabel(rootType.getName());
        if(browserArea.getFocusedBrowser() instanceof DefaultExtendedSearchBrowserModel)
        {
            browserArea.replaceBrowser(browserArea.getFocusedBrowser(), (BrowserModel)browserModel);
        }
        else
        {
            browserArea.addVisibleBrowser((BrowserModel)browserModel, false);
        }
        browserModel.focus();
        browserModel.updateItems();
    }


    private boolean canRead(String typeCode)
    {
        return (UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(typeCode, "read") &&
                        !ArrayUtils.contains((Object[])this.inactiveTypeCodes, typeCode));
    }


    public void setTypeCodes(String[] typeCodes)
    {
        this.typeCodes = typeCodes;
    }


    public void setInactiveTypeCodes(String[] inactiveTypeCodes)
    {
        this.inactiveTypeCodes = inactiveTypeCodes;
    }


    public void setIncludeSubtypes(boolean includeSubtypes)
    {
        this.includeSubtypes = includeSubtypes;
    }


    public boolean isIncludeSubtypes()
    {
        return this.includeSubtypes;
    }


    public void setDisplayType(String type)
    {
        Type value = Type.valueOf(type);
        if(value != null)
        {
            this.displayType = value;
        }
    }


    public String getDisplayType()
    {
        return this.displayType.name();
    }


    @Required
    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }


    public TypeService getCockpitTypeService()
    {
        return this.cockpitTypeService;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public void setSearchable(boolean searchable)
    {
        this.searchable = searchable;
    }


    public boolean isSearchable()
    {
        return this.searchable;
    }


    @Required
    public void setBrowserModelFactory(BrowserModelFactory browserModelFactory)
    {
        this.browserModelFactory = browserModelFactory;
    }


    @Required
    public void setTypeDefinitionDao(TypeDefinitionDao typeDefinitionDao)
    {
        this.typeDefinitionDao = typeDefinitionDao;
    }


    public void setAllowDuplicates(boolean allowDuplicates)
    {
        this.allowDuplicates = allowDuplicates;
    }
}
