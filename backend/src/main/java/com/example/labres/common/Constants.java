package com.example.labres.common;

public final class Constants {
    private Constants() {}

    public static final String ROLE_USER = "USER";
    public static final String ROLE_LAB_ADMIN = "LAB_ADMIN";
    public static final String ROLE_TECHNICIAN = "TECHNICIAN";
    public static final String ROLE_SYS_ADMIN = "SYS_ADMIN";

    public static final String USER_ACTIVE = "ACTIVE";
    public static final String USER_DISABLED = "DISABLED";

    public static final String EQUIPMENT_AVAILABLE = "AVAILABLE";
    public static final String EQUIPMENT_REPAIRING = "REPAIRING";
    public static final String EQUIPMENT_RETIRED = "RETIRED";

    public static final String RESERVATION_PENDING = "PENDING";
    public static final String RESERVATION_APPROVED = "APPROVED";
    public static final String RESERVATION_REJECTED = "REJECTED";
    public static final String RESERVATION_CANCELLED = "CANCELLED";
    public static final String RESERVATION_COMPLETED = "COMPLETED";

    public static final String REPAIR_PENDING = "PENDING";
    public static final String REPAIR_ASSIGNED = "ASSIGNED";
    public static final String REPAIR_REPAIRING = "REPAIRING";
    public static final String REPAIR_COMPLETED = "COMPLETED";
    public static final String REPAIR_CONFIRMED = "CONFIRMED";
}
