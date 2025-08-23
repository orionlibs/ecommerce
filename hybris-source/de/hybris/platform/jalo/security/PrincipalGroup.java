package de.hybris.platform.jalo.security;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class PrincipalGroup extends GeneratedPrincipalGroup
{
    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    protected void setMembers(SessionContext ctx, Collection<?> newMembers) throws ConsistencyCheckException
    {
        setMembers(ctx, (newMembers == null || newMembers instanceof Set) ? (Set)newMembers : new HashSet(newMembers));
    }


    @SLDSafe
    public Set<Principal> getMembers(SessionContext ctx)
    {
        if(isPrincipalGroupRelationCachingEnabled())
        {
            return getFromRelationCache(ctx, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, "members", getPK());
        }
        return (Set<Principal>)(new Object(this, getTenant().getCache(), 201, "members" +
                        getPK().getLongValueAsString(), ctx))
                        .getCached();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean addMember(Principal p)
    {
        return addMember(getSession().getSessionContext(), p);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean addMember(SessionContext ctx, Principal p)
    {
        return p.addToGroup(this);
    }


    @SLDSafe(portingClass = "GroupsCycleCheckValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void addToGroups(SessionContext ctx, PrincipalGroup group)
    {
        if(equals(group))
        {
            throw new JaloInvalidParameterException("group " + getUID() + " cannot be added to itself", 0);
        }
        if(group.getAllGroups().contains(this))
        {
            throw new JaloInvalidParameterException("group " +
                            getUID() + " cannot be added to its subgroup " + group.getUID(), 0);
        }
        super.addToGroups(ctx, group);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean addToGroup(PrincipalGroup group)
    {
        if(equals(group))
        {
            throw new JaloInvalidParameterException("group " + getUID() + " cannot be added to itself", 0);
        }
        if(group.getAllGroups().contains(this))
        {
            throw new JaloInvalidParameterException("group " +
                            getUID() + " cannot be added to its subgroup " + group.getUID(), 0);
        }
        return super.addToGroup(group);
    }


    @SLDSafe(portingClass = "GroupsCycleCheckValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void setGroups(SessionContext ctx, Set<?> groups)
    {
        if(groups != null && !groups.isEmpty())
        {
            Set newGroups = new HashSet(groups);
            newGroups.removeAll(getGroups());
            for(Iterator<PrincipalGroup> it = newGroups.iterator(); it.hasNext(); )
            {
                PrincipalGroup grp = it.next();
                if(equals(grp))
                {
                    throw new JaloInvalidParameterException("group " + getUID() + " cannot be placed inside itself", 0);
                }
                if(grp.getAllGroups().contains(this))
                {
                    throw new JaloInvalidParameterException("group " +
                                    getUID() + " is already subgroup of and cannot be placed inside " + grp.getUID(), 0);
                }
            }
        }
        super.setGroups(ctx, groups);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean removeMember(Principal p) throws ConsistencyCheckException
    {
        return removeMember(getSession().getSessionContext(), p);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean removeMember(SessionContext ctx, Principal p) throws ConsistencyCheckException
    {
        return p.removeFromGroup(this);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean containsMember(Principal p)
    {
        return containsMember(getSession().getSessionContext(), p);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean containsMember(SessionContext ctx, Principal p)
    {
        return (p == null) ? false : p.getGroups(ctx).contains(this);
    }


    @SLDSafe(portingClass = "PrincipalGroupDisplayNameLocalizedAttributeHandler", portingMethod = "get(final PrincipalGroupModel model)")
    @Deprecated(since = "ages", forRemoval = false)
    public String getDisplayName(SessionContext ctx)
    {
        return getLocName(ctx);
    }


    @SLDSafe(portingClass = "PrincipalGroupDisplayNameLocalizedAttributeHandler", portingMethod = "get(final PrincipalGroupModel model, final Locale loc)")
    @Deprecated(since = "ages", forRemoval = false)
    public Map<Language, String> getAllDisplayName(SessionContext ctx)
    {
        return getAllLocName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllLocNames(SessionContext ctx)
    {
        return getAllLocName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllLocNames(SessionContext ctx, Map names)
    {
        setAllLocName(ctx, names);
    }
}
