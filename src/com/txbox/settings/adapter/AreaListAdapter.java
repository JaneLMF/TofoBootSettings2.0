package com.txbox.settings.adapter;

import java.util.List;
import java.util.Map;

import com.txbox.txsdk.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AreaListAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mLData;
	private int curPosition;

	public AreaListAdapter(Context context, List<String> mlist) {
		mContext = context;
		mLData = mlist;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewCache viewCache;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.common_area_list_item, null);
			viewCache = new ViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		String item = mLData.get(position % mLData.size());

		TextView TitleTextView = viewCache.getTitleTextView();
		TitleTextView.setText(item);
		if (curPosition == position) {
			TitleTextView.setTextSize(35);
			TitleTextView.setTextColor(Color.parseColor("#FFFFFF"));
			TitleTextView.setAlpha(1f);
		} else {
			TitleTextView.setTextSize(30);
			TitleTextView.setTextColor(Color.parseColor("#828282"));
			TitleTextView.setAlpha(0.6f);
		}
		
		return convertView;
	}

	
	public void setPosition(int p){
		curPosition = p;
		notifyDataSetChanged();
	}
	public int getCount() {
//		return mLData.size();
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		position = position % mLData.size();
		if (mLData != null && mLData.size() > position) {
			return mLData.get(position);
		} else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public class ViewCache {
		private View baseView;
		private TextView TitleTextView;

		public ViewCache(View baseView) {
			this.baseView = baseView;
		}

		public TextView getTitleTextView() {
			if (TitleTextView == null) {
				TitleTextView = (TextView) baseView.findViewById(R.id.areaText);
			}
			return TitleTextView;
		}

	}
}
