package com.txbox.settings.adapter;

import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Text;

import com.txbox.txsdk.R;
import com.txbox.settings.adapter.AreaListAdapter.ViewCache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScreenPMFBAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<Map<String, String>> dataList = null;
	public ScreenPMFBAdapter(Context mContext,ArrayList<Map<String, String>> dataList){
		this.mContext = mContext;
		this.dataList = dataList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(dataList!=null&&dataList.size()>0){
			return dataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewCache viewCache;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.pmfb_list_item, null);
			viewCache = new ViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		TextView BigText = viewCache.getBigText();
		TextView littleTextView =  viewCache.getLittleText();
		Map<String, String> item = dataList.get(position);
		BigText.setText(item.get("Big"));
		littleTextView.setText(item.get("Little"));
		
		return convertView;
	}
	public class ViewCache {
		private View baseView;
		private TextView BigText;
		private TextView LittleText;

		public ViewCache(View baseView) {
			this.baseView = baseView;
		}

		public TextView getBigText() {
			if (BigText == null) {
				BigText = (TextView) baseView.findViewById(R.id.pmfb_big);
			}
			return BigText;
		}
		public TextView getLittleText() {
			if (LittleText == null) {
				LittleText = (TextView) baseView.findViewById(R.id.pmfb_little);
			}
			return LittleText;
		}
	}
}
