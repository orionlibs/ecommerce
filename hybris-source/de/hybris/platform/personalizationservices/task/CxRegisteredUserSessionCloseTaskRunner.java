package de.hybris.platform.personalizationservices.task;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CxRegisteredUserSessionCloseTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CxRegisteredUserSessionCloseTaskRunner.class);
    private ModelService modelService;
    private UserService userService;
    private BaseSiteService baseSiteService;
    private CxUserSegmentService cxUserSegmentService;
    private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;


    public void run(TaskService taskService, TaskModel task)
    {
        Map<String, Object> contextMap = (Map<String, Object>)task.getContext();
        String userUid = (String)contextMap.get("user");
        Collection<UserToSegmentData> userSegments = (Collection<UserToSegmentData>)contextMap.get("segments");
        String baseSiteId = (String)contextMap.get("baseSite");
        if(StringUtils.isNotEmpty(userUid) && CollectionUtils.isNotEmpty(userSegments))
        {
            UserModel user = this.userService.getUserForUID(userUid);
            BaseSiteModel baseSite = this.baseSiteService.getBaseSiteForUID(baseSiteId);
            Collection<CxUserToSegmentModel> userSegmentModels = this.cxUserSegmentConversionHelper.convertToModel(user, userSegments, baseSite);
            this.cxUserSegmentService.setUserSegments(user, baseSite, userSegmentModels);
            LOG.debug("{} segments was assigned to user : {}", Integer.valueOf(userSegments.size()), userUid);
        }
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
        LOG.error("Assigning segments to user failed ", error);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CxUserSegmentService getCxUserSegmentService()
    {
        return this.cxUserSegmentService;
    }


    @Required
    public void setCxUserSegmentService(CxUserSegmentService cxUserSegmentService)
    {
        this.cxUserSegmentService = cxUserSegmentService;
    }


    protected CxUserSegmentConversionHelper getCxUserSegmentConversionHelper()
    {
        return this.cxUserSegmentConversionHelper;
    }


    @Required
    public void setCxUserSegmentConversionHelper(CxUserSegmentConversionHelper cxUserSegmentConversionHelper)
    {
        this.cxUserSegmentConversionHelper = cxUserSegmentConversionHelper;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
