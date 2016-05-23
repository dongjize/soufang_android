package com.dong.soufang.http;

/**
 * Description: 路由列表
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public class HttpApi {
    public static final int TIME_OUT = 20 * 1000;
    public static final int RESULT_OK = 200;

    //    public static final String DOMAIN = "soufang.app/";
    public static final String DOMAIN = "http://10.0.3.2:80/";
    //    public static final String DOMAIN = "http://101.231.75.108/";
    public static String CITY = "021/";

    public static final String IMAGE_URL = DOMAIN + "images/";

    public static final String BaseUrl = DOMAIN + "api/";

    public static final String PostRegisterApi = BaseUrl + "user/register"; //用户注册
    public static final String PostSendVerifyCodeApi = BaseUrl + "user/verify_code"; //发送验证码
    public static final String PostLoginApi = BaseUrl + "user/login"; //用户登录
    public static final String GetPersonalInfoApi = BaseUrl + "user/profile"; //用户信息

    public static final String GetUserEstateCollectionsApi = BaseUrl + "estates/collections"; //用户收藏房产列表
    public static final String GetCollectEstateApi = BaseUrl + "estates/collect"; //收藏房产
    public static final String GetNewEstatesListApi = BaseUrl + CITY + "new_estates"; //新房列表
    public static final String GetNewEstateDetailsApi = BaseUrl + CITY + "new_estates/"; //房产详情页
    public static final String GetFilteredNewEstatesApi = BaseUrl + CITY + "new_estates/sale/"; //条件筛选后的房产列表
    public static final String GetOldHousesListApi = BaseUrl + CITY + "old_houses/"; //二手房列表
    public static final String GetOldHouseDetailsApi = BaseUrl + CITY + "old_houses/sale/"; //二手房详情
    public static final String GetFilteredOldHousesApi = BaseUrl + CITY + "old_houses/sale/"; //条件筛选后的二手房列表
    public static final String GetRentHousesListApi = BaseUrl + CITY + "rent_houses/"; //租房列表

    public static final String GetArticlesListApi = BaseUrl + "articles/"; //资讯列表
    public static final String GetCollectArticleApi = BaseUrl + "article/collect"; //收藏资讯
    public static final String GetUserArticleCollectionsApi = BaseUrl + "articles/collections"; //用户收藏资讯列表

    public static final String GetCityListApi = BaseUrl + "cities"; //获取城市列表
    public static final String GetFilterVariablesApi = BaseUrl + CITY + "filter/new_estates"; //获取新房筛选条件列表
    public static final String GetOldHouseFilterVariablesApi = BaseUrl + CITY + "filter/old_houses"; //获取二手房筛选条件列表

    public static final String GetHomePageIntroduceApi = BaseUrl + CITY + "introduce"; //获取首页推荐列表
    public static final String GetHousesOfNewEstateApi = BaseUrl + CITY + "new_estates/"; // + {id}/house 获取地产的户型列表

}
