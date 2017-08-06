package alpha.gentwihan.com.alpha;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.io.File;
import java.io.IOException;

import alpha.gentwihan.com.alpha.utils.network.RetrofitClients;
import alpha.gentwihan.com.alpha.utils.permissions.PermissionUtils;
import alpha.gentwihan.com.alpha.utils.token.TokenUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import alpha.gentwihan.com.alpha.utils.encoding.Md5Encoder;
import alpha.gentwihan.com.alpha.utils.lessons.LessonUtils;
import alpha.gentwihan.com.alpha.utils.network.GsonUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button record, stop, play;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private boolean isRecorderInitialized = false;
    private String mCourseId;
  
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    TextView array_tb[][] = new TextView[5][11];
    ArrayList<JSONObject> lectures = new ArrayList<JSONObject>();
    HashMap<String, String> bg_color = new HashMap();
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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null)
        {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            return;
        }
      
        //initView();//심각한 오류 내포하고 있음
        InputData();
        ShowData();
        PermissionUtils.chkAudioNWriteRecordPermission(this);
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
                if(bg_color.containsKey(jObject.get("courseCode")) == false) {
                    Log.d("bg_color", bg_color.toString());
                    bg_color.put(jObject.get("courseCode").toString(), default_theme[color_cnt]);
                    color_cnt++;
                }
                //color_cnt++;
               // lectures.get(i).put("bg_color", default_theme[0]);
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
                    array_tb[jObject1.getInt("day") - 1][j].setText(jObject1.getString("courseName"));
                    array_tb[jObject1.getInt("day") - 1][j].setBackgroundColor(Color.parseColor(bg_color.get(jObject1.get("courseCode"))));
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }


    void initView() {

        play = (Button) findViewById(R.id.btn_play);
        stop = (Button) findViewById(R.id.btn_stop);
        record = (Button) findViewById(R.id.btn_record);

        stop.setEnabled(false);
        play.setEnabled(false);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRecordClicked();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStopClicked();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlayClicked();
            }
        });
    }

    private void btnRecordClicked() {
//        if (!isRecorderInitialized) {
        String filename = "record";//파일명 계속 변경핼
        mCourseId = "1";
        initRecorder(generateFilename());
//            isRecorderInitialized = true;
//        }

        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }
        catch (IllegalStateException ise) {

        }
        catch (IOException ioe) {

        }

        record.setEnabled(false);
        stop.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG);
    }

    private String generateFilename() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd__hhmmss");
        Log.d(TAG, "generateFilename: " + sdf.toString());

        return null;
    }

    private void btnPlayClicked() {
        MediaPlayer mediaplayer = new MediaPlayer();

        try {
            mediaplayer.setDataSource(outputFile);
            mediaplayer.prepare();
            mediaplayer.start();

            Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG);
        }
        catch (Exception e) {

        }
    }

    private void btnStopClicked() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        record.setEnabled(true);
        stop.setEnabled(false);
        play.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Audio Recorded Successfully", Toast.LENGTH_LONG);

        File outputFileAsFile = new File(outputFile);

        createRecord(outputFileAsFile);
//        convertFile(outputFileAsFile, new Consumer<File>() {
//            @Override
//            public void accept(File file) {
//                Log.d(TAG, "accept: convertFile finished");
//                createRecord(file);
//            }
//        });
    }

    private void createRecord(File file) {
        Log.d(TAG, "createRecord: ");
        String filename = file.getName();
        MultipartBody.Part body = null;

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("audio/x-m4a"),
                        file
                );

//        body = RequestBody.create(okhttp3.MultipartBody.FORM, outputFileAsFile.getName(), requestFile);

        RequestBody courseIdAsRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, mCourseId);
        RequestBody filenameAsRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, filename);

        body = MultipartBody.Part.createFormData("voice", file.getName(), requestFile);

        ApiClient client = RetrofitClients.getInstance()
                .getService(ApiClient.class);

        Call<okhttp3.ResponseBody> call = client.createRecord("Token " + TokenUtils.getInstance().getToken(),
//                "title",
                courseIdAsRequestBody,
                filenameAsRequestBody,
                body
        );

        call.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                Log.d(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private void initRecorder(String filename) {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename + ".3gp";
        Log.d(TAG, "onCreate: outputFilepath=" + outputFile);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }
}


