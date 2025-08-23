package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.user.UserGroupModel;

public class UserGroupModelBuilder
{
    private final UserGroupModel model = new UserGroupModel();


    private UserGroupModel getModel()
    {
        return this.model;
    }


    public static UserGroupModelBuilder aModel()
    {
        return new UserGroupModelBuilder();
    }


    public UserGroupModel build()
    {
        return getModel();
    }


    public UserGroupModelBuilder withUid(String uid)
    {
        getModel().setUid(uid);
        return this;
    }
}
