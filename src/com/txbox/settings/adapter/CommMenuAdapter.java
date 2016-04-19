package com.txbox.settings.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.txbox.txsdk.R;
import com.txbox.settings.adapter.AreaListAdapter.ViewCache;
import com.txbox.settings.utils.DensityUtil;
import com.txbox.settings.utils.ScaleAnimEffect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommMenuAdapter extends BaseAdapter{

	private  ArrayList<Map<String, String>> Data;
	private Context mContext;
	private int curPosition = 0;
	
	private TextView commText;
	private TextView commState;
	private ImageView commleftIcon,commRightIcon;
	private LinearLayout comm_ll;
	private ScaleAnimEffect animEffect;
	private float scaleX = 1.105f;
	private float scaleY = 1.105f;
	public CommMenuAdapter(Context mContext,ArrayList<Map<String, String>> Data){
		this.mContext = mContext;
		this.Data = Data;
		animEffect = new ScaleAnimEffect();
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		ViewCache viewCache;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.common_list_iem, null);
			viewCache = new ViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		
		/*comm_ll = viewCache.getItemView();
		commText = viewCache.getTitleTextView();
		commState = viewCache.getStateTextView();
		commleftIcon = viewCache.getleftIcon();
		commRightIcon = viewCache.getrightIcon();
		
		if(pos == curPosition){
//			startAnimation(comm_ll, commText, commState,commleftIcon,commRightIcon);
			commText.setAlpha(1.0f);
			commText.setTextSize(34);
			commState.setTextColor(mContext.getResources().getColor(R.color.common_color_blue));
		}else{
//			looseAnimation(comm_ll, commText, commState,commleftIcon,commRightIcon);
			commText.setAlpha(0.5f);
			commText.setTextSize(28);
			commState.setTextColor(mContext.getResources().getColor(R.color.common_color_grey));
		}*/
		
		
		
		if(pos == curPosition){
			viewCache.getSelectedrl().setVisibility(View.VISIBLE);
			viewCache.getrl().setVisibility(View.GONE);
			commText = viewCache.getTitleSelectedTextView();
			commState = viewCache.getStateSelectedTextView();
			commleftIcon = viewCache.getSelectedleftIcon();
			commRightIcon = viewCache.getSelectedrightIcon();
		}else{
			viewCache.getrl().setVisibility(View.VISIBLE);
			viewCache.getSelectedrl().setVisibility(View.GONE);
			commText = viewCache.getTitleTextView();
			commState = viewCache.getStateTextView();
			commleftIcon = viewCache.getleftIcon();
			commRightIcon = viewCache.getrightIcon();
		}
		Map<String, String> map = Data.get(pos);
		commText.setText(map.get("text"));
		commState.setText(map.get("state"));
		if(pos == 3){
			commleftIcon.setVisibility(View.INVISIBLE);
			commRightIcon.setVisibility(View.INVISIBLE);
		}else{
			commleftIcon.setVisibility(View.VISIBLE);
			commRightIcon.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	public class ViewCache {
		private View baseView;
		private RelativeLayout rl;
		private TextView text;
		private TextView text_state;
		private ImageView pmbh_right,pmbh_left;
		
		private RelativeLayout selected_rl;
		private TextView text_selected;
		private TextView text_selected_state;
		private ImageView pmbh_selected_right ,pmbh_selected_left;
		
		private LinearLayout item_ll;
		public ViewCache(View baseView) {
			this.baseView = baseView;
		}

		public LinearLayout getItemView() {
			if (item_ll == null) {
				item_ll =  (LinearLayout) baseView.findViewById(R.id.item_ll);
			}
			return item_ll;
		}
		public TextView getTitleTextView() {
			if (text == null) {
				text = (TextView) baseView.findViewById(R.id.text);
			}
			return text;
		}
		public TextView getStateTextView() {
			if (text_state == null) {
				text_state = (TextView) baseView.findViewById(R.id.text_state);
			}
			return text_state;
		}
		public RelativeLayout getSelectedrl(){
			if(selected_rl==null){
				selected_rl = (RelativeLayout) baseView.findViewById(R.id.selected_ll);
			}
			return selected_rl;
		}
		
		public TextView getTitleSelectedTextView() {
			if (text_selected == null) {
				text_selected = (TextView) baseView.findViewById(R.id.text_selected);
			}
			return text_selected;
		}
		public TextView getStateSelectedTextView() {
			if (text_selected_state == null) {
				text_selected_state = (TextView) baseView.findViewById(R.id.text_selected_state);
			}
			return text_selected_state;
		}
		public RelativeLayout getrl(){
			if(rl==null){
				rl = (RelativeLayout) baseView.findViewById(R.id.ll);
			}
			return rl;
		}
		public ImageView getleftIcon(){
			if (pmbh_left == null) {
				pmbh_left = (ImageView) baseView.findViewById(R.id.pmbh_left);
			}
			return pmbh_left;
		}
		public ImageView getrightIcon(){
			if (pmbh_right == null) {
				pmbh_right = (ImageView) baseView.findViewById(R.id.pmbh_right);
			}
			return pmbh_right;
		}
		public ImageView getSelectedrightIcon(){
			if (pmbh_selected_right == null) {
				pmbh_selected_right = (ImageView) baseView.findViewById(R.id.pmbh_selected_right);
			}
			return pmbh_selected_right;
		}
		public ImageView getSelectedleftIcon(){
			if (pmbh_selected_left == null) {
				pmbh_selected_left = (ImageView) baseView.findViewById(R.id.pmbh_selected_left);
			}
			return pmbh_selected_left;
		}

	}

	public void setPosition(int pos){
		this.curPosition = pos;
		notifyDataSetChanged();
	}
	
	private void startAnimation(final View v,final View title,final View state,final View left,final View right){
		this.animEffect.setAttributs(1.0F, scaleX, 1.0F, scaleY, 100L);
		Animation localAnimation = this.animEffect.createAnimation();
		localAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				title.setAlpha(1.0f);
				((TextView)state).setTextColor(mContext.getResources().getColor(R.color.common_color_blue));
				
			}
		});
		title.startAnimation(localAnimation);
		left.startAnimation(localAnimation);
		right.startAnimation(localAnimation);
	}
	private void looseAnimation(final View v,final View title,final View state,final View left,final View right){
		this.animEffect.setAttributs(1.0f, 1.0F,1.0f, 1.0F, 100L);
		Animation localAnimation = this.animEffect.createAnimation();
		localAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				title.setAlpha(0.5f);
				((TextView)state).setTextColor(mContext.getResources().getColor(R.color.common_color_grey));
			}
		});
		title.startAnimation(localAnimation);
		left.startAnimation(localAnimation);
		right.startAnimation(localAnimation);
	}
	
}
