package de.hybris.platform.cockpit.helpers.validation;

import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Message;
import de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.impl.DefaultValueHandler;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.EditableComponent;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultEditorSectionPanelModel;
import de.hybris.platform.cockpit.session.impl.EditorArea;
import de.hybris.platform.cockpit.session.impl.EditorPropertyRow;
import de.hybris.platform.cockpit.session.impl.EditorSection;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Br;
import org.zkoss.zhtml.H3;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Space;
import org.zkoss.zul.impl.XulElement;

public class ValidationUIHelper
{
    private static final String ADMINCOCKPIT_CONTEXT = "/admincockpit/index.zul";
    protected static final String PERSP_TAG = "persp";
    protected static final String PERSP_ID = "admincockpit.perspective.admincockpit";
    protected static final String EVENT_TAG = "events";
    protected static final String NAVIGATION_EVENT = "activation";
    protected static final String PNAV_CONSTRAINT = "act-item";
    private static final Logger LOG = LoggerFactory.getLogger(ValidationUIHelper.class);
    private CockpitValidationService validationService;
    private LocalizationService localizationService;
    private TypeService typeService;
    private I18NService i18nService;
    private ModelService modelService;


    public void addTypeConstraintMessages(SectionPanelModel panelModel, Set<CockpitValidationDescriptor> validationInfo)
    {
        List<CockpitValidationDescriptor> typeValidationDescriptors = new ArrayList<>(getValidationService().getTypeValidationDescriptors(validationInfo));
        if(!typeValidationDescriptors.isEmpty())
        {
            Collections.sort(typeValidationDescriptors, (Comparator<? super CockpitValidationDescriptor>)new CockpitValidationDescriptorComparator(this));
            for(CockpitValidationDescriptor cockpitValidationDescriptor : typeValidationDescriptors)
            {
                Message msg = new Message(cockpitValidationDescriptor);
                ((AbstractSectionPanelModel)panelModel).addMessage(msg);
            }
        }
    }


    public Menupopup buildTypeConstraintValidationMenuPopup(String constraintPk, ObjectValueContainer objectValueContainer, int messageLevel, List<String> forceWritePks, SectionPanelModel panelModel)
    {
        Menupopup menuPopup = new Menupopup();
        Menuitem viewConstraintMenuItem = createViewConstraintMenuItem(constraintPk);
        menuPopup.appendChild((Component)viewConstraintMenuItem);
        if(messageLevel == 2)
        {
            Menuitem ignoreConstraintMenuItem = createIgnoreConstraintMenuItem(objectValueContainer, forceWritePks, panelModel);
            menuPopup.appendChild((Component)ignoreConstraintMenuItem);
        }
        return menuPopup;
    }


    public void clearRowMarkings(SectionPanelModel panelModel)
    {
        if(panelModel instanceof RowlayoutSectionPanelModel)
        {
            Set<SectionRow> allRows = ((RowlayoutSectionPanelModel)panelModel).getAllRows();
            for(SectionRow sectionRow : allRows)
            {
                if(sectionRow instanceof EditorPropertyRow)
                {
                    ((RowlayoutSectionPanelModel)panelModel).setRowStatus(sectionRow, -1);
                }
            }
        }
    }


    @Deprecated
    public Menupopup buildTypeConstraintValidationMenuPopup(String constraintPk, ObjectValueContainer objectValueContainer, int messageLevel, List<String> forceWritePks)
    {
        return buildTypeConstraintValidationMenuPopup(constraintPk, objectValueContainer, messageLevel, forceWritePks, null);
    }


    public void clearSectionHeaderMarkings(SectionPanelModel panelModel)
    {
        if(panelModel instanceof RowlayoutSectionPanelModel)
        {
            List<Section> sections = ((RowlayoutSectionPanelModel)panelModel).getSections();
            for(Section section : sections)
            {
                if(section instanceof EditorSection)
                {
                    ((RowlayoutSectionPanelModel)panelModel).setSectionHeaderStatus(section, -1);
                }
            }
        }
    }


    public void markField(SectionPanelModel panelModel, Set<CockpitValidationDescriptor> validationInfo, TypedObject currentObject)
    {
        if(panelModel instanceof RowlayoutSectionPanelModel)
        {
            Set<SectionRow> allRows = ((RowlayoutSectionPanelModel)panelModel).getAllRows();
            for(SectionRow sectionRow : allRows)
            {
                if(sectionRow instanceof EditorPropertyRow)
                {
                    PropertyDescriptor prop = ((EditorPropertyRow)sectionRow).getRowConfiguration().getPropertyDescriptor();
                    Set<CockpitValidationDescriptor> valDescriptorsForProp = getValidationService().getValidationDescriptors(validationInfo, prop);
                    if(!valDescriptorsForProp.isEmpty())
                    {
                        String buildValidationMessages = getValidationService().buildFormattedValidationMessages(valDescriptorsForProp);
                        int rowStatus = -1;
                        List<String> forceWritePks = new ArrayList<>();
                        CockpitValidationDescriptor mostImportantDescr = null;
                        List<CockpitValidationDescriptor> sortedValDescriptors = new ArrayList<>(valDescriptorsForProp);
                        Collections.sort(sortedValDescriptors, (Comparator<? super CockpitValidationDescriptor>)new CockpitValidationDescriptorComparator(this));
                        for(CockpitValidationDescriptor validationDescr : sortedValDescriptors)
                        {
                            int cockpitMessageLevel = validationDescr.getCockpitMessageLevel();
                            if(cockpitMessageLevel == 2)
                            {
                                forceWritePks.add(validationDescr.getConstraintPk());
                            }
                            if(rowStatus < cockpitMessageLevel || rowStatus == -1)
                            {
                                rowStatus = cockpitMessageLevel;
                                mostImportantDescr = validationDescr;
                            }
                        }
                        if(mostImportantDescr != null)
                        {
                            ObjectValueContainer currentObjectValues = getEditorArea(panelModel).getCurrentObjectValues();
                            ((RowlayoutSectionPanelModel)panelModel).setRowStatus(sectionRow, rowStatus, buildValidationMessages);
                            Menupopup menuPopup = new Menupopup();
                            Menuitem viewConstraintMenuItem = createViewConstraintMenuItem(mostImportantDescr.getConstraintPk());
                            menuPopup.appendChild((Component)viewConstraintMenuItem);
                            Menuitem undoMenuItem = createResetValueMenuItem(currentObject, mostImportantDescr
                                            .getPropertyDescriptor(), currentObjectValues, panelModel, sectionRow, validationInfo);
                            menuPopup.appendChild((Component)undoMenuItem);
                            if(rowStatus == 2)
                            {
                                Menuitem ignoreConstraintMenuItem = createIgnoreConstraintMenuItem(currentObjectValues, forceWritePks, panelModel);
                                menuPopup.appendChild((Component)ignoreConstraintMenuItem);
                            }
                            ((RowlayoutSectionPanelModel)panelModel).setValidationIconMenu(sectionRow, menuPopup);
                        }
                    }
                }
            }
        }
    }


    public void markSectionHeader(SectionPanelModel panelModel, Set<CockpitValidationDescriptor> validationInfo)
    {
        if(panelModel instanceof RowlayoutSectionPanelModel)
        {
            List<Section> sections = ((RowlayoutSectionPanelModel)panelModel).getSections();
            for(Section section : sections)
            {
                if(section instanceof EditorSection)
                {
                    List<EditorRowConfiguration> sectionRows = ((EditorSection)section).getSectionConfiguration().getSectionRows();
                    for(EditorRowConfiguration editorRowConfiguration : sectionRows)
                    {
                        Set<CockpitValidationDescriptor> validationDescriptors = getValidationService().getValidationDescriptors(validationInfo, editorRowConfiguration.getPropertyDescriptor());
                        if(!validationDescriptors.isEmpty())
                        {
                            int highestMessageLevel = getValidationService().getHighestMessageLevel(validationDescriptors);
                            ((RowlayoutSectionPanelModel)panelModel).setSectionHeaderStatus(section, highestMessageLevel);
                        }
                    }
                }
            }
        }
    }


    private Menuitem createIgnoreConstraintMenuItem(ObjectValueContainer container, List<String> forceWritePks, SectionPanelModel panelModel)
    {
        Menuitem menuItem = new Menuitem(Labels.getLabel(getSaveAnywayKey(container.getObject())));
        menuItem.setCheckmark(true);
        menuItem.addEventListener("onClick", (EventListener)new Object(this, forceWritePks, container, menuItem, panelModel));
        return menuItem;
    }


    private Menuitem createResetValueMenuItem(TypedObject typedObject, PropertyDescriptor propertyDescriptor, ObjectValueContainer objectValueContainer, SectionPanelModel panelModel, SectionRow sectionRow, Set<CockpitValidationDescriptor> validationInfo)
    {
        Menuitem menuItem = new Menuitem(Labels.getLabel("validation.menupopup.reset.value"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, propertyDescriptor, objectValueContainer, panelModel, typedObject, sectionRow, validationInfo));
        return menuItem;
    }


    public Menuitem createViewConstraintMenuItem(String constraintPk)
    {
        Menuitem menuItem = new Menuitem(Labels.getLabel("validation.menupopup.view.constraint"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, constraintPk));
        return menuItem;
    }


    private EditorArea getEditorArea(SectionPanelModel sectionPanelModel)
    {
        EditorArea ret = null;
        if(sectionPanelModel instanceof DefaultEditorSectionPanelModel)
        {
            UIEditorArea editorArea = ((DefaultEditorSectionPanelModel)sectionPanelModel).getEditorArea();
            if(editorArea instanceof EditorArea)
            {
                ret = (EditorArea)editorArea;
            }
        }
        if(ret == null)
        {
            ret = (EditorArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea();
        }
        return ret;
    }


    private int getNumWarnings(Set<CockpitValidationDescriptor> validationInfo)
    {
        Set<CockpitValidationDescriptor> warnings = getValidationService().filterByMessageLevel(validationInfo, 2);
        int numWarnings = 0;
        if(warnings != null)
        {
            numWarnings = warnings.size();
        }
        return numWarnings;
    }


    private String getSaveAnywayKey(Object object)
    {
        String result;
        if(object != null && !UISessionUtils.getCurrentSession().getModelService().isNew(object))
        {
            result = "validation.menupopup.save.anyway";
        }
        else
        {
            result = "validation.menupopup.ignore.warnings";
        }
        return result;
    }


    private CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }


    public void createValidationMessages(HtmlBasedComponent parent, Set<CockpitValidationDescriptor> violations, TypedObject typedObject, Set<String> forceWritePks, EditableComponent editableComponent)
    {
        CockpitValidationDescriptor mostImportantViolation = violations.iterator().next();
        PropertyDescriptor propertyDescriptor = mostImportantViolation.getPropertyDescriptor();
        XulElement zkElement = null;
        if(propertyDescriptor != null && "FEATURE".equals(propertyDescriptor.getEditorType()))
        {
            zkElement = parent.getFirstChild().getChildren().get(0);
        }
        else
        {
            zkElement = parent.getChildren().get(0);
        }
        int level = getValidationService().getHighestMessageLevel(violations);
        Menupopup menuPopup = buildConstraintValidationMenuPopup(parent, mostImportantViolation, forceWritePks, violations, editableComponent);
        Popup tooltip = buildConstraintValidationTooltip(violations);
        changeSClass(parent, level);
        zkElement.appendChild((Component)menuPopup);
        zkElement.appendChild((Component)tooltip);
        Image image = (Image)parent.getAttribute("validationIconAdded");
        if(image == null)
        {
            image = buildConstraintValidationIcon(level, menuPopup, tooltip);
            zkElement.appendChild((Component)image);
            parent.setAttribute("validationIconAdded", image);
        }
        else
        {
            updateConstraintValidationIcon(image, level, menuPopup, tooltip);
        }
    }


    public Set<CockpitValidationDescriptor> sortCockpitValidationDescriptors(Set<CockpitValidationDescriptor> violations)
    {
        Set<CockpitValidationDescriptor> result = new TreeSet<>((Comparator<? super CockpitValidationDescriptor>)new Object(this));
        result.addAll(violations);
        return result;
    }


    public void changeSClass(HtmlBasedComponent parent, int cockpitMessageLevel)
    {
        switch(cockpitMessageLevel)
        {
            case 3:
                UITools.modifySClass(parent, "sectionRowError", true);
                UITools.modifySClass(parent, "sectionRowWarning", false);
                UITools.modifySClass(parent, "sectionRowOkWarning", false);
                UITools.modifySClass(parent, "sectionRowInfo", false);
                break;
            case 2:
                UITools.modifySClass(parent, "sectionRowError", false);
                UITools.modifySClass(parent, "sectionRowWarning", true);
                UITools.modifySClass(parent, "sectionRowOkWarning", false);
                UITools.modifySClass(parent, "sectionRowInfo", false);
                break;
            case 1:
                UITools.modifySClass(parent, "sectionRowError", false);
                UITools.modifySClass(parent, "sectionRowWarning", false);
                UITools.modifySClass(parent, "sectionRowOkWarning", true);
                UITools.modifySClass(parent, "sectionRowInfo", false);
                break;
            case 0:
                UITools.modifySClass(parent, "sectionRowError", false);
                UITools.modifySClass(parent, "sectionRowWarning", false);
                UITools.modifySClass(parent, "sectionRowOkWarning", false);
                UITools.modifySClass(parent, "sectionRowInfo", true);
                break;
        }
    }


    private Popup buildConstraintValidationTooltip(Set<CockpitValidationDescriptor> violations)
    {
        Popup tooltip = new Popup();
        H3 popupHeader = new H3();
        Label headerLabel = new Label();
        headerLabel.setValue(Labels.getLabel("validation.popup.header"));
        popupHeader.appendChild((Component)headerLabel);
        tooltip.appendChild((Component)popupHeader);
        for(CockpitValidationDescriptor violation : violations)
        {
            tooltip.appendChild((Component)new Image("/cockpit/images/" + getValidationIcon(violation.getCockpitMessageLevel())));
            tooltip.appendChild((Component)new Space());
            tooltip.appendChild((Component)new Label(violation.getValidationMessage()));
            tooltip.appendChild((Component)new Br());
        }
        tooltip.setSclass("validationTooltipPopup");
        tooltip.setStyle("max-width: 400px");
        return tooltip;
    }


    public Popup buildConstraintValidationTooltip(Message violation)
    {
        Popup tooltip = new Popup();
        H3 popupHeader = new H3();
        Label headerLabel = new Label();
        headerLabel.setValue(Labels.getLabel("validation.popup.header"));
        popupHeader.appendChild((Component)headerLabel);
        tooltip.appendChild((Component)popupHeader);
        tooltip.appendChild((Component)new Image("/cockpit/images/" + getValidationIcon(violation.getLevel())));
        tooltip.appendChild((Component)new Space());
        tooltip.appendChild((Component)new Label(violation.getMessageText()));
        tooltip.appendChild((Component)new Br());
        tooltip.setSclass("validationTooltipPopup");
        tooltip.setStyle("max-width: 400px");
        return tooltip;
    }


    private Menupopup buildConstraintValidationMenuPopup(HtmlBasedComponent parent, CockpitValidationDescriptor violation, Set<String> forceWritePks, Set<CockpitValidationDescriptor> violations, EditableComponent editableComponent)
    {
        List<String> warningPKs = new ArrayList<>();
        for(CockpitValidationDescriptor warning : violations)
        {
            if(warning.getCockpitMessageLevel() == 2 || warning.getCockpitMessageLevel() == 1)
            {
                warningPKs.add(warning.getConstraintPk());
            }
        }
        Menupopup menuPopup = new Menupopup();
        Menuitem viewConstraintMenuItem = createViewConstraintMenuItem(violation.getConstraintPk());
        menuPopup.appendChild((Component)viewConstraintMenuItem);
        Menuitem undoMenuItem = createResetValueMenuItem(editableComponent);
        menuPopup.appendChild((Component)undoMenuItem);
        if(violation.getCockpitMessageLevel() == 2)
        {
            Menuitem ignoreConstraintMenuItem = createIgnoreConstraintMenuItem(parent, forceWritePks, warningPKs);
            menuPopup.appendChild((Component)ignoreConstraintMenuItem);
        }
        return menuPopup;
    }


    private Menuitem createIgnoreConstraintMenuItem(HtmlBasedComponent parent, Set<String> forceWritePks, List<String> warningsPKs)
    {
        Menuitem menuItem = new Menuitem(Labels.getLabel("validation.menupopup.ignore.warnings"));
        menuItem.setCheckmark(true);
        menuItem.addEventListener("onClick", (EventListener)new Object(this, forceWritePks, warningsPKs, menuItem, parent));
        return menuItem;
    }


    private Menuitem createResetValueMenuItem(EditableComponent editableComponent)
    {
        Menuitem menuItem = new Menuitem(Labels.getLabel("validation.menupopup.reset.value"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, editableComponent));
        return menuItem;
    }


    public Image buildConstraintValidationIcon(int level, Menupopup menuPopup, Popup tooltip)
    {
        Image image = new Image();
        image.setStyle("position:absolute;right:-20px;top:+3px");
        image.setZindex(888);
        image.setSrc("/cockpit/images/" + getValidationIcon(level));
        image.setPopup((Popup)menuPopup);
        image.setTooltip(tooltip);
        return image;
    }


    private void updateConstraintValidationIcon(Image image, int level, Menupopup menuPopup, Popup tooltip)
    {
        image.setSrc("/cockpit/images/" + getValidationIcon(level));
        image.setPopup((Popup)menuPopup);
        image.setTooltip(tooltip);
    }


    public String getValidationIcon(int messageLvl)
    {
        String result = "";
        switch(messageLvl)
        {
            case 3:
                result = "validation_error_14.gif";
                return result;
            case 2:
                result = "validation_warning_14.gif";
                return result;
            case 0:
                result = "validation_info_14.gif";
                return result;
            case 1:
                result = "validation_warning_14.gif";
                return result;
        }
        result = "validation_info_14.gif";
        return result;
    }


    public boolean allWarningsForced(Set<String> forceWritePks, Set<CockpitValidationDescriptor> violations)
    {
        if(forceWritePks.isEmpty())
        {
            return false;
        }
        List<String> violationsPKs = new ArrayList<>();
        for(CockpitValidationDescriptor violation : violations)
        {
            if(violation.getCockpitMessageLevel() == 3)
            {
                return false;
            }
            if(violation.getCockpitMessageLevel() == 2)
            {
                violationsPKs.add(violation.getConstraintPk());
            }
        }
        if(forceWritePks.containsAll(violationsPKs))
        {
            return true;
        }
        return false;
    }


    public ObjectValueContainer createModelFromContainer(TypedObject typedObject, String languageIso, PropertyDescriptor propertyDescriptor, UIEditor editor)
    {
        DefaultValueHandler dvh = new DefaultValueHandler(propertyDescriptor);
        ObjectValueContainer currentObjectValues = dvh.getValueContainer((ObjectType)typedObject.getType(), typedObject);
        if(propertyDescriptor.isLocalized())
        {
            Object oldValue = dvh.getValue(typedObject, languageIso);
            currentObjectValues.addValue(propertyDescriptor, languageIso, oldValue);
            currentObjectValues.getValue(propertyDescriptor, languageIso).setLocalValue(editor.getValue());
        }
        else
        {
            Object oldValue = dvh.getValue(typedObject);
            currentObjectValues.addValue(propertyDescriptor, null, oldValue);
            currentObjectValues.getValue(propertyDescriptor, null).setLocalValue(editor.getValue());
        }
        Iterator<ObjectValueContainer.ObjectValueHolder> allValuesIterator = currentObjectValues.getAllValues().iterator();
        while(allValuesIterator.hasNext())
        {
            ObjectValueContainer.ObjectValueHolder ovh = allValuesIterator.next();
            if(ovh.isModified() && !"FEATURE".equals(propertyDescriptor.getEditorType()))
            {
                if(ovh.getPropertyDescriptor().isLocalized())
                {
                    Map<Locale, Object> values = new HashMap<>();
                    values.put(getLocalizationService().getLocaleByString(languageIso), ovh.getLocalValue());
                    setModelLocalizedValue((ItemModel)currentObjectValues.getObject(),
                                    getTypeService().getAttributeCodeFromPropertyQualifier(ovh.getPropertyDescriptor().getQualifier()), languageIso, values);
                    continue;
                }
                setModelSingleValue((ItemModel)currentObjectValues.getObject(),
                                getTypeService().getAttributeCodeFromPropertyQualifier(ovh.getPropertyDescriptor().getQualifier()), ovh
                                                .getLocalValue());
            }
        }
        return currentObjectValues;
    }


    private LocalizationService getLocalizationService()
    {
        if(this.localizationService == null)
        {
            this.localizationService = (LocalizationService)SpringUtil.getBean("localizationService");
        }
        return this.localizationService;
    }


    TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    private I18NService getI18NService()
    {
        if(this.i18nService == null)
        {
            this.i18nService = (I18NService)SpringUtil.getBean("i18nService");
        }
        return this.i18nService;
    }


    ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public void setModelSingleValue(ItemModel model, String qualifier, Object value) throws AttributeNotSupportedException
    {
        try
        {
            Object _value = value;
            if(value instanceof TypedObject)
            {
                _value = ((TypedObject)value).getObject();
            }
            Object valueModel = getModelService().toModelLayer(_value);
            getModelService().setAttributeValue(model, qualifier, valueModel);
        }
        catch(IllegalArgumentException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, qualifier);
        }
    }


    public void setModelLocalizedValue(ItemModel model, String qualifier, String languageIso, Object value) throws AttributeNotSupportedException
    {
        I18NService i18n = getI18NService();
        Locale backup = i18n.getCurrentLocale();
        try
        {
            i18n.setCurrentLocale(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIso).getLocale());
            Object valueModel = getModelService().toModelLayer(value);
            getModelService().setAttributeValue(model, qualifier, valueModel);
        }
        catch(IllegalArgumentException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, qualifier);
        }
        finally
        {
            i18n.setCurrentLocale(backup);
        }
    }
}
