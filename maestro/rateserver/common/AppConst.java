/*     */ package com.maestro.rateserver.common;
/*     */
/*     */ import java.io.File;
/*     */
/*     */ public class AppConst
/*     */ {
/*     */   public static final String VOUCHER_TYPE_JOURNAL = "JOURNAL";
/*  12 */   public static String YES = "Y";
/*  13 */   public static String NO = "N";
/*  14 */   public static String ACTIVE = "Active";
/*  15 */   public static String INACTIVE = "Inactive";
/*  16 */   public static String OLD = "OLD";
/*  17 */   public static String NEW = "NEW";
/*  18 */   public static String CARD_TYPE_MC = "163279";
/*  19 */   public static String CARD_TYPE_TC = "163278";
/*  20 */   public static String CARD_TYPE_FC = "163280";
/*     */
/*  25 */   public static String UserType = "4";
/*  26 */   public static String FixedAccForEmp = "122";
/*     */
/*  28 */   public static String GET_SMTP_HOST = "smtp.bizmail.yahoo.com";
/*  29 */   public static String GET_SMTP_PASS = "CtrlAltDel";
/*  30 */   public static String GET_SMTP_USER = "info@1pinless.com";
/*  31 */   public static String GET_SMTP_PORT = "587";
/*     */   public static final String ERROR_COLOR = "#AA0000";
/*     */   public static final String SUCCESS_COLOR = "#00AA00";
/*     */   public static final String TABLE_ROW_BACKGROUND_COLOR = "#eeffee";
/*  37 */   public static String COLOR_VENDOR = "#003366";
/*  38 */   public static String COLOR_WEB_VENDOR = "#77A6AB";
/*  39 */   public static String COLOR_MASTER_ = "#006666";
/*  40 */   public static String COLOR_REPORT = "#FAF9F1";
/*  41 */   public static String COLOR_REPORT_ALTERNATE = "#FFFFFF";
/*  42 */   public static String COLOR_REPORT_MOUSEOVER = "#FFF8DC";
/*  43 */   public static String COLOR_REPORT_HEADER = "#EEEEEE";
/*     */   public static final String DATE_FORMAT = "dd-mm-rrrr";
/*     */   public static final String DATETIME_FORMAT = "dd-mm-rrrr hh24:mi:ss";
/*     */   public static final String DATE_FORMAT_JAVA = "dd-MM-yyyy";
/*     */   public static final String DATETIME_FORMAT_JAVA = "dd-MM-yyyy HH:mm:ss";
/*     */   public static final String DEFAULT_END_TIME = "17:00:00";
/*     */   public static final int AUDIT_PACKAGE = 1;
/*     */   public static final int AUDIT_PAYMENT_DEPOSIT = 2;
/*     */   public static final int AUDIT_ADJUST = 3;
/*     */   public static final String DB_LINK_ACC = "acc_link";
/*     */   public static final String DB_LINK_CRBSG = "crbsg_lnk";
/*     */   public static final int HW_COMPANY_ID = 1;
/*     */   public static final String DB_LINK_OMS = "OMS_LINK";
/*     */   public static final String ROLE_VIEW = "view";
/*     */   public static final String ROLE_ADMIN = "admin";
/*     */   public static final String ROLE_ACCOUNTS = "accounts";
/*     */   public static final String ROLE_FINANCE = "finance";
/*     */   public static final String ROLE_NOC = "noc";
/*     */   public static final String ROLE_GUEST = "guest";
/*     */   public static final String ROLE_OSS = "oss";
/*     */   public static final int ACC_AR = 181;
/*     */   public static final int ACC_AP = 190;
/*     */   public static final int OMS_ITEM_HEAD_CURRENCY = 3;
/*     */   public static final int OMS_ACCOUNT_HEAD_ASSET = 1;
/*     */   public static final int OMS_ACCOUNT_CURRENT_ASSET = 11;
/*     */   public static final int OMS_ACCOUNT_FIXED_ASSET = 119;
/*     */   public static final int OMS_ACCOUNT_HEAD_BANK = 16;
/*     */   public static final int OMS_ACCOUNT_HEAD_CASH = 13;
/*     */   public static final int OMS_ACCOUNT_RECEIVABLE = 15;
/*     */   public static final int OMS_ACCOUNT_RECEIVABLE_MIDDLE_PRIRITY = 128;
/*     */   public static final int OMS_ACCOUNT_RECEIVABLE_HIGH_PRIRITY = 127;
/*     */   public static final int OMS_ACCOUNT_PAYABLE_MIDDLE_PRIRITY = 124;
/*     */   public static final int OMS_ACCOUNT_COGS_MIDDLE_PRIRITY = 126;
/*     */   public static final int OMS_ACCOUNT_COGS_HIGH_PRIRITY = 125;
/*     */   public static final int OMS_ACCOUNT_REVENUE_FROM_SALES = 31;
/*     */   public static final int OMS_ACCOUNT_HEAD_LIABLITY = 2;
/*     */   public static final int OMS_ACCOUNT_HEAD_REVENUE = 3;
/*     */   public static final int OMS_ACCOUNT_CURRENT_LIABLITY = 21;
/*     */   public static final int OMS_ACCOUNT_HEAD_EXPENSE = 4;
/*     */   public static final int OMS_ACCOUNT_GOGS = 42;
/*     */   public static final int OMS_ACCOUNT_COMMISSION = 108;
/*     */   public static final int OMS_ACCOUNT_ACTUAL_COMMISSION = 221;
/*     */   public static final int OMS_ACCOUNT_PURCHASE_DISCOUNT = 143;
/*     */   public static final int OMS_ACCOUNT_DISCOUNT = 241;
/*     */   public static final int OMS_ACCOUNT_COGS = 42;
/*     */   public static final int OMS_ACCOUNT_CDAS = 141;
/*     */   public static final int OMS_ACCOUNT_COGS_GROUP = 7;
/*     */   public static final int OMS_ACCOUNT_SALARY = 45;
/*     */   public static final int OMS_AREA_CENTRAL = 1;
/*     */   public static final int OMS_ACCOUNT_PAYABLE = 22;
/*     */   public static final int OMS_ACCOUNT_OWNER_EQUITY = 23;
/*     */   public static final int OMS_ACCOUNT_LOAN = 24;
/*     */   public static final int OMS_ACCOUNT_INVENTORY = 142;
/*     */   public static final String OMS_BASE_CURRENCY_ID = "31";
/*     */   public static final String OMS_SCHEDULE_VOUCHER_TYPE = "INV_SCHEDULE";
/*     */   public static final String OMS_JOURNAL_VOUCHER_TYPE = "JOURNAL";
/*     */   public static final String OMS_TRANSACTION_PAYABLE_VOUCHER_TYPE = "TRANSACTION_PAY";
/*     */   public static final String OMS_TRANSACTION_PAYABLE_SCHEDULE_VOUCHER_TYPE = "TRANSACTION_PAY_SCHEDULE";
/*     */   public static final String OMS_TRANSACTION_RECEIVABLE_SCHEDULE_VOUCHER_TYPE = "TRANSACTION_RCV_SCHEDULE";
/*     */   public static final String OMS_TRANSACTION_RECEIVABLE_VOUCHER_TYPE = "TRANSACTION_RCV";
/*     */   public static final String OMS_VOUCHER_TYPE_BANK_CASH_RCV_ACT = "RECEIVE_ACT";
/*     */   public static final String OMS_VOUCHER_TYPE_BANK_CASH_RCV_FLX = "RECEIVE_FLX";
/*     */   public static final String OMS_PROPOSE_VOUCHER_TYPE = "INV_PROPOSE";
/*     */   public static final String OMS_PAYMENT_SCHEDULE_SUFFIX = "PAY";
/*     */   public static final String OMS_RECEIVE_SCHEDULE_SUFFIX = "RCV";
/*     */   public static final int OMS_SCHEDULE_AREA_ID = 1;
/*     */   public static final int OMS_COMPANY_DEFAULT = 3;
/*     */   public static final int OMS_TRANSACTION_TYPE_CREDIT = -1;
/*     */   public static final int OMS_TRANSACTION_TYPE_DEBIT = 1;
/*     */   public static final String OMS_TRANSN_PROPOSE = "trn_propose";
/*     */   public static final String OMS_TRANSN_SCHEDULE = "trn_schedule";
/*     */   public static final String OMS_TRANSN_PERFORM = "trn_perform";
/*     */   public static final int OMS_TRANSN_STATUS_RAW = 1;
/*     */   public static final int OMS_TRANSN_STATUS_APPROVED = 5;
/*     */   public static final int OMS_TRANSN_STATUS_CHECKED = 3;
/* 136 */   public static final String FILE_PATH_NEXTONE = File.separator + "tmp" + File.separator + "nextone";
/*     */   public static final String NOTIFICATION_TYPE_RATECHART = "RATECHART";
/*     */   public static final String NOTIFICATION_TYPE_OTHERS = "OTHERS";
/*     */   public static final String NOTIFICATION_TYPE_USER_IP = "USER_IP";
/*     */   public static final String NOTIFICATION_TYPE_EMAIL = "email";
/*     */   public static final String NOTIFICATION_TYPE_SMS = "sms";
/*     */   public static final String NOTIFICATION_TYPE_PHONE = "phone";
/*     */   public static final String NOTIFICATION_TYPE_WEB = "WEB_LOGIN";
/*     */ }

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.common.AppConst
 * JD-Core Version:    0.6.0
 */