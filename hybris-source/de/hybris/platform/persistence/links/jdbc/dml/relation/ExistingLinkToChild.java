package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.MoreObjects;

class ExistingLinkToChild
{
    private final Long linkPK;
    private final Long childPK;
    private final Integer position;
    private final Long version;


    ExistingLinkToChild(long linkPK, long childPK, int position, long version)
    {
        this.linkPK = Long.valueOf(linkPK);
        this.childPK = Long.valueOf(childPK);
        this.position = Integer.valueOf(position);
        this.version = Long.valueOf(version);
    }


    public long getLinkPK()
    {
        return this.linkPK.longValue();
    }


    public Long getChildPK()
    {
        return this.childPK;
    }


    public int getPosition()
    {
        return this.position.intValue();
    }


    public long getVersion()
    {
        return this.version.longValue();
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("linkPK", this.linkPK).add("childPK", this.childPK).add("position", this.position)
                        .add("version", this.version).toString();
    }
}
