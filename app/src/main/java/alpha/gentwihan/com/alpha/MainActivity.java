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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;

import alpha.gentwihan.com.alpha.utils.network.RetrofitClients;
import alpha.gentwihan.com.alpha.utils.permissions.PermissionUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private Button record, stop, play;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private boolean isRecorderInitialized = false;
    private String mCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        if(mFirebaseUser == null)
//        {
//            startActivity(new Intent(MainActivity.this, AuthActivity.class));
//            finish();
//            return;
//        }

        initView();
        PermissionUtils.chkAudioNWriteRecordPermission(this);
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
        String filename = "record";
        mCourseId = "1";
        initRecorder(filename);
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

        Call<okhttp3.ResponseBody> call = client.createRecord("Token 75e6c8ae42e71d54c441c8b03d65ad80681f57ad",
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