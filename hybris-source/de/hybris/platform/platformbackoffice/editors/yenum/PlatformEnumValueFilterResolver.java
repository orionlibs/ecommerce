package de.hybris.platform.platformbackoffice.editors.yenum;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.editor.defaultenum.EnumValueFilterResolver;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class PlatformEnumValueFilterResolver implements EnumValueFilterResolver
{
    public <T> List<T> filterEnumValues(List<T> values, String textQuery)
    {
        if(CollectionUtils.isEmpty(values))
        {
            return Collections.emptyList();
        }
        ArrayList<T> list = Lists.newArrayList(values);
        list.removeIf(input -> StringUtils.isBlank(textQuery)
                        ? false
                        : ((input instanceof Enum)
                                        ? (!((Enum)input).name().toLowerCase(Locale.getDefault()).startsWith(textQuery.toLowerCase(Locale.getDefault())))
                                        : ((input instanceof HybrisEnumValue) ? (!((HybrisEnumValue)input).getCode().toLowerCase(Locale.getDefault()).startsWith(textQuery.toLowerCase(Locale.getDefault()))) : false)));
        return list;
    }
}
