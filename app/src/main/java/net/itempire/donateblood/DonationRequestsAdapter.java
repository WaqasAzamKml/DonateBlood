package net.itempire.donateblood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MIRSAAB on 12/3/2016.
 */

public class DonationRequestsAdapter extends ArrayAdapter<DonationRequests> {
    ArrayList<DonationRequests> arrayListDonationRequests;
    int Resource;
    LayoutInflater vi;
    ViewHolder holder;

    public DonationRequestsAdapter(Context context, int resource, ArrayList<DonationRequests> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayListDonationRequests = objects;
        Resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = vi.inflate(Resource, null);
            holder = new ViewHolder();
            holder.tvRequestBloodGroup = (TextView) convertView.findViewById(R.id.tvRequestBloodGroup);
            holder.tvRequestDate = (TextView) convertView.findViewById(R.id.tvRequestDate);
            holder.tvRequestCity = (TextView) convertView.findViewById(R.id.tvRequestCity);
            holder.tvRequestIcon = (TextView) convertView.findViewById(R.id.tvRequestIcon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvRequestBloodGroup.setText(arrayListDonationRequests.get(position).getRequestBloodGroup());
        holder.tvRequestDate.setText(arrayListDonationRequests.get(position).getRequestDate());
        holder.tvRequestCity.setText(arrayListDonationRequests.get(position).getRequestCity());
        holder.tvRequestIcon.setText(arrayListDonationRequests.get(position).getRequestIcon());
        return convertView;
    }

    static class ViewHolder{
        public TextView tvRequestBloodGroup, tvRequestDate, tvRequestCity, tvRequestIcon;
    }
}
