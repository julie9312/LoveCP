package com.julie.lovecp.utils;

public class Utils {

    public static final String BASE_URL = "http://injiserver-env.eba-e7mykvsr.ap-northeast-2.elasticbeanstalk.com";

    public static final String PREFERENCES_NAME = "lcpapp";

    public static final String NEWS_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=fff16952121844b19511c54cee08ca62";

    public static final String DATABASE_NAME = "trans_db";
    public static final int DATABASE_VERSION = 1;

    // 테이블 이름 지정
    public static final String TABLE_NAME = "trans";

    // 컬럼 이름 지정 (당근 무조건 String) 컬럼 안에 데이터 저장하는 거 아님 주의
    public static final String KEY_ID = "id";
    public static final String KEY_TRANS = "trans";
    public static final String KEY_BEFORE = "before";
    public static final String KEY_AFTER = "after";
}