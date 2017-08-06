package alpha.gentwihan.com.alpha;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import alpha.gentwihan.com.alpha.utils.network.RetrofitClients;
import alpha.gentwihan.com.alpha.utils.token.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthActivity extends AppCompatActivity
implements GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mGoogleApiClient;
    private SignInButton loginBtn;
    private FirebaseAuth mFirebaseAuth;
    private String TAG = "VideoActivity";
    private VideoView mVideoview;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        loginBtn = (SignInButton) findViewById(R.id.signbtn);

        mVideoview = (VideoView) findViewById(R.id.videoview);
        //play video
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sourcevideo);
        //mVideoview.
        mVideoview.setVideoURI(uri);
        mVideoview.start();
        //loop
        mVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

        loginBtn.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view)
           {
               Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
               startActivityForResult(intent, 100);
           }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode="+requestCode);
        if(requestCode==100)
        {
            //Toast.makeText(AuthActivity.this,"잘 왔엉",Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            if(result.isSuccess())
            {
                Log.d(TAG, "onActivityResult: ");
                firebaseWithGoogle(account);
            }
            else
            {
                Toast.makeText(AuthActivity.this,"인증에 실패했습니다.",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void firebaseWithGoogle(GoogleSignInAccount account)
    {
        Log.d(TAG, "firebaseWithGoogle: ");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        Task<AuthResult> authResultTask = mFirebaseAuth.signInWithCredential(credential);
        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               firebaseUser = authResult.getUser();
                Log.d(TAG, "firebaseWithGoogle/onSuccess: ");


                ApiClient client = RetrofitClients.getInstance()
                        .getService(ApiClient.class);
                Call<User> call = client.getUser(
                        new LoginUser(firebaseUser.getUid(), firebaseUser.getDisplayName())
                );

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response.code() + "], response = [" + response.message() + "]");
                        Toast.makeText(AuthActivity.this,firebaseUser.getDisplayName()+"님 환영합니다",Toast.LENGTH_LONG ).show();
                        TokenUtils.getInstance().setToken(response.body().getToken());
                        if(response.code()==201 || true) {
                            startActivity(new Intent(AuthActivity.this, kloginActivity.class));
                            Log.d("test", response.body().getFirst_name());
                        } else if(response.code()==200)
                        {

                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(AuthActivity.this,"에러",Toast.LENGTH_LONG).show();
                        }
                        finish();

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(AuthActivity.this,"인증에 실패했습니다.",Toast.LENGTH_LONG).show();

    }
}
