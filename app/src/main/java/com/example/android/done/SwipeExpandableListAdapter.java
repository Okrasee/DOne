package com.example.android.done;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import java.util.ArrayList;


/**
 * Created by peng on 2018/4/18.
 */

public class SwipeExpandableListAdapter extends BaseExpandableListAdapter {
    private ArrayList<Info> group;
    private ArrayList<ArrayList<Info>> child;
    private Context mContext;
    private OnClickListenerEditOrDelete onClickListenerDelete;

    public SwipeExpandableListAdapter(ArrayList<Info> group, ArrayList<ArrayList<Info>> child, Context mContext) {
        this.group = group;
        this.child = child;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Info getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Info getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        SwipeExpandableListAdapter.ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.category, parent, false);
            groupHolder = new SwipeExpandableListAdapter.ViewHolderGroup();
            groupHolder.currCategory = (TextView) convertView.findViewById(R.id.categoryName);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (SwipeExpandableListAdapter.ViewHolderGroup) convertView.getTag();
        }
        groupHolder.currCategory.setText(group.get(groupPosition).getTitle());
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderItem itemHolder;
        Info currentTask = child.get(groupPosition).get(childPosition);
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.tasks_list_, parent, false);
            itemHolder = new SwipeExpandableListAdapter.ViewHolderItem();
            itemHolder.currTitle = convertView.findViewById(R.id.CaptionShow);
            itemHolder.currTitle.setText(currentTask.getTitle());
            itemHolder.currTitle.setInputType(InputType.TYPE_NULL);
            convertView.setTag(itemHolder);//store up viewHolder
        }else{
            itemHolder = (SwipeExpandableListAdapter.ViewHolderItem) convertView.getTag();
        }
        itemHolder.deleteTask = (FloatingActionButton) convertView.findViewById(R.id.deleteTask);
        itemHolder.editTask = (FloatingActionButton) convertView.findViewById(R.id.editTask);
        itemHolder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDelete.OnClickListenerDelete(groupPosition,childPosition); }
        });
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolderGroup{
        TextView currCategory;
    }

    private static class ViewHolderItem{
        TextView currTitle;
        FloatingActionButton deleteTask, editTask;
    }

    public interface OnClickListenerEditOrDelete{
        void OnClickListenerDelete(int groupPosition,int position);
    }

    public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerDelete){
        this.onClickListenerDelete=onClickListenerDelete;
    }
}