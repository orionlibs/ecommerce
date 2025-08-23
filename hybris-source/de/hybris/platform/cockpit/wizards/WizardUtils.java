package de.hybris.platform.cockpit.wizards;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class WizardUtils
{
    public static String getObjectTextLabel(TypedObject object)
    {
        return getObjectTextLabel(object, UISessionUtils.getCurrentSession());
    }


    public static String getObjectTextLabel(TypedObject object, UISession session)
    {
        return session.getLabelService().getObjectTextLabel(object);
    }


    public static boolean isLocalValueEmpty(Object localValue)
    {
        return (localValue == null || (localValue instanceof Collection && CollectionUtils.isEmpty((Collection)localValue)) || (localValue instanceof String &&
                        StringUtils.isEmpty((String)localValue)));
    }
}
