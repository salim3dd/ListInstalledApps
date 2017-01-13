package com.ma3ddom.listinstalledapps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllAppsActivity extends AppCompatActivity {
    ArrayList<ListApps> appsList = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager manager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent, 0);
//        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(manager));

        for (ResolveInfo info : resolveInfos) {
            ApplicationInfo all_App = info.activityInfo.applicationInfo;
            appsList.add(new ListApps(all_App.loadLabel(manager).toString(), all_App.processName, all_App.loadIcon(manager)));
        }


        ///ListView
        listView = (ListView) findViewById(R.id.list_app);
        ArrayAdaptor_App arrayAdaptor = new ArrayAdaptor_App(appsList);
        listView.setAdapter(arrayAdaptor);

        //listItemClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchApp(appsList.get(i).Package);
            }
        });
    }

    protected void launchApp(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            try {
                startActivity(mIntent);
            } catch (ActivityNotFoundException err) {
                Toast.makeText(this, "حدث خطأ لا يمكن فتح التطبيق", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class ListApps {
        String name;
        String Package;
        Drawable img;

        public ListApps(String name, String app_package, Drawable img) {
            this.name = name;
            Package = app_package;
            this.img = img;
        }

    }

    public class ArrayAdaptor_App extends BaseAdapter {
        ArrayList<ListApps> listapp = new ArrayList<>();

        public ArrayAdaptor_App(ArrayList<ListApps> listapp) {
            this.listapp = listapp;
        }

        @Override
        public int getCount() {
            return listapp.size();
        }

        @Override
        public Object getItem(int i) {
            return listapp.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(getApplicationContext(), R.layout.list_row, null);

            final TextView app_name = (TextView) v.findViewById(R.id.app_name);
            final TextView app_package = (TextView) v.findViewById(R.id.app_package);
            final ImageView app_icont = (ImageView) v.findViewById(R.id.app_icon);
            app_name.setText(listapp.get(i).name);
            app_package.setText(listapp.get(i).Package);
            app_icont.setImageDrawable(listapp.get(i).img);

            return v;
        }
    }
}

