package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSearchFieldGroup implements SearchFieldGroup
{
    private String label = null;
    private final List<SearchField> fields = new ArrayList<>();
    private List<SearchFieldGroup> groups = new ArrayList<>();
    private SearchFieldGroup parent = null;


    public DefaultSearchFieldGroup(String name)
    {
        this.label = name;
    }


    public void setLabel(String name)
    {
        this.label = name;
    }


    public void setSearchFields(List<? extends SearchField> fields)
    {
        if(!this.fields.equals(fields))
        {
            this.fields.clear();
            for(SearchField field : fields)
            {
                this.fields.add(field);
                if(field instanceof DefaultSearchField)
                {
                    ((DefaultSearchField)field).setGroup(this);
                }
            }
        }
    }


    public void setSearchFieldGroups(List<? extends SearchFieldGroup> groups)
    {
        this.groups = (List)groups;
        if(this.groups != null)
        {
            for(SearchFieldGroup group : this.groups)
            {
                if(group instanceof DefaultSearchFieldGroup)
                {
                    ((DefaultSearchFieldGroup)group).setParentSearchFieldGroup(this);
                }
            }
        }
    }


    public void addSearchField(SearchField field)
    {
        if(!getAllSearchFields().contains(field))
        {
            this.fields.add(field);
        }
        if(field instanceof DefaultSearchField)
        {
            ((DefaultSearchField)field).setGroup(this);
        }
    }


    public void addSearchFieldGroup(SearchFieldGroup group)
    {
        if(!getAllSearchFieldGroups().contains(group))
        {
            this.groups.add(group);
            if(group instanceof DefaultSearchFieldGroup)
            {
                ((DefaultSearchFieldGroup)group).setParentSearchFieldGroup(this);
            }
        }
    }


    public void setParentSearchFieldGroup(SearchFieldGroup parent)
    {
        this.parent = parent;
    }


    public String getLabel()
    {
        return this.label;
    }


    public SearchFieldGroup getParentSearchFieldGroup()
    {
        return this.parent;
    }


    public List<SearchFieldGroup> getSearchFieldGroups()
    {
        return this.groups;
    }


    public List<SearchField> getSearchFields()
    {
        return this.fields;
    }


    public boolean isVisible()
    {
        return !getAllVisibleSearchFieldGroups().isEmpty();
    }


    public List<SearchFieldGroup> getAllHiddenSearchFieldGroups()
    {
        List<SearchFieldGroup> allHiddenGroups = new ArrayList<>();
        if(!isVisible())
        {
            allHiddenGroups.add(this);
        }
        allHiddenGroups.addAll(getHiddenSearchFieldGroups());
        for(SearchFieldGroup hiddenSubGroup : getHiddenSearchFieldGroups())
        {
            allHiddenGroups.addAll(hiddenSubGroup.getAllHiddenSearchFieldGroups());
        }
        return Collections.unmodifiableList(allHiddenGroups);
    }


    public List<SearchField> getAllHiddenSearchFields()
    {
        List<SearchField> allHiddenFields = new ArrayList<>();
        allHiddenFields.addAll(getHiddenSearchFields());
        for(SearchFieldGroup hiddenSubGroup : getHiddenSearchFieldGroups())
        {
            allHiddenFields.addAll(hiddenSubGroup.getAllHiddenSearchFields());
        }
        return Collections.unmodifiableList(allHiddenFields);
    }


    public List<SearchFieldGroup> getAllSearchFieldGroups()
    {
        List<SearchFieldGroup> allGroups = new ArrayList<>();
        allGroups.addAll(this.groups);
        for(SearchFieldGroup group : this.groups)
        {
            allGroups.addAll(group.getAllSearchFieldGroups());
        }
        return Collections.unmodifiableList(allGroups);
    }


    public List<SearchField> getAllSearchFields()
    {
        List<SearchField> allFields = new ArrayList<>();
        allFields.addAll(this.fields);
        for(SearchFieldGroup group : this.groups)
        {
            allFields.addAll(group.getAllSearchFields());
        }
        return Collections.unmodifiableList(allFields);
    }


    public List<SearchFieldGroup> getAllVisibleSearchFieldGroups()
    {
        List<SearchFieldGroup> allVisibleGroups = new ArrayList<>();
        if(!getVisibleSearchFields().isEmpty())
        {
            allVisibleGroups.add(this);
        }
        allVisibleGroups.addAll(getVisibleSearchFieldGroups());
        for(SearchFieldGroup visibleSubGroup : getVisibleSearchFieldGroups())
        {
            allVisibleGroups.addAll(visibleSubGroup.getAllVisibleSearchFieldGroups());
        }
        return Collections.unmodifiableList(allVisibleGroups);
    }


    public List<SearchField> getAllVisibleSearchFields()
    {
        List<SearchField> allVisibleFields = new ArrayList<>();
        allVisibleFields.addAll(getVisibleSearchFields());
        for(SearchFieldGroup visibleSubGroup : getVisibleSearchFieldGroups())
        {
            allVisibleFields.addAll(visibleSubGroup.getAllVisibleSearchFields());
        }
        return Collections.unmodifiableList(allVisibleFields);
    }


    public List<SearchFieldGroup> getHiddenSearchFieldGroups()
    {
        List<SearchFieldGroup> hiddenGroups = new ArrayList<>();
        hiddenGroups.addAll(getSearchFieldGroups());
        hiddenGroups.removeAll(getVisibleSearchFieldGroups());
        return Collections.unmodifiableList(hiddenGroups);
    }


    public List<SearchField> getHiddenSearchFields()
    {
        List<SearchField> hiddenFields = new ArrayList<>();
        hiddenFields.addAll(getSearchFields());
        hiddenFields.removeAll(getVisibleSearchFields());
        return Collections.unmodifiableList(hiddenFields);
    }


    public List<SearchFieldGroup> getVisibleSearchFieldGroups()
    {
        List<SearchFieldGroup> visibleGroups = new ArrayList<>();
        for(SearchFieldGroup group : this.groups)
        {
            if(group.isVisible())
            {
                visibleGroups.add(group);
            }
        }
        return Collections.unmodifiableList(visibleGroups);
    }


    public List<SearchField> getVisibleSearchFields()
    {
        List<SearchField> visibleFields = new ArrayList<>();
        for(SearchField field : getSearchFields())
        {
            if(field.isVisible())
            {
                visibleFields.add(field);
            }
        }
        return visibleFields;
    }


    public List<SearchFieldGroup> getAllPartiallyVisibleGroups()
    {
        List<SearchFieldGroup> partiallyHiddenGroups = new ArrayList<>();
        if(!getAllHiddenSearchFieldGroups().isEmpty() || !getAllHiddenSearchFields().isEmpty())
        {
            partiallyHiddenGroups.add(this);
            for(SearchFieldGroup subGroup : getSearchFieldGroups())
            {
                partiallyHiddenGroups.addAll(subGroup.getAllPartiallyVisibleGroups());
            }
        }
        return partiallyHiddenGroups;
    }


    public List<SearchFieldGroup> getPartiallyVisibleGroups()
    {
        List<SearchFieldGroup> partiallyHiddenGroups = new ArrayList<>();
        for(SearchFieldGroup subGroup : getSearchFieldGroups())
        {
            List<SearchFieldGroup> tmpSubGroup = subGroup.getPartiallyVisibleGroups();
            if(!tmpSubGroup.isEmpty() || !subGroup.getHiddenSearchFields().isEmpty())
            {
                partiallyHiddenGroups.add(subGroup);
            }
        }
        return partiallyHiddenGroups;
    }
}
