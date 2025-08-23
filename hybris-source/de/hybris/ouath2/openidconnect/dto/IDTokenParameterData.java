package de.hybris.ouath2.openidconnect.dto;

import java.io.Serializable;

public class IDTokenParameterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String kid;
    private String securityKeystoreLocation;
    private String securityKeystorePassword;
    private String algorithm;
    private int idTokenValiditySeconds;


    public void setKid(String kid)
    {
        this.kid = kid;
    }


    public String getKid()
    {
        return this.kid;
    }


    public void setSecurityKeystoreLocation(String securityKeystoreLocation)
    {
        this.securityKeystoreLocation = securityKeystoreLocation;
    }


    public String getSecurityKeystoreLocation()
    {
        return this.securityKeystoreLocation;
    }


    public void setSecurityKeystorePassword(String securityKeystorePassword)
    {
        this.securityKeystorePassword = securityKeystorePassword;
    }


    public String getSecurityKeystorePassword()
    {
        return this.securityKeystorePassword;
    }


    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }


    public String getAlgorithm()
    {
        return this.algorithm;
    }


    public void setIdTokenValiditySeconds(int idTokenValiditySeconds)
    {
        this.idTokenValiditySeconds = idTokenValiditySeconds;
    }


    public int getIdTokenValiditySeconds()
    {
        return this.idTokenValiditySeconds;
    }
}
