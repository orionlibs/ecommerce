package de.hybris.platform.persistence.links.jdbc;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.c2l.Language;
import java.util.ArrayList;

public class JdbcLinksSupport
{
    public static final PK NONE_LANGUAGE_PK = PK.NULL_PK;
    public static final Long NONE_LANGUAGE_PK_VALUE = NONE_LANGUAGE_PK.getLong();
    public static final Language NONE_LANGUAGE = (Language)new Object();
    public static final Iterable<Long> ALL_LANGUAGES = new ArrayList<>(0);
}
