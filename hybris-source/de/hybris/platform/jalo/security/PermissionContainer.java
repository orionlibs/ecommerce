package de.hybris.platform.jalo.security;

import de.hybris.platform.core.PK;
import java.io.Serializable;

public class PermissionContainer implements Serializable
{
    private final PK principalPK;
    private final PK rightPK;
    private final boolean negative;


    public PermissionContainer(PK principalPK, PK rightPK, boolean negative)
    {
        if(principalPK == null || rightPK == null)
        {
            throw new IllegalArgumentException("Neither principalPK nor rightPK can be null.");
        }
        this.principalPK = principalPK;
        this.rightPK = rightPK;
        this.negative = negative;
    }


    public PK getPrincipalPK()
    {
        return this.principalPK;
    }


    public PK getRightPK()
    {
        return this.rightPK;
    }


    public boolean isNegative()
    {
        return this.negative;
    }
}
