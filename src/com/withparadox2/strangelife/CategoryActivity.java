package com.withparadox2.strangelife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.withparadox2.strangelife.custom.CustomViewGroup;
import com.withparadox2.strangelife.dao.CategoryHelper;

public class CategoryActivity extends Activity{
	
    private CategoryHelper mCHelper;
    private List<String> motherItems = new ArrayList<String>();
    private List<Long> motherIds = new ArrayList<Long>();
    private Map<Long, List<String>> map = new HashMap<Long, List<String>>();
    private Map<Long, List<String>> mapId = new HashMap<Long, List<String>>();
    private ListView myListView;
    private MyAdapter myAdapter;
    private Button addMotherItemButton;
    
    private final static int ADD_MOTHER_DIALOG = 1;
    private final static int ADD_CHILD_DIALOG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		mCHelper = new CategoryHelper(this);
		mCHelper.open();
		getAllMotherCategory();
		myListView = (ListView) findViewById(R.id.category_listview);
		myAdapter = new MyAdapter(this, R.layout.category_item);
		myListView.setAdapter(myAdapter);
		addMotherItemButton = (Button) findViewById(R.id.add_mother_item_button);
		addMotherItemButton.setOnClickListener(new AddMotherItemClickListener());
	}
	

	private void getAllMotherCategory(){
		
		Cursor c = mCHelper.fetchAllMotherCategoryItem();
		Cursor cc;
		String motherItem;
		long motherId;
        List<String> childrenItems;
        List<Long> childrenIds;
		while(c.moveToNext()){
			motherItem = c.getString(c.getColumnIndexOrThrow(CategoryHelper.KEY_CATEGOTY));
			motherItems.add(motherItem);
			motherId = c.getLong(c.getColumnIndexOrThrow(CategoryHelper.KEY_ID));
			motherIds.add(motherId);
			cc = mCHelper.fetchAllChildrenCategoryItem(motherId);
			childrenItems = new ArrayList<String>();
			childrenIds = new ArrayList<Long>();
			while(cc.moveToNext()){
				childrenItems.add(cc.getString(cc.getColumnIndex(CategoryHelper.KEY_CATEGOTY)));
				childrenIds.add(cc.getLong(cc.getColumnIndex(CategoryHelper.KEY_ID)));
			}
			map.put(motherId, childrenItems);
		}
		c.close();
	}
	
	public class MyAdapter extends BaseAdapter{
		int id_row_layout;
		LayoutInflater mInflater;
		public MyAdapter(Context context, int id_row_layout) {
			super();
			this.id_row_layout = id_row_layout;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return motherIds.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentView) {
			ViewHolder holder = null;
			if (convertView == null) {
				synchronized (CategoryActivity.this) {
					convertView = mInflater.inflate(id_row_layout, parentView, false);
					holder = new ViewHolder();
					
					holder.firstButton = (TextView) convertView
							.findViewById(R.id.first_category_text);
					holder.customViewGroup = (CustomViewGroup) convertView
							.findViewById(R.id.second_viewgroup);
					convertView.setTag(holder);
				}
			} else {
				holder = (ViewHolder) convertView.getTag();   
			}
			
			holder.firstButton.setText(motherItems.get(position));
			List<String> itemList = map.get(motherIds.get(position));
			Button myButton;
			holder.customViewGroup.removeAllViews();
			for(String childrenItem : itemList){
				myButton = new Button(CategoryActivity.this);
				myButton.setTag(motherItems.get(position)+"|"+childrenItem);
				myButton.setText(childrenItem);
				myButton.setBackgroundResource(R.drawable.login_button_bg);
				myButton.setOnClickListener(new ChoseClickListener());
				myButton.setOnLongClickListener(new ChoseLongClickListener());
				holder.customViewGroup.addView(myButton, new LayoutParams(LayoutParams.WRAP_CONTENT, 20));
			}
			myButton = new Button(CategoryActivity.this);
			myButton.setText("+");
			myButton.setTag(motherIds.get(position));
			myButton.setBackgroundResource(R.drawable.login_button_bg);
			myButton.setOnClickListener(new AddChildItemClickListener());
			holder.customViewGroup.addView(myButton);
	

			return convertView;
		}

		class ViewHolder {
			TextView firstButton;
			CustomViewGroup customViewGroup;
		}
		
		class ChoseClickListener implements View.OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(CategoryActivity.this, (CharSequence) v.getTag(), Toast.LENGTH_LONG).show();
				Intent i = new Intent(CategoryActivity.this, WorkOfToday.class);
				i.putExtra("category", (CharSequence) v.getTag());
				setResult(RESULT_OK, i);
				finish();
			}
			
		}
		
		class ChoseLongClickListener implements View.OnLongClickListener{

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				return false;
			}
			
		}
		
	}
	
	
	class AddMotherItemClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showAddMotherItemDialog();
		}
		
	}
	
	
	class AddChildItemClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showAddChildItemDialog((Long)v.getTag());
		}
		
	}
	
	private void showDialog(String title, final int motherOrChild, final long motherId){
		final EditText editText = new EditText(this);
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title).
			setView(editText).
			setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String text = editText.getText().toString();
				if(!text.equals("")){
					if(motherOrChild == ADD_MOTHER_DIALOG){
						mCHelper.creatMotherCategoryItem(text);
					}else{
						mCHelper.creatChildrenCategoryItem(text, motherId);
					}
					dialog.dismiss();
					refreshListView();
				}else{
					Toast.makeText(CategoryActivity.this, "输入无效啊亲!", Toast.LENGTH_LONG).show();
					dialog.dismiss();
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).create().show();
	}
	
	private void showAddMotherItemDialog(){
		 showDialog("添加母类别", ADD_MOTHER_DIALOG, 0);
	}
	
	private void showAddChildItemDialog(long motherId){
		 showDialog("添加子类别", ADD_CHILD_DIALOG, motherId);
	}

	private void refreshListView(){
		motherIds.clear();
		motherItems.clear();
		map.clear();
		getAllMotherCategory();
		myAdapter.notifyDataSetChanged();
	}
}
