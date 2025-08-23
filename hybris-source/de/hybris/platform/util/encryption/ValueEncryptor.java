package de.hybris.platform.util.encryption;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.log4j.Logger;

public class ValueEncryptor
{
    private static final Logger LOG = Logger.getLogger(ValueEncryptor.class.getName());
    private final String defaultSymmetricEncryptionKeyID;
    private final String signature;
    private final Map<String, SecretKey> symmetricEncryptionKeyMap;
    private final ConfigIntf cfg;
    private static final SecureRandom random = new SecureRandom();


    public ValueEncryptor(ConfigIntf cfg, String provider, String signature, Map<String, SecretKey> keys, String algorithm)
    {
        this(cfg, provider, signature, keys, algorithm, "1");
    }


    public ValueEncryptor(ConfigIntf cfg, String provider, String signature, Map<String, SecretKey> keys, String algorithm, String defaultSymmetricEncryptionKeyID)
    {
        Preconditions.checkArgument((cfg != null), "cfg needs to be provided");
        this.cfg = cfg;
        this.defaultSymmetricEncryptionKeyID = defaultSymmetricEncryptionKeyID;
        checkProvider(provider);
        checkSignature(signature);
        this.signature = signature;
        checkAlgorithm(algorithm);
        initProvider(provider);
        ImmutableMap.Builder<String, SecretKey> builder = ImmutableMap.builder();
        if(keys == null || keys.isEmpty())
        {
            try
            {
                builder.put(defaultSymmetricEncryptionKeyID,
                                EncryptionUtil.loadKey("symmetric.key.file." + defaultSymmetricEncryptionKeyID));
            }
            catch(Exception e)
            {
                LOG.error("Error loading default encryption key", e);
            }
        }
        else
        {
            builder.putAll(keys);
        }
        this.symmetricEncryptionKeyMap = (Map<String, SecretKey>)builder.build();
        assureInitialized();
    }


    public ValueEncryptor(ConfigIntf cfg, String provider, String signature, String keyID, String algorithm)
    {
        this(cfg, provider, signature, Collections.emptyMap(), algorithm, keyID);
    }


    @Deprecated(since = "1811", forRemoval = true)
    public ValueEncryptor(ConfigIntf cfg, String provider, String signature, String key, String keyID, String algorithm)
    {
        this(cfg, provider, signature, Collections.EMPTY_MAP, algorithm, keyID);
    }


    protected ConfigIntf getConfig()
    {
        return this.cfg;
    }


    protected Cipher getCipher() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        return Cipher.getInstance(
                        getConfig().getParameter("symmetric.cipher"), this.signature);
    }


    protected void checkSignature(String signature)
    {
        if(signature == null)
        {
            throw new IllegalStateException("No property 'encryption.provider.signature' found, using signature = null ");
        }
    }


    protected void checkKey(String key)
    {
        if(key == null)
        {
            throw new IllegalStateException("No property 'symmetric.key.file', 'symmetric.key.1' or  'symmetric.key' found, using " + key);
        }
    }


    protected void checkAlgorithm(String algorithm)
    {
        if(algorithm == null)
        {
            throw new IllegalStateException("No property 'symmetric.algorithm' or 'symmetric.algorithm' found, using " + algorithm);
        }
    }


    protected void checkProvider(String provider)
    {
        if(provider == null)
        {
            throw new IllegalStateException("No property 'encryption.provider.class' found, using provider=null");
        }
    }


    private synchronized void initProvider(String provider)
    {
        try
        {
            if(Security.getProvider(provider) == null)
            {
                Class<?> clazz = Class.forName(provider);
                Security.insertProviderAt((Provider)clazz.newInstance(), 0);
                if(LOG.isDebugEnabled())
                {
                    dumpProviderInfo();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public String encrypt(String plaintext) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        return encrypt(null, plaintext);
    }


    private String encrypt(SecretKey key, String keyID, String plaintext) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        byte[] byteArray = new byte[16];
        random.nextBytes(byteArray);
        Cipher cipher = getCipher();
        IvParameterSpec spec = new IvParameterSpec(byteArray);
        cipher.init(1, key, spec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("utf-8"));
        String saltString = Base64.encodeBytes(byteArray, 8);
        String ciphertextString = Base64.encodeBytes(ciphertext, 8);
        StringBuilder stringBuilder = (new StringBuilder(keyID)).append(":").append(saltString).append(ciphertextString);
        return stringBuilder.toString();
    }


    private String encryptViaOldKey(SecretKey encryptionKey, String keyID, String plaintext)
                    throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        byte[] salt = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = getCipher();
        cipher.init(1, encryptionKey, paramSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("utf-8"));
        String saltString = Base64.encodeBytes(salt, 8);
        String ciphertextString = Base64.encodeBytes(ciphertext, 8);
        return keyID + ":" + keyID + saltString;
    }


    private String getPPV(String keyID)
    {
        return getConfig().getParameter("symmetric.key.file." + keyID);
    }


    private String encrypt(String keyID, String plaintext) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        if(keyID == null)
        {
            keyID = this.defaultSymmetricEncryptionKeyID;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Submitted keyID was 'null' - Using ID '" + this.defaultSymmetricEncryptionKeyID + "' instead!");
            }
        }
        String ppv = getPPV(keyID);
        if(ppv == null || !EncryptionUtil.isNewStyleKey(ppv))
        {
            return encryptViaOldKey(this.symmetricEncryptionKeyMap.get(keyID), keyID, plaintext);
        }
        return encrypt(this.symmetricEncryptionKeyMap.get(keyID), keyID, plaintext);
    }


    public String decrypt(String encrypted)
    {
        String decrypted = null;
        if(encrypted != null)
        {
            KeyIdAndValuePair pair = new KeyIdAndValuePair(this, encrypted);
            String keyFile = getPPV(pair.keyId);
            if(keyFile == null || !EncryptionUtil.isNewStyleKey(keyFile))
            {
                decrypted = decryptViaOldKey(this.symmetricEncryptionKeyMap.get(pair.keyId), pair.text);
            }
            else
            {
                decrypted = decrypt(pair.keyId, pair.text);
            }
        }
        return decrypted;
    }


    private String decryptViaOldKey(SecretKey key, String text)
    {
        if(text == null || key == null)
        {
            throw new InvalidParameterException("Submitted 'ciphertext', 'algorithmus' or 'password' was <null>!");
        }
        if(text.length() < 12)
        {
            throw new InvalidParameterException("Corrupted ciphertext! (too short)");
        }
        String salt = text.substring(0, 12);
        byte[] saltArray = Base64.decode(salt);
        if(saltArray == null)
        {
            throw new IllegalStateException("Corrupted ciphertext! (salt bytes not found)");
        }
        String ciphertext = text.substring(12, text.length());
        byte[] ciphertextArray = Base64.decode(ciphertext);
        if(ciphertextArray == null)
        {
            throw new IllegalStateException("Corrupted ciphertext! (text bytes not found)");
        }
        try
        {
            PBEParameterSpec paramSpec = new PBEParameterSpec(saltArray, 1000);
            Cipher cipher = getCipher();
            cipher.init(2, key, paramSpec);
            byte[] plaintextArray = cipher.doFinal(ciphertextArray);
            return new String(plaintextArray);
        }
        catch(Exception e)
        {
            throw new IllegalStateException("error in decryption", e);
        }
    }


    public String decrypt(SecretKey key, String text)
    {
        if(text.length() < 24)
        {
            throw new InvalidParameterException("Corrupted ciphertext! (too short)");
        }
        try
        {
            Cipher cipher = getCipher();
            String salt = text.substring(0, 24);
            byte[] saltArray = Base64.decode(salt);
            String ciphertext = text.substring(24, text.length());
            IvParameterSpec spec = new IvParameterSpec(saltArray);
            cipher.init(2, key, spec);
            byte[] ciphertextArray = Base64.decode(ciphertext);
            byte[] plaintextArray = cipher.doFinal(ciphertextArray);
            return new String(plaintextArray);
        }
        catch(Exception e)
        {
            throw new IllegalStateException("error in decryption", e);
        }
    }


    private String decrypt(String keyID, String text)
    {
        if(keyID == null)
        {
            keyID = this.defaultSymmetricEncryptionKeyID;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Submitted keyID was 'null' - Using ID '" + this.defaultSymmetricEncryptionKeyID + "' instead!");
            }
        }
        return decrypt(this.symmetricEncryptionKeyMap.get(keyID), text);
    }


    private void assureInitialized()
    {
        try
        {
            getCipher();
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Value encryptor not properly initialized");
        }
    }


    public static void dumpProviderInfo()
    {
        Provider[] providers = Security.getProviders();
        Set<String> ciphers = new HashSet();
        Set<String> keyAgreements = new HashSet();
        Set<String> macs = new HashSet();
        Set<String> messageDigests = new HashSet();
        Set<String> signatures = new HashSet();
        for(int i = 0; i != providers.length; i++)
        {
            Iterator<String> iterator = providers[i].keySet().iterator();
            while(iterator.hasNext())
            {
                String entry = iterator.next();
                if(entry.startsWith("Alg.Alias."))
                {
                    entry = entry.substring("Alg.Alias.".length());
                }
                if(entry.startsWith("Cipher."))
                {
                    ciphers.add(entry.substring("Cipher.".length()));
                    continue;
                }
                if(entry.startsWith("KeyAgreement."))
                {
                    keyAgreements.add(entry.substring("KeyAgreement.".length()));
                    continue;
                }
                if(entry.startsWith("Mac."))
                {
                    macs.add(entry.substring("Mac.".length()));
                    continue;
                }
                if(entry.startsWith("MessageDigest."))
                {
                    messageDigests.add(entry.substring("MessageDigest.".length()));
                    continue;
                }
                if(entry.startsWith("Signature."))
                {
                    signatures.add(entry.substring("Signature.".length()));
                }
            }
        }
        printSet("Ciphers", ciphers);
        printSet("KeyAgreements", keyAgreements);
        printSet("Macs", macs);
        printSet("MessageDigests", messageDigests);
        printSet("Signatures", signatures);
    }


    private static void printSet(String setName, Set algorithms)
    {
        LOG.info(setName + ":");
        if(algorithms.isEmpty())
        {
            LOG.info("\t\tNone available.");
        }
        else
        {
            Iterator<String> iterator = algorithms.iterator();
            while(iterator.hasNext())
            {
                String name = iterator.next();
                LOG.info("\t\t" + name);
            }
        }
    }
}
