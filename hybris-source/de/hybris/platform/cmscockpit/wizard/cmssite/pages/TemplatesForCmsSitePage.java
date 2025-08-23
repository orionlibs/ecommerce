package de.hybris.platform.cmscockpit.wizard.cmssite.pages;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class TemplatesForCmsSitePage extends AbstractGenericItemPage
{
    protected static final String WIZARD_TEMPLATES_CONTAINER = "templatesForCmsSitePage";
    protected static final String WIZARD_TEMPLATES_LISTBOX = "wizardTemplatesListbox";
    protected static final String WIZARD_TEMPLATES_LISTBOX_ODD = "wizardTemplatesListboxOdd";
    protected static final String WIZARD_TEMPLATE_LIST_ENTRY = "templateListEntry";
    protected static final String WIZARD_TEMPLATE_CONTEXT_INFO = "templates";
    protected static final String CHECKBOX_VALUE_ATTRIBUTE = "value";
    private static final String COCKPIT_ID_CREATEWEBSITE_PAGETEMPLATE = "CreateWebsite_PageTemplate_";
    private CatalogService catalogService;
    private CMSAdminPageService cmsAdminPageService;
    private ListModel simpleListModel;
    private final List<TypedObject> selectedTemplates = new ArrayList<>();


    public TemplatesForCmsSitePage()
    {
    }


    public TemplatesForCmsSitePage(String pageTitle)
    {
        super(pageTitle);
    }


    public TemplatesForCmsSitePage(String pageTitle, GenericItemWizard wizard)
    {
        super(pageTitle, (Wizard)wizard);
    }


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        Div labelContainer = new Div();
        labelContainer.setSclass("wizardLabelContainer");
        Label pageLabelInfo = new Label(Labels.getLabel("wizard.templates.infolabel"));
        labelContainer.appendChild((Component)pageLabelInfo);
        labelContainer.setParent((Component)this.pageContent);
        Div templatesContainer = new Div();
        templatesContainer.setSclass("templatesForCmsSitePage");
        templatesContainer.setParent((Component)this.pageContent);
        Listbox templatesListbox = new Listbox();
        templatesListbox.setSclass("wizardTemplatesListbox");
        templatesListbox.setOddRowSclass("wizardTemplatesListboxOdd");
        templatesListbox.setModel(getInitializedListModel());
        templatesListbox.setMultiple(true);
        templatesListbox.setItemRenderer((ListitemRenderer)new Object(this));
        this.pageContent.appendChild((Component)templatesListbox);
        setController((WizardPageController)new Object(this));
        return (Component)this.pageContainer;
    }


    protected ListModel getInitializedListModel()
    {
        if(this.simpleListModel == null)
        {
            this
                            .simpleListModel = (ListModel)new SimpleListModel(new ArrayList(UISessionUtils.getCurrentSession().getTypeService().wrapItems(getPagetTemplates())));
        }
        return this.simpleListModel;
    }


    protected Collection<PageTemplateModel> getPagetTemplates()
    {
        return getCmsAdminPageService().getAllPageTemplates(getAllActiveCatalogVersions());
    }


    protected List<CatalogVersionModel> getAllActiveCatalogVersions()
    {
        List<CatalogVersionModel> activeCatalogVersions = new ArrayList<>();
        for(CatalogModel catalogModel : getCatalogService().getAllCatalogs())
        {
            if(catalogModel instanceof de.hybris.platform.cms2.model.contents.ContentCatalogModel)
            {
                if(catalogModel.getActiveCatalogVersion() != null)
                {
                    activeCatalogVersions.add(catalogModel.getActiveCatalogVersion());
                }
            }
        }
        return activeCatalogVersions;
    }


    public CatalogService getCatalogService()
    {
        if(this.catalogService == null)
        {
            this.catalogService = (CatalogService)SpringUtil.getBean("catalogService");
        }
        return this.catalogService;
    }


    protected CMSAdminPageService getCmsAdminPageService()
    {
        if(this.cmsAdminPageService == null)
        {
            this.cmsAdminPageService = (CMSAdminPageService)SpringUtil.getBean("cmsAdminPageService");
        }
        return this.cmsAdminPageService;
    }
}
