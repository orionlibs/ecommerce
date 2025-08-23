package de.hybris.platform.cockpit.services.security.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.daos.CockpitUIComponentAccessRightDao;
import de.hybris.platform.cockpit.model.CockpitUIComponentAccessRightModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultUIAccessRightService extends AbstractServiceImpl implements UIAccessRightService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUIAccessRightService.class);
    private static final String UI_ACCESS_RIGHT_SERVICE_WRITE_REQUEST_CACHE = "uiAccessRightServiceWriteRequestCache";
    private static final String UI_ACCESS_RIGHT_SERVICE_READ_REQUEST_CACHE = "uiAccessRightServiceReadRequestCache";
    private static final String PRODUCT_CODE = "Product";
    private static final String PRODUCT_FEATURES_CODE = "Product.features";
    protected CockpitUIComponentAccessRightDao cockpitUIComponentAccessRightDao;
    private UserService userService;
    private SystemService systemService;


    public boolean canRead(UserModel user, String componentCode)
    {
        CockpitUIComponentAccessRightModel access = this.cockpitUIComponentAccessRightDao.findCockpitUIComponentAccessRight(componentCode);
        boolean canread = true;
        if(access != null)
        {
            canread = !restricts(access.getReadPrincipals(), user);
            if(!canread)
            {
                Collection<PrincipalModel> writePrincipals = access.getWritePrincipals();
                if(!writePrincipals.isEmpty())
                {
                    canread = !restricts(access.getWritePrincipals(), user);
                }
            }
        }
        return canread;
    }


    public boolean canWrite(UserModel user, String componentCode)
    {
        CockpitUIComponentAccessRightModel access = this.cockpitUIComponentAccessRightDao.findCockpitUIComponentAccessRight(componentCode);
        boolean canwrite = true;
        if(access != null)
        {
            canwrite = !restricts(access.getWritePrincipals(), user);
        }
        return canwrite;
    }


    protected boolean restricts(Collection<PrincipalModel> principals, UserModel user)
    {
        boolean restricts = true;
        if(principals.contains(user))
        {
            restricts = false;
        }
        else
        {
            for(UserGroupModel group : this.userService.getAllUserGroupsForUser(user))
            {
                if(principals.contains(group))
                {
                    restricts = false;
                    break;
                }
            }
        }
        return restricts;
    }


    @Required
    public void setCockpitUIComponentAccessRightDao(CockpitUIComponentAccessRightDao cockpitUIComponentAccessRightDao)
    {
        this.cockpitUIComponentAccessRightDao = cockpitUIComponentAccessRightDao;
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


    public boolean isReadable(ObjectType type)
    {
        return (type != null && ((Boolean)(new Object(this, "uiAccessRightServiceReadRequestCache", type))
                        .get(type.getCode())).booleanValue());
    }


    public boolean isReadable(ObjectType type, PropertyDescriptor propDescr, boolean creationMode)
    {
        StringBuilder key = new StringBuilder();
        if(type == null || type.getCode() == null)
        {
            key.append('.');
        }
        else
        {
            key.append(type.getCode());
        }
        if(propDescr == null || propDescr.getQualifier() == null)
        {
            key.append('.');
        }
        else
        {
            key.append(propDescr.getQualifier());
        }
        key.append('.').append(creationMode);
        return ((Boolean)(new Object(this, "uiAccessRightServiceReadRequestCache", type, propDescr, creationMode))
                        .get(key.toString())).booleanValue();
    }


    public boolean isWritable(ObjectType type)
    {
        return (type != null && ((Boolean)(new Object(this, "uiAccessRightServiceWriteRequestCache", type))
                        .get(type.getCode())).booleanValue());
    }


    public boolean isWritable(ObjectType type, TypedObject item)
    {
        boolean accessOk = isWritable(type);
        if(item != null && item.getObject() != null)
        {
            if(getModelService().isNew(item.getObject()))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Write permission for instance could not be determined since item is being created.");
                }
            }
            else
            {
                accessOk = ((Boolean)(new Object(this, "uiAccessRightServiceWriteRequestCache", item)).get(((type != null) ? type.getCode() : item.getType()) + '.' + ((ItemModel)item.getObject()).getPk())).booleanValue();
            }
        }
        return accessOk;
    }


    public boolean isWritable(ObjectType type, TypedObject item, PropertyDescriptor propDescr, boolean creationMode)
    {
        boolean writableType = isWritable(type, item);
        if(writableType && propDescr != null && type != null)
        {
            return ((Boolean)(new Object(this, "uiAccessRightServiceWriteRequestCache", propDescr, type, creationMode, item))
                            .get(type.getCode() + '.' + propDescr.getQualifier() + '.' + creationMode))
                            .booleanValue();
        }
        return writableType;
    }


    public boolean isWritable(ObjectType type, PropertyDescriptor propDescr, boolean creationMode)
    {
        return isWritable(type, null, propDescr, creationMode);
    }


    protected String getBaseTypeCode(ObjectType type, ItemAttributePropertyDescriptor descr)
    {
        String code = null;
        if(descr != null && descr.getEnclosingType() instanceof de.hybris.platform.variants.model.VariantTypeModel)
        {
            code = descr.getTypeCode();
        }
        else if(type instanceof de.hybris.platform.cockpit.model.meta.BaseType)
        {
            code = type.getCode();
        }
        else if(type instanceof ObjectTemplate)
        {
            code = ((ObjectTemplate)type).getBaseType().getCode();
        }
        return code;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    public boolean canWrite(UserModel user, CatalogVersionModel catVersion)
    {
        boolean accessOk = false;
        if(user == null || getModelService().isNew(user) || catVersion == null || getModelService().isNew(catVersion))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not determine write access. Reason: User or catalog version not persisted.");
            }
        }
        else
        {
            accessOk = CatalogManager.getInstance().canWrite(JaloSession.getCurrentSession().getSessionContext(), (User)
                            getModelService().getSource(user), (CatalogVersion)getModelService().getSource(catVersion));
        }
        return accessOk;
    }


    protected SessionContext getSessionContext()
    {
        return JaloSession.getCurrentSession().getSessionContext();
    }
}
