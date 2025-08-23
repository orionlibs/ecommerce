package de.hybris.bootstrap.typesystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class YTypeSystemElement extends YNameSpaceElement
{
    private String metaTypeCode;
    private YComposedType metaType;
    private Map<String, String> customProps;
    private boolean autocreate = true;
    private boolean generate = true;


    YTypeSystemElement(YNamespace container)
    {
        super(container);
    }


    public void validate()
    {
        super.validate();
        getMetaType();
    }


    public void resetCaches()
    {
        super.resetCaches();
        this.metaType = null;
    }


    protected abstract String getDefaultMetaTypeCode();


    public String getMetaTypeCode()
    {
        return this.metaTypeCode;
    }


    public YComposedType getMetaType()
    {
        if(this.metaType == null)
        {
            String typeCode = (getMetaTypeCode() != null) ? getMetaTypeCode() : getDefaultMetaTypeCode();
            YType tmp = getTypeSystem().getType(typeCode);
            if(!(tmp instanceof YComposedType))
            {
                throw new IllegalStateException("invalid element (" + this + ") due to missing meta type '" + typeCode + "'");
            }
            this.metaType = (YComposedType)tmp;
        }
        return this.metaType;
    }


    public void setMetaTypeCode(String metaTypeCode)
    {
        this.metaTypeCode = metaTypeCode;
    }


    public Map<String, String> getCustomProps()
    {
        return (this.customProps == null) ? Collections.EMPTY_MAP : Collections.<String, String>unmodifiableMap(this.customProps);
    }


    public void addCustomProperties(Map<String, String> props)
    {
        if(props != null)
        {
            for(Map.Entry<String, String> entry : props.entrySet())
            {
                addCustomProperty(entry.getKey(), entry.getValue());
            }
        }
    }


    public void addCustomProperty(String qualifer, String valueDef)
    {
        if(this.customProps == null)
        {
            this.customProps = new HashMap<>();
        }
        else if(this.customProps.containsKey(qualifer))
        {
            throw new IllegalStateException("custom property '" + qualifer + "' of " + this + " already set to '" + (String)this.customProps
                            .get(qualifer) + " - cannot set to '" + valueDef + "'");
        }
        this.customProps.put(qualifer, valueDef);
    }


    public boolean isAutocreate()
    {
        return this.autocreate;
    }


    public void setAutocreate(boolean autocreate)
    {
        this.autocreate = autocreate;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }


    public void setGenerate(boolean generate)
    {
        this.generate = generate;
    }
}
