package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

public class CMSDefaultPageInterceptor implements PrepareInterceptor
{
    protected CMSPageDao cmsPageDao;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ContentPageModel)
        {
            ContentPageModel page = (ContentPageModel)model;
            String label = page.getLabel();
            if(!StringUtils.isEmpty(label))
            {
                Set<AbstractPageModel> related = new HashSet<>(this.cmsPageDao.findPagesByLabel(label,
                                Collections.singletonList(page.getCatalogVersion())));
                related.remove(page);
                boolean isDefault = BooleanUtils.toBoolean(page.getDefaultPage());
                boolean otherDefaulExists = defaultPageExists(related);
                if(isDefault && otherDefaulExists)
                {
                    throw new InterceptorException("Page [" + page.getUid() + "] is marked as default and there is another page with the same label marked as default as well.");
                }
                if(!isDefault && !otherDefaulExists)
                {
                    throw new InterceptorException("Page [" + page.getUid() + "] must be marked as default because there is no other page with the same label marked as default.");
                }
            }
        }
    }


    protected boolean defaultPageExists(Set<AbstractPageModel> pages)
    {
        for(AbstractPageModel page : pages)
        {
            if(BooleanUtils.toBoolean(page.getDefaultPage()))
            {
                return true;
            }
        }
        return false;
    }


    public void setCmsPageDao(CMSPageDao cmsPageDao)
    {
        this.cmsPageDao = cmsPageDao;
    }
}
