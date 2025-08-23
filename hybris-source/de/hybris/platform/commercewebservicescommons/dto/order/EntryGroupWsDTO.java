package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(value = "EntryGroup", description = "Representation of an Entry Group")
public class EntryGroupWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "entries", value = "List of order entries")
    private Collection<OrderEntryWsDTO> entries;
    @ApiModelProperty(name = "entryGroups", value = "List of child entry groups")
    private Collection<EntryGroupWsDTO> entryGroups;
    @ApiModelProperty(name = "entryGroupNumber", value = "Identifier of the entry group", example = "1")
    private Integer entryGroupNumber;
    @ApiModelProperty(name = "label", value = "Label for the entry group", example = "Photo On The Go Package")
    private String label;
    @ApiModelProperty(name = "erroneous", value = "Indicates if the entry group is in an error state", example = "true")
    private Boolean erroneous;
    @ApiModelProperty(name = "type", value = "Indicates type of the group, possible values are STANDALONE, CONFIGURABLEBUNDLE or any customer implemented type for any new provider", example = "STANDALONE")
    private String type;


    public void setEntries(Collection<OrderEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public Collection<OrderEntryWsDTO> getEntries()
    {
        return this.entries;
    }


    public void setEntryGroups(Collection<EntryGroupWsDTO> entryGroups)
    {
        this.entryGroups = entryGroups;
    }


    public Collection<EntryGroupWsDTO> getEntryGroups()
    {
        return this.entryGroups;
    }


    public void setEntryGroupNumber(Integer entryGroupNumber)
    {
        this.entryGroupNumber = entryGroupNumber;
    }


    public Integer getEntryGroupNumber()
    {
        return this.entryGroupNumber;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setErroneous(Boolean erroneous)
    {
        this.erroneous = erroneous;
    }


    public Boolean getErroneous()
    {
        return this.erroneous;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }
}
