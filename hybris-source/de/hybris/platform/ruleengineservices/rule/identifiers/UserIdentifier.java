package de.hybris.platform.ruleengineservices.rule.identifiers;

import de.hybris.platform.core.model.user.UserModel;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class UserIdentifier implements Function<UserModel, String>
{
    private boolean usePk;


    public String apply(UserModel userModel)
    {
        return isUsePk() ? userModel.getPk().toString() : userModel.getUid();
    }


    protected boolean isUsePk()
    {
        return this.usePk;
    }


    @Required
    public void setUsePk(boolean usePk)
    {
        this.usePk = usePk;
    }
}
