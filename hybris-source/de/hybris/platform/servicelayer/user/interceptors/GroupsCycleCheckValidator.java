package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.HashSet;
import java.util.Set;

public class GroupsCycleCheckValidator implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof PrincipalGroupModel)
        {
            PrincipalGroupModel principalGroup = (PrincipalGroupModel)model;
            if(ctx.isModified(principalGroup, "groups"))
            {
                checkGroups(principalGroup);
            }
            if(ctx.isModified(principalGroup, "members"))
            {
                checkMembers(principalGroup);
            }
        }
    }


    private void checkGroups(PrincipalGroupModel principalGroup) throws InterceptorException
    {
        if(principalGroup.getGroups() != null)
        {
            for(PrincipalGroupModel group : principalGroup.getGroups())
            {
                Set<PrincipalGroupModel> allGroups = getAllGroups((PrincipalModel)group);
                if(allGroups.contains(principalGroup))
                {
                    throw new InterceptorException("Cycle in groups was detected!");
                }
            }
        }
    }


    private void checkMembers(PrincipalGroupModel principalGroup) throws InterceptorException
    {
        if(principalGroup.getMembers() != null)
        {
            for(PrincipalModel principal : principalGroup.getMembers())
            {
                if(principal instanceof PrincipalGroupModel)
                {
                    Set<PrincipalGroupModel> allGroups = getAllGroups((PrincipalModel)principalGroup);
                    if(allGroups.contains(principal))
                    {
                        throw new InterceptorException("Cycle in members was detected!");
                    }
                }
            }
        }
    }


    private Set<PrincipalGroupModel> getAllGroups(PrincipalModel principal)
    {
        Set<PrincipalGroupModel> allGroups = new HashSet<>();
        addGroups(allGroups, principal.getGroups());
        return allGroups;
    }


    private void addGroups(Set<PrincipalGroupModel> allGroups, Set<PrincipalGroupModel> groups)
    {
        if(groups != null)
        {
            for(PrincipalGroupModel group : groups)
            {
                if(!allGroups.contains(group))
                {
                    allGroups.add(group);
                    addGroups(allGroups, group.getGroups());
                }
            }
        }
    }
}
