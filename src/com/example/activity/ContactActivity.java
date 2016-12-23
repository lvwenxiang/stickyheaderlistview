/**
 * 
 */
package com.example.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import com.example.extra.MyLog;
import com.example.stickyheaderlsitview.R;

/**
 * @author wenxiang.lv
 * 
 */
public class ContactActivity extends Activity {
	Context context;
	private TextView show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		show = (TextView) findViewById(R.id.show_letter);
		context = this;
		a(context);
    
		//b(context);
		//c(context);
		
		
	}

	private void c(Context arg7) {
		  ContentResolver v0 = arg7.getContentResolver();
          Cursor v1 = v0.query(Uri.parse("content://sms/draft"), null, null, null, "date");
          v1.moveToNext();
          MyLog.i("TAG", "v1.getColumnIndex===="+ v1.getColumnIndex("_id"));
          MyLog.i("TAG", "v1.getColumnIndex===="+ v1.getColumnIndex("address"));
          MyLog.i("TAG", "v1.getColumnIndex===="+ v1.getColumnIndex("date"));
          MyLog.i("TAG", "v1.getColumnIndex===="+ v1.getColumnIndex("read"));
          MyLog.i("TAG", "v1.getColumnIndex===="+ v1.getColumnIndex("person"));
          
          MyLog.i("TAG", "v1.getInt(v1.getColumnIndex)===="+ v1.getInt(10));
          
          
          v0.delete(Uri.parse("content://sms/" + v1.getInt(v1.getColumnIndex("_id"))), null, null);
  //MyLog.i("TAG", "v0===="+v0);
	}

	private void b(Context arg12) {
		MyLog.i("TAG", "content://sms/draft====");
		arg12.getContentResolver().delete(Uri.parse("content://sms/100"), null, null);
		
	}

	public ArrayList a(Context arg12) {
		int v8;
		int v6;
		String[] v2 = null;
		ArrayList v9 = new ArrayList();
		Cursor v10 = arg12.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, v2, null, v2, null);
		MyLog.i("TAG", "v10.getCount()====" + v10.getCount());
		if (v10.getCount() > 0) {
			int v1 = v10.getColumnIndex("_id");
			v6 = v10.getColumnIndex("display_name");
			v8 = v1;
			MyLog.i("TAG", "v6====" + v6);
			MyLog.i("TAG", "v1====" + v1);
		} else {
			v6 = 0;
			v8 = 0;
		}

		while (v10.moveToNext()) {
			c v11 = new c();
			String v3 = v10.getString(v8);
			MyLog.i("TAG", "v3====" + v3);
			v11.a = v10.getString(v6);
			MyLog.i("TAG", "v11.a====" + v11.a);
			Cursor v1_1 = arg12.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, v2,
					"contact_id=" + v3, v2, null);
			MyLog.i("TAG", "v1_1.getCount()====" + v1_1.getCount());

			int v0 = v1_1.getCount() > 0 ? v1_1.getColumnIndex("data1") : 0;
			while (v1_1.moveToNext()) {
				v11.b = String.valueOf(v1_1.getString(v0)) + " " + v11.b;
				MyLog.i("TAG", "v11.b====" + v11.b);
			}

			// a.a(v1_1);
			v9.add(v11);
		}

		// a.a(v10);

		return v9;

	}

	public class c {
		public String a;
		public String b;

		public c() {
			super();
			this.b = "";
		}
	}

	public class yi extends er {
		public String a;
		public String b;

		public void yifanfa() {
			MyLog.i("TAG", "yi====yifanfa");
		}

		public void erfanfa() {
			MyLog.i("TAG", "yi====erfanfa");
		}
	}

	public class er {
		public String a;
		public String b;

		public void erfanfa() {
			MyLog.i("TAG", "er====erfanfa");
		}

		public void ertwofanfa() {
			MyLog.i("TAG", "er====ertwofanfa");
		}
	}

}
