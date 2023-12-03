package com.astarconcepts.brandbanao.custom.poster.create;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.astarconcepts.brandbanao.custom.poster.model.ElementInfoPoster;
import com.astarconcepts.brandbanao.custom.poster.views.text.TextInfo;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String BG_ALPHA = "BG_ALPHA";
    private static final String BG_COLOR = "BG_COLOR";
    private static final String BG_DRAWABLE = "BG_DRAWABLE";
    private static final String COLORTYPE = "COLORTYPE";
    private static final String COMPONENT_INFO = "COMPONENT_INFO";

    private static final String CREATE_TABLE_COMPONENT_INFO = "CREATE TABLE COMPONENT_INFO(COMP_ID INTEGER PRIMARY KEY,TEMPLATE_ID TEXT,POS_X TEXT,POS_Y TEXT,WIDHT TEXT,HEIGHT TEXT,ROTATION TEXT,Y_ROTATION TEXT,RES_ID TEXT,TYPE TEXT,ORDER_ TEXT,STC_COLOR TEXT,STC_OPACITY TEXT,XROTATEPROG TEXT,YROTATEPROG TEXT,ZROTATEPROG TEXT,STC_SCALE TEXT,STKR_PATH TEXT,COLORTYPE TEXT,STC_HUE TEXT,FIELD_ONE TEXT,FIELD_TWO TEXT,FIELD_THREE TEXT,FIELD_FOUR TEXT)";
    private static final String CREATE_TABLE_TEMPLATES = "CREATE TABLE TEMPLATES(TEMPLATE_ID INTEGER PRIMARY KEY,THUMB_URI TEXT,FRAME_NAME TEXT,RATIO TEXT,PROFILE_TYPE TEXT,SEEK_VALUE TEXT,TYPE TEXT,TEMP_PATH TEXT,TEMP_COLOR TEXT,OVERLAY_NAME TEXT,OVERLAY_OPACITY TEXT,OVERLAY_BLUR TEXT)";
    private static final String CREATE_TABLE_TEXT_INFO = "CREATE TABLE TEXT_INFO(TEXT_ID INTEGER PRIMARY KEY,TEMPLATE_ID TEXT,TEXT TEXT,FONT_NAME TEXT,TEXT_COLOR TEXT,TEXT_ALPHA TEXT,SHADOW_COLOR TEXT,SHADOW_PROG TEXT,BG_DRAWABLE TEXT,BG_COLOR TEXT,BG_ALPHA TEXT,POS_X TEXT,POS_Y TEXT,WIDHT TEXT,HEIGHT TEXT,ROTATION TEXT,TYPE TEXT,ORDER_ TEXT,XROTATEPROG TEXT,YROTATEPROG TEXT,ZROTATEPROG TEXT,CURVEPROG TEXT,FIELD_ONE TEXT,FIELD_TWO TEXT,FIELD_THREE TEXT,FIELD_FOUR TEXT,FIELD_LEFT_RIGH_SHADOW TEXT,FIELD_TOP_BOTTOM_SHADOW TEXT,FIELD_OUTER_SIZE TEXT,FIELD_OUTER_COLOR TEXT)";
    private static final String CURVEPROG = "CURVEPROG";
    private static final String DATABASE_NAME = "POSTERMAKER_DB";

    private static final String FIELD_FOUR = "FIELD_FOUR";
    private static final String FIELD_LEFT_RIGH_SHADOW = "FIELD_LEFT_RIGH_SHADOW";
    private static final String FIELD_ONE = "FIELD_ONE";
    private static final String FIELD_OUTER_COLOR = "FIELD_OUTER_COLOR";
    private static final String FIELD_OUTER_SIZE = "FIELD_OUTER_SIZE";
    private static final String FIELD_THREE = "FIELD_THREE";
    private static final String FIELD_TOP_BOTTOM_SHADOW = "FIELD_TOP_BOTTOM_SHADOW";
    private static final String FIELD_TWO = "FIELD_TWO";
    private static final String FONT_NAME = "FONT_NAME";
    private static final String FRAME_NAME = "FRAME_NAME";
    private static final String HEIGHT = "HEIGHT";
    private static final String ORDER = "ORDER_";
    private static final String OVERLAY_BLUR = "OVERLAY_BLUR";
    private static final String OVERLAY_NAME = "OVERLAY_NAME";
    private static final String OVERLAY_OPACITY = "OVERLAY_OPACITY";
    private static final String POS_X = "POS_X";
    private static final String POS_Y = "POS_Y";
    private static final String PROFILE_TYPE = "PROFILE_TYPE";
    private static final String RATIO = "RATIO";
    private static final String RES_ID = "RES_ID";
    private static final String ROTATION = "ROTATION";
    private static final String SEEK_VALUE = "SEEK_VALUE";
    private static final String SHADOW_COLOR = "SHADOW_COLOR";
    private static final String SHADOW_PROG = "SHADOW_PROG";
    private static final String STC_COLOR = "STC_COLOR";
    private static final String STC_HUE = "STC_HUE";
    private static final String STC_OPACITY = "STC_OPACITY";
    private static final String STC_SCALE = "STC_SCALE";
    private static final String STKR_PATH = "STKR_PATH";
    private static final String TEMPLATES = "TEMPLATES";
    private static final String TEMPLATE_ID = "TEMPLATE_ID";
    private static final String TEMP_COLOR = "TEMP_COLOR";
    private static final String TEMP_PATH = "TEMP_PATH";
    private static final String TEXT = "TEXT";
    private static final String TEXT_ALPHA = "TEXT_ALPHA";
    private static final String TEXT_COLOR = "TEXT_COLOR";

    private static final String TEXT_INFO = "TEXT_INFO";
    private static final String THUMB_URI = "THUMB_URI";
    private static final String TYPE = "TYPE";
    private static final String WIDHT = "WIDHT";
    private static final String XROTATEPROG = "XROTATEPROG";
    private static final String YROTATEPROG = "YROTATEPROG";
    private static final String Y_ROTATION = "Y_ROTATION";
    private static final String ZROTATEPROG = "ZROTATEPROG";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static DatabaseHandler getDbHandler(Context context) {
        return new DatabaseHandler(context);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_TEMPLATES);
        sQLiteDatabase.execSQL(CREATE_TABLE_TEXT_INFO);
        sQLiteDatabase.execSQL(CREATE_TABLE_COMPONENT_INFO);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS TEMPLATES");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS TEXT_INFO");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS COMPONENT_INFO");
        onCreate(sQLiteDatabase);
    }

    public long insertTemplateRow(TemplateInfo templateInfo) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(THUMB_URI, templateInfo.getTHUMB_URI());
        contentValues.put(FRAME_NAME, templateInfo.getFRAME_NAME());
        contentValues.put(RATIO, templateInfo.getRATIO());
        contentValues.put(PROFILE_TYPE, templateInfo.getPROFILE_TYPE());
        contentValues.put(SEEK_VALUE, templateInfo.getSEEK_VALUE());
        contentValues.put(TYPE, templateInfo.getTYPE());
        contentValues.put(TEMP_PATH, templateInfo.getTEMP_PATH());
        contentValues.put(TEMP_COLOR, templateInfo.getTEMPCOLOR());
        contentValues.put(OVERLAY_NAME, templateInfo.getOVERLAY_NAME());
        contentValues.put(OVERLAY_OPACITY, templateInfo.getOVERLAY_OPACITY());
        contentValues.put(OVERLAY_BLUR, templateInfo.getOVERLAY_BLUR());
        long insert = writableDatabase.insert(TEMPLATES, null, contentValues);
        writableDatabase.close();
        return insert;
    }

    public void insertComponentInfoRow(ElementInfoPoster elementInfo) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEMPLATE_ID, elementInfo.getTEMPLATE_ID());
        contentValues.put(POS_X, elementInfo.getPOS_X());
        contentValues.put(POS_Y, elementInfo.getPOS_Y());
        contentValues.put(WIDHT, elementInfo.getWIDTH());
        contentValues.put(HEIGHT, elementInfo.getHEIGHT());
        contentValues.put(ROTATION, elementInfo.getROTATION());
        contentValues.put(Y_ROTATION, elementInfo.getY_ROTATION());
        contentValues.put(RES_ID, elementInfo.getRES_ID());
        contentValues.put(TYPE, elementInfo.getTYPE());
        contentValues.put(ORDER, elementInfo.getORDER());
        contentValues.put(STC_COLOR, elementInfo.getSTC_COLOR());
        contentValues.put(STC_OPACITY, elementInfo.getSTC_OPACITY());
        contentValues.put(XROTATEPROG, elementInfo.getXRotateProg());
        contentValues.put(YROTATEPROG, elementInfo.getYRotateProg());
        contentValues.put(ZROTATEPROG, elementInfo.getZRotateProg());
        contentValues.put(STC_SCALE, elementInfo.getScaleProg());
        contentValues.put(STKR_PATH, elementInfo.getSTKR_PATH());
        contentValues.put(COLORTYPE, elementInfo.getCOLORTYPE());
        contentValues.put(STC_HUE, elementInfo.getSTC_HUE());
        contentValues.put(FIELD_ONE, elementInfo.getFIELD_ONE());
        contentValues.put(FIELD_TWO, elementInfo.getFIELD_TWO());
        contentValues.put(FIELD_THREE, elementInfo.getFIELD_THREE());
        contentValues.put(FIELD_FOUR, elementInfo.getFIELD_FOUR());
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(writableDatabase.insert(COMPONENT_INFO, (String) null, contentValues));
        writableDatabase.close();
    }

    public void insertTextRow(TextInfo textInfo) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEMPLATE_ID, textInfo.getTEMPLATE_ID());
        contentValues.put(TEXT, textInfo.getTEXT());
        contentValues.put(FONT_NAME, textInfo.getFONT_NAME());
        contentValues.put(TEXT_COLOR, textInfo.getTEXT_COLOR());
        contentValues.put(TEXT_ALPHA, textInfo.getTEXT_ALPHA());
        contentValues.put(SHADOW_COLOR, textInfo.getSHADOW_COLOR());
        contentValues.put(SHADOW_PROG, textInfo.getSHADOW_PROG());
        contentValues.put(BG_DRAWABLE, textInfo.getBG_DRAWABLE());
        contentValues.put(BG_COLOR, textInfo.getBG_COLOR());
        contentValues.put(BG_ALPHA, textInfo.getBG_ALPHA());
        contentValues.put(POS_X, textInfo.getPOS_X());
        contentValues.put(POS_Y, textInfo.getPOS_Y());
        contentValues.put(WIDHT, textInfo.getWIDTH());
        contentValues.put(HEIGHT, textInfo.getHEIGHT());
        contentValues.put(ROTATION, textInfo.getROTATION());
        contentValues.put(TYPE, textInfo.getTYPE());
        contentValues.put(ORDER, textInfo.getORDER());
        contentValues.put(XROTATEPROG, textInfo.getXRotateProg());
        contentValues.put(YROTATEPROG, textInfo.getYRotateProg());
        contentValues.put(ZROTATEPROG, textInfo.getZRotateProg());
        contentValues.put(CURVEPROG, textInfo.getCurveRotateProg());
        contentValues.put(FIELD_ONE, textInfo.getFIELD_ONE());
        contentValues.put(FIELD_TWO, textInfo.getFIELD_TWO());
        contentValues.put(FIELD_THREE, textInfo.getFIELD_THREE());
        contentValues.put(FIELD_FOUR, textInfo.getFIELD_FOUR());
        contentValues.put(FIELD_FOUR, textInfo.getFIELD_FOUR());
        contentValues.put(FIELD_FOUR, textInfo.getFIELD_FOUR());
        contentValues.put(FIELD_FOUR, textInfo.getFIELD_FOUR());
        contentValues.put(FIELD_LEFT_RIGH_SHADOW, textInfo.getLeftRighShadow());
        contentValues.put(FIELD_TOP_BOTTOM_SHADOW, textInfo.getTopBottomShadow());
        contentValues.put(FIELD_OUTER_SIZE, textInfo.getOutLineSize());
        contentValues.put(FIELD_OUTER_COLOR, textInfo.getOutLineColor());

        writableDatabase.insert(TEXT_INFO, (String) null, contentValues);
        writableDatabase.close();
    }

    public ArrayList<TemplateInfo> getTemplateList(String str) {
        ArrayList<TemplateInfo> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT  * FROM TEMPLATES WHERE TYPE='" + str + "' ORDER BY " + TEMPLATE_ID + " ASC;", (String[]) null);
        if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
            readableDatabase.close();
            Log.e("templateList size is", "" + arrayList.size());
        } else {
            do {
                TemplateInfo templateInfo = new TemplateInfo();
                templateInfo.setTEMPLATE_ID(rawQuery.getInt(0));
                templateInfo.setTHUMB_URI(rawQuery.getString(1));
                templateInfo.setFRAME_NAME(rawQuery.getString(2));
                templateInfo.setRATIO(rawQuery.getString(3));
                templateInfo.setPROFILE_TYPE(rawQuery.getString(4));
                templateInfo.setSEEK_VALUE(rawQuery.getString(5));
                templateInfo.setTYPE(rawQuery.getString(6));
                templateInfo.setTEMP_PATH(rawQuery.getString(7));
                templateInfo.setTEMPCOLOR(rawQuery.getString(8));
                templateInfo.setOVERLAY_NAME(rawQuery.getString(9));
                templateInfo.setOVERLAY_OPACITY(rawQuery.getInt(10));
                templateInfo.setOVERLAY_BLUR(rawQuery.getInt(11));
                arrayList.add(templateInfo);
            } while (rawQuery.moveToNext());
            readableDatabase.close();
            Log.e("templateList size is", "" + arrayList.size());
        }
        return arrayList;
    }

    public ArrayList<TemplateInfo> getTemplateListDes(String str) {
        ArrayList<TemplateInfo> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT  * FROM TEMPLATES WHERE TYPE='" + str + "' ORDER BY " + TEMPLATE_ID + " DESC;", (String[]) null);
        if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
            readableDatabase.close();
            Log.e("templateList size is", "" + arrayList.size());
        } else {
            do {
                TemplateInfo templateInfo = new TemplateInfo();
                templateInfo.setTEMPLATE_ID(rawQuery.getInt(0));
                templateInfo.setTHUMB_URI(rawQuery.getString(1));
                templateInfo.setFRAME_NAME(rawQuery.getString(2));
                templateInfo.setRATIO(rawQuery.getString(3));
                templateInfo.setPROFILE_TYPE(rawQuery.getString(4));
                templateInfo.setSEEK_VALUE(rawQuery.getString(5));
                templateInfo.setTYPE(rawQuery.getString(6));
                templateInfo.setTEMP_PATH(rawQuery.getString(7));
                templateInfo.setTEMPCOLOR(rawQuery.getString(8));
                templateInfo.setOVERLAY_NAME(rawQuery.getString(9));
                templateInfo.setOVERLAY_OPACITY(rawQuery.getInt(10));
                templateInfo.setOVERLAY_BLUR(rawQuery.getInt(11));
                arrayList.add(templateInfo);
            } while (rawQuery.moveToNext());
            readableDatabase.close();
            Log.e("templateList size is", "" + arrayList.size());
        }
        return arrayList;
    }

    public ArrayList<TemplateInfo> getTemplateList() {
        ArrayList<TemplateInfo> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT  * FROM TEMPLATES ORDER BY TEMPLATE_ID DESC;", (String[]) null);
        if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
            readableDatabase.close();
        } else {
            do {
                TemplateInfo templateInfo = new TemplateInfo();
                templateInfo.setTEMPLATE_ID(rawQuery.getInt(0));
                templateInfo.setTHUMB_URI(rawQuery.getString(1));
                templateInfo.setFRAME_NAME(rawQuery.getString(2));
                templateInfo.setRATIO(rawQuery.getString(3));
                templateInfo.setPROFILE_TYPE(rawQuery.getString(4));
                templateInfo.setSEEK_VALUE(rawQuery.getString(5));
                templateInfo.setTYPE(rawQuery.getString(6));
                templateInfo.setTEMP_PATH(rawQuery.getString(7));
                templateInfo.setTEMPCOLOR(rawQuery.getString(8));
                templateInfo.setOVERLAY_NAME(rawQuery.getString(9));
                templateInfo.setOVERLAY_OPACITY(rawQuery.getInt(10));
                templateInfo.setOVERLAY_BLUR(rawQuery.getInt(11));
                arrayList.add(templateInfo);
            } while (rawQuery.moveToNext());
            readableDatabase.close();
        }
        return arrayList;
    }

    public ArrayList<ElementInfoPoster> getComponentInfoList(int i, String str) {
        ArrayList<ElementInfoPoster> arrayList = new ArrayList<>();
        String str2 = "SELECT * FROM COMPONENT_INFO WHERE TEMPLATE_ID='" + i + "' AND " + TYPE + " = '" + str + "'";
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery(str2, null);
        if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
            readableDatabase.close();
        } else {
            do {
                ElementInfoPoster elementInfo = new ElementInfoPoster();
                elementInfo.setCOMP_ID(rawQuery.getInt(0));
                elementInfo.setTEMPLATE_ID(rawQuery.getInt(1));
                elementInfo.setPOS_X(rawQuery.getFloat(2));
                elementInfo.setPOS_Y(rawQuery.getFloat(3));
                elementInfo.setWIDTH(rawQuery.getInt(4));
                elementInfo.setHEIGHT(rawQuery.getInt(5));
                elementInfo.setROTATION(rawQuery.getFloat(6));
                elementInfo.setY_ROTATION(rawQuery.getFloat(7));
                elementInfo.setRES_ID(rawQuery.getString(8));
                elementInfo.setTYPE(rawQuery.getString(9));
                elementInfo.setORDER(rawQuery.getInt(10));
                elementInfo.setSTC_COLOR(rawQuery.getInt(11));
                elementInfo.setSTC_OPACITY(rawQuery.getInt(12));
                elementInfo.setXRotateProg(rawQuery.getInt(13));
                elementInfo.setYRotateProg(rawQuery.getInt(14));
                elementInfo.setZRotateProg(rawQuery.getInt(15));
                elementInfo.setScaleProg(rawQuery.getInt(16));
                elementInfo.setSTKR_PATH(rawQuery.getString(17));
                elementInfo.setCOLORTYPE(rawQuery.getString(18));
                elementInfo.setSTC_HUE(rawQuery.getInt(19));
                elementInfo.setFIELD_ONE(rawQuery.getInt(20));
                elementInfo.setFIELD_TWO(rawQuery.getString(21));
                elementInfo.setFIELD_THREE(rawQuery.getString(22));
                elementInfo.setFIELD_FOUR(rawQuery.getString(23));
                arrayList.add(elementInfo);
            } while (rawQuery.moveToNext());
            readableDatabase.close();
        }
        return arrayList;
    }

    public ArrayList<TextInfo> getTextInfoList(int i) {
        ArrayList<TextInfo> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM TEXT_INFO WHERE TEMPLATE_ID='" + i + "'", (String[]) null);
        if (rawQuery == null || rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
            readableDatabase.close();
        } else {
            do {
                TextInfo textInfo = new TextInfo();
                textInfo.setTEXT_ID(rawQuery.getInt(0));
                textInfo.setTEMPLATE_ID(rawQuery.getInt(1));
                textInfo.setTEXT(rawQuery.getString(2));
                textInfo.setFONT_NAME(rawQuery.getString(3));
                textInfo.setTEXT_COLOR(rawQuery.getInt(4));
                textInfo.setTEXT_ALPHA(rawQuery.getInt(5));
                textInfo.setSHADOW_COLOR(rawQuery.getInt(6));
                textInfo.setSHADOW_PROG(rawQuery.getInt(7));
                textInfo.setBG_DRAWABLE(rawQuery.getString(8));
                textInfo.setBG_COLOR(rawQuery.getInt(9));
                textInfo.setBG_ALPHA(rawQuery.getInt(10));
                textInfo.setPOS_X(rawQuery.getFloat(11));
                textInfo.setPOS_Y(rawQuery.getFloat(12));
                textInfo.setWIDTH(rawQuery.getInt(13));
                textInfo.setHEIGHT(rawQuery.getInt(14));
                textInfo.setROTATION(rawQuery.getFloat(15));
                textInfo.setTYPE(rawQuery.getString(16));
                textInfo.setORDER(rawQuery.getInt(17));
                textInfo.setXRotateProg(rawQuery.getInt(18));
                textInfo.setYRotateProg(rawQuery.getInt(19));
                textInfo.setZRotateProg(rawQuery.getInt(20));
                textInfo.setCurveRotateProg(rawQuery.getInt(21));
                textInfo.setFIELD_ONE(rawQuery.getInt(22));
                textInfo.setFIELD_TWO(rawQuery.getString(23));
                textInfo.setFIELD_THREE(rawQuery.getString(24));
                textInfo.setFIELD_FOUR(rawQuery.getString(25));

                arrayList.add(textInfo);
            } while (rawQuery.moveToNext());
            readableDatabase.close();
        }
        return arrayList;
    }

    public boolean deleteTemplateInfo(int i) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            writableDatabase.execSQL("DELETE FROM TEMPLATES WHERE TEMPLATE_ID='" + i + "'");
            writableDatabase.execSQL("DELETE FROM COMPONENT_INFO WHERE TEMPLATE_ID='" + i + "'");
            writableDatabase.execSQL("DELETE FROM TEXT_INFO WHERE TEMPLATE_ID='" + i + "'");
            writableDatabase.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
