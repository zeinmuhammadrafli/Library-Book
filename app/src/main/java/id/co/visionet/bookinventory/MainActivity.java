package id.co.visionet.bookinventory;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.visionet.bookinventory.core.CoreApplication;
import id.co.visionet.bookinventory.fragment.AboutFragment;
import id.co.visionet.bookinventory.fragment.HomeFragment;
import id.co.visionet.bookinventory.helper.SessionManagement;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    TextView txtUserName;
    private ActionBarDrawerToggle mDrawerToggle;
    private HashMap<String, String> activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        activeUser = CoreApplication.getInstance().getSession().getActiveInformation();
        String email = activeUser.get(SessionManagement.KEY_EMAIL);

        if (!email.equals("")) {
            View header = navDrawer.getHeaderView(0);
            txtUserName = (TextView) header.findViewById(R.id.username);
            txtUserName.setText(email);
        }

         /* setting drawer toggle yang menangani ketika drawer terbuka, tertutup
            dan ketika sedang di-slide
         */
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // checking status item dalam menu apakah terpilih atau tidak.
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                //menutup drawer saat item dipilih
                mDrawerLayout.closeDrawers();

                //menampilkan fragment yang bersesuaian
                displayView(item.getItemId());
                return true;
            }
        });
        //menampilkan menu pertama secara default
        displayView(R.id.homeCatalog);

    }

    private void displayView(int position) {
        //proses pemilihan fragment sesuai item yang diklik
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case R.id.homeCatalog:
                fragment = new HomeFragment();
                title = "Books Catalog";
                break;
            case R.id.about:
                fragment = new AboutFragment();
                title = "About";
                break;
            case R.id.logout:
                CoreApplication.getInstance().logout();
            default:
                break;
        }

        // melakukan pemasangan fragment ke dalam frame layout secara programmatically
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // implementasi search view bukan lagi di main activity, tetapi di fragment
                return false;
            case R.id.action_refresh:
                // implementasi refresh di fragment
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        CoreApplication.getInstance().showQuitConfirmation(this);
    }
}
