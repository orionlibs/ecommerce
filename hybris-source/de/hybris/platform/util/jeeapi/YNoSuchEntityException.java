package de.hybris.platform.util.jeeapi;

import de.hybris.platform.core.PK;

public class YNoSuchEntityException extends YEJBException
{
    private final PK pk;


    public YNoSuchEntityException(String s, PK pk)
    {
        super(s);
        this.pk = pk;
    }


    public PK getPk()
    {
        return this.pk;
    }
}
