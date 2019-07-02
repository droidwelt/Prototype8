package ru.droidwelt.prototype8.utils.ebus;

public class Events {

    public static final String EB_MAIN_RECREATE_PREFERENCE = "EB_MAIN_RECREATE_PREFERENCE";
    public static final String EB_MAIN_REFRESH_RECORDS = "EB_MAIN_REFRESH_RECORDS";
    public static final String EB_MAIN_REFRESH_MAINMENU = "EB_MAIN_REFRESH_MAINMENU";
    public static final String EB_MAIN_SET_MASTRESTARTONRESUME = "EB_MAIN_SET_MASTRESTARTONRESUME";

    public static final String EB_CLIENTINTO_UPDATE = "EB_CLIENTINTO_UPDATE";

    public static final String EB_CONTACTINFO_INDICATOR_ON = "EB_CONTACTINFO_INDICATOR_ON";
    public static final String EB_CONTACTINFO_INDICATOR_OFF = "EB_CONTACTINFO_INDICATOR_OFF";


    public static final String EB_MSAMAIN_REFRESH_RECORDS = "EB_MSAMAIN_REFRESH_RECORDS";
    public static final String EB_MSAMAIN_SET_FILTER_LBL = "EB_MSAMAIN_SET_FILTER_LBL";
    public static final String EB_MSAMAIN_DATASET_CHANGED = "EB_MSAMAIN_DATASET_CHANGED";
    public static final String EB_MSAMAIN_ITEM_CHANGED = "EB_MSAMAIN_ITEM_CHANGED";
    public static final String EB_MSAMAIN_SET_IMAGESIZE = "EB_MSAMAIN_SET_IMAGESIZE";

    public static final String EB_INSPECT_REFRESH_RECORDS = "EB_INSPECT_REFRESH_RECORDS";

    public static final String EB_MSA_COPY_ONE_RECORD_TO_DRAFT = "EB_MSA_COPY_ONE_RECORD_TO_DRAFT";
    public static final String EB_MSA_DELETE_ONE_RECORD = "EB_MSA_DELETE_ONE_RECORD";

    public static final String EB_MSAEDIT_SET_BACKGROUND_CLR = "EB_MSAEDIT_SET_BACKGROUND_CLR";
    public static final String EB_MSAEDIT_SET_LBL = "EB_MSAEDIT_SET_LBL";
    public static final String EB_MSAEDIT_SET_SEND_LIST = "EB_MSAEDIT_SET_SEND_LIST";
    public static final String EB_MSAEDIT_REDUCE_IMAGE = "EB_MSAEDIT_REDUCE_IMAGE";
    public static final String EB_MSAEDIT_INPORT_FILE = "EB_MSAEDIT_INPORT_FILE";



    public static class EventsMessage {

        private String mes_code;

        private String mes_str;

        private String mes_s1;

        public String getMes_s1() {
            return mes_s1;
        }

        public void setMes_s1(String mes_s1) {
            this.mes_s1 = mes_s1;
        }

        public String getMes_s2() {
            return mes_s2;
        }

        public void setMes_s2(String mes_s2) {
            this.mes_s2 = mes_s2;
        }

        private String mes_s2;

        private Integer mes_int;

        private Integer mes_x;

        public Boolean getMes_bool() {
            return mes_bool;
        }

        public void setMes_bool(Boolean mes_bool) {
            this.mes_bool = mes_bool;
        }

        private Boolean mes_bool;

        public Integer getMes_y() {
            return mes_y;
        }

        public void setMes_y(Integer mes_y) {
            this.mes_y = mes_y;
        }

        private Integer mes_y;

        public Integer getMes_x() {
            return mes_x;
        }

        public void setMes_x(Integer mes_x) {
            this.mes_x = mes_x;
        }

        public String getMes_code() {
            return mes_code;
        }

        public void setMes_code(String mes_code) {
            this.mes_code = mes_code;
        }

        public String getMes_str() {
            return mes_str;
        }

        public void setMes_str(String mes_str) {
            this.mes_str = mes_str;
        }

        public Integer getMes_int() {
            return mes_int;
        }

        public void setMes_int(Integer mes_int) {
            this.mes_int = mes_int;
        }
    }

}

