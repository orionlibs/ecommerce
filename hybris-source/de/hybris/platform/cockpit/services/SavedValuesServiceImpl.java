package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.enums.SavedValueEntryType;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hmc.jalo.SavedValues;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavedValuesServiceImpl extends AbstractServiceImpl implements SavedValuesService
{
    private static final Logger LOG = LoggerFactory.getLogger(SavedValuesServiceImpl.class);
    private static final String COMMA = ", ";


    public Set<TypedObject> getSavedValues(TypedObject wrappedProduct)
    {
        Set<TypedObject> ret = new LinkedHashSet<>();
        Collection<SavedValues> savedValues = ServicelayerManager.getInstance().getSavedValues((Item)
                        getModelService().getSource(wrappedProduct.getObject()));
        for(SavedValues value : savedValues)
        {
            ret.add(getTypeService().wrapItem(value));
        }
        return ret;
    }


    public String getAuthor(TypedObject savedValue)
    {
        String author = null;
        if(savedValue == null)
        {
            throw new IllegalArgumentException("Saved value can not be null.");
        }
        Object object = savedValue.getObject();
        if(object instanceof SavedValuesModel)
        {
            UserModel user = ((SavedValuesModel)object).getUser();
            if(user == null)
            {
                LOG.warn("Could not retrieve author. Reason: User was null.");
            }
            else
            {
                author = getObjectLabel(user);
            }
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValuesModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return StringUtils.isBlank(author) ? "" : author;
    }


    public String getModificationTime(TypedObject savedValue)
    {
        String modTime = null;
        if(savedValue == null)
        {
            throw new IllegalArgumentException("Saved value can not be null.");
        }
        Object object = savedValue.getObject();
        if(object instanceof SavedValuesModel)
        {
            Date timestamp = ((SavedValuesModel)object).getTimestamp();
            if(timestamp == null)
            {
                LOG.warn("Could not retrieve modification time. Reason: Timestamp was null.");
            }
            else
            {
                modTime = SimpleDateFormat.getDateTimeInstance(2, 3, UISessionUtils.getCurrentSession().getLocale()).format(timestamp);
            }
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValuesModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return StringUtils.isBlank(modTime) ? "" : modTime;
    }


    public String getModificationType(TypedObject savedValue)
    {
        String modType = null;
        if(savedValue == null)
        {
            throw new IllegalArgumentException("Saved value can not be null.");
        }
        Object object = savedValue.getObject();
        if(object instanceof SavedValuesModel)
        {
            SavedValueEntryType modificationType = ((SavedValuesModel)object).getModificationType();
            if(modificationType == null)
            {
                LOG.warn("Could not retrieve modification type. Reason: Entry type was null.");
            }
            else
            {
                modType = modificationType.toString();
            }
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValuesModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return StringUtils.isBlank(modType) ? "" : modType;
    }


    public String getModifiedAttribute(TypedObject savedValueEntry)
    {
        String modAttr = null;
        if(savedValueEntry == null)
        {
            throw new IllegalArgumentException("Saved value entry can not be null.");
        }
        Object object = savedValueEntry.getObject();
        if(object instanceof SavedValueEntryModel)
        {
            modAttr = ((SavedValueEntryModel)object).getOldValueAttributeDescriptor().getName(
                            UISessionUtils.getCurrentSession().getLocale());
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValueEntryModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return StringUtils.isBlank(modAttr) ? "" : modAttr;
    }


    public List<String> getNewValues(TypedObject savedValueEntry)
    {
        List<String> newValues = new ArrayList<>();
        if(savedValueEntry == null)
        {
            throw new IllegalArgumentException("Saved value entry can not be null.");
        }
        Object object = savedValueEntry.getObject();
        if(object instanceof SavedValueEntryModel)
        {
            Object newValue = ((SavedValueEntryModel)object).getNewValue();
            if(newValue instanceof Map)
            {
                Map valueMap = (Map)newValue;
                for(Object key : valueMap.keySet())
                {
                    newValues.add(getObjectLabel(valueMap.get(key)));
                }
            }
            else
            {
                newValues.add(getObjectLabel(newValue));
            }
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValueEntryModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return Collections.unmodifiableList(newValues);
    }


    public List<String> getOldValues(TypedObject savedValueEntry)
    {
        List<String> oldValues = new ArrayList<>();
        if(savedValueEntry == null)
        {
            throw new IllegalArgumentException("Saved value entry can not be null.");
        }
        Object object = savedValueEntry.getObject();
        if(object instanceof SavedValueEntryModel)
        {
            Object oldValue = ((SavedValueEntryModel)object).getOldValue();
            if(oldValue instanceof Map)
            {
                Map valueMap = (Map)oldValue;
                for(Object key : valueMap.keySet())
                {
                    oldValues.add(getObjectLabel(valueMap.get(key)));
                }
            }
            else
            {
                oldValues.add(getObjectLabel(oldValue));
            }
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValueEntryModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return Collections.unmodifiableList(oldValues);
    }


    public List<TypedObject> getSavedValuesEntries(TypedObject savedValue)
    {
        List<TypedObject> entries = null;
        if(savedValue == null)
        {
            throw new IllegalArgumentException("Saved value can not be null.");
        }
        Object object = savedValue.getObject();
        if(object instanceof SavedValuesModel)
        {
            Set<SavedValueEntryModel> valueEntries = ((SavedValuesModel)object).getSavedValuesEntries();
            if(valueEntries != null && !valueEntries.isEmpty())
            {
                entries = UISessionUtils.getCurrentSession().getTypeService().wrapItems(valueEntries);
            }
        }
        else
        {
            throw new IllegalArgumentException("Expected type 'SavedValuesModel' but found '" + (
                            (object == null) ? "null" : object.getClass().getSimpleName()) + "'.");
        }
        return (entries == null) ? Collections.EMPTY_LIST : Collections.<TypedObject>unmodifiableList(entries);
    }


    private String getObjectLabel(Object value)
    {
        String label = null;
        TypedObject item = null;
        if(value instanceof de.hybris.platform.core.model.ItemModel)
        {
            item = UISessionUtils.getCurrentSession().getTypeService().wrapItem(value);
        }
        else if(value instanceof TypedObject)
        {
            item = (TypedObject)value;
        }
        else if(value instanceof Collection)
        {
            StringBuffer ret = new StringBuffer();
            for(Object object : value)
            {
                ret.append(getObjectLabel(object));
                ret.append(", ");
            }
            return (ret.length() > 0) ? ret.substring(0, ret.length() - 2) : "";
        }
        if(item != null)
        {
            label = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(item);
        }
        else if(value != null)
        {
            label = value.toString();
        }
        return StringUtils.isBlank(label) ? "" : label;
    }


    public UserModel getLastModifyingUser(TypedObject item)
    {
        Collection<SavedValues> savedValues = ServicelayerManager.getInstance().getSavedValues((Item)
                        getModelService().getSource(item.getObject()));
        if(savedValues != null && !savedValues.isEmpty())
        {
            SavedValues lastSavedValue = (new ArrayList<>(savedValues)).get(savedValues.size() - 1);
            if(lastSavedValue != null && lastSavedValue.getUser() != null)
            {
                return (UserModel)getModelService().get(lastSavedValue.getUser());
            }
        }
        return null;
    }
}
