package com.example.helsanf.submissionkamus.activity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.helsanf.submissionkamus.R;
import com.example.helsanf.submissionkamus.adapter.AdapterKamus;
import com.example.helsanf.submissionkamus.db.KamusHelper;
import com.example.helsanf.submissionkamus.model.KamusModel;
import com.example.helsanf.submissionkamus.res.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    AdapterKamus adapterKamus;
    KamusHelper kamusHelper;
    String language;
    android.widget.SearchView searchView;
    private ArrayList<KamusModel> mList = new ArrayList<>();
    private List<KamusModel> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        kamusHelper = new KamusHelper(this);
        adapterKamus = new AdapterKamus(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterKamus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchView = findViewById(R.id.search);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);

        language = "Eng";
        getData(language, "");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void getData(String languange,  String search) {
        try {
            kamusHelper.open();
            if (search.isEmpty()) {
                mList = kamusHelper.getAllData(languange);
            } else {
                mList = kamusHelper.getDatabyKata(search, languange);
            }

            String title = null;
            String hint = null;
            if (languange == "Eng") {
                title   = getResources().getString(R.string.eng_in);
                hint    = getResources().getString(R.string.search);
            } else {
                title = getResources().getString(R.string.in_eng);
                hint    = getResources().getString(R.string.search);
            }
            getSupportActionBar().setSubtitle(title);
            searchView.setQueryHint(hint);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamusHelper.close();
        }
        adapterKamus.replaceAdapter(mList);
        reloadView(adapterKamus,mList);
    }

    private void clickItemDetail(KamusModel kamusModel){
        Intent detailActivity = new Intent(this, DetilActivity.class);
        detailActivity.putExtra(DetilActivity.KATA,kamusModel.getKosakata());
        detailActivity.putExtra(DetilActivity.ARTI,kamusModel.getDeskripsi());
        startActivity(detailActivity);
    }

    public void reloadView(RecyclerView.Adapter adapter, final List<KamusModel> list ){
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                clickItemDetail(mList.get(position));
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_eng) {
            // Handle the camera action
            language = "Eng";
            getData(language,"");
        } else if (id == R.id.nav_in) {
            language = "Ind";
            getData(language, "");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        getData(language, keyword);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String keyword) {
        getData(language, keyword);
        return false;
    }
}
