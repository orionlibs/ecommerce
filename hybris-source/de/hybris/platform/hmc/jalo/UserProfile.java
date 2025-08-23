package de.hybris.platform.hmc.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class UserProfile extends GeneratedUserProfile
{
    private static final Logger log = Logger.getLogger(UserProfile.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    public Collection<Language> getAllReadableLanguages(SessionContext ctx)
    {
        Set<Language> readableLanguages = new HashSet<>();
        User user = (User)getOwner();
        if(user.isAdmin())
        {
            return ServicelayerManager.getInstance().getAllLanguages();
        }
        for(PrincipalGroup group : user.getAllGroups())
        {
            Collection<Language> languages = ((UserGroup)group).getReadableLanguages();
            readableLanguages.addAll(languages);
        }
        if(readableLanguages.isEmpty())
        {
            readableLanguages = ServicelayerManager.getInstance().getAllLanguages();
        }
        return readableLanguages;
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<Language> getAllWritableLanguages(SessionContext ctx)
    {
        Set<Language> writableLanguages = new HashSet<>();
        User user = (User)getOwner();
        if(user.isAdmin())
        {
            return ServicelayerManager.getInstance().getAllLanguages();
        }
        for(PrincipalGroup group : user.getAllGroups())
        {
            if(!((UserGroup)group).isDenyWritePermissionForAllLanguagesAsPrimitive())
            {
                Collection<Language> languages = ((UserGroup)group).getWriteableLanguages();
                writableLanguages.addAll(languages);
            }
        }
        if(writableLanguages.isEmpty() && !allGroupsAreDenyingWriteRights(user))
        {
            writableLanguages = (Set<Language>)getAllReadableLanguages(ctx);
        }
        return writableLanguages;
    }


    protected boolean allGroupsAreDenyingWriteRights(User user)
    {
        return user.getGroups().stream()
                        .allMatch(principalGroup -> ((UserGroup)principalGroup).isDenyWritePermissionForAllLanguagesAsPrimitive());
    }


    public boolean isLanguageSelected()
    {
        return (getReadableLanguages().size() != 0 || getWritableLanguages().size() != 0);
    }


    @ForceJALO(reason = "something else")
    public List<Language> getReadableLanguages(SessionContext ctx)
    {
        if((ConfigConstants.getInstance()).USERPROFILE_ACTIVE)
        {
            return super.getReadableLanguages(ctx);
        }
        return new LinkedList<>(getAllReadableLanguages(ctx));
    }


    @ForceJALO(reason = "something else")
    public List<Language> getWritableLanguages(SessionContext ctx)
    {
        if((ConfigConstants.getInstance()).USERPROFILE_ACTIVE)
        {
            return super.getWritableLanguages(ctx);
        }
        return new LinkedList<>(getAllWritableLanguages(ctx));
    }


    @ForceJALO(reason = "something else")
    public Boolean isExpandInitial(SessionContext ctx)
    {
        if((ConfigConstants.getInstance()).USERPROFILE_ACTIVE)
        {
            return super.isExpandInitial(ctx);
        }
        return Boolean.FALSE;
    }
}
