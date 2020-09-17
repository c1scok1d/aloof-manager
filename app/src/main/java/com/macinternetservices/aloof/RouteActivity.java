/*
 * Copyright 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.macinternetservices.aloof;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.macinternetservices.aloof.R;

public class RouteActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentManager = getSupportFragmentManager();//Get Fragment Manager


        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();

            Fragment argumentFragment = new RouteFragment();//Get Fragment Instance
            Bundle data = new Bundle();//Use bundle to pass data
            data.putLong("deviceId", bundle.getLong("deviceId"));//put string, int, etc in bundle with a key value
            data.putString("startTime", bundle.getString("startTime"));
            data.putString("endTime", bundle.getString("endTime"));
            argumentFragment.setArguments(data);//Finally
            //argumentFragment.setTargetFragment(DelDeviceFragment,);

            fragmentManager
                    .beginTransaction().replace(R.id.content_layout, argumentFragment)
                    .commit();
            /*getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_layout, new RouteFragment())
                    .commit(); */
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
