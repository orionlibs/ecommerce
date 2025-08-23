/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.registration.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.b2b.dao.B2BRegistrationDao;
import de.hybris.platform.b2bacceleratorservices.registration.B2BRegistrationService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link B2BRegistrationService}
 */
public class DefaultB2BRegistrationService implements B2BRegistrationService
{
    private B2BRegistrationDao registrationDao;
    private EmailService emailService;


    /**
     * @param registrationDao
     *           the registrationDao to set
     */
    @Required
    public void setRegistrationDao(final B2BRegistrationDao registrationDao)
    {
        this.registrationDao = registrationDao;
    }


    /**
     * @param emailService
     *           the emailService to set
     */
    @Required
    public void setEmailService(final EmailService emailService)
    {
        this.emailService = emailService;
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.b2bacceleratorservices.registration.B2BRegistrationService#getEmployeesInUserGroup(java.lang.String)
     */
    @Override
    public List<EmployeeModel> getEmployeesInUserGroup(final String userGroup)
    {
        return registrationDao.getEmployeesInUserGroup(userGroup);
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.b2bacceleratorservices.registration.B2BRegistrationService#getEmailAddressesOfEmployees(java.util.List)
     */
    @Override
    public List<EmailAddressModel> getEmailAddressesOfEmployees(final List<EmployeeModel> employees)
    {
        final List<EmailAddressModel> emails = new ArrayList<>();
        for(final EmployeeModel employee : employees)
        {
            for(final AddressModel address : Lists.newArrayList(employee.getAddresses()))
            {
                if(BooleanUtils.isTrue(address.getContactAddress()))
                {
                    addEmployeeEmail(employee, address, emails);
                    break;
                }
            }
        }
        return emails;
    }


    private void addEmployeeEmail(final EmployeeModel employee, final AddressModel address, final List<EmailAddressModel> emails)
    {
        if(StringUtils.isNotBlank(address.getEmail()))
        {
            final EmailAddressModel emailAddress = emailService.getOrCreateEmailAddressForEmail(address.getEmail(),
                            employee.getName());
            emails.add(emailAddress);
        }
    }
}
