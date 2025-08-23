package de.hybris.platform.cockpit.components.sync.dialog;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Popup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public abstract class AbstractSyncDialog extends Window
{
    private static final Logger log = LoggerFactory.getLogger(AbstractSyncDialog.class);
    private static final String CENTER = "center";
    private static final String HORIZONTAL = "horizontal";
    private static final String _100PERCENT = "100%";
    protected static final String AFTER_SYNC_EVENT = "onAfterSynchronization";
    protected static final String NAME_ATTRIBUTE = "name";
    private TypedObject sourceItem;
    private Listbox availableCatalogVersions;
    private List<SyncItemJobModel>[] matrixRules;
    private Map<String, String>[] rules;
    private CatalogVersionModel sourceCatalogVersion;


    public AbstractSyncDialog(TypedObject sourceItem, List<SyncItemJobModel>[] matrixRules)
    {
        this.sourceItem = sourceItem;
        this.matrixRules = matrixRules;
        constructDialog();
    }


    public AbstractSyncDialog(CatalogVersionModel sourceCatalogVersion, List<SyncItemJobModel>[] matrixRules)
    {
        this.sourceCatalogVersion = sourceCatalogVersion;
        this.matrixRules = matrixRules;
        constructDialog();
    }


    public AbstractSyncDialog(Map<String, String>[] rules)
    {
        this.rules = rules;
        constructDialog();
    }


    public CatalogVersionModel getSourceCatalogVersion()
    {
        return this.sourceCatalogVersion;
    }


    public List<SyncItemJobModel>[] getTargetCatalogVersions()
    {
        return this.matrixRules;
    }


    public Listbox getAvailableCatalogVersions()
    {
        return this.availableCatalogVersions;
    }


    public Map<String, String>[] getSyncRules()
    {
        return this.rules;
    }


    private final void constructDialog()
    {
        Hbox currentCatalogHeader = null;
        boolean twoTabs = (this.sourceCatalogVersion != null || this.sourceItem != null);
        boolean syncRulesAvailable = true;
        setSclass("synchroznizationConfig");
        setWidth("380px");
        setClosable(false);
        setPosition("center");
        setSizable(false);
        setTitle(Labels.getLabel("synchronization.config"));
        setShadow(false);
        Tabbox tabbox = new Tabbox();
        tabbox.setOrient("horizontal");
        tabbox.setPanelSpacing("2px");
        Tabs tabsContainer = new Tabs();
        tabsContainer.setParent((Component)tabbox);
        Tabpanels tabsPanels = new Tabpanels();
        tabsPanels.setParent((Component)tabbox);
        Tab targetCatalogTab = new Tab();
        Vbox targetCatalogHeader = new Vbox();
        targetCatalogHeader.setWidth("100%");
        targetCatalogHeader.setSclass("targetCatalogHeader");
        Label targetCatalogVersion = new Label();
        targetCatalogVersion.setValue(Labels.getLabel("sync.config.target.catalogversion"));
        if(twoTabs)
        {
            CatalogVersionModel source = (this.sourceCatalogVersion == null) ? getSynchronizationService().getCatalogVersionForItem(this.sourceItem) : this.sourceCatalogVersion;
            Label currentCatalogVersion = new Label();
            currentCatalogVersion.setValue(Labels.getLabel("sync.config.source.catalogversion"));
            Hbox labelBox = prepareCatalogVersionLabels(source, null, null);
            currentCatalogHeader = new Hbox();
            currentCatalogHeader.setSclass("sourceCatalogHeader");
            currentCatalogHeader.appendChild((Component)currentCatalogVersion);
            currentCatalogHeader.appendChild((Component)labelBox);
            currentCatalogHeader.setAlign("center");
            currentCatalogHeader.setHeight("40px");
            Tab currentCatalogTab = new Tab();
            currentCatalogTab.setParent((Component)tabsContainer);
            currentCatalogTab.setLabel(Labels.getLabel("sync.tab.source"));
            targetCatalogTab.setLabel(Labels.getLabel("sync.tab.target"));
            targetCatalogHeader.appendChild((Component)targetCatalogVersion);
            Space spacer = new Space();
            spacer.setHeight("5px");
            spacer.setOrient("vertical");
            targetCatalogHeader.appendChild((Component)spacer);
            Tabpanel sourcePanel = new Tabpanel();
            sourcePanel.setParent((Component)tabsPanels);
            sourcePanel.appendChild((Component)currentCatalogHeader);
        }
        else
        {
            targetCatalogTab.setLabel(Labels.getLabel("sync.rules"));
        }
        targetCatalogTab.setParent((Component)tabsContainer);
        Label forbidenCatalogVersion = new Label();
        forbidenCatalogVersion.setValue(Labels.getLabel("sync.unavailable.rules"));
        Div mainContainer = new Div();
        Vbox verticalContainer = new Vbox();
        verticalContainer.setHeight("100%");
        verticalContainer.setWidth("100%");
        this.availableCatalogVersions = createAvailableRuleList(twoTabs);
        if(this.availableCatalogVersions.getItemCount() == 0)
        {
            syncRulesAvailable = false;
        }
        this.availableCatalogVersions.addEventListener("onAfterSynchronization", (EventListener)new Object(this));
        targetCatalogHeader.appendChild((Component)this.availableCatalogVersions);
        if(!getForbidenRulesListModel().isEmpty())
        {
            Listbox forbiddenCatalogVersions = createForbiddenRuleList(targetCatalogHeader);
            Space spacer = new Space();
            spacer.setHeight("5px");
            spacer.setOrient("vertical");
            targetCatalogHeader.appendChild((Component)spacer);
            targetCatalogHeader.appendChild((Component)forbidenCatalogVersion);
            spacer = new Space();
            spacer.setHeight("5px");
            spacer.setOrient("vertical");
            targetCatalogHeader.appendChild((Component)spacer);
            targetCatalogHeader.appendChild((Component)forbiddenCatalogVersions);
        }
        verticalContainer.appendChild((Component)targetCatalogHeader);
        Hbox footer = new Hbox();
        footer.setAlign("right");
        Button doCancel = new Button();
        doCancel.setLabel(Labels.getLabel("sync.close"));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "SyncCloseButton_";
            UITools.applyTestID((Component)doCancel, "SyncCloseButton_");
        }
        doCancel.addEventListener("onClick", (EventListener)new Object(this));
        Button doSync = new Button();
        doSync.setLabel(Labels.getLabel("sync.ok"));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "SyncOkButton_";
            UITools.applyTestID((Component)doSync, "SyncOkButton_");
        }
        doSync.addEventListener("onClick", (EventListener)new Object(this));
        if(!syncRulesAvailable && !twoTabs)
        {
            targetCatalogTab.setLabel(Labels.getLabel("sync.available.rules.list.empty"));
        }
        if(syncRulesAvailable)
        {
            footer.appendChild((Component)doSync);
        }
        footer.appendChild((Component)doCancel);
        verticalContainer.appendChild((Component)footer);
        Tabpanel targetPanel = new Tabpanel();
        targetPanel.setParent((Component)tabsPanels);
        targetPanel.appendChild((Component)verticalContainer);
        mainContainer.appendChild((Component)tabbox);
        appendChild((Component)mainContainer);
    }


    protected Hbox prepareCatalogVersionLabels(CatalogVersionModel catalogVersion, SyncRule rule, Listitem item)
    {
        try
        {
            String catalogname = "";
            String label = "";
            String mnemonicLabel = "";
            String labelLanguage = null;
            Hbox horizontal = new Hbox();
            horizontal.setWidths("90%,10%");
            horizontal.setSclass("catalogVersionLabel");
            CatalogModel catalog = catalogVersion.getCatalog();
            Locale locale = UISessionUtils.getCurrentSession().getGlobalDataLocale();
            if(catalog.getName(locale) != null)
            {
                catalogname = catalog.getName(locale);
            }
            else
            {
                List<String> result = UITools.searchForLabel(catalog, CatalogModel.class.getMethod("getName", new Class[] {Locale.class}), catalogVersion
                                .getLanguages());
                if(result.size() == 2)
                {
                    catalogname = result.get(0);
                    labelLanguage = result.get(1);
                }
            }
            label = ((catalogname != null) ? catalogname : ("<" + catalog.getId() + ">")) + " " + ((catalogname != null) ? catalogname : ("<" + catalog.getId() + ">"));
            if(item != null)
            {
                item.setAttribute("name", label);
            }
            if(rule != null)
            {
                label = label + " (" + label + ")";
            }
            mnemonicLabel = catalogVersion.getMnemonic();
            Label cellLabel = new Label();
            cellLabel.setValue(label);
            horizontal.appendChild((Component)cellLabel);
            if(rule != null)
            {
                Label ruleName = new Label("(" + rule.getName() + ")");
                ruleName.setSclass("ruleNameLabelSclass");
            }
            if(mnemonicLabel != null)
            {
                Label mnemLabel = new Label(" (" + mnemonicLabel + ")");
                mnemLabel.setSclass("catalog-mnemonic-label");
                horizontal.appendChild((Component)mnemLabel);
            }
            if(labelLanguage != null)
            {
                Label langLabel = new Label(" [" + labelLanguage + "]");
                langLabel.setSclass("catalog-language-label");
                horizontal.appendChild((Component)langLabel);
            }
            return horizontal;
        }
        catch(IllegalAccessException e)
        {
            log.error(e.getMessage(), e);
        }
        catch(NoSuchMethodException e)
        {
            log.error(e.getMessage(), e);
        }
        catch(InvocationTargetException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    protected Listbox createAvailableRuleList(boolean twoTabs)
    {
        Listbox availableCatalogVersions = new Listbox();
        availableCatalogVersions.setOddRowSclass("no");
        availableCatalogVersions.setSclass("availableCatalogVersions");
        availableCatalogVersions.setWidth("100%");
        availableCatalogVersions.setMultiple(true);
        availableCatalogVersions.setModel((ListModel)new SimpleListModel(getAccessibleRulesListModel()));
        if(getAccessibleRulesListModel() != null && !getAccessibleRulesListModel().isEmpty())
        {
            availableCatalogVersions.setSelectedIndex(0);
        }
        if(!twoTabs)
        {
            availableCatalogVersions.setMultiple(true);
        }
        availableCatalogVersions.setItemRenderer((ListitemRenderer)new Object(this));
        return availableCatalogVersions;
    }


    protected Listbox createForbiddenRuleList(Vbox parent)
    {
        Listbox forbiddenCatalogVersions = new Listbox();
        forbiddenCatalogVersions.setOddRowSclass("no");
        forbiddenCatalogVersions.setSclass("forbidenCatalogVersions");
        forbiddenCatalogVersions.setModel((ListModel)new SimpleListModel(getForbidenRulesListModel()));
        forbiddenCatalogVersions.setDisabled(true);
        Popup toolTip = new Popup();
        toolTip.setParent((Component)parent);
        Label tooltipLabel = new Label(Labels.getLabel("sync.initial.tooltip"));
        tooltipLabel.setParent((Component)toolTip);
        forbiddenCatalogVersions.setTooltip(toolTip);
        forbiddenCatalogVersions.setItemRenderer((ListitemRenderer)new Object(this));
        return forbiddenCatalogVersions;
    }


    public SynchronizationService getSynchronizationService()
    {
        return (SynchronizationService)SpringUtil.getBean("synchronizationService");
    }


    public abstract void performAction();


    public void updateBackground(List<String> chosenRules)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().update();
        UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().update();
    }


    public abstract void performReturn();


    public abstract List<SyncRule> getAccessibleRulesListModel();


    public abstract List<SyncRule> getForbidenRulesListModel();


    public abstract void itemListRenderer(Listitem paramListitem, Object paramObject);
}
