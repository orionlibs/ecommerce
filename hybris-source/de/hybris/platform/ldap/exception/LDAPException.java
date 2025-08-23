package de.hybris.platform.ldap.exception;

import de.hybris.platform.jalo.JaloBusinessException;

public class LDAPException extends JaloBusinessException
{
    public static final int SUCCESS = 0;
    public static final int OPERATIONS_ERROR = 1;
    public static final int PROTOCOL_ERROR = 2;
    public static final int TIME_LIMIT_EXCEEDED = 3;
    public static final int SIZE_LIMIT_EXCEEDED = 4;
    public static final int COMPARE_FALSE = 5;
    public static final int COMPARE_TRUE = 6;
    public static final int AUTH_METHOD_NOT_SUPPORTED = 7;
    public static final int STRONG_AUTH_REQUIRED = 8;
    public static final int LDAP_PARTIAL_RESULTS = 9;
    public static final int REFERRAL = 10;
    public static final int ADMIN_LIMIT_EXCEEDED = 11;
    public static final int UNAVAILABLE_CRITICAL_EXTENSION = 12;
    public static final int CONFIDENTIALITY_REQUIRED = 13;
    public static final int SASL_BIND_IN_PROGRESS = 14;
    public static final int NO_SUCH_ATTRIBUTE = 16;
    public static final int UNDEFINED_ATTRIBUTE_TYPE = 17;
    public static final int INAPPROPRIATE_MATCHING = 18;
    public static final int CONSTRAINT_VIOLATION = 19;
    public static final int ATTRIBUTE_OR_VALUE_EXISTS = 20;
    public static final int INVALID_ATTRIBUTE_SYNTAX = 21;
    public static final int NO_SUCH_OBJECT = 32;
    public static final int ALIAS_PROBLEM = 33;
    public static final int INVALID_DN_SYNTAX = 34;
    public static final int IS_LEAF = 35;
    public static final int ALIAS_DEREFERENCING_PROBLEM = 36;
    public static final int INAPPROPRIATE_AUTHENTICATION = 48;
    public static final int INVALID_CREDENTIALS = 49;
    public static final int INSUFFICIENT_ACCESS_RIGHTS = 50;
    public static final int BUSY = 51;
    public static final int UNAVAILABLE = 52;
    public static final int UNWILLING_TO_PERFORM = 53;
    public static final int LOOP_DETECT = 54;
    public static final int NAMING_VIOLATION = 64;
    public static final int OBJECT_CLASS_VIOLATION = 65;
    public static final int NOT_ALLOWED_ON_NONLEAF = 66;
    public static final int NOT_ALLOWED_ON_RDN = 67;
    public static final int ENTRY_ALREADY_EXISTS = 68;
    public static final int OBJECT_CLASS_MODS_PROHIBITED = 69;
    public static final int AFFECTS_MULTIPLE_DSAS = 71;
    public static final int OTHER = 80;
    public static final int SERVER_DOWN = 81;
    public static final int LOCAL_ERROR = 82;
    public static final int ENCODING_ERROR = 83;
    public static final int DECODING_ERROR = 84;
    public static final int LDAP_TIMEOUT = 85;
    public static final int AUTH_UNKNOWN = 86;
    public static final int FILTER_ERROR = 87;
    public static final int USER_CANCELLED = 88;
    public static final int NO_MEMORY = 90;
    public static final int CONNECT_ERROR = 91;
    public static final int LDAP_NOT_SUPPORTED = 92;
    public static final int CONTROL_NOT_FOUND = 93;
    public static final int NO_RESULTS_RETURNED = 94;
    public static final int MORE_RESULTS_TO_RETURN = 95;
    public static final int CLIENT_LOOP = 96;
    public static final int REFERRAL_LIMIT_EXCEEDED = 97;
    public static final int INVALID_RESPONSE = 100;
    public static final int AMBIGUOUS_RESPONSE = 101;
    public static final int TLS_NOT_SUPPORTED = 112;
    public static final String ERROR_EDIR_NO_SUCH_ENTRY = "-601";
    public static final String ERROR_EDIR_NO_SUCH_VALUE = "-602";
    public static final String ERROR_EDIR_NO_SUCH_ATTRIBUTE = "-603";
    public static final String ERROR_EDIR_ENTRY_ALREADY_EXISTS = "-606";
    public static final String ERROR_EDIR_ILLEGAL_ATTRIBUTE = "-608";
    public static final String ERROR_EDIR_DS_LOCKED = "-663";
    public static final String ERROR_EDIR_BAD_PASSWORD = "-222";
    public static final String ERROR_EDIR_FAILED_AUTHENTICATION = "-669";
    public static final String ERROR_EDIR_DUPLICATE_PASSWORD = "-215";
    public static final String ERROR_EDIR_PASSWORD_TO_SHORT = "-216";


    public LDAPException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }
}
