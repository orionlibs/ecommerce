package de.hybris.platform.cockpit.wizards.media;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.wizards.generic.AdvancedSearchPage;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public class UploadMediaFirstPage extends AdvancedSearchPage
{
    private TypeService cockpitTypeService;
    private static final String FILE_UPLOAD_CONTAINER = "fileUploadContainer";
    private static final String FILE_UPLOAD = "fileUpload";
    protected static final String WIZARD_GROUP_LABEL = "wizardGroupLabel";
    protected static final String WIZARD_SECTION = "wizardGroupLabel";
    private static final String UPLOAD_TIPS_DIV = "uploadTips";
    private static final String FILEUPLOAD_TIPS = "uploadTipsLabel";


    protected ComponentController createListViewSelectorController(UIListView listView)
    {
        return (ComponentController)new Object(this, listView);
    }


    public Component createRepresentationItself()
    {
        Vbox box = new Vbox();
        box.setWidth("100%");
        AdvancedGroupbox createNewMediaBox = createGroup((HtmlBasedComponent)box, true, Labels.getLabel("uploadmediawizard.createnew"));
        HtmlBasedComponent fileUpluadComponent = createFileUploadComponent();
        AdvancedGroupbox selectExistingMediabox = createGroup((HtmlBasedComponent)box, true, Labels.getLabel("uploadmediawizard.existing"));
        initAdvanceModeModels();
        Component searchComponent = super.createRepresentationItself();
        Div tipsAndTricksDiv = new Div();
        tipsAndTricksDiv.setSclass("uploadTips");
        Label label = new Label(Labels.getLabel("uploadmediawizard.fileupload.tips"));
        label.setSclass("uploadTipsLabel");
        tipsAndTricksDiv.appendChild((Component)label);
        tipsAndTricksDiv.setParent((Component)createNewMediaBox);
        fileUpluadComponent.setParent((Component)createNewMediaBox);
        searchComponent.setParent((Component)selectExistingMediabox);
        if(isMultiple())
        {
            showSelectColumn();
        }
        else
        {
            hideSelectColumn();
        }
        return (Component)box;
    }


    protected void hideSelectColumn()
    {
        int index = 0;
        for(ColumnDescriptor desc : ((DefaultTableModel)getTableModel()).getColumnComponentModel().getVisibleColumns())
        {
            if(StringUtils.isBlank(desc.getName()))
            {
                ((DefaultTableModel)getTableModel()).getColumnComponentModel().hideColumn(index);
                break;
            }
            index++;
        }
    }


    protected void showSelectColumn()
    {
        for(ColumnDescriptor desc : ((DefaultTableModel)getTableModel()).getColumnComponentModel().getColumns())
        {
            if(StringUtils.isBlank(desc.getName()))
            {
                ((DefaultTableModel)getTableModel()).getColumnComponentModel().showColumn(desc, Integer.valueOf(0));
                break;
            }
        }
        int index = 0;
        for(ColumnDescriptor desc : ((DefaultTableModel)getTableModel()).getColumnComponentModel().getVisibleColumns())
        {
            if(StringUtils.isBlank(desc.getName()))
            {
                if(index != 0)
                {
                    ((DefaultTableModel)getTableModel()).getColumnComponentModel().moveColumn(index, 0);
                }
                break;
            }
            index++;
        }
    }


    protected AdvancedGroupbox createGroup(HtmlBasedComponent parent, boolean open, String label)
    {
        AdvancedGroupbox groupbox = new AdvancedGroupbox();
        groupbox.setSclass("wizardGroupLabel");
        groupbox.setWidth("100%");
        groupbox.setOpen(open);
        groupbox.setParent((Component)parent);
        Label captionLabel = new Label(label);
        captionLabel.setSclass("wizardGroupLabel");
        groupbox.getCaptionContainer().appendChild((Component)captionLabel);
        return groupbox;
    }


    protected boolean isMultiple()
    {
        if(this.wizard == null)
        {
            return false;
        }
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor)this.wizard.getWizardContext().getAttribute("propertyDescriptor");
        if(propertyDescriptor != null)
        {
            return !PropertyDescriptor.Multiplicity.SINGLE.equals(propertyDescriptor.getMultiplicity());
        }
        return false;
    }


    private HtmlBasedComponent createFileUploadComponent()
    {
        Div fileUploadContainer = new Div();
        fileUploadContainer.setSclass("fileUploadContainer");
        Fileupload fileUpload = new Fileupload();
        fileUpload.setSclass("fileUpload");
        ObjectType currentType = this.cockpitTypeService.getObjectType("Media");
        getWizard().setCurrentType(currentType);
        fileUpload.setMaxsize(-1);
        fileUpload.addEventListener("onUpload", (EventListener)new Object(this));
        fileUploadContainer.appendChild((Component)fileUpload);
        return (HtmlBasedComponent)fileUploadContainer;
    }


    protected Map<String, Object> getMediaPredefinedAttributes(Media media)
    {
        Map<String, Object> predefinedValues = new HashMap<>();
        Object wizardPredefinedValuesMap = getWizard().getWizardContext().getAttribute("predefinedValues");
        if(wizardPredefinedValuesMap instanceof Map)
        {
            for(Map.Entry<String, Object> wizardPredefinedValueEntry : (Iterable<Map.Entry<String, Object>>)((Map)wizardPredefinedValuesMap)
                            .entrySet())
            {
                predefinedValues.put(wizardPredefinedValueEntry.getKey(), wizardPredefinedValueEntry.getValue());
            }
        }
        predefinedValues.put("Media.code", media
                        .getName());
        predefinedValues.put("Media.realFileName", media
                        .getName());
        predefinedValues.put("Media.altText", media
                        .getName());
        predefinedValues.put("Media.description", media
                        .getName());
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)getWizard().getWizardContext().getAttribute("catalogVersion");
        if(catalogVersionModel != null)
        {
            predefinedValues.put("Media.catalogVersion", this.cockpitTypeService
                            .wrapItem(catalogVersionModel));
        }
        predefinedValues.put("Media.mime", media
                        .getContentType());
        return predefinedValues;
    }


    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }
}
