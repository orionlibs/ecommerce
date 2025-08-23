package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

public class AtpFormulaPanelRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(AtpFormulaPanelRenderer.class);
    protected static final String ADD_VARIABLE_IN_ATPFORMULA = "addvariableatpformula";
    protected static final String SUB_VARIABLE_IN_ATPFORMULA = "subvariableatpformula";
    protected static final String ATPFORMULA_BUILDER_HEADER_ACTIONS = "atpformulabuilderheaderactions";
    protected static final String ATPFORMULA_BUILDER_HEADER_VARIABLES = "atpformulabuilderheadervariables";
    protected static final String ATPFORMULA_TABLE_CLASS = "oms-widget-atpformula-table";
    protected static final String AVAILABILITY = "availability";
    protected static final String PLUS_OPERATOR = "+";
    protected static final String MINUS_OPERATOR = "-";
    private TypeFacade typeFacade;
    private Map<String, String> atpFormulaVar2ArithmeticOperatorMap;
    private Div attributeContainer;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractPanelConfiguration instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel && object instanceof AtpFormulaModel)
        {
            AtpFormulaModel atpFormula = (AtpFormulaModel)object;
            try
            {
                DataType atpFormulaType = getTypeFacade().load("AtpFormula");
                boolean canReadObject = getPermissionFacade().canReadInstanceProperty(atpFormulaType.getClazz(), "availability");
                if(canReadObject)
                {
                    this.attributeContainer = new Div();
                    this.attributeContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
                    this.attributeContainer.setParent(component);
                    renderAtpFormulaVariables((HtmlBasedComponent)this.attributeContainer, atpFormula, widgetInstanceManager);
                    setAfterCancelListener(widgetInstanceManager);
                }
                else
                {
                    component.appendChild((Component)new Label(
                                    resolveLabel(widgetInstanceManager.getLabel("warehousingbackoffice.atpformula.no.access"))));
                }
            }
            catch(TypeNotFoundException e)
            {
                LOG.error("Could not find the ATP Formula Model");
            }
        }
        else
        {
            LOG.error(resolveLabel(widgetInstanceManager.getLabel("warehousingbackoffice.atpformula.render.error")));
        }
    }


    protected void renderAtpFormulaVariables(HtmlBasedComponent attributeContainer, AtpFormulaModel atpFormula, WidgetInstanceManager widgetInstanceManager)
    {
        Vlayout vlayout = new Vlayout();
        vlayout.setClass("oms-widget-atpformula-table");
        renderAtpFormulaBuilderHeader(vlayout);
        renderIndividualFormulaVariables(atpFormula, widgetInstanceManager, vlayout);
        attributeContainer.appendChild((Component)vlayout);
    }


    protected void renderIndividualFormulaVariables(AtpFormulaModel atpFormula, WidgetInstanceManager widgetInstanceManager, Vlayout vlayout)
    {
        Set<PropertyDescriptor> formulaVarPropertyDescriptors = getAllAtpFormulaVariables(atpFormula);
        if(CollectionUtils.isNotEmpty(formulaVarPropertyDescriptors))
        {
            Optional<PropertyDescriptor> optionalPropDescriptor = formulaVarPropertyDescriptors.stream().filter(propertyDescriptor -> "availability".equalsIgnoreCase(propertyDescriptor.getName())).findFirst();
            PropertyDescriptor availabilityPropDescriptor = optionalPropDescriptor.isPresent() ? optionalPropDescriptor.get() : null;
            renderAtpFormulaVariableRow(atpFormula, widgetInstanceManager, vlayout, availabilityPropDescriptor);
            formulaVarPropertyDescriptors.stream()
                            .filter(propertyDescriptor -> !propertyDescriptor.getName().equalsIgnoreCase(availabilityPropDescriptor.getName()))
                            .forEach(formulaVarPropertyDescriptor -> renderAtpFormulaVariableRow(atpFormula, widgetInstanceManager, vlayout, formulaVarPropertyDescriptor));
        }
    }


    protected void renderAtpFormulaVariableRow(AtpFormulaModel atpFormula, WidgetInstanceManager widgetInstanceManager, Vlayout vlayout, PropertyDescriptor formulaVarPropertyDescriptor)
    {
        Hlayout hlayout = new Hlayout();
        hlayout.setClass("atpformulas-row");
        Checkbox sliderCheckbox = new Checkbox();
        sliderCheckbox.setClass("ye-switch-checkbox");
        Div signDiv = new Div();
        if("+"
                        .equals(getAtpFormulaVar2ArithmeticOperatorMap().get(formulaVarPropertyDescriptor.getName().toLowerCase())))
        {
            signDiv.setClass("addvariableatpformula");
        }
        else if("-"
                        .equals(getAtpFormulaVar2ArithmeticOperatorMap().get(formulaVarPropertyDescriptor.getName().toLowerCase())))
        {
            signDiv.setClass("subvariableatpformula");
        }
        try
        {
            Boolean formulaVarValue = (Boolean)formulaVarPropertyDescriptor.getReadMethod().invoke(atpFormula, new Object[0]);
            Hlayout innerHlayout = new Hlayout();
            String formulaVarLabel = getLabelService().getObjectLabel("AtpFormula." + formulaVarPropertyDescriptor.getName());
            Label formulaLabel = new Label(formulaVarLabel);
            formulaLabel.setClass("atpformulas-availability--bold");
            innerHlayout.appendChild((Component)formulaLabel);
            if("availability".equalsIgnoreCase(formulaVarPropertyDescriptor.getName()))
            {
                Label descLabel = new Label("- " + resolveLabel("warehousingbackoffice.atpformula.formulabuilder.variable.availability.description"));
                descLabel.setClass("atpformulas-availability--description");
                innerHlayout.appendChild((Component)descLabel);
                sliderCheckbox.setClass("atpformulas-availability");
            }
            if(formulaVarValue != null && formulaVarValue.booleanValue())
            {
                sliderCheckbox.setChecked(true);
            }
            else
            {
                sliderCheckbox.setChecked(false);
            }
            innerHlayout.setClass("atpformulas-variables");
            sliderCheckbox.addEventListener("onCheck", checkboxEvent -> handleOnCheckEvent(atpFormula, widgetInstanceManager, formulaVarPropertyDescriptor, sliderCheckbox));
            hlayout.appendChild((Component)signDiv);
            hlayout.appendChild((Component)innerHlayout);
            hlayout.appendChild((Component)sliderCheckbox);
            vlayout.appendChild((Component)hlayout);
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            LOG.error("Failed to interpret the ATP formula. Please review your formula variable {}", formulaVarPropertyDescriptor
                            .getName());
        }
    }


    protected void handleOnCheckEvent(AtpFormulaModel atpFormula, WidgetInstanceManager widgetInstanceManager, PropertyDescriptor formulaVarPropertyDescriptor, Checkbox checkbox)
    {
        try
        {
            formulaVarPropertyDescriptor.getWriteMethod().invoke(atpFormula, new Object[] {Boolean.valueOf(checkbox.isChecked())});
            widgetInstanceManager.getModel().setValue("currentObject", atpFormula);
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            LOG.error("Failed to interpret the ATP formula. Please review your formula variable {}", formulaVarPropertyDescriptor
                            .getName());
        }
    }


    protected void renderAtpFormulaBuilderHeader(Vlayout vlayout)
    {
        Hlayout headerLayout = new Hlayout();
        headerLayout.setClass("atpformulas-header");
        Div variablesHeader = new Div();
        variablesHeader.appendChild((Component)new Label(resolveLabel("warehousingbackoffice.atpformula.formulabuilder.header.variables")));
        variablesHeader.setClass("atpformulabuilderheadervariables");
        headerLayout.appendChild((Component)variablesHeader);
        Div includeVarHeader = new Div();
        includeVarHeader
                        .appendChild((Component)new Label(resolveLabel("warehousingbackoffice.atpformula.formulabuilder.header.variable.include")));
        includeVarHeader.setClass("atpformulabuilderheaderactions");
        headerLayout.appendChild((Component)includeVarHeader);
        vlayout.appendChild((Component)headerLayout);
    }


    protected void setAfterCancelListener(WidgetInstanceManager widgetInstanceManager)
    {
        EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(), "refreshAtpFormulaPanel", event -> {
            this.attributeContainer.getChildren().clear();
            try
            {
                AtpFormulaModel freshAtpFormulaModel = (AtpFormulaModel)getObjectFacade().reload(widgetInstanceManager.getModel().getValue("currentObject", Object.class));
                widgetInstanceManager.getModel().setValue("currentObject", freshAtpFormulaModel);
                renderAtpFormulaVariables((HtmlBasedComponent)this.attributeContainer, freshAtpFormulaModel, widgetInstanceManager);
            }
            catch(ObjectNotFoundException e)
            {
                LOG.error(resolveLabel(widgetInstanceManager.getLabel("warehousingbackoffice.atpforumla.not.found.on.reload")));
            }
        } false);
    }


    protected Set<PropertyDescriptor> getAllAtpFormulaVariables(AtpFormulaModel atpFormula)
    {
        Set<PropertyDescriptor> formulaVariablePropertyDescriptors = new HashSet<>();
        if(atpFormula != null)
        {
            try
            {
                Objects.requireNonNull(formulaVariablePropertyDescriptors);
                Arrays.<PropertyDescriptor>stream(Introspector.getBeanInfo(atpFormula.getClass()).getPropertyDescriptors()).filter(descriptor -> (descriptor.getPropertyType() != null)).filter(descriptor -> descriptor.getPropertyType().equals(Boolean.class))
                                .forEach(formulaVariablePropertyDescriptors::add);
            }
            catch(IntrospectionException e)
            {
                LOG.error("Failed to interpret the ATP formula.");
            }
        }
        return formulaVariablePropertyDescriptors;
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


    protected Map<String, String> getAtpFormulaVar2ArithmeticOperatorMap()
    {
        return this.atpFormulaVar2ArithmeticOperatorMap;
    }


    @Required
    public void setAtpFormulaVar2ArithmeticOperatorMap(Map<String, String> atpFormulaVar2ArithmeticOperatorMap)
    {
        this.atpFormulaVar2ArithmeticOperatorMap = atpFormulaVar2ArithmeticOperatorMap;
    }
}
