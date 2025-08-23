package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.forms.login.LoginForm;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.UIRole;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.A;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.LayoutRegion;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class LeftSectionHeaderComponent extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(LeftSectionHeaderComponent.class);
    protected Component dataLanguageEntry;
    protected Component userRoleEntry;
    protected Component logoutEntry;
    protected static final String COCKPIT_ID_MENU_LABEL = "MenuLabel_";
    protected static final String COCKPIT_ID_LOGOUT_MEONUITEM = "LogoutMenuitem_";


    public LeftSectionHeaderComponent()
    {
        setSclass("section_header");
        setHeight("63px");
        setWidth("100%");
        Hbox hbox = new Hbox();
        hbox.setParent((Component)this);
        hbox.setWidth("100%");
        hbox.setWidths("22px, none, 100%, 22px");
        hbox.setStyle("padding-top: 5px;");
        String homeRoot = getWebRoot("mcc");
        if(StringUtils.isNotBlank(homeRoot))
        {
            Label sepLabel = new Label("|");
            sepLabel.setParent((Component)hbox);
            sepLabel.setSclass("menu_home_sep");
        }
        else
        {
            hbox.appendChild((Component)new Div());
        }
        Div div = new Div();
        Div cnt = new Div();
        cnt.appendChild((Component)div);
        hbox.appendChild((Component)cnt);
        div.setSclass("menulabel");
        Label label = new Label(Labels.getLabel("general.menu"));
        label.setParent((Component)div);
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "MenuLabel_";
            UITools.applyTestID((Component)label, "MenuLabel_");
        }
        Image image = new Image("/cockpit/images/menu_arrow_right.gif");
        image.setParent((Component)div);
        image.setSclass("menu_label_img");
        Menupopup menupopup = new Menupopup();
        menupopup.setParent((Component)div);
        div.addEventListener("onClick", (EventListener)new Object(this, menupopup, div));
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(currentPerspective instanceof BaseUICockpitPerspective)
        {
            LayoutRegion navigationAreaComponent = ((BaseUICockpitPerspective)currentPerspective).getNavigationAreaComponent();
            if(navigationAreaComponent != null)
            {
                navigationAreaComponent.setAttribute("main_menu_popup", menupopup);
            }
        }
        Menu menu = new Menu(Labels.getLabel("menu.datalanguage"));
        this.dataLanguageEntry = (Component)menu;
        menu.setParent((Component)menupopup);
        Menupopup menupopup2 = new Menupopup();
        menupopup2.setParent((Component)menu);
        menupopup2.addEventListener("onOpen", (EventListener)new Object(this, menupopup2));
        Menu menu2 = new Menu(Labels.getLabel("menu.usergroup"));
        this.userRoleEntry = (Component)menu2;
        menu2.setParent((Component)menupopup);
        Menupopup menupopup3 = new Menupopup();
        menupopup3.setParent((Component)menu2);
        menupopup3.addEventListener("onOpen", (EventListener)new Object(this, menupopup3));
        Menu userSettingsMenu = new Menu(Labels.getLabel("menu.usersettings"));
        userSettingsMenu.setParent((Component)menupopup);
        Menupopup userSettingsMenuPopup = new Menupopup();
        userSettingsMenuPopup.setParent((Component)userSettingsMenu);
        userSettingsMenuPopup.addEventListener("onOpen", (EventListener)new Object(this, userSettingsMenuPopup));
        Menuitem menuitem = new Menuitem(Labels.getLabel("general.logout"));
        this.logoutEntry = (Component)menuitem;
        menuitem.setParent((Component)menupopup);
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "LogoutMenuitem_";
            UITools.applyTestID((Component)menuitem, "LogoutMenuitem_");
        }
        menuitem.addEventListener("onClick", (EventListener)new Object(this));
        if(StringUtils.isNotBlank(homeRoot))
        {
            Div homeDiv = new Div();
            hbox.insertBefore((Component)homeDiv, hbox.getFirstChild());
            homeDiv.setSclass("cockpitHomeDiv");
            A homeBtn = new A();
            homeBtn.setParent((Component)homeDiv);
            homeBtn.setSclass("ucockpit_link");
            homeBtn.setDynamicProperty("href", homeRoot);
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Extension 'mcc' not available. Not rendering link.");
            }
            hbox.insertBefore((Component)new Div(), (Component)div);
        }
        Div hyLogoDiv = new Div();
        hyLogoDiv.setParent((Component)hbox);
        hyLogoDiv.setSclass("cockpitYLogoDiv");
    }


    protected String getWebRoot(String extName)
    {
        String webRoot = null;
        if(Utilities.getInstalledWebModules().containsKey("mcc"))
        {
            Object nativeResponse = Executions.getCurrent().getNativeResponse();
            if(nativeResponse instanceof HttpServletResponse)
            {
                webRoot = ((HttpServletResponse)nativeResponse).encodeURL((String)Utilities.getInstalledWebModules().get(extName) + "/");
            }
            else
            {
                LOG.warn("Could not get web root for extension '" + extName + "'. Reason: Unexpected Servlet response type");
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Could not get web root for extension 'mcc'. Reason: Extension not available.");
        }
        return webRoot;
    }


    protected String getLanguageLabel(LanguageModel language)
    {
        String iso = language.getIsocode();
        if(language.getName() == null)
        {
            return iso;
        }
        return language.getName() + " [" + language.getName() + "]";
    }


    protected void createLanguageMenuItems(Menupopup menuPopup, boolean open)
    {
        if(open)
        {
            menuPopup.getChildren().clear();
            for(LanguageModel lang : UISessionUtils.getCurrentSession().getSystemService().getAllReadableLanguages())
            {
                Menuitem mitem = new Menuitem(getLanguageLabel(lang));
                mitem.setValue(lang.getIsocode());
                mitem.setCheckmark(true);
                if(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso().equals(lang.getIsocode()))
                {
                    mitem.setChecked(true);
                }
                mitem.setParent((Component)menuPopup);
                mitem.addEventListener("onClick", (EventListener)new Object(this, mitem));
            }
        }
        else
        {
            menuPopup.invalidate();
        }
    }


    protected void createUserGroupMenuItems(Menupopup menupopup, boolean open)
    {
        if(open)
        {
            menupopup.getChildren().clear();
            UIConfigurationService uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
            List<UIRole> fallbackRoleList = new ArrayList<>();
            fallbackRoleList.add(uiConfigurationService.getFallbackRole());
            for(UIRole role : (uiConfigurationService.getPossibleRoles().size() > 0) ?
                            uiConfigurationService.getPossibleRoles() : fallbackRoleList)
            {
                String name = role.getName();
                Menuitem menuitem = new Menuitem(name);
                menuitem.setCheckmark(true);
                if(uiConfigurationService.getSessionRole().equals(role))
                {
                    menuitem.setChecked(true);
                }
                menuitem.setParent((Component)menupopup);
                menuitem.addEventListener("onClick", (EventListener)new Object(this, uiConfigurationService));
            }
        }
        else
        {
            menupopup.invalidate();
        }
    }


    protected void createUserSettingsMenuItems(Menupopup menupopup, boolean open)
    {
        if(open)
        {
            menupopup.getChildren().clear();
            if(UISessionUtils.getCurrentSession().getUiAccessRightService()
                            .canRead(UISessionUtils.getCurrentSession().getUser(), "cockpit.personalizedconfiguration"))
            {
                Menuitem menuitem = new Menuitem(Labels.getLabel("menu.usersettings.resetuiconfig"));
                menuitem.setParent((Component)menupopup);
                menuitem.addEventListener("onClick", (EventListener)new Object(this));
            }
        }
        else
        {
            menupopup.invalidate();
        }
    }


    protected LoginForm getLoginForm()
    {
        return (LoginForm)SpringUtil.getBean("LoginForm");
    }
}
