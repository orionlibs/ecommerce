package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.EditableSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRowRenderer;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UpdateAwareCustomSectionConfiguration;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.EditorSectionPanelModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public class DefaultEditorAreaController extends AbstractEditorAreaController
{
    public void resetSectionPanelModel()
    {
        EditorSectionPanelModel model = (EditorSectionPanelModel)getSectionPanelModel();
        ((DefaultEditorSectionPanelModel)model).reset();
        clearSectionUpdateRegistry();
        TypedObject current = getModel().getCurrentObject();
        model.setCreateMode((current == null));
        if(current == null)
        {
            if(getModel().getCurrentObjectType() == null)
            {
                ((DefaultEditorSectionPanelModel)model).setLabel(Labels.getLabel("editorarea.nothing_selected"));
                ((DefaultEditorSectionPanelModel)model).setImageUrl(null);
            }
            else
            {
                setDefaultValues();
                addCreationSection(model);
            }
        }
        else
        {
            EditorConfiguration cfg = getModel().getTypeConfiguration();
            Object object = current.getObject();
            if(object instanceof de.hybris.platform.core.model.ItemModel)
            {
                updateLabel((SectionPanelModel)model);
            }
            EditorSection unassignedSection = null;
            for(EditorSectionConfiguration secConf : cfg.getSections())
            {
                List<SectionRow> rows = new ArrayList<>();
                for(EditorRowConfiguration rowConf : secConf.getSectionRows())
                {
                    if(isReadable(rowConf, model.isCreateMode()))
                    {
                        EditorPropertyRow row = new EditorPropertyRow(rowConf);
                        if(row.isValid())
                        {
                            row.setVisible(isVisible(rowConf, model.isCreateMode()));
                            if(rowConf.getPropertyDescriptor() instanceof de.hybris.platform.cockpit.model.meta.impl.WidgetParameterPropertyDescriptor)
                            {
                                String localizedString = Localization.getLocalizedString("cockpit.reports.param." + row.getLabel());
                                if(localizedString != null && !localizedString.startsWith("cockpit.reports.param."))
                                {
                                    row.setLabel(localizedString);
                                }
                            }
                            row.setEditable(isEditable(rowConf, model.isCreateMode()));
                            rows.add(row);
                        }
                    }
                }
                if(secConf instanceof de.hybris.platform.cockpit.services.config.impl.UnassignedEditorSectionConfiguration && unassignedSection == null)
                {
                    unassignedSection = new EditorSection(secConf);
                    ((DefaultEditorSectionPanelModel)model).addSection((Section)unassignedSection, rows);
                    continue;
                }
                if(secConf instanceof de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration)
                {
                    CustomEditorSection customEditorSection = new CustomEditorSection(secConf);
                    ((DefaultEditorSectionPanelModel)model).addSection((Section)customEditorSection, rows);
                    if(secConf instanceof UpdateAwareCustomSectionConfiguration)
                    {
                        registerSectionUpdateHandler(((UpdateAwareCustomSectionConfiguration)secConf).getUpdateTriggerProperties(), ((UpdateAwareCustomSectionConfiguration)secConf)
                                        .getUpdateTriggerTypes(), (Section)customEditorSection);
                    }
                    continue;
                }
                ((DefaultEditorSectionPanelModel)model).addSection((Section)new EditorSection(secConf), rows);
            }
        }
    }


    public void updateSections()
    {
    }


    public SectionRowRenderer getSectionRowRenderer()
    {
        EditorRowRenderer rowRenderer = (EditorRowRenderer)super.getSectionRowRenderer();
        if(rowRenderer == null)
        {
            rowRenderer = new EditorRowRenderer();
            rowRenderer.setEditorFactory(getEditorFactory());
            setSectionRowRenderer((SectionRowRenderer)rowRenderer);
        }
        return (SectionRowRenderer)rowRenderer;
    }


    public EditorFactory getEditorFactory()
    {
        EditorFactory editorFactory = super.getEditorFactory();
        if(editorFactory == null)
        {
            editorFactory = (EditorFactory)SpringUtil.getBean("EditorFactory");
            setEditorFactory(editorFactory);
        }
        return editorFactory;
    }


    protected void storeEditorSectionLabel(EditableSection section)
    {
        if(section instanceof EditorSection)
        {
            EditorSectionConfiguration sectionConfiguration = ((EditorSection)section).getSectionConfiguration();
            Map<LanguageModel, String> labelsMap = new HashMap<>(sectionConfiguration.getAllLabel());
            labelsMap.put(UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage(), section.getLabel());
            sectionConfiguration.setAllLabel(labelsMap);
        }
    }


    protected void storeEditorSectionOrder()
    {
        DefaultEditorSectionPanelModel sectionModel = (DefaultEditorSectionPanelModel)getSectionPanelModel();
        if(!sectionModel.isCreateMode())
        {
            EditorConfiguration config = getModel().getTypeConfiguration();
            if(config != null)
            {
                int sectionPosition = 1;
                List<EditorSectionConfiguration> sectionConfigs = new ArrayList<>();
                for(Section sec : sectionModel.getSections())
                {
                    if(sec instanceof EditorSection)
                    {
                        List<EditorRowConfiguration> rowConfigs = new ArrayList<>();
                        for(SectionRow row : sectionModel.getRows(sec))
                        {
                            if(row instanceof EditorPropertyRow)
                            {
                                EditorRowConfiguration rowConf = ((EditorPropertyRow)row).getRowConfiguration();
                                rowConf.setVisible(((EditorPropertyRow)row).isVisible());
                                rowConfigs.add(rowConf);
                            }
                        }
                        EditorSectionConfiguration secConf = ((EditorSection)sec).getSectionConfiguration();
                        secConf.setPosition(sectionPosition++);
                        secConf.setVisible(((EditorSection)sec).isVisible());
                        secConf.setInitiallyOpened(((EditorSection)sec).isOpen());
                        secConf.setSectionRows(rowConfigs);
                        sectionConfigs.add(secConf);
                    }
                }
                config.setSections(sectionConfigs);
                ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getModel().getCurrentObjectType().getCode());
                getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)config, UISessionUtils.getCurrentSession().getUser(), template, "editorArea", EditorConfiguration.class);
            }
        }
    }


    public void updateLabel(SectionPanelModel sectionPanelModel)
    {
        if(sectionPanelModel instanceof AbstractSectionPanelModel)
        {
            TypedObject currentObject = getModel().getCurrentObject();
            if(currentObject != null)
            {
                LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
                ((AbstractSectionPanelModel)sectionPanelModel).setLabel(labelService.getObjectTextLabel(currentObject));
            }
        }
    }


    protected void setDefaultValues()
    {
        ObjectValueContainer currentObjectValues = getModel().getCurrentObjectValues();
        if(this.initialValues != null)
        {
            for(ObjectValueContainer.ObjectValueHolder holder : this.initialValues.getAllValues())
            {
                try
                {
                    ObjectValueContainer.ObjectValueHolder currentHolder = currentObjectValues.getValue(holder.getPropertyDescriptor(), holder
                                    .getLanguageIso());
                    currentHolder.setLocalValue(holder.getLocalValue());
                }
                catch(IllegalArgumentException e)
                {
                    currentObjectValues.addValue(holder.getPropertyDescriptor(), holder.getLanguageIso(), holder.getLocalValue());
                }
            }
        }
    }
}
