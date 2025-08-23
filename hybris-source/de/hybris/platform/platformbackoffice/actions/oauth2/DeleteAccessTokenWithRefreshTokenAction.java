package de.hybris.platform.platformbackoffice.actions.oauth2;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthTokenService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;

public class DeleteAccessTokenWithRefreshTokenAction implements CockpitAction<Object, String>
{
    @Resource
    private OAuthTokenService oaAuthTokenService;


    public ActionResult<String> perform(ActionContext<Object> ctx)
    {
        Collection<Object> ctxObjects = new ArrayList();
        if(ctx.getData() instanceof Collection)
        {
            ctxObjects.addAll((Collection)ctx.getData());
        }
        else
        {
            ctxObjects.add(ctx.getData());
        }
        for(Object entityToRemove : ctxObjects)
        {
            Optional<OAuthAccessTokenModel> token = Optional.ofNullable((OAuthAccessTokenModel)entityToRemove);
            Objects.requireNonNull(this.oaAuthTokenService);
            token.map(OAuthAccessTokenModel::getRefreshToken).map(OAuthRefreshTokenModel::getTokenId).ifPresent(this.oaAuthTokenService::removeRefreshToken);
            Objects.requireNonNull(this.oaAuthTokenService);
            token.map(OAuthAccessTokenModel::getTokenId).ifPresent(this.oaAuthTokenService::removeAccessToken);
        }
        ActionResult<String> result = new ActionResult("success");
        result.setStatusFlags(EnumSet.of(ActionResult.StatusFlag.OBJECT_DELETED));
        return result;
    }


    public boolean canPerform(ActionContext<Object> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Object> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<Object> ctx)
    {
        return ctx.getLabel("label.action.confirmremove.oauthtoken");
    }


    protected OAuthTokenService getOaAuthTokenService()
    {
        return this.oaAuthTokenService;
    }
}
