package de.hybris.platform.servicelayer.user.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.ContactInfoService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DefaultContactInfoService implements ContactInfoService
{
    public AbstractContactInfoModel getMainContactInfo(UserModel user)
    {
        Preconditions.checkNotNull(user);
        Collection<AbstractContactInfoModel> contactInfos = user.getContactInfos();
        return (contactInfos == null) ? null : (AbstractContactInfoModel)Iterables.getFirst(contactInfos, null);
    }


    public void setMainContactInfo(UserModel user, AbstractContactInfoModel contactInfo)
    {
        List<AbstractContactInfoModel> newContactInfos;
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(contactInfo);
        Collection<AbstractContactInfoModel> currentContactInfos = user.getContactInfos();
        if(currentContactInfos == null)
        {
            newContactInfos = Lists.newArrayList((Object[])new AbstractContactInfoModel[] {contactInfo});
        }
        else
        {
            newContactInfos = Lists.newArrayList(currentContactInfos);
            if(newContactInfos.contains(contactInfo))
            {
                newContactInfos.remove(newContactInfos.indexOf(contactInfo));
            }
            newContactInfos.add(0, contactInfo);
        }
        contactInfo.setUser(user);
        user.setContactInfos(newContactInfos);
    }


    public void addContactInfos(UserModel user, AbstractContactInfoModel... contactInfos)
    {
        List<AbstractContactInfoModel> newContactInfos;
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(contactInfos);
        Collection<AbstractContactInfoModel> currentContactInfos = user.getContactInfos();
        if(currentContactInfos == null)
        {
            newContactInfos = Lists.newArrayList((Object[])contactInfos);
        }
        else
        {
            newContactInfos = Lists.newArrayList(currentContactInfos);
            newContactInfos.addAll(Arrays.asList(contactInfos));
        }
        for(AbstractContactInfoModel contactInfo : newContactInfos)
        {
            contactInfo.setUser(user);
        }
        user.setContactInfos(newContactInfos);
    }


    public void removeContactInfos(UserModel user, AbstractContactInfoModel... contactInfos)
    {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(contactInfos);
        Collection<AbstractContactInfoModel> currentContactInfos = user.getContactInfos();
        if(currentContactInfos != null)
        {
            List<AbstractContactInfoModel> newContactInfos = Lists.newArrayList(currentContactInfos);
            newContactInfos.removeAll(Arrays.asList((Object[])contactInfos));
            user.setContactInfos(newContactInfos);
        }
    }
}
