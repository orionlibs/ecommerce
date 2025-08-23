package de.hybris.platform.jalo;

import de.hybris.platform.util.JspContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Deprecated(since = "ages", forRemoval = false)
public interface DataCreator
{
    void createData(Map paramMap, JspContext paramJspContext) throws Exception;


    Collection getCreatorParameterNames();


    String getCreatorParameterDefault(String paramString);


    List getCreatorParameterPossibleValues(String paramString);


    String getCreatorName();


    String getCreatorDescription();


    boolean isCreatorDisabled();
}
