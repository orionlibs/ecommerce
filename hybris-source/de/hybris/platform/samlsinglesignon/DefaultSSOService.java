package de.hybris.platform.samlsinglesignon;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.model.SamlUserGroupModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSSOService extends AbstractService implements SSOUserService
{
    protected static final String MD5_PASS_ENCODING = "md5";
    protected static final String SSO_PASS_ENCODING = "sso.password.encoding";
    protected static final String SSO_DATABASE_USERGROUP_MAPPING = "sso.database.usergroup.mapping";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSSOService.class);
    private transient ModelService modelService;
    private transient UserService userService;
    private transient SamlUserGroupDAO samlUserGroupDAO;


    public UserModel getOrCreateSSOUser(String id, String name, Collection<String> roles)
    {
        Preconditions.checkArgument((StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(name)), "User info must not be empty");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(roles), "Roles must not be empty");
        SSOUserMapping userMapping = findMapping(roles);
        Preconditions.checkArgument((userMapping != null), "No SSO user mapping available for roles " + roles + " - cannot accept user " + id);
        UserModel user = lookupExisting(id, userMapping);
        if(user == null)
        {
            user = createNewUser(id, name, userMapping);
        }
        adjustUserAttributes(user, userMapping);
        this.modelService.save(user);
        return user;
    }


    protected UserModel createNewUser(String id, String name, SSOUserMapping userMapping)
    {
        UserModel user = (UserModel)this.modelService.create(userMapping.type);
        user.setUid(id);
        user.setName(name);
        String defaultPasswordEncoding = getPasswordEncoding();
        this.userService.setPassword(user, UUID.randomUUID().toString(), defaultPasswordEncoding);
        return user;
    }


    protected UserModel lookupExisting(String id, SSOUserMapping mapping)
    {
        LOGGER.info("SSOUserMapping {}", mapping);
        try
        {
            return this.userService.getUserForUID(id);
        }
        catch(UnknownIdentifierException e)
        {
            LOGGER.warn(e.getMessage());
            LOGGER.debug(e.getMessage(), (Throwable)e);
            return null;
        }
    }


    protected void adjustUserAttributes(UserModel user, SSOUserMapping mapping)
    {
        user.setGroups((Set)mapping.groups.stream().map(it -> this.userService.getUserGroupForUID(it)).collect(Collectors.toSet()));
    }


    protected SSOUserMapping findMapping(Collection<String> roles)
    {
        if(Config.getBoolean("sso.database.usergroup.mapping", false))
        {
            return findMappingInDatabase(roles);
        }
        return findMappingInProperties(roles);
    }


    protected SSOUserMapping findMappingInProperties(Collection<String> roles)
    {
        SSOUserMapping mergedMapping = null;
        for(String role : roles)
        {
            SSOUserMapping mapping = getMappingForRole(role);
            if(mapping != null)
            {
                if(mergedMapping == null)
                {
                    mergedMapping = new SSOUserMapping();
                    mergedMapping.type = mapping.type;
                }
                if(Objects.equals(mapping.type, mergedMapping.type))
                {
                    mergedMapping.groups.addAll(mapping.groups);
                    continue;
                }
                throw new SystemException("SSO user cannot be configured due to ambigous type mappings (roles: " + roles + ")");
            }
        }
        return mergedMapping;
    }


    protected SSOUserMapping findMappingInDatabase(Collection<String> roles)
    {
        Objects.requireNonNull(this.samlUserGroupDAO);
        List<SamlUserGroupModel> userGroupModels = (List<SamlUserGroupModel>)roles.stream().map(this.samlUserGroupDAO::findSamlUserGroup).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        validateMappings(roles, userGroupModels);
        return performMapping(userGroupModels);
    }


    protected SSOUserMapping performMapping(List<SamlUserGroupModel> userGroupModels)
    {
        String userType = ((SamlUserGroupModel)userGroupModels.get(0)).getUserType().getCode();
        Set<String> userGroups = (Set<String>)userGroupModels.stream().flatMap(samlUserGroupModel -> samlUserGroupModel.getUserGroups().stream()).map(PrincipalModel::getUid).collect(Collectors.toSet());
        SSOUserMapping ssoUserMapping = new SSOUserMapping();
        ssoUserMapping.setType(userType);
        ssoUserMapping.setGroups(userGroups);
        return ssoUserMapping;
    }


    protected void validateMappings(Collection<String> roles, List<SamlUserGroupModel> userGroupModels)
    {
        if(CollectionUtils.isEmpty(userGroupModels))
        {
            throw new IllegalArgumentException("Cannot find mapping for SSO user with roles: " + roles);
        }
        Set<TypeModel> typeMappings = (Set<TypeModel>)userGroupModels.stream().map(SamlUserGroupModel::getUserType).collect(Collectors.toSet());
        boolean moreThanOneTypeMappingExists = (typeMappings.size() > 1);
        if(moreThanOneTypeMappingExists)
        {
            throw new IllegalArgumentException("SSO user cannot be configured due to ambiguous type mappings (roles: " + roles + ")");
        }
    }


    protected SSOUserMapping getMappingForRole(String role)
    {
        Map<String, String> params = Registry.getCurrentTenantNoFallback().getConfig().getParametersMatching("sso\\.mapping\\." + role + "\\.(.*)", true);
        if(MapUtils.isNotEmpty(params))
        {
            SSOUserMapping mapping = new SSOUserMapping();
            mapping.type = params.get("usertype");
            mapping.groups = new LinkedHashSet(Arrays.asList((Object[])((String)params.get("groups")).split("[,;]")));
            return mapping;
        }
        return null;
    }


    private static String getPasswordEncoding()
    {
        return Config.getString("sso.password.encoding", "md5");
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setSamlUserGroupDAO(SamlUserGroupDAO samlUserGroupDAO)
    {
        this.samlUserGroupDAO = samlUserGroupDAO;
    }
}
