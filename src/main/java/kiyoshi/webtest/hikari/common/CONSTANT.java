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
        MOTIVATE(2,"MOTIVATE");


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


    public static final String ORDER_BY_CREATE_TIME = "create_time";
    public static final String ORDER_BY_UPDATE_TIME = "update_time";

    public static final String POST_JENKINS_SERVER = "http://100.64.240.27:8080/";



}
