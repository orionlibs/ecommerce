package de.hybris.platform.util.localization.jalo;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JaloBasedTypeLocalization
{
    private static final Logger LOG = LoggerFactory.getLogger(JaloBasedTypeLocalization.class);
    private static final String DEFAULT_RESOURCE_BUNDLE_ENCODING = "UTF-8";
    private static final String LOC_FILES_DIR = "localization";
    private static final String LOCALE_FILE_NAME_TEMPLATE = "%s-locales_%s.properties";
    private volatile Map<Language, Properties> localizations = null;
    private static final SingletonCreator.Creator<JaloBasedTypeLocalization> SINGLETON_CREATOR = (SingletonCreator.Creator<JaloBasedTypeLocalization>)new Object();


    public static JaloBasedTypeLocalization getInstance()
    {
        return (JaloBasedTypeLocalization)Registry.getSingleton(SINGLETON_CREATOR);
    }


    private JaloBasedTypeLocalization()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY,
                        PlatformStringUtils.valueOf(32)});
        topic.addInvalidationListener((key, invalidationType, target, remoteSrc) -> clearCache());
    }


    public Map<Language, Properties> getLocalizations()
    {
        Map<Language, Properties> _localizations = this.localizations;
        if(_localizations == null)
        {
            synchronized(this)
            {
                _localizations = this.localizations;
                if(_localizations == null)
                {
                    _localizations = loadLocalizations();
                    this.localizations = _localizations;
                }
            }
        }
        return _localizations;
    }


    public synchronized void localizeTypes()
    {
        Transaction tx = Transaction.current();
        tx.begin();
        boolean success = false;
        try
        {
            Collection<Type> coll = TypeManager.getInstance().getAllTypes();
            LOG.info("Localizing {} types ...", Integer.valueOf(coll.size()));
            tx = localizeTypes(coll, tx);
            success = true;
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage());
            LOG.debug(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        finally
        {
            if(success)
            {
                tx.commit();
            }
            else
            {
                tx.rollback();
            }
        }
    }


    public void clearCache()
    {
        this.localizations = null;
    }


    private Transaction localizeTypes(Collection<Type> types, Transaction existing) throws Exception
    {
        Transaction tx = existing;
        for(Iterator<Type> it = resortTypes(types).iterator(); it.hasNext(); )
        {
            long s = System.currentTimeMillis();
            Type type = it.next();
            tx = localize(type, tx);
            long e = System.currentTimeMillis();
            LOG.debug("   Localizing '{}' took {} ms", type.getCode(), Utilities.formatTime(e - s));
        }
        return tx;
    }


    private Transaction localize(Type type, Transaction existing)
    {
        Transaction tx = existing;
        tx = Transaction.performCommitBeginEvery(tx, 1000);
        long s = System.currentTimeMillis();
        localizeName(type);
        long e = System.currentTimeMillis();
        LOG.debug("      name '{}' took {}", type.getCode(), Utilities.formatTime(e - s));
        s = System.currentTimeMillis();
        localizeDescription(type);
        e = System.currentTimeMillis();
        LOG.debug("      desc '{}' took {}", type.getCode(), Utilities.formatTime(e - s));
        if(type instanceof EnumerationType)
        {
            s = System.currentTimeMillis();
            localizeEnumerationValues((EnumerationType)type);
            e = System.currentTimeMillis();
            LOG.debug("      enum '{}' took {}", type.getCode(), Utilities.formatTime(e - s));
        }
        else if(type instanceof ComposedType)
        {
            s = System.currentTimeMillis();
            tx = localizeAttributes((ComposedType)type, tx);
            e = System.currentTimeMillis();
            LOG.debug("      localized attributes '{}' took {}", type.getCode(), Utilities.formatTime(e - s));
        }
        return tx;
    }


    private Transaction localizeAttributes(ComposedType type, Transaction existing)
    {
        Transaction tx = existing;
        for(Iterator<AttributeDescriptor> it = type.getDeclaredAttributeDescriptors().iterator(); it.hasNext(); )
        {
            tx = Transaction.performCommitBeginEvery(tx, 1000);
            AttributeDescriptor ad = it.next();
            localizeAttributeName(type, ad);
            localizeAttributeDescription(type, ad);
        }
        Type variantType = null;
        try
        {
            variantType = TypeManager.getInstance().getType("VariantType");
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(variantType != null && variantType.isAssignableFrom((Type)type.getComposedType()))
        {
            try
            {
                for(Iterator<AttributeDescriptor> iterator = ((Collection)type.getAttribute("variantAttributes")).iterator(); iterator.hasNext(); )
                {
                    tx = Transaction.performCommitBeginEvery(tx, 1000);
                    AttributeDescriptor ad = iterator.next();
                    localizeAttributeName(type, ad);
                    localizeAttributeDescription(type, ad);
                }
            }
            catch(JaloInvalidParameterException e)
            {
                LOG.debug("Cannot localize variant attributes: {}", e.getMessage(), e);
            }
            catch(JaloSecurityException e)
            {
                LOG.debug("Cannot localize variant attributes: {}", e.getMessage(), e);
            }
        }
        for(Iterator<AttributeDescriptor> iterator1 = type.getInheritedAttributeDescriptors().iterator(); iterator1.hasNext(); )
        {
            tx = Transaction.performCommitBeginEvery(tx, 1000);
            AttributeDescriptor ad = iterator1.next();
            localizeAttributeName(type, ad);
            localizeAttributeDescription(type, ad);
        }
        return tx;
    }


    private void localizeEnumerationValues(EnumerationType type)
    {
        if(type.getValueType().getJaloClass().isAssignableFrom(EnumerationValue.class))
        {
            for(Iterator<EnumerationValue> it = type.getValues().iterator(); it.hasNext(); )
            {
                EnumerationValue ev = it.next();
                Map<Object, Object> allNames = new HashMap<>();
                for(Iterator<Language> iter = getLocalizations().keySet().iterator(); iter.hasNext(); )
                {
                    Language lang = iter.next();
                    Properties props = getLocalizations().get(lang);
                    String localizedWord = props.getProperty(buildEnumerationValueKey(ev, (Type)type));
                    if(StringUtils.isNotBlank(localizedWord))
                    {
                        LOG.debug("Localizing enumeration value name '{}:{}={}'", new Object[] {type.getCode(), lang.getIsoCode(), localizedWord});
                        allNames.put(lang, localizedWord);
                    }
                    else
                    {
                        LOG.debug("No localization found for enumeration value name '{}:{}'", type.getCode(), lang.getIsoCode());
                    }
                    if(!allNames.isEmpty())
                    {
                        Map<Object, Object> before = new HashMap<>(ev.getAllNames(JaloSession.getCurrentSession().getSessionContext()));
                        before.putAll(allNames);
                        ev.setAllNames(JaloSession.getCurrentSession().getSessionContext(), before);
                    }
                }
            }
        }
    }


    private void localizeName(Type type)
    {
        Map<Object, Object> allNames = new HashMap<>();
        for(Iterator<Language> iter = getLocalizations().keySet().iterator(); iter.hasNext(); )
        {
            Language lang = iter.next();
            Properties props = getLocalizations().get(lang);
            String localizedWord = props.getProperty(buildTypeNameKey(type));
            if(StringUtils.isNotBlank(localizedWord))
            {
                LOG.debug("Localizing type name '{}:{}={}'", new Object[] {type.getCode(), lang.getIsoCode(), localizedWord});
                allNames.put(lang, localizedWord);
                continue;
            }
            LOG.debug("No localization found for type name '{}:{}'", type.getCode(), lang.getIsoCode());
        }
        if(!allNames.isEmpty())
        {
            Map<Object, Object> before = new HashMap<>(type.getAllNames());
            before.putAll(allNames);
            type.setAllNames(before);
        }
    }


    private void localizeDescription(Type type)
    {
        Map<Object, Object> allDescriptions = new HashMap<>();
        for(Iterator<Language> iter = getLocalizations().keySet().iterator(); iter.hasNext(); )
        {
            Language lang = iter.next();
            Properties props = getLocalizations().get(lang);
            String localizedWord = props.getProperty(buildTypeDescriptionKey(type));
            if(StringUtils.isNotBlank(localizedWord))
            {
                LOG.debug("Localizing type description '{}:{}={}'", new Object[] {type.getCode(), lang.getIsoCode(), localizedWord});
                allDescriptions.put(lang, localizedWord);
                continue;
            }
            LOG.debug("No localization found for type description '{}:{}", type.getCode(), lang.getIsoCode());
        }
        if(!allDescriptions.isEmpty())
        {
            Map<Object, Object> before = new HashMap<>(type.getAllDescriptions());
            before.putAll(allDescriptions);
            type.setAllDescriptions(before);
        }
    }


    private void localizeAttributeName(ComposedType type, AttributeDescriptor feature)
    {
        Map<Object, Object> names = new HashMap<>();
        for(Iterator<Language> iter = getLocalizations().keySet().iterator(); iter.hasNext(); )
        {
            Language language = iter.next();
            Properties props = getLocalizations().get(language);
            String localizedWord = props.getProperty(buildAttributeNameKey((Type)type, feature));
            if(StringUtils.isNotBlank(localizedWord))
            {
                LOG.debug("Localizing feature name: '{}:{}:{}'='{}'", new Object[] {type.getCode(), feature.getQualifier(), language
                                .getIsoCode(), localizedWord});
                names.put(language, localizedWord);
                continue;
            }
            LOG.debug("No localization found for feature name '{}:{}:{}'", new Object[] {type.getCode(), feature.getQualifier(), language
                            .getIsoCode()});
        }
        if(!names.isEmpty())
        {
            Map<Object, Object> before = new HashMap<>(feature.getAllNames());
            before.putAll(names);
            feature.setAllNames(before);
        }
    }


    private void localizeAttributeDescription(ComposedType type, AttributeDescriptor feature)
    {
        Map<Language, String> descriptions = new HashMap<>();
        for(Iterator<Language> iter = getLocalizations().keySet().iterator(); iter.hasNext(); )
        {
            Language language = iter.next();
            Properties props = getLocalizations().get(language);
            String localizedWord = props.getProperty(buildAttributeDescriptionKey((Type)type, feature));
            if(StringUtils.isNotBlank(localizedWord))
            {
                LOG.debug("Localizing feature description: '{}:{}:{}'='{}'", new Object[] {type.getCode(), feature.getQualifier(), language
                                .getIsoCode(), localizedWord});
                descriptions.put(language, localizedWord);
                continue;
            }
            LOG.debug("No localization found for feature description '{}:{}:{}'", new Object[] {type.getCode(), feature.getQualifier(), language
                            .getIsoCode()});
        }
        if(!descriptions.isEmpty())
        {
            Map<Object, Object> before = new HashMap<>(feature.getAllDescriptions());
            before.putAll(descriptions);
            feature.setAllDescriptions(before);
        }
    }


    private static Map<Language, Properties> loadLocalizations()
    {
        Map<Language, Properties> localizations = new HashMap<>();
        Collection<Language> languages = C2LManager.getInstance().getAllLanguages();
        for(Iterator<Language> l_it = languages.iterator(); l_it.hasNext(); )
        {
            Language lang = l_it.next();
            Properties locs = loadLocalizations(lang);
            localizations.put(lang, locs);
        }
        return localizations;
    }


    static Properties loadLocalizations(Language lang)
    {
        Properties properties = new Properties();
        LinkedList<String> extNames = new LinkedList<>(Utilities.getExtensionNames());
        extNames.stream().map(Utilities::getExtensionInfo).forEach(e -> tryLoadTranslationsForLanguage(properties, lang, e));
        return convertKeysToLowerCase(properties);
    }


    private static void tryLoadTranslationsForLanguage(Properties properties, Language lang, ExtensionInfo extensionInfo)
    {
        Path localizationsDir = Paths.get(extensionInfo.getExtensionDirectory().getAbsolutePath(), new String[] {"resources", "localization"});
        try
        {
            DirectoryStream<Path> stream = Files.newDirectoryStream(localizationsDir, p -> isLocalizationFileExist(extensionInfo.getName(), p, lang));
            try
            {
                stream.forEach(p -> loadProperties(p, properties));
                if(stream != null)
                {
                    stream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(stream != null)
                {
                    try
                    {
                        stream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            LOG.debug("!!! Localization dir {} not found for language {}", localizationsDir, lang);
        }
    }


    private static boolean isLocalizationFileExist(String extName, Path path, Language lang)
    {
        String expectedFileName = String.format("%s-locales_%s.properties", new Object[] {extName, lang.getIsocode()});
        return expectedFileName.equalsIgnoreCase(path.getFileName().toString());
    }


    private static void loadProperties(Path path, Properties properties)
    {
        try
        {
            FileInputStream is = FileUtils.openInputStream(path.toFile());
            try
            {
                LOG.debug(">>> Trying to load localization file from path: {}", path);
                properties.load(new InputStreamReader(is, readAndCheckEncodingFromProperty()));
                LOG.debug(">>> Loaded localization file from path: {}", path);
                if(is != null)
                {
                    is.close();
                }
            }
            catch(Throwable throwable)
            {
                if(is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            LOG.debug("!!! Type localization not found in: {}", path);
        }
    }


    private static Properties convertKeysToLowerCase(Properties original)
    {
        Properties result = new Properties();
        original.keySet().forEach(k -> result.put(((String)k).toLowerCase(LocaleHelper.getPersistenceLocale()), original.get(k)));
        return result;
    }


    private static List<Type> resortTypes(Collection<Type> types)
    {
        List<Type> result = new LinkedList<>();
        for(Iterator<Type> it = types.iterator(); it.hasNext(); )
        {
            Type type = it.next();
            boolean added = false;
            for(int i = 0; i < result.size(); i++)
            {
                Type temp = result.get(i);
                if(type.isAssignableFrom(temp))
                {
                    result.add(i, type);
                    added = true;
                    break;
                }
            }
            if(!added)
            {
                result.add(type);
            }
        }
        return result;
    }


    private static String readAndCheckEncodingFromProperty()
    {
        String encoding = Config.getString("resource.bundle.encoding", "UTF-8");
        if(!Charset.isSupported(encoding))
        {
            LOG.warn("The given encoding '{}' is not supported by this VM. Please change in your property file the value for the key resource.bundle.encoding to a valid one. As fallback {} is used for now.", encoding, "UTF-8");
            return "UTF-8";
        }
        return encoding;
    }


    private static String buildEnumerationValueKey(EnumerationValue ev, Type type)
    {
        return "type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + ev.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".name";
    }


    private static String buildTypeNameKey(Type type)
    {
        return "type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".name";
    }


    private static String buildTypeDescriptionKey(Type type)
    {
        return "type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".description";
    }


    private static String buildAttributeNameKey(Type type, AttributeDescriptor feature)
    {
        return "type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + feature.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".name";
    }


    private static String buildAttributeDescriptionKey(Type type, AttributeDescriptor feature)
    {
        return "type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + feature.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".description";
    }
}
