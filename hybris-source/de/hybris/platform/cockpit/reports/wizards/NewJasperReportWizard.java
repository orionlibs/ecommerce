package de.hybris.platform.cockpit.reports.wizards;

import de.hybris.platform.cockpit.enums.RefreshTimeOption;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.factory.JasperReportParameterFactory;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.generic.DecisionPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

public class NewJasperReportWizard
{
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String DEFAULT_WIZARD_FRAME = "/cockpit/wizards/defaultWizardFrame.zul";
    protected static final String CMSITEM_UID_PREFIX = "comp";
    protected static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    protected Component parent;
    protected ObjectTemplate currentType;
    protected BrowserModel browserModel = null;
    protected CreateContext createContext;
    private JasperMediaModel jasperMedia;
    protected UIConfigurationService uiConfigurationService = null;


    public void setCreateContext(CreateContext createContext)
    {
        this.createContext = createContext;
    }


    public NewJasperReportWizard(ObjectTemplate currentType, Component parent, BrowserModel browserModel)
    {
        this.currentType = currentType;
        this.parent = parent;
        this.browserModel = browserModel;
    }


    public NewJasperReportWizard(Component parent)
    {
        this.parent = parent;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        WizardConfiguration ret = null;
        if(this.currentType != null)
        {
            ret = (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(this.currentType, "wizardConfig", WizardConfiguration.class);
        }
        return ret;
    }


    public Wizard start()
    {
        Object object = new Object(this);
        GenericItemWizard wizard = new GenericItemWizard(this.browserModel, this.createContext);
        wizard.setTitle("cockpit.report.create_item");
        wizard.setDefaultController((WizardPageController)object);
        wizard.setComponentURI("/cockpit/wizards/defaultWizardFrame.zul");
        List<WizardPage> pages = new ArrayList<>();
        DecisionPage decisionPage = new DecisionPage();
        List<DecisionPage.Decision> decList = new ArrayList<>();
        Objects.requireNonNull(decisionPage);
        decList.add(new DecisionPage.Decision(decisionPage, "jasperMediaChoosePage",
                        Labels.getLabel("cockpit.wizard.createwidget.decision.chooseexisting"), null));
        Objects.requireNonNull(decisionPage);
        decList.add(new DecisionPage.Decision(decisionPage, "uploadPage", Labels.getLabel("cockpit.wizard.createwidget.decision.upload"), null));
        decisionPage.setDecisions(decList);
        decisionPage.setController((WizardPageController)new Object(this));
        pages.add(decisionPage);
        JasperMediaChoosePage chooserPage = new JasperMediaChoosePage();
        chooserPage.setTitle("wizard.report.choosereport");
        chooserPage.setWizard((Wizard)wizard);
        chooserPage.setId("jasperMediaChoosePage");
        chooserPage.setController((WizardPageController)new Object(this, chooserPage));
        pages.add(chooserPage);
        UploadJasperMediaPage uploadPage = new UploadJasperMediaPage();
        uploadPage.setTitle("cockpit.wizard.createwidget.uploadpage.title");
        uploadPage.setWizard((Wizard)wizard);
        uploadPage.setId("uploadPage");
        List<String> props = new ArrayList<>();
        props.add("JasperMedia.code");
        props.add("JasperMedia.title");
        props.add("JasperMedia.description");
        uploadPage.setDisplayedAttributes(props);
        uploadPage.setController((WizardPageController)new Object(this, uploadPage));
        pages.add(uploadPage);
        wizard.setPages(pages);
        wizard.setParent(this.parent);
        wizard.show();
        return (Wizard)wizard;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public void doAfterDone(AbstractGenericItemPage page)
    {
        if(this.jasperMedia == null)
        {
            Message msg = new Message(3, Labels.getLabel("wizard.jasperreport.choosemedia"), null);
            page.getWizard().addMessage(msg);
            return;
        }
        JasperWidgetPreferencesModel jasperWidgetPreferencesModel = (JasperWidgetPreferencesModel)UISessionUtils.getCurrentSession().getModelService().create(JasperWidgetPreferencesModel.class);
        jasperWidgetPreferencesModel.setOwnerUser(UISessionUtils.getCurrentSession().getUser());
        jasperWidgetPreferencesModel.setReport(this.jasperMedia);
        saveParametersWithReport(jasperWidgetPreferencesModel, this.jasperMedia);
        UISessionUtils.getCurrentSession().getModelService().save(jasperWidgetPreferencesModel);
        UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().update();
        UISessionUtils.getCurrentSession().getCurrentPerspective()
                        .activateItemInEditor(UISessionUtils.getCurrentSession().getTypeService().wrapItem(jasperWidgetPreferencesModel));
    }


    private void saveParametersWithReport(JasperWidgetPreferencesModel jasperWidgetPreferences, JasperMediaModel jasperMedia)
    {
        JasperReport compiledReport;
        MediaService mediaService = (MediaService)SpringUtil.getBean("mediaService");
        JasperReportParameterFactory jasperReportParameterFactory = (JasperReportParameterFactory)SpringUtil.getBean("jasperReportParameterFactory");
        InputStream reportInputStream = mediaService.getDataStreamFromMedia((MediaModel)jasperMedia);
        try
        {
            compiledReport = JasperCompileManager.compileReport(reportInputStream);
        }
        catch(Throwable t)
        {
            throw new RuntimeException("Couldn't compile jasper report source definition", t);
        }
        JRParameter[] parameters = compiledReport.getParameters();
        Collection<WidgetParameterModel> preferencesParameters = new ArrayList<>();
        for(JRParameter param : parameters)
        {
            if(param.isForPrompting() && !param.isSystemDefined())
            {
                WidgetParameterModel widgetParameterModel = jasperReportParameterFactory.createParameter(param);
                if(param.getDefaultValueExpression() != null)
                {
                    widgetParameterModel.setDefaultValueExpression(param.getDefaultValueExpression().getText());
                }
                preferencesParameters.add(widgetParameterModel);
                UISessionUtils.getCurrentSession().getModelService().save(widgetParameterModel);
            }
        }
        WidgetParameterModel newParameter = createRefreshParameter();
        preferencesParameters.add(newParameter);
        UISessionUtils.getCurrentSession().getModelService().save(newParameter);
        jasperWidgetPreferences.setParameters(preferencesParameters);
        CommonI18NService i18nService = (CommonI18NService)SpringUtil.getBean("commonI18NService");
        for(LanguageModel lang : i18nService.getAllLanguages())
        {
            Locale locale = i18nService.getLocaleForLanguage(lang);
            jasperWidgetPreferences.setTitle(jasperMedia.getTitle(locale), locale);
        }
    }


    protected WidgetParameterModel createRefreshParameter()
    {
        WidgetParameterModel parameter = new WidgetParameterModel();
        parameter.setName("Refresh");
        parameter.setType(getTypeService().getTypeForCode(RefreshTimeOption.class.getSimpleName()));
        parameter.setValue(RefreshTimeOption.NEVER);
        parameter.setDefaultValueExpression("NEVER");
        return parameter;
    }


    public TypeService getTypeService()
    {
        return (TypeService)SpringUtil.getBean("typeService", TypeService.class);
    }
}
