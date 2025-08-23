package de.hybris.platform.search.restriction.session;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.validator.GenericValidator;

public class SessionSearchRestriction
{
    private final String code;
    private final String query;
    private final ComposedTypeModel restrictedType;


    public SessionSearchRestriction(String code, String query, ComposedTypeModel restrictedType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("restrictedType", restrictedType);
        if(GenericValidator.isBlankOrNull(query))
        {
            throw new IllegalArgumentException("query is required, null or empty string given");
        }
        if(GenericValidator.isBlankOrNull(code))
        {
            throw new IllegalArgumentException("code is required, null given");
        }
        this.code = code;
        this.query = query;
        this.restrictedType = restrictedType;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getQuery()
    {
        return this.query;
    }


    public ComposedTypeModel getRestrictedType()
    {
        return this.restrictedType;
    }


    public boolean equals(Object another)
    {
        return (super.equals(another) || (another instanceof SessionSearchRestriction &&
                        getCode().equals(((SessionSearchRestriction)another)
                                        .getCode()) && getRestrictedType()
                        .equals(((SessionSearchRestriction)another).getRestrictedType())));
    }


    public int hashCode()
    {
        return (new HashCodeBuilder()).append(getCode()).append(getRestrictedType()).toHashCode();
    }
}
