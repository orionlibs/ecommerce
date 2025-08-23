package de.hybris.platform.media.services.impl;

import com.google.common.base.Strings;
import de.hybris.platform.util.MediaUtil;

public final class HierarchicalMediaPathBuilder
{
    public static final String HASHING_DEPTH_KEY = "hashing.depth";
    public static final String GLOBAL_HASHING_DEPTH_KEY = "media.default.hashing.depth";
    private static final String HASH_FORMAT = "%02x";
    private static final int DEFAULT_DIRECTORIES_DEPTH = 0;
    private static final int DEFAULT_MAX_DIRECTORIES_DEPTH = 4;
    private static final int MASK = 255;
    private static final int SHIFT = 8;
    private final int hashingDepth;


    public static HierarchicalMediaPathBuilder forDepth(int depth)
    {
        return new HierarchicalMediaPathBuilder(depth);
    }


    private HierarchicalMediaPathBuilder(int hashingDepth)
    {
        this
                        .hashingDepth = (hashingDepth < 0 || hashingDepth > 4) ? 0 : hashingDepth;
    }


    public String buildPath(String folderPath, String mediaId)
    {
        String path, subSubpath = getSubFolderPath(mediaId, this.hashingDepth);
        if(folderPath == null)
        {
            path = Strings.nullToEmpty(subSubpath);
        }
        else
        {
            path = MediaUtil.addTrailingFileSepIfNeeded(Strings.nullToEmpty(folderPath)) + MediaUtil.addTrailingFileSepIfNeeded(Strings.nullToEmpty(folderPath));
        }
        return path;
    }


    private String getSubFolderPath(String mediaId, int pathDepth)
    {
        StringBuilder builder = new StringBuilder();
        int token = getHashForMediaIdentifier(mediaId);
        int move = 0;
        int subdirDepth = getSubdirDepth(pathDepth) - 1;
        if(wantHierarchicalStructure(subdirDepth))
        {
            for(int i = 0; i <= 8 * subdirDepth; i += 8)
            {
                long dirName = shiftAndMask(token, i);
                builder.append('h').append(String.format("%02x", new Object[] {Long.valueOf(dirName)}));
                builder.append("/");
            }
        }
        return builder.toString();
    }


    private int getHashForMediaIdentifier(String mediaId)
    {
        long counter = (mediaId.hashCode() >> 15);
        return (int)(counter ^ counter >>> 32L);
    }


    private int getSubdirDepth(int pathDepth)
    {
        return (pathDepth >= 0 && pathDepth <= 4) ? pathDepth : 0;
    }


    private boolean wantHierarchicalStructure(int subdirDepth)
    {
        return (subdirDepth >= 0);
    }


    private long shiftAndMask(long token, int moveStep)
    {
        return token >> moveStep & 0xFFL;
    }
}
