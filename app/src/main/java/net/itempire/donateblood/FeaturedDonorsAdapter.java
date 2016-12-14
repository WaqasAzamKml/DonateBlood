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
 * Created by MIRSAAB on 12/14/2016.
 */

public class FeaturedDonorsAdapter extends ArrayAdapter<FeaturedDonors> {
    ArrayList<FeaturedDonors> arrayListFeaturedDonors;
    int Resource;
    LayoutInflater vi;
    ViewHolder holder;

    public FeaturedDonorsAdapter(Context context, int resource, ArrayList<FeaturedDonors> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayListFeaturedDonors = objects;
        Resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = vi.inflate(Resource, null);
            holder = new ViewHolder();
            holder.tvDonorName = (TextView) convertView.findViewById(R.id.tvDonorName);
            holder.tvDonorThanks = (TextView) convertView.findViewById(R.id.tvDonorThanks);
            holder.tvDonorCity = (TextView) convertView.findViewById(R.id.tvDonorCity);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDonorName.setText(arrayListFeaturedDonors.get(position).getDonorName());
        holder.tvDonorThanks.setText(arrayListFeaturedDonors.get(position).getDonoroThanks());
        holder.tvDonorCity.setText(arrayListFeaturedDonors.get(position).getDonorCity());
        return convertView;
    }

    static class ViewHolder{
        public TextView tvDonorName, tvDonorThanks, tvDonorCity;
    }
}
