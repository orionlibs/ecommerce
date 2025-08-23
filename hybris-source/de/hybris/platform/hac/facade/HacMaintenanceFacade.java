package de.hybris.platform.hac.facade;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import de.hybris.bootstrap.ddl.DataSourceCreator;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.bootstrap.ddl.tools.TypeSystemHelper;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantPropertiesLoader;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.hac.dao.CreditCardPaymentInfoDao;
import de.hybris.platform.hac.data.dto.DeploymentData;
import de.hybris.platform.hac.data.dto.TypeCodeTableEntry;
import de.hybris.platform.order.strategies.paymentinfo.CreditCardNumberHelper;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.typesystem.YDeploymentJDBC;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacMaintenanceFacade
{
    private static final Logger LOG = Logger.getLogger(HacMaintenanceFacade.class);
    private static final int MAX_TYPE_CODE = 32768;
    private static final String SELECT = "SELECT {";
    private static final String GET_ALL_COMPOSED_TYPES = "SELECT {" + "pk" + "} FROM {" +
                    "ComposedType" +
                    "} " +
                    "ORDER BY {" +
                    "creationtime" +
                    "} DESC, {" +
                    "pk" +
                    "} ASC ";
    private FlexibleSearchService flexibleSearchService;
    private FormatFactory formatFactory;
    private CreditCardNumberHelper creditCardNumberHelper;
    private CreditCardPaymentInfoDao creditCardDao;
    private ModelService modelService;
    private TypeService typeService;


    public List<TypeCodeTableEntry> deploymentType(DeploymentType type)
    {
        switch(null.$SwitchMap$de$hybris$platform$hac$facade$HacMaintenanceFacade$DeploymentType[type.ordinal()])
        {
            case 1:
                return getDeploymentData().getTypesWithDeployment();
            case 2:
                return getDeploymentData().getTypesWithoutDeployment();
            case 3:
                return getDeploymentData().getDeploymentsWithoutTypeCodes();
        }
        return Collections.emptyList();
    }


    private List<ComposedTypeModel> getAllComposedTypes()
    {
        return this.flexibleSearchService.search(GET_ALL_COMPOSED_TYPES).getResult();
    }


    public DeploymentData getDeploymentData()
    {
        int firstFreeTypeCode = 0;
        BitSet usedTypeCodes = new BitSet();
        List<ItemDeployment> usedDepl = new ArrayList<>();
        List<TypeCodeTableEntry> typesWithDeployments = new ArrayList<>();
        List<TypeCodeTableEntry> typesWithoutDeployments = new ArrayList<>();
        List<TypeCodeTableEntry> deploymentsWithoutType = new ArrayList<>();
        for(ComposedTypeModel type : getAllComposedTypes())
        {
            ItemDeployment depl = Registry.getPersistenceManager().getItemDeployment(Registry.getPersistenceManager().getJNDIName(type.getCode()));
            if(depl == null)
            {
                TypeCodeTableEntry entry = new TypeCodeTableEntry();
                entry.setType(type
                                .getCode() + type.getCode() + (type.getAbstract().booleanValue() ? " (Abstract)" : ""));
                entry.setTypePK(type.getPk().toString());
                entry.setExtension(type.getExtensionName());
                typesWithoutDeployments.add(entry);
                continue;
            }
            typesWithDeployments.add(getTypeCodeTableEntry(type, depl));
            usedTypeCodes.set(depl.getTypeCode());
            usedDepl.add(depl);
        }
        for(int i = 0; i < 32768; i++)
        {
            ItemDeployment depl = Registry.getPersistenceManager().getItemDeployment(i);
            if(depl != null && !usedDepl.contains(depl))
            {
                deploymentsWithoutType.add(getTypeCodeTableEntry(depl));
            }
            if(!usedTypeCodes.get(i) && firstFreeTypeCode == 0)
            {
                firstFreeTypeCode = i;
            }
        }
        return (new DeploymentData.Builder())
                        .withDeploymentsWithoutTypeCodes(deploymentsWithoutType)
                        .withFirstFreeTypeCode(firstFreeTypeCode)
                        .withTypesWithDeployment(typesWithDeployments)
                        .withTypesWithoutDeployment(typesWithoutDeployments)
                        .build();
    }


    private TypeCodeTableEntry getTypeCodeTableEntry(ItemDeployment depl)
    {
        TypeCodeTableEntry entry = new TypeCodeTableEntry();
        entry.setTypecode(Integer.valueOf(depl.getTypeCode()));
        entry.setTable(depl.getDatabaseTableName());
        entry.setPropsTable(depl.getDumpPropertyTableName());
        entry.setPersistent(Boolean.valueOf(YDeploymentJDBC.existsDeployment(depl)));
        entry.setAbstractValue(Boolean.valueOf(depl.isAbstract()));
        entry.setFinalValue(Boolean.valueOf(depl.isFinal()));
        entry.setDeploymentName(depl.getName());
        entry.setSuperDeploymentName(depl.getSuperDeploymentName());
        return entry;
    }


    private TypeCodeTableEntry getTypeCodeTableEntry(ComposedTypeModel type, ItemDeployment depl)
    {
        TypeCodeTableEntry entry = new TypeCodeTableEntry();
        entry.setTypecode(Integer.valueOf(depl.getTypeCode()));
        entry.setType(type.getCode() + type.getCode() + (type.getAbstract().booleanValue() ? " (Abstract)" : ""));
        entry.setTypePK(type.getPk().toString());
        entry.setTable(depl.getDatabaseTableName());
        entry.setPropsTable(depl.getDumpPropertyTableName());
        entry.setExtension(type.getExtensionName());
        entry.setPersistent(Boolean.valueOf(YDeploymentJDBC.existsDeployment(depl)));
        entry.setAbstractValue(Boolean.valueOf(depl.isAbstract()));
        entry.setFinalValue(Boolean.valueOf(depl.isFinal()));
        entry.setDeploymentName(depl.getName());
        entry.setSuperDeploymentName(depl.getSuperDeploymentName());
        return entry;
    }


    private <T> T get(ItemModel model, String attribute)
    {
        return (T)this.modelService.getAttributeValue(model, attribute);
    }


    public List<MediaModel> getMediasForPk(String pkString)
    {
        String query = "SELECT {pk}FROM {Media} WHERE {pk}= ?myPK OR {dataPK}=?myPK";
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {pk}FROM {Media} WHERE {pk}= ?myPK OR {dataPK}=?myPK");
        searchQuery.addQueryParameter("myPK", Long.valueOf(pkString));
        SearchResult<MediaModel> searchResult = this.flexibleSearchService.search(searchQuery);
        return searchResult.getResult();
    }


    public Map<String, Object> getUnencryptedCreditCards(int start, int count, int orderByNumCol, CreditCardPaymentInfoDao.SortDirection sortDirection)
    {
        Map<String, Object> result = new HashMap<>();
        List<List<Object>> cards = new ArrayList<>();
        Object object = new Object(this, cards);
        object.readAndProcessRange(start, count, orderByNumCol, sortDirection);
        result.put("iTotalRecords", Integer.valueOf(object.getTotalCount()));
        result.put("iTotalDisplayRecords", Integer.valueOf(object.getTotalCount()));
        result.put("aaData", cards);
        return result;
    }


    private String getFormattedDate(Date date)
    {
        if(date != null)
        {
            DateFormat dateTimeFormat = this.formatFactory.createDateTimeFormat(1, 1);
            return dateTimeFormat.format(date);
        }
        return null;
    }


    public void resaveUnencryptedCreditCardNumbers()
    {
        Object object = new Object(this);
        object.readAndProcessAll();
    }


    public boolean isNumberEncryptedForCreditCard()
    {
        ComposedTypeModel typeModel = this.typeService.getComposedTypeForClass(CreditCardPaymentInfoModel.class);
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(typeModel, "number");
        return attributeDescriptor.getEncrypted().booleanValue();
    }


    public void removeTypeSystem(String typeSystemName) throws IOException
    {
        TypeSystemHelper.removeTypeSystem(getDataSourceCreator(), getPropertiesLoader(), typeSystemName);
    }


    public Map<String, String> getRemovableTypeSystems()
    {
        PersistenceInformation persistenceInformation = new PersistenceInformation(getDataSourceCreator(), getPropertiesLoader());
        Set<String> typeSystemNames = new HashSet<>(persistenceInformation.getAllTypeSystems());
        typeSystemNames.remove(Config.getString("db.type.system.name", "DEFAULT"));
        return (Map<String, String>)Maps.uniqueIndex(typeSystemNames, (Function)new Object(this));
    }


    private DataSourceCreator getDataSourceCreator()
    {
        return (DataSourceCreator)new Object(this);
    }


    private PropertiesLoader getPropertiesLoader()
    {
        return (PropertiesLoader)new TenantPropertiesLoader(Registry.getCurrentTenant());
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setFormatFactory(FormatFactory formatFactory)
    {
        this.formatFactory = formatFactory;
    }


    @Required
    public void setCreditCardNumberHelper(CreditCardNumberHelper creditCardNumberHelper)
    {
        this.creditCardNumberHelper = creditCardNumberHelper;
    }


    @Required
    public void setCreditCardDao(CreditCardPaymentInfoDao creditCardDao)
    {
        this.creditCardDao = creditCardDao;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
