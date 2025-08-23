package de.hybris.platform.platformbackoffice.widgets.catalogversion;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.PK;
import de.hybris.platform.platformbackoffice.data.CatalogVersionDiffDTO;
import de.hybris.platform.platformbackoffice.services.catalogversion.CatalogVersionCompareService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;

public class CatalogVersionDiffController extends DefaultWidgetController
{
    public static final String IN_SOCKET_CATALOGVERSIONDIFF = "catalogversiondiff";
    public static final String LABLE_CATALOG_VERSION_DIFF_PROCESSING = "catalogVersionDiff.GenerateDiff.Processing";
    protected Button generateButton;
    protected Combobox diffOperationComboBox;
    protected Grid diffResultGrid;
    @Resource
    private transient CatalogVersionCompareService catalogVersionCompareService;
    @Resource
    private transient LabelService labelService;
    @Resource
    private transient ModelService modelService;
    @Resource
    private transient FormatFactory formatFactory;


    @SocketEvent(socketId = "catalogversiondiff")
    public void showReport(CatalogVersionDiffDTO diffDTO)
    {
        Validate.notNull("diffDTO mustn't be null", new Object[] {diffDTO});
        attachListeners();
        fillComboBox(diffDTO);
        selectFirstItemInComboBox();
    }


    private void attachListeners()
    {
        this.generateButton.addEventListener("onClick", e -> {
            if(canGenerateDiff())
            {
                generateDiff();
            }
        });
        this.diffOperationComboBox.addEventListener("onSelect", e -> this.generateButton.setDisabled(!canGenerateDiff()));
    }


    private void fillComboBox(CatalogVersionDiffDTO diffDTO)
    {
        for(CatalogVersionCompareService.CatalogVersionComparison comparison : diffDTO.getPossibleComparisons())
        {
            Comboitem cmbItem = new Comboitem(getLabelFromPK(comparison.getSyncItemJobPK()));
            cmbItem.setValue(comparison);
            this.diffOperationComboBox.appendChild((Component)cmbItem);
        }
    }


    private void selectFirstItemInComboBox()
    {
        if(this.diffOperationComboBox.getItemCount() > 0)
        {
            this.diffOperationComboBox.setSelectedIndex(0);
        }
    }


    private String getLabelFromPK(PK pk)
    {
        return this.labelService.getObjectLabel(this.modelService.get(pk));
    }


    private Object getSelectedValue()
    {
        Comboitem selectedItem = this.diffOperationComboBox.getSelectedItem();
        if(selectedItem == null)
        {
            return null;
        }
        return selectedItem.getValue();
    }


    private boolean canGenerateDiff()
    {
        return (getSelectedValue() != null);
    }


    private void generateDiff()
    {
        CatalogVersionCompareService.CatalogVersionComparison comparison = (CatalogVersionCompareService.CatalogVersionComparison)getSelectedValue();
        if(null != comparison)
        {
            Clients.showBusy(this.self, Labels.getLabel("catalogVersionDiff.GenerateDiff.Processing"));
            getWidgetInstanceManager().executeOperation((Operation)new CatalogVersionGenerateDiffOperation(this, comparison),
                            callbackEventListener4GenerateDiff(this.self), "");
        }
    }


    private EventListener<Event> callbackEventListener4GenerateDiff(Component component)
    {
        return (EventListener<Event>)new Object(this, component);
    }


    public CatalogVersionCompareService getCatalogVersionCompareService()
    {
        return this.catalogVersionCompareService;
    }


    public void setCatalogVersionCompareService(CatalogVersionCompareService catalogVersionCompareService)
    {
        this.catalogVersionCompareService = catalogVersionCompareService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public FormatFactory getFormatFactory()
    {
        return this.formatFactory;
    }


    public void setFormatFactory(FormatFactory formatFactory)
    {
        this.formatFactory = formatFactory;
    }
}
