package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

public class ComponentsHelper
{
    public static final String PRINCIPAL = "principal";
    private static final Logger LOG = LoggerFactory.getLogger(ComponentsHelper.class);
    private static final String ALL_GROUP_MEMEBERS = "general.all.group.members";


    public static void createMenuWithUsers(Menupopup parentMenupopup, PrincipalModel parentPrincipal, Collection<PrincipalModel> checkedUsers, EventListener listener)
    {
        if(parentPrincipal instanceof UserGroupModel)
        {
            UserGroupModel model = (UserGroupModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            Menu groupMenu = new Menu(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(modelObject));
            Menupopup groupMenupopup = new Menupopup();
            Menuitem allgroupMembers = new Menuitem(Labels.getLabel("general.all.group.members"));
            allgroupMembers.setCheckmark(true);
            allgroupMembers.setChecked(checkedUsers.contains(model));
            allgroupMembers.setAttribute("principal", model);
            allgroupMembers.addEventListener("onClick", listener);
            allgroupMembers.setParent((Component)groupMenupopup);
            Menuseparator menuseparator = new Menuseparator();
            menuseparator.setParent((Component)groupMenupopup);
            parentMenupopup.addEventListener("onOpen", (EventListener)new Object(groupMenupopup, model, checkedUsers, listener));
            groupMenupopup.setParent((Component)groupMenu);
            groupMenu.setParent((Component)parentMenupopup);
        }
        else if(parentPrincipal instanceof UserModel)
        {
            UserModel model = (UserModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            Menuitem userMenuitem = new Menuitem(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(modelObject));
            userMenuitem.setCheckmark(true);
            userMenuitem.setChecked(checkedUsers.contains(model));
            userMenuitem.setAttribute("principal", model);
            userMenuitem.addEventListener("onClick", listener);
            userMenuitem.setParent((Component)parentMenupopup);
        }
    }


    public static void displayNotification(String captionKey, String messageKey, Object... messageAttrs)
    {
        BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(basePerspective.getNotifier() != null)
        {
            basePerspective.getNotifier()
                            .setNotification(new Notification(Labels.getLabel(captionKey), Labels.getLabel(messageKey, messageAttrs)));
        }
    }


    public static void displayCustomNotification(String title, String messageKey, Object... messageAttrs)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier()
                        .setNotification(new Notification(title, Labels.getLabel(messageKey, messageAttrs)));
    }


    public static Popup createSimpleRenamePopup(String defaultNameValue, Component parent, EventListener onOkListener)
    {
        Popup popup = new Popup();
        Textbox textbox = new Textbox(defaultNameValue);
        textbox.setParent((Component)popup);
        textbox.addEventListener("onOK", onOkListener);
        textbox.addEventListener("onCancel", (EventListener)new Object(popup));
        textbox.addEventListener("onFocusLater", (EventListener)new Object(textbox));
        popup.setParent(parent);
        popup.open(parent, "end_before");
        Events.echoEvent("onFocusLater", (Component)textbox, null);
        return popup;
    }


    public static void displayConfirmationPopup(String title, String message, EventListener onYesListener)
    {
        String displayTitle = title.isEmpty() ? Labels.getLabel("general.confirmation") : title;
        String displayMessage = message.isEmpty() ? Labels.getLabel("general.confirm") : message;
        try
        {
            Messagebox.show(displayMessage, displayTitle, 48, "z-msgbox z-msgbox-question", onYesListener);
        }
        catch(InterruptedException e)
        {
            LOG.warn(e.getMessage(), e);
        }
    }


    public static void displayConfirmationPopup(EventListener onYesListener)
    {
        displayConfirmationPopup(Labels.getLabel("general.confirmation"), Labels.getLabel("general.confirm"), onYesListener);
    }
}
