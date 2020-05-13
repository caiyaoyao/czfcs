package com.sz.kejin.czfcs.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.sz.kejin.czfcs.R;


import java.util.ArrayList;
import java.util.List;

//import com.kjce.dshbgt.Upload.UploadListActivity;

public class MapActivity extends AppCompatActivity implements OnItemClickListener, OnDismissListener {

    MapView mMapView = null;
    BaiduMap mBaiduMap;
    private Toolbar toolBar;
    Button zoomOutBtn;
    Button zoomInBtn;

    Button currentLocationBtn;
    Button clickLocationBtn;
    Boolean isClickBtnShow;

    //定位相关
    private LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private BDLocation currentLocation;

    //当前经纬度
    double jn_log;
    double jn_lat;
    double click_log;
    double click_lat;

    public final static String CURRENT_LOG = "currentLog";
    public final static String CURRENT_LAT = "currentLat";
    public final static String CLICK_LOG = "clickLog";
    public final static String CLICK_LAT = "clickLat";
    public final static String TYPE = "type";

    private long exitTime = 0;

    // 版本号
    private String currentVersion;
    private String lastVersion;

    private AlertView mAlertView;

    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    private UiSettings mUiSettings;
    private static final int paddingBottom = 60;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bd;
    private Marker mMarkerA;

    // 动画相关
    private ScaleAnimation myAnimation_Scale;
    private ScaleAnimation clickAnimation_Scale;

    // marker弹出视图
    public TextView tvView;
    private InfoWindow mInfoWindow;

    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_navigation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolBar = (Toolbar) findViewById(R.id.tb_navigation);
        toolBar.inflateMenu(R.menu.help_tool_bar);
        toolBar.setOnMenuItemClickListener(myMenuItemClickListener);

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            from = intent.getStringExtra("from");
        } else {
            from = savedInstanceState.getString("from");
        }

        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        // 设置定位模式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 设置logo位置
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mBaiduMap.setPadding(0, 0, 0, paddingBottom * 2);
            }
        });
        //设置不启用俯视手势
        mUiSettings.setOverlookingGesturesEnabled(false);
        //不显示比例尺
        mMapView.showScaleControl(false);
        //不显示缩放
        mMapView.showZoomControls(false);

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        // 1.0 初始化LocationClient类
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        // 2.0 配置定位sdk参数
        initLocation();
        // 3.0 开始定位
        if (!mLocClient.isStarted()) {
            mLocClient.start();
        }

        // 点击定位按钮
        Button locationBtn = (Button) findViewById(R.id.btn_location);
        locationBtn.setOnClickListener(getMyCurrentLocationListener);
        // 放大,缩小按钮
        zoomOutBtn = (Button) findViewById(R.id.btn_zoom_out);
        zoomOutBtn.setOnClickListener(zoomOutListener);

        zoomInBtn = (Button) findViewById(R.id.btn_zoom_in);
        zoomInBtn.setOnClickListener(zoomInListener);

        // 初始化长按监听
        initLongclickListener();

        bd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);

        // marker弹出视图
        tvView = new Button(getApplicationContext());
        tvView.setBackgroundResource(R.mipmap.dialog);

        // 点击当前位置曝光按钮
        currentLocationBtn = (Button) findViewById(R.id.btn_currentLocation);
        currentLocationBtn.setOnClickListener(clickCurrentLocationListener);
        // 点击点击位置曝光按钮
        clickLocationBtn = (Button) findViewById(R.id.btn_clickLocation);
        clickLocationBtn.setOnClickListener(clickClickLocationListener);

        isClickBtnShow = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super. onSaveInstanceState(outState);
        outState.putString("from", from);
    }

    /**
     * toolBar菜单栏监听
     */
    public Toolbar.OnMenuItemClickListener myMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int menuItemId = item.getItemId();
            if (menuItemId == R.id.item_help) {
                new AlertView("帮助","长按地图，在地图上添加标注点\n\n1.可以选择获取当前位置坐标点。\n\n2.可以选择获取点击位置坐标点。",
                        "确定",null,null,MapActivity.this, AlertView.Style.Alert,MapActivity.this).show();
            }
            return true;
        }
    };

    /**
     * 配置定位sdk参数
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocClient.setLocOption(option);
    }


    /**
     * 定位SDK监听函数
     */
    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            currentLocation = location;
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());//
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            List<Poi> list = location.getPoiList();// POI信息
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.e("定位", sb.toString());

            jn_log = location.getLongitude();
            jn_lat = location.getLatitude();
            float jn_direction = location.getDirection();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(jn_direction).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);	//设置定位数据


            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 18);	//设置地图中心点以及缩放级别
                mBaiduMap.animateMapStatus(u);
            }
        }
    };

    /**
     * 跳转到当前定位位置
     */
    private View.OnClickListener getMyCurrentLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 跳转到定位位置
            LatLng ll = new LatLng(jn_lat, jn_log);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 18);	//设置地图中心点以及缩放级别
            mBaiduMap.animateMapStatus(u);
        }
    };

    /**
     * 地图缩放
     */
    private View.OnClickListener zoomOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float zoomLevel = mBaiduMap.getMapStatus().zoom;
            if(zoomLevel<=20){
//					MapStatusUpdateFactory.zoomIn();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
            }else{
                Toast.makeText(MapActivity.this, "已经放至最大！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener zoomInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float zoomLevel = mBaiduMap.getMapStatus().zoom;
            if (zoomLevel > 4 ) {
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());

            } else {
                Toast.makeText(MapActivity.this, "已经缩至最小！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 长按监听
     */
    private void initLongclickListener() {
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                click_lat = latLng.latitude;
                click_log = latLng.longitude;
                if (mMarkerA != null) {
                    mMarkerA.remove();
                    mMarkerA = null;
                }
//                Toast.makeText(MapActivity.this, "长按点坐标" + click_lat + click_log, Toast.LENGTH_SHORT).show();
                //添加标注点
                MarkerOptions ooA = new MarkerOptions().position(latLng).icon(bd).draggable(false).title("点击位置").animateType(MarkerOptions.MarkerAnimateType.none);
                mMarkerA =(Marker)(mBaiduMap.addOverlay(ooA));
                if (isClickBtnShow == false) {
                    showClickBtn();
                    isClickBtnShow = true;
                }
            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarkerA != null) {
                    mMarkerA.remove();
                    mMarkerA = null;
                }

                if (isClickBtnShow == true) {
                    // 隐藏btn
                    hideClickBtn();
                    isClickBtnShow = false;
                }
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });



        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                InfoWindow.OnInfoWindowClickListener listener = null;
//                if (marker == mMarkerA) {
//                    tvView.setText("点击位置");
//                    listener = new InfoWindow.OnInfoWindowClickListener() {
//                        public void onInfoWindowClick() {
////                            LatLng ll = mMarkerA.getPosition();
////                            LatLng llNew = new LatLng(ll.latitude + 0.005,
////                                    ll.longitude + 0.005);
////                            mMarkerA.setPosition(llNew);
////                            mBaiduMap.hideInfoWindow();
//                        }
//                    };
//                    LatLng ll = marker.getPosition();
//                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(tvView), ll, -90, listener);
//                    mBaiduMap.showInfoWindow(mInfoWindow);
//                } else {
//                    tvView.setText("我的位置");
//                    LatLng ll = marker.getPosition();
//                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(tvView), ll, -90, listener);
//                    mBaiduMap.showInfoWindow(mInfoWindow);
//                }
                return false;
            }
        });
    }

    /**
     * 按钮的动画效果
     */
    public void  showClickBtn() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float scaleFloat = (currentLocationBtn.getWidth() - ((dm.widthPixels - currentLocationBtn.getWidth())/2.0f)) * 0.5f / currentLocationBtn.getWidth();
        float expandSize = 1.0f/scaleFloat;


        AnimatorSet currentSet = new AnimatorSet();
        currentLocationBtn.setPivotX(0.0f);
//        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(currentLocationBtn,"translationX", 0, -200.0f);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(currentLocationBtn,"scaleX", 1.0f, scaleFloat);
        ObjectAnimator textScaleAnimator = ObjectAnimator.ofFloat(currentLocationBtn,"textScaleX", 1.0f, expandSize);
        final List<Animator> currentList = new ArrayList<Animator>();
//        currentList.add(translationAnimator);
        currentList.add(scaleAnimator);
        currentList.add(textScaleAnimator);
        currentSet.playTogether(currentList);
        currentSet.setDuration(500);

        AnimatorSet clickSet = new AnimatorSet();
        int width = clickLocationBtn.getWidth();
        clickLocationBtn.setPivotX(width);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(clickLocationBtn,"alpha", 0.0f, 1.0f);
        ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(clickLocationBtn,"scaleX", 1.0f, scaleFloat);
        ObjectAnimator textScaleAnimator2 = ObjectAnimator.ofFloat(clickLocationBtn,"textScaleX", 1.0f, expandSize);
        List<Animator> clickList = new ArrayList<Animator>();
        clickList.add(textScaleAnimator2);
        clickList.add(alphaAnimator);
        clickList.add(scaleAnimator2);
        clickSet.playTogether(clickList);
        clickSet.setDuration(500);

        currentSet.start();
        clickSet.start();

        scaleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentLocationBtn.setBackgroundResource(R.drawable.button_drak_blue);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        scaleAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                clickLocationBtn.setBackgroundResource(R.drawable.button_red);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    public void hideClickBtn() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float scaleFloat = (currentLocationBtn.getWidth() - ((dm.widthPixels - currentLocationBtn.getWidth())/2.0f)) * 0.5f / currentLocationBtn.getWidth();
        float expandSize = 1.0f/scaleFloat;


        AnimatorSet currentSet = new AnimatorSet();
        currentLocationBtn.setPivotX(0.0f);
//        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(currentLocationBtn,"translationX", 0, -200.0f);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(currentLocationBtn,"scaleX", scaleFloat, 1.0f);
        ObjectAnimator textScaleAnimator = ObjectAnimator.ofFloat(currentLocationBtn,"textScaleX", expandSize, 1.0f);
        final List<Animator> currentList = new ArrayList<Animator>();
//        currentList.add(translationAnimator);
        currentList.add(scaleAnimator);
        currentList.add(textScaleAnimator);
        currentSet.playTogether(currentList);
        currentSet.setDuration(500);

        AnimatorSet clickSet = new AnimatorSet();
        int width = clickLocationBtn.getWidth();
        clickLocationBtn.setPivotX(width);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(clickLocationBtn,"alpha", 1.0f, 0.0f);
        ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(clickLocationBtn,"scaleX", scaleFloat, 1.0f);
        ObjectAnimator textScaleAnimator2 = ObjectAnimator.ofFloat(clickLocationBtn,"textScaleX", expandSize, 1.0f);
        List<Animator> clickList = new ArrayList<Animator>();
        clickList.add(textScaleAnimator2);
        clickList.add(alphaAnimator);
        clickList.add(scaleAnimator2);
        clickSet.playTogether(clickList);
        clickSet.setDuration(500);

        currentSet.start();
        clickSet.start();
    }


    /**
     * 点击当前位置曝光
     */
    private View.OnClickListener clickCurrentLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentLocation == null) {
                Toast.makeText(MapActivity.this, "定位失败,请开启定位权限!", Toast.LENGTH_SHORT).show();
//                if (from.equals("1")) {
//                    Intent intent = new Intent(MapActivity.this, EmergencyResponseUploadActivity.class);
//                    startActivity(intent);
//                } else if (from.equals("2")) {
//                    Intent intent = new Intent(MapActivity.this, TzzlbInputActivity.class);
//                    startActivity(intent);
//                } else if (from.equals("3")) {
//                    Intent intent = new Intent(MapActivity.this, AddXhRecordActivity.class);
//                    startActivity(intent);
//                }
//                else if (from.equals("0")) {
//                    Intent intent = new Intent(MapActivity.this, EventUploadActivity.class);
//                    startActivity(intent);
//                } else if (from.equals("2")) {
//                    Intent intent = new Intent(MapActivity.this, WgyFinishEventUploadActivity.class);
//                    startActivity(intent);
//                }
                return;
            }
            // 跳转到其他Activity
//            if (from.equals("1")) {
//                Intent intent = new Intent(MapActivity.this, EmergencyResponseUploadActivity.class);
//                Log.i("当前坐标点", jn_log + "-----" + jn_lat);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            } else if (from.equals("2")) {
//                Intent intent = new Intent(MapActivity.this, TzzlbInputActivity.class);
//                Log.i("当前坐标点", jn_log + "-----" + jn_lat);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            }  else if (from.equals("3")) {
//                Intent intent = new Intent(MapActivity.this, AddXhRecordActivity.class);
//                Log.i("当前坐标点", jn_log + "-----" + jn_lat);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            }
//            else if (from.equals("0")) {
//                Intent intent = new Intent(MapActivity.this, EventUploadActivity.class);
//                Log.i("当前坐标点", jn_log + "-----" + jn_lat);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            } else if (from.equals("2")) {
//                Intent intent = new Intent(MapActivity.this, WgyFinishEventUploadActivity.class);
//                Log.i("当前坐标点", jn_log + "-----" + jn_lat);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            }

        }
    };

    /**
     * 点击点击位置曝光
     */
    private View.OnClickListener clickClickLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentLocation == null) {
                Toast.makeText(MapActivity.this, "定位失败,请开启定位权限!", Toast.LENGTH_SHORT).show();
//                if (from.equals("1")) {
//                    Intent intent = new Intent(MapActivity.this, EmergencyResponseUploadActivity.class);
//                    startActivity(intent);
//                } else if (from.equals("2")) {
//                    Intent intent = new Intent(MapActivity.this, TzzlbInputActivity.class);
//                    startActivity(intent);
//                } else if (from.equals("3")) {
//                    Intent intent = new Intent(MapActivity.this, AddXhRecordActivity.class);
//                    startActivity(intent);
//                }
//                else if (from.equals("0")) {
//                    Intent intent = new Intent(MapActivity.this, EventUploadActivity.class);
//                    startActivity(intent);
//                } else if (from.equals("2")) {
//                    Intent intent = new Intent(MapActivity.this, WgyFinishEventUploadActivity.class);
//                    startActivity(intent);
//                }
                return;
            }
            // 跳转到其他Activity
//            if (from.equals("1")) {
//                // 跳转到其他Activity
//                Intent intent = new Intent(MapActivity.this, EmergencyResponseUploadActivity.class);
//                intent.putExtra(CURRENT_LOG, click_log);
//                intent.putExtra(CURRENT_LAT, click_lat);
//                intent.putExtra(TYPE, "clickLocation");
//                startActivity(intent);
//            }  else if (from.equals("2")) {
//                // 跳转到其他Activity
//                Intent intent = new Intent(MapActivity.this, TzzlbInputActivity.class);
//                intent.putExtra(CURRENT_LOG, click_log);
//                intent.putExtra(CURRENT_LAT, click_lat);
//                intent.putExtra(TYPE, "clickLocation");
//                startActivity(intent);
//            } else if (from.equals("3")) {
//                Intent intent = new Intent(MapActivity.this, AddXhRecordActivity.class);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            }
//            else if (from.equals("0")) {
//                // 跳转到其他Activity
//                Intent intent = new Intent(MapActivity.this, EventUploadActivity.class);
//                intent.putExtra(CURRENT_LOG, click_log);
//                intent.putExtra(CURRENT_LAT, click_lat);
//                intent.putExtra(TYPE, "clickLocation");
//                startActivity(intent);
//            } else if (from.equals("2")) {
//                Intent intent = new Intent(MapActivity.this, WgyFinishEventUploadActivity.class);
//                Log.i("当前坐标点", jn_log + "-----" + jn_lat);
//                intent.putExtra(CURRENT_LOG, jn_log);
//                intent.putExtra(CURRENT_LAT, jn_lat);
//                intent.putExtra(TYPE, "crrentLocation");
//                startActivity(intent);
//            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        mLocClient.unRegisterLocationListener(myListener);
        mLocClient.stop();
//        OkHttpUtils.getInstance().cancelTag(MapActivity.this);//取消以Activity.this作为tag的请求
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * AlertView 的协议
     * @param o
     */
    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        //判断是否是拓展窗口View，而且点击的是非取消按钮
        if (o == mAlertView && position != AlertView.CANCELPOSITION) {
            // 跳转到网页上去下载
//            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://222.92.10.42:8012/flfg.apk"));
//            startActivity(viewIntent);
        }
    }
}

