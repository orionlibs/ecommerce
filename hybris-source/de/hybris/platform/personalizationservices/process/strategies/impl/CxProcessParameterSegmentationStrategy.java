package de.hybris.platform.personalizationservices.process.strategies.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class CxProcessParameterSegmentationStrategy extends AbstractCxProcessParameterStrategy
{
    private UserService userService;
    private CxSegmentService cxSegmentService;
    private CxUserSegmentSessionService cxUserSegmentSessionService;
    private CxUserSegmentService cxUserSegmentService;
    private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;
    private CxConfigurationService cxConfigurationService;


    public void store(CxPersonalizationProcessModel process)
    {
        UserModel user = process.getUser();
        if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user))
        {
            Collection<CxUserToSegmentModel> userSegments = this.cxSegmentService.getUserToSegmentForCalculation(user);
            if(!this.userService.isAnonymousUser(user))
            {
                this.cxUserSegmentService.setUserSegments(user, userSegments);
            }
            Collection<UserToSegmentData> userSegmentsData = this.cxUserSegmentConversionHelper.convertToData(userSegments);
            getProcessParameterHelper().setProcessParameter((BusinessProcessModel)process, "userToSegmentsProcessParameter", userSegmentsData);
        }
    }


    public void load(CxPersonalizationProcessModel process)
    {
        consumeProcessParameter(process, "userToSegmentsProcessParameter", uts -> loadUserSegments(process.getUser(), uts));
    }


    protected void loadUserSegments(UserModel user, Collection<UserToSegmentData> segmentation)
    {
        this.cxUserSegmentSessionService.setUserSegmentsInSession(user, segmentation);
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


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxUserSegmentSessionService getCxUserSegmentSessionService()
    {
        return this.cxUserSegmentSessionService;
    }


    @Required
    public void setCxUserSegmentSessionService(CxUserSegmentSessionService cxUserSegmentSessionService)
    {
        this.cxUserSegmentSessionService = cxUserSegmentSessionService;
    }


    protected CxUserSegmentConversionHelper getCxUserSegmentConversionHelper()
    {
        return this.cxUserSegmentConversionHelper;
    }


    @Required
    public void setCxUserSegmentConversionHelper(CxUserSegmentConversionHelper cxUserSegmentsConversionHelper)
    {
        this.cxUserSegmentConversionHelper = cxUserSegmentsConversionHelper;
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
}
