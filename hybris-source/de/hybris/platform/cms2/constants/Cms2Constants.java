package de.hybris.platform.cms2.constants;

import com.google.common.collect.Sets;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import java.util.Set;

public final class Cms2Constants extends GeneratedCms2Constants
{
    public static final String EXTENSIONNAME = "cms2";
    public static final String PREVIEW_TIME = "previewTime";
    public static final String RESOLVE_PAGE_URL_TOKEN = "resolvePageUrl";
    public static final String RESOLVE_PAGE_URL_TICKET_ID = "cmsPageResolveTicketId";
    public static final String NAVIGATION_NODE_ENTRY_GENERATOR_UID_PREFIX = "CMSNavigationEntry";
    public static final String ROOT = "root";
    public static final String PAGE_CONTEXT_KEY = "page";
    @Deprecated(since = "2105", forRemoval = true)
    public static final String SHOULD_CLONE_COMPONENTS_CONTEXT_KEY = "shouldCloneComponents";
    public static final String CLONE_ACTION = "cloneAction";
    public static final String IS_SLOT_CUSTOM = "isSlotCustom";
    public static final String CATALOG_VERSION = "catalogVersion";
    public static final String SESSION_VERSION_TRANSACTION_ID = "sessionVersionTransactionId";
    public static final String COLLECTION_TYPE_LIST = "1";
    public static final String COLLECTION_TYPE_SET = "2";
    public static final String COLLECTION_TYPE_SORTED_SET = "3";
    public static final String COLLECTION_TYPE_OTHER = "0";
    public static final String GENERATED_CACHED_ITEMS = "cachedItems";
    public static final String GENERATED_ATTRIBUTE_PERMISSION_CACHED_RESULTS = "attributePermissionCachedResults";
    public static final String GENERATED_TYPE_PERMISSION_CACHED_RESULTS = "typePermissionCachedResults";
    public static final String CACHED_VERSIONABLE_ITEMS = "cachedVersionableItems";
    public static final String CACHED_CONTENT_SLOTS_FOR_PAGE = "cachedContentSlotsForPage";
    public static final String CACHED_UNSAVED_VERSIONED_ITEMS = "cachedUnsavedVersionedItems";
    public static final String CACHED_PAGE_VERSIONED_IN_TRANSACTION = "cachedPageVersionedInTransaction";
    public static final String CMS_WORKFLOW_COMMENT_DOMAIN = "cmsWorkflowDomain";
    public static final String CMS_WORKFLOW_COMMENT_COMPONENT = "cmsWorkflowActionComponent";
    public static final String CMS_WORKFLOW_DECISION_COMMENT_TYPE = "cmsWorkflowDecisionComment";
    public static final Set<CronJobStatus> CMS_WORKFLOW_ACTIVE_STATUSES = (Set<CronJobStatus>)Sets.immutableEnumSet((Enum)CronJobStatus.PAUSED, (Enum[])new CronJobStatus[] {CronJobStatus.RUNNING});
    public static final int DEFAULT_VERSION_GC_PAGE_SIZE = 1000;
    public static final int DEFAULT_VERSION_GC_MAX_AGE_DAYS = 0;
    public static final int DEFAULT_VERSION_GC_MAX_NUMBER_VERSIONS = 20;
    public static final String VERSION_GC_MAX_AGE_DAYS_PROPERTY = "version.gc.maxAgeDays";
    public static final String VERSION_GC_MAX_NUMBER_VERSIONS_PROPERTY = "version.gc.maxNumberVersions";
    public static final String UNAUTHORIZED_TYPE_INSUFFICIENT_ACCESS = "unauthorized.type.insufficient.permission";
    public static final String UNAUTHORIZED_ATTRIBUTE_INSUFFICIENT_ACCESS = "unauthorized.attribute.insufficient.permission";
    public static final String CMS_SYNC_USER_ID = "cmssyncuser";
    public static final String CMS_SYNC_USER_NAME = "Cms Sync User";
}
