package de.hybris.platform.warehousingbackoffice.widgets.atp;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.atp.formula.services.AtpFormulaService;
import de.hybris.platform.warehousing.atp.services.impl.WarehousingCommerceStockService;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import de.hybris.platform.warehousingbackoffice.dtos.AtpFormDto;
import de.hybris.platform.warehousingbackoffice.dtos.AtpViewDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;

public class AtpViewController extends DefaultWidgetController
{
    protected static final String WAREHOUSINGBACKOFFICE_ATP_VIEWS_TITLE = "warehousingbackoffice.atp.views.title";
    protected static final String IN_SOCKET = "atpFormInput";
    protected Component widgetComponent;
    @Wire
    private Listbox atpListView;
    @WireVariable
    private transient WarehousingCommerceStockService warehousingCommerceStockService;
    @WireVariable
    private transient AtpFormulaService atpFormulaService;


    public void initialize(Component component)
    {
        component.setVisible(false);
        this.widgetComponent = component;
    }


    @SocketEvent(socketId = "atpFormInput")
    public void initializeView(AtpFormDto atpFormDto)
    {
        ServicesUtil.validateParameterNotNull(atpFormDto, "Empty AtpFormDto object received");
        ServicesUtil.validateParameterNotNull(atpFormDto.getBaseStore(), "BaseStore cannot be null");
        ServicesUtil.validateParameterNotNull(atpFormDto.getProduct(), "Product cannot be null");
        this.widgetComponent.setVisible(true);
        getWidgetInstanceManager().setTitle(getLabel("warehousingbackoffice.atp.views.title") + getLabel("warehousingbackoffice.atp.views.title"));
        ProductModel product = atpFormDto.getProduct();
        BaseStoreModel baseStore = atpFormDto.getBaseStore();
        List<AtpViewDto> atpViewEntries = new ArrayList<>();
        Collection<AtpFormulaModel> atpFormulas = getAtpFormulaService().getAllAtpFormula();
        atpFormulas.forEach(atpFormula -> populateAtpFormula(atpFormDto, product, baseStore, atpViewEntries, atpFormula));
        getAtpListView().setModel((ListModel)new ListModelArray(atpViewEntries));
    }


    protected void populateAtpFormula(AtpFormDto atpFormDto, ProductModel product, BaseStoreModel baseStore, List<AtpViewDto> atpViewEntries, AtpFormulaModel atpFormula)
    {
        Long atp;
        if(atpFormDto.getPointOfService() != null)
        {
            PointOfServiceModel pointOfService = atpFormDto.getPointOfService();
            Assert.isTrue(baseStore.getPointsOfService().contains(pointOfService),
                            String.format("Selected Point of Service: [%s] does not belong to the selected BaseStore: [%s]", new Object[] {pointOfService.getName(), baseStore.getUid()}));
            pointOfService.getBaseStore().setDefaultAtpFormula(atpFormula);
            atp = getWarehousingCommerceStockService().getStockLevelForProductAndPointOfService(product, pointOfService);
        }
        else
        {
            baseStore.setDefaultAtpFormula(atpFormula);
            atp = getWarehousingCommerceStockService().getStockLevelForProductAndBaseStore(product, baseStore);
        }
        Boolean isActive = Boolean.valueOf(atpFormula.getBaseStores().contains(baseStore));
        if(isActive.booleanValue())
        {
            atpViewEntries.add(new AtpViewDto(atpFormula, atp, Boolean.TRUE));
        }
    }


    protected WarehousingCommerceStockService getWarehousingCommerceStockService()
    {
        return this.warehousingCommerceStockService;
    }


    public void setWarehousingCommerceStockService(WarehousingCommerceStockService warehousingCommerceStockService)
    {
        this.warehousingCommerceStockService = warehousingCommerceStockService;
    }


    protected AtpFormulaService getAtpFormulaService()
    {
        return this.atpFormulaService;
    }


    public void setAtpFormulaService(AtpFormulaService atpFormulaService)
    {
        this.atpFormulaService = atpFormulaService;
    }


    protected Listbox getAtpListView()
    {
        return this.atpListView;
    }
}
