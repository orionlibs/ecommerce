package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.contentbrowser.AdvanceMenu;
import de.hybris.platform.cockpit.components.contentbrowser.AdvanceMenupopup;
import de.hybris.platform.cockpit.components.menu.ManageMenuitem;
import de.hybris.platform.cockpit.components.menu.impl.UsersAssignedListQueryRenderer;
import de.hybris.platform.cockpit.components.menu.impl.UsersAssignedListSavedQueryRenderer;
import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.QueryTypeSectionModel;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryUserRightsService;
import de.hybris.platform.cockpit.services.query.impl.DummySavedQueryUserRightsServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

public class QueryRenderer implements ListitemRenderer
{
    private UserService userService;
    private final QueryRendererUtils queryRendererUtils = new QueryRendererUtils();
    public static final String COCKPITGROUP_UID = "cockpitgroup";
    private static final Logger LOG = LoggerFactory.getLogger(QueryRenderer.class);
    private BaseUICockpitNavigationArea navigationArea;
    private final QuerySectionRenderer querySectionRenderer;


    public QueryRenderer(BaseUICockpitNavigationArea navigationArea, QuerySectionRenderer querySectionRenderer)
    {
        this.navigationArea = navigationArea;
        this.querySectionRenderer = querySectionRenderer;
    }


    public BaseUICockpitNavigationArea getNavigationArea()
    {
        return this.navigationArea;
    }


    public void render(Listitem item, Object data) throws Exception
    {
        String queryTyp;
        Listcell cell = new Listcell();
        Hbox hbox = new Hbox();
        hbox.setSpacing("0");
        hbox.setWidths("3,100%,3");
        Div div = new Div();
        div.setSclass("navigation-query-left");
        hbox.appendChild((Component)div);
        div = new Div();
        div.setSclass("navigation-query-center");
        hbox.appendChild((Component)div);
        div = new Div();
        div.setSclass("navigation-query-right");
        hbox.appendChild((Component)div);
        cell.appendChild((Component)hbox);
        Div labelDiv = new Div();
        labelDiv.setSclass("navigation-query-label");
        Label label = new Label();
        Menupopup menupopup = null;
        Textbox pasteOperationArea = null;
        if(data instanceof UICollectionQuery)
        {
            queryTyp = "query";
            UICollectionQuery coll = (UICollectionQuery)data;
            if(coll.getObjectCollection().getUser() == null)
            {
                UITools.modifySClass((HtmlBasedComponent)cell, "global-collection", true);
            }
            label.setValue(((UICollectionQuery)data).getLabel() + " (" + ((UICollectionQuery)data).getLabel() + ")");
            UserModel collectionOwner = ((UICollectionQuery)data).getObjectCollection().getUser();
            if(collectionOwner != null && collectionOwner.equals(getCurrentUser()))
            {
                menupopup = createCollectionContextMenu((UICollectionQuery)data, label, (Component)cell);
            }
            cell.setDroppable("PerspectiveDND");
            cell.addEventListener("onDrop", (EventListener)new Object(this, coll, data, label));
            cell.addEventListener("onCtrlKey", (EventListener)new Object(this));
            if(coll.isInitial())
            {
                coll.setInitial(false);
                cell.addEventListener("onRenameEvent", (EventListener)new Object(this, coll, cell));
                Events.echoEvent("onRenameEvent", (Component)cell, null);
            }
            pasteOperationArea = new Textbox();
            pasteOperationArea.setSclass("paste_to_collection_input");
            pasteOperationArea.addEventListener("onChanging", (EventListener)new Object(this, coll));
            String actionJsBody = "onkeypress: var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();  if(  event.keyCode == 0 )     pressedKey = String.fromCharCode(event.charCode).toLowerCase();  if( event.ctrlKey ) if( pressedKey == 'v' )     return true;  return false;";
            pasteOperationArea.setAction("onkeypress: var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();  if(  event.keyCode == 0 )     pressedKey = String.fromCharCode(event.charCode).toLowerCase();  if( event.ctrlKey ) if( pressedKey == 'v' )     return true;  return false;");
        }
        else if(data instanceof UISavedQuery)
        {
            queryTyp = "saved";
            UISavedQuery query = (UISavedQuery)data;
            if(query.getSavedQuery().getUser() == null)
            {
                UITools.modifySClass((HtmlBasedComponent)cell, "global-savedquery", true);
            }
            label.setValue(query.getLabel());
            menupopup = createSavedQueryContextMenu((UISavedQuery)data, label, (Component)cell);
            Div imgDiv = new Div();
            QueryTypeSectionModel selectedQueryType = ((AbstractNavigationAreaModel)getNavigationArea().getSectionModel()).getSelectedQueryType();
            imgDiv.setSclass(selectedQueryType.getListItemSclass());
            cell.appendChild((Component)imgDiv);
            UITools.modifySClass((HtmlBasedComponent)labelDiv, "saved-query-list-item-navigation-query-label", true);
        }
        else if(data instanceof de.hybris.platform.cockpit.model.query.impl.UIDynamicQuery)
        {
            label.setValue(((UIQuery)data).getLabel());
            queryTyp = "dynamic";
        }
        else
        {
            queryTyp = null;
        }
        labelDiv.appendChild((Component)label);
        if(pasteOperationArea != null)
        {
            pasteOperationArea.addEventListener("onFocusLater", (EventListener)new Object(this));
            labelDiv.appendChild((Component)pasteOperationArea);
            if(item.equals(item.getListbox().getSelectedItem()))
            {
                Events.echoEvent("onFocusLater", (Component)pasteOperationArea, null);
            }
        }
        if(menupopup != null)
        {
            cell.appendChild((Component)menupopup);
            cell.setContext((Popup)menupopup);
            if(data instanceof UICollectionQuery &&
                            getObjectCollectionService().isCollectionOwner((PrincipalModel)getCurrentUser(), ((UICollectionQuery)data)
                                            .getObjectCollection()).booleanValue())
            {
                Menu sendToOthersSumbmenuRead = createSendToSubmenu(item, data, CollectionRight.READ);
                if(sendToOthersSumbmenuRead != null)
                {
                    menupopup.appendChild((Component)sendToOthersSumbmenuRead);
                }
                Menu sendToOthersSumbmenuWrite = createSendToSubmenu(item, data, CollectionRight.WRITE);
                if(sendToOthersSumbmenuWrite != null)
                {
                    menupopup.appendChild((Component)sendToOthersSumbmenuWrite);
                }
            }
            if(data instanceof UISavedQuery)
            {
                Menu sendToSavedQuerySumbmenuRead = createSendToSavedQuerySubmenu(item, data, CollectionRight.READ);
                if(sendToSavedQuerySumbmenuRead != null)
                {
                    menupopup.appendChild((Component)sendToSavedQuerySumbmenuRead);
                }
            }
        }
        else
        {
            cell.addEventListener("onRightClick", (EventListener)new Object(this, data));
        }
        cell.appendChild((Component)labelDiv);
        UITools.addBusyListener((Component)item, "onClick", (EventListener)new Object(this, data, queryTyp, item, labelDiv), null, null);
        item.appendChild((Component)cell);
        item.setValue(data);
    }


    public void setNavigationArea(BaseUICockpitNavigationArea navigationArea)
    {
        this.navigationArea = navigationArea;
    }


    protected void buildMenuRecursively(Menupopup parentMenupopup, PrincipalModel parentPrincipal, List<PrincipalModel> readUsers, EventListener listener)
    {
        if(parentPrincipal instanceof UserGroupModel)
        {
            UserGroupModel model = (UserGroupModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            Menu groupMenu = new Menu(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(modelObject));
            Menupopup groupMenupopup = new Menupopup();
            Menuitem allgroupMembers = new Menuitem(Labels.getLabel("collection.all.group.members"));
            allgroupMembers.setCheckmark(true);
            allgroupMembers.setChecked(readUsers.contains(model));
            allgroupMembers.setAttribute("principal", model);
            allgroupMembers.addEventListener("onClick", listener);
            allgroupMembers.setParent((Component)groupMenupopup);
            Menuseparator menuseparator = new Menuseparator();
            menuseparator.setParent((Component)groupMenupopup);
            parentMenupopup.addEventListener("onOpen", (EventListener)new Object(this, groupMenupopup, model, readUsers, listener));
            groupMenupopup.setParent((Component)groupMenu);
            groupMenu.setParent((Component)parentMenupopup);
        }
        else if(parentPrincipal instanceof UserModel)
        {
            UserModel model = (UserModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            Menuitem userMenuitem = new Menuitem(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(modelObject));
            userMenuitem.setCheckmark(true);
            userMenuitem.setChecked(readUsers.contains(model));
            userMenuitem.setAttribute("principal", model);
            userMenuitem.addEventListener("onClick", listener);
            userMenuitem.setParent((Component)parentMenupopup);
        }
    }


    protected void buildSavedQueryMenuRecursively(Menupopup parentMenupopup, PrincipalModel parentPrincipal, List<PrincipalModel> readUsers, EventListener listener)
    {
        if(parentPrincipal instanceof UserGroupModel)
        {
            UserGroupModel model = (UserGroupModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            boolean checked = readUsers.contains(model);
            int totalUserCount = this.queryRendererUtils.getTotalUserCount(model);
            int checkedUserCount = this.queryRendererUtils.getCheckedUserCount(model, readUsers);
            String menuLabel = null;
            if(checked || totalUserCount == checkedUserCount)
            {
                menuLabel = this.queryRendererUtils.buildGroupMenuLabel(UISessionUtils.getCurrentSession().getLabelService()
                                .getObjectTextLabel(modelObject), totalUserCount);
            }
            else
            {
                menuLabel = this.queryRendererUtils.buildGroupMenuLabel(UISessionUtils.getCurrentSession().getLabelService()
                                .getObjectTextLabel(modelObject), checkedUserCount, totalUserCount);
            }
            Menu groupMenu = new Menu(menuLabel);
            groupMenu.setTooltiptext(this.queryRendererUtils.getGroupMenuItemTooltip(UISessionUtils.getCurrentSession().getLabelService()
                            .getObjectTextLabel(modelObject), checkedUserCount, totalUserCount, checked));
            Menupopup groupMenupopup = new Menupopup();
            Menuitem allgroupMembers = new Menuitem(Labels.getLabel("collection.all.group.members"));
            allgroupMembers.setCheckmark(true);
            allgroupMembers.setChecked(checked);
            allgroupMembers.setAttribute("principal", model);
            allgroupMembers.addEventListener("onClick", listener);
            allgroupMembers.setParent((Component)groupMenupopup);
            Menuseparator menuseparator = new Menuseparator();
            menuseparator.setParent((Component)groupMenupopup);
            parentMenupopup.addEventListener("onOpen", (EventListener)new Object(this, groupMenupopup, model, readUsers, listener));
            groupMenupopup.setParent((Component)groupMenu);
            groupMenu.setParent((Component)parentMenupopup);
        }
        else if(parentPrincipal instanceof UserModel)
        {
            UserModel model = (UserModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            Menuitem userMenuitem = new Menuitem(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(modelObject));
            userMenuitem.setCheckmark(true);
            userMenuitem.setChecked(readUsers.contains(model));
            userMenuitem.setAttribute("principal", model);
            userMenuitem.addEventListener("onClick", listener);
            userMenuitem.setParent((Component)parentMenupopup);
        }
    }


    protected Menupopup createCollectionContextMenu(UICollectionQuery query, Label queryLabel, Component parent)
    {
        Menupopup ret = new Menupopup();
        ret.addEventListener("onOpen", (EventListener)new Object(this, query));
        Menuitem menuItem = new Menuitem(Labels.getLabel("query.rename"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, query, parent));
        menuItem.setParent((Component)ret);
        menuItem = new Menuitem(Labels.getLabel("query.delete"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, query));
        menuItem.setParent((Component)ret);
        ret.appendChild((Component)new Menuseparator());
        return ret;
    }


    protected Menupopup createSavedQueryContextMenu(UISavedQuery query, Label queryLabel, Component parent)
    {
        Menupopup ret = new Menupopup();
        ret.addEventListener("onOpen", (EventListener)new Object(this, query));
        UserModel queryUser = query.getSavedQuery().getUser();
        boolean isCreator = (queryUser != null && queryUser.equals(getCurrentUser()));
        QueryRendererUtils.SavedQuerySharingMode savedQuerySharingMode = this.queryRendererUtils.getSavedQuerySharingMode(getCurrentUser(), query
                        .getSavedQuery());
        if(isCreator)
        {
            Menuitem menuItem = new Menuitem(this.queryRendererUtils.getSavedQueryRenameLabel(savedQuerySharingMode));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, query, queryLabel, parent));
            menuItem.setParent((Component)ret);
            menuItem = new Menuitem(this.queryRendererUtils.getSavedQueryDeleteLabel(savedQuerySharingMode));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, query));
            menuItem.setParent((Component)ret);
        }
        return ret;
    }


    protected Menu createSendToSubmenu(Listitem item, Object data, CollectionRight right)
    {
        if(!(data instanceof UICollectionQuery))
        {
            return null;
        }
        UICollectionQuery query = (UICollectionQuery)data;
        CheckboxClickListener checkboxClickListener = new CheckboxClickListener(this, query, right);
        String label = null;
        List<PrincipalModel> rightUusers = null;
        if(right == CollectionRight.READ)
        {
            label = Labels.getLabel("collection.send.collection.as_read.to");
            rightUusers = getObjectCollectionService().getReadUsers(query.getObjectCollection());
        }
        else if(right == CollectionRight.WRITE)
        {
            label = Labels.getLabel("collection.send.collection.as_write.to");
            rightUusers = getObjectCollectionService().getWriteUsers(query.getObjectCollection());
        }
        AdvanceMenu sendMenu = new AdvanceMenu(label);
        Popup popup = new Popup();
        popup.appendChild((Component)new Textbox());
        sendMenu.setPopup(popup);
        sendMenu.setContext(popup);
        sendMenu.appendChild((Component)popup);
        if(getObjectCollectionService().isCollectionOwner((PrincipalModel)getCurrentUser(), query.getObjectCollection()).booleanValue())
        {
            AdvanceMenupopup sendMenupopup = new AdvanceMenupopup();
            UserGroupModel groupModel = getUserService().getUserGroupForUID("cockpitgroup");
            UsersAssignedListQueryRenderer editor = new UsersAssignedListQueryRenderer(UISessionUtils.getCurrentSession().getTypeService().wrapItems(rightUusers), query, right.toString());
            sendMenupopup.appendChild((Component)editor.createMenuListViewComponent());
            List<PrincipalModel> usersWithRights = rightUusers;
            if(!usersWithRights.isEmpty())
            {
                sendMenupopup.appendChild((Component)new Menuseparator());
            }
            Object object = new Object(this, right, query, usersWithRights);
            ManageMenuitem manageMenuitem = new ManageMenuitem(Labels.getLabel("workflow.users.manage"), (EventListener)object);
            sendMenupopup.appendChild((Component)manageMenuitem);
            sendMenupopup.appendChild((Component)new Menuseparator());
            buildMenuRecursively((Menupopup)sendMenupopup, (PrincipalModel)groupModel, rightUusers, (EventListener)checkboxClickListener);
            sendMenupopup.setStyle("overflow:hidden");
            sendMenupopup.setParent((Component)sendMenu);
        }
        return (Menu)sendMenu;
    }


    protected Menu createSendToSavedQuerySubmenu(Listitem item, Object data, CollectionRight right)
    {
        if(!(data instanceof UISavedQuery))
        {
            return null;
        }
        UISavedQuery query = (UISavedQuery)data;
        CheckboxSavedQueryClickListener checkboxSavedQueryClickListener = new CheckboxSavedQueryClickListener(this, query, CollectionRight.READ);
        String label = null;
        List<PrincipalModel> rightUsers = null;
        if(right == CollectionRight.READ)
        {
            label = Labels.getLabel("savedquery.send.as_read.to");
            rightUsers = getSavedQueryService().getReadUsersForSavedQuery(query.getSavedQuery());
        }
        AdvanceMenu sendMenu = new AdvanceMenu(label);
        Popup popup = new Popup();
        popup.appendChild((Component)new Textbox());
        sendMenu.setPopup(popup);
        sendMenu.setContext(popup);
        sendMenu.appendChild((Component)popup);
        AdvanceMenupopup sendMenupopup = new AdvanceMenupopup();
        UserGroupModel groupModel = getUserService().getUserGroupForUID("cockpitgroup");
        UsersAssignedListSavedQueryRenderer editor = new UsersAssignedListSavedQueryRenderer(UISessionUtils.getCurrentSession().getTypeService().wrapItems(rightUsers), query, right.toString());
        sendMenupopup.appendChild((Component)editor.createMenuListViewComponent());
        List<PrincipalModel> usersWithRights = rightUsers;
        if(!usersWithRights.isEmpty())
        {
            sendMenupopup.appendChild((Component)new Menuseparator());
        }
        Object object = new Object(this, right, query, usersWithRights);
        ManageMenuitem manageMenuitem = new ManageMenuitem(Labels.getLabel("query.manageUser"), (EventListener)object);
        sendMenupopup.appendChild((Component)manageMenuitem);
        sendMenupopup.appendChild((Component)new Menuseparator());
        buildSavedQueryMenuRecursively((Menupopup)sendMenupopup, (PrincipalModel)groupModel, rightUsers, (EventListener)checkboxSavedQueryClickListener);
        sendMenupopup.setParent((Component)sendMenu);
        return (Menu)sendMenu;
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return getNavigationArea().getPerspective().getDragAndDropWrapperService().getWrapper();
    }


    private UserModel getCurrentUser()
    {
        return UISessionUtils.getCurrentSession().getUser();
    }


    private ObjectCollectionService getObjectCollectionService()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getObjectCollectionService();
    }


    private SavedQueryUserRightsService getSavedQueryService()
    {
        SavedQueryService savedQueryService = UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getSavedQueryService();
        if(savedQueryService instanceof SavedQueryUserRightsService)
        {
            return (SavedQueryUserRightsService)savedQueryService;
        }
        return (SavedQueryUserRightsService)new DummySavedQueryUserRightsServiceImpl(savedQueryService);
    }


    private void setNotification(String captionKey, String messageKey, Object... messageAttrs)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier()
                        .setNotification(new Notification(Labels.getLabel(captionKey), Labels.getLabel(messageKey, messageAttrs)));
    }


    private UserService getUserService()
    {
        if(this.userService == null)
        {
            this.userService = (UserService)SpringUtil.getBean("userService");
        }
        return this.userService;
    }
}
