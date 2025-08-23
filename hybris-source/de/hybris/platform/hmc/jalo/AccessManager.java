package de.hybris.platform.hmc.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.SingletonCreator;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessManager
{
    private static final boolean DISABLED = false;
    public static final String READ = "read";
    public static final String CHANGE = "change";
    public static final String CHANGE_PERMISSIONS = "changerights";
    public static final String CREATE = "create";
    public static final String REMOVE = "remove";
    private static final List<String> userRightKeys = Arrays.asList(new String[] {"read", "change", "changerights", "create", "remove"});
    public static final String GRANT = "grant";
    public static final String SUPER_GRANT = "super_grant";
    public static final String DENY = "deny";
    public static final String SUPER_DENY = "super_deny";
    public static final String NONE = "none";
    private volatile transient List<UserRight> theTypeUserRights;
    private volatile transient List<UserRight> theDescriptorUserRights;
    public static final SingletonCreator.Creator<AccessManager> SINGLETON_CREATOR = (SingletonCreator.Creator<AccessManager>)new Object();


    public static final void resetUserRights()
    {
        getInstance().clearCaches();
    }


    private synchronized void clearCaches()
    {
        this.theTypeUserRights = null;
        this.theDescriptorUserRights = null;
    }


    public static final AccessManager getInstance()
    {
        return (AccessManager)Registry.getSingleton(SINGLETON_CREATOR);
    }


    public List<UserRight> getTypeUserRights()
    {
        if(this.theTypeUserRights == null)
        {
            synchronized(this)
            {
                if(this.theTypeUserRights == null)
                {
                    this.theTypeUserRights = createTypeUserRights();
                }
            }
        }
        return this.theTypeUserRights;
    }


    public List<UserRight> getDescriptorUserRights()
    {
        if(this.theDescriptorUserRights == null)
        {
            synchronized(this)
            {
                if(this.theDescriptorUserRights == null)
                {
                    this.theDescriptorUserRights = createDescriptorUserRights();
                }
            }
        }
        return this.theDescriptorUserRights;
    }


    private List<UserRight> createTypeUserRights()
    {
        List<UserRight> userRightList = new ArrayList<>();
        for(String rightKey : userRightKeys)
        {
            userRightList.add(JaloSession.getCurrentSession().getAccessManager().getOrCreateUserRightByCode(rightKey));
        }
        return userRightList;
    }


    private List<UserRight> createDescriptorUserRights()
    {
        List<UserRight> userRightList = new ArrayList<>();
        List<UserRight> typeUserRights = getTypeUserRights();
        userRightList.add(typeUserRights.get(0));
        userRightList.add(typeUserRights.get(1));
        userRightList.add(typeUserRights.get(2));
        return userRightList;
    }


    private UserRight getUserRight(String name)
    {
        UserRight right = getTypeUserRights().get(getRightIndex(name));
        if(right == null)
        {
            throw new HMCSystemException(new InvalidParameterException(), "Could not find UserRight for the key '" + name + "'! The key has to be one of [read, change, create, remove, changerights]");
        }
        return right;
    }


    public int getRightIndex(String userRight)
    {
        return userRightKeys.indexOf(userRight);
    }


    public boolean hasRight(AttributeDescriptor descriptor, String userRightKey)
    {
        return (isAdmin() || descriptor.checkTypePermission(getUserRight(userRightKey)));
    }


    public boolean hasRight(Principal principal, AttributeDescriptor descriptor, String userRightKey)
    {
        return (principal.isAdmin() || descriptor.checkTypePermission(principal, getUserRight(userRightKey)));
    }


    public boolean hasRight(ComposedType type, String userRightKey)
    {
        return (isAdmin() || type.checkTypePermission(getUserRight(userRightKey)));
    }


    public boolean hasRight(Principal principal, ComposedType type, String userRightKey)
    {
        return (principal.isAdmin() || type.checkTypePermission(principal, getUserRight(userRightKey)));
    }


    public boolean isAdmin()
    {
        return isAdmin((Principal)getJaloSession().getUser());
    }


    public boolean isAdmin(Principal principal)
    {
        return principal.isAdmin();
    }


    public boolean canChangeInstance(Item item)
    {
        if(item == null || !item.isAlive())
        {
            return false;
        }
        return (canChange(item.getComposedType()) && permittedByExtensions(item, null));
    }


    public boolean canChangeInstance(Item item, AttributeDescriptor att)
    {
        if(item != null && !item.isAlive())
        {
            return false;
        }
        return (canChange(att) && permittedByExtensions(item, att));
    }


    public boolean canChange(ComposedType type)
    {
        return hasRight(type, "change");
    }


    public boolean canChange(AttributeDescriptor descriptor)
    {
        return hasRight(descriptor, "change");
    }


    public boolean canRead(ComposedType type)
    {
        return hasRight(type, "read");
    }


    public boolean canRead(AttributeDescriptor descriptor)
    {
        return hasRight(descriptor, "read");
    }


    public boolean canRemoveInstance(Item item)
    {
        return canRemoveInstance(item, null);
    }


    public boolean canRemoveInstance(Item item, AttributeDescriptor att)
    {
        if(item == null || !item.isAlive())
        {
            return false;
        }
        return (canRemove(item.getComposedType()) && permittedByExtensions(item, att));
    }


    public boolean canRemove(ComposedType type)
    {
        return hasRight(type, "remove");
    }


    public boolean canChangePermissions(ComposedType type)
    {
        return hasRight(type, "changerights");
    }


    public boolean canChangePermissions(AttributeDescriptor descriptor)
    {
        return hasRight(descriptor, "changerights");
    }


    public boolean canCreateInstance(Item item)
    {
        return canCreateInstance(item, null);
    }


    public boolean canCreateInstance(Item item, AttributeDescriptor att)
    {
        if(item == null || !item.isAlive())
        {
            return false;
        }
        return (canCreate(item.getComposedType()) && permittedByExtensions(item, att));
    }


    public boolean canCreate(ComposedType type)
    {
        return hasRight(type, "create");
    }


    private JaloSession getJaloSession()
    {
        return JaloSession.getCurrentSession();
    }


    public Map getPermissionMap(ComposedType type)
    {
        Map permissionMap = type.getPermissionMap(getTypeUserRights());
        filterInvalidPrincipals(permissionMap.keySet());
        return permissionMap;
    }


    public Map getPermissionMap(AttributeDescriptor attributeDescriptor)
    {
        Map permissionMap = attributeDescriptor.getPermissionMap(getDescriptorUserRights());
        filterInvalidPrincipals(permissionMap.keySet());
        return permissionMap;
    }


    public static void filterInvalidPrincipals(Set principals)
    {
        for(Iterator iter = principals.iterator(); iter.hasNext(); )
        {
            Object principal = iter.next();
            if(principal == null || !(principal instanceof Principal) || !((Principal)principal).isAlive())
            {
                iter.remove();
            }
        }
    }


    public void setPermissionMap(ComposedType type, Map permissionMap)
    {
        try
        {
            type.setPermissionsByMap(getTypeUserRights(), permissionMap);
        }
        catch(JaloSecurityException e)
        {
            throw new HMCSystemException(e, "Could not set permissions for type '" + type.getCode() + "'!!");
        }
    }


    public void setPermissionMap(AttributeDescriptor attributeDescriptor, Map permissionMap)
    {
        try
        {
            attributeDescriptor.setPermissionsByMap(getDescriptorUserRights(), permissionMap);
        }
        catch(JaloSecurityException e)
        {
            throw new HMCSystemException(e, "Could not set permissions for descriptor '" + attributeDescriptor
                            .getQualifier() + "'!!");
        }
    }


    public Map getPermissionMap(Principal principal)
    {
        Map<Item, List> permissions = principal.getItemPermissionsMap(getTypeUserRights());
        Set items = collectItemsWithPermissions(principal);
        if(items != null)
        {
            Set itemsInMap = permissions.keySet();
            for(Iterator<Item> iter = items.iterator(); iter.hasNext(); )
            {
                List emptyPermissions = Arrays.asList(new Object[] {null, null, null, null, null});
                Item item = iter.next();
                if(!itemsInMap.contains(item))
                {
                    permissions.put(item, emptyPermissions);
                }
            }
        }
        return permissions;
    }


    private Set collectItemsWithPermissions(Principal principal)
    {
        Set allGroups = principal.getAllGroups();
        if(allGroups.isEmpty())
        {
            return null;
        }
        Set items = new HashSet();
        for(Iterator<Principal> iter = allGroups.iterator(); iter.hasNext(); )
        {
            Principal nextPrincipal = iter.next();
            items.addAll(nextPrincipal.getItemPermissionsMap(getTypeUserRights()).keySet());
        }
        return items;
    }


    public void setPermissionMap(Principal principal, Map permissionMap)
    {
        principal.setItemPermissionsByMap(getTypeUserRights(), permissionMap);
    }


    public boolean subTypesVisible(ComposedType type)
    {
        if(type == null)
        {
            return false;
        }
        if(canRead(type) && !type.isAbstract())
        {
            return true;
        }
        for(Iterator<ComposedType> iter = type.getAllSubTypes().iterator(); iter.hasNext(); )
        {
            ComposedType subType = iter.next();
            if(canRead(subType) && !subType.isAbstract())
            {
                return true;
            }
        }
        return false;
    }


    private boolean permittedByExtensions(Item item, AttributeDescriptor descriptor)
    {
        if(item != null && !item.isAlive())
        {
            return false;
        }
        return de.hybris.platform.jalo.security.AccessManager.getInstance().isEditable(item, descriptor);
    }


    public ArrayList checkLicence()
    {
        return de.hybris.platform.jalo.security.AccessManager.getInstance().checkLicence();
    }


    public Set<Language> getRestrictedLanguages(Item item)
    {
        return de.hybris.platform.jalo.security.AccessManager.getInstance().getRestrictedLanguages(item);
    }
}
