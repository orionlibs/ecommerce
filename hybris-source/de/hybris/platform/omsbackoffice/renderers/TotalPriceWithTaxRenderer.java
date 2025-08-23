package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class TotalPriceWithTaxRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(TotalPriceWithTaxRenderer.class);
    protected static final String ORDER = "Order";
    protected static final String QUALIFIER = "totalPrice";
    protected static final String LABEL = "customersupportbackoffice.order.details.total";
    protected static final int TOTAL_PRICE_ROUNDING_SCALE = 2;
    private TypeFacade typeFacade;
    private LabelService labelService;
    private Double totalPriceWithTax;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractPanelConfiguration instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel && object instanceof AbstractOrderModel)
        {
            this.totalPriceWithTax = getOrderTotalWithTax((AbstractOrderModel)object);
            try
            {
                Attribute attribute = new Attribute();
                attribute.setLabel("customersupportbackoffice.order.details.total");
                attribute.setQualifier("totalPrice");
                attribute.setReadonly(Boolean.TRUE);
                DataType order = getTypeFacade().load("Order");
                boolean canReadObject = getPermissionFacade().canReadInstanceProperty(order.getClazz(), "totalPrice");
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
                LOG.warn(e.getMessage());
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
        Editor editor = new Editor();
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
        editor.setInitialValue(this.totalPriceWithTax);
        return editor;
    }


    protected Double getOrderTotalWithTax(AbstractOrderModel abstractOrderModel)
    {
        return Double.valueOf(BigDecimal.valueOf(abstractOrderModel.getTotalPrice().doubleValue())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
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
