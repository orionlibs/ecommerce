package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import java.util.HashMap;
import java.util.Map;

public class ViewAttributeDescriptor extends AttributeDescriptor
{
    static final String POSITION = "position";
    static final String PARAM = "param";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap myAttr = new Item.ItemAttributeMap((Map)allAttributes);
        boolean isParam = Boolean.TRUE.equals(myAttr.get("param"));
        myAttr.put(READABLE, Boolean.TRUE);
        myAttr.put(WRITABLE, Boolean.FALSE);
        myAttr.put(REMOVABLE, Boolean.TRUE);
        myAttr.put(PARTOF, Boolean.FALSE);
        myAttr.put(SEARCH, isParam ? Boolean.TRUE : Boolean.FALSE);
        myAttr.put(INITIAL, Boolean.FALSE);
        myAttr.put(PRIVATE, Boolean.FALSE);
        myAttr.put(PROPERTY, Boolean.FALSE);
        myAttr.put(LOCALIZED, Boolean.FALSE);
        return super.createItem(ctx, type, myAttr);
    }


    public int getPosition()
    {
        Integer i = (Integer)getProperty("position");
        return (i != null) ? i.intValue() : 0;
    }


    public void setPosition(int pos)
    {
        setProperty("position", Integer.valueOf(pos));
    }


    public boolean isParam()
    {
        return Boolean.TRUE.equals(getProperty("param"));
    }


    public void setParam(boolean isParam)
    {
        setProperty("param", isParam ? Boolean.TRUE : Boolean.FALSE);
        super.setSearchable(isParam);
    }


    public boolean isSearchable()
    {
        boolean b = super.isSearchable();
        return (b || isParam());
    }


    public boolean isProperty()
    {
        return false;
    }


    public String getPersistenceQualifier()
    {
        return null;
    }


    public void setPrivate(boolean b)
    {
    }


    public void setProperty(boolean b)
    {
    }


    public void setLocalized(boolean b)
    {
    }


    public void setWritable(boolean b)
    {
    }


    public void setReadable(boolean b)
    {
    }


    public void setSearchable(boolean b)
    {
    }


    public void setPartOf(boolean b)
    {
    }


    public void setInitial(boolean b)
    {
    }


    public void setRemovable(boolean b)
    {
    }


    protected Map getXMLCustomProperties()
    {
        Map<Object, Object> ret = new HashMap<>();
        ret.put("param", isParam() ? "Boolean.TRUE" : "Boolean.FALSE");
        ret.put("position", "new Integer(" + getPosition() + ")");
        return ret;
    }
}
