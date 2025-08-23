package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UserPasswordTranslator extends AbstractSpecialValueTranslator
{
    private Set<String> supportedEncodings;


    public UserPasswordTranslator()
    {
    }


    public UserPasswordTranslator(Set<String> encodings)
    {
        this.supportedEncodings = encodings;
    }


    public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.init(columnDescriptor);
        ComposedType composedType = columnDescriptor.getHeader().getConfiguredComposedType();
        if(!TypeManager.getInstance().getComposedType(User.class).isAssignableFrom((Type)composedType))
        {
            throw new HeaderValidationException("UserPasswordTranslator is only applicable for type User and its subtypes - got " + composedType
                            .getCode(), 0);
        }
        try
        {
            this.supportedEncodings = new HashSet<>();
            for(Iterator<String> iter = composedType.getTenant().getJaloConnection().getPasswordEncoderFactory().getSupportedEncodings().iterator(); iter.hasNext(); )
            {
                String enc = iter.next();
                this.supportedEncodings.add(enc.toLowerCase());
            }
        }
        catch(Exception e)
        {
            throw new HeaderValidationException(e.getMessage(), 74);
        }
    }


    public String performExport(Item item) throws ImpExException
    {
        User user = (User)item;
        String enc = user.getPasswordEncoding();
        String password = user.getEncodedPassword();
        return ((enc != null && enc.length() > 0) ? (enc + ":") : "") + ((enc != null && enc.length() > 0) ? (enc + ":") : "");
    }


    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        if(cellValue != null && !cellValue.trim().startsWith("<ignore>") && processedItem != null && processedItem
                        .isAlive() && processedItem instanceof User)
        {
            User user = (User)processedItem;
            if(cellValue.length() > 0)
            {
                int pos = cellValue.indexOf(':');
                String enc = (pos != -1) ? cellValue.substring(0, pos).trim() : null;
                if("".equals(enc))
                {
                    enc = null;
                }
                String password = (pos != -1) ? cellValue.substring(pos + 1).trim() : cellValue;
                if("".equals(password))
                {
                    password = null;
                }
                if(enc != null && !this.supportedEncodings.contains(enc.toLowerCase()) && !canInstantiateEncoding(enc))
                {
                    throw new UnresolvedValueException("unknown password encoding '" + enc + "' - current system only supports " + this.supportedEncodings);
                }
                if(enc != null)
                {
                    user.setEncodedPassword(password, enc);
                }
                else
                {
                    user.setEncodedPassword(password);
                }
            }
            else
            {
                user.setEncodedPassword(null);
            }
        }
    }


    private boolean canInstantiateEncoding(String encoder)
    {
        try
        {
            Class<?> clazz = Class.forName(encoder);
            if(!(clazz.newInstance() instanceof de.hybris.platform.persistence.security.PasswordEncoder))
            {
                return false;
            }
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
}
