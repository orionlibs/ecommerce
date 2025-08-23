package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.QueryTypeSectionModel;
import de.hybris.platform.cockpit.components.navigationarea.SectionToolbarComponent;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIDynamicQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.PK;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

public class QuerySectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final Logger log = LoggerFactory.getLogger(QuerySectionRenderer.class.getName());


    public static String getDefaultQueryName(UIQuery query)
    {
        return Labels.getLabel("collection.unnamed").equalsIgnoreCase(query.getLabel()) ? "" : query.getLabel();
    }


    protected Separator createSeparator()
    {
        Separator sep = new Separator();
        sep.setBar(true);
        sep.setOrient("horizontal");
        return sep;
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return getNavigationArea().getPerspective().getDragAndDropWrapperService().getWrapper();
    }


    protected Div renderNewCollectionDropArea()
    {
        Div cell = new Div();
        Div singleRow = new Div();
        Hbox h = new Hbox();
        h.setSpacing("0");
        h.setWidths("3,100%,3");
        Div d = new Div();
        d.setSclass("navigation-query-left");
        h.appendChild((Component)d);
        singleRow.setSclass("navigation-query-center");
        h.appendChild((Component)singleRow);
        d = new Div();
        d.setSclass("navigation-query-right");
        h.appendChild((Component)d);
        cell.appendChild((Component)h);
        Div labelDiv = new Div();
        labelDiv.setSclass("navigation-query-label");
        Label label = new Label();
        cell.setSclass("collection-drag4new navigation_collectionlist");
        label.setValue(Labels.getLabel("collection.dragnew"));
        cell.setDroppable("PerspectiveDND");
        cell.addEventListener("onDrop", (EventListener)new Object(this));
        cell.addEventListener("onDoubleClick", (EventListener)new Object(this));
        labelDiv.appendChild((Component)label);
        Textbox pasteOperationArea = new Textbox();
        pasteOperationArea.setStyle("width: 0px; height: 0px; position: fixed; left: -1000px;");
        cell.setTooltiptext(Labels.getLabel("collection.paste.tooltip"));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "NavigationArea_Queries_Create_New_List_element";
            UITools.applyTestID((Component)cell, "NavigationArea_Queries_Create_New_List_element");
        }
        pasteOperationArea.addEventListener("onChanging", (EventListener)new Object(this));
        pasteOperationArea.addEventListener("onFocus", (EventListener)new Object(this, cell));
        pasteOperationArea.addEventListener("onBlur", (EventListener)new Object(this, cell));
        String actionJsBody = "onkeypress: var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();  if(  event.keyCode ==0 )     pressedKey = String.fromCharCode(event.charCode).toLowerCase();  if( event.ctrlKey) if( pressedKey == 'v' )     return true;  return false;";
        pasteOperationArea.setAction("onkeypress: var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();  if(  event.keyCode ==0 )     pressedKey = String.fromCharCode(event.charCode).toLowerCase();  if( event.ctrlKey) if( pressedKey == 'v' )     return true;  return false;");
        Div pasteOperationAreaDiv = new Div();
        pasteOperationAreaDiv.appendChild((Component)pasteOperationArea);
        pasteOperationAreaDiv.setWidth("95%");
        pasteOperationAreaDiv.setStyle("position: relative; overflow: hidden;");
        pasteOperationArea.addEventListener("onFocusLater", (EventListener)new Object(this, pasteOperationArea));
        cell.addEventListener("onClick", (EventListener)new Object(this, pasteOperationArea, cell));
        labelDiv.setTooltiptext(Labels.getLabel("collection.paste.tooltip"));
        singleRow.appendChild((Component)pasteOperationAreaDiv);
        cell.appendChild((Component)labelDiv);
        return cell;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(panel.getModel() instanceof AbstractNavigationAreaModel)
        {
            AbstractNavigationAreaModel model = (AbstractNavigationAreaModel)panel.getModel();
            List<UICollectionQuery> coll = model.getCollections();
            List<UISavedQuery> savedQueries = model.getSavedQueries();
            List<UIDynamicQuery> dynQueries = model.getDynamicQueries();
            QueryRenderer qr = new QueryRenderer(getNavigationArea(), this);
            SectionToolbarComponent toolbar = new SectionToolbarComponent(model);
            toolbar.setParent(parent);
            for(QueryTypeSectionModel buttonModel : model.getAvailableViewModes())
            {
                toolbar.createButton(buttonModel);
            }
            Listbox collList = createList("navigation_collectionlist", coll, (ListitemRenderer)qr);
            Listbox savedList = createList("navigation_savedquerylist", savedQueries, (ListitemRenderer)qr);
            if(savedList != null)
            {
                applyTestIdForCockpitSavedQueries(savedList.getChildren());
            }
            Listbox dynamicList = createList("navigation_dynamicquerylist", dynQueries, (ListitemRenderer)qr);
            Vbox vbox = new Vbox();
            vbox.setSclass("navigation_queries");
            if(collList == null && dynamicList == null && savedList == null)
            {
                vbox.setAlign("left");
                vbox.appendChild((Component)new Label(Labels.getLabel("querysection.empty")));
                vbox.appendChild((Component)renderNewCollectionDropArea());
            }
            else
            {
                if(dynamicList != null)
                {
                    dynamicList.addEventListener("onSelect", (EventListener)new Object(this, savedList, collList));
                    dynamicList.addEventListener("onOK", (EventListener)new Object(this, dynamicList));
                    Integer selectedIndex = getNavigationArea().getSelectedIndex("dynamic");
                    if(selectedIndex != null)
                    {
                        dynamicList.setSelectedIndex(selectedIndex.intValue());
                    }
                    dynamicList.setFixedLayout(true);
                    vbox.appendChild((Component)dynamicList);
                    vbox.appendChild((Component)createSeparator());
                }
                if(collList != null)
                {
                    collList.addEventListener("onSelect", (EventListener)new Object(this, savedList, dynamicList));
                    collList.addEventListener("onOK", (EventListener)new Object(this, collList));
                    Integer selectedIndex = getNavigationArea().getSelectedIndex("query");
                    if(selectedIndex != null && collList.getItemCount() > selectedIndex.intValue())
                    {
                        collList.setSelectedIndex(selectedIndex.intValue());
                    }
                    collList.setFixedLayout(true);
                    vbox.appendChild((Component)collList);
                }
                vbox.appendChild((Component)renderNewCollectionDropArea());
                if(!savedQueries.isEmpty())
                {
                    vbox.appendChild((Component)createSeparator());
                }
                if(savedList != null)
                {
                    savedList.addEventListener("onSelect", (EventListener)new Object(this, collList, dynamicList));
                    savedList.addEventListener("onOK", (EventListener)new Object(this, savedList));
                    Integer selectedIndex = getNavigationArea().getSelectedIndex("saved");
                    if(selectedIndex != null)
                    {
                        if(selectedIndex.intValue() >= savedQueries.size())
                        {
                            selectedIndex = Integer.valueOf(savedQueries.size() - 1);
                        }
                        savedList.setSelectedIndex(selectedIndex.intValue());
                    }
                    savedList.setFixedLayout(true);
                    vbox.appendChild((Component)savedList);
                }
            }
            parent.appendChild((Component)vbox);
        }
        else
        {
            log.error("Could not render section '" + section
                            .getLabel() + "'. Model is not an instance of AbstractNavigationAreaModel.");
        }
    }


    public BaseUICockpitNavigationArea getNavigationArea()
    {
        return (BaseUICockpitNavigationArea)super.getNavigationArea();
    }


    public boolean collectionRemoved(UIQuery query)
    {
        PK pk = null;
        if(query instanceof UICollectionQuery)
        {
            ObjectCollection coll = ((UICollectionQuery)query).getObjectCollection();
            pk = coll.getPK();
        }
        else if(query instanceof UISavedQuery)
        {
            CockpitSavedQueryModel model = ((UISavedQuery)query).getSavedQuery();
            pk = model.getPk();
        }
        else if(query instanceof UIDynamicQuery)
        {
            return false;
        }
        return !UISessionUtils.getCurrentSession().getSystemService().itemExist(pk);
    }


    public void refreshCockpit()
    {
        UISessionUtils.getCurrentSession()
                        .getCurrentPerspective()
                        .getNotifier()
                        .setNotification(new Notification(
                                        Labels.getLabel("collection.not.available"),
                                        Labels.getLabel("collection.not.longer.available")));
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
        UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().update();
        UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().update();
    }


    private ObjectCollectionService getObjectCollectionService()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getObjectCollectionService();
    }


    private void applyTestIdForCockpitSavedQueries(List<Component> savedQueryList)
    {
        for(int i = 0; i < savedQueryList.size(); i++)
        {
            UITools.applyTestID(savedQueryList.get(i), "COCKPIT_SAVED_QUERY_ID_" + i);
        }
    }
}
