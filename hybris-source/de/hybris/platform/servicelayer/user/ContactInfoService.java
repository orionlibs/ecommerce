package de.hybris.platform.servicelayer.user;

import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import javax.annotation.Nullable;

public interface ContactInfoService
{
    @Nullable
    AbstractContactInfoModel getMainContactInfo(UserModel paramUserModel);


    void setMainContactInfo(UserModel paramUserModel, AbstractContactInfoModel paramAbstractContactInfoModel);


    void addContactInfos(UserModel paramUserModel, AbstractContactInfoModel... paramVarArgs);


    void removeContactInfos(UserModel paramUserModel, AbstractContactInfoModel... paramVarArgs);
}
