package alpha.gentwihan.com.alpha;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alpha.gentwihan.com.alpha.utils.encoding.Md5Encoder;
import alpha.gentwihan.com.alpha.utils.lessons.LessonUtils;
import alpha.gentwihan.com.alpha.utils.network.GsonUtils;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    TextView array_tb[][] = new TextView[5][11];
    ArrayList<JSONObject> lectures = new ArrayList<JSONObject>();
    int color_cnt = 0;
    String default_theme[] = {"#375e97", "#fb6542", "#ffbb00", "#3f681c", "#9a9eab", "#5d535e", "#ec96a4", "#dfe166", "#4cb5f5", "#34675c", "#fb6542", "#ffbb00", "#3f681c", "#9a9eab", "#5d535e", "#ec96a4", "#dfe166", "#4cb5f5", "#34675c", "#fb6542", "#ffbb00", "#3f681c", "#9a9eab", "#5d535e", "#ec96a4", "#dfe166", "#4cb5f5", "#34675c"};//시간표 색깔 조합
    JSONArray jarray;
    //String chunk -> sample data
    String chunk = "[{'id':1,'year':17,'term':'2R','courseCode':'KECE109','courseName':'공업수학1','day':0,'startTime':1,'endTime':1,'buildingName':'미래융합기술관','roomType':'강의실','roomName':'B101','profName':'도락주','created':'2017-08-05T11:29:58.541099Z','user':1}," +
            "{'id':2,'year':17,'term':'2R','courseCode':'KECE109','courseName':'공업수학1','day':2,'startTime':1,'endTime':1,'buildingName':'미래융합기술관','roomType':'강의실','roomName':'B101','profName':'도락주','created':'2017-08-05T11:29:58.541099Z','user':1}," +
            "{'id':3,'year':17,'term':'2R','courseCode':'BUSS205','courseName':'마케팅원리','day':0,'startTime':3,'endTime':4,'buildingName':'엘포관','roomType':'강의실','roomName':'B101','profName':'서준','created':'2017-08-05T11:29:58.541099Z','user':1}]";
    //mon: 1이다.. ,, 하지만 1교시는 1이다
    //chunk에다가 파싱한 String값을 넣어주면 된다!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        출처: http://deokho.tistory.com/28 [::NOTE::]
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null)
        {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            return;
        }
        InputData();
        ShowData();
    }

    void InputData() {
        try
        {
            String tmp=GsonUtils.getGson().toJson(LessonUtils.getInstance().getLessons());
            Log.d("test",tmp);
            //obj_temp.put("courseCode","A");
            jarray = new JSONArray(tmp);

            for(int i = 0 ; i < jarray.length() ; i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                lectures.add(jObject);
                color_cnt++;
                lectures.get(i).put("bg_color", default_theme[0]);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    void ShowData() {

        for(int i = 0 ; i < 5 ; i++) {
            for(int j = 1 ; j <= 10 ; j++) {
                String tb_id = "tb_" + i + "_" + j;
                int resID = getResources().getIdentifier(tb_id, "id", getPackageName());
                array_tb[i][j] = (TextView)findViewById(resID);
            }
        }
        try {
            for(int i = 0 ; i < lectures.size() ; i++) {
                JSONObject jObject1 = lectures.get(i);
                for(int j = jObject1.getInt("startTime") ; j <= jObject1.getInt("endTime") ; j++) {
                    array_tb[jObject1.getInt("day")][j].setText(jObject1.getString("courseName"));
                    array_tb[jObject1.getInt("day")][j].setBackgroundColor(Color.parseColor(jObject1.getString("bg_color")));
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }
}



/*
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null)
        {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            return;
        }

    }
}*/