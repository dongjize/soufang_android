package com.dong.soufang.http;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dong.soufang.GlobalData;
import com.dong.soufang.bean.Article;
import com.dong.soufang.bean.Banner;
import com.dong.soufang.bean.City;
import com.dong.soufang.bean.District;
import com.dong.soufang.bean.Estate;
import com.dong.soufang.bean.NewEstate;
import com.dong.soufang.bean.NewHouse;
import com.dong.soufang.bean.OldHouse;
import com.dong.soufang.bean.RentHouse;
import com.dong.soufang.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: http管理类
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class HttpHandler {
    protected static final String TAG = HttpHandler.class.getSimpleName();
    private Context context;
    VolleyUtils volleyUtils;

    public HttpHandler() {
        volleyUtils = new VolleyUtils();
    }

    public HttpHandler(Context context) {
        this.context = context;
        volleyUtils = new VolleyUtils();
    }

    /**
     * @param jsonObject
     * @return
     */
    private boolean preCheck(JSONObject jsonObject) {
        try {
            if (jsonObject.getInt("result_code") == 3) {
                Intent intent = new Intent();
                intent.setAction(GlobalData.RELOGIN_ACTION);
                context.sendBroadcast(intent);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    /**
     * 用户注册
     *
     * @param requestBean
     * @param callback
     */
    public void userRegister(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpPostString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        BaseResult result = new BaseResult();
                        result.setValue("");
                        result.setData(null);
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 用户登录
     *
     * @param requestBean
     * @param callback
     */
    public void userLogin(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpPostString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        BaseResult result = new BaseResult();
                        result.setValue("");
                        result.setData(null);
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 获取首页推荐列表
     *
     * @param requestBean
     * @param callback
     */
    public void getHomeIntroduceList(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        List list = new ArrayList();
                        JSONArray dataList = jsonObject.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < dataList.length(); i++) {
                            JSONObject data = dataList.getJSONObject(i);
                            String dataType = data.getString("data_type");
                            switch (dataType) {
                                case "article":
                                    Article article = gson.fromJson(data.toString(), new TypeToken<Article>() {
                                    }.getType());
                                    list.add(article);
                                    break;
                                case "new_estate":
                                    NewEstate newEstate = gson.fromJson(data.toString(), new TypeToken<NewEstate>() {
                                    }.getType());
                                    list.add(newEstate);
                                    break;
                                case "old_house":
                                    OldHouse oldHouse = gson.fromJson(data.toString(), new TypeToken<OldHouse>() {
                                    }.getType());
                                    list.add(oldHouse);
                                    break;
                                case "rent_house":
                                    RentHouse rentHouse = gson.fromJson(data.toString(), new TypeToken<RentHouse>() {
                                    }.getType());
                                    list.add(rentHouse);
                                    break;
                            }
                        }
                        BaseResult result = new BaseResult();
                        result.setData(list);
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 获取首页资讯列表
     *
     * @param requestBean
     * @param callback
     */
    public void getArticlesList(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Article>>() {
                        }.getType();
                        List<Article> articlesList = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(articlesList);
//                        result.setValue(jsonObject.getJSONObject("data").getInt("next_start") + "");
//                        result.setHasMore(jsonObject.getJSONObject("data").getBoolean("has_more"));
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 收藏文章
     *
     * @param requestBean
     * @param callback
     */
    public void collectArticle(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    /**
     * 获取首页个人信息
     *
     * @param requestBean
     * @param callback
     */
    public void getPersonalInfo(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<User>() {
                        }.getType());
                        BaseResult result = new BaseResult();
                        result.setData(user);
                        result.setValue("");
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 获取房产首页Banner图
     *
     * @param requestBean
     * @param callback
     */
    public void getEstatesBannerImages(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<Banner>() {
                        }.getType();
                        List<Banner> banners = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(banners);
                        result.setValue("");
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 新房列表
     *
     * @param requestBean
     * @param callback
     */
    public void getNewEstatesList(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewEstate>>() {
                        }.getType();
                        List<NewEstate> newEstates = gson.fromJson(jsonObject.getJSONObject("data").
                                getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(newEstates);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 获取estate详细信息
     *
     * @param requestBean
     * @param callback
     */
    public void getEstateDetails(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        NewEstate newEstate = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<NewEstate>() {
                        }.getType());
                        BaseResult result = new BaseResult();
                        result.setData(newEstate);
                        result.setValue("");
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 二手房列表
     *
     * @param requestBean
     * @param callback
     */
    public void getOldHousesList(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OldHouse>>() {
                        }.getType();
                        List<OldHouse> oldHouses = gson.fromJson(jsonObject.getJSONObject("data").
                                getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(oldHouses);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 二手房详情列表
     *
     * @param requestBean
     * @param callback
     */
    public void getOldHouseDetails(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        OldHouse oldHouse = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<OldHouse>() {
                        }.getType());
                        BaseResult result = new BaseResult();
                        result.setData(oldHouse);
                        result.setValue("");
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 获取筛选后的二手房列表
     *
     * @param requestBean
     * @param callback
     */
    public void getFilteredOldHouses(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OldHouse>>() {
                        }.getType();
                        List<OldHouse> oldHouses = gson.fromJson(jsonObject.getJSONObject("data").
                                getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(oldHouses);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 获取新房过滤参数
     *
     * @param requestBean
     * @param callback
     */
    public void getNewEstatesFilters(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        JSONObject data = jsonObject.getJSONObject("data");
                        Gson gson = new Gson();
                        List<District> districts = gson.fromJson(data.getJSONArray("districts").toString(), new TypeToken<List<District>>() {
                        }.getType());
                        List<String> types = gson.fromJson(data.getJSONArray("types").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        List<String> prices = gson.fromJson(data.getJSONArray("prices").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        params.put("districts", districts);
                        params.put("types", types);
                        params.put("prices", prices);

                        BaseResult result = new BaseResult();
                        result.setData(params);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 获取过滤后的新房列表
     *
     * @param requestBean
     * @param callback
     */
    public void getFilteredNewEstates(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewEstate>>() {
                        }.getType();
                        List<NewEstate> newEstates = gson.fromJson(jsonObject.getJSONObject("data").
                                getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(newEstates);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 获取某房产的新房列表
     *
     * @param requestBean
     * @param callback
     */
    public void getHousesOfNewEstate(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewHouse>>() {
                        }.getType();
                        List<NewHouse> newHouses = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(newHouses);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 获取新房过滤参数
     *
     * @param requestBean
     * @param callback
     */
    public void getOldHousesFilters(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        JSONObject data = jsonObject.getJSONObject("data");
                        Gson gson = new Gson();
                        List<District> districts = gson.fromJson(data.getJSONArray("districts").toString(), new TypeToken<List<District>>() {
                        }.getType());
                        List<String> areas = gson.fromJson(data.getJSONArray("areas").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        List<String> houseTypes = gson.fromJson(data.getJSONArray("house_types").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        List<String> salePrices = gson.fromJson(data.getJSONArray("sale_prices").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        params.put("districts", districts);
                        params.put("areas", areas);
                        params.put("house_types", houseTypes);
                        params.put("sale_prices", salePrices);

                        BaseResult result = new BaseResult();
                        result.setData(params);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }

    /**
     * 租房列表
     *
     * @param requestBean
     * @param callback
     */
    public void getRentHousesList(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewEstate>>() {
                        }.getType();
                        List<RentHouse> rentHouses = gson.fromJson(jsonObject.getJSONObject("data").
                                getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(rentHouses);
                        result.setValue(""); //TODO
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 租房详情
     *
     * @param requestBean
     * @param callback
     */
    public void getRentHouseDetails(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        RentHouse rentHouse = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<RentHouse>() {
                        }.getType());
                        BaseResult result = new BaseResult();
                        result.setData(rentHouse);
                        result.setValue("");
                        result.setMessage(jsonObject.getString("message"));
                        result.setResultCode(jsonObject.getInt("result_code"));
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 获取estates收藏列表
     *
     * @param requestBean
     * @param callback
     */
    public void getUserEstatesCollection(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                        Gson gson = new Gson();
                        List estateList = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = (JSONObject) jsonArray.get(i);
                            int itemType = jo.getInt("item_type");
                            if (itemType == 1) { //新房newestates
                                Estate estate = gson.fromJson(jo.toString(), new TypeToken<Estate>() {
                                }.getType());
                                estateList.add(estate);
                            } else if (itemType == 2) { //二手房 oldhouses
                                OldHouse oldHouse = gson.fromJson(jo.toString(), new TypeToken<OldHouse>() {
                                }.getType());
                                estateList.add(oldHouse);
                            } else if (itemType == 3) { //租房
                                RentHouse rentHouse = gson.fromJson(jo.toString(), new TypeToken<RentHouse>() {
                                }.getType());
                                estateList.add(rentHouse);
                            }
                        }
                        BaseResult result = new BaseResult();
                        result.setData(result);
                        result.setValue("");
                        //TODO
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


    /**
     * 获取文章收藏列表
     *
     * @param requestBean
     * @param callback
     */
    public void getUserArticlesCollection(RequestBean requestBean, final Callback callback) {
        getArticlesList(requestBean, callback);
    }

    /**
     * 获取城市列表
     *
     * @param requestBean
     * @param callback
     */
    public void getCityList(RequestBean requestBean, final Callback callback) {
        volleyUtils.httpGetString(requestBean, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("result_code") == HttpApi.RESULT_OK) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<City>>() {
                        }.getType();
                        List<City> cityList = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("list").toString(), listType);
                        BaseResult result = new BaseResult();
                        result.setData(cityList);
                        result.setValue("");
                        result.setMessage(jsonObject.getString("message").toString());
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onFailure(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
    }


}
