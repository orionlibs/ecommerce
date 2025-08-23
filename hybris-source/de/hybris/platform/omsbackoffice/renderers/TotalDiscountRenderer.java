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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class TotalDiscountRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(TotalDiscountRenderer.class);
    private static final String ORDER = "Order";
    private static final String QUALIFIER = "totalDiscounts";
    private TypeFacade typeFacade;
    private Double totalDiscountAmount;
    private LabelService labelService;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractPanelConfiguration instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel)
        {
            this.totalDiscountAmount = getOrderTotalDiscount((ReturnRequestModel)object);
            try
            {
                Attribute attribute = new Attribute();
                attribute.setLabel("customersupportbackoffice.returnentry.totaldiscount");
                attribute.setQualifier("totalDiscounts");
                attribute.setReadonly(Boolean.TRUE);
                DataType order = getTypeFacade().load("Order");
                boolean canReadObject = getPermissionFacade().canReadInstanceProperty(order.getClazz(), "totalDiscounts");
                if(canReadObject)
                {
                    createAttributeRenderer().render(component, attribute, object, order, widgetInstanceManager);
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


    protected Editor createEditor(DataType genericType, WidgetInstanceManager widgetInstanceManager, Attribute attribute, Object object)
    {
        DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
        if(genericAttribute == null)
        {
            return null;
        }
        String qualifier = genericAttribute.getQualifier();
        String referencedModelProperty = "Order." + attribute.getQualifier();
        Editor editor = createEditor(genericAttribute, widgetInstanceManager.getModel(), referencedModelProperty);
        editor.setReadOnly(Boolean.TRUE.booleanValue());
        editor.setLocalized(Boolean.FALSE.booleanValue());
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setType(resolveEditorType(genericAttribute));
        editor.setOptional(!genericAttribute.isMandatory());
        YTestTools.modifyYTestId((Component)editor, "editor_Order." + qualifier);
        editor.setAttribute("parentObject", object);
        editor.setWritableLocales(getPermissionFacade().getWritableLocalesForInstance(object));
        editor.setReadableLocales(getPermissionFacade().getReadableLocalesForInstance(object));
        if(genericAttribute.isLocalized())
        {
            editor.addParameter("localizedEditor.attributeDescription", getAttributeDescription(genericType, attribute));
        }
        editor.setProperty(referencedModelProperty);
        if(StringUtils.isNotBlank(attribute.getEditor()))
        {
            editor.setDefaultEditor(attribute.getEditor());
        }
        editor.setPartOf(genericAttribute.isPartOf());
        editor.setOrdered(genericAttribute.isOrdered());
        editor.afterCompose();
        editor.setSclass("ye-default-editor-readonly");
        editor.setInitialValue(this.totalDiscountAmount);
        return editor;
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


    protected Double getOrderTotalDiscount(ReturnRequestModel returnRequest)
    {
        OrderModel order = returnRequest.getOrder();
        Double totalDiscount = Double.valueOf((order.getTotalDiscounts() != null) ? order.getTotalDiscounts().doubleValue() : 0.0D);
        totalDiscount = Double.valueOf(totalDiscount.doubleValue() + order.getEntries().stream()
                        .mapToDouble(entry -> entry.getDiscountValues().stream().mapToDouble(()).sum())
                        .sum());
        return totalDiscount;
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
