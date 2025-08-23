package de.hybris.platform.cmscockpit.components.welcomepage;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.services.SavedValuesService;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.user.UserModel;
import java.text.DateFormat;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

public class PageBox extends Div
{
    private static final long serialVersionUID = -8093036653423296759L;
    private static final Logger LOG = Logger.getLogger(PageBox.class);


    public PageBox(AbstractPageModel page)
    {
        setStyle("float: left; position: relative;");
        setWidth("170px");
        setHeight("160px");
        UITools.addBusyListener((Component)this, "onClick", (EventListener)new Object(this, page), null, null);
        Div innerDiv = new Div();
        innerDiv.setStyle("position: relative; top: 5px; left: 5px;");
        innerDiv.setWidth("150px");
        innerDiv.setHeight("156px");
        innerDiv.setSclass("itemBox");
        appendChild((Component)innerDiv);
        Div container = new Div();
        container.setStyle("margin: 5px; position: relative;");
        innerDiv.appendChild((Component)container);
        String imageUrl = null;
        if(page.getPreviewImage() != null)
        {
            imageUrl = UITools.getAdjustedUrl(page.getPreviewImage().getDownloadURL());
        }
        if(imageUrl == null)
        {
            imageUrl = "/cmscockpit/images/" + page.getClass().getSimpleName() + "_nopreview.gif";
        }
        Div imgContainer = new Div();
        imgContainer.setStyle("width: 70px; height: 75px; overflow: hidden; border: 1px solid #999; position: relative; background: white;");
        Image img = new Image(imageUrl);
        img.setWidth("70px");
        imgContainer.appendChild((Component)img);
        container.appendChild((Component)imgContainer);
        Div mnemDiv = new Div();
        mnemDiv.setStyle("position: absolute; bottom: 0; right: 0;");
        mnemDiv.setSclass("welcome_page_mnem");
        imgContainer.appendChild((Component)mnemDiv);
        mnemDiv.appendChild((Component)new Label(page.getCatalogVersion().getMnemonic()));
        Div actionDiv = new Div();
        actionDiv.setStyle("position: absolute; top: 0; right: 0; width: 20px; height: 60px;");
        actionDiv.setSclass("welcome_page_action");
        container.appendChild((Component)actionDiv);
        Div nameDiv = new Div();
        nameDiv.setStyle("position: absolute; top: 80px; left: 0; white-space:nowrap; right: 0; overflow: hidden;");
        nameDiv.setSclass("welcome_page_name");
        Label name = new Label(page.getName());
        name.setStyle("font-weight:bold; font-size:11px");
        nameDiv.appendChild((Component)name);
        nameDiv.setTooltiptext(page.getName());
        container.appendChild((Component)nameDiv);
        String catalogVersionText = getLabelService().getObjectTextLabel(getTypeService().wrapItem(page.getCatalogVersion()));
        Label catalogVersionLabel = new Label(catalogVersionText);
        catalogVersionLabel.setStyle("position:relative;top:37px;font-size:10px;line-height:11px");
        Div catalogVersionContainer = new Div();
        catalogVersionContainer.setStyle("text-align:left");
        catalogVersionContainer.appendChild((Component)catalogVersionLabel);
        container.appendChild((Component)catalogVersionContainer);
        Div dateDiv = new Div();
        dateDiv.setStyle("position: absolute; top: 93px; left: 0; white-space:nowrap; right: 0; overflow: hidden;");
        dateDiv.setSclass("welcome_page_date");
        String dateStr = DateFormat.getDateTimeInstance(3, 3, UISessionUtils.getCurrentSession().getLocale()).format(page.getModifiedtime());
        dateDiv.appendChild((Component)new Label(dateStr));
        dateDiv.setTooltiptext(dateStr);
        container.appendChild((Component)dateDiv);
        SavedValuesService savedValuesService = (SavedValuesService)SpringUtil.getBean("savedValuesService");
        if(savedValuesService != null)
        {
            UserModel lastModifyingUser = savedValuesService.getLastModifyingUser(UISessionUtils.getCurrentSession().getTypeService().wrapItem(page));
            if(lastModifyingUser != null)
            {
                Div ownerDiv = new Div();
                ownerDiv.setStyle("position: absolute; top: 106px; white-space:nowrap; left: 0; right: 0; overflow: hidden;");
                ownerDiv.setSclass("welcome_page_user");
                ownerDiv.appendChild((Component)new Label(lastModifyingUser.getName()));
                container.appendChild((Component)ownerDiv);
            }
        }
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
    }


    protected LabelService getLabelService()
    {
        return UISessionUtils.getCurrentSession().getLabelService();
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
