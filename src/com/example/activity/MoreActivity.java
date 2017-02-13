package com.example.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stickyheaderlsitview.R;

public class MoreActivity extends BaseActivity{

	
    private static final DemoInfo[] DEMOS = {
    	new DemoInfo(R.string.moreactivity_listname1, R.string.moreactivity_listname1desc, AddressActivity.class),
    	new DemoInfo(R.string.moreactivity_listname2, R.string.moreactivity_listname2desc, ContactActivity.class),
    	new DemoInfo(R.string.moreactivity_listname3, R.string.moreactivity_listname3desc, FontgradientcolorActivity.class),
    	new DemoInfo(R.string.moreactivity_listname4, R.string.moreactivity_listname4desc, EleActivity.class),
    	new DemoInfo(R.string.moreactivity_listname5, R.string.moreactivity_listname5desc, ClipHeadportraitActivity.class),
    	new DemoInfo(R.string.moreactivity_listname6, R.string.moreactivity_listname6desc, LightkeyActivity.class)};
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		ListView mListView = (ListView) findViewById(R.id.listView);
		mListView.setAdapter(new MoreDemoListAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick(position);
			}
		});
	}
	
	
	
    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(MoreActivity.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }
	
    private static class DemoInfo {
        private final int title;
        private final int desc;
        private final Class<? extends Activity> demoClass;

        public DemoInfo(int title, int desc,
                Class<? extends Activity> demoClass) {
            this.title = title;
            this.desc = desc;
            this.demoClass = demoClass;
        }
    }
    
    public class MoreDemoListAdapter extends BaseAdapter {

    	@Override
    	public int getCount() {
    		  return DEMOS.length;
    	}

    	@Override
    	public Object getItem(int position) {
    		 return DEMOS[position];
    	}

    	@Override
    	public long getItemId(int position) {
    		return position;
    	}

    	@SuppressLint("ViewHolder")
		@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MoreActivity.this).inflate(R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            title.setText(DEMOS[position].title);
            desc.setText(DEMOS[position].desc);
            if (position >= 25) {
                title.setTextColor(Color.YELLOW);
            }
            return convertView;
    	}

    }
}
