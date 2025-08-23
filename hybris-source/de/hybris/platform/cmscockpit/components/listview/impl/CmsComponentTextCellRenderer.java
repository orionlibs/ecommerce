package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTextCellRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class CmsComponentTextCellRenderer extends DefaultTextCellRenderer
{
    private static final Logger LOG = Logger.getLogger(CmsComponentTextCellRenderer.class);
    private CmsCockpitService cmsService = null;


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        Div cmsStatusWrapper = new Div();
        parent.appendChild((Component)cmsStatusWrapper);
        StringBuffer sclass = new StringBuffer(50);
        try
        {
            TypedObject comp = (TypedObject)model.getListComponentModel().getValueAt(rowIndex);
            if(getCmsCockpitService().isRestricted(comp))
            {
                sclass.append("restrictedComponent");
            }
            if(getCmsCockpitService().isPartOfTemplate(comp))
            {
                sclass.append(" partOfTemplate");
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        cmsStatusWrapper.setSclass(sclass.toString().trim());
        super.render(model, colIndex, rowIndex, (Component)cmsStatusWrapper);
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsService == null)
        {
            this.cmsService = (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
        }
        return this.cmsService;
    }
}
