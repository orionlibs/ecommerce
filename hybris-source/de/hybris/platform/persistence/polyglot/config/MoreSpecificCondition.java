package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.model.Identity;
import java.util.Objects;
import java.util.StringJoiner;

public class MoreSpecificCondition
{
    private final Identity sourceIdentity;
    private final String qualifier;
    private final Identity targetIdentity;


    public MoreSpecificCondition(Identity sourceIdentity, String qualifier, Identity targetIdentity)
    {
        this.sourceIdentity = sourceIdentity;
        this.qualifier = qualifier;
        this.targetIdentity = targetIdentity;
    }


    public Identity getSourceIdentity()
    {
        return this.sourceIdentity;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public Identity getTargetIdentity()
    {
        return this.targetIdentity;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        MoreSpecificCondition that = (MoreSpecificCondition)o;
        return (Objects.equals(this.sourceIdentity, that.sourceIdentity) &&
                        Objects.equals(this.qualifier, that.qualifier) &&
                        Objects.equals(this.targetIdentity, that.targetIdentity));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.sourceIdentity, this.qualifier, this.targetIdentity});
    }


    public String toString()
    {
        return (new StringJoiner(", ", MoreSpecificCondition.class.getSimpleName() + "[", "]"))
                        .add("sourceIdentity=" + this.sourceIdentity)
                        .add("qualifier='" + this.qualifier + "'")
                        .add("targetIdentity=" + this.targetIdentity)
                        .toString();
    }
}
