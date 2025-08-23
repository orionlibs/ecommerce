package de.hybris.platform.customerreview.setup;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.customerreview.constants.GeneratedCustomerReviewConstants;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "customerreview")
public class CustomerReviewSystemSetup
{
    private static final Logger LOG = Logger.getLogger(CustomerReviewSystemSetup.class);
    private static final String SEARCH_RESTRICTION_FRONT_END_REVIEWS = "FrontEnd_Reviews";
    private static final String SEARCH_RESTRICTION_CREATE_DEFAULT_KEY = "customerreview.searchrestrictions.create";
    private ModelService modelService;
    private TypeService typeService;
    private UserService userService;
    private SearchRestrictionService searchRestrictionService;
    private ConfigurationService configurationService;
    private String searchRestrictionCreateKey = "customerreview.searchrestrictions.create";


    public void setSearchRestrictionCreateKey(String searchRestrictionCreateKey)
    {
        this.searchRestrictionCreateKey = searchRestrictionCreateKey;
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createSearchRestrictions(SystemSetupContext context)
    {
        ComposedTypeModel restrictedType = getTypeService().getComposedTypeForClass(CustomerReviewModel.class);
        UserGroupModel userGroupModel = getUserService().getUserGroupForUID(Constants.USER.CUSTOMER_USERGROUP);
        if(getConfigurationService().getConfiguration().getBoolean(this.searchRestrictionCreateKey))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Creating essential data for customerreview");
            }
            removeRestrictions(restrictedType, userGroupModel);
            EnumerationValueModel enumerationValueModel = getTypeService().getEnumerationValue(GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEWAPPROVALTYPE, GeneratedCustomerReviewConstants.Enumerations.CustomerReviewApprovalType.APPROVED);
            SearchRestrictionModel searchRestriction = (SearchRestrictionModel)getModelService().create(SearchRestrictionModel.class);
            searchRestriction.setCode("FrontEnd_Reviews");
            searchRestriction.setActive(Boolean.TRUE);
            searchRestriction.setQuery("{approvalStatus}=" + enumerationValueModel.getPk());
            searchRestriction.setRestrictedType(restrictedType);
            searchRestriction.setPrincipal((PrincipalModel)userGroupModel);
            searchRestriction.setGenerate(Boolean.TRUE);
            getModelService().save(searchRestriction);
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Creating essential data for customerreview skipped, due to '" + this.searchRestrictionCreateKey + "' is not setup");
            }
            removeRestrictions(restrictedType, userGroupModel);
        }
    }


    private void removeRestrictions(ComposedTypeModel restrictedType, UserGroupModel userGroupModel)
    {
        Collection<SearchRestrictionModel> restrictions = getSearchRestrictionService().getSearchRestrictions((PrincipalModel)userGroupModel, true,
                        Collections.singleton(restrictedType));
        for(SearchRestrictionModel r : restrictions)
        {
            if(r.getCode().equals("FrontEnd_Reviews"))
            {
                this.modelService.remove(r);
            }
        }
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


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
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


    protected SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
