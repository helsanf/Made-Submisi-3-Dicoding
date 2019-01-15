package com.example.helsanf.submissionkamus.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_ENG_IND = "tbl_eng_ind";
    static String TABLE_IND_ENG = "tbl_ind_eng";

    static final class KamusColumns implements BaseColumns{
        static String KATA = "kata";
        static String ARTI = "arti";
    }
}
