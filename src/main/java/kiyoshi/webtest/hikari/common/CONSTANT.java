package kiyoshi.webtest.hikari.common;

import lombok.Getter;

public class CONSTANT {
    public static final String LOGIN_USER = "loginUser";

    public interface ROLE{
        int CUSTOMER = 1;
        int ADMIN = 0;
    }

    //public static final Integer CATEGORY_ROOT = 0;

    @Getter
    public enum AdministrationStatus{//调度状态

        MOTIONLESS(1,"MOTIONLESS"),
        MOTIVATE(2,"motivate");


        private final int code;
        private final String description;

        AdministrationStatus(int code, String description){
            this.code = code;
            this.description = description;
        }

    }
    public enum ReportStatus{//报告状态

        NOT_EXIST(1," not exist"),
        GENERATING(2,"generating"),
        READY_FOR_READING(3,"ready for reading");


        private final int code;
        private final String description;

        ReportStatus(int code, String description){
            this.code = code;
            this.description = description;
        }

    }


    public static final String PRODUCT_ORDER_BY_PRICE_ASC = "price_asc";
    public static final String PRODUCT_ORDER_BY_PRICE_DESC = "price_desc";

    public interface CART{
        int CHECKED = 1;
        int UNCHECKED = 2;
    }





}
