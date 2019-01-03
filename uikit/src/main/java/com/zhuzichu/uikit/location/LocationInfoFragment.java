package com.zhuzichu.uikit.location;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.common.base.Optional;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.permission.MPermission;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionDenied;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionGranted;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionNeverAskAgain;
import com.zhuzichu.library.utils.BitmapUtils;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.library.utils.DrawableUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.adapter.ImageAdapter;
import com.zhuzichu.uikit.databinding.FragmentLocationInfoBinding;
import com.zhuzichu.uikit.utils.UserInfoUtils;

public class LocationInfoFragment extends NiceFragment<FragmentLocationInfoBinding> {
    private static final String TAG = "LocationInfoFragment";

    public interface Extra {
        String EXTRA_LONGITUDE = "extra_longitude";
        String EXTRA_LATITUDE = "extra_latitude";
        String EXTRA_ADDRESS = "extra_address";
        String EXTRA_ACCOUNT = "extra_account";
    }

    private double mLongitude;
    private double mLatitude;
    private String mAccount;
    private Marker mMarker;
    private String mAddress;
    private MapView mapView;
    private AMap map;
    private float mZoomLevel = 14;
    private boolean first = true;

    public static LocationInfoFragment newInstance(double longitude, double latitude, String address, String account) {

        Bundle args = new Bundle();
        args.putString(Extra.EXTRA_ADDRESS, address);
        args.putDouble(Extra.EXTRA_LONGITUDE, longitude);
        args.putDouble(Extra.EXTRA_LATITUDE, latitude);
        args.putString(Extra.EXTRA_ACCOUNT, account);
        LocationInfoFragment fragment = new LocationInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        initStatusBar();
        initTopBar();
        initListener();
        mBind.address.setText(mAddress);
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        MPermission.with(this)
                .setRequestCode(Nice.PermissionCode.LOCATION_PERMISSION_REQUEST_CODE)
                .permissions(Nice.Permission.LOCATION_PERMISSIONS)
                .request();
    }

    @OnMPermissionGranted(Nice.PermissionCode.LOCATION_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {

    }

    @OnMPermissionDenied(Nice.PermissionCode.LOCATION_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(Nice.PermissionCode.LOCATION_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        pop();
    }

    private void initListener() {

    }

    private void parseData() {
        mAddress = getArguments().getString(Extra.EXTRA_ADDRESS);
        mLatitude = getArguments().getDouble(Extra.EXTRA_LATITUDE);
        mLongitude = getArguments().getDouble(Extra.EXTRA_LONGITUDE);
        mAccount = getArguments().getString(Extra.EXTRA_ACCOUNT);
    }

    private void initTopBar() {
        mBind.topbar.setTitle("位置信息");
        mBind.topbar.setTitleGravity(Gravity.LEFT);
        QMUIAlphaImageButton ibLeft = mBind.topbar.addLeftBackImageButton();
        ibLeft.setImageDrawable(DrawableUtils.transformColor(ibLeft.getDrawable(), R.color.color_grey_333333));
        ibLeft.setOnClickListener(view -> pop());
        mBind.topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_location_info)
                .setOnClickListener(view -> Toast.makeText(getActivity(), "更多", Toast.LENGTH_SHORT).show());
        ImageButton ibRight = mBind.getRoot().findViewById(R.id.topbar_right_location_info);
        ibRight.setImageDrawable(DrawableUtils.transformColor(ibRight.getDrawable(), R.color.color_grey_333333));
    }

    private void initStatusBar() {
        mStatus.autoColorPrimary(false);
        mStatus.setColor(R.color.color_white_f8f8f8);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_location_info;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        super.onBindView(savedInstanceState, rootView);
        parseData();
        mapView = rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initMapView();
    }

    private void initMapView() {
        if (map == null)
            map = mapView.getMap();
        //去掉缩放按钮
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        //显示指南针
        uiSettings.setCompassEnabled(true);
        //隐藏logo
        uiSettings.setLogoBottomMargin(-50);
        uiSettings.setMyLocationButtonEnabled(true);
        setupLocationStyle();
        map.setOnMyLocationChangeListener(location -> {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //排除 第一次加载无网情况下失败的情况
            if (latitude == 0.0 && longitude == 0.0) {
                return;
            }
            if (first) {
                //第一次定位调整视图
                LatLngBounds bounds = createBounds(mLatitude, mLongitude, latitude, longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50), 1000L, null);
                first = false;
            }
            Log.i(TAG, "initMapView: " + location.getLatitude() + "," + location.getLongitude());
        });
        map.setMyLocationEnabled(true);
        //移动视野
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        Log.i(TAG, "initMapView:---------------------- " + mLatitude + "," + mLongitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mZoomLevel));
        //添加 maker点
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.purple_pin));
        mMarker = map.addMarker(markerOptions);
        Optional<NimUserInfo> userInfo = UserInfoUtils.getUserInfo(mAccount);
        if (userInfo.isPresent()) {
            String url = userInfo.get().getAvatar();
            Glide.with(this)
                    .asBitmap()
                    .load(!TextUtils.isEmpty(url) ? url : R.mipmap.avatar_default)
                    .apply(ImageAdapter.mCircleOptions).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    map.clear();
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.resizeBitmap(resource, DensityUtils.dip2px(Nice.getContext(), 35), DensityUtils.dip2px(Nice.getContext(), 35))));
                    mMarker = map.addMarker(markerOptions);
                }
            });
        }
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        map.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * 根据2个坐标返回一个矩形Bounds
     * 以此来智能缩放地图显示
     */
    public static LatLngBounds createBounds(Double latA, Double lngA, Double latB, Double lngB) {
        LatLng northeastLatLng;
        LatLng southwestLatLng;

        Double topLat, topLng;
        Double bottomLat, bottomLng;
        if (latA >= latB) {
            topLat = latA;
            bottomLat = latB;
        } else {
            topLat = latB;
            bottomLat = latA;
        }
        if (lngA >= lngB) {
            topLng = lngA;
            bottomLng = lngB;
        } else {
            topLng = lngB;
            bottomLng = lngA;
        }
        northeastLatLng = new LatLng(topLat, topLng);
        southwestLatLng = new LatLng(bottomLat, bottomLng);
        return new LatLngBounds(southwestLatLng, northeastLatLng);
    }
}
