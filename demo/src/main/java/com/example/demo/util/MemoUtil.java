package com.example.demo.util;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class MemoUtil {
    private MemoUtil() {
    };

    // TimeStampをStringに変換
    public static String timestampToString(Timestamp ts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(ts);
    }
}
