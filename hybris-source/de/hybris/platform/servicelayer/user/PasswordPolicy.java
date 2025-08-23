package de.hybris.platform.servicelayer.user;

import de.hybris.platform.core.model.user.UserModel;
import java.util.List;

public interface PasswordPolicy
{
    List<PasswordPolicyViolation> verifyPassword(UserModel paramUserModel, String paramString1, String paramString2);


    String getPolicyName();
}
