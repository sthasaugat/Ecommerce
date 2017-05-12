package com.dev.app.playground;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bumptech.glide.Glide;
import com.dev.app.playground.Adapter.FeaturedAdapter;
import com.dev.app.playground.DatabaseClasses.nBulletinDatabase;
import com.dev.app.playground.Helper.Constant;
import com.dev.app.playground.Helper.GlobalState;
import com.dev.app.playground.JavaObject.ImagesModel;
import com.dev.app.playground.JSONParser.JsonParser;
import com.dev.app.playground.JavaObject.Category;
import com.dev.app.playground.JavaObject.FeaturedProduct;
import com.dev.app.playground.Slider.ViewPagerAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //declearation for featured
    private static int currentPage = 0;
    private static int Num_Pages = 0;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    List<FeaturedProduct> featuredProductList, featuredProductList1, featuredProductList2;
    private RecyclerView mProductRV, mProductRV1, mProductRV2;
    private FeaturedAdapter mAdapter, mAdapter1, mAdapter2;
    Button mBtnMore;

    TextView menuMyWishList, menuMyCart;
    nBulletinDatabase db;
    private static long back_pressed;
    GlobalState globalState = GlobalState.singleton;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mProductRV = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager
                (ActivityHome.this, LinearLayoutManager.HORIZONTAL, false);
        mProductRV.setLayoutManager(llm);

        mBtnMore = (Button) findViewById(R.id.btnMore);
        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, ActivityMore.class));
            }
        });
        mProductRV1 = (RecyclerView) findViewById(R.id.recycler_view1);
        LinearLayoutManager llm1 = new LinearLayoutManager
                (ActivityHome.this, LinearLayoutManager.HORIZONTAL, false);
        mProductRV1.setLayoutManager(llm1);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        mProductRV2 = (RecyclerView) findViewById(R.id.recycler_view2);
        LinearLayoutManager llm2 = new LinearLayoutManager
                (ActivityHome.this, LinearLayoutManager.HORIZONTAL, false);
        mProductRV2.setLayoutManager(llm2);

        prepareSlider();

        db = nBulletinDatabase.getInstace(getApplicationContext());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menuMyWishList = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_wishList));
        menuMyCart = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_Cart));
        loginDetail lD = gson.fromJson(globalState.getPREFS_VALID_USER_INFO(), loginDetail.class);
        //finding header layout view of navigationDrawer
        View header = navigationView.getHeaderView(0);
        TextView headerUsername = (TextView) header.findViewById(R.id.headerUsername);
        TextView headerEmail = (TextView) header.findViewById(R.id.headerEmail);
        ImageView headerImage = (ImageView) header.findViewById(R.id.nav_ImageView);
        if (globalState.getPrefsIsLoggedIn().equals("true")) {
            headerUsername.setText(lD.sFullName);
            headerEmail.setText(lD.sEmail);
            Glide.with(headerImage.getContext()).load("http://159.203.186.37/laravel/ecommerce/public/users/" + lD.sImage)
                    .dontAnimate().into(headerImage);

        }
        new AsyncGet().execute();
    }

    private void initializeCountDrawer() {
        int size = db.getMyWishlist().size();
        int size1 = db.getMyCart().size();
        if (size > 0) {
            menuMyWishList.setGravity(Gravity.CENTER_VERTICAL);
            menuMyWishList.setTypeface(null, Typeface.BOLD);
            menuMyWishList.setTextColor(getResources().getColor(R.color.grey));
            menuMyWishList.setText(Integer.toString(size));
        }

        if (size1 > 0) {
            menuMyCart.setGravity(Gravity.CENTER_VERTICAL);
            menuMyCart.setTypeface(null, Typeface.BOLD);
            menuMyCart.setTextColor(getResources().getColor(R.color.grey));
            menuMyCart.setText(Integer.toString(size1));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
            else
                Snackbar.make(findViewById(R.id.scrollView), "Press again to exit", Snackbar.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
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
        if (id == R.id.mainMyCart) {
            startActivity(new Intent(ActivityHome.this, ActivityMyCart.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            onBackPressed();
        } else if (id == R.id.nav_MyAccount) {
            startActivity(new Intent(ActivityHome.this, ActivityMyAccount.class));
        } else if (id == R.id.nav_wishList) {
            startActivity(new Intent(ActivityHome.this, ActivityMyWishlist.class));
        } else if (id == R.id.nav_Cart) {
            startActivity(new Intent(ActivityHome.this, ActivityMyCart.class));
        } else if (id == R.id.nav_MyOrder) {
            //startActivity(new Intent(ActivityHome.this, ActivityMyOrders.class));
        } else if (id == R.id.nav_CustomerSupport) {
            startActivity(new Intent(ActivityHome.this, ActivityCustomer.class));
        } else if (id == R.id.nav_Logout) {
            logOutCheck();
        } else if (id == R.id.nav_Legal) {
            startActivity(new Intent(ActivityHome.this, ActivityLegal.class));

        }
        initializeCountDrawer();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOutCheck() {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage("Log out of TulipBazar ?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // positive button logic
                globalState.setPrefsValidUserInfo("false", 0);
                globalState.setPrefsIsLoggedIn("false", 0);
                db.deleteDatabase();
                startActivity(new Intent(ActivityHome.this, ActivityLogin.class));
                finish();
            }
        });
        builder.show();

    }

    private class AsyncGet extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(ActivityHome.this);
        int flag = 0;
        public List<Category> cat = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (!GlobalState.isNetworkOnline() || !GlobalState.isGoogleReachableWithInetAddress()) {
                flag = 2;
            } else {
                HashMap<String, String> loginHm = new HashMap<>();

                JsonParser jsonParser = new JsonParser();
                JSONObject jsonObject = jsonParser.getForJSONObject(Constant.BASE_URL + Constant.PRODUCTS, loginHm);

                try {
                    if (jsonObject == null) {
                        flag = 1;

                    } else if (jsonObject.getString("error").equals("false")) {
                        flag = 2;

                        JSONArray jArray = jsonObject.getJSONArray("category");

                        // Extract data from json and store into ArrayList as class objects
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);
                            String category = json_data.getString("name");
                            Category categories = new Category(category);
                            cat.add(categories);
                            JSONArray productArray = json_data.getJSONArray("products");
                            for (int j = 0; j < productArray.length(); j++) {
                                JSONObject product_data = productArray.getJSONObject(j);
                                String image = Constant.IMAGE_BASE + product_data.getString("image");
                                String name = product_data.getString("name");
                                String price = product_data.getString("price");
                                String pID = product_data.getString("id");
                                String category_id = product_data.getString("category_id");
                                db.addProducts(new FeaturedProduct(image, name, price, category_id, pID, "0"));

                            }
                        }
                    } else {
                        flag = 3;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            //this method will be running on UI thread
            progressDialog.dismiss();
            if (flag == 1) {
                Toast.makeText(ActivityHome.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
            if (flag == 2) {
                // Setup and Handover data to recyclerView

                featuredProductList = db.getProduct(12);
                mAdapter = new FeaturedAdapter(featuredProductList);
                mProductRV.setAdapter(mAdapter);

                featuredProductList1 = db.getProduct(7);
                mAdapter1 = new FeaturedAdapter(featuredProductList1);
                mProductRV1.setAdapter(mAdapter1);

                featuredProductList2 = db.getProduct(8);
                mAdapter2 = new FeaturedAdapter(featuredProductList2);
                mProductRV2.setAdapter(mAdapter2);
            }
            if (flag == 3) {
                Toast.makeText(ActivityHome.this, "unexpected error", Toast.LENGTH_SHORT).show();
                flag = 2;
            }
            if (flag == 4) {
                Toast.makeText(ActivityHome.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void prepareSlider() {
        List<ImagesModel> imagesModelList = new ArrayList<>();
        final ArrayList<Integer> IMAGES = new ArrayList<>();
        ImagesModel imagesModel = new ImagesModel(R.drawable.banner1);
        imagesModelList.add(imagesModel);
        imagesModel = new ImagesModel(R.drawable.banner2);
        imagesModelList.add(imagesModel);
        imagesModel = new ImagesModel(R.drawable.banner3);
        imagesModelList.add(imagesModel);
        imagesModel = new ImagesModel(R.drawable.banner4);
        imagesModelList.add(imagesModel);

        viewPagerAdapter = new ViewPagerAdapter(ActivityHome.this, imagesModelList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();
        Num_Pages = imagesModelList.size();
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == Num_Pages) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 4000, 4000);
    }
}


