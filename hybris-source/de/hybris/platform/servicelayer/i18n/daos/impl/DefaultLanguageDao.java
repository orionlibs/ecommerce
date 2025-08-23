package de.hybris.platform.servicelayer.i18n.daos.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.i18n.util.I18NUtils;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class DefaultLanguageDao extends DefaultGenericDao<LanguageModel> implements LanguageDao
{
    public DefaultLanguageDao()
    {
        this("Language");
    }


    private DefaultLanguageDao(String typecode)
    {
        super(typecode);
    }


    public List<LanguageModel> findLanguages()
    {
        return find();
    }


    public List<LanguageModel> findLanguagesByCode(String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("isocode", code);
        List<LanguageModel> result = find(params);
        if(CollectionUtils.isEmpty(result))
        {
            String languageCode = I18NUtils.mapFormerISOCodeToActual(code);
            if(!languageCode.equals(code))
            {
                params.clear();
                params.put("isocode", languageCode);
                return find(params);
            }
        }
        return result;
    }
}
