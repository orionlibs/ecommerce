package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.persistence.security.PasswordEncoderFactory;

public class ConvertPlaintextToEncodedUserPasswordTranslator extends UserPasswordTranslator
{
    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        if(cellValue != null && !cellValue.trim().startsWith("<ignore>") && processedItem != null && processedItem
                        .isAlive() && processedItem instanceof User)
        {
            User user = (User)processedItem;
            if(cellValue.length() > 0)
            {
                int pos = cellValue.indexOf(':');
                String encoding = (pos != -1) ? cellValue.substring(0, pos).trim() : null;
                if("".equals(encoding))
                {
                    encoding = "*";
                }
                String password = (pos != -1) ? cellValue.substring(pos + 1).trim() : cellValue;
                if("".equals(password))
                {
                    password = null;
                }
                PasswordEncoderFactory factory = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoderFactory();
                user.setEncodedPassword(factory.getEncoder(encoding).encode(user.getUID(), password), encoding);
            }
            else
            {
                user.setEncodedPassword(null);
            }
        }
    }
}
