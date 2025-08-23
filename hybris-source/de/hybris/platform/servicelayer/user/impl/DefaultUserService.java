package de.hybris.platform.servicelayer.user.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.model.user.UserPasswordChangeAuditModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import de.hybris.platform.servicelayer.user.daos.UserAuditDao;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordPolicyViolationException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultUserService extends AbstractBusinessService implements UserService
{
    private UserDao userDao;
    private UserGroupDao userGroupDao;
    private TitleDao titleDao;
    private UserAuditDao userAuditDao;
    private SearchRestrictionService searchRestrictionService;
    private EventService eventService;
    private PasswordEncoderService passwordEncoderService;
    private PasswordPolicyService passwordPolicyService;
    private String defaultPasswordEncoding = "*";


    @Deprecated(since = "6.1.0", forRemoval = true)
    public UserModel getUser(String uid)
    {
        return getUserForUID(uid);
    }


    public UserModel getUserForUID(String userId)
    {
        ServicesUtil.validateParameterNotNull(userId, "The given userID is null!");
        UserModel user = this.userDao.findUserByUID(userId);
        if(user == null)
        {
            throw new UnknownIdentifierException("Cannot find user with uid '" + userId + "'");
        }
        return user;
    }


    public <T extends UserModel> T getUserForUID(String userId, Class<T> clazz)
    {
        ServicesUtil.validateParameterNotNull(userId, "The given userID is null!");
        ServicesUtil.validateParameterNotNull(clazz, "The given clazz is null!");
        return (T)validateType(getUserForUID(userId), clazz);
    }


    public boolean isUserExisting(String uid)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("uid", uid);
        return (this.userDao.findUserByUID(uid) != null);
    }


    @Deprecated(since = "6.1.0", forRemoval = true)
    public UserGroupModel getUserGroup(String groupId)
    {
        return getUserGroupForUID(groupId);
    }


    public UserModel getCurrentUser()
    {
        return (UserModel)getSessionService().getAttribute("user");
    }


    public void setCurrentUser(UserModel user)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        UserModel previous = getCurrentUser();
        if(!user.equals(previous))
        {
            getSessionService().setAttribute("user", user);
            AfterSessionUserChangeEvent evt = new AfterSessionUserChangeEvent((Serializable)JaloSession.getCurrentSession());
            evt.setPreviousUserUID((previous == null) ? null : previous.getUid());
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    public String getPassword(UserModel user) throws CannotDecodePasswordException, PasswordEncoderNotFoundException
    {
        return this.passwordEncoderService.decode(user);
    }


    public void setPassword(UserModel user, String plainPassword, String encoding)
    {
        String realEncoding = getRealEncoding(encoding);
        assurePasswordCompliance(user, plainPassword, encoding);
        user.setEncodedPassword(this.passwordEncoderService.encode(user, plainPassword, realEncoding));
        user.setPasswordEncoding(realEncoding);
    }


    protected void assurePasswordCompliance(UserModel user, String plainPassword, String encoding)
    {
        List<PasswordPolicyViolation> policyViolations = this.passwordPolicyService.verifyPassword(user, plainPassword, encoding);
        if(!policyViolations.isEmpty())
        {
            throw new PasswordPolicyViolationException(policyViolations);
        }
    }


    protected String getRealEncoding(String optionalEncoding)
    {
        if(StringUtils.isBlank(optionalEncoding) || "*".equalsIgnoreCase(optionalEncoding))
        {
            return getDefaultPasswordEncoding();
        }
        return optionalEncoding;
    }


    public void setPasswordWithDefaultEncoding(UserModel user, String plainPassword) throws PasswordEncoderNotFoundException
    {
        setPassword(user, plainPassword, getDefaultPasswordEncoding());
    }


    public String getPassword(String userId) throws CannotDecodePasswordException, PasswordEncoderNotFoundException
    {
        UserModel userModel = getUserForUID(userId);
        return getPassword(userModel);
    }


    public void setPassword(String userId, String password) throws PasswordEncoderNotFoundException
    {
        UserModel userModel = getUserForUID(userId);
        assurePasswordCompliance(userModel, password, getDefaultPasswordEncoding());
        setPassword(userModel, password);
        getModelService().save(userModel);
    }


    public void setPassword(String userId, String password, String encoding) throws PasswordEncoderNotFoundException
    {
        UserModel userModel = getUserForUID(userId);
        assurePasswordCompliance(userModel, password, encoding);
        setPassword(userModel, password, encoding);
        getModelService().save(userModel);
    }


    public UserGroupModel getUserGroupForUID(String uid)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("uid", uid);
        return validateNotNull(this.userGroupDao.findUserGroupByUid(uid), UserGroupModel.class, "uid", uid);
    }


    public <T extends UserGroupModel> T getUserGroupForUID(String uid, Class<T> clazz)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("clazz", clazz);
        return (T)validateType(getUserGroupForUID(uid), clazz);
    }


    public Set<UserGroupModel> getAllUserGroupsForUser(UserModel user)
    {
        return getAllUserGroupsForUser(user, UserGroupModel.class);
    }


    public <T extends UserGroupModel> Set<T> getAllUserGroupsForUser(UserModel user, Class<T> clazz)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("clazz", clazz);
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        return collectAndFilterGroups(user.getGroups(), clazz);
    }


    public <T extends UserGroupModel> Set<T> getAllUserGroupsForUserGroup(UserGroupModel userGroup, Class<T> clazz)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("clazz", clazz);
        ServicesUtil.validateParameterNotNullStandardMessage("userGroup", userGroup);
        return collectAndFilterGroups(userGroup.getGroups(), clazz);
    }


    public List<AbstractUserAuditModel> getUserAudits(UserModel user)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        return this.userAuditDao.getUserAudit(user.getPk());
    }


    public boolean isPasswordIdenticalToAudited(UserModel user, String plainPassword, UserPasswordChangeAuditModel audit)
    {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(audit);
        String encodedPassword = this.passwordEncoderService.encode(user, plainPassword, audit.getPasswordEncoding());
        return encodedPassword.equals(audit.getEncodedPassword());
    }


    public boolean isMemberOfGroup(UserModel member, UserGroupModel groupToCheckFor)
    {
        return isMemberOfGroup(member, groupToCheckFor, true);
    }


    public boolean isMemberOfGroup(UserModel member, UserGroupModel groupToCheckFor, boolean includeSuperGroups)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("member", member);
        return isMemberOfGroup(member.getGroups(), (PrincipalGroupModel)groupToCheckFor, includeSuperGroups);
    }


    public boolean isMemberOfGroup(UserGroupModel member, UserGroupModel groupToCheckFor)
    {
        return isMemberOfGroup(member, groupToCheckFor, true);
    }


    public boolean isMemberOfGroup(UserGroupModel member, UserGroupModel groupToCheckFor, boolean includeSuperGroups)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("member", member);
        return isMemberOfGroup(member.getGroups(), (PrincipalGroupModel)groupToCheckFor, includeSuperGroups);
    }


    private boolean isMemberOfGroup(Set<PrincipalGroupModel> memberGroups, PrincipalGroupModel groupToFind, boolean includeSuperGroups)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("groupToFind", groupToFind);
        if(!CollectionUtils.isEmpty(memberGroups))
        {
            for(PrincipalGroupModel groupToCheck : memberGroups)
            {
                if(groupToFind.equals(groupToCheck) || (includeSuperGroups &&
                                isMemberOfGroup(groupToCheck.getGroups(), groupToFind, true)))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public Collection<TitleModel> getAllTitles()
    {
        return this.titleDao.findTitles();
    }


    public TitleModel getTitleForCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        return validateNotNull(this.titleDao.findTitleByCode(code), TitleModel.class, "code", code);
    }


    public EmployeeModel getAdminUser()
    {
        return (EmployeeModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this));
    }


    public UserGroupModel getAdminUserGroup()
    {
        return (UserGroupModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this));
    }


    public boolean isAdmin(UserModel user)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        return (isAdminEmployee(user) || getAllUserGroupsForUser(user).contains(getAdminUserGroup()));
    }


    public boolean isAdminGroup(UserGroupModel userGroup)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("userGroup", userGroup);
        UserGroupModel adminUserGroup = getAdminUserGroup();
        return (adminUserGroup.equals(userGroup) || userGroup.getGroups().contains(adminUserGroup));
    }


    public boolean isAdminEmployee(UserModel user)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        return getAdminUser().equals(user);
    }


    public CustomerModel getAnonymousUser()
    {
        return (CustomerModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this));
    }


    public boolean isAnonymousUser(UserModel user)
    {
        return (user != null && user.equals(getAnonymousUser()));
    }


    public void setEncodedPassword(UserModel user, String encodedPassword)
    {
        setEncodedPassword(user, encodedPassword, getRealEncoding(user.getPasswordEncoding()));
    }


    public void setEncodedPassword(UserModel user, String encodedPassword, String encoding)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        user.setEncodedPassword(encodedPassword);
        user.setPasswordEncoding(encoding);
    }


    private <T> T validateNotNull(T value, Class type, String qualifier, String qualifierValue)
    {
        return validateNotNull(value, type, new String[] {qualifier}, new String[] {qualifierValue});
    }


    private <T> T validateNotNull(T value, Class type, String[] qualifiers, String[] qualifierValues)
    {
        if(value == null)
        {
            throw new UnknownIdentifierException(type.getSimpleName() + " with " + type.getSimpleName() + " '" + StringUtils.join(qualifiers, ", ") + "' not found!");
        }
        return value;
    }


    private <T> T validateType(T value, Class type)
    {
        if(!type.isInstance(value))
        {
            throw new ClassMismatchException(type, value.getClass());
        }
        return value;
    }


    private void collectGroups(Set<PrincipalGroupModel> collectedGroups, Set<PrincipalGroupModel> groups)
    {
        if(groups != null)
        {
            for(PrincipalGroupModel group : groups)
            {
                if(!collectedGroups.contains(group))
                {
                    collectedGroups.add(group);
                    collectGroups(collectedGroups, group.getGroups());
                }
            }
        }
    }


    private <T extends UserGroupModel> Set<T> filterGroups(Set<PrincipalGroupModel> groups, Class<T> clazz)
    {
        Set<T> filteredGroups = new HashSet<>();
        for(PrincipalGroupModel group : groups)
        {
            if(clazz.isInstance(group))
            {
                filteredGroups.add((T)group);
            }
        }
        return filteredGroups;
    }


    private <T extends UserGroupModel> Set<T> collectAndFilterGroups(Set<PrincipalGroupModel> groups, Class<T> clazz)
    {
        Set<PrincipalGroupModel> allGroups = new HashSet<>();
        collectGroups(allGroups, groups);
        return filterGroups(allGroups, clazz);
    }


    @Required
    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }


    @Required
    public void setUserGroupDao(UserGroupDao userGroupDao)
    {
        this.userGroupDao = userGroupDao;
    }


    @Required
    public void setTitleDao(TitleDao titleDao)
    {
        this.titleDao = titleDao;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    @Required
    public void setPasswordEncoderService(PasswordEncoderService passwordEncoderService)
    {
        this.passwordEncoderService = passwordEncoderService;
    }


    public String getDefaultPasswordEncoding()
    {
        return this.defaultPasswordEncoding;
    }


    public void setDefaultPasswordEncoding(String defaultPasswordEncoding)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(defaultPasswordEncoding), "default password encoding cannot be null or empty");
        this.defaultPasswordEncoding = defaultPasswordEncoding;
    }


    public void setPassword(UserModel user, String plainPassword) throws PasswordEncoderNotFoundException
    {
        setPassword(user, plainPassword, user.getPasswordEncoding());
    }


    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService)
    {
        this.passwordPolicyService = passwordPolicyService;
    }


    @Required
    public void setUserAuditDao(UserAuditDao userAuditDao)
    {
        this.userAuditDao = userAuditDao;
    }
}
