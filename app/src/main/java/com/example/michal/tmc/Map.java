package com.example.michal.tmc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class Map extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("message");
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        db = openOrCreateDatabase("TREES",MODE_PRIVATE,null);
        ArrayList<GeoPoint> arrTblNames = new ArrayList<GeoPoint>();
        //db.execSQL("INSERT INTO " + message.toString() +" (LON, LAT, IMAGE) values ('1','2', 'test')");
        Cursor c = db.rawQuery("SELECT * FROM " + message.toString()+";", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( new GeoPoint( c.getDouble( c.getColumnIndex("LAT")), c.getDouble(c.getColumnIndex("LON"))) );
                c.moveToNext();
            }
        }

        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        if(arrTblNames.get(0) != null){
        mapController.setCenter(arrTblNames.get(0));}
        for (GeoPoint point : arrTblNames ) {
            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);
        }



    }
}
