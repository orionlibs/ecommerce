package de.hybris.platform.couponservices.couponcodegeneration.impl;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import org.apache.log4j.Logger;

public abstract class AbstractCouponCodeGenerationStrategy
{
    private static final Logger LOG = Logger.getLogger(AbstractCouponCodeGenerationStrategy.class);


    protected String createTwoCharactersFromByte(int value, int offset, String alphabet)
    {
        return pickCharacter(value >> 4, offset, alphabet).concat(pickCharacter(value & 0xF, offset + 4, alphabet));
    }


    protected String pickCharacter(int value, int offset, String alphabet)
    {
        int alphabetIndex = (value + offset) % alphabet.length();
        return alphabet.substring(alphabetIndex, alphabetIndex + 1);
    }


    protected int createIntFromTwoCharactersString(String value, int offset, String alphabet)
    {
        return (pickInt(value.charAt(0), offset, alphabet) << 4) + pickInt(value.charAt(1), offset + 4, alphabet);
    }


    protected int pickInt(char value, int offset, String alphabet)
    {
        int position = alphabet.indexOf(value);
        if(position == -1)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Invalid character given: '" + value + "' is not in current coupon alphabet:" + alphabet);
            }
            throw new SystemException("Invalid character given: " + value + "'' is not in current coupon alphabet.");
        }
        position -= offset;
        while(position < 0)
        {
            position += alphabet.length();
        }
        return position % alphabet.length();
    }
}
