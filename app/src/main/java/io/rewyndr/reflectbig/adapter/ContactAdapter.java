package io.rewyndr.reflectbig.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rewyndr.reflectbig.R;
import io.rewyndr.reflectbig.model.Contacts;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter {
	Activity ctx = null;
	List<Contacts> toBeShown;
	boolean[] itemChecked;
	ArrayList<Contacts> selected = new ArrayList<Contacts>();

	public ContactAdapter(Activity ctx, List<Contacts> allContacts) {
		this.ctx = ctx;
		this.toBeShown = allContacts;
		this.itemChecked = new boolean[allContacts.size()];
	}

	@Override
	public int getCount() {
		return toBeShown.size();
	}

	@Override
	public Object getItem(int position) {
		return toBeShown.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if(convertView == null) {
			LayoutInflater inflater = ctx.getLayoutInflater();
			view = inflater.inflate(R.layout.activity_contact, null);
		} else { 
			view = convertView;
		}
		TextView contactName = (TextView) view.findViewById(R.id.textView1);
		contactName.setText(toBeShown.get(position).getContactName());
		TextView email = (TextView) view.findViewById(R.id.textView2);
		email.setText(toBeShown.get(position).getEmail());
		final CheckBox box = (CheckBox) view.findViewById(R.id.checkBox1);
		if (itemChecked[position]) 
			box.setChecked(true);
		else
			box.setChecked(false);
		box.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(box.isChecked()) {
					itemChecked[position] = true;
					selected.add(toBeShown.get(position));
				} else {
					itemChecked[position] = false;
					selected.remove(toBeShown.get(position));
				}
			}
		});
		return view;
	}
	
	public ArrayList<Contacts> getSelected() {
		return selected;
	}
}
