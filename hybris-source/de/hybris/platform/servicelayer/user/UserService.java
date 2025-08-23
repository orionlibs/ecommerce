package de.hybris.platform.servicelayer.user;

import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.model.user.UserPasswordChangeAuditModel;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserService
{
    @Deprecated(since = "6.1.0", forRemoval = true)
    UserModel getUser(String paramString);


    UserModel getUserForUID(String paramString);


    <T extends UserModel> T getUserForUID(String paramString, Class<T> paramClass);


    boolean isUserExisting(String paramString);


    @Deprecated(since = "6.1.0", forRemoval = true)
    UserGroupModel getUserGroup(String paramString);


    UserGroupModel getUserGroupForUID(String paramString);


    <T extends UserGroupModel> T getUserGroupForUID(String paramString, Class<T> paramClass);


    Set<UserGroupModel> getAllUserGroupsForUser(UserModel paramUserModel);


    <T extends UserGroupModel> Set<T> getAllUserGroupsForUser(UserModel paramUserModel, Class<T> paramClass);


    List<AbstractUserAuditModel> getUserAudits(UserModel paramUserModel);


    boolean isPasswordIdenticalToAudited(UserModel paramUserModel, String paramString, UserPasswordChangeAuditModel paramUserPasswordChangeAuditModel);


    <T extends UserGroupModel> Set<T> getAllUserGroupsForUserGroup(UserGroupModel paramUserGroupModel, Class<T> paramClass);


    boolean isMemberOfGroup(UserModel paramUserModel, UserGroupModel paramUserGroupModel);


    boolean isMemberOfGroup(UserModel paramUserModel, UserGroupModel paramUserGroupModel, boolean paramBoolean);


    boolean isMemberOfGroup(UserGroupModel paramUserGroupModel1, UserGroupModel paramUserGroupModel2);


    boolean isMemberOfGroup(UserGroupModel paramUserGroupModel1, UserGroupModel paramUserGroupModel2, boolean paramBoolean);


    Collection<TitleModel> getAllTitles();


    TitleModel getTitleForCode(String paramString);


    EmployeeModel getAdminUser();


    UserGroupModel getAdminUserGroup();


    boolean isAdmin(UserModel paramUserModel);


    boolean isAdminGroup(UserGroupModel paramUserGroupModel);


    boolean isAdminEmployee(UserModel paramUserModel);


    CustomerModel getAnonymousUser();


    boolean isAnonymousUser(UserModel paramUserModel);


    UserModel getCurrentUser();


    void setCurrentUser(UserModel paramUserModel);


    String getPassword(String paramString) throws CannotDecodePasswordException, PasswordEncoderNotFoundException;


    void setPassword(String paramString1, String paramString2) throws PasswordEncoderNotFoundException;


    void setPassword(String paramString1, String paramString2, String paramString3) throws PasswordEncoderNotFoundException;


    void setPassword(UserModel paramUserModel, String paramString1, String paramString2) throws PasswordEncoderNotFoundException;


    void setPassword(UserModel paramUserModel, String paramString) throws PasswordEncoderNotFoundException;


    void setPasswordWithDefaultEncoding(UserModel paramUserModel, String paramString) throws PasswordEncoderNotFoundException;


    String getPassword(UserModel paramUserModel) throws CannotDecodePasswordException, PasswordEncoderNotFoundException;


    void setEncodedPassword(UserModel paramUserModel, String paramString);


    void setEncodedPassword(UserModel paramUserModel, String paramString1, String paramString2);
}
