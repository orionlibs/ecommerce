package de.hybris.platform.cmscockpit.wizard;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.wizard.controller.CmsReferencePageController;
import de.hybris.platform.cmscockpit.wizard.controller.CreateCmsPageController;
import de.hybris.platform.cmscockpit.wizard.page.CmsPageSelectorPage;
import de.hybris.platform.cmscockpit.wizard.page.CmsPageSelectorPageFactory;
import de.hybris.platform.cmscockpit.wizard.page.CreatePageMandatoryPage;
import de.hybris.platform.cmscockpit.wizard.page.ReferencePage;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

public class CmsCreatePageWizard
{
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String DEFAULT_WIZARD_FRAME = "/cockpit/wizards/defaultWizardFrame.zul";
    protected static final String CMSITEM_CATALOGVERSION = "CMSItem.catalogVersion";
    protected static final String CMSITEM_UID = "CMSItem.uid";
    protected static final String ABSTRACTPAGE_UID_PREFIX = "page";
    protected Component parent = null;
    protected BrowserSectionModel model = null;
    protected UIConfigurationService uiConfigurationService = null;
    private boolean displaySubtypes = true;


    public CmsCreatePageWizard(BrowserSectionModel model, Component parent)
    {
        this.model = model;
        this.parent = parent;
    }


    public CmsCreatePageWizard(Component parent)
    {
        this.parent = parent;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCms2Constants.TC.ABSTRACTPAGE);
        return (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "wizardConfig", WizardConfiguration.class);
    }


    public Wizard start()
    {
        DefaultPageController defaultPageController = new DefaultPageController();
        WizardConfiguration config = getWizardConfiguration();
        CmsWizard wizard = new CmsWizard(null, this.model);
        Map<String, Object> contextValues = new HashMap<>();
        contextValues.put("CMSItem.catalogVersion", getCmsAdminSiteService().getActiveCatalogVersion());
        contextValues.put("CMSItem.uid",
                        getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.ABSTRACTPAGE, "page"));
        wizard.setTitle("wizard.page");
        wizard.setDefaultController((WizardPageController)defaultPageController);
        wizard.setComponentURI("/cockpit/wizards/defaultWizardFrame.zul");
        CreatePageMandatoryPage createPageMandatoryPage = new CreatePageMandatoryPage("wizard.cmspage.title", wizard);
        createPageMandatoryPage.setController((WizardPageController)new CreateCmsPageController());
        createPageMandatoryPage.setId("mandatoryPage");
        CmsPageSelectorPage typeSelectorPage = getCmsPageSelectorPageFactory().createCmsPageSelectorPage();
        typeSelectorPage.setWizard((Wizard)wizard);
        typeSelectorPage.setRootSelectorType(UISessionUtils.getCurrentSession().getTypeService()
                        .getObjectType(GeneratedCms2Constants.TC.ABSTRACTPAGE));
        ReferencePage reference = new ReferencePage("wizard.page.templateReference", (Wizard)wizard);
        reference.setController((WizardPageController)new CmsReferencePageController());
        reference.setId("referenceSelector");
        if(config != null)
        {
            wizard.setShowPrefilledValues(config.isShowPrefilledValues());
            createPageMandatoryPage.setDisplayedAttributes(new ArrayList(config.getQualifiers(true).keySet()));
            typeSelectorPage.setDisplaySubtypes((config.isDisplaySubtypes() && isDisplaySubtypes()));
        }
        List<WizardPage> pages = new ArrayList<>();
        pages.add(typeSelectorPage);
        pages.add(reference);
        pages.add(createPageMandatoryPage);
        wizard.setPages(pages);
        wizard.setParent(this.parent);
        wizard.initialize(null, contextValues);
        wizard.show();
        return (Wizard)wizard;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
    }


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        return (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected CmsPageSelectorPageFactory getCmsPageSelectorPageFactory()
    {
        return (CmsPageSelectorPageFactory)SpringUtil.getBean("cmsPageSelectorPageFactory");
    }
}
