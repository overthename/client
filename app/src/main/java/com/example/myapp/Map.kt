package com.example.myapp
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Map : Fragment(),OnMapReadyCallback{
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var map:GoogleMap
    private lateinit var locationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("fragment","ddddd")


        AndPermission.with(this).runtime().permission(Permission.Group.LOCATION)
            .onGranted { permissions ->
                Log.d("permissions", "허용된 권한: ${permissions.size}")
            }
            .onDenied { permissions ->
                Log.d("permissions", "거부된 권한: ${permissions.size}")
            }.start()

        locationClient=LocationServices.getFusedLocationProviderClient(this.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("fragment","onCreateView실행")
        val view:View= inflater.inflate(R.layout.fragment_map,container,false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.sanitarypad_map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d("fragment","onMapReady실행")
        map=p0
        map.mapType=GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled=true

        try {
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val lat=location!!.latitude
                    val lng=location.longitude

                    //이미지 필요없이 옵션 사용해서 마커 추가
                    map.addMarker(MarkerOptions().position(LatLng(lat,lng)).title("현재 내 위치")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat,lng),15f))
                }
        }
        catch(e:SecurityException){
            e.printStackTrace()
        }
    }


    private fun requestLocation(){
        Log.d("req_location","위치 변화 감지")
        try{
            locationClient?.lastLocation?.addOnSuccessListener{location:Location->
                if(location==null){
                    Log.d("location_check","최근 위치 추적 실패")
                }
                else{
                    Log.d("location_check","최근 위치 추적 성공: ${location.latitude},${location.longitude}")
                }
            }
                    ?.addOnFailureListener{
                        Log.d("location_check","최근 위치 추적 실패-리스너 실행x")
                        it.printStackTrace()
                    }

            val curr_location_request= LocationRequest.create()
            curr_location_request.run{
                priority=LocationRequest.PRIORITY_HIGH_ACCURACY
                interval=60*1000
            }
            val curr_location_callback=object: LocationCallback(){
                override fun onLocationResult(p0: LocationResult?) {
                    p0?.let{
                        for((i,location) in it.locations.withIndex()){
                            Log.d("my_location","내 위치:${location.latitude},${location.longitude}")
                        }
                        val curPoint=LatLng(it.locations[0].latitude,it.locations[0].longitude)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint,15f))
                        map.addMarker(MarkerOptions().position(LatLng(it.locations[0].latitude,it.locations[0].longitude))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                    }
                }
            }
            locationClient?.requestLocationUpdates(curr_location_request,curr_location_callback, Looper.myLooper())
        }
        catch(e:SecurityException){
            e.printStackTrace()
        }
    }

}