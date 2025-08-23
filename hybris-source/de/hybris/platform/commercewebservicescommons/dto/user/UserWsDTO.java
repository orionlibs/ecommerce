package de.hybris.platform.commercewebservicescommons.dto.user;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

@ApiModel(value = "User", description = "Representation of an User")
public class UserWsDTO extends PrincipalWsDTO
{
    @ApiModelProperty(name = "defaultAddress", value = "User address")
    private AddressWsDTO defaultAddress;
    @ApiModelProperty(name = "titleCode", value = "User title code")
    private String titleCode;
    @ApiModelProperty(name = "title", value = "User title")
    private String title;
    @ApiModelProperty(name = "firstName", value = "User first name")
    private String firstName;
    @ApiModelProperty(name = "lastName", value = "User last name")
    private String lastName;
    @ApiModelProperty(name = "currency", value = "User preferred currency")
    private CurrencyWsDTO currency;
    @ApiModelProperty(name = "language", value = "User preferred language")
    private LanguageWsDTO language;
    @ApiModelProperty(name = "displayUid", value = "User identifier")
    private String displayUid;
    @ApiModelProperty(name = "customerId", value = "Customer identifier")
    private String customerId;
    @ApiModelProperty(name = "deactivationDate", value = "Deactivation date")
    private Date deactivationDate;
    @ApiModelProperty(name = "orgUnit", value = "The unit of the User", example = "Rustic")
    private B2BUnitWsDTO orgUnit;
    @ApiModelProperty(name = "roles")
    private List<String> roles;
    @ApiModelProperty(name = "approvers", value = "List of organizational approvers")
    private List<UserWsDTO> approvers;
    @ApiModelProperty(name = "selected", value = "Boolean flag of whether the user is selected", example = "true")
    private Boolean selected;
    @ApiModelProperty(name = "active", value = "Boolean flag of whether the user is active/enabled or not", example = "true")
    private Boolean active;
    @ApiModelProperty(name = "email", value = "Email of the user", example = "mark.rivers@rustic-hw.com")
    private String email;
    @ApiModelProperty(name = "units")
    private List<B2BUnitWsDTO> units;
    @ApiModelProperty(name = "accountPreferences")
    private String accountPreferences;


    public void setDefaultAddress(AddressWsDTO defaultAddress)
    {
        this.defaultAddress = defaultAddress;
    }


    public AddressWsDTO getDefaultAddress()
    {
        return this.defaultAddress;
    }


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getFirstName()
    {
        return this.firstName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getLastName()
    {
        return this.lastName;
    }


    public void setCurrency(CurrencyWsDTO currency)
    {
        this.currency = currency;
    }


    public CurrencyWsDTO getCurrency()
    {
        return this.currency;
    }


    public void setLanguage(LanguageWsDTO language)
    {
        this.language = language;
    }


    public LanguageWsDTO getLanguage()
    {
        return this.language;
    }


    public void setDisplayUid(String displayUid)
    {
        this.displayUid = displayUid;
    }


    public String getDisplayUid()
    {
        return this.displayUid;
    }


    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }


    public String getCustomerId()
    {
        return this.customerId;
    }


    public void setDeactivationDate(Date deactivationDate)
    {
        this.deactivationDate = deactivationDate;
    }


    public Date getDeactivationDate()
    {
        return this.deactivationDate;
    }


    public void setOrgUnit(B2BUnitWsDTO orgUnit)
    {
        this.orgUnit = orgUnit;
    }


    public B2BUnitWsDTO getOrgUnit()
    {
        return this.orgUnit;
    }


    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }


    public List<String> getRoles()
    {
        return this.roles;
    }


    public void setApprovers(List<UserWsDTO> approvers)
    {
        this.approvers = approvers;
    }


    public List<UserWsDTO> getApprovers()
    {
        return this.approvers;
    }


    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }


    public Boolean getSelected()
    {
        return this.selected;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setUnits(List<B2BUnitWsDTO> units)
    {
        this.units = units;
    }


    public List<B2BUnitWsDTO> getUnits()
    {
        return this.units;
    }


    public void setAccountPreferences(String accountPreferences)
    {
        this.accountPreferences = accountPreferences;
    }


    public String getAccountPreferences()
    {
        return this.accountPreferences;
    }
}
