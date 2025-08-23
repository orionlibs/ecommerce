package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CMSTimeRestrictionEvaluator implements CMSRestrictionEvaluator<CMSTimeRestrictionModel>
{
    private static final Logger LOG = Logger.getLogger(CMSTimeRestrictionEvaluator.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Resource(name = "sessionService")
    private SessionService sessionService;
    private TimeService timeService;


    public boolean evaluate(CMSTimeRestrictionModel timeRestriction, RestrictionData context)
    {
        Date now = (Date)getSessionService().getAttribute("previewTime");
        boolean previewDate = true;
        if(now == null)
        {
            previewDate = false;
            now = Boolean.TRUE.equals(timeRestriction.getUseStoreTimeZone()) ? getTimeService().getCurrentTime() : new Date();
        }
        Date from = timeRestriction.getActiveFrom();
        Date until = timeRestriction.getActiveUntil();
        if(LOG.isDebugEnabled())
        {
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            if(previewDate)
            {
                LOG.debug("Using preview time: " + dateformat.format(now));
            }
            LOG.debug("Current time: " + dateformat.format(now));
            LOG.debug("Valid from: " + ((from != null) ? dateformat.format(from) : "null"));
            LOG.debug("Valid until: " + ((until != null) ? dateformat.format(until) : "null"));
        }
        boolean after = true;
        if(from != null)
        {
            after = now.after(from);
        }
        boolean before = true;
        if(until != null)
        {
            before = now.before(until);
        }
        return (after && before);
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
