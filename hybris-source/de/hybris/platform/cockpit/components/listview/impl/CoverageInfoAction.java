package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.coverage.CoverageCalculationService;
import de.hybris.platform.validation.coverage.CoverageInfo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class CoverageInfoAction extends AbstractListViewAction
{
    private static final Logger LOG = LoggerFactory.getLogger(CoverageInfoAction.class);
    private static final String COVERAGE_INFO_REQUEST_CACHE = "coverageInfoRequestCache";
    private static final String COVERAGE_INDEX = "coverageIndex";
    private static final String COVERAGE_INFO = "coverageInfo";
    private CoverageCalculationService coverageCalculationService;


    protected Integer getCoveragePercentageValue(CoverageInfo covInfo)
    {
        return (covInfo == null) ? null : Integer.valueOf((int)Math.round(covInfo.getCoverageIndex() * 100.0D));
    }


    public String getImageURI(ListViewAction.Context context)
    {
        Object object = context.getMap().get("coverageIndex");
        if(object instanceof Integer)
        {
            int percentage = ((Integer)object).intValue();
            if(percentage == 100)
            {
                return "/cockpit/images/icon_status_coverage4_full.png";
            }
            if(percentage >= 67)
            {
                return "/cockpit/images/icon_status_coverage3.png";
            }
            if(percentage >= 34)
            {
                return "/cockpit/images/icon_status_coverage2.png";
            }
            if(percentage >= 1)
            {
                return "/cockpit/images/icon_status_coverage1.png";
            }
            return "/cockpit/images/icon_status_coverage0_empty.png";
        }
        return "/cockpit/images/icon_status_coverage_none.png";
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        Object object = context.getMap().get("coverageIndex");
        String tooltip = (object == null) ? Labels.getLabel("cockpit.coverage.notAvailable") : (Labels.getLabel("cockpit.coverage.label") + ": " + Labels.getLabel("cockpit.coverage.label") + "%");
        Object covInfo = context.getMap().get("coverageInfo");
        if(covInfo instanceof CoverageInfo)
        {
            List<CoverageInfo.CoveragePropertyInfoMessage> propertyInfoMessages = ((CoverageInfo)covInfo).getPropertyInfoMessages();
            for(CoverageInfo.CoveragePropertyInfoMessage coveragePropertyInfoMessage : propertyInfoMessages)
            {
                tooltip = tooltip + ", " + tooltip;
            }
        }
        return tooltip;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
        String templateCode = null;
        try
        {
            templateCode = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(context.getItem()).getCode();
        }
        catch(Exception e)
        {
            LOG.error("Could not get template code for typed object, reason was: ", e);
        }
        String finalTemplateCode = templateCode;
        CoverageInfo covInfo = null;
        ItemModel item = (ItemModel)context.getItem().getObject();
        if(item != null && item.getPk() != null)
        {
            covInfo = (CoverageInfo)(new Object(this, "coverageInfoRequestCache", item, finalTemplateCode)).get(item.getPk().getLongValueAsString());
        }
        else
        {
            covInfo = this.coverageCalculationService.calculate(item, finalTemplateCode, null);
        }
        if(covInfo != null)
        {
            context.getMap().put("coverageIndex", getCoveragePercentageValue(covInfo));
            context.getMap().put("coverageInfo", covInfo);
        }
    }


    @Required
    public void setCoverageCalculationService(CoverageCalculationService coverageCalculationService)
    {
        this.coverageCalculationService = coverageCalculationService;
    }
}
