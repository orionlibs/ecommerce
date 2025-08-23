package de.hybris.platform.util.encryption;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.SystemConfig;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.SecurityUtils;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(EncryptionUtil.class);
    private static final Item.AttributeFilter ENCRYPTED_ATTR_FILTER;
    public static final String SUBFOLDER = "security";
    public static final String PROVIDER_CLASS = "org.bouncycastle.jce.provider.BouncyCastleProvider";
    public static final String DEFAULT_KEYFILE_NAME = "default-128-bit-aes-key.hybris";
    private static final String SYMMETRIC_KEY_FILE_SEARCH_PATTERN = "symmetric.key.file.*";
    private static final String SYMMETRIC_KEY_FILE_SUBSTRING = "symmetric.key.file.";
    private static final int ITERATIONS = 1000;

    static
    {
        ENCRYPTED_ATTR_FILTER = (attributeDescriptor -> (!attributeDescriptor.isInitial() && attributeDescriptor.isEncrypted()));
    }

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static Map<String, String> keyfiles;


    public static void setKeyfiles(Map<String, String> keyfiles)
    {
        EncryptionUtil.keyfiles = keyfiles;
    }


    static
    {
        try
        {
            Class<?> clazz = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Security.insertProviderAt((Provider)clazz.newInstance(), 0);
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage());
            LOG.debug(e.getMessage(), e);
        }
    }

    public static String getDefaultKeyFileName()
    {
        String configuredId = getDefaultKeyFileId();
        String foundFileName = getConfig().getParameter("symmetric.key.file." + configuredId);
        return StringUtils.isEmpty(foundFileName) ? "default-128-bit-aes-key.hybris" : foundFileName;
    }


    public static String getDefaultKeyFileId()
    {
        String foundId = getConfig().getParameter("symmetric.key.file.default");
        return StringUtils.isEmpty(foundId) ? "1" : foundId;
    }


    protected static ConfigIntf getConfig()
    {
        return Registry.getMasterTenant().getConfig();
    }


    public static String getOldKeyFile()
    {
        return getConfig().getParameter("symmetric.key.file");
    }


    public static Map<String, SecretKey> getSecretKeysFromConfig()
    {
        LOG.debug("Building encryption key map : id -> key file");
        Map<String, SecretKey> result = mapConfiguration((key, value) -> loadKeySafely(key));
        return result.isEmpty() ? fallbackToDefaultKeys() : result;
    }


    private static SecretKey loadKeySafely(String keyID)
    {
        try
        {
            return loadKey(keyID);
        }
        catch(GeneralSecurityException | IOException e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }


    private static Map<String, SecretKey> fallbackToDefaultKeys()
    {
        LOG.debug("No 'new style' encryption key settings like 'symmetric.key.file.1' found!");
        LOG.debug("Searching for 'old style' encryption key settings ('symmetric.key.file')");
        Map<String, SecretKey> result = new TreeMap<>();
        ConfigIntf cfg = getConfig();
        String oldkeyfile = cfg.getParameter("symmetric.key.file");
        try
        {
            if(oldkeyfile == null)
            {
                String defaultKeyFileName = getDefaultKeyFileName();
                LOG.debug("no project.properties entry for encryption key file found! using default keyfile: {}", defaultKeyFileName);
                cfg.setParameter("symmetric.key.file.1", defaultKeyFileName);
                result.put("1", loadKey(defaultKeyFileName));
            }
            else
            {
                LOG.debug("'old style' encryption key settings found!");
                result.put("1", loadKey("symmetric.key.file"));
            }
        }
        catch(GeneralSecurityException | IOException e)
        {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }


    public static Map<String, String> getConfiguredEncryptionKeys()
    {
        if(keyfiles == null)
        {
            keyfiles = mapConfiguration((key, value) -> value);
        }
        return keyfiles;
    }


    public static boolean isConfiguredMigrationKey()
    {
        Map<String, String> configuredKeys = mapConfiguration((key, value) -> value);
        return (configuredKeys.size() != 1);
    }


    private static <T> Map<String, T> mapConfiguration(BiFunction<String, String, T> valueMapper)
    {
        ConfigIntf cfg = getConfig();
        Map<String, String> foundConfig = cfg.getParametersMatching("symmetric.key.file.*");
        Map<String, T> result = new TreeMap<>();
        for(Map.Entry<String, String> entry : foundConfig.entrySet())
        {
            String propertyKey = entry.getKey();
            String propertyValue = entry.getValue();
            if(!"symmetric.key.file.default".equalsIgnoreCase(propertyKey))
            {
                String keyNum = StringUtils.substringAfter(propertyKey, "symmetric.key.file.");
                T value = valueMapper.apply(propertyKey, propertyValue);
                if(StringUtils.isNotBlank(keyNum) && value != null)
                {
                    result.put(keyNum, value);
                }
            }
        }
        return result;
    }


    @Deprecated(since = "1905", forRemoval = true)
    public Map<String, String> getConfiguredEncryptionKey()
    {
        return getConfiguredEncryptionKeys();
    }


    public void migrate(String type, String attribute)
    {
        migrate(type, attribute, null);
    }


    public static void migrate(String type, String qualifier, JspContext jspc)
    {
        if(type == null || qualifier == null || type.equals("") || qualifier.equals(""))
        {
            if(jspc != null)
            {
                jspc.print("<b><font color=\"red\">No type for migration selected!!</font></b>");
            }
            return;
        }
        LOG.info(">>> {} // {} - Encryption Key Migration running ... <<<", type, qualifier);
        if(jspc != null)
        {
            jspc.print(type + " // " + type + " - Encryption Key Migration running ...<br>");
        }
        int total = getAllInstances(type, qualifier).size();
        if(total < 1)
        {
            LOG.warn(">>> {}/{} - Encryption Key Migration finished (NOTHING TO DO HERE) <<<", type, qualifier);
            return;
        }
        long start = System.currentTimeMillis();
        int count = 1;
        for(List<Long> res : getAllInstances(type, qualifier))
        {
            PK pk = PK.fromLong(((Long)res.get(0)).longValue());
            Item item = JaloSession.getCurrentSession().getItem(pk);
            if(item.isAlive())
            {
                LOG.debug("{}/{} - Migrating PK: {}", new Object[] {Integer.valueOf(count++), Integer.valueOf(total), pk});
                try
                {
                    LOG.debug("migrating encryption key: {} ( + {}/{} )", new Object[] {pk, type, qualifier});
                    item.setAttribute(qualifier, item.getAttribute(qualifier));
                }
                catch(Exception e)
                {
                    LOG.error("migrating: {}\n {}", item, e.getMessage());
                    LOG.debug(e.getMessage(), e);
                }
                continue;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("skipping instance: {}", item);
            }
        }
        LOG.info(">>> {}/{} - Encryption Key Migration finished <<<", type, qualifier);
        long end = System.currentTimeMillis() - start;
        LOG.info("Time: {} m/s for '{}' steps -- {} ms/step", new Object[] {Long.valueOf(end), Integer.valueOf(total), Long.valueOf(end / total)});
        if(jspc != null)
        {
            jspc.print("... finished <br>");
            jspc.print("Time: " + end + " m/s for '" + total + "' steps -- " + end / total + " ms/step <br> <br/>DONE");
        }
    }


    public static Map<String, Integer> getUsedKeys(String type, String qualifier)
    {
        Map<String, Integer> keys = new TreeMap<>();
        for(List<Long> res : getAllInstances(type, qualifier))
        {
            PK pk = PK.fromLong(((Long)res.get(0)).longValue());
            Item item = JaloSession.getCurrentSession().getItem(pk);
            String value = (String)res.get(1);
            if(item.isAlive())
            {
                if(value != null)
                {
                    int index = value.indexOf(":");
                    if(index > 0)
                    {
                        String id = value.substring(0, index);
                        if(keys.containsKey(id))
                        {
                            Integer integer = keys.get(id);
                            keys.put(id, Integer.valueOf(integer.intValue() + 1));
                            continue;
                        }
                        keys.put(id, Integer.valueOf(1));
                    }
                }
            }
        }
        return keys;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public Map<String, Integer> getUsedKeys(String type, String qualifier, JspContext jspc)
    {
        return getUsedKeys(type, qualifier);
    }


    private static List<List> getAllInstances(String type, String attribute)
    {
        return FlexibleSearch.getInstance().search("SELECT {PK}, {" + attribute + "} FROM {" + type + "}", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {Long.class, String.class}, ), true, true, 0, -1)
                        .getResult();
    }


    public static Map<ComposedType, List<AttributeDescriptor>> getAllTypesWhichHasEncyrptedAttributes(Predicate<ComposedType> typeFilter)
    {
        Map<Object, Object> cryptedTypes = new TreeMap<>();
        for(ComposedType type : TypeManager.getInstance()
                        .getAllComposedTypes()
                        .stream()
                        .filter(typeFilter)
                        .collect(Collectors.toSet()))
        {
            List<AttributeDescriptor> encryptedAttributes = new ArrayList<>();
            try
            {
                for(AttributeDescriptor ad : type.getAttributeDescriptors())
                {
                    if(ENCRYPTED_ATTR_FILTER.processAttribute(ad))
                    {
                        encryptedAttributes.add(ad);
                    }
                }
                if(!encryptedAttributes.isEmpty())
                {
                    cryptedTypes.put(type, encryptedAttributes);
                }
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return (Map)cryptedTypes;
    }


    public static Map<ComposedType, List<AttributeDescriptor>> getAllTypesWhichHasEncyrptedAttributes()
    {
        Predicate<ComposedType> allTypesFilter = type -> true;
        return getAllTypesWhichHasEncyrptedAttributes(allTypesFilter);
    }


    public static boolean generateAESKey(String keyfile, int keysize) throws GeneralSecurityException, IOException
    {
        ConfigIntf cfg = getConfig();
        return createKey(keyfile, keysize, cfg
                        .getParameter("symmetric.cipher"), cfg
                        .getParameter("symmetric.algorithm"), cfg
                        .getParameter("symmetric.key.master.password").toCharArray());
    }


    public static boolean createKey(String filename, int keysize, String cipherName, String cipherAlgorithm, char[] password) throws GeneralSecurityException, IOException
    {
        if(SecurityUtils.isPathEscapingDirectory(Paths.get(filename, new String[0])))
        {
            LOG.warn("Provided filename '{}' is not valid filename for key generation process.", filename);
            return false;
        }
        Path securityConfigDir = getSecurityConfigDir().toAbsolutePath();
        if(!securityConfigDir.toFile().exists())
        {
            securityConfigDir.toFile().mkdir();
        }
        Path keyFile = securityConfigDir.resolve(filename);
        if(keyFile.toFile().exists())
        {
            LOG.warn("Key file '{}' already exists! skipping key generation process!", keyFile);
            return false;
        }
        keyFile.toFile().createNewFile();
        KeyGenerator generator = KeyGenerator.getInstance(cipherName,
                        Config.getParameter("encryption.provider.signature"));
        generator.init(keysize);
        Key key = generator.generateKey();
        byte[] salt = new byte[8];
        SECURE_RANDOM.nextBytes(salt);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(cipherAlgorithm);
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(1, pbeKey, pbeParamSpec);
        byte[] encryptedKeyBytes = cipher.doFinal(key.getEncoded());
        FileOutputStream fos = new FileOutputStream(keyFile.toString());
        try
        {
            fos.write(salt);
            fos.write(encryptedKeyBytes);
            fos.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                fos.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return true;
    }


    public static SecretKey loadKey(String keyID) throws GeneralSecurityException, IOException
    {
        ConfigIntf cfg = getConfig();
        if(keyID.equals(getDefaultKeyFileName()))
        {
            return loadDefaultKey(cfg
                            .getParameter("symmetric.cipher"), cfg
                            .getParameter("symmetric.algorithm"), cfg
                            .getParameter("symmetric.key.master.password").toCharArray());
        }
        return loadKey(keyID, cfg
                        .getParameter("symmetric.cipher"), cfg
                        .getParameter("symmetric.algorithm"), cfg
                        .getParameter("symmetric.key.master.password").toCharArray());
    }


    public static SecretKey loadDefaultKey(String cipherName, String cipherAlgorithm, char[] password)
                    throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException
    {
        Path keyFilePath = getSecurityConfigDir().resolve(getDefaultKeyFileName());
        File file = keyFilePath.toFile();
        if(file.exists())
        {
            LOG.debug("DEFAULT KEY in /config/security found !!!");
            FileInputStream fis = new FileInputStream(file);
            try
            {
                SecretKey secretKey = loadKey(fis, cipherName, cipherAlgorithm, password);
                fis.close();
                return secretKey;
            }
            catch(Throwable throwable)
            {
                try
                {
                    fis.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        LOG.debug("no DEFAULT KEY in /config/security found !!! ... loading from resource folder");
        InputStream inputStream = EncryptionUtil.class.getResourceAsStream("/security/" + getDefaultKeyFileName());
        try
        {
            SecretKey secretKey = loadKey(inputStream, cipherName, cipherAlgorithm, password);
            if(inputStream != null)
            {
                inputStream.close();
            }
            return secretKey;
        }
        catch(Throwable throwable)
        {
            if(inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    public static SecretKey loadKey(String keyID, String cipherName, String cipherAlgorithm, char[] password)
    {
        ConfigIntf cfg = getConfig();
        String keyFileName = cfg.getParameter(keyID);
        Preconditions.checkArgument((keyFileName != null));
        if(!isNewStyleKey(keyFileName))
        {
            return loadOldKey(keyFileName);
        }
        Path keyFilePath = getSecurityConfigDir().resolve(keyFileName);
        try
        {
            FileInputStream fis = new FileInputStream(keyFilePath.toString());
            try
            {
                SecretKey secretKey = loadKey(fis, cipherName, cipherAlgorithm, password);
                fis.close();
                return secretKey;
            }
            catch(Throwable throwable)
            {
                try
                {
                    fis.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage());
            LOG.debug(e.getMessage(), e);
            return null;
        }
    }


    public static SecretKey loadKey(InputStream fis, String cipherName, String cipherAlgorithm, char[] password)
                    throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
    {
        Preconditions.checkArgument((fis != null));
        SecretKeySpec key = null;
        byte[] saltAndKeyBytes = streamToBytes(fis);
        byte[] salt = new byte[8];
        System.arraycopy(saltAndKeyBytes, 0, salt, 0, 8);
        int length = saltAndKeyBytes.length - 8;
        byte[] encryptedKeyBytes = new byte[length];
        System.arraycopy(saltAndKeyBytes, 8, encryptedKeyBytes, 0, length);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(cipherAlgorithm);
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(2, pbeKey, pbeParamSpec);
        byte[] decryptedKeyBytes = cipher.doFinal(encryptedKeyBytes);
        key = new SecretKeySpec(decryptedKeyBytes, cipherName);
        return key;
    }


    private static byte[] streamToBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            int index = 0;
            while((index = inputStream.read()) != -1)
            {
                baos.write(index);
            }
            byte[] arrayOfByte = baos.toByteArray();
            baos.close();
            return arrayOfByte;
        }
        catch(Throwable throwable)
        {
            try
            {
                baos.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }


    private static SecretKey loadOldKey(String keyFileName)
    {
        try
        {
            Path keyFilePath = getSecurityConfigDir().resolve(keyFileName);
            String keyContent = FileUtils.readFileToString(keyFilePath.toFile());
            PBEKeySpec pbeKeySpec = new PBEKeySpec(keyContent.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(
                            Config.getParameter("symmetric.algorithm"),
                            Config.getParameter("encryption.provider.signature"));
            return keyFactory.generateSecret(pbeKeySpec);
        }
        catch(IOException | NoSuchAlgorithmException | java.security.NoSuchProviderException | InvalidKeySpecException e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }


    public static final byte[] getOldSalt()
    {
        byte[] salt = {-57, 115, 33, -116, 126, -56, -18, -103};
        return salt;
    }


    public static final PBEParameterSpec getOldPBEParameterSpec()
    {
        PBEParameterSpec pSpec = null;
        try
        {
            pSpec = new PBEParameterSpec(getOldSalt(), 1000);
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return pSpec;
    }


    public static boolean isNewStyleKey(String keyfile)
    {
        return keyfile.endsWith(".hybris");
    }


    public static String asHex(byte[] buf)
    {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for(int index = 0; index < buf.length; index++)
        {
            if((buf[index] & 0xFF) < 16)
            {
                strbuf.append('0');
            }
            strbuf.append(Long.toString((buf[index] & 0xFF), 16));
        }
        return strbuf.toString();
    }


    private static Path getSecurityConfigDir()
    {
        SystemConfig systemConfig = ConfigUtil.getPlatformConfig(EncryptionUtil.class).getSystemConfig();
        return Paths.get(systemConfig.getConfigDir().toString(), new String[] {"security"});
    }
}
