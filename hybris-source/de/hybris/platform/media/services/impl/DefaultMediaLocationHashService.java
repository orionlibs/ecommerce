package de.hybris.platform.media.services.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.media.services.MediaLocationHashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMediaLocationHashService implements MediaLocationHashService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMediaLocationHashService.class);
    private String salt;


    public void verifyHashForLocation(String hash, String folderQualifier, String location)
    {
        if(!isHashValidForLocation(hash, folderQualifier, location))
        {
            LOG.error("Provided hash is not verified as valid when requesting Media location");
            throw new MediaInvalidLocationException("Given location: " + location + " does not belong to provided folder qualifier: " + folderQualifier);
        }
    }


    public MediaLocationHashService.HashType verifyHash(String hash, String folderQualifier, String location, long size, String mime)
    {
        if(isHashValidForLocationMimeAndSize(hash, folderQualifier, location, size, mime))
        {
            return MediaLocationHashService.HashType.LOCATION_MIME_SIZE;
        }
        if(isHashValidForLocation(hash, folderQualifier, location))
        {
            return MediaLocationHashService.HashType.LOCATION;
        }
        LOG.error("Provided hash is not verified as valid when requesting Media");
        throw new MediaInvalidLocationException("Given media data are not valid with provided hash");
    }


    private boolean isHashValidForLocationMimeAndSize(String hash, String folderQualifier, String location, long size, String mime)
    {
        String userProvidedHash = createHash(folderQualifier, location, size, mime);
        return userProvidedHash.equals(hash);
    }


    private boolean isHashValidForLocation(String hash, String folderQualifier, String location)
    {
        String userProvidedHash = createHashForLocation(folderQualifier, location);
        return userProvidedHash.equals(hash);
    }


    public String createHashForLocation(String folderQualifier, String location)
    {
        HashFunction hashFunction = Hashing.sha256();
        return hashFunction.hashUnencodedChars(folderQualifier + folderQualifier + location).toString();
    }


    public String createHash(String folderQualifier, String location, long size, String mime)
    {
        HashFunction hashFunction = Hashing.sha256();
        HashCode hash = hashFunction.hashUnencodedChars(folderQualifier + folderQualifier + location + size + mime);
        return hash.toString();
    }


    public void setSalt(String salt)
    {
        this.salt = salt;
    }


    public String getSalt()
    {
        return this.salt;
    }
}
