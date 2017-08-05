package alpha.gentwihan.com.alpha;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class AuthActivity extends AppCompatActivity
implements GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mGoogleApiClient;
    private SignInButton loginBtn;
    private FirebaseAuth mFirebaseAuth;
    private String TAG = "VideoActivity";
    private VideoView mVideoview;

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
        if(requestCode==100)
        {
            //Toast.makeText(AuthActivity.this,"잘 왔엉",Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            if(result.isSuccess())
            {
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
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        Task<AuthResult> authResultTask = mFirebaseAuth.signInWithCredential(credential);
        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = authResult.getUser();
                Toast.makeText(AuthActivity.this,firebaseUser.getEmail()+" \n 환영합니다",Toast.LENGTH_LONG ).show();
                startActivity(new Intent(AuthActivity.this,MainActivity.class));
                finish();

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(AuthActivity.this,"인증에 실패했습니다.",Toast.LENGTH_LONG).show();

    }
}
