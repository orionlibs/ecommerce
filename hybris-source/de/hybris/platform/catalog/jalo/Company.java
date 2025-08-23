package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.UserGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public class Company extends GeneratedCompany
{
    private static final Logger LOG = Logger.getLogger(Company.class.getName());
    @Deprecated(since = "ages", forRemoval = false)
    public static final String SHIPPINGADDRESSES = "shippingAddresses";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String UNLOADINGADDRESSES = "unloadingAddresses";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String BILLINGADDRESSES = "billingAddresses";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CONTACTADDRESSES = "contactAddresses";


    public Collection<Catalog> getCatalogsAsBuyer()
    {
        return getPurchasedCatalogs();
    }


    public Collection<Catalog> getCatalogsAsSupplier()
    {
        return getProvidedCatalogs();
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<Address> getAddresses(SessionContext ctx)
    {
        HashMap<Object, Object> attributes = new HashMap<>();
        attributes.put("me", this);
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                        getSession().getTypeManager().getComposedType(Address.class).getCode() + "} WHERE {" + Item.OWNER + "} = ?me ", attributes,
                                        Collections.singletonList(Address.class), true, true, 0, -1)
                        .getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setAddresses(SessionContext ctx, Collection<Address> addresses)
    {
        Collection<Address> toRemove = new ArrayList<>(getAddresses(ctx));
        if(addresses != null)
        {
            toRemove.removeAll(addresses);
        }
        for(Iterator<Address> it = toRemove.iterator(); it.hasNext(); )
        {
            try
            {
                Address address = it.next();
                if(equals(address.getOwner()))
                {
                    address.remove(ctx);
                    continue;
                }
                LOG.warn("Skipped deletion of address: " + address + "! Because its owner was not: " + getLocName());
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    @SLDSafe(portingClass = "CompanyShippingAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getShippingAddresses()
    {
        return getShippingAddresses(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "CompanyShippingAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getShippingAddresses(SessionContext ctx)
    {
        Collection<Address> shippingAddresses = getAllAddresses((UserGroup)this);
        for(Iterator<Address> it = shippingAddresses.iterator(); it.hasNext(); )
        {
            Address address = it.next();
            if(!CatalogManager.getInstance().isShippingAddressAsPrimitive(address))
            {
                it.remove();
            }
        }
        return shippingAddresses;
    }


    private Collection<Address> getAllAddresses(UserGroup userGroup)
    {
        Collection<Address> addresses = new ArrayList<>();
        if(userGroup instanceof Company)
        {
            addresses.addAll(((Company)userGroup).getAddresses());
        }
        Set userGroups = userGroup.getGroups();
        if(userGroups != null)
        {
            for(Iterator<UserGroup> it = userGroups.iterator(); it.hasNext(); )
            {
                UserGroup group = it.next();
                addresses.addAll(getAllAddresses(group));
            }
        }
        return addresses;
    }


    @SLDSafe(portingClass = "CompanyUnloadingAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getUnloadingAddresses()
    {
        return getUnloadingAddresses(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "CompanyUnloadingAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getUnloadingAddresses(SessionContext ctx)
    {
        Collection<Address> unloadingAddresses = getAllAddresses((UserGroup)this);
        for(Iterator<Address> it = unloadingAddresses.iterator(); it.hasNext(); )
        {
            Address address = it.next();
            if(!CatalogManager.getInstance().isUnloadingAddressAsPrimitive(address))
            {
                it.remove();
            }
        }
        return unloadingAddresses;
    }


    @SLDSafe(portingClass = "CompanyBillingAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getBillingAddresses()
    {
        return getBillingAddresses(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "CompanyBillingAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getBillingAddresses(SessionContext ctx)
    {
        Collection<Address> billingAddresses = getAllAddresses((UserGroup)this);
        for(Iterator<Address> it = billingAddresses.iterator(); it.hasNext(); )
        {
            Address address = it.next();
            if(!CatalogManager.getInstance().isBillingAddressAsPrimitive(address))
            {
                it.remove();
            }
        }
        return billingAddresses;
    }


    @SLDSafe(portingClass = "CompanyContactAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getContactAddresses()
    {
        return getContactAddresses(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "CompanyContactAddressesAttributeHandler", portingMethod = "get(final CompanyModel model)")
    public Collection<Address> getContactAddresses(SessionContext ctx)
    {
        Collection<Address> contactAddresses = getAllAddresses((UserGroup)this);
        for(Iterator<Address> it = contactAddresses.iterator(); it.hasNext(); )
        {
            Address address = it.next();
            if(!CatalogManager.getInstance().isContactAddressAsPrimitive(address))
            {
                it.remove();
            }
        }
        return contactAddresses;
    }
}
