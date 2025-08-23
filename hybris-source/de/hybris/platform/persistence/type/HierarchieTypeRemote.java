package de.hybris.platform.persistence.type;

import java.util.List;

public interface HierarchieTypeRemote extends TypeRemote
{
    List getInheritancePath();


    String getInheritancePathString();


    void setInheritancePathString(String paramString);
}
