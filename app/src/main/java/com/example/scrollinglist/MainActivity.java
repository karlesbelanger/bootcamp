package com.example.scrollinglist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scrollinglist.pojo.Episode;
import com.example.scrollinglist.pojo.EpisodeResponse;
import com.example.scrollinglist.pojorec.Doc;
import com.example.scrollinglist.pojorec.RecommandationResponse;
import com.example.scrollinglist.tve.LoginActivity;
import com.example.scrollinglist.tve.TVEDelegate;
import com.example.scrollinglist.video.EpisodeAdapter;
import com.example.scrollinglist.video.EpisodesListener;
import com.example.scrollinglist.video.RecommandationListener;
import com.example.scrollinglist.video.RetrofitRec;
import com.example.scrollinglist.video.RetrofitVid;
import com.vmn.android.tveauthcomponent.component.TVEComponent;
import com.vmn.android.tveauthcomponent.error.TVEException;
import com.vmn.android.tveauthcomponent.error.TVEMessage;
import com.vmn.android.tveauthcomponent.model.TVESubscriber;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EpisodesListener, RecommandationListener, TVEDelegate.TveEventListener, SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<Episode> favourites;
    private boolean isFavorite = false;
    private List<Episode> mArrayList;
    private EpisodeAdapter mBasicAdapter;

    private TVEDelegate tve;

    private static final String API_KEY = "rsvA6TrDBB84UAI92oV6u4IYCEpREzk8ayuB8oIr";
    private MenuItem loginBtn;
    private String loginTitle = "Login";

    private static final String[] COUNTRIES = new String[]{"Belgium",
            "France", "France_", "Italy", "Germany", "Spain"};
    private Menu menu;
    private String filterText;
    private SearchView searchView;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tve = TVEDelegate.getInstance();
        tve.subscribeEventListener(this);


//
//        retrofitRec = new RetrofitRec(new RecommandationListener() {
//            @Override
//            public void onSuccess(RecommandationResponse data) {
//                Log.d(TAG, "onSuccess: " + data.responseHeader.toString());
//            }
//
//            @Override
//            public void onFailure(String errorMsg) {
//                Log.d(TAG, "onFailure: " + errorMsg);
//            }
//        });
//        retrofitRec.getRecommandation("south");



    }

//    private void toolbarSearchSetup(){
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
//        // actionBar.setDisplayShowTitleEnabled(false);
//        // actionBar.setIcon(R.drawable.ic_action_search);
//
//        LayoutInflater inflator = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.actionbar, null);
//
//        actionBar.setCustomView(v);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView) v
//                .findViewById(R.id.editText1);
//        textView.setAdapter(adapter);

    //    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        new RetrofitVid(this)
                .getEpisodes(API_KEY);
        mToolbar = (Toolbar) findViewById(R.id.a_main_toolbar);
        setSupportActionBar(mToolbar);
        //toolbarSearchSetup();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.collapseActionView(searchItem);
    }


    @Override
    public void onSuccess(RecommandationResponse data) {
        ArrayList<String> arrQ = new ArrayList<String>();
        ArrayAdapter<String> arrAdapter=
                new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrQ);
        for (Doc query :
                data.getResponse().getDocs()) {//get what
            arrQ.add(query.getTitleT());
        }
//        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.m_auto_comp);
//        autoCompleteTextView.setAdapter(arrAdapter);//sec
        MySearchDialog mySearchDialog = new MySearchDialog(this, data.getResponse().getDocs());
        mySearchDialog.show();
    }

    @Override
    public void onFailure(String errorMsg) {
        Toast.makeText(this, "Error: Please try again later.", Toast.LENGTH_LONG).show();
    }


    @Override//vid mgid
    public void onSuccess(EpisodeResponse data) {
        List<Episode> temp = data.getResponse().getEpisodes();
        if(isFavorite) {
            temp = filter(temp, filterText);
            if (favourites != null && favourites.size() <= 0) {
                Toast.makeText(this, "No Episode with the name of " + filterText + " found!", Toast.LENGTH_LONG).show();
            }
        }
        if (!isUserLoggedIn()) {
            mArrayList = new ArrayList<Episode>();
            for (Episode episode : temp) {//lets just populate the search cause the others didnt do the update on recycler view
                if (episode.getPlatforms().size() > 0 && episode.getPlatforms().get(0).getAuthRequired().equals("false"))
                    mArrayList.add(episode);
            }
        } else {
            mArrayList = temp;
        }
        mBasicAdapter = new EpisodeAdapter(mArrayList, this);
        Log.d(TAG, "onsuccess test: " + mArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.a_main_recycler);

        mRecyclerView.setAdapter(mBasicAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    // TVE Callbacks
    @Override
    public void initializationCompleted(TVESubscriber subscriber) {
    }

    @Override
    public void checkStatusCompleted(TVESubscriber subscriber) {
    }

    @Override
    public void loginFormPrepared(Fragment tveFragment) {

    }

    @Override//
    public void loginCompleted(TVESubscriber subscriber) {
        loginTitle = "Logout";
        Log.d(TAG, "loginCompleted: " + isUserLoggedIn());
    }

    @Override
    public void logoutCompleted() {
//TODO: tell your recycler adapter to refresh itself, now that it has lost permissions
        loginTitle = "Login";
        Log.d(TAG, "logoutCompleted: " + isUserLoggedIn());
    }

    @Override
    public void errorHappened(TVEException error) {
    }

    @Override
    public void onDisplayMessage(TVEMessage message) {
    }

    @Override
    public void onUserIdChanged(String userId) {
    }

    @Override
    public void learnMoreButtonClicked() {
    }

    @Override
    public void watchNowButtonClicked() {
        loginTitle = "Logout";
    }

    @Override
    public void closeButtonClicked() {
    }

    @Override
    public void freePreviewHasJustExpired() {
    }

    public boolean isUserLoggedIn() {
        return tve.getCurrentTveSubscriber() != null &&
                tve.getCurrentTveSubscriber().isLoggedIn();

    }

    private List<Episode> filter(List<Episode> episodes, String query) {
        query = query.toLowerCase();

        final List<Episode> filteredEpisodeList = new ArrayList<>();
        for (Episode episode : episodes) {
            final String text = episode.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredEpisodeList.add(episode);
            }
        }
        return filteredEpisodeList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);//can we fix this first tho <<<
        searchView.setOnSearchClickListener(searchClickListener);
        //autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_searchWidget);
        return true;
    }

    private RetrofitRec retrofitRec;
    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {//well im going for lunch for about 30-45
   
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_login) {
            if (item.getTitle().equals("Login")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                item.setTitle(loginTitle);
            } else {
                tve.unsubscribeEventListener(this);
                TVEComponent.getInstance().logout();
                item.setTitle(loginTitle);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        retrofitRec = new RetrofitRec(MainActivity.this);//nothing pops when i type south i did
        retrofitRec.getRecommandation(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            isFavorite = true;
        } else
            isFavorite = false;
        return false;
    }
    private class MySearchDialog extends Dialog {

        private List<Doc> doc;
        private ArrayList<String> arrQ;
        public MySearchDialog(Context context, List<Doc> doc ) {
            super(context);
            this.doc = doc;
            arrQ = new ArrayList<String>();
        }



        @Override
        public void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.assert_list);
            for (Doc query :
                    doc) {
                arrQ.add(query.getTitleT());
            }
            ListView lv = (ListView) findViewById(R.id.lv);
            String[] stockArr = new String[arrQ.size()];
            stockArr = arrQ.toArray(stockArr);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(),android.R.layout.simple_list_item_1, stockArr);
            lv.setAdapter(adapter);

        }

    }
}