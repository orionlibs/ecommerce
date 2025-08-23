package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;

public interface CMSMediaFormatDao extends Dao
{
    Collection<MediaFormatModel> getAllMediaFormats();


    MediaFormatModel getMediaFormatByQualifier(String paramString) throws IllegalArgumentException, UnknownIdentifierException, AmbiguousIdentifierException;


    Collection<MediaFormatModel> getMediaFormatsByQualifiers(Collection<String> paramCollection) throws IllegalArgumentException;
}
