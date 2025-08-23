package de.hybris.platform.servicelayer.i18n.daos;

import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;

public interface LanguageDao
{
    List<LanguageModel> findLanguages();


    List<LanguageModel> findLanguagesByCode(String paramString);
}
