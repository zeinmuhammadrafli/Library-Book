package id.co.visionet.bookinventory.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.visionet.bookinventory.MainActivity;
import id.co.visionet.bookinventory.R;
import id.co.visionet.bookinventory.core.CoreApplication;

public class SplashScreen extends Activity {
    Thread threadSplash;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtVersion)
    TextView txtVersion;
    private Runnable runnableTungguSplash = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("splash gagal sleep");
            }

            if (CoreApplication.getInstance().getSession().getKeyIsLoggedIn()) {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
            } else {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this,
                        LoginActivity.class));
            }
            finish();

            threadSplash = null;
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        animation();

        if (threadSplash == null) {
            threadSplash = new Thread(runnableTungguSplash);
            threadSplash.start();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void animation() {
        this.txtTitle.setAlpha(1.0F);
        Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        this.txtTitle.startAnimation(localAnimation);
    }
}
