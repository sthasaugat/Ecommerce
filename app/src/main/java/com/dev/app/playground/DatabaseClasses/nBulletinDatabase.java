package com.dev.app.playground.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dev.app.playground.JavaObject.Add2wishList;
import com.dev.app.playground.JavaObject.FeaturedProduct;
import com.dev.app.playground.JavaObject.MyCart;

import java.util.ArrayList;
import java.util.List;

public class nBulletinDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tulip_Bazar";
    private static nBulletinDatabase mInstace = null;

    // PRODUCT TABLE CREATION
    public static final String TABLE_PRODUCT = "product_table";
    public static final String TABLE_MY_CART = "cart_table";
    public static final String TABLE_MY_WISHLIST = "wishlist_table";

    // PRODUCT ATTRIBUTES
    public static final String KEY_ID = "id";
    public static final String KEY_PRODUCT = "product_name";
    public static final String KEY_PRICE = "product_price";
    public static final String KEY_IMAGE = "product_image";
    public static final String KEY_CATEGORY_ID = "product_category";
    public static final String KEY_STATUS = "status";
    // CART ATTRIBUTES
    public static final String KEY_CART_ID = "id";
    public static final String KEY_CART_PRODUCT = "product_name";
    public static final String KEY_CART_PRICE = "product_price";
    public static final String KEY_CART_IMAGE = "product_image";
    public static final String KEY_CART_STATUS = "status";
    //WISHLIST ATTRIBUTES
    public static final String KEY_WISHLIST_ID = "id";
    public static final String KEY_WISHLIST_PRODUCT = "product_name";
    public static final String KEY_WISHLIST_PRICE = "product_price";
    public static final String KEY_WISHLIST_IMAGE = "product_image";
    public static final String KEY_WISHLIST_WISHID = "wishlist_id";


    public nBulletinDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static nBulletinDatabase getInstace(Context ctx) {
        if (mInstace == null) {
            mInstace = new nBulletinDatabase(ctx.getApplicationContext());
        }
        return mInstace;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_CATEGORY_ID + " TEXT,"
                + KEY_STATUS + " TEXT " +
                ")";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_MY_CART = "CREATE TABLE " + TABLE_MY_CART +
                "("
                + KEY_CART_ID + " INTEGER PRIMARY KEY,"
                + KEY_CART_PRODUCT + " TEXT,"
                + KEY_CART_PRICE + " TEXT,"
                + KEY_CART_IMAGE + " TEXT,"
                + KEY_CART_STATUS + " TEXT " +
                ")";
        db.execSQL(CREATE_MY_CART);

        String CREATE_MY_WISHLIST = "CREATE TABLE " + TABLE_MY_WISHLIST +
                "("
                + KEY_WISHLIST_ID + " INTEGER PRIMARY KEY,"
                + KEY_WISHLIST_PRODUCT + " TEXT,"
                + KEY_WISHLIST_PRICE + " TEXT,"
                + KEY_WISHLIST_IMAGE + " TEXT,"
                + KEY_WISHLIST_WISHID + " TEXT " +
                ")";
        db.execSQL(CREATE_MY_WISHLIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MY_CART);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MY_WISHLIST);
        onCreate(db);
    }

    public boolean addProducts(FeaturedProduct featuredProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, featuredProduct.getpID());
        values.put(KEY_PRODUCT, featuredProduct.getProductName());
        values.put(KEY_PRICE, featuredProduct.getPrice());
        values.put(KEY_IMAGE, featuredProduct.getImage());
        values.put(KEY_CATEGORY_ID, featuredProduct.getCategory_id());
        values.put(KEY_STATUS, featuredProduct.getStatus());
        long result = db.insert(TABLE_PRODUCT, null, values);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<FeaturedProduct> getProduct(int flag) {
        List<FeaturedProduct> featuredProductList = new ArrayList<>();
        // String SELECT = "SELECT * FROM " + TABLE_PRODUCT + " ORDER BY " + KEY_ID + " ASC ";
        String SELECT = null;
        if (flag == 12) {
            SELECT = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + KEY_CATEGORY_ID + " = " + "12";
        }
        if (flag == 7) {
            SELECT = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + KEY_CATEGORY_ID + " = " + "7";
        }
        if (flag == 8) {
            SELECT = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + KEY_CATEGORY_ID + " = " + "8";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);
        if (cursor.moveToFirst()) {
            do {
                FeaturedProduct featuredProduct = new FeaturedProduct();
                featuredProduct.setpID(cursor.getString(0));
                featuredProduct.setProductName(cursor.getString(1));
                featuredProduct.setPrice(cursor.getString(2));
                featuredProduct.setImage(cursor.getString(3));
                featuredProduct.setCategory_id(cursor.getString(4));
                featuredProduct.setStatus(cursor.getString(5));
                featuredProductList.add(featuredProduct);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return featuredProductList;
    }

    public boolean updateProduct(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, status);
        db.update(TABLE_PRODUCT, contentValues, KEY_ID + " =? ", new String[]{id});
        return true;
    }

    public boolean addToMyCart(MyCart myCart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CART_ID, myCart.getpID());
        values.put(KEY_CART_PRODUCT, myCart.getProductName());
        values.put(KEY_CART_PRICE, myCart.getPrice());
        values.put(KEY_CART_IMAGE, myCart.getImage());
        values.put(KEY_CART_STATUS, myCart.getStatus());
        long result = db.insert(TABLE_MY_CART, null, values);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<MyCart> getMyCart() {
        List<MyCart> myCartList = new ArrayList<>();
        String SELECT = "SELECT * FROM " + TABLE_MY_CART + " ORDER BY " + KEY_CART_ID + " ASC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);
        if (cursor.moveToFirst()) {
            do {
                MyCart myCartItem = new MyCart();
                myCartItem.setpID(cursor.getString(0));
                myCartItem.setProductName(cursor.getString(1));
                myCartItem.setPrice(cursor.getString(2));
                myCartItem.setImage(cursor.getString(3));
                myCartItem.setStatus(cursor.getString(4));
                myCartList.add(myCartItem);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return myCartList;
    }

    public void deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MY_CART, "id=?", new String[]{String.valueOf(id)});
    }

    public boolean updateCart(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, status);
        db.update(TABLE_MY_CART, contentValues, KEY_ID + " =? ", new String[]{id});
        return true;
    }

    public boolean addToWishlist(Add2wishList add2wishList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WISHLIST_ID, add2wishList.getpID());
        values.put(KEY_WISHLIST_PRODUCT, add2wishList.getProductName());
        values.put(KEY_WISHLIST_PRICE, add2wishList.getPrice());
        values.put(KEY_WISHLIST_IMAGE, add2wishList.getImage());
        values.put(KEY_WISHLIST_WISHID, add2wishList.getWishID());
        long result = db.insert(TABLE_MY_WISHLIST, null, values);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<Add2wishList> getMyWishlist() {
        List<Add2wishList> myWishList = new ArrayList<Add2wishList>();
        String SELECT = "SELECT * FROM " + TABLE_MY_WISHLIST + " ORDER BY " + KEY_WISHLIST_ID + " ASC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);
        if (cursor.moveToFirst()) {
            do {
                Add2wishList myWishItem = new Add2wishList();
                myWishItem.setpID(cursor.getString(0));
                myWishItem.setProductName(cursor.getString(1));
                myWishItem.setPrice(cursor.getString(2));
                myWishItem.setImage(cursor.getString(3));
                myWishItem.setWishID(cursor.getString(4));
                myWishList.add(myWishItem);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return myWishList;
    }

    public void deleteAllWish(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MY_WISHLIST,null,null);
    }

    public void deleteWishlistItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MY_WISHLIST, "id=?", new String[]{String.valueOf(id)});
    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                               String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield +  " = " + fieldValue ;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void deleteDatabase (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT,null,null);
        db.delete(TABLE_MY_WISHLIST,null,null);
        db.delete(TABLE_MY_CART,null,null);
    }
}