package de.hybris.platform.personalizationintegration.segment.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationintegration.segment.UserSegmentsProvider;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class UserTypeSegmentsProvider implements UserSegmentsProvider
{
    public static final String ANONYMOUS_USER = "USER_ANONYMOUS";
    public static final String REGISTERED_USER = "USER_REGISTERED";
    private static final BigDecimal AFFINITY = BigDecimal.TEN;
    private UserService userService;
    private CxConfigurationService cxConfigurationService;


    public List<SegmentMappingData> getUserSegments(UserModel user)
    {
        if(this.userService.isAnonymousUser(user))
        {
            return getSegmentMapping("USER_ANONYMOUS");
        }
        return getSegmentMapping("USER_REGISTERED");
    }


    private List<SegmentMappingData> getSegmentMapping(String name)
    {
        SegmentMappingData data = new SegmentMappingData();
        data.setCode(name);
        data.setAffinity(getAffinity());
        return Collections.singletonList(data);
    }


    private BigDecimal getAffinity()
    {
        return this.cxConfigurationService.getMinAffinity().add(AFFINITY);
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }
}
