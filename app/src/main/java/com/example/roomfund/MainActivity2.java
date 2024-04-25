package com.example.roomfund;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.roomfund.fragment.home;
import com.example.roomfund.fragment.myProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    public static final int MY_REQUEST_CODE = 10;
    private static final int FRAGMENT_HOME=0;
    private static final int FRAGMENT_HISTORY=1;
    private static final int FRAGMENT_MY_PROFILE=2;
    private int mCurrentFragment = FRAGMENT_HOME;
    private BottomNavigationView navigationView;
    private ViewPager mViewPager;
    final private myProfile mMyProfile= new myProfile();
    final private ActivityResultLauncher<Intent> mActivityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK) {
                        Intent intent= result.getData();
                        if (intent == null) {
                            return;
                        }
                        Uri uri=intent.getData();
                        mMyProfile.setUri(uri);
                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            mMyProfile.setBitmapImgView(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        navigationView=findViewById(R.id.bottom_bar);
        mViewPager=findViewById(R.id.viewpager);

        setUpViewpager();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.action_home) {
                    mViewPager.setCurrentItem(0);
                } else if(id==R.id.action_history) {
                    mViewPager.setCurrentItem(1);
                } else {
                    mViewPager.setCurrentItem(2);
                }
                return true;
            }
        });
    }

    private void setUpViewpager() {
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.action_history).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.action_myProfile).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void openGallery(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture" ));
    }
}