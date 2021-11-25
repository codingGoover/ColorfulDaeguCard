package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    Intent intent;
    ArrayList<MemberStore> mealMemberStore;
    ArrayList<MemberStore> sideMealMemberStore;
    ArrayList<MemberStore> eduMemberStore;
    ArrayList<MemberStore> favorMemberStore;

    private GoogleMap googleMap;
    View card_view;
    Button mealBtn, sideBtn, eduBtn, favorBtn;
    ImageButton call;
    ImageView searchimage;
    ArrayList<Marker> mealMarker = new ArrayList<Marker>();
    ArrayList<Marker> sideMealMarker = new ArrayList<Marker>();
    ArrayList<Marker> eduMarker = new ArrayList<Marker>();
    ArrayList<Marker> favorMarker = new ArrayList<Marker>();
    Boolean mealBtnFlag =false;
    Boolean sideBtnFlag =false;
    Boolean eduBtnFlag =false;
    Boolean favorBtnFlag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        mealMemberStore=intent.getParcelableArrayListExtra("mealMemberStores"); //인텐트로 arraylist를 받아올 때 사용함
        sideMealMemberStore=intent.getParcelableArrayListExtra("sideMealMemberStores");
        eduMemberStore=intent.getParcelableArrayListExtra("eduMemberStores");
        favorMemberStore=intent.getParcelableArrayListExtra("favorMemberStores");


        for(MemberStore store : sideMealMemberStore)  //확인용
        {
            System.out.println("부식: " + store.getStype());
            System.out.println("부식: " + store.getSaddress());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        card_view=findViewById(R.id.card_view);
        mealBtn = (Button) findViewById(R.id.btn1);  //급식버튼
        sideBtn = (Button) findViewById(R.id.btn2);  //부식버튼
        eduBtn = (Button) findViewById(R.id.btn3);  //급식버튼
        favorBtn = (Button) findViewById(R.id.btn4); //즐찾버튼

        searchimage = (ImageView) findViewById(R.id.sv_location);
        call=(ImageButton)findViewById(R.id.call);

        searchimage.getBackground().setAlpha(140);

        searchimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchStoreActivity.class);
                startActivity(i);
            }


        });

        //즐겨찾기 마커는 항상 보여준다!
        //마커 보여주는 규칙. 급식 버튼 클릭시 모든 급식 가맹점만 보여주는데 특별히 즐겨찾기인 경우 즐겨찾기 마커로 보여준다.
        //급식 버튼 클릭 해제시 모든 즐겨찾기를 보여준다. 그리고 부식,교육 버튼도 해제된 상태라면 모든 가맹점을 보여준다.

        mealBtn.setOnClickListener(new View.OnClickListener() {  //급식클릭시
            @Override
            public void onClick(View v) {

                mealBtnFlag =!mealBtnFlag;
                if(mealBtnFlag) //true면 급식만 보여줌
                {
                    for (Marker marker : mealMarker) {
                        marker.setVisible(true);
                    }
                    for (Marker marker : sideMealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : eduMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : favorMarker) {
                        //급식 유형인 즐겨찾기 마커를 보여준다.
                        MemberStore store= (MemberStore) marker.getTag();
                        if(store.getStype().equals("급식")){
                            marker.setVisible(true);
                        }
                    }
                    mealBtn.setBackgroundColor(Color.parseColor("#133A55"));   //눌렀을 떄 색깔
                }
                else //false면 전체 다 보여줌
                {
                    if(sideBtnFlag ==false && eduBtnFlag ==false) {

                        for (Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : eduMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : favorMarker) {
                            marker.setVisible(true);
                        }
                    }
                    mealBtn.setBackgroundColor(Color.parseColor("#2980B9"));  //풀었을 때 버튼색깔
                }
            }
        });


        sideBtn.setOnClickListener(new View.OnClickListener() { //부식클릭시
            @Override
            public void onClick(View v) {
                sideBtnFlag =!sideBtnFlag;
                if(sideBtnFlag)
                {
                    for(Marker marker : sideMealMarker) {
                        marker.setVisible(true);
                    }
                    for(Marker marker : mealMarker) {
                        marker.setVisible(false);
                    }
                    for(Marker marker : eduMarker){
                        marker.setVisible(false);
                    }
                    for (Marker marker : favorMarker) {
                        //부식 유형인 즐겨찾기 마커를 보여준다.
                        MemberStore store= (MemberStore) marker.getTag();
                        if(store.getStype().equals("부식")){
                            marker.setVisible(true);
                        }
                    }
                    sideBtn.setBackgroundColor(Color.parseColor("#0B4D40"));
                }
                else{
                    if(mealBtnFlag ==false && eduBtnFlag ==false) {

                        for(Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : eduMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : favorMarker) {
                            marker.setVisible(true);
                        }
                    }
                    sideBtn.setBackgroundColor(Color.parseColor("#16A085"));

                }
            }

        });

        eduBtn.setOnClickListener(new View.OnClickListener() { //교육클릭시
            @Override
            public void onClick(View v) {

                eduBtnFlag = !eduBtnFlag;
                if (eduBtnFlag) {
                    for (Marker marker : eduMarker) {
                        marker.setVisible(true);
                    }
                    for (Marker marker : mealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : sideMealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : favorMarker) {
                        //교육 유형인 즐겨찾기 마커를 보여준다.
                        MemberStore store= (MemberStore) marker.getTag();
                        if(store.getStype().equals("교육")){
                            marker.setVisible(true);
                        }
                    }
                    eduBtn.setBackgroundColor(Color.parseColor("#D9A800"));

                }else{

                    if(mealBtnFlag ==false&& sideBtnFlag ==false) {
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : favorMarker) {
                            marker.setVisible(true);
                        }
                    }

                    eduBtn.setBackgroundColor(Color.parseColor("#FFDB58"));
                }

            }
        });

        eduBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favorBtnFlag = !favorBtnFlag;
                if (favorBtnFlag) {   //즐겨찾기만 보여준다

                    for (Marker marker : eduMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : mealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : sideMealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : favorMarker) {
                        marker.setVisible(true);
                    }

                    favorBtn.setBackgroundColor(Color.parseColor("#A82929"));
                }
                else {

                    if (mealBtnFlag == false && sideBtnFlag == false && eduBtnFlag == false) {
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : favorMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : favorMarker) {
                            marker.setVisible(true);
                        }

                    }
                    favorBtn.setBackgroundColor(Color.parseColor("#ff4040"));
                }
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //지도 객체 추출
        mapFragment.getMapAsync(this);
        //지도객체와 onMapReadyCallback객체를 연결함


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //map이 준비되면 호출되는 함수
        this.googleMap = googleMap;
        // 35.8691036023011, 128.59554606027856 중앙로 대백앞
        LatLng latLng = new LatLng(35.8691036023011, 128.59554606027856);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //지도는 카메라가 아래를 내려다보는 듯한 내용이 모델링됨


        //마커이미지 결정
        BitmapDrawable bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.bluemarker);
        Bitmap b=bitmapDraw.getBitmap();
        Bitmap mealCustomMarker = Bitmap.createScaledBitmap(b, 60, 60, false);

        for (MemberStore store : mealMemberStore) {

            MarkerOptions markerOptions = new MarkerOptions();
            //마커에 대한 정보를 갖고 있는 객체
            markerOptions.position(new LatLng(store.getLatitude(), store.getSlongitude()))
                    .title(store.getSname())
                    .icon(BitmapDescriptorFactory.fromBitmap(mealCustomMarker));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            mealMarker.add(marker);
        }
        //마커이미지 결정
        BitmapDrawable bitmapDraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.greenmarker);
        Bitmap b2=bitmapDraw2.getBitmap();
        Bitmap sideCustomMarker = Bitmap.createScaledBitmap(b2, 60, 60, false);

        for(MemberStore store: sideMealMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getSlongitude()))
                    .title(store.getSname())
                    .icon(BitmapDescriptorFactory.fromBitmap(sideCustomMarker));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            sideMealMarker.add(marker);
        }


        //마커이미지 결정
        BitmapDrawable bitmapDraw3=(BitmapDrawable)getResources().getDrawable(R.drawable.yellowmarker);
        Bitmap b3=bitmapDraw3.getBitmap();
        Bitmap eduCustomMarker = Bitmap.createScaledBitmap(b3, 60, 60, false);

        for(MemberStore store: eduMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getSlongitude()))
                    .title(store.getSname())
                    .icon(BitmapDescriptorFactory.fromBitmap(eduCustomMarker));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            eduMarker.add(marker);
        }

        //마커이미지 결정
        BitmapDrawable bitmapDraw4=(BitmapDrawable)getResources().getDrawable(R.drawable.favormarker);
        Bitmap b4=bitmapDraw4.getBitmap();
        Bitmap favorCustomMarker = Bitmap.createScaledBitmap(b4, 60, 60, false);

        for(MemberStore store: favorMemberStore) {
            Log.d("tag","즐찾마커설정");
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getSlongitude()))
                    .title(store.getSname())
                    .icon(BitmapDescriptorFactory.fromBitmap(favorCustomMarker));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            favorMarker.add(marker);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }

        this.googleMap.setOnMarkerClickListener(markerClickListener);
        this.googleMap.setOnMapClickListener(mapClickListener);

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(@NonNull @NotNull LatLng latLng) {
            card_view.setVisibility(View.GONE);
        }


    };

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            card_view.setVisibility(View.VISIBLE);
            TextView name = (TextView)findViewById(R.id.st_name);
            TextView num =(TextView)findViewById(R.id.st_num);
            TextView address=(TextView)findViewById(R.id.st_address);

            MemberStore store = (MemberStore) marker.getTag();
            name.setText(store.getSname());
            num.setText(store.getSnum());
            address.setText(store.getSaddress());

            String st_num=store.getSnum().toString();
            st_num=st_num.replace("-","");
            String tell;
            tell="tel:"+st_num;

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tell));
                    startActivity(intent);
                }
            });

            return false;
        }
    };


}