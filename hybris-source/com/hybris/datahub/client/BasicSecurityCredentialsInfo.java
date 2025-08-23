package com.hybris.datahub.client;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class BasicSecurityCredentialsInfo extends SecurityCredentialsInfo
{
    private final String username;
    private final String password;


    public BasicSecurityCredentialsInfo(String username, String password)
    {
        this.username = username;
        this.password = password;
    }


    public void applyTo(ClientConfiguration clientConfiguration)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.username), "Username cannot be empty or null");
        Preconditions.checkArgument(!Objects.isNull(this.password), "Password cannot be null");
        HttpAuthenticationFeature securityClientFilter = HttpAuthenticationFeature.basic(this.username, this.password);
        clientConfiguration.setSecurityClientFilter(securityClientFilter);
    }


    public boolean isValid()
    {
        return (!StringUtils.isEmpty(this.username) && !StringUtils.isEmpty(this.password));
    }


    public String toString()
    {
        return "BasicSecurityCredentialsInfo{username='" + this.username + "'}";
    }
}
