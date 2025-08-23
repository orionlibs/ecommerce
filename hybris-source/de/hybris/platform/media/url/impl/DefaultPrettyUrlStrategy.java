package de.hybris.platform.media.url.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.media.url.PrettyUrlStrategy;
import de.hybris.platform.util.MediaUtil;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

final class DefaultPrettyUrlStrategy implements PrettyUrlStrategy
{
    private static final char DOT = '.';


    public Optional<String> assemblePath(PrettyUrlStrategy.MediaData data)
    {
        Objects.requireNonNull(data);
        StringBuilder sb = new StringBuilder(512);
        sb.append("sys_").append(data.getTenantId()).append("/");
        sb.append(data.getFolderQualifier()).append("/");
        String location = data.getLocation();
        int lastDotIdx = location.lastIndexOf('.');
        if(lastDotIdx < 0)
        {
            return Optional.empty();
        }
        String fileExtension = location.substring(lastDotIdx + 1);
        String realFileName = normalizeRealFileName(data.getRealFileName());
        if(realFileName == null)
        {
            String basePath = data.getLocation().substring(0, lastDotIdx);
            int lastSlashIdx = StringUtils.lastIndexOf(basePath, "/");
            String fileName = basePath.substring(lastSlashIdx + 1);
            sb.append(basePath).append("/").append(fileName).append('.').append(fileExtension);
        }
        else
        {
            String basePath = location.substring(0, lastDotIdx);
            int lastDotIndexForRealFileName = realFileName.lastIndexOf('.');
            if(lastDotIndexForRealFileName != -1)
            {
                realFileName = realFileName.substring(0, lastDotIndexForRealFileName);
            }
            sb.append(basePath).append("/").append(realFileName).append('.').append(fileExtension);
        }
        return Optional.of(sb.toString());
    }


    public PrettyUrlStrategy.ParsedPath parsePath(String path)
    {
        int firstSlashIdx = StringUtils.ordinalIndexOf(path, "/", 1);
        int secondSlashIdx = StringUtils.ordinalIndexOf(path, "/", 2);
        if(-1 == firstSlashIdx || -1 == secondSlashIdx)
        {
            throw new IllegalArgumentException("The URL has wrong format");
        }
        String tenantPart = path.substring(0, firstSlashIdx);
        if(!tenantPart.startsWith("sys_"))
        {
            throw new IllegalArgumentException("Wrong tenant ID format in URL");
        }
        String tenantId = tenantPart.substring("sys_".length());
        String folderQualifier = path.substring(firstSlashIdx + 1, secondSlashIdx);
        String rest = MediaUtil.removeLeadingFileSepIfNeeded(path.substring(secondSlashIdx));
        int lastDotIdx = StringUtils.lastIndexOf(rest, 46);
        int lastSlashIdx = StringUtils.lastIndexOf(rest, "/");
        String fileExt = rest.substring(lastDotIdx + 1);
        if(-1 == lastSlashIdx)
        {
            throw new IllegalArgumentException("The URL has wrong format");
        }
        String location = rest.substring(0, lastSlashIdx) + "." + rest.substring(0, lastSlashIdx);
        return new PrettyUrlStrategy.ParsedPath(tenantId, MediaUtil.removeTrailingFileSepIfNeeded(folderQualifier), location);
    }


    public boolean isPrettyUrlEnabled()
    {
        return Registry.getCurrentTenantNoFallback().getConfig().getBoolean("media.legacy.prettyURL", false);
    }


    private String normalizeRealFileName(String mediaRealFileName)
    {
        return StringUtils.isNotBlank(mediaRealFileName) ? MediaUtil.normalizeRealFileName(mediaRealFileName) : null;
    }
}
