package de.hybris.platform.jalo;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.ImportExportUserRightsHelper;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.SearchRestriction;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.WebSessionFunctions;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class CoreBasicDataCreator
{
    private static final Logger LOG = Logger.getLogger(CoreBasicDataCreator.class);
    public static final String FORCE_CLEAN = "force.clean";
    public static final String MOVE_PROPS = "move.properties";
    public static final String LOCALIZE_TYPES = "localize.types";
    private SessionContext deCtx;
    private SessionContext enCtx;


    @Deprecated(since = "ages", forRemoval = false)
    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        createBasicC2L();
        createBasicUnits();
        createBasicUserGroups();
        updateExistingRestrictionsActiveFlag();
        createBasicRestrictions();
        createBasicTypesSecurity();
        createBasicSavedQueries();
        localizeOrderStatus();
        createSupportedEncodings();
        createRootMediaFolder();
    }


    private List<List> getAllCreditCardNumbers()
    {
        return FlexibleSearch.getInstance().search("SELECT {PK}, {number} FROM {CreditCardPaymentInfo}", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {Long.class, String.class}, ), true, true, 0, -1)
                        .getResult();
    }


    public void encryptExistingCreditCardNumbers(JspContext jspc)
    {
        try
        {
            if(!TypeManager.getInstance().getComposedType("CreditCardPaymentInfo").getAttributeDescriptor("number").isEncrypted())
            {
                String msg = "Encryption skipped!\nAttribute 'number' of type CreditCardPaymentInfo is defined as unencrypted in your core-items.xml.<br>Change the modifier of attribute <font color='black'>encrypted</font> from <font color='black'>false</font> to <font color='black'>true</font> and update your system!";
                AbstractSystemCreator.logln(
                                "<br/><font color='red'>Encryption skipped!\nAttribute 'number' of type CreditCardPaymentInfo is defined as unencrypted in your core-items.xml.<br>Change the modifier of attribute <font color='black'>encrypted</font> from <font color='black'>false</font> to <font color='black'>true</font> and update your system!</font>",
                                jspc);
                return;
            }
        }
        catch(JaloItemNotFoundException e)
        {
            return;
        }
        JaloSession jaloSession = JaloSession.getCurrentSession();
        for(List<Long> res : getAllCreditCardNumbers())
        {
            PK pk = PK.fromLong(((Long)res.get(0)).longValue());
            String number = (String)res.get(1);
            if(number != null && number.length() < 21)
            {
                try
                {
                    jaloSession.getItem(pk).setAttribute("number", number);
                }
                catch(JaloInvalidParameterException e)
                {
                    LOG.error(e.getMessage());
                }
                catch(JaloBusinessException e)
                {
                    LOG.error(e.getMessage());
                }
            }
        }
        AbstractSystemCreator.logln("<br/><b>Encryption of creditcard numbers is finished</b>", jspc);
    }


    public boolean checkEncryptionOfCreditCardNumbers() throws JaloBusinessException
    {
        List<List<String>> result = FlexibleSearch.getInstance().search("select {code}, {number} from {CreditCardPaymentInfo}", Collections.EMPTY_MAP, Arrays.asList((Class<?>[][])new Class[] {String.class, String.class}, ), true, true, 0, -1).getResult();
        for(List<String> row : result)
        {
            String type = row.get(0);
            String number = row.get(1);
            if(number != null)
            {
                boolean isValid = false;
                if("visa".equalsIgnoreCase(type))
                {
                    isValid = PaymentInfo.isVisa(number);
                }
                else if("master".equalsIgnoreCase(type))
                {
                    isValid = PaymentInfo.isMaster(number);
                }
                else if("amex".equalsIgnoreCase(type))
                {
                    isValid = PaymentInfo.isAmericanExpress(number);
                }
                else if("diners".equalsIgnoreCase(type))
                {
                    isValid = PaymentInfo.isDiners(number);
                }
                if(!isValid)
                {
                    isValid = (number.length() <= 20);
                }
                if(isValid)
                {
                    return false;
                }
                continue;
            }
            LOG.error("Missing creditcard number! Invalid CreditCardPaymentInfo instance '" + type + "' found!");
        }
        return true;
    }


    public boolean hasPINAttribute()
    {
        try
        {
            ComposedType composedType = TypeManager.getInstance().getComposedType("CreditCardPaymentInfo");
            return (composedType.getAttributeDescriptorIncludingPrivate("pin") != null);
        }
        catch(JaloItemNotFoundException e)
        {
            return false;
        }
    }


    public void dropPINAttributeDescriptor(JspContext jspc)
    {
        ComposedType composedType;
        AttributeDescriptor attrib;
        try
        {
            composedType = TypeManager.getInstance().getComposedType("CreditCardPaymentInfo");
            attrib = composedType.getAttributeDescriptorIncludingPrivate("pin");
        }
        catch(JaloItemNotFoundException e)
        {
            return;
        }
        String table = composedType.getTable();
        String column = attrib.getDatabaseColumn();
        try
        {
            AbstractSystemCreator.logln("removing AttributeDescriptor(PIN)... " + attrib, jspc);
            attrib.remove();
        }
        catch(ConsistencyCheckException e)
        {
            LOG.error(e.getMessage());
        }
        if(column != null)
        {
            Tenant tenant = Registry.getCurrentTenant();
            Connection conn = null;
            Statement stmt = null;
            try
            {
                AbstractSystemCreator.logln("cleaning DB column '" + column + "'... ", jspc);
                conn = tenant.getDataSource().getConnection();
                tenant.getCache().clear();
                stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE " + table + " SET " + column + " = ''");
            }
            catch(SQLException e)
            {
                throw new JaloSystemException(e);
            }
            finally
            {
                if(stmt != null)
                {
                    try
                    {
                        stmt.close();
                        stmt = null;
                    }
                    catch(SQLException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(e.getMessage());
                        }
                    }
                }
                if(conn != null)
                {
                    try
                    {
                        conn.close();
                        conn = null;
                    }
                    catch(SQLException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(e.getMessage());
                        }
                    }
                }
            }
        }
        AbstractSystemCreator.logln("<br/><b>Removal of creditcard PIN's is finished</b>", jspc);
    }


    public void createRootMediaFolder()
    {
        try
        {
            MediaManager.getInstance().getRootMediaFolder();
        }
        catch(JaloSystemException e)
        {
            MediaFolder rootFolder = MediaManager.getInstance().createMediaFolder("root", null);
            LOG.info("created the root folder " + rootFolder);
        }
    }


    public void localizeOrderStatus()
    {
        EnumerationManager enumerationManager = EnumerationManager.getInstance();
        EnumerationValue enumerationValue = enumerationManager.getEnumerationValue(enumerationManager
                        .getEnumerationType("OrderStatus"), "CREATED");
        enumerationValue.setName(this.deCtx, "Angelegt");
        enumerationValue.setName(this.enCtx, "Created");
        enumerationValue = enumerationManager.getEnumerationValue(enumerationManager
                        .getEnumerationType("OrderStatus"), "ON_VALIDATION");
        enumerationValue.setName(this.deCtx, "Zur Bestätigung");
        enumerationValue.setName(this.enCtx, "On validation");
        enumerationValue = enumerationManager.getEnumerationValue(enumerationManager
                        .getEnumerationType("OrderStatus"), "COMPLETED");
        enumerationValue.setName(this.deCtx, "Komplett");
        enumerationValue.setName(this.enCtx, "Completed");
        enumerationValue = enumerationManager.getEnumerationValue(enumerationManager
                        .getEnumerationType("OrderStatus"), "CANCELLED");
        enumerationValue.setName(this.deCtx, "Storniert");
        enumerationValue.setName(this.enCtx, "Cancelled");
    }


    public void createEmptySystemOrUpdate(Map<?, ?> params, JspContext jspc) throws Exception
    {
        AbstractSystemCreator.logln("Please wait. This step can take some minutes to complete.", jspc);
        AbstractSystemCreator.logln("If you do not receive any feedback on this page in this time, consult the applicationserver logs for possible errors.", jspc);
        if(Initialization.forceClean(params))
        {
            AbstractSystemCreator.logln("Dropping old and creating new empty system ... ", jspc);
        }
        else
        {
            AbstractSystemCreator.logln("Updating system... ", jspc);
        }
        Map<Object, Object> initProp = new HashMap<>(params);
        initProp.put("jspc", jspc);
        Initialization.initialize(initProp, null);
        HttpSession session = null;
        if(jspc.getServletRequest() != null)
        {
            session = jspc.getServletRequest().getSession();
            WebSessionFunctions.clearSession(session);
        }
        if(jspc.getServletResponse() != null)
        {
            WebSessionFunctions.getSession(Collections.EMPTY_MAP, null, session, jspc
                            .getServletRequest(), jspc.getServletResponse());
        }
    }


    public void createBasicC2L()
    {
        try
        {
            Language de, en;
            Currency eur;
            Country country_de;
            try
            {
                de = C2LManager.getInstance().getLanguageByIsoCode("de");
            }
            catch(JaloItemNotFoundException e)
            {
                de = C2LManager.getInstance().createLanguage("de");
            }
            de.setActive(true);
            JaloSession.getCurrentSession().getSessionContext().setLanguage(de);
            try
            {
                en = C2LManager.getInstance().getLanguageByIsoCode("en");
            }
            catch(JaloItemNotFoundException e)
            {
                en = C2LManager.getInstance().createLanguage("en");
            }
            en.setActive(true);
            this.deCtx = JaloSession.getCurrentSession().createSessionContext();
            this.enCtx = JaloSession.getCurrentSession().createSessionContext();
            this.deCtx.setLanguage(de);
            this.enCtx.setLanguage(en);
            en.setName(this.enCtx, "English");
            en.setName(this.deCtx, "Englisch");
            de.setName(this.enCtx, "German");
            de.setName(this.deCtx, "Deutsch");
            try
            {
                eur = C2LManager.getInstance().getCurrencyByIsoCode("EUR");
            }
            catch(JaloItemNotFoundException e)
            {
                eur = C2LManager.getInstance().createCurrency("EUR");
            }
            eur.setActive(true);
            eur.setName(this.enCtx, "Euro");
            eur.setName(this.deCtx, "Euro");
            eur.setSymbol("€");
            eur.setDigits(2);
            eur.setConversionFactor(1.0D);
            C2LManager.getInstance().setBaseCurrency(eur);
            JaloSession.getCurrentSession().getSessionContext().setCurrency(eur);
            try
            {
                country_de = C2LManager.getInstance().getCountryByIsoCode("DE");
            }
            catch(JaloItemNotFoundException e)
            {
                country_de = C2LManager.getInstance().createCountry("DE");
            }
            country_de.setActive(true);
            country_de.setName(this.enCtx, "Germany");
            country_de.setName(this.deCtx, "Deutschland");
            try
            {
                C2LManager.getInstance().getCurrencyByIsoCode("---").remove();
            }
            catch(Exception exception)
            {
            }
            try
            {
                Language lang = C2LManager.getInstance().getLanguageByIsoCode("---");
                lang.setActive(false);
                lang.remove();
            }
            catch(Exception exception)
            {
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public void createBasicUnits()
    {
        Unit unit_pieces;
        Collection<Unit> coll = ProductManager.getInstance().getUnitsByCode("pieces");
        if(coll.size() == 1)
        {
            unit_pieces = coll.iterator().next();
        }
        else
        {
            unit_pieces = ProductManager.getInstance().createUnit("pieces", "pieces");
        }
        unit_pieces.setName(this.deCtx, "Stück");
        unit_pieces.setName(this.enCtx, "piece");
        unit_pieces.setConversionFactor(1.0D);
    }


    public void createBasicUserGroups() throws Exception
    {
        UserGroup userGroup = UserManager.getInstance().getAdminUserGroup();
        userGroup.setLocName(this.enCtx, "Administrator group");
        userGroup.setLocName(this.deCtx, "Administratorgruppe");
        UserGroup employeeGroup = null;
        try
        {
            employeeGroup = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
        }
        catch(JaloItemNotFoundException e)
        {
            employeeGroup = UserManager.getInstance().createUserGroup(Constants.USER.EMPLOYEE_USERGROUP);
        }
        ComposedType employeeT = TypeManager.getInstance().getComposedType(Employee.class);
        Set<UserGroup> employeegrouplist = new LinkedHashSet();
        employeegrouplist.add(employeeGroup);
        employeeT.getAttributeDescriptor("groups").setDefaultValue(employeegrouplist);
        employeeGroup.setLocName(this.enCtx, "Employee group");
        employeeGroup.setLocName(this.deCtx, "Mitarbeitergruppe");
        UserGroup customerGroup = null;
        try
        {
            customerGroup = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.CUSTOMER_USERGROUP);
        }
        catch(JaloItemNotFoundException e)
        {
            customerGroup = UserManager.getInstance().createUserGroup(Constants.USER.CUSTOMER_USERGROUP);
        }
        ComposedType customerT = TypeManager.getInstance().getComposedType(Customer.class);
        Set<UserGroup> customergrouplist = new LinkedHashSet();
        customergrouplist.add(customerGroup);
        customerT.getAttributeDescriptor("groups").setDefaultValue(customergrouplist);
        UserManager.getInstance().getAnonymousCustomer().addToGroup((PrincipalGroup)customerGroup);
        customerGroup.setLocName(this.enCtx, "Customer group");
        customerGroup.setLocName(this.deCtx, "Kundengruppe");
    }


    public void updateExistingRestrictionsActiveFlag()
    {
        List existing = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(SearchRestriction.class).getCode() + "} WHERE {active} IS NULL ", Collections.EMPTY_MAP, Collections.singletonList(SearchRestriction.class), true, true, 0, -1)
                        .getResult();
        for(Iterator<SearchRestriction> it = existing.iterator(); it.hasNext(); )
        {
            ((SearchRestriction)it.next()).setActive(true);
        }
    }


    public void createBasicRestrictions() throws Exception
    {
        UserGroup employees = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
        TypeManager typeManager = TypeManager.getInstance();
        if(typeManager.getSearchRestriction(typeManager.getComposedType(Principal.class), "HideSystemPrincipals") == null)
        {
            TypeManager.getInstance()
                            .createRestriction("HideSystemPrincipals", (Principal)employees, typeManager.getComposedType(Principal.class), "{uid} not in ( 'anonymous', 'admin', 'admingroup' )");
        }
    }


    public void createBasicSavedQueries() throws Exception
    {
        ComposedType sqt = TypeManager.getInstance().getComposedType(SavedQuery.class);
        AtomicType stringType = TypeManager.getInstance().getRootAtomicType(String.class);
        AtomicType dateType = TypeManager.getInstance().getRootAtomicType(Date.class);
        SavedQuery savedQuery = FlexibleSearch.getInstance().getSavedQuery("CodeOrNameQuery");
        if(savedQuery == null)
        {
            Map<Object, Object> attributes = new HashMap<>();
            attributes.put("code", "CodeOrNameQuery");
            attributes.put("resultType", TypeManager.getInstance().getComposedType(Product.class));
            attributes.put("query", "SELECT {p:" + Item.PK + "} FROM {$$$ AS p} WHERE {p:code} LIKE ?txt OR {p:name:o} LIKE ?txt ORDER BY {p:code} ASC");
            attributes.put("params", Collections.singletonMap("txt", stringType));
            savedQuery = (SavedQuery)sqt.newInstance(attributes);
        }
        savedQuery.setName(this.deCtx, "Name oder Code");
        savedQuery.setName(this.enCtx, "Name or code");
        savedQuery.setLocalizedProperty(this.deCtx, "description", "LIKE Suche nach Code x ODER Name x");
        savedQuery.setLocalizedProperty(this.enCtx, "description", "LIKE Search for code x OR name x");
        savedQuery = FlexibleSearch.getInstance().getSavedQuery("ModifiedItemQuery");
        if(savedQuery == null)
        {
            Map<Object, Object> attributes = new HashMap<>();
            Map<Object, Object> params = new HashMap<>();
            params.put("startDate", dateType);
            params.put("endDate", dateType);
            attributes.put("code", "ModifiedItemQuery");
            attributes.put("resultType", TypeManager.getInstance().getComposedType(Item.class));
            attributes.put("query", "SELECT {" + Item.PK + "} FROM {$$$} WHERE {" + Item.MODIFIED_TIME + "} >= ?startDate AND {" + Item.MODIFIED_TIME + "} <=?endDate ORDER BY {" + Item.MODIFIED_TIME + "} DESC");
            attributes.put("params", params);
            savedQuery = (SavedQuery)sqt.newInstance(attributes);
        }
        savedQuery.setName(this.deCtx, "Geändert im Zeitraum");
        savedQuery.setName(this.enCtx, "Modified between");
        savedQuery.setLocalizedProperty(this.deCtx, "description", "findet Items welche in einem bestimmten Zeitraum geändert wurden");
        savedQuery.setLocalizedProperty(this.enCtx, "description", "finds items changed within a given timeframe");
    }


    public void createBasicTypesSecurity() throws Exception
    {
        String filename = "/core/userrights.csv";
        InputStream inputStream = CoreBasicDataCreator.class.getResourceAsStream("/core/userrights.csv");
        InputStreamReader isr = new InputStreamReader(inputStream);
        if(inputStream != null)
        {
            ImportExportUserRightsHelper helper = new ImportExportUserRightsHelper(isr, '"', ';', ',', false);
            helper.importSecurity();
        }
        else
        {
            throw new JaloSystemException("Ressource /core/userrights.csv in ressources/jar/ not found - aborting import and setting TypeRights", 5566);
        }
    }


    public void createSupportedEncodings()
    {
        EnumerationManager enumerationManager = EnumerationManager.getInstance();
        EnumerationType encodingEnum = EnumerationManager.getInstance().getEnumerationType(Constants.ENUMS.ENCODINGENUM);
        List<EnumerationValue> encodings = new ArrayList<>();
        for(String code : Charset.availableCharsets().keySet())
        {
            try
            {
                encodings.add(enumerationManager.getEnumerationValue(encodingEnum, code));
            }
            catch(JaloItemNotFoundException e)
            {
                try
                {
                    EnumerationValue value = enumerationManager.createEnumerationValue(encodingEnum, code);
                    value.setExtensionName("core");
                    encodings.add(value);
                }
                catch(Exception e1)
                {
                    LOG.error("Could not create encoding enum '" + code + " due to: " + e1.getMessage());
                }
            }
        }
        Collections.sort(encodings, (Comparator<? super EnumerationValue>)new Object(this));
        try
        {
            encodingEnum.setAttribute("values", encodings);
        }
        catch(Exception e)
        {
            LOG.error("could not set encoding enums to " + encodings + " due to " + e.getMessage());
        }
    }
}
