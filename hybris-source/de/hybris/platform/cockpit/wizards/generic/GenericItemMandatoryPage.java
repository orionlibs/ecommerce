package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.config.impl.WizardConfigurationFactory;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public class GenericItemMandatoryPage extends AbstractGenericItemPage
{
    private static final Logger LOG = LoggerFactory.getLogger(GenericItemMandatoryPage.class);
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String WIZARD_GROUP_LABEL = "wizardGroupLabel";
    protected static final String WIZARD_GROUP_CONTAINER = "wizardGroupContainer";
    protected static final String WIZARD_GROUP_EDITOR_CONTAINER = "wizardGroupEditorContainer";
    protected static final String WIZARD_GROUP_ROW_LABEL = "wizardRowGroupLabel";
    protected static final String WIZARD_SECTION = "wizardGroupLabel";
    private Object value;
    private UIConfigurationService uiConfigurationService;
    private Map<String, ? extends Object> parameters = new HashMap<>();
    private DefaultGenericItemMandatoryPageController defaultController = new DefaultGenericItemMandatoryPageController();


    public DefaultGenericItemMandatoryPageController getDefaultController()
    {
        return this.defaultController;
    }


    public void setDefaultController(DefaultGenericItemMandatoryPageController defaultController)
    {
        this.defaultController = defaultController;
    }


    public GenericItemMandatoryPage()
    {
    }


    public GenericItemMandatoryPage(String pageTitle)
    {
        super(pageTitle);
    }


    public GenericItemMandatoryPage(String pageTitle, GenericItemWizard wizard)
    {
        super(pageTitle, (Wizard)wizard);
    }


    public WizardPageController getController()
    {
        return (super.getController() == null) ? (WizardPageController)getDefaultController() : super.getController();
    }


    protected final Component createGroupRow(Component parent, PropertyDescriptor descriptor, TypedObject currentObject, String editorCode, int errorLevel, Map<String, String> params)
    {
        Vbox groupRowContainer = new Vbox();
        groupRowContainer.setParent(parent);
        groupRowContainer.setWidth("100%");
        Div itemDiv = new Div();
        itemDiv.setHeight("100%");
        itemDiv.setWidth("100%");
        Hbox horizontalBox = new Hbox();
        horizontalBox.setParent((Component)itemDiv);
        horizontalBox.setWidth("100%");
        switch(errorLevel)
        {
            case 3:
                horizontalBox.setSclass("contentElementErrorBoxEditor");
                break;
            case 1:
            case 2:
                horizontalBox.setSclass("contentElementWarningBoxEditor");
                break;
            case 0:
                horizontalBox.setSclass("contentElementInfoBoxEditor");
                break;
            default:
                horizontalBox.setSclass("contentElementBoxEditor");
                break;
        }
        Vbox cellContainer = new Vbox();
        cellContainer.setSclass("wizardGroupContainer");
        String label = descriptor.getName();
        if(StringUtils.isEmpty(label))
        {
            label = (label == null || label.isEmpty()) ? ("[" + descriptor.getQualifier() + "]") : descriptor.getName();
        }
        Label rowLabel = new Label(label + ":");
        rowLabel.setSclass("wizardRowGroupLabel");
        cellContainer.appendChild((Component)rowLabel);
        horizontalBox.appendChild((Component)cellContainer);
        Div editorContainer = new Div();
        editorContainer.setSclass("wizardGroupEditorContainer");
        cellContainer.appendChild((Component)editorContainer);
        Map<String, Object> parameters = new HashMap<>(params);
        parameters.put(AdditionalReferenceEditorListener.class.getName(), new Object(this));
        if(StringUtils.isNotEmpty(editorCode))
        {
            EditorHelper.createEditor(currentObject, descriptor, (HtmlBasedComponent)editorContainer, getWizard().getObjectValueContainer(), false, editorCode, parameters);
        }
        else
        {
            EditorHelper.createEditor(currentObject, descriptor, (HtmlBasedComponent)editorContainer, getWizard().getObjectValueContainer(), false, null, parameters);
        }
        groupRowContainer.appendChild((Component)itemDiv);
        return (Component)groupRowContainer;
    }


    protected void createGroupSectionContent(HtmlBasedComponent group, Collection<String> allreadyDisplayedProperties, Collection<String> displaydDescriptors, WizardConfiguration wizardConfiguration)
    {
        for(String stringDescriptor : displaydDescriptors)
        {
            if(!allreadyDisplayedProperties.contains(stringDescriptor))
            {
                PropertyDescriptor descriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(stringDescriptor);
                String editorCode = (String)wizardConfiguration.getQualifiers().get(descriptor.getQualifier());
                Map<String, String> params = wizardConfiguration.getParameterMap(descriptor.getQualifier());
                loadInitConfigValues(descriptor, wizardConfiguration);
                createGroupRow((Component)group, descriptor, getWizard().getItem(), editorCode,
                                getWizard().getCurrentPage().isCauseError(descriptor), params);
            }
        }
    }


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        ObjectType currentType = getWizard().getCurrentType();
        WizardConfiguration wizardConfiguration = getWizardConfiguration();
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        ObjectTemplate currentObjectTemplate = typeService.getObjectTemplate(currentType.getCode());
        Map<PropertyDescriptor, Object> prefilledDescriptorsMap = TypeTools.getAllDefaultValues(typeService, currentObjectTemplate,
                        Collections.singleton(UISessionUtils.getCurrentSession().getLanguageIso()));
        Set<PropertyDescriptor> mandatoryDescriptors = TypeTools.getMandatoryAttributes(currentType, true);
        List<PropertyDescriptor> requiredForInstance = new ArrayList<>(mandatoryDescriptors);
        requiredForInstance.removeAll(prefilledDescriptorsMap.keySet());
        for(String qualifier : getWizard().getPredefinedValues().keySet())
        {
            PropertyDescriptor descriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(qualifier);
            if(requiredForInstance.contains(descriptor))
            {
                requiredForInstance.remove(descriptor);
            }
        }
        for(PropertyDescriptor propertyDescriptor : requiredForInstance)
        {
            loadInitConfigValues(propertyDescriptor, wizardConfiguration);
        }
        Set<String> propertiesToRemoveFromEssential = new HashSet<>(wizardConfiguration.getQualifiers().keySet());
        propertiesToRemoveFromEssential.removeAll(wizardConfiguration.getUnboundProperties().keySet());
        propertiesToRemoveFromEssential.addAll(wizardConfiguration.getQualifiers(false).keySet());
        for(String propertyQualifier : propertiesToRemoveFromEssential)
        {
            PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier);
            loadInitConfigValues(propertyDescriptor, wizardConfiguration);
            requiredForInstance.remove(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier));
        }
        render((HtmlBasedComponent)this.pageContent, extractPropertyQualifiers(requiredForInstance));
        return (Component)this.pageContainer;
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


    protected List<String> extractPropertyQualifiers(List<PropertyDescriptor> propertyDescriptors)
    {
        List<String> ret = new ArrayList<>();
        for(PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            ret.add(propertyDescriptor.getQualifier());
        }
        return ret;
    }


    public List<String> getDisplayedAttributes()
    {
        List<String> ret = new ArrayList<>();
        if(getWizardConfiguration() != null)
        {
            return new ArrayList<>(getWizardConfiguration().getQualifiers(true).keySet());
        }
        return ret;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public Object getValue()
    {
        return this.value;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        if(getWizard().getCurrentType() != null)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getWizard().getCurrentType().getCode());
            return (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "wizardConfig", WizardConfiguration.class);
        }
        return null;
    }


    protected void loadInitConfigValues(PropertyDescriptor descriptor, WizardConfiguration wizardConfig)
    {
        ObjectValueContainer objectValueContainer = getWizard().getObjectValueContainer();
        ObjectValueContainer.ObjectValueHolder objectValueHolder = null;
        if(descriptor.isLocalized())
        {
            objectValueHolder = objectValueContainer.getValue(descriptor, UISessionUtils.getCurrentSession().getLanguageIso());
        }
        else
        {
            objectValueHolder = objectValueContainer.getValue(descriptor, null);
        }
        if(objectValueHolder != null)
        {
            Map<String, String> parameters = wizardConfig.getParameterMap(descriptor.getQualifier());
            if(parameters != null)
            {
                String rawValue = parameters.get("defaultValue");
                if(parameters.containsKey("defaultValueResolver"))
                {
                    String resolverBeanId = parameters.get("defaultValueResolver");
                    DefaultValueResolver resolver = (DefaultValueResolver)SpringUtil.getBean(resolverBeanId);
                    if(resolver != null)
                    {
                        Object defaultValue = resolver.getValue(rawValue);
                        objectValueHolder.setLocalValue(defaultValue);
                        objectValueHolder.stored();
                    }
                    else
                    {
                        LOG.warn("No resolver found for bean id " + resolverBeanId);
                    }
                }
                else if(parameters.containsKey("defaultValue"))
                {
                    if("BOOLEAN".equals(descriptor.getEditorType()) && rawValue != null)
                    {
                        Boolean booleanValue = Boolean.valueOf(rawValue);
                        objectValueHolder.setLocalValue(booleanValue);
                        objectValueHolder.stored();
                    }
                    else
                    {
                        objectValueHolder.setLocalValue(parameters.get("defaultValue"));
                        objectValueHolder.stored();
                    }
                }
            }
        }
    }


    protected void render(HtmlBasedComponent parent, Collection<String> requiredDescriptors)
    {
        WizardConfiguration wizardConfiguration = getWizardConfiguration();
        HtmlBasedComponent localParent = parent;
        List<String> alreadyRenderedProperties = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(requiredDescriptors))
        {
            AdvancedGroupbox advancedGroupbox = createGroup(parent, true, Labels.getLabel("wizard.essentials"));
            createGroupSectionContent((HtmlBasedComponent)advancedGroupbox, Collections.EMPTY_LIST, requiredDescriptors, wizardConfiguration);
            alreadyRenderedProperties.addAll(requiredDescriptors);
        }
        if(CollectionUtils.isNotEmpty(getWizard().getPredefinedValues().keySet()) && wizardConfiguration.isShowPrefilledValues())
        {
            AdvancedGroupbox advancedGroupbox = createGroup(parent, true, Labels.getLabel("wizard.initially.fulfilled"));
            Collection<String> initialyFullfilledProperties = getWizard().getPredefinedValues().keySet();
            createGroupSectionContent((HtmlBasedComponent)advancedGroupbox, alreadyRenderedProperties, initialyFullfilledProperties, wizardConfiguration);
            alreadyRenderedProperties.addAll(initialyFullfilledProperties);
        }
        if(CollectionUtils.isNotEmpty(wizardConfiguration.getGroups().keySet()))
        {
            int index = 0;
            for(WizardConfigurationFactory.WizardGroupConfiguration wizardGroupConfiguration : wizardConfiguration.getGroups().keySet())
            {
                AdvancedGroupbox advancedGroupbox = createGroup(parent, (index == 0 || wizardGroupConfiguration.isInitiallyOpened()), wizardGroupConfiguration
                                .getLabel());
                List<String> propertyDescriptors = (List<String>)wizardConfiguration.getGroups().get(wizardGroupConfiguration);
                createGroupSectionContent((HtmlBasedComponent)advancedGroupbox, alreadyRenderedProperties, propertyDescriptors, wizardConfiguration);
                alreadyRenderedProperties.addAll(propertyDescriptors);
                index++;
            }
        }
        if(CollectionUtils.isNotEmpty(wizardConfiguration.getUnboundProperties().keySet()))
        {
            AdvancedGroupbox advancedGroupbox = createGroup(parent, true, Labels.getLabel("wizard.unbound"));
            Set<String> unboundPropertyDescriptors = wizardConfiguration.getUnboundProperties().keySet();
            createGroupSectionContent((HtmlBasedComponent)advancedGroupbox, alreadyRenderedProperties, unboundPropertyDescriptors, wizardConfiguration);
            alreadyRenderedProperties.addAll(unboundPropertyDescriptors);
        }
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }
}
