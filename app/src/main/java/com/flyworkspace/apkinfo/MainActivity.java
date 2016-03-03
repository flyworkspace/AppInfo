package com.flyworkspace.apkinfo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(android.R.id.list);
        AppInfoAdapter adapter = new AppInfoAdapter(getAppList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = (AppInfo) parent.getAdapter().getItem(position);
                if (!TextUtils.equals(appInfo.getPackageName(), PublicFunction.getPackageName(MainActivity.this))) {
                    try {
                        startActivity(appInfo.getAppIntent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = (AppInfo) parent.getAdapter().getItem(position);
                PublicFunction.copy(appInfo.toString(), MainActivity.this);
                Toast.makeText(MainActivity.this, "copy success", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private class AppInfoAdapter extends BaseAdapter {
        private List<AppInfo> mList;
        private LayoutInflater mInflater;

        public AppInfoAdapter(List<AppInfo> mList) {
            this.mList = mList;
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.app_info_list_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AppInfo appInfo = mList.get(position);
            viewHolder.ivIcon.setImageDrawable(appInfo.getIcon());
            viewHolder.tvName.setText(appInfo.getAppName());
            viewHolder.tvPkgName.setText(appInfo.getPackageName());
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvPkgName;

        public ViewHolder(View view) {
            this.ivIcon = (ImageView) view.findViewById(R.id.imageview_icon);
            this.tvName = (TextView) view.findViewById(R.id.textview_name);
            this.tvPkgName = (TextView) view.findViewById(R.id.textview_pkgname);
        }
    }

    private List<AppInfo> getAppList() {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
                AppInfo info = new AppInfo();
                info.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
                info.setPackageName(packageInfo.packageName);
                info.setIcon(packageInfo.applicationInfo.loadIcon(pm));
                info.setAppIntent(pm.getLaunchIntentForPackage(packageInfo.packageName));
                appInfoList.add(info);
            } else {
                // System app
            }
        }
        return appInfoList;
    }
}
