package de.hybris.platform.cockpit.components.inspector.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.CoverageInfoAction;
import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ListActionHelper;
import de.hybris.platform.cockpit.util.ListProvider;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.coverage.CoverageCalculationService;
import de.hybris.platform.validation.coverage.CoverageInfo;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

public class DefaultCoverageInspectorRenderer extends AbstractInspectorRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCoverageInspectorRenderer.class);
    private CoverageCalculationService coverageCalculationService;
    private boolean showOnlyVisibleRows = true;


    public void renderEmpty(Component parent)
    {
        Label label = new Label(Labels.getLabel("editorarea.nothing_selected"));
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        UITools.applyTestID((Component)headerDiv, "inspectorMode");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
    }


    public void render(Component parent, TypedObject object)
    {
        String objectTextLabel = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(object);
        Label label = new Label(objectTextLabel);
        label.setTooltiptext(objectTextLabel);
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        UITools.applyTestID((Component)headerDiv, "inspectorMode");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
        Div toolbarDiv = new Div();
        toolbarDiv.setSclass("infoHeaderToolbar");
        headerDiv.appendChild((Component)toolbarDiv);
        renderToolbar((Component)toolbarDiv, object);
        Div propertiesCnt = new Div();
        propertiesCnt.setSclass("inspectorPropertiesCnt");
        parent.appendChild((Component)propertiesCnt);
        propertiesCnt.appendChild(createCoverageComponent(object));
        propertiesCnt.appendChild(createFilledValuesComponent(object));
    }


    protected void renderToolbar(Component parent, TypedObject item)
    {
        prepareEditActionButton(parent, item);
        Div coverageActionCnt = new Div();
        coverageActionCnt.setSclass("inspectorToolbarButton roundedCorners");
        UITools.applyTestID((Component)coverageActionCnt, "inspectorToolbarCoverageButton");
        parent.appendChild((Component)coverageActionCnt);
        CoverageInfoAction coverageAction = (CoverageInfoAction)Registry.getApplicationContext().getBean("CoverageInfoAction", CoverageInfoAction.class);
        if(coverageAction != null)
        {
            ListViewAction.Context context = coverageAction.createContext((ListComponentModel)ListActionHelper.createDefaultListModel(item), item);
            ListActionHelper.renderSingleAction((ListViewAction)coverageAction, context, (Component)coverageActionCnt, "infoToolbarAction");
        }
    }


    protected Component createCoverageComponent(TypedObject typedObject)
    {
        Div ret = new Div();
        Object object = typedObject.getObject();
        if(this.coverageCalculationService != null && object instanceof ItemModel)
        {
            Div coverageSection = new Div();
            coverageSection.setSclass("infoAreaCoverageSection");
            ret.appendChild((Component)coverageSection);
            Div sectionLabelCnt = new Div();
            sectionLabelCnt.setSclass("infoSectionLabelCnt");
            coverageSection.appendChild((Component)sectionLabelCnt);
            Label sectionLabel = new Label(Labels.getLabel("cockpit.coverage.label"));
            sectionLabel.setSclass("infoAreaSectionLabel");
            sectionLabelCnt.appendChild((Component)sectionLabel);
            CoverageInfo coverageInfo = this.coverageCalculationService.calculate((ItemModel)object, "cockpit");
            if(coverageInfo != null)
            {
                double coverageIndex = coverageInfo.getCoverageIndex();
                Label percentageLabel = new Label("(" + Integer.valueOf((int)Math.round(coverageIndex * 100.0D)) + "%)");
                sectionLabelCnt.appendChild((Component)percentageLabel);
                List<CoverageInfo.CoveragePropertyInfoMessage> propertyInfoMessages = coverageInfo.getPropertyInfoMessages();
                for(CoverageInfo.CoveragePropertyInfoMessage coveragePropertyInfoMessage : propertyInfoMessages)
                {
                    Hbox hbox = new Hbox();
                    Div covInfoMsgCnt = new Div();
                    covInfoMsgCnt.setSclass("covInfoMsgCnt");
                    covInfoMsgCnt.appendChild((Component)hbox);
                    coverageSection.appendChild((Component)covInfoMsgCnt);
                    String propQualifier = StringUtils.isNotBlank(coveragePropertyInfoMessage.getPropertyQualifier()) ? coveragePropertyInfoMessage.getPropertyQualifier() : "Item.composedType";
                    PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propQualifier);
                    String propertyLabel = (propertyDescriptor == null) ? "" : propertyDescriptor.getName();
                    Label rowLabel = new Label(propertyLabel);
                    Div div = new Div();
                    div.setSclass("infoAreaLabelDiv");
                    div.appendChild((Component)rowLabel);
                    hbox.appendChild((Component)div);
                    Label valueLabel = new Label(coveragePropertyInfoMessage.getMessage());
                    div = new Div();
                    div.setSclass("infoAreaValueDiv");
                    hbox.appendChild((Component)div);
                    div.appendChild((Component)valueLabel);
                }
            }
        }
        return (Component)ret;
    }


    protected Component createFilledValuesComponent(TypedObject object)
    {
        UIConfigurationService uiConfigurationService = UISessionUtils.getCurrentSession().getUiConfigurationService();
        ObjectTemplate bestTemplate = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(object);
        String configCode = UITools.getCockpitParameter("default.inspector.configcode", Executions.getCurrent());
        EditorConfiguration componentConfiguration = (EditorConfiguration)uiConfigurationService.getComponentConfiguration(bestTemplate,
                        StringUtils.isBlank(configCode) ? "editorArea" : configCode, EditorConfiguration.class);
        List<EditorSectionConfiguration> sections = componentConfiguration.getSections();
        Div ret = new Div();
        for(EditorSectionConfiguration editorSectionConfiguration : sections)
        {
            if(editorSectionConfiguration.isVisible() && (!this.showOnlyVisibleRows || editorSectionConfiguration.isInitiallyOpened()))
            {
                String label = (editorSectionConfiguration instanceof DefaultEditorSectionConfiguration) ? ((DefaultEditorSectionConfiguration)editorSectionConfiguration).getLabelWithFallback() : editorSectionConfiguration.getLabel();
                Label sectionLabel = new Label(label);
                sectionLabel.setSclass("infoAreaSectionLabel");
                Div infoSectionLabelCnt = new Div();
                infoSectionLabelCnt.setSclass("infoSectionLabelCnt");
                infoSectionLabelCnt.appendChild((Component)sectionLabel);
                List<EditorRowConfiguration> sectionRows = editorSectionConfiguration.getSectionRows();
                if(CollectionUtils.isNotEmpty(sectionRows))
                {
                    ret.appendChild((Component)infoSectionLabelCnt);
                    int realNrRows = 0;
                    for(EditorRowConfiguration editorRowConfiguration : sectionRows)
                    {
                        if(editorRowConfiguration.isVisible() || !this.showOnlyVisibleRows)
                        {
                            try
                            {
                                Object value = getValueService().getValue(object, editorRowConfiguration.getPropertyDescriptor());
                                if(value != null && (!(value instanceof Collection) || !CollectionUtils.isEmpty((Collection)value)))
                                {
                                    Div rowCnt = new Div();
                                    rowCnt.setSclass("inspectorRowCnt");
                                    Hbox hbox = new Hbox();
                                    rowCnt.appendChild((Component)hbox);
                                    ret.appendChild((Component)rowCnt);
                                    String labelString = editorRowConfiguration.getPropertyDescriptor().getName();
                                    if(StringUtils.isEmpty(labelString))
                                    {
                                        labelString = "[" + editorRowConfiguration.getPropertyDescriptor().getQualifier() + "]";
                                    }
                                    Label rowLabel = new Label(labelString);
                                    Div infoAreaLabelDiv = new Div();
                                    infoAreaLabelDiv.setSclass("infoAreaLabelDiv");
                                    infoAreaLabelDiv.appendChild((Component)rowLabel);
                                    hbox.appendChild((Component)infoAreaLabelDiv);
                                    Label valueLabel = new Label(TypeTools.getValueAsString(UISessionUtils.getCurrentSession()
                                                    .getLabelService(), value));
                                    infoAreaLabelDiv = new Div();
                                    infoAreaLabelDiv.setSclass("infoAreaValueDiv");
                                    hbox.appendChild((Component)infoAreaLabelDiv);
                                    infoAreaLabelDiv.appendChild((Component)valueLabel);
                                    realNrRows++;
                                }
                            }
                            catch(ValueHandlerException e)
                            {
                                LOG.error("Could not get value for property '" + editorRowConfiguration.getPropertyDescriptor() + "', reason: ", (Throwable)e);
                            }
                        }
                    }
                    if(realNrRows == 0)
                    {
                        infoSectionLabelCnt.detach();
                    }
                }
            }
        }
        return (Component)ret;
    }


    public void render(Component parent, ListProvider<TypedObject> objectsProvider)
    {
        Label label = new Label(Labels.getLabel("inspector.multipleSelection", (Object[])new String[] {String.valueOf(objectsProvider.getListSize())}));
        label.setSclass("");
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
    }


    @Required
    public void setCoverageCalculationService(CoverageCalculationService coverageCalculationService)
    {
        this.coverageCalculationService = coverageCalculationService;
    }


    public void setShowOnlyVisibleRows(boolean showOnlyVisibleRows)
    {
        this.showOnlyVisibleRows = showOnlyVisibleRows;
    }


    public boolean isShowOnlyVisibleRows()
    {
        return this.showOnlyVisibleRows;
    }
}
