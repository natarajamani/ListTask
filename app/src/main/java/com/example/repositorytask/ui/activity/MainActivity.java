package com.example.repositorytask.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.example.repositorytask.R;
import com.example.repositorytask.apibridge.ApiUtils;
import com.example.repositorytask.apibridge.APIService;
import com.example.repositorytask.model.RepositoryData;
import com.example.repositorytask.app.MyApplication;
import com.example.repositorytask.db.AppDataBase;
import com.example.repositorytask.ui.adapter.ItemAdapter;
import com.example.repositorytask.utils.LogUtils;
import com.example.repositorytask.viewmodel.ItemListViewModel;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity  extends AppCompatActivity{

    RecyclerView repoList;

    TextView tv_empty_list;

    MaterialSearchView searchView;

    SwipeRefreshLayout swipe_refresh;

    LinearLayout noConnection;

    Button btn_try;

    Context context;

    APIService apiService;

    private Dialog dialog;

    private ItemAdapter adpRepo;

    MenuItem searchItem;

    ItemListViewModel viewModel;

    boolean isRepoListAvailable=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            repoList=(RecyclerView)findViewById(R.id.repoList);
            tv_empty_list=(TextView)findViewById(R.id.tv_empty_list);
            searchView=(MaterialSearchView)findViewById(R.id.search_view);
            swipe_refresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
            noConnection=(LinearLayout)findViewById(R.id.noConnection);
            btn_try=(Button)findViewById(R.id.btn_try);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle(getResources().getString(R.string.trendingRepositories));

            context=this;

            apiService = ApiUtils.getAPIService(ApiUtils.BASE_URL);

            viewModel= ViewModelProviders.of(MainActivity.this).get(ItemListViewModel.class);

           // viewModel= ViewModelProviders.of(this).get(ItemListViewModel.class);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            repoList.setLayoutManager(mLayoutManager);
            repoList.setItemAnimator(new DefaultItemAnimator());

            LinearLayoutManager lManager = new LinearLayoutManager(getApplicationContext());
            lManager.setOrientation(RecyclerView.VERTICAL);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(repoList.getContext(),
                    lManager.getOrientation());
            repoList.addItemDecoration(dividerItemDecoration);

            adpRepo = new ItemAdapter(context);
            repoList.setAdapter(adpRepo);

            if(checknetworkConnection()){
                getNetworkList(false);
            }else{
                getLocalList();
            }


            btn_try.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{

                        if(checknetworkConnection()){
                            getNetworkList(false);
                            noConnection.setVisibility(View.GONE);
                        }

                    }catch (Exception e){
                        LogUtils.printException(e);
                    }
                }
            });

            searchView.setHint(getResources().getString(R.string.search));

            searchView.setVoiceSearch(false);

            searchView.setEllipsize(true);

            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    try{

                        if(query.length()>0) {

                            getLocalSearchList(query);
                        }
                        else{
                            getLocalList();
                        }
                    }catch (Exception e){
                        LogUtils.printException(e);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Do some magic
                    try{

                     if(newText.length()>0) {

                         getLocalSearchList(newText);

                      }
                     else{
                           getLocalList();
                      }
                    }catch (Exception e){
                        LogUtils.printException(e);
                    }

                    return true;
                }
            });

            swipe_refresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#0B6BC3"));
            swipe_refresh.setColorSchemeColors(Color.parseColor("#FFFFFF"));

            swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try{

                        if(searchView.isSearchOpen())
                        {
                            searchView.closeSearch();
                        }

                        getNetworkList(true);

                    }catch (Exception e){
                        LogUtils.printException(e);
                    }
                }
            });


        }catch (Exception e){
            LogUtils.printException(e);
        }


    }

    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        try{
            searchItem = menu.findItem(R.id.action_search);

            if( !isRepoListAvailable)
            {
                searchItem.setVisible(false);
            }

            searchView.setMenuItem(searchItem);
        }catch (Exception e){
            LogUtils.printException(e);
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is used to show the progress bar
     * @param context
     */
    public  void showProgress(Context context){
        try{
            if(dialog==null) {
                dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.progress_bar);

                ProgressBar text = (ProgressBar) dialog.findViewById(R.id.progress);
                dialog.getWindow().setBackgroundDrawable(null);


                dialog.show();

            }
        }catch(Exception e){
            LogUtils.printException(e);
        }
    }

    /**
     * This method is used to hide the progress bar
     */
    public  void hideProgress(){
        try{
            LogUtils.e("Login","hideProgress");
            if(dialog!=null) {
                LogUtils.e("Login","dialog not noll");
                dialog.dismiss();
                dialog=null;
            }else{
                LogUtils.e("Login","dialog  noll");
            }
        }catch(Exception e){
            LogUtils.printException(e);
        }
    }



    private void getNetworkList(boolean isSwipe){

        try {

            if(!isSwipe){
                showProgress(context);
            }

            apiService.repositories("en").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<List<RepositoryData>>>() {
                @Override
                public void onCompleted() {
                    try{
                        LogUtils.e("getList","onCompleted");
                        if(!isSwipe){
                            hideProgress();
                        }else{
                            swipe_refresh.setRefreshing(false);
                        }

                    }catch (Exception e){
                        LogUtils.printException(e);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    try{
                        LogUtils.e("getList","onError");
                        if(!isSwipe){
                            hideProgress();
                        }else{
                            swipe_refresh.setRefreshing(false);
                        }
                        connectionError(context,e);
                    }catch (Exception e1){
                        LogUtils.printException(e1);
                    }
                }

                @Override
                public void onNext(retrofit2.Response<List<RepositoryData>> res) {
                    try{
                        LogUtils.e("getList","onNext");
                        if(!isSwipe){
                            hideProgress();
                        }else{
                            swipe_refresh.setRefreshing(false);
                        }

                        if(res.isSuccessful()) {
                            List<RepositoryData> lsResData=res.body();
                            if(lsResData!=null){
                                List<RepositoryData> alData=lsResData;
                                LogUtils.e("getList","onNext Status : "+lsResData.size());
                              //  final ItemListViewModel viewModel= ViewModelProviders.of(MainActivity.this).get(ItemListViewModel.class);
                                insertItem(viewModel,alData);
                            }

                        }else{
                           connectionError(context,res);
                        }

                    }catch (Exception e){
                        LogUtils.printException(e);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.printException(e);
        }


    }


    private void getLocalList(){

            try{

                Log.i("Observer","getLocalList()");

             // final ItemListViewModel viewModel= ViewModelProviders.of(this).get(ItemListViewModel.class);

                viewModel.getItems().observe(this, new Observer<PagedList<RepositoryData>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<RepositoryData> items) {
                        try{
                            Log.i("Observer","ListItem Change ......");

                            if (items != null) {
                                Log.i("Observer","ListItem Change ......"+items.size());

                                if(items.size()>0){
                                    tv_empty_list.setVisibility(View.GONE);
                                    noConnection.setVisibility(View.GONE);
                                    repoList.setVisibility(View.VISIBLE);
                                    adpRepo.submitList(items);
                                    isRepoListAvailable=true;
                                    setMenuVisibility();
                                }else{
                                    if(checknetworkConnection()){
                                        noConnection.setVisibility(View.GONE);
                                        tv_empty_list.setVisibility(View.VISIBLE);
                                    }else{
                                        noConnection.setVisibility(View.VISIBLE);
                                        tv_empty_list.setVisibility(View.GONE);
                                    }
                                    repoList.setVisibility(View.GONE);
                                    adpRepo.submitList(items);
                                }

                            } else {
                                if(checknetworkConnection()){
                                    noConnection.setVisibility(View.GONE);
                                    tv_empty_list.setVisibility(View.VISIBLE);
                                }else{
                                    noConnection.setVisibility(View.VISIBLE);
                                    tv_empty_list.setVisibility(View.GONE);
                                }
//                                tv_empty_list.setVisibility(View.VISIBLE);
                                repoList.setVisibility(View.GONE);
                            }



                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

    }

    private void setMenuVisibility()
    {
        if( null != searchItem)
        {
            searchItem.setVisible(true);
        }
    }



    private void getLocalSearchList(String strSearch){

        try{

           // final ItemListViewModel viewModel= ViewModelProviders.of(this).get(ItemListViewModel.class);

            String strFinalSearch="%"+strSearch+"%";
            viewModel.getSearchItems(strFinalSearch).observe(this, new Observer<PagedList<RepositoryData>>() {
                @Override
                public void onChanged(@Nullable PagedList<RepositoryData> items) {
                    try{
                        Log.i("Observer","ListItem Change ......");

                        if (items != null) {
                            Log.i("Observer","ListItem Change ......"+items.size());

                            if(items.size()>0){
                                tv_empty_list.setVisibility(View.GONE);
                                noConnection.setVisibility(View.GONE);
                                repoList.setVisibility(View.VISIBLE);
                                adpRepo.submitList(items);
                            }else{

                                noConnection.setVisibility(View.GONE);
                                tv_empty_list.setVisibility(View.VISIBLE);
                                repoList.setVisibility(View.GONE);
                                adpRepo.submitList(items);
                            }

                        } else {


                            noConnection.setVisibility(View.GONE);
                            tv_empty_list.setVisibility(View.VISIBLE);

                            repoList.setVisibility(View.GONE);
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void insertItem(ItemListViewModel viewModel,List<RepositoryData> alData){
     try{
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    viewModel.insertAll(alData);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Log.i("Observer","onPostExecute");

                getLocalList();

            }
        }.execute();

    } catch (Exception e) {
        LogUtils.printException(e);
    }
    }

    public  void connectionError(Context context, Response response){
        try{
            if (checknetworkConnection()){
                LogUtils.i("NetworkError","NetworkError : "+response.code()+" "+response.message());

                composeServerFailedAlert(context,response);
            }else {
                composeInternetFailedAlert(context);
            }
        }catch(Exception e){
            LogUtils.printException(e);
        }
    }


    /**
     * This method is used to compose connection error message and show the toast using exception
     * @param context Screen context
     */
    public  void connectionError(Context context, Throwable error){
        try{
            if (checknetworkConnection()){
                LogUtils.i("NetworkError","NetworkError : "+error.getMessage());
                error.printStackTrace();
                composeServerFailedAlert( context,  error);

            }else{
                composeInternetFailedAlert(context);
            }
        }catch(Exception e){
            LogUtils.printException(e);
        }
    }

    public  boolean checknetworkConnection(){
        boolean isConnected=false;
        try{
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }catch(Exception e){
            LogUtils.printException(e);
        }
        return  isConnected;
    }

    private  void composeInternetFailedAlert(Context context){
        try {
            String title =context.getResources().getString(R.string.connection_error);
            String msg =context.getResources().getString(R.string.check_internet_conn);
            String positivetext =context.getResources().getString(R.string.ok);
            showConnectionErrorAlert(context,title,msg,positivetext,true);
        }catch (Exception e){
            LogUtils.printException(e);
        }
    }


    private  void composeServerFailedAlert(Context context,Throwable error){
        try {
            String title =context.getResources().getString(R.string.server_error);
            String msg =context.getResources().getString(R.string.check_internet_conn);
            String positivetext =context.getResources().getString(R.string.ok);


            try{
                if(error instanceof UnknownHostException){
                    msg="Server not found";
                    showConnectionErrorAlert(context,title,msg,positivetext,true);
//                    writeLogEvent(context,"Server Error ",msg);
                }



            }catch (Exception e){
                LogUtils.printException(e);
            }
        }catch (Exception e){
            LogUtils.printException(e);
        }
    }

    private  void composeServerFailedAlert(Context context, Response response){
        try {
            String title =context.getResources().getString(R.string.server_error);
            String msg =context.getResources().getString(R.string.check_internet_conn);
            String positivetext =context.getResources().getString(R.string.ok);

            switch (response.code()) {
                case 404:
                    msg =context.getResources().getString(R.string.server_not_found);
                    break;
                case 500:
                    msg =response.message();
                    break;
                default:
                    msg =response.message();
                    break;
            }

            showConnectionErrorAlert(context,title,msg,positivetext,true);

        }catch (Exception e){
            LogUtils.printException(e);
        }
    }

    private  void showConnectionErrorAlert(Context context, String title, String message, String positivetext, boolean isdismisible){
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(positivetext,null);


            final android.app.AlertDialog dialog = builder.create();
            // display dialog
            dialog.setCancelable(isdismisible);
            dialog.show();


            Button pos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            pos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        LogUtils.printException(e);
                    }
                }
            });

        }catch (Exception e){
            LogUtils.printException(e);
        }
    }




    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





}
