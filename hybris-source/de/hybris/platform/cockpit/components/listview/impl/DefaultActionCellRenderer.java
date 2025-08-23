package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ActionCellRenderer;
import de.hybris.platform.cockpit.components.listview.AdvancedListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class DefaultActionCellRenderer implements ActionCellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultActionCellRenderer.class);
    private List<? extends ListViewAction> listViewActions = null;


    public List<ListViewAction> getActions()
    {
        return (List)this.listViewActions;
    }


    public void setActions(List<ListViewAction> listViewActions)
    {
        this.listViewActions = listViewActions;
    }


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null)
        {
            LOG.error("Could not render action cell: table model was null.");
            return;
        }
        Div actionBox = new Div();
        parent.appendChild((Component)actionBox);
        actionBox.setHeight("100%");
        UITools.modifySClass((HtmlBasedComponent)parent, "lvActionCell", true);
        Set<TypedObject> additionalUpdateNotification = new HashSet<>();
        TypedObject item = (TypedObject)model.getListComponentModel().getValueAt(rowIndex);
        ColumnDescriptor column = model.getColumnComponentModel().getVisibleColumns().get(colIndex);
        if(!UISessionUtils.getCurrentSession().getModelService().isNew(item.getObject()))
        {
            List<ListViewAction> actions = getActions();
            for(ListViewAction listViewAction : actions)
            {
                ListViewAction.Context context = listViewAction.createContext(model, item, column);
                Object object = context.getMap().get("affectedItems");
                if(object instanceof Collection)
                {
                    Collection<TypedObject> affectedItems = (Collection<TypedObject>)object;
                    additionalUpdateNotification.addAll(affectedItems);
                }
                String imgURI = listViewAction.getImageURI(context);
                if(imgURI != null && imgURI.length() > 0)
                {
                    Image actionImg = new Image(imgURI);
                    if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                    {
                        String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(item);
                        id = id.replaceAll("\\W", "") + "_" + id.replaceAll("\\W", "");
                        String statusCode = listViewAction.getStatusCode(context);
                        if(statusCode != null)
                        {
                            id = id + "." + id;
                        }
                        UITools.applyTestID((Component)actionImg, id);
                    }
                    actionImg.setStyle("display: inline-block; cursor: pointer;padding-right:3px");
                    Menupopup popup = listViewAction.getPopup(context);
                    registerEventListener(actionImg, listViewAction, context, parent);
                    if(listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
                    {
                        actionImg.setTooltiptext(listViewAction.getTooltip(context));
                    }
                    if(popup != null)
                    {
                        actionBox.appendChild((Component)popup);
                        actionImg.setPopup((Popup)popup);
                    }
                    actionBox.appendChild((Component)actionImg);
                    Menupopup contextPopup = listViewAction.getContextPopup(context);
                    if(contextPopup != null)
                    {
                        actionBox.appendChild((Component)contextPopup);
                        actionImg.setContext((Popup)contextPopup);
                    }
                    if(model.getListComponentModel() != null && !model.getListComponentModel().isEditable() && listViewAction instanceof AdvancedListViewAction &&
                                    !((AdvancedListViewAction)listViewAction).isAlwaysEnabled())
                    {
                        actionImg.setVisible(false);
                    }
                }
            }
        }
        if(model.getListComponentModel() != null)
        {
            model.getListComponentModel().addToAdditionalItemChangeUpdateNotificationMap(item, additionalUpdateNotification);
        }
    }


    protected void registerEventListener(Image actionImg, ListViewAction listViewAction, ListViewAction.Context context, Component parent)
    {
        EventListener listener = listViewAction.getEventListener(context);
        if(listener != null)
        {
            actionImg.addEventListener("onClick", (EventListener)new Object(this, parent));
            actionImg.addEventListener("onClickLater", listener);
            actionImg.addEventListener("onDoubleClick", (EventListener)new Object(this));
        }
    }
}
