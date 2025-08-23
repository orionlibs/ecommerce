package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class ListActionHelper
{
    public static MutableListModel createDefaultListModel(TypedObject item)
    {
        DefaultListComponentModel defaultListComponentModel = new DefaultListComponentModel();
        defaultListComponentModel.setEditable(true);
        defaultListComponentModel.setSelectable(true);
        defaultListComponentModel.setActivatable(true);
        defaultListComponentModel.setMultiple(true);
        DefaultListModel<TypedObject> resultModel = new DefaultListModel();
        resultModel.add(item);
        defaultListComponentModel.setListModel((ListModel)resultModel);
        return (MutableListModel)defaultListComponentModel;
    }


    public static void renderSingleAction(ListViewAction listViewAction, ListViewAction.Context context, Component parent, String imageStyleClass)
    {
        String imgURI = listViewAction.getImageURI(context);
        if(imgURI != null && imgURI.length() > 0)
        {
            Image actionImg = new Image(imgURI);
            actionImg.setSclass(imageStyleClass);
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(context.getItem());
                id = id.replaceAll("\\W", "") + "_" + id.replaceAll("\\W", "");
                String statusCode = listViewAction.getStatusCode(context);
                if(statusCode != null)
                {
                    id = id + "." + id;
                }
                UITools.applyTestID((Component)actionImg, id);
            }
            EventListener listener = listViewAction.getEventListener(context);
            if(listener != null)
            {
                actionImg.addEventListener("onClick", listener);
                actionImg.addEventListener("onLater", listener);
            }
            if(listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
            {
                actionImg.setTooltiptext(listViewAction.getTooltip(context));
            }
            Menupopup popup = listViewAction.getPopup(context);
            if(popup != null)
            {
                parent.appendChild((Component)popup);
                actionImg.setPopup((Popup)popup);
            }
            parent.appendChild((Component)actionImg);
            Menupopup contextPopup = listViewAction.getContextPopup(context);
            if(contextPopup != null)
            {
                parent.appendChild((Component)contextPopup);
                actionImg.setContext((Popup)contextPopup);
            }
        }
    }


    public static void renderActions(Component parent, TypedObject item, ActionColumnConfiguration actionConfiguration, String imageStyleClass)
    {
        renderActions(parent, item, actionConfiguration, imageStyleClass, Collections.EMPTY_MAP);
    }


    public static void renderActions(Component parent, TypedObject item, ActionColumnConfiguration actionConfiguration, String imageStyleClass, Map<?, ?> ctx)
    {
        if(actionConfiguration == null)
        {
            return;
        }
        MutableListModel listComponentModel = createDefaultListModel(item);
        List<ListViewAction> listViewActions = actionConfiguration.getActions();
        Set<TypedObject> additionalUpdateNotification = new HashSet<>();
        Div actionBox = new Div();
        parent.appendChild((Component)actionBox);
        for(ListViewAction listViewAction : listViewActions)
        {
            ListViewAction.Context context = listViewAction.createContext((ListComponentModel)listComponentModel, item);
            Object object = context.getMap().get("affectedItems");
            if(object instanceof Collection)
            {
                Collection<TypedObject> affectedItems = (Collection<TypedObject>)object;
                additionalUpdateNotification.addAll(affectedItems);
            }
            context.getMap().putAll(ctx);
            renderSingleAction(listViewAction, context, (Component)actionBox, imageStyleClass);
        }
    }
}
