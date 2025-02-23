package com.example.labandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userId = getIntent().getStringExtra("userId");

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, userId);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("My Books");
                    break;
                case 1:
                    tab.setText("Available Books");
                    break;
                case 2:
                    tab.setText("Borrowed Books");
                    break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                updateTabContent(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                updateTabContent(position);
            }
        });
    }

    private void updateTabContent(int position) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + position);
        if (fragment != null) {
            if (fragment instanceof Tab1Fragment) {
                ((Tab1Fragment) fragment).updateContent();
            } else if (fragment instanceof Tab2Fragment) {
                ((Tab2Fragment) fragment).updateContent();
            } else if (fragment instanceof Tab3Fragment) {
                ((Tab3Fragment) fragment).updateContent();
            }
        }
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private String userId;

        public ViewPagerAdapter(FragmentActivity fa, String userId) {
            super(fa);
            this.userId = userId;
        }

        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                Tab1Fragment tab1Fragment = new Tab1Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                tab1Fragment.setArguments(bundle);
                return tab1Fragment;
            } else if (position == 1) {
                Tab2Fragment tab2Fragment = new Tab2Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                tab2Fragment.setArguments(bundle);
                return tab2Fragment;
            } else {
                Tab3Fragment tab3Fragment = new Tab3Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                tab3Fragment.setArguments(bundle);
                return tab3Fragment;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}