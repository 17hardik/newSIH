package com.example.sih.Jobs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sih.R;
import com.example.sih.Registration.dataListView;

import java.util.List;

public class adapterListView extends BaseAdapter {

    private List<dataListView> listViewItem = null;

    private Context ctx = null;

    public adapterListView (Context ctx, List<dataListView> listViewItem) {
        this.ctx = ctx;
        this.listViewItem = listViewItem;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(listViewItem!=null)
        {
            ret = listViewItem.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int itemIndex) {
        Object ret = null;
        if(listViewItem!=null) {
            ret = listViewItem.get(itemIndex);
        }
        return ret;
    }

    @Override
    public long getItemId(int itemIndex) {
        return itemIndex;
    }

    @Override
    public View getView(int itemIndex, View convertView, ViewGroup viewGroup) {

        ViewHolder_ListView viewHolder = null;

        if(convertView!=null)
        {
            viewHolder = (ViewHolder_ListView) convertView.getTag();
        }else
        {
            convertView = View.inflate(ctx, R.layout.activity_checkbox_listview, null);

            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.list_view_item_checkbox);

            TextView listItemText = (TextView) convertView.findViewById(R.id.list_view_item_text);

            viewHolder = new ViewHolder_ListView(convertView);

            viewHolder.setItemCheckbox(listItemCheckbox);

            viewHolder.setItemTextView(listItemText);

            convertView.setTag(viewHolder);
        }

        dataListView listViewItemDto = listViewItem.get(itemIndex);
        viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());
        viewHolder.getItemTextView().setText(listViewItemDto.getItemText());

        return convertView;
    }
}
