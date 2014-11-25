package com.jdmaestre.uninorte.barranquillareport;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jose on 05/11/2014.
 */
public class MainReportesFragment extends Fragment {

    ExpandableListAdapter mlistAdapter;
    ExpandableListView mexpListView;
    List<String> mlistDataHeader;
    HashMap<String, List<String>> mlistDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reportes, container, false);
        return rootView;


    }

    @Override
    public void onResume(){
        super.onResume();

        Button mReportesButton = (Button)getView().findViewById(R.id.reportes_Button);
        // get the listview
        mexpListView = (ExpandableListView) getView().findViewById(R.id.menuExpandableListView);
        // preparing list data

        // setting list adapter


        mexpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                Toast.makeText(
                        getActivity(),
                        mlistDataHeader.get(i)
                                + " : "
                                + mlistDataChild.get(
                                mlistDataHeader.get(i)).get(
                                i2), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        mReportesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence[] tipoReporte = new CharSequence[]{"Año","Mes","Dia","Cancelar"};
                new AlertDialog.Builder(getActivity())
                        .setTitle("Organizar por:")
                        .setItems(tipoReporte, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item


                                if (which == 0){
                                    prepareListDataAño();
                                    mlistAdapter = new ExpandableListAdapter(getActivity(), mlistDataHeader, mlistDataChild);
                                    mexpListView.setAdapter(mlistAdapter);

                                }else if (which == 1){

                                    prepareListDataMes();
                                    mlistAdapter = new ExpandableListAdapter(getActivity(), mlistDataHeader, mlistDataChild);
                                    mexpListView.setAdapter(mlistAdapter);

                                }else if (which == 2){
                                    prepareListDataDia();
                                    mlistAdapter = new ExpandableListAdapter(getActivity(), mlistDataHeader, mlistDataChild);
                                    mexpListView.setAdapter(mlistAdapter);

                                }



                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });



    }


    private void prepareListDataAño() {
        mlistDataHeader = new ArrayList<String>();
        mlistDataChild = new HashMap<String, List<String>>();
        List<String> list0 = new ArrayList<String>();List<String> list1 = new ArrayList<String>();List<String> list2 = new ArrayList<String>();List<String> list3 = new ArrayList<String>();
        List<String> list4 = new ArrayList<String>();List<String> list5 = new ArrayList<String>();List<String> list6 = new ArrayList<String>();List<String> list7 = new ArrayList<String>();
        String dateString = "No paso nada";
        int anho = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        try{
            for (int i = 0;i<MainMapFragment.globalJSON.length() ;i++){
                JSONObject post = MainMapFragment.globalJSON.getJSONObject(i);
                Date date = null;
                dateString = post.getString("fecha");
                dateString = dateString.substring(9, 20);

                try{
                    date = dateFormat.parse(dateString);
                    anho = date.getYear();
                    boolean sw = false;
                    for (int n=0;n<mlistDataHeader.size();n++){
                        if (String.valueOf(anho + 1900).equals(mlistDataHeader.get(n))){
                            sw = true;
                        }
                    }

                    if (sw == false){
                        mlistDataHeader.add(String.valueOf((anho + 1900)));
                    }

                }catch (Exception e){

                }
            }

        }catch (JSONException e){

        }


        List<String> top = new ArrayList<String>();
        try{
            for (int i = 0;i<MainMapFragment.globalJSON.length() ;i++){
                JSONObject post = MainMapFragment.globalJSON.getJSONObject(i);
                Date date = null;
                dateString = post.getString("fecha");
                String nivel = post.getString("nivel");
                String substring = dateString.substring(9, 20);

                try{
                    date = dateFormat.parse(substring);
                    anho = date.getYear();

                    for (int n=0;n<mlistDataHeader.size();n++){
                        if (String.valueOf(anho + 1900).equals(mlistDataHeader.get(n))){
                            switch (n){case 0: list0.add(dateString+nivel);break; case 1: list1.add(dateString+nivel);break;case 2: list2.add(dateString+nivel);break;case 3: list3.add(dateString+nivel);
                                break;case 4: list4.add(dateString+nivel);break;case 5: list5.add(dateString+nivel);break;case 6: list6.add(dateString+nivel);break;case 7: list7.add(dateString+nivel);}
                        }
                    }


                }catch (Exception e){

                }
            }

        }catch (JSONException e){

        }

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

       for (int n = 0;n<mlistDataHeader.size();n++){
           switch (n){case 0:mlistDataChild.put(mlistDataHeader.get(n), list0);break;case 1:mlistDataChild.put(mlistDataHeader.get(n), list1);break;case 2:mlistDataChild.put(mlistDataHeader.get(n), list2);
               break;case 3:mlistDataChild.put(mlistDataHeader.get(n), list3);break;case 4:mlistDataChild.put(mlistDataHeader.get(n), list4);break;case 5:mlistDataChild.put(mlistDataHeader.get(n), list5);}
       }



    }
    private void prepareListDataMes() {
        mlistDataHeader = new ArrayList<String>();
        mlistDataChild = new HashMap<String, List<String>>();
        List<String> list0 = new ArrayList<String>();List<String> list1 = new ArrayList<String>();List<String> list2 = new ArrayList<String>();List<String> list3 = new ArrayList<String>();
        List<String> list4 = new ArrayList<String>();List<String> list5 = new ArrayList<String>();List<String> list6 = new ArrayList<String>();List<String> list7 = new ArrayList<String>();
        List<String> list8 = new ArrayList<String>();List<String> list9 = new ArrayList<String>();List<String> list10 = new ArrayList<String>();List<String> list11 = new ArrayList<String>();
        String dateString = "No paso nada";
        int mes = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        try{
            for (int i = 0;i<MainMapFragment.globalJSON.length() ;i++){
                JSONObject post = MainMapFragment.globalJSON.getJSONObject(i);
                Date date = null;
                dateString = post.getString("fecha");
                dateString = dateString.substring(9, 20);

                try{
                    date = dateFormat.parse(dateString);
                    mes = date.getMonth();
                    boolean sw = false;
                    for (int n=0;n<mlistDataHeader.size();n++){
                        if (getMes(mes).equals(mlistDataHeader.get(n))){
                            sw = true;
                        }
                    }

                    if (sw == false){
                        mlistDataHeader.add(getMes(mes));
                    }

                }catch (Exception e){

                }
            }

        }catch (JSONException e){

        }

        try{
            for (int i = 0;i<MainMapFragment.globalJSON.length() ;i++){
                JSONObject post = MainMapFragment.globalJSON.getJSONObject(i);
                Date date = null;
                dateString = post.getString("fecha");
                String nivel = post.getString("nivel");
                String substring = dateString.substring(9, 20);

                try{
                    date = dateFormat.parse(substring);
                    mes = date.getMonth();

                    for (int n=0;n<mlistDataHeader.size();n++){
                        if (getMes(mes).equals(mlistDataHeader.get(n))){
                            switch (n){case 0: list0.add(dateString+nivel);break; case 1: list1.add(dateString+nivel);break;case 2: list2.add(dateString+nivel);break;case 3: list3.add(dateString+nivel);
                                break;case 4: list4.add(dateString+nivel);break;case 5: list5.add(dateString+nivel);break;case 6: list6.add(dateString+nivel);break;case 7: list7.add(dateString+nivel);
                                break;case 8: list8.add(dateString+nivel);break;case 9: list9.add(dateString+nivel);break;case 10: list10.add(dateString+nivel);break;case 11: list11.add(dateString+nivel);}
                        }
                    }


                }catch (Exception e){

                }
            }

        }catch (JSONException e){

        }


        for (int n = 0;n<mlistDataHeader.size();n++){
            switch (n){case 0:mlistDataChild.put(mlistDataHeader.get(n), list0);break;case 1:mlistDataChild.put(mlistDataHeader.get(n), list1);break;case 2:mlistDataChild.put(mlistDataHeader.get(n), list2);
                break;case 3:mlistDataChild.put(mlistDataHeader.get(n), list3);break;case 4:mlistDataChild.put(mlistDataHeader.get(n), list4);break;case 5:mlistDataChild.put(mlistDataHeader.get(n), list5);}
        }



    }
    private void prepareListDataDia() {
        mlistDataHeader = new ArrayList<String>();
        mlistDataChild = new HashMap<String, List<String>>();
        List<String> list0 = new ArrayList<String>();List<String> list1 = new ArrayList<String>();List<String> list2 = new ArrayList<String>();List<String> list3 = new ArrayList<String>();
        List<String> list4 = new ArrayList<String>();List<String> list5 = new ArrayList<String>();List<String> list6 = new ArrayList<String>();List<String> list7 = new ArrayList<String>();
        List<String> list8 = new ArrayList<String>();List<String> list9 = new ArrayList<String>();List<String> list10 = new ArrayList<String>();List<String> list11 = new ArrayList<String>();
        String dateString = "No paso nada";
        int dia = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        try{
            for (int i = 0;i<MainMapFragment.globalJSON.length() ;i++){
                JSONObject post = MainMapFragment.globalJSON.getJSONObject(i);
                Date date = null;
                dateString = post.getString("fecha");
                dateString = dateString.substring(9, 20);

                try{
                    date = dateFormat.parse(dateString);
                    dia = date.getDay();
                    boolean sw = false;
                    for (int n=0;n<mlistDataHeader.size();n++){
                        if (getDia(dia).equals(mlistDataHeader.get(n))){
                            sw = true;
                        }
                    }

                    if (sw == false){


                        mlistDataHeader.add(String.valueOf((getDia(dia))));
                    }

                }catch (Exception e){

                }
            }

        }catch (JSONException e){

        }

        try{
            for (int i = 0;i<MainMapFragment.globalJSON.length() ;i++){
                JSONObject post = MainMapFragment.globalJSON.getJSONObject(i);
                Date date = null;
                dateString = post.getString("fecha");
                String nivel = post.getString("nivel");
                String substring = dateString.substring(9, 20);

                try{
                    date = dateFormat.parse(substring);
                    dia = date.getDay();

                    for (int n=0;n<mlistDataHeader.size();n++){
                        if (getDia(dia).equals(mlistDataHeader.get(n))){
                            switch (n){case 0: list0.add(dateString+nivel);break; case 1: list1.add(dateString+nivel);break;case 2: list2.add(dateString+nivel);break;case 3: list3.add(dateString+nivel);
                                break;case 4: list4.add(dateString+nivel);break;case 5: list5.add(dateString+nivel);break;case 6: list6.add(dateString+nivel);break;case 7: list7.add(dateString+nivel);
                                break;case 8: list8.add(dateString+nivel);break;case 9: list9.add(dateString+nivel);break;case 10: list10.add(dateString+nivel);break;case 11: list11.add(dateString+nivel);}
                        }
                    }


                }catch (Exception e){

                }
            }

        }catch (JSONException e){

        }


        for (int n = 0;n<mlistDataHeader.size();n++){
            switch (n){case 0:mlistDataChild.put(mlistDataHeader.get(n), list0);break;case 1:mlistDataChild.put(mlistDataHeader.get(n), list1);break;case 2:mlistDataChild.put(mlistDataHeader.get(n), list2);
                break;case 3:mlistDataChild.put(mlistDataHeader.get(n), list3);break;case 4:mlistDataChild.put(mlistDataHeader.get(n), list4);break;case 5:mlistDataChild.put(mlistDataHeader.get(n), list5);
                break;case 6:mlistDataChild.put(mlistDataHeader.get(n), list7);break;case 7:mlistDataChild.put(mlistDataHeader.get(n), list7);break;case 8:mlistDataChild.put(mlistDataHeader.get(n), list8);
                break;case 9:mlistDataChild.put(mlistDataHeader.get(n), list9);break;case 10:mlistDataChild.put(mlistDataHeader.get(n), list10);break;case 11:mlistDataChild.put(mlistDataHeader.get(n), list11);}
        }



    }

    private String getDia(int dia) {
        String dayString;
        switch (dia+1) {
            case 1:  dayString = "Lunes";
                break;
            case 2:  dayString = "Martes";
                break;
            case 3:  dayString = "Miercoles";
                break;
            case 4:  dayString = "Jueves";
                break;
            case 5:  dayString = "Viernes";
                break;
            case 6:  dayString = "Sabado";
                break;
            case 7:  dayString = "Domingo";
                break;
            default: dayString = "Invalid day";
                break;
        }
        return dayString;
    }
    private String getMes(int mes) {
        String monthString;
        switch (mes+1) {
            case 1:  monthString = "Enero";
                break;
            case 2:  monthString = "Febrero";
                break;
            case 3:  monthString = "Marzo";
                break;
            case 4:  monthString = "Abril";
                break;
            case 5:  monthString = "Mayo";
                break;
            case 6:  monthString = "Junio";
                break;
            case 7:  monthString = "July¿io";
                break;
            case 8:  monthString = "Agosto";
                break;
            case 9:  monthString = "Septiembre";
                break;
            case 10: monthString = "Octubre";
                break;
            case 11: monthString = "Noviembre";
                break;
            case 12: monthString = "Diciembre";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

}

class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap< String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.menuListItem);
        TextView txtListChildReview = (TextView) convertView
                .findViewById(R.id.menuListItemReview);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.menuListImage);

        String fecha = childText.substring(9, 20);
        String grado = childText.substring(63);



        if (grado.equals("Leve")){
            Picasso.with(this._context).load(R.drawable.cone).resize(60,60).into(imgView);}
        else if (grado.equals("Grave"))
        {Picasso.with(this._context).load(R.drawable.cone).resize(60,60).into(imgView);
        }else{
            Picasso.with(this._context).load(R.drawable.truck17).resize(60,60).into(imgView);;
        }


        txtListChild.setText(grado);
        txtListChildReview.setText(fecha);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.menuListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
