package de.hybris.platform.cockpit.reports.wizards;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.services.reports.ReportsService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class JasperMediaChoosePage extends AbstractGenericItemPage
{
    public static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String TYPE_SELECTOR_CMSWIZARD_PAGE_SCLASS = "typSelectorCmsWizardPage";
    protected static final String TYPE_SELECTOR_CMSWIZARD_ROW_SCLASS = "typSelectorRow";
    protected TypedObject chosenReference = null;


    public TypedObject getChosenReference()
    {
        return this.chosenReference;
    }


    public Component createRepresentationItself()
    {
        this.height = "400px";
        this.pageContent.getChildren().clear();
        Div firstStep = new Div();
        firstStep.setSclass("jasperMediaChooseWizPage");
        firstStep.setParent((Component)this.pageContent);
        Listbox listbox = new Listbox();
        listbox.setSclass("typSelectorCmsWizardPage");
        listbox.setWidth("100%");
        listbox.setParent((Component)firstStep);
        listbox.setModel((ListModel)new SimpleListModel(getReferences()));
        listbox.setItemRenderer((ListitemRenderer)new Object(this));
        return (Component)this.pageContainer;
    }


    private List<TypedObject> getReferences()
    {
        ReportsService reportsService = (ReportsService)SpringUtil.getBean("reportsService");
        List<JasperMediaModel> jasperMedias = reportsService.findJasperMediasByMediaFolder("jasperreports");
        return UISessionUtils.getCurrentSession().getTypeService().wrapItems(jasperMedias);
    }


    private String getDescription(JasperMediaModel jasperMedia)
    {
        SessionService sessionService = (SessionService)SpringUtil.getBean("sessionService");
        Object labelObject = sessionService.executeInLocalView((SessionExecutionBody)new Object(this, sessionService, jasperMedia));
        return (String)labelObject;
    }
}
