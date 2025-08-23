package de.hybris.platform.cmscockpit.wizard.cmssite.pages;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

public class CmsSiteSummaryPage extends GenericItemMandatoryPage
{
    private static final String SUMMARY_ROW_SCLASS = "summaryRow";
    private static final String SUMMARY_PAGE_CNT_SCLASS = "summaryPageCnt";
    private static final String SITE_NAME_QUALIFIER = "siteName";
    private static final String SITE_ACTIVE_QUALIFIER = "active";
    private static final String SITE_STORES_QUALIFIER = "stores";
    private static final String SITE_TEMPLATES_QUALIFIER = "templates";
    private static final String SITE_CONTENTCATALOG_NAME_QUALIFIER = "contentcatalogname";
    protected static final String BOOLEAN_TRUE_IMG = "/cockpit/images/bool_true.gif";
    protected static final String BOOLEAN_FALSE_IMG = "/cockpit/images/bool_false.gif";
    protected static final String BOOLEAN_NULL_IMG = "/cockpit/images/bool_null.gif";
    private static final String COCKPIT_ID_CREATEWEBSITE_SUMMARY_NAME_INPUT = "CreateWebsite_Summary_Name_input";
    private static final String COCKPIT_ID_CREATEWEBSITE_SUMMARY_ACTIVE_INPUT = "CreateWebsite_Summary_Active_image";
    private static final String COCKPIT_ID_CREATEWEBSITE_SUMMARY_BASESTORES_INPUT = "CreateWebsite_Summary_Basestores_input";
    private static final String COCKPIT_ID_CREATEWEBSITE_SUMMARY_PAGETEMPLATES_INPUT = "CreateWebsite_Summary_Pagetemplates_input";
    private static final String COCKPIT_ID_CREATEWEBSITE_SUMMARY_CONTENTCATALOGS_INPUT = "CreateWebsite_Summary_Contentcatalogs_input";


    public CmsSiteSummaryPage()
    {
    }


    public CmsSiteSummaryPage(String pageTitle)
    {
        super(pageTitle);
    }


    public CmsSiteSummaryPage(String pageTitle, GenericItemWizard wizard)
    {
        super(pageTitle, wizard);
    }


    public Component createSummaryEntryRow(String labelValue, Component value)
    {
        Hbox summaryEntryRow = new Hbox();
        summaryEntryRow.setWidths("45%,55%");
        summaryEntryRow.setSclass("summaryRow");
        Label summaryInfoLabel = new Label(labelValue);
        summaryEntryRow.appendChild((Component)summaryInfoLabel);
        summaryEntryRow.appendChild(value);
        return (Component)summaryEntryRow;
    }


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        ListboxRenderer listboxRenderer = new ListboxRenderer(this);
        Div labelInfoContainer = new Div();
        labelInfoContainer.setSclass("wizardLabelContainer");
        labelInfoContainer.setParent((Component)this.pageContent);
        Label labelInfo = new Label(Labels.getLabel("summary.page.label.info"));
        labelInfoContainer.appendChild((Component)labelInfo);
        Vbox contextInformation = new Vbox();
        contextInformation.setSclass("summaryPageCnt");
        contextInformation.setParent((Component)this.pageContent);
        Map<String, Object> information = new HashMap<>(collectAllInformation());
        Textbox nameBox = new Textbox();
        nameBox.setReadonly(true);
        nameBox.setValue((String)information.get("siteName"));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)nameBox, "CreateWebsite_Summary_Name_input");
        }
        contextInformation.appendChild(createSummaryEntryRow(Labels.getLabel("wizard.summarypage.cmssite.name"), (Component)nameBox));
        Object object = information.get("active");
        Image booleanImg = new Image();
        if(Boolean.TRUE.equals(object))
        {
            booleanImg.setSrc("/cockpit/images/bool_true.gif");
        }
        else if(Boolean.FALSE.equals(object))
        {
            booleanImg.setSrc("/cockpit/images/bool_false.gif");
        }
        else
        {
            booleanImg.setSrc("/cockpit/images/bool_null.gif");
        }
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)booleanImg, "CreateWebsite_Summary_Active_image");
        }
        contextInformation.appendChild(createSummaryEntryRow(Labels.getLabel("wizard.summarypage.cmssite.active"), (Component)booleanImg));
        Listbox storesCombobox = new Listbox();
        storesCombobox.setItemRenderer((ListitemRenderer)listboxRenderer);
        storesCombobox.setModel((ListModel)new SimpleListModel((List)information.get("stores")));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)storesCombobox, "CreateWebsite_Summary_Basestores_input");
        }
        contextInformation.appendChild(createSummaryEntryRow(Labels.getLabel("wizard.summarypage.cmssite.stores"), (Component)storesCombobox));
        Listbox tempaltesCombobox = new Listbox();
        tempaltesCombobox.setItemRenderer((ListitemRenderer)listboxRenderer);
        tempaltesCombobox.setModel((ListModel)new SimpleListModel((List)information.get("templates")));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)tempaltesCombobox, "CreateWebsite_Summary_Pagetemplates_input");
        }
        contextInformation.appendChild(createSummaryEntryRow(Labels.getLabel("wizard.summarypage.cmssite.templates"), (Component)tempaltesCombobox));
        String contentCatalogName = (String)information.get("contentcatalogname");
        if(StringUtils.isNotBlank(contentCatalogName))
        {
            Textbox contentCatalogNameBox = new Textbox();
            contentCatalogNameBox.setReadonly(true);
            contentCatalogNameBox.setValue(contentCatalogName);
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                UITools.applyTestID((Component)contentCatalogNameBox, "CreateWebsite_Summary_Contentcatalogs_input");
            }
            contextInformation.appendChild(createSummaryEntryRow(Labels.getLabel("wizard.summarypage.cmssite.contentcatalog.name"), (Component)contentCatalogNameBox));
        }
        else
        {
            Listbox selectedContentCatalogs = new Listbox();
            selectedContentCatalogs.setItemRenderer((ListitemRenderer)listboxRenderer);
            selectedContentCatalogs.setModel((ListModel)new SimpleListModel((List)information.get("selectedcontentcatalogs")));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                UITools.applyTestID((Component)selectedContentCatalogs, "CreateWebsite_Summary_Contentcatalogs_input");
            }
            contextInformation.appendChild(createSummaryEntryRow(Labels.getLabel("wizard.summarypage.cmssite.contentcatalogs"), (Component)selectedContentCatalogs));
        }
        return (Component)this.pageContainer;
    }


    protected Map<String, Object> collectAllInformation()
    {
        Map<String, Object> finalContextInformation = new HashMap<>();
        PropertyDescriptor desc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("CMSSite.name");
        finalContextInformation.put("siteName",
                        getWizard().getObjectValueContainer().getValue(desc, UISessionUtils.getCurrentSession().getGlobalDataLanguageIso())
                                        .getLocalValue());
        desc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("CMSSite.active");
        finalContextInformation.put("active", getWizard().getObjectValueContainer().getValue(desc, null).getLocalValue());
        desc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("CMSSite.stores");
        finalContextInformation.put("stores", getWizard().getObjectValueContainer().getValue(desc, null).getLocalValue());
        finalContextInformation.putAll(getWizard().getContext());
        return finalContextInformation;
    }
}
