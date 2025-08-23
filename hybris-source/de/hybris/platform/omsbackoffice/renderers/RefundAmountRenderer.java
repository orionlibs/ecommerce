package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class RefundAmountRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(RefundAmountRenderer.class);
    protected static final String REFUND_AMOUNT_OBSERVER_ID = "refundAmountObserver";
    protected static final String REFUND_ENTRY = "RefundEntry";
    protected static final String QUALIFIER = "amount";
    protected static final String CURRENT_OBJECT = "currentObject";
    private Editor editor;
    private TypeFacade typeFacade;
    private BigDecimal totalRefundAmount;
    private LabelService labelService;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(!(abstractPanelConfiguration instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel) || !(object instanceof ReturnRequestModel))
        {
            return;
        }
        this.totalRefundAmount = getOrderRefundAmount((ReturnRequestModel)object);
        try
        {
            Attribute attribute = new Attribute();
            attribute.setLabel("customersupportbackoffice.returnentry.totalrefundamount");
            attribute.setQualifier("amount");
            attribute.setReadonly(Boolean.TRUE);
            DataType refundEntry = getTypeFacade().load("RefundEntry");
            boolean canReadObject = getPermissionFacade().canReadInstanceProperty(refundEntry.getClazz(), "amount");
            if(!canReadObject)
            {
                Div attributeContainer = new Div();
                attributeContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
                renderNotReadableLabel((HtmlBasedComponent)attributeContainer, attribute, dataType, getLabelService().getAccessDeniedLabel(attribute));
                attributeContainer.setParent(component);
                return;
            }
            createAttributeRenderer().render(component, attribute, object, refundEntry, widgetInstanceManager);
            WidgetModel widgetInstanceModel = widgetInstanceManager.getModel();
            widgetInstanceModel.removeObserver("refundAmountObserver");
            widgetInstanceModel.addObserver("currentObject", (ValueObserver)new Object(this, widgetInstanceModel));
        }
        catch(TypeNotFoundException e)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn(e.getMessage(), (Throwable)e);
            }
        }
    }


    protected Editor createEditor(DataType genericType, WidgetInstanceManager widgetInstanceManager, Attribute attribute, Object object)
    {
        DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
        if(genericAttribute == null)
        {
            return null;
        }
        String editorSClass = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-editor";
        boolean editable = (!attribute.isReadonly() && canChangeProperty(genericAttribute, object));
        if(!editable)
        {
            editorSClass = "ye-default-editor-readonly";
        }
        String qualifier = genericAttribute.getQualifier();
        String referencedModelProperty = "RefundEntry." + attribute.getQualifier();
        Editor newEditor = createEditor(genericAttribute, widgetInstanceManager.getModel(), referencedModelProperty);
        newEditor.setReadOnly(!editable);
        newEditor.setLocalized(Boolean.FALSE.booleanValue());
        newEditor.setWidgetInstanceManager(widgetInstanceManager);
        newEditor.setType(resolveEditorType(genericAttribute));
        newEditor.setOptional(!genericAttribute.isMandatory());
        YTestTools.modifyYTestId((Component)newEditor, "editor_RefundEntry." + qualifier);
        newEditor.setAttribute("parentObject", object);
        newEditor.setWritableLocales(getPermissionFacade().getWritableLocalesForInstance(object));
        newEditor.setReadableLocales(getPermissionFacade().getReadableLocalesForInstance(object));
        if(genericAttribute.isLocalized())
        {
            newEditor.addParameter("localizedEditor.attributeDescription", getAttributeDescription(genericType, attribute));
        }
        newEditor.setProperty(referencedModelProperty);
        if(StringUtils.isNotBlank(attribute.getEditor()))
        {
            newEditor.setDefaultEditor(attribute.getEditor());
        }
        newEditor.setPartOf(genericAttribute.isPartOf());
        newEditor.setOrdered(genericAttribute.isOrdered());
        newEditor.afterCompose();
        newEditor.setSclass(editorSClass);
        newEditor.setInitialValue(this.totalRefundAmount);
        this.editor = newEditor;
        return newEditor;
    }


    protected Editor createEditor(DataAttribute genericAttribute, WidgetModel model, String referencedModelProperty)
    {
        if(isReferenceEditor(genericAttribute))
        {
            Object object = new Object(this);
            model.addObserver(referencedModelProperty, (ValueObserver)object);
            return (Editor)new Object(this, model, referencedModelProperty, (ModelObserver)object);
        }
        return new Editor();
    }


    protected BigDecimal getOrderRefundAmount(ReturnRequestModel returnRequest)
    {
        BigDecimal refundAmount = returnRequest.getReturnEntries().stream().map(returnEntry -> getRefundEntryAmount(returnEntry)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(returnRequest.getOrder().getTotalDiscounts().doubleValue() > 0.0D)
        {
            BigDecimal subDiscount = BigDecimal.valueOf(returnRequest
                            .getOrder().getTotalDiscounts().doubleValue() * refundAmount.doubleValue() / returnRequest.getOrder()
                            .getSubtotal().doubleValue());
            refundAmount = refundAmount.subtract(subDiscount);
        }
        if(returnRequest.getRefundDeliveryCost() != null && returnRequest.getRefundDeliveryCost().booleanValue())
        {
            refundAmount = refundAmount.add(BigDecimal.valueOf(returnRequest.getOrder().getDeliveryCost().doubleValue()));
        }
        return refundAmount.setScale(returnRequest.getOrder().getCurrency().getDigits().intValue(), 3);
    }


    protected BigDecimal getRefundEntryAmount(ReturnEntryModel returnEntryModel)
    {
        ReturnRequestModel returnRequest = returnEntryModel.getReturnRequest();
        BigDecimal refundEntryAmount = BigDecimal.ZERO;
        if(returnEntryModel instanceof RefundEntryModel)
        {
            RefundEntryModel refundEntry = (RefundEntryModel)returnEntryModel;
            if(refundEntry.getAmount() != null)
            {
                refundEntryAmount = refundEntry.getAmount();
                refundEntryAmount = refundEntryAmount.setScale(returnRequest.getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_DOWN);
            }
        }
        return refundEntryAmount;
    }


    protected boolean isReferenceEditor(DataAttribute genericAttribute)
    {
        return (genericAttribute.getValueType() != null && !genericAttribute.getValueType().isAtomic());
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
