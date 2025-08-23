package de.hybris.platform.servicelayer.model.visitor;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ItemVisitorRegistry
{
    private static final String EXCLAMATION = "!";
    private TypeService typeService;
    private ItemVisitor defaultItemVisitor;
    private Map<String, ItemVisitor<? extends ItemModel>> visitors;
    private final Map<String, ItemVisitor<? extends ItemModel>> visitorsCache = new ConcurrentHashMap<>();


    public ItemVisitor getVisitorByTypeCode(String typeCode)
    {
        ItemVisitor<? extends ItemModel> theVisitor = this.visitorsCache.get(typeCode);
        if(theVisitor == null)
        {
            if(StringUtils.isNotBlank(typeCode))
            {
                theVisitor = this.visitors.get(composeVisitorKey(typeCode));
                if(theVisitor == null)
                {
                    ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(typeCode);
                    Collection<ComposedTypeModel> allSuperTypes = composedType.getAllSuperTypes();
                    List<ComposedTypeModel> typesToCheck = Lists.newArrayList((Object[])new ComposedTypeModel[] {composedType});
                    typesToCheck.addAll(allSuperTypes);
                    for(ComposedTypeModel theType : typesToCheck)
                    {
                        theVisitor = this.visitors.get(theType.getCode());
                        if(theVisitor != null)
                        {
                            break;
                        }
                    }
                }
            }
            if(theVisitor == null)
            {
                theVisitor = this.defaultItemVisitor;
            }
            this.visitorsCache.put(typeCode, theVisitor);
        }
        return theVisitor;
    }


    private String composeVisitorKey(String existTypeCode)
    {
        return String.format("%s%s", new Object[] {existTypeCode, "!"});
    }


    private void resetVisitorsCache()
    {
        if(!this.visitorsCache.isEmpty())
        {
            this.visitorsCache.clear();
        }
    }


    @Required
    public void setDefaultVisitor(ItemVisitor defaultItemVisitor)
    {
        resetVisitorsCache();
        this.defaultItemVisitor = defaultItemVisitor;
    }


    public ItemVisitor getDefaultItemVisitor()
    {
        return this.defaultItemVisitor;
    }


    @Required
    public void setVisitors(Map<String, ItemVisitor<? extends ItemModel>> visitors)
    {
        resetVisitorsCache();
        this.visitors = visitors;
    }


    public Map<String, ItemVisitor<? extends ItemModel>> getVisitors()
    {
        return (this.visitors != null) ? Collections.<String, ItemVisitor<? extends ItemModel>>unmodifiableMap(this.visitors) : Collections.<String, ItemVisitor<? extends ItemModel>>emptyMap();
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }
}
