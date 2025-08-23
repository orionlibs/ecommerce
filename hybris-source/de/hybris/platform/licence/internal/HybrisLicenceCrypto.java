package de.hybris.platform.licence.internal;

import de.hybris.platform.licence.Licence;
import de.hybris.platform.util.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.apache.log4j.Logger;

public class HybrisLicenceCrypto
{
    private static final Logger LOG = Logger.getLogger(HybrisLicenceCrypto.class);
    private static final KeyFactory KEY_FACTORY;

    static
    {
        try
        {
            KEY_FACTORY = KeyFactory.getInstance("DSA");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException("DSA algorithm not available for checking licences : " + e.toString());
        }
    }

    public boolean verifyAgainstPublicKey(Licence licence)
    {
        try
        {
            Signature sig = Signature.getInstance("DSA");
            sig.initVerify(getPublicKey());
            String text = licence.toString();
            sig.update(text.getBytes());
            boolean verifyResult = sig.verify(licence.getSignature());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Checking licence ...\ntext = " + text + "\nsig = " + licence.getSignature());
            }
            return verifyResult;
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("DSA algorithm not available for checking licences : " + e.toString());
        }
        catch(InvalidKeyException e)
        {
            throw new IllegalStateException("wrong key as hybris platform public key : " + e.toString());
        }
        catch(SignatureException e)
        {
            throw new IllegalStateException("wrong licence signature : " + e.toString());
        }
        catch(InvalidKeySpecException e)
        {
            throw new IllegalStateException("wrong key spec for hybris platform public key (X509 required): " + e.toString());
        }
    }


    private PublicKey getPublicKey() throws InvalidKeySpecException
    {
        return KEY_FACTORY.generatePublic(new X509EncodedKeySpec(
                        Base64.decode("MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAO7nE4YWou9YiGdMAm+6M7bH1vKSdEuSsxXkH+EPa1hqr5nSQd8PbCLOlpYRz3x1gisDyhDEWdtTxXqDpodN6N5dyEs2H837A2sviaBruu3NUrdFO2mPfKAyHegi82Pn77KZjDJAlpD6zfoEYp8skR7hyH93yjDZBCFIvFjSZvJY=")));
    }
}
