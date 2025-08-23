package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class RefundDeliveryCostRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(RefundDeliveryCostRenderer.class);
    private static final String RETURN_REQUEST = "ReturnRequest";
    private static final String QUALIFIER = "refundDeliveryCost";
    private static final String OPTIONAL_FIELD_NAME = "showOptionalField";
    private static final String OPTIONA_FIELD_VALUE = "false";
    private TypeFacade typeFacade;
    private ReturnService returnService;
    private Set<ReturnStatus> invalidReturnStatusForRefundDeliveryCost;
    private LabelService labelService;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractPanelConfiguration instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel && object instanceof ReturnRequestModel)
        {
            try
            {
                ReturnRequestModel requestModel = (ReturnRequestModel)object;
                Attribute attribute = new Attribute();
                attribute.setQualifier("refundDeliveryCost");
                attribute.setReadonly(Boolean.valueOf(!isDeliveryCostRefundable(requestModel.getOrder().getCode(), requestModel.getRMA())));
                Parameter optionalParameter = new Parameter();
                optionalParameter.setName("showOptionalField");
                optionalParameter.setValue("false");
                attribute.getEditorParameter().add(optionalParameter);
                DataType returnRequest = getTypeFacade().load("ReturnRequest");
                boolean canReadObject = getPermissionFacade().canReadInstanceProperty(returnRequest.getClass(), "refundDeliveryCost");
                if(canReadObject)
                {
                    createAttributeRenderer()
                                    .render(component, attribute, object, returnRequest, widgetInstanceManager);
                }
                else
                {
                    Div attributeContainer = new Div();
                    attributeContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
                    renderNotReadableLabel((HtmlBasedComponent)attributeContainer, attribute, dataType, getLabelService().getAccessDeniedLabel(attribute));
                    attributeContainer.setParent(component);
                }
            }
            catch(TypeNotFoundException e)
            {
                if(LOG.isWarnEnabled())
                {
                    LOG.warn(e.getMessage(), (Throwable)e);
                }
            }
        }
    }


    protected boolean isDeliveryCostRefundable(String orderCode, String returnRequestRMA)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("orderCode", orderCode);
        ServicesUtil.validateParameterNotNullStandardMessage("returnRequestRMA", returnRequestRMA);
        List<ReturnRequestModel> previousReturns = getReturnService().getReturnRequests(orderCode);
        boolean isDeliveryCostAlreadyRefunded = previousReturns.stream().filter(previousReturn -> !getInvalidReturnStatusForRefundDeliveryCost().contains(previousReturn.getStatus()))
                        .anyMatch(previousReturn -> (!returnRequestRMA.equals(previousReturn.getRMA()) && previousReturn.getRefundDeliveryCost() != null && previousReturn.getRefundDeliveryCost().booleanValue()));
        return !isDeliveryCostAlreadyRefunded;
    }


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected ReturnService getReturnService()
    {
        return this.returnService;
    }


    @Required
    public void setReturnService(ReturnService returnService)
    {
        this.returnService = returnService;
    }


    protected Set<ReturnStatus> getInvalidReturnStatusForRefundDeliveryCost()
    {
        return this.invalidReturnStatusForRefundDeliveryCost;
    }


    @Required
    public void setInvalidReturnStatusForRefundDeliveryCost(Set<ReturnStatus> invalidReturnStatusForRefundDeliveryCost)
    {
        this.invalidReturnStatusForRefundDeliveryCost = invalidReturnStatusForRefundDeliveryCost;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
