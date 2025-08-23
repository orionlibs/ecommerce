package de.hybris.platform.admincockpit.components.navigationarea.renderer;

import de.hybris.platform.admincockpit.session.impl.AdmincockpitConstraintBrowserModel;
import de.hybris.platform.admincockpit.util.ComposedTypeComparator;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserArea;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.validation.services.ConstraintService;
import de.hybris.platform.validation.services.ValidationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

public class ConstraintsSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(ConstraintsSectionRenderer.class);
    private ValidationService validationService;
    private ConstraintService constraintService;


    @Required
    public void setValidationService(ValidationService validationService)
    {
        this.validationService = validationService;
    }


    @Required
    public void setConstraintService(ConstraintService constraintService)
    {
        this.constraintService = constraintService;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(panel.getModel() instanceof de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel)
        {
            Vbox vbox = new Vbox();
            vbox.setSclass("navigation_queries");
            Label constrainedTypeLabel = new Label(Labels.getLabel("na.constrainedSection.constrained_type_label") + ": ");
            vbox.appendChild((Component)constrainedTypeLabel);
            Listbox constrainedComposedTypeListBox = createConstrainedTypeListbox();
            renderConstrainedComposedTypeListBox(vbox, constrainedComposedTypeListBox);
            addSeparatorBar(vbox);
            addSeparatorBar(vbox);
            renderAllConstraintsButton(vbox, constrainedComposedTypeListBox);
            addSeparatorBar(vbox);
            renderAllConstraintsPojoButton(vbox);
            addSeparatorBar(vbox);
            renderReloadValidationEngineButton(vbox, constrainedComposedTypeListBox);
            parent.appendChild((Component)vbox);
        }
    }


    public BaseUICockpitNavigationArea getNavigationArea()
    {
        return (BaseUICockpitNavigationArea)super.getNavigationArea();
    }


    private UIBrowserArea getBrowserArea()
    {
        return getNavigationArea().getPerspective().getBrowserArea();
    }


    private void addSeparatorBar(Vbox vbox)
    {
        Separator separator = new Separator();
        separator.setBar(true);
        vbox.appendChild((Component)separator);
    }


    private void markListAsEmpty(Vbox vbox)
    {
        Label constrainedTypeEmptyListLabel = new Label(Labels.getLabel("na.constrainedSection.empty_list_label"));
        constrainedTypeEmptyListLabel.setSclass("navigation-query-label");
        vbox.appendChild((Component)constrainedTypeEmptyListLabel);
    }


    private Listbox createConstrainedTypeListbox()
    {
        List<ComposedTypeModel> listComposedTypeModel = new ArrayList<>(this.constraintService.getConstraintedComposedTypes());
        Collections.sort(listComposedTypeModel, (Comparator<? super ComposedTypeModel>)new ComposedTypeComparator());
        List<TypedObject> listTypeObject = new ArrayList<>();
        for(ComposedTypeModel composedTypeModel : listComposedTypeModel)
        {
            TypedObject wrappedComposedTypeModel = UISessionUtils.getCurrentSession().getTypeService().wrapItem(composedTypeModel.getPk());
            listTypeObject.add(wrappedComposedTypeModel);
        }
        ConstrainedTypeListitemRenderer typeListitemRenderer = new ConstrainedTypeListitemRenderer();
        Listbox constrainedComposedTypeListBox = createList("navigation_collectionlist", listTypeObject, (ListitemRenderer)typeListitemRenderer);
        if(constrainedComposedTypeListBox != null)
        {
            constrainedComposedTypeListBox.setMold("paging");
            int pgsz = Config.getInt("admincockpit.na.constrained_item_types_paging", 10);
            constrainedComposedTypeListBox.setPageSize(pgsz);
        }
        return constrainedComposedTypeListBox;
    }


    private void updateBrowserItems(Set<ComposedTypeModel> composedTypeModelsSet)
    {
        BrowserModel browserModel = getNavigationArea().getPerspective().getBrowserArea().getFocusedBrowser();
        if(browserModel instanceof de.hybris.platform.admincockpit.session.impl.AdmincockpitConstraintPojoBrowserModel || browserModel instanceof de.hybris.platform.admincockpit.session.impl.AdmincockpitConstraintGroupsBrowserModel)
        {
            String typeCode = ((DefaultSearchBrowserArea)getBrowserArea()).getRootSearchTypeCode();
            AdmincockpitConstraintBrowserModel admincockpitBrowserModel = new AdmincockpitConstraintBrowserModel(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(typeCode));
            getBrowserArea().replaceBrowser(getBrowserArea().getFocusedBrowser(), (BrowserModel)admincockpitBrowserModel);
            admincockpitBrowserModel.setComposedTypeModelsSet(composedTypeModelsSet);
            admincockpitBrowserModel.updateItems(0);
        }
        else if(browserModel instanceof AdmincockpitConstraintBrowserModel)
        {
            AdmincockpitConstraintBrowserModel admincockpitBrowserModel = (AdmincockpitConstraintBrowserModel)browserModel;
            admincockpitBrowserModel.setComposedTypeModelsSet(composedTypeModelsSet);
            ObjectTemplate objectTemplate = admincockpitBrowserModel.getRootType();
            List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService()
                            .getSearchType((ObjectType)objectTemplate));
            Query query = new Query(searchTypes, null, 0, admincockpitBrowserModel.getPageSize());
            query.setContextParameter("objectTemplate", objectTemplate);
            List<SearchParameterValue> parameterValues = new LinkedList<>();
            List<List<SearchParameterValue>> orValues = new LinkedList<>();
            query.setParameterValues(parameterValues);
            query.setParameterOrValues(orValues);
            admincockpitBrowserModel.updateItems(query);
        }
    }


    private void renderConstrainedComposedTypeListBox(Vbox vbox, Listbox constrainedComposedTypeListBox)
    {
        if(constrainedComposedTypeListBox != null)
        {
            constrainedComposedTypeListBox.setMultiple(true);
            constrainedComposedTypeListBox.addEventListener("onSelect", (EventListener)new Object(this, constrainedComposedTypeListBox));
            vbox.appendChild((Component)constrainedComposedTypeListBox);
            Hbox hbox = new Hbox();
            vbox.appendChild((Component)hbox);
            hbox.appendChild((Component)new Label(Labels.getLabel("na.constrainedSection.pager_label")));
            Combobox pageSizeCombobox = new Combobox();
            hbox.appendChild((Component)pageSizeCombobox);
            pageSizeCombobox.setSclass("constrained_componsed_type_listbox_pagesize_changer");
            pageSizeCombobox.setWidth("50px");
            hbox.setWidth("100%");
            hbox.setPack("end");
            hbox.setAlign("center");
            String pageDefaultValue = Config.getString("admincockpit.na.constrained_item_types_paging", "10");
            pageSizeCombobox.setValue(pageDefaultValue);
            String page_default_values = Config.getString("admincockpit.na.constrained_item_types_paging_defaults", "10,20");
            ListModelList listModel = new ListModelList((Object[])page_default_values.split(","));
            pageSizeCombobox.setModel((ListModel)listModel);
            Object object = new Object(this, pageDefaultValue, constrainedComposedTypeListBox);
            pageSizeCombobox.addEventListener("onChange", (EventListener)object);
            pageSizeCombobox.addEventListener("onOK", (EventListener)object);
        }
        else
        {
            markListAsEmpty(vbox);
        }
    }


    private void renderAllConstraintsPojoButton(Vbox vbox)
    {
        renderCustomButton(vbox, "na.constrainedSection.all_pojo_constraints_button", "onClick", (EventListener)new Object(this));
    }


    private void renderAllConstraintsButton(Vbox vbox, Listbox constrainedComposedTypeListBox)
    {
        renderCustomButton(vbox, "na.constrainedSection.all_constraints_button", "onClick", (EventListener)new Object(this, constrainedComposedTypeListBox));
    }


    private void renderReloadValidationEngineButton(Vbox vbox, Listbox constrainedComposedTypeListBox)
    {
        renderCustomButton(vbox, "na.constrainedSection.reload_engine_button", "onClick", (EventListener)new Object(this, constrainedComposedTypeListBox));
    }


    private void renderCustomButton(Vbox vbox, String key, String evtnm, EventListener evtListener)
    {
        Button customButton = new Button(Labels.getLabel(key));
        customButton.addEventListener(evtnm, evtListener);
        vbox.appendChild((Component)customButton);
    }


    private void clearConstrainedComposedTypeListBoxSelection(Listbox constrainedComposedTypeListBox)
    {
        if(constrainedComposedTypeListBox != null)
        {
            constrainedComposedTypeListBox.clearSelection();
        }
    }
}
