package de.hybris.platform.jalo.flexiblesearch;

import com.google.common.collect.Sets;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PolyglotQueryLogger
{
    private static final Logger LOGGER = LoggerFactory.getLogger("POLYGLOT");
    private static final Logger CLASS_LOGGER = LoggerFactory.getLogger(PolyglotQueryLogger.class);
    private static final LoggerConfig LOGGER_CONFIG = new LoggerConfig();
    private static final ConcurrentHashMap<Path, String> extensions = new ConcurrentHashMap<>();
    private static final String EXT_NAME_UNKNOWN = "UNKNOWN";
    private static final int CHECK_LIB_MAX_LEVELS = 5;


    static void logQueryIfNeeded(QueryOptions options, Set<Integer> usedTypeCodes)
    {
        if(!LOGGER_CONFIG.isLoggingEnabled())
        {
            return;
        }
        if(Sets.intersection(usedTypeCodes, LOGGER_CONFIG.getConfiguredTypeCodes()).isEmpty())
        {
            return;
        }
        StringBuilder messageBuilder = (new StringBuilder("EXPECTED POLYGLOT! ")).append(options.getQuery()).append(" ");
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach(f -> {
            boolean skip = false;
            skip |= f.getClassName().contains("$");
            skip |= f.getClassName().endsWith("FlexibleSearch");
            skip |= f.getClassName().endsWith("FlexibleSearchService");
            skip |= f.getClassName().endsWith("PolyglotQueryLogger");
            skip |= f.getClassName().startsWith("de.hybris.platform.jalo.type.ReflectionAttributeAccess");
            skip |= f.getClassName().contains("JUnit");
            skip |= f.getClassName().startsWith("org.junit.runners.ParentRunner");
            skip |= f.getClassName().startsWith("org.junit");
            skip |= f.getClassName().startsWith("de.hybris.platform.testframework");
            skip |= f.getClassName().startsWith("de.hybris.platform.jalo.Item");
            skip |= f.getClassName().startsWith("de.hybris.platform.tx.Transaction");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.session.impl.DefaultSessionService");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.dao.AbstractItemDao");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter");
            skip |= f.getClassName().equals("de.hybris.platform.jalo.type.ComposedType");
            skip |= f.getClassName().equals("de.hybris.platform.cache.AbstractCacheUnit");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.polyglot.UnitOfWorkInterceptor");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.polyglot.UnitOfWorkInterceptor");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.model.impl.ResolvingModelPersister");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper");
            skip |= f.getClassName().equals("de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService");
            URL url = Optional.<Class<?>>ofNullable(f.getDeclaringClass()).map(Class::getProtectionDomain).map(ProtectionDomain::getCodeSource).map(CodeSource::getLocation).orElse(null);
            int i = skip | ((url == null || isLib(url)) ? 1 : 0);
            if(i == 0)
            {
                String extensionName;
                try
                {
                    extensionName = getExtensionName(Paths.get(url.toURI()));
                }
                catch(URISyntaxException e)
                {
                    CLASS_LOGGER.warn("setting extension as '{}' because of exception", "UNKNOWN", e);
                    extensionName = "UNKNOWN";
                }
                messageBuilder.append("<-").append(extensionName).append(":::").append(f.getFileName()).append(":::").append(f.getLineNumber()).append(":::").append(f.getClassName()).append(":::").append(f.getMethodName());
            }
        });
        LOGGER.info(messageBuilder.toString());
    }


    private static boolean isLib(URL url)
    {
        if(!url.getFile().endsWith(".jar"))
        {
            return false;
        }
        String[] parts = url.getFile().split(System.lineSeparator());
        String[] subarray = (String[])ArrayUtils.subarray((Object[])parts, parts.length - 5, parts.length);
        return ArrayUtils.contains((Object[])subarray, "lib");
    }


    private static String getExtensionName(Path path)
    {
        if(path == null)
        {
            return "UNKNOWN";
        }
        String extName = extensions.get(path);
        if(extName != null)
        {
            return extName;
        }
        if(Files.isDirectory(path, new java.nio.file.LinkOption[0]))
        {
            Path extInfo = path.resolve("extensioninfo.xml");
            if(Files.exists(extInfo, new java.nio.file.LinkOption[0]))
            {
                extName = path.getName(path.getNameCount() - 1).toString();
                extensions.putIfAbsent(path, extName);
                return extName;
            }
        }
        extName = getExtensionName(path.getParent());
        extensions.putIfAbsent(path, extName);
        return extName;
    }
}
