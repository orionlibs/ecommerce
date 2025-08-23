package de.hybris.platform.cockpit.components.menu;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;

public abstract class AbstractAssignedListRenderer<K> implements AssignedListRenderer
{
    private static final int SHORT_LIST_MAX = 8;
    private static final int LONG_LIST_MAX = 25;
    private final List<K> assignedValuesList;
    private int valuesListLong;
    private int valuesListShort;


    public AbstractAssignedListRenderer(List<K> assignedValuesList)
    {
        this.assignedValuesList = assignedValuesList;
    }


    public HtmlBasedComponent createMenuListViewComponent()
    {
        fillParameters();
        Div mainContainer = new Div();
        Listbox collectionAssignedItems = new Listbox();
        collectionAssignedItems.setDisabled(true);
        collectionAssignedItems.setFixedLayout(false);
        collectionAssignedItems.setItemRenderer(createAvailableCollectionMenuItemListRenderer());
        boolean showMore = (this.assignedValuesList.size() > this.valuesListShort);
        if(showMore)
        {
            setResultListData(collectionAssignedItems, (List)this.assignedValuesList, this.valuesListShort);
        }
        else
        {
            setResultListData(collectionAssignedItems, (List)this.assignedValuesList, this.assignedValuesList.size());
        }
        mainContainer.appendChild((Component)collectionAssignedItems);
        if(showMore)
        {
            Div labelContainer = new Div();
            labelContainer.setClass("userList-adjust");
            Label label = showMoreLabel(collectionAssignedItems, labelContainer);
            labelContainer.appendChild((Component)label);
            mainContainer.appendChild((Component)labelContainer);
        }
        collectionAssignedItems.setClass("assignedList_short");
        return (HtmlBasedComponent)mainContainer;
    }


    private void fillParameters()
    {
        int listShortMax, listLongMax;
        String shortListParam = UITools.getCockpitParameter("default.menu.shortList.max", Executions.getCurrent());
        if(StringUtils.isNotBlank(shortListParam))
        {
            listShortMax = Integer.parseInt(shortListParam);
        }
        else
        {
            listShortMax = 8;
        }
        if(listShortMax < this.assignedValuesList.size())
        {
            this.valuesListShort = listShortMax;
        }
        else
        {
            this.valuesListShort = this.assignedValuesList.size();
        }
        String longListParam = UITools.getCockpitParameter("default.menu.longList.max", Executions.getCurrent());
        if(StringUtils.isNotBlank(longListParam))
        {
            listLongMax = Integer.parseInt(longListParam);
        }
        else
        {
            listLongMax = 25;
        }
        if(listLongMax < this.assignedValuesList.size())
        {
            this.valuesListLong = listLongMax;
        }
        else
        {
            this.valuesListLong = this.assignedValuesList.size();
        }
    }


    protected Label showMoreLabel(Listbox collectionAssignedItems, Div labelContainer)
    {
        Label label = new Label(Labels.getLabel("referenceselector.more"));
        label.addEventListener("onClick", (EventListener)new Object(this, collectionAssignedItems, labelContainer, label));
        collectionAssignedItems.setClass("assignedList_short");
        return label;
    }


    protected Label showLessLabel(Listbox collectionAssignedItems, Div labelContainer)
    {
        Label label = new Label(Labels.getLabel("referenceselector.less"));
        label.setStyle("cursor:pointer;");
        label.addEventListener("onClick", (EventListener)new Object(this, collectionAssignedItems, labelContainer, label));
        return label;
    }


    protected void setResultListData(Listbox collectionItems, List<? extends Object> resultList, int rows)
    {
        collectionItems.setModel((ListModel)new SimpleListModel(resultList));
        collectionItems.setRows(rows);
    }


    protected ObjectCollectionService getObjectCollectionService()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getObjectCollectionService();
    }


    protected void setNotification(String captionKey, String messageKey, Object... messageAttrs)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier()
                        .setNotification(new Notification(Labels.getLabel(captionKey), Labels.getLabel(messageKey, messageAttrs)));
    }


    protected List<? extends Object> getAssignedValuesList()
    {
        return (List)this.assignedValuesList;
    }
}
