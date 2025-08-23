package de.hybris.platform.warehousingbackoffice.widgets.atp;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousingbackoffice.dtos.AtpFormDto;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;

public class AtpFormController extends DefaultWidgetController
{
    protected static final String WAREHOUSINGBACKOFFICE_ATPFORM_VALIDATION_MISSING_BASE_STORE = "warehousingbackoffice.atpform.validation.missing.base.store";
    protected static final String WAREHOUSINGBACKOFFICE_ATPFORM_VALIDATION_MISSING_PRODUCT_CODE = "warehousingbackoffice.atpform.validation.missing.product.code";
    protected static final String WAREHOUSINGBACKOFFICE_ATPFORM_VALIDATION_VALID_POS = "warehousingbackoffice.atpform.validation.valid.pos";
    protected static final String ON_STORE_CHANGE = "onStoreChange";
    protected static final String ON_LATER_STORE_CHANGE = "onLaterStoreChange";
    protected static final String ON_POS_CHANGE = "onPoSChange";
    protected static final String ON_LATER_POS_CHANGE = "onLaterPoSChange";
    protected static final String SEARCH_ATP = "searchAtp";
    protected static final String WAREHOUSINGBACKOFFICE_ATPFORM_TITLE = "warehousingbackoffice.atpform.title";
    protected static final String OUT_CONFIRM = "populatedAtpForm";
    protected static final String SELECT_A_POINT_OF_SERVICE = "warehousingbackoffice.atpform.pos.placeholder";
    protected static final String SELECT_A_BASE_STORE = "warehousingbackoffice.atpform.store.placeholder";
    @Wire
    private Combobox baseStores;
    @Wire
    private Combobox pointOfServices;
    @Wire
    private Editor product;
    @WireVariable
    private transient BaseStoreService baseStoreService;


    public void initialize(Component component)
    {
        getWidgetInstanceManager().setTitle(getLabel("warehousingbackoffice.atpform.title"));
        refreshForm();
    }


    protected void refreshForm()
    {
        List<String> baseStoreList = new ArrayList();
        baseStoreList.add(getLabel("warehousingbackoffice.atpform.store.placeholder"));
        baseStoreList.addAll(getBaseStoreService().getAllBaseStores());
        getBaseStores().setModel((ListModel)new ListModelArray(baseStoreList));
        getBaseStores().setValue(getLabel("warehousingbackoffice.atpform.store.placeholder"));
        addListeners();
    }


    @ViewEvent(componentID = "searchAtp", eventName = "onClick")
    public void performSearchOperation()
    {
        validateForm();
        BaseStoreModel selectedBaseStore = (BaseStoreModel)getBaseStores().getSelectedItem().getValue();
        PointOfServiceModel selectedPos = (getPointOfServices().getSelectedItem() != null && getPointOfServices().getSelectedItem().getValue() instanceof PointOfServiceModel) ? (PointOfServiceModel)getPointOfServices().getSelectedItem().getValue() : null;
        AtpFormDto atpForm = new AtpFormDto((ProductModel)getProduct().getValue(), selectedBaseStore, selectedPos);
        sendOutput("populatedAtpForm", atpForm);
    }


    protected void addListeners()
    {
        getBaseStores().addEventListener("onStoreChange", event -> Events.echoEvent("onLaterStoreChange", (Component)getBaseStores(), event.getData()));
        getBaseStores().addEventListener("onLaterStoreChange", event -> {
            if(getBaseStores().getSelectedItem() != null && getBaseStores().getSelectedItem().getValue() instanceof BaseStoreModel)
            {
                List<String> pointOfServiceList = new ArrayList();
                pointOfServiceList.add(getLabel("warehousingbackoffice.atpform.pos.placeholder"));
                pointOfServiceList.addAll(((BaseStoreModel)getBaseStores().getSelectedItem().getValue()).getPointsOfService());
                getPointOfServices().setModel((ListModel)new ListModelArray(pointOfServiceList));
                getPointOfServices().setValue(getLabel("warehousingbackoffice.atpform.pos.placeholder"));
            }
            Clients.clearWrongValue((Component)getBaseStores());
            getBaseStores().invalidate();
        });
        getPointOfServices().addEventListener("onPoSChange", event -> Events.echoEvent("onLaterPoSChange", (Component)getPointOfServices(), event.getData()));
        getPointOfServices().addEventListener("onLaterPoSChange", event -> {
            Clients.clearWrongValue((Component)getPointOfServices());
            getPointOfServices().invalidate();
        });
    }


    protected void validateForm()
    {
        if(getProduct().getValue() == null || ((ProductModel)getProduct().getValue()).getCode().isEmpty())
        {
            throw new WrongValueException(getProduct(), getLabel("warehousingbackoffice.atpform.validation.missing.product.code"));
        }
        if(getBaseStores().getSelectedItem() == null ||
                        !(getBaseStores().getSelectedItem().getValue() instanceof BaseStoreModel))
        {
            throw new WrongValueException(getBaseStores(), getLabel("warehousingbackoffice.atpform.validation.missing.base.store"));
        }
        if(getPointOfServices().getSelectedItem() != null && getPointOfServices().getSelectedItem().getValue() != getLabel("warehousingbackoffice.atpform.pos.placeholder") &&
                        !((BaseStoreModel)getBaseStores().getSelectedItem().getValue()).getPointsOfService().contains(getPointOfServices().getSelectedItem().getValue()))
        {
            throw new WrongValueException(getPointOfServices(), getLabel("warehousingbackoffice.atpform.validation.valid.pos"));
        }
    }


    protected BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    protected Combobox getBaseStores()
    {
        return this.baseStores;
    }


    protected Combobox getPointOfServices()
    {
        return this.pointOfServices;
    }


    protected Editor getProduct()
    {
        return this.product;
    }
}
