package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SavedValuesService;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

public class LastChangesSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(LastChangesSectionConfiguration.class);
    private static final String LAST_CHANGES_ODD_ROW_SCLASS = "lastChangesOddRow";
    private static final String DETAIL_CONTENT_SCLASS = "lastChangesContent";
    private static final String ATTR_DETAIL_LOADED = "detailLoaded";
    protected final SectionRenderer renderer = (SectionRenderer)new LastChangesSectionRenderer(this);
    private SavedValuesService savedValuesService = null;


    public void allInitialized(EditorConfiguration config, ObjectType type, TypedObject object)
    {
    }


    public List<EditorSectionConfiguration> getAdditionalSections()
    {
        return Collections.EMPTY_LIST;
    }


    public SectionRenderer getCustomRenderer()
    {
        return this.renderer;
    }


    public void initialize(EditorConfiguration config, ObjectType type, TypedObject object)
    {
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    private void renderGrid(Component parent, TypedObject currentItem)
    {
        Grid grid = new Grid();
        grid.setSclass("lastChangesGrid");
        grid.setFixedLayout(true);
        grid.setWidth("100%");
        grid.setParent(parent);
        grid.setOddRowSclass("lastChangesOddRow");
        Set<TypedObject> values = (currentItem == null) ? null : getSavedValuesService().getSavedValues(currentItem);
        if(values == null || values.isEmpty())
        {
            parent.appendChild((Component)new Label(Labels.getLabel("general.nothingtodisplay")));
        }
        else
        {
            createColumns(grid);
            createRows(grid, currentItem);
        }
    }


    private void createColumns(Grid grid)
    {
        Columns cols = new Columns();
        cols.setParent((Component)grid);
        cols.setSclass("lastChangesGridCols");
        Column firstCol = new Column();
        firstCol.setParent((Component)cols);
        firstCol.setWidth("25px");
        Column timeCol = new Column(Labels.getLabel("editorarea.lastchanges.timestamp"));
        timeCol.setParent((Component)cols);
        Column authorCol = new Column(Labels.getLabel("editorarea.lastchanges.author"));
        authorCol.setParent((Component)cols);
        Column changeTypeCol = new Column(Labels.getLabel("editorarea.lastchanges.changetype"));
        changeTypeCol.setParent((Component)cols);
    }


    private void createRows(Grid grid, TypedObject currentItem)
    {
        Rows rows = new Rows();
        rows.setParent((Component)grid);
        try
        {
            Set<TypedObject> values = getSavedValuesService().getSavedValues(currentItem);
            if(values != null)
            {
                for(TypedObject value : values)
                {
                    if(value == null)
                    {
                        continue;
                    }
                    Row row = new Row();
                    row.setParent((Component)rows);
                    addRowContent(row, value);
                    addRowDetailContent(row, value);
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not retrieve last changes for active item.", e);
        }
    }


    private void addRowContent(Row row, TypedObject savedValue)
    {
        row.appendChild((Component)new Label(getSavedValuesService().getModificationTime(savedValue)));
        row.appendChild((Component)new Label(getSavedValuesService().getAuthor(savedValue)));
        row.appendChild((Component)new Label(getSavedValuesService().getModificationType(savedValue)));
    }


    private void addRowDetailContent(Row row, TypedObject savedValue)
    {
        Detail detail = new Detail();
        detail.setStyle("padding-bottom:10px;");
        detail.setParent((Component)row);
        detail.setOpen(false);
        detail.setAttribute("detailLoaded", Boolean.FALSE);
        detail.addEventListener("onOpen", (EventListener)new Object(this, detail, savedValue));
    }


    private void renderDetailContent(HtmlBasedComponent parent, TypedObject savedValueEntry)
    {
        Div detailDiv = new Div();
        detailDiv.setParent((Component)parent);
        detailDiv.setWidth("100%");
        detailDiv.setHeight("100%");
        detailDiv.setSclass("lastChangesContent");
        Vbox vbox = new Vbox();
        vbox.setParent((Component)detailDiv);
        Label attrLabel = new Label(getSavedValuesService().getModifiedAttribute(savedValueEntry));
        attrLabel.setParent((Component)vbox);
        attrLabel.setStyle("font-weight:bold");
        List<String> oldValues = getSavedValuesService().getOldValues(savedValueEntry);
        if(!oldValues.isEmpty())
        {
            Hbox hbox = new Hbox();
            hbox.setParent((Component)vbox);
            hbox.setAlign("center");
            hbox.setWidth("100%");
            hbox.setWidths("7em,none");
            Label oldLabel = new Label(Labels.getLabel("general.oldvalue"));
            oldLabel.setParent((Component)hbox);
            Textbox oldValueTextbox = new Textbox();
            oldValueTextbox.setParent((Component)hbox);
            oldValueTextbox.setReadonly(true);
            oldValueTextbox.setValue(oldValues.get(0));
        }
        List<String> newValues = getSavedValuesService().getNewValues(savedValueEntry);
        if(!oldValues.isEmpty())
        {
            Hbox hbox = new Hbox();
            hbox.setParent((Component)vbox);
            hbox.setAlign("center");
            hbox.setWidth("100%");
            hbox.setWidths("7em,none");
            Label newLabel = new Label(Labels.getLabel("general.newvalue"));
            newLabel.setParent((Component)hbox);
            Textbox newValueTextbox = new Textbox();
            newValueTextbox.setParent((Component)hbox);
            newValueTextbox.setReadonly(true);
            newValueTextbox.setValue(newValues.get(0));
        }
    }


    private SavedValuesService getSavedValuesService()
    {
        if(this.savedValuesService == null)
        {
            this.savedValuesService = (SavedValuesService)SpringUtil.getBean("savedValuesService");
        }
        return this.savedValuesService;
    }
}
