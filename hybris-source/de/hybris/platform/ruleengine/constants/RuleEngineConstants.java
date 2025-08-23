package de.hybris.platform.ruleengine.constants;

import java.io.File;

public final class RuleEngineConstants extends GeneratedRuleEngineConstants
{
    public static final String RULE_ENGINE_ACTIVE = "ruleengine.engine.active";
    @Deprecated(since = "2105", forRemoval = true)
    public static final String RULE_ENGINE_INIT_MODE = "ruleengine.engine.init.mode";
    public static final String MEDIA_CODE_POSTFIX = "RuleMedia";
    public static final String MEDIA_DRL_FILE_EXTENSION = ".drl";
    public static final String DROOLS_DATE_FORMAT_KEY = "drools.dateformat";
    public static final String VALIDATE_DROOLSRULE_RULECODE = "droolsruleengineservices.validate.droolsrule.rulecode";
    public static final String VALIDATE_DROOLSRULE_MODULENAME = "droolsruleengineservices.validate.droolsrule.modulename";
    public static final String VALIDATE_DROOLSRULE_RULENAME = "droolsruleengineservices.validate.droolsrule.rulename";
    public static final String VALIDATE_DROOLSRULE_RULEPACKAGE = "droolsruleengineservices.validate.droolsrule.rulepackage";
    public static final boolean VALIDATE_DROOLSRULE_DEFAULT_FLAG = true;
    public static final String DEFAULT_DROOLS_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String DROOLS_BASE_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar;
    public static final String RULEMETADATA_RULECODE = "ruleCode";
    public static final String RULEMETADATA_RULEGROUP_CODE = "ruleGroupCode";
    public static final String RULEMETADATA_RULEGROUP_EXCLUSIVE = "ruleGroupExclusive";
    public static final String RULEMETADATA_MODULENAME = "moduleName";
    public static final String RULEMETADATA_MAXIMUM_RULE_EXECUTIONS = "maxRuleExecutions";
    public static final String KIE_MODULE_MEDIA_FOLDER_QUALIFIER = "ruleengine.kie.module.media.folder.qualifier";
    public static final String KIE_MODULE_MEDIA_FOLDER_QUALIFIER_DEFAULT_VALUE = "kie-modules";
}
