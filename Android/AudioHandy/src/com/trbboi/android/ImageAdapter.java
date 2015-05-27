package com.trbboi.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<Integer> {

	public ImageAdapter(Context context, int resource, Integer[] objects) {
	  super( context, resource, objects );
  }

	@Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return _getView( position, convertView, parent);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
      return _getView( position, convertView, parent);
  }
 
  private View _getView(int position, View convertView, ViewGroup parent ) {
		ImageView imageView;
		if (convertView != null ) {
			imageView = (ImageView) convertView;
		}
		else {
			imageView = new ImageView( this.getContext() );
		}

		int resId = super.getItem( position );
	  imageView.setImageResource( resId );
		return imageView;
  }
}
