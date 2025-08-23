package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hmc.jalo.AccessManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class SystemServiceImpl extends AbstractServiceImpl implements SystemService
{
    protected UserRightsCache urCache;
    private UserService userService;


    public Set<LanguageModel> getAvailableLanguages()
    {
        Set<LanguageModel> ret = new LinkedHashSet<>();
        for(Language l : C2LManager.getInstance().getAllLanguages())
        {
            ret.add((LanguageModel)this.modelService.get(l));
        }
        return ret;
    }


    public Set<String> getAvailableLanguageIsos()
    {
        Set<String> ret = new HashSet<>();
        for(Language l : C2LManager.getInstance().getAllLanguages())
        {
            ret.add(l.getIsoCode());
        }
        return ret;
    }


    public Set<LanguageModel> getAllReadableLanguages()
    {
        Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(getCurrentUser());
        Set<LanguageModel> ret = new LinkedHashSet<>();
        for(UserGroupModel group : userGroups)
        {
            ret.addAll(group.getReadableLanguages());
        }
        return ret.isEmpty() ? getAvailableLanguages() : ret;
    }


    public Set<String> getAllReadableLanguageIsos()
    {
        Set<LanguageModel> readableLanguages = getAllReadableLanguages();
        Set<String> ret = new LinkedHashSet<>();
        for(LanguageModel l : readableLanguages)
        {
            ret.add(l.getIsocode());
        }
        return ret;
    }


    public Set<LanguageModel> getAllWriteableLanguages()
    {
        Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(getCurrentUser());
        Set<LanguageModel> ret = new LinkedHashSet<>();
        for(UserGroupModel group : userGroups)
        {
            ret.addAll(group.getWriteableLanguages());
        }
        return ret.isEmpty() ? getAvailableLanguages() : ret;
    }


    public Set<String> getAllWriteableLanguageIsos()
    {
        Set<LanguageModel> writeableLanguages = getAllWriteableLanguages();
        Set<String> ret = new HashSet<>();
        for(LanguageModel l : writeableLanguages)
        {
            ret.add(l.getIsocode());
        }
        return ret;
    }


    public LanguageModel getLanguageForLocale(Locale loc)
    {
        Language language = matchLanguage(loc);
        if(language == null && loc.getVariant() != null && loc.getVariant().length() > 0)
        {
            Locale locNew = new Locale(loc.getLanguage(), loc.getCountry());
            language = matchLanguage(locNew);
        }
        if(language == null && loc.getCountry() != null && loc.getCountry().length() > 0)
        {
            Locale locNew = new Locale(loc.getLanguage());
            language = matchLanguage(locNew);
        }
        return (LanguageModel)this.modelService.get((language != null) ? language : JaloSession.getCurrentSession().getSessionContext()
                        .getLanguage());
    }


    public LanguageModel getCurrentLanguage()
    {
        Language language = JaloSession.hasCurrentSession() ? JaloSession.getCurrentSession().getSessionContext().getLanguage() : null;
        return (language != null) ? (LanguageModel)this.modelService.get(language) : null;
    }


    public UserModel getCurrentUser()
    {
        User user = JaloSession.hasCurrentSession() ? JaloSession.getCurrentSession().getSessionContext().getUser() : null;
        return (user != null) ? (UserModel)this.modelService.get(user) : null;
    }


    private Language matchLanguage(Locale loc)
    {
        Language language = null;
        try
        {
            language = C2LManager.getInstance().getLanguageByIsoCode(loc.toString());
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(language == null)
        {
            for(Language lx : C2LManager.getInstance().getAllLanguages())
            {
                if(loc.equals(lx.getLocale()) || loc.toString().equalsIgnoreCase(lx.getLocale().toString()))
                {
                    language = lx;
                    break;
                }
            }
        }
        return language;
    }


    public UserModel getUserByUID(String uid)
    {
        String typeCode = TypeManager.getInstance().getComposedType(User.class).getCode();
        StringBuilder query = (new StringBuilder("SELECT {")).append(User.PK).append("} FROM {").append(typeCode).append("}");
        query.append(" WHERE ");
        query.append("{").append("uid").append("} = ?").append("uid");
        List<User> list = FlexibleSearch.getInstance().search(query.toString(), Collections.singletonMap("uid", uid), User.class).getResult();
        User user = list.get(0);
        return list.isEmpty() ? null : (UserModel)this.modelService.get(user);
    }


    public List<String> getUsersByName(String name, String typecode)
    {
        Map<String, String> values = Collections.singletonMap("name", name.trim().endsWith("%") ? name : (name + "%"));
        String typeCode = StringUtils.isNotBlank(typecode) ? typecode : TypeManager.getInstance().getComposedType(User.class).getCode();
        StringBuilder query = (new StringBuilder("SELECT {")).append("name").append("} FROM {").append(typeCode).append("}");
        query.append(" WHERE ");
        query.append("{").append("name").append("} LIKE ?").append("name");
        return FlexibleSearch.getInstance().search(query.toString(), values, String.class).getResult();
    }


    public UserModel getUserByName(String name)
    {
        String typeCode = TypeManager.getInstance().getComposedType(User.class).getCode();
        StringBuilder query = (new StringBuilder("SELECT {")).append(User.PK).append("} FROM {").append(typeCode).append("}");
        query.append(" WHERE ");
        query.append("{").append("name").append("} = ?").append("name");
        List<User> list = FlexibleSearch.getInstance().search(query.toString(), Collections.singletonMap("name", name), User.class).getResult();
        User user = list.get(0);
        return list.isEmpty() ? null : (UserModel)this.modelService.get(user);
    }


    public void setSessionLanguage(LanguageModel lang)
    {
        if(lang == null)
        {
            return;
        }
        JaloSession.getCurrentSession().getSessionContext()
                        .setLanguage(C2LManager.getInstance().getLanguageByIsoCode(lang.getIsocode()));
    }


    public boolean checkPermissionOn(UserModel user, String typeCode, String permissionCode)
    {
        User userItem = (User)this.modelService.getSource(user);
        ComposedType composedType = TypeManager.getInstance().getComposedType(typeCode);
        Boolean cached = getUserRightFromCache(composedType.getPK(), permissionCode);
        if(cached != null)
        {
            return cached.booleanValue();
        }
        boolean computed = AccessManager.getInstance().hasRight((Principal)userItem, composedType, permissionCode);
        addUserRightToCache(composedType.getPK(), permissionCode, computed);
        return computed;
    }


    public boolean checkPermissionOn(String typeCode, String permissionCode)
    {
        return checkPermissionOn(getCurrentUser(), typeCode, permissionCode);
    }


    public boolean checkAttributePermissionOn(String typeCode, String attributeQualifier, String permissionCode)
    {
        ComposedType composedType = TypeManager.getInstance().getComposedType(typeCode);
        AttributeDescriptor descriptor = composedType.getAttributeDescriptorIncludingPrivate(attributeQualifier);
        Boolean cached = getUserRightFromCache(descriptor.getPK(), permissionCode);
        if(cached != null)
        {
            return cached.booleanValue();
        }
        boolean computed = false;
        if(permissionCode.equals("change"))
        {
            computed = (AccessManager.getInstance().canChange(descriptor) && descriptor.isWritable());
        }
        else if(permissionCode.equals("read"))
        {
            computed = (AccessManager.getInstance().canRead(descriptor) && descriptor.isReadable());
        }
        addUserRightToCache(descriptor.getPK(), permissionCode, computed);
        return computed;
    }


    public CatalogVersionModel getCatalogVersion(TypedObject typedObject)
    {
        CatalogVersionModel catalogVersion = null;
        List<CatalogVersionModel> allAvailableCatalogVersions = new ArrayList<>();
        if(typedObject != null)
        {
            Object object = typedObject.getObject();
            if(object instanceof de.hybris.platform.core.model.ItemModel)
            {
                Object jaloObject = CatalogManager.getInstance().getCatalogVersion((Item)this.modelService
                                .getSource(object));
                if(jaloObject != null)
                {
                    catalogVersion = (CatalogVersionModel)this.modelService.get(jaloObject);
                    User user = JaloSession.getCurrentSession().getUser();
                    if(!user.isAdmin())
                    {
                        SessionContext ctx = null;
                        try
                        {
                            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
                            for(CatalogVersion jaloCatalogVersion : CatalogManager.getInstance().getAvailableCatalogVersions(
                                            JaloSession.getCurrentSession()))
                            {
                                CatalogVersionModel uiVersion = (CatalogVersionModel)this.modelService.get(jaloCatalogVersion);
                                allAvailableCatalogVersions.add(uiVersion);
                            }
                            if(!allAvailableCatalogVersions.contains(catalogVersion))
                            {
                                catalogVersion = null;
                            }
                        }
                        finally
                        {
                            if(ctx != null)
                            {
                                JaloSession.getCurrentSession().removeLocalSessionContext();
                            }
                        }
                    }
                }
            }
        }
        return catalogVersion;
    }


    public void setUserRightsCache(UserRightsCache urCache)
    {
        this.urCache = urCache;
    }


    protected Boolean getUserRightFromCache(PK relatedObject, String right)
    {
        if(this.urCache != null)
        {
            return this.urCache.getRight(relatedObject, right);
        }
        return null;
    }


    protected void addUserRightToCache(PK relatedObject, String right, boolean value)
    {
        if(this.urCache != null)
        {
            this.urCache.addRight(relatedObject, right, value);
        }
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public boolean itemExist(PK pk)
    {
        if(pk == null)
        {
            return false;
        }
        try
        {
            JaloSession.getCurrentSession().getItem(pk);
        }
        catch(JaloItemNotFoundException e)
        {
            return false;
        }
        return true;
    }
}
