package de.hybris.platform.dynamiccontent.impl;

import de.hybris.platform.dynamiccontent.DynamicContentChecksumCalculator;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;

public class DefaultDynamicContentChecksumCalculator implements DynamicContentChecksumCalculator
{
    public String calculateChecksumOf(String content)
    {
        return DigestUtils.md5Hex(Objects.<String>requireNonNull(content));
    }
}
