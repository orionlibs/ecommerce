package de.hybris.platform.masterserver.collector.system.impl.spring;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class ExtensionNameFromPathExtractor
{
    private final Map<Path, String> extensionPaths;
    private final Function<Path, Optional<String>> cache;


    ExtensionNameFromPathExtractor(Map<Path, String> extensionPaths)
    {
        this.extensionPaths = Map.copyOf(extensionPaths);
        this.cache = (Function<Path, Optional<String>>)CacheBuilder.newBuilder().build(CacheLoader.from(this::detectExtensionName));
    }


    public static ExtensionNameFromPathExtractor fromConfig()
    {
        PlatformConfig config = ConfigUtil.getPlatformConfig(ExtensionNameFromPathExtractor.class);
        Map<Path, String> paths = (Map<Path, String>)config.getExtensionInfosInBuildOrder().stream().collect(Collectors.toMap(i -> i.getExtensionDirectory().toPath(), ExtensionInfo::getName));
        return new ExtensionNameFromPathExtractor(paths);
    }


    public Optional<String> getExtensionName(Path path)
    {
        return this.cache.apply(path);
    }


    private Optional<String> detectExtensionName(Path path)
    {
        Path absPath = ((Path)Objects.<Path>requireNonNull(path)).toAbsolutePath();
        return this.extensionPaths.entrySet().stream().filter(e -> absPath.startsWith(((Path)e.getKey()).toAbsolutePath())).findAny().map(Map.Entry::getValue);
    }
}
