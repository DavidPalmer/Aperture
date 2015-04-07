package io.rewyndr.reflectbig.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;
import com.rewyndr.reflectbig.R;
import io.rewyndr.reflectbig.activity.EventDetailActivity;
import io.rewyndr.reflectbig.activity.PhotoMultiViewActivity;
import io.rewyndr.reflectbig.model.AttendeeStatus;
import io.rewyndr.reflectbig.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Dileeshvar on 8/31/14.
 */
public class EventListAdapter extends SectionAdapter {
    private Activity context;
    private String[] headerTitle = {"Past Events", "Now", "Upcoming Events"};
    private ArrayList<Event> pastEventList;
    private ArrayList<Event> currentEventList;
    private ArrayList<Event> upcomingEventList;

    public EventListAdapter(Activity context, ArrayList<Event> pastEventList, ArrayList<Event> currentEventList, ArrayList<Event> upcomingEventList) {
        this.context = context;
        this.pastEventList = pastEventList;
        this.currentEventList = currentEventList;
        this.upcomingEventList = upcomingEventList;
    }

    @Override
    public int numberOfSections() {
        return 3;
    }

    @Override
    public int numberOfRows(int section) {
        if (section == 0)
            return pastEventList.size();
        else if (section == 1)
            return currentEventList.size();
        else if (section == 2)
            return upcomingEventList.size();
        else
            return 0;
    }

    @Override
    public Object getRowItem(int section, int row) {
        if (section == 0)
            return pastEventList.get(row);
        if (section == 1)
            return currentEventList.get(row);
        if (section == 2)
            return upcomingEventList.get(row);
        else
            return 0;
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.event_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) view.findViewById(R.id.event_name);
            viewHolder.eventDate = (TextView) view.findViewById(R.id.event_date);
            viewHolder.eventLocation = (TextView) view.findViewById(R.id.event_place);
            viewHolder.eventCount = (TextView) view.findViewById(R.id.count);
            viewHolder.eventCountImage = (ImageView) view.findViewById(R.id.event_pic_or_user_icon);
            viewHolder.eventConfirmation = (ImageView) view.findViewById(R.id.event_invite_status);
            view.setTag(viewHolder);
            if (section == 0)
                ((ViewHolder) view.getTag()).eventName.setTag(pastEventList.get(row).getEventName());
            if (section == 1)
                ((ViewHolder) view.getTag()).eventName.setTag(currentEventList.get(row).getEventName());
            if (section == 2)
                ((ViewHolder) view.getTag()).eventName.setTag(upcomingEventList.get(row).getEventName());
        } else {
            view = convertView;
            if (section == 0)
                ((ViewHolder) view.getTag()).eventName.setTag(pastEventList.get(row).getEventName());
            if (section == 1)
                ((ViewHolder) view.getTag()).eventName.setTag(currentEventList.get(row).getEventName());
            if (section == 2)
                ((ViewHolder) view.getTag()).eventName.setTag(upcomingEventList.get(row).getEventName());
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if (section == 0 && pastEventList != null) {
            view.setBackgroundResource(Color.TRANSPARENT);
            holder.eventName.setText(pastEventList.get(row).getEventName());
            holder.eventDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(pastEventList.get(row).getStartDate()));
            holder.eventLocation.setText(pastEventList.get(row).getShortLocation());
            holder.eventCountImage.setImageResource(R.drawable.ic_action_camera);
            holder.eventCount.setText(pastEventList.get(row).getPhotosCount() + "");
            if (pastEventList.get(row).getMyStatus() == AttendeeStatus.ACCEPTED || pastEventList.get(row).getMyStatus() == AttendeeStatus.ATTENDED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_accept);
            else if (pastEventList.get(row).getMyStatus() == AttendeeStatus.DECLINED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_cancel);
            else if (pastEventList.get(row).getMyStatus() == AttendeeStatus.NOT_RESPONDED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_warning);
        }
        if (section == 1 && currentEventList != null) {
            view.setBackgroundResource(R.drawable.green_gradiant);
            holder.eventName.setText(currentEventList.get(row).getEventName());
            holder.eventDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(currentEventList.get(row).getStartDate()));
            holder.eventLocation.setText(currentEventList.get(row).getShortLocation());
            holder.eventCountImage.setImageResource(R.drawable.ic_action_camera);
            holder.eventCount.setText(currentEventList.get(row).getPhotosCount() + "");
            if (currentEventList.get(row).getMyStatus() == AttendeeStatus.ACCEPTED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_accept);
            else if (currentEventList.get(row).getMyStatus() == AttendeeStatus.DECLINED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_cancel);
            else if (currentEventList.get(row).getMyStatus() == AttendeeStatus.NOT_RESPONDED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_warning);
        }
        if (section == 2 && upcomingEventList != null) {
            view.setBackgroundResource(Color.TRANSPARENT);
            holder.eventName.setText(upcomingEventList.get(row).getEventName());
            holder.eventDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(upcomingEventList.get(row).getStartDate()));
            holder.eventLocation.setText(upcomingEventList.get(row).getShortLocation());
            holder.eventCountImage.setImageResource(R.drawable.ic_action_group);
            holder.eventCount.setText(upcomingEventList.get(row).getAttendeesCount() + "");
            if (upcomingEventList.get(row).getMyStatus() == AttendeeStatus.ACCEPTED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_accept);
            else if (upcomingEventList.get(row).getMyStatus() == AttendeeStatus.DECLINED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_cancel);
            else if (upcomingEventList.get(row).getMyStatus() == AttendeeStatus.NOT_RESPONDED)
                holder.eventConfirmation.setImageResource(R.drawable.ic_action_warning);
        }
        return view;
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    @Override
    public int getSectionHeaderItemViewType(int section) {
        return section % 2;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.event_header, null);
            convertView = (TextView) view.findViewById(R.id.event_header_title);
        }
        ((TextView) convertView).setText(headerTitle[section]);
        if (section == 1) {
            convertView.setBackgroundResource(R.drawable.header_color);
//            ((TextView) convertView).setTextColor(R.color.abc_search_url_text_normal);
        } else
            convertView.setBackgroundResource(R.drawable.header_color);
        return convertView;
    }


    @Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
        super.onRowItemClick(parent, view, section, row, id);
        Event event = null;
        if (section == 0) {
            event = pastEventList.get(row);
            Intent intent = new Intent(context, PhotoMultiViewActivity.class);
            intent.putExtra("event", event);
            context.startActivity(intent);
        } else if (section == 1) {
            event = currentEventList.get(row);
            Intent intent = new Intent(context, PhotoMultiViewActivity.class);
            intent.putExtra("event", event);
            context.startActivity(intent);
        } else if (section == 2) {
            event = upcomingEventList.get(row);
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("event", event);
            context.startActivity(intent);
        }
//        Toast.makeText(DemoActivity.this, "Section " + section + " Row " + row, Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder {
        protected TextView eventName;
        protected TextView eventDate;
        protected TextView eventLocation;
        protected TextView eventCount;
        protected ImageView eventConfirmation;
        protected ImageView eventCountImage;
    }
}
