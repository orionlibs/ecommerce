package de.hybris.platform.persistence.audit.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.persistence.audit.AuditChangeFilter;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.AuditableChange;
import de.hybris.platform.persistence.audit.AuditableSaver;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.TypeAuditRecordCommand;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.internal.LocalizedAttributesList;
import de.hybris.platform.persistence.audit.internal.ValuesContainer;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditableSaver implements AuditableSaver
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditableSaver.class);
    private static final String UNKNOWN_USER_UID = "<unknown>";
    public static final String ACTING_USER = "actingUser";
    private static final String ACTING_USER_SESSION_ATTR_KEY = "ACTING_USER_UID";
    private UserService userService;
    private WriteAuditGateway writeAuditGateway;
    private SessionService sessionService;
    private List<AuditChangeFilter> auditChangeFilters;


    public void storeAudit(Collection<AuditableChange> auditableChanges)
    {
        List<AuditableChange> changesForSaving = filterAuditChangesForSaving(auditableChanges);
        List<TypeAuditRecordCommand> typeCmdList = new ArrayList<>();
        List<LinkAuditRecordCommand> linkCmdList = new ArrayList<>();
        changesForSaving.stream()
                        .filter(AuditableChange::isMeaningful)
                        .map(DefaultAuditableSaver::setUpValuesContainer)
                        .forEach(container -> prepareAuditRecordsCommands(container, typeCmdList, linkCmdList));
        this.writeAuditGateway.saveTypeAuditRecords(typeCmdList);
        this.writeAuditGateway.saveLinkAuditRecords(linkCmdList);
    }


    private List<AuditableChange> filterAuditChangesForSaving(Collection<AuditableChange> auditableChanges)
    {
        Set<AuditableChange> ignoredChanges = (Set<AuditableChange>)auditableChanges.stream().filter(i -> this.auditChangeFilters.stream().anyMatch(())).collect(Collectors.toSet());
        return (List<AuditableChange>)auditableChanges.stream().filter(i -> !ignoredChanges.contains(i))
                        .collect(Collectors.toList());
    }


    private void prepareAuditRecordsCommands(ValuesContainer container, List<TypeAuditRecordCommand> typeList, List<LinkAuditRecordCommand> linkList)
    {
        LOG.debug("Saving audit record: {}", container);
        if(container.isRelationType())
        {
            linkList.add(toLinkAuditRecordCommand(container));
        }
        else
        {
            typeList.add(toTypeAuditRecordCommand(container));
        }
    }


    private static ValuesContainer setUpValuesContainer(AuditableChange containerTuple)
    {
        AuditType auditType = calculateAuditType(containerTuple);
        SLDDataContainer containerToGetItem = (containerTuple.getAfter() != null) ? containerTuple.getAfter() : containerTuple.getBefore();
        String typeCode = extractTypeCode(containerTuple);
        Map<String, Boolean> blacklistedAttributes = findBlacklistedAttributes(containerTuple.getBefore(), containerTuple
                        .getAfter(), typeCode);
        ValuesContainer.Builder builder = ValuesContainer.builder().withAuditType(auditType).withPk(containerToGetItem.getPk()).withTypePk(containerToGetItem.getTypePk()).withValuesBefore(getLocalizedAttributes(containerTuple.getBefore(), blacklistedAttributes))
                        .withValuesAfter(getLocalizedAttributes(containerTuple.getAfter(), blacklistedAttributes)).withContext(createContext(blacklistedAttributes));
        return builder.build();
    }


    private static Map<String, Object> createContext(Map<String, Boolean> blacklistedAttributes)
    {
        return BlacklistedAttributesHelper.createContext(blacklistedAttributes);
    }


    private static Map<String, Boolean> findBlacklistedAttributes(SLDDataContainer containerBefore, SLDDataContainer containerAfter, String typeCode)
    {
        if(Objects.isNull(containerAfter) && Objects.isNull(containerBefore))
        {
            throw new IllegalArgumentException("Both containers cannot be null at once");
        }
        List<SLDDataContainer.AttributeValue> allAttributesBefore = (containerBefore == null) ? Collections.<SLDDataContainer.AttributeValue>emptyList() : containerBefore.getAllAttributes();
        List<SLDDataContainer.AttributeValue> allAttributesAfter = (containerAfter == null) ? Collections.<SLDDataContainer.AttributeValue>emptyList() : containerAfter.getAllAttributes();
        Set<String> blacklistedAttributes = BlacklistedAttributesHelper.getBlacklistedAttributesForTypeHierarchy(typeCode);
        if(blacklistedAttributes.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<String, SLDDataContainer.AttributeValue> valuesBefore = (Map<String, SLDDataContainer.AttributeValue>)allAttributesBefore.stream().collect(Collectors.toMap(SLDDataContainer.AttributeValue::getName, attributeValue -> attributeValue));
        Map<String, SLDDataContainer.AttributeValue> valuesAfter = (Map<String, SLDDataContainer.AttributeValue>)allAttributesAfter.stream().collect(Collectors.toMap(SLDDataContainer.AttributeValue::getName, attributeValue -> attributeValue));
        Map<String, Boolean> blacklist = new HashMap<>();
        for(String blacklistedAttribute : blacklistedAttributes)
        {
            SLDDataContainer.AttributeValue before = valuesBefore.get(blacklistedAttribute);
            SLDDataContainer.AttributeValue after = valuesAfter.get(blacklistedAttribute);
            blacklist.put(StringUtils.lowerCase(blacklistedAttribute, LocaleHelper.getPersistenceLocale()), Boolean.valueOf(attributesNotEqual(before, after)));
        }
        return blacklist;
    }


    private static String extractTypeCode(AuditableChange containerTuple)
    {
        return (containerTuple.getBefore() == null) ? containerTuple.getAfter().getTypeCode() :
                        containerTuple.getBefore().getTypeCode();
    }


    private static boolean attributesNotEqual(SLDDataContainer.AttributeValue matchingAttributeBefore, SLDDataContainer.AttributeValue matchingAttributeAfter)
    {
        return (matchingAttributeBefore != null && matchingAttributeAfter != null &&
                        !Objects.equals(matchingAttributeBefore.getValue(), matchingAttributeAfter.getValue()));
    }


    private static AuditType calculateAuditType(AuditableChange tuple)
    {
        if(tuple.getBefore() == null && tuple.getAfter() != null)
        {
            return AuditType.CREATION;
        }
        if(tuple.getBefore() != null && tuple.getAfter() != null)
        {
            return AuditType.MODIFICATION;
        }
        if(tuple.getBefore() != null && tuple.getAfter() == null)
        {
            return AuditType.DELETION;
        }
        throw new IllegalStateException("AuditType not supported");
    }


    private static Map<String, Object> getLocalizedAttributes(SLDDataContainer sldDataContainer, Map<String, Boolean> blacklistedAttributes)
    {
        if(sldDataContainer == null)
        {
            return Collections.emptyMap();
        }
        Map<String, Object> resultAttributes = new HashMap<>();
        for(SLDDataContainer.AttributeValue a : sldDataContainer.getAllAttributes())
        {
            boolean isBlacklisted = blacklistedAttributes.containsKey(a.getName());
            if(a.getLangPk() == null)
            {
                if(isBlacklisted)
                {
                    resultAttributes.put(a.getName(), new SLDDataContainer.AttributeValue(a
                                    .getName(), BlacklistedAttributesHelper.getBlacklistedAttrObfuscationValue()));
                    continue;
                }
                resultAttributes.put(a.getName(), a);
                continue;
            }
            Object storedValue = resultAttributes.get(a.getName());
            SLDDataContainer.AttributeValue valueToAdd = isBlacklisted ? new SLDDataContainer.AttributeValue(a.getName(), BlacklistedAttributesHelper.getBlacklistedAttrObfuscationValue(), a.getLangPk()) : a;
            if(storedValue instanceof LocalizedAttributesList)
            {
                ((LocalizedAttributesList)storedValue).add(valueToAdd);
                continue;
            }
            LocalizedAttributesList list = new LocalizedAttributesList();
            list.add(valueToAdd);
            resultAttributes.put(a.getName(), list);
        }
        return resultAttributes;
    }


    private TypeAuditRecordCommand toTypeAuditRecordCommand(ValuesContainer container)
    {
        return TypeAuditRecordCommand.builder()
                        .withPk(container.getPk())
                        .withType(container.getTypeCode())
                        .withTypePk(container.getTypePk())
                        .withChangingUser(getChangingUser())
                        .withTimestamp(getModifiedTime(container))
                        .withCurrentTimestamp(new Date())
                        .withPayloadBefore(container.getValuesBefore())
                        .withPayloadAfter(container.getValuesAfter())
                        .withAuditType(container.getAuditType())
                        .withContext(prepareContext(container))
                        .build();
    }


    private LinkAuditRecordCommand toLinkAuditRecordCommand(ValuesContainer container)
    {
        return LinkAuditRecordCommand.builder()
                        .withTypeAuditRecordCommand(toTypeAuditRecordCommand(container))
                        .withSourcePk(getSourcePk(container))
                        .withTargetPk(getTargetPk(container))
                        .withLanguagePk(getLanguagePk(container))
                        .build();
    }


    private static PK getSourcePk(ValuesContainer container)
    {
        ItemPropertyValue ipv = getValue(container, "source", ItemPropertyValue.class);
        return toPk(ipv);
    }


    private static PK getTargetPk(ValuesContainer container)
    {
        ItemPropertyValue ipv = getValue(container, "target", ItemPropertyValue.class);
        return toPk(ipv);
    }


    private static PK getLanguagePk(ValuesContainer container)
    {
        ItemPropertyValue ipv = getValue(container, "language", ItemPropertyValue.class);
        return toPk(ipv);
    }


    private static PK toPk(ItemPropertyValue ipv)
    {
        return (ipv == null) ? null : ipv.getPK();
    }


    private static Date getModifiedTime(ValuesContainer container)
    {
        return (container.getAuditType() == AuditType.DELETION) ? new Date() : getValue(container, "modifiedtime", Date.class);
    }


    private static <T> T getValue(ValuesContainer container, String attribute, Class<T> clazz)
    {
        Objects.requireNonNull(container, "container is required");
        Objects.requireNonNull(attribute, "attribute is required");
        Objects.requireNonNull(clazz, "clazz is required");
        Map<String, Object> values = container.getValuesAfter().isEmpty() ? container.getValuesBefore() : container.getValuesAfter();
        SLDDataContainer.AttributeValue attributeValue = (SLDDataContainer.AttributeValue)values.get(attribute);
        Objects.requireNonNull(attributeValue, "AttributeValue for attribute '" + attribute + "' not found");
        Object value = attributeValue.getValue();
        if(value == null)
        {
            return null;
        }
        Preconditions.checkState(clazz.isInstance(value), "AtributeValue is not an instance of %s", clazz);
        return (T)value;
    }


    private String getChangingUser()
    {
        UserModel currentUser = this.userService.getCurrentUser();
        return (currentUser == null) ? "<unknown>" : currentUser.getUid();
    }


    private String getActingUser()
    {
        return (String)this.sessionService.getAttribute("ACTING_USER_UID");
    }


    private Map<String, Object> prepareContext(ValuesContainer container)
    {
        Map<String, Object> context = new HashMap<>();
        context.putAll(container.getContext());
        String actingUserUid = getActingUser();
        if(actingUserUid != null)
        {
            context.put("actingUser", actingUserUid);
        }
        return context;
    }


    @Required
    public void setAuditChangeFilters(List<AuditChangeFilter> auditChangeFilters)
    {
        this.auditChangeFilters = auditChangeFilters;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setWriteAuditGateway(WriteAuditGateway writeAuditGateway)
    {
        this.writeAuditGateway = writeAuditGateway;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
