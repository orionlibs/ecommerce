/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.utils;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SapS4OrderUtil
{
    private UserService userService;
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
    private BaseStoreService baseStoreService;
    private static final Logger LOG = LoggerFactory.getLogger(SapS4OrderUtil.class);


    public String getSoldToParty(UserModel userModel)
    {
        LOG.debug("Method call getSoldToParty");
        if(userModel == null)
        {
            LOG.debug("UserModel is null. Fetch current user");
            userModel = getUserService().getCurrentUser();
        }
        if(userModel instanceof B2BCustomerModel b2bCustomer)
        {
            final B2BUnitModel parent = getB2bUnitService().getParent(b2bCustomer);
            String user = (parent.getUid().contains("_")) ? parent.getUid().split("_")[0] : parent.getUid();
            LOG.debug("User {} is B2B customer", user);
            return user;
        }
        else
        {
            LOG.debug("No B2B customer found , falling back to reference customer");
            return getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().getSapcommon_referenceCustomer();
        }
    }


    public String getCommonTransactionType()
    {
        if(getBaseStoreService().getCurrentBaseStore() != null && getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null)
        {
            LOG.debug("Method call getCommonTransactionType,  TransactionType: {} ", getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().getSapcommon_transactionType());
            return getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().getSapcommon_transactionType();
        }
        return null;
    }


    protected UserService getUserService()
    {
        return userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }


    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
    {
        return b2bUnitService;
    }


    public void setB2bUnitService(B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
    {
        this.b2bUnitService = b2bUnitService;
    }
}