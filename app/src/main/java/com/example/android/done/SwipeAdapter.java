package com.example.android.done;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SwipeAdapter extends BaseExpandableListAdapter implements View.OnClickListener{

    private final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_COUNT = 2;
    private Context context;
    private ArrayList<Info> tasksInfo;
    private LayoutInflater mLayoutInflator;
    private LinearLayout Detail;
    private ArrayList<ArrayList<Info>> child;
    private ArrayList<Info> group;
    private MyClickListener mListener;

    public interface MyClickListener {
        public void onButtonShowPopupWindowClick(View v);
    }

    public String Month_str(int month) {
        if (month == 1) {
            return "Jan";
        }
        else if (month == 2) {
            return "Feb";
        }
        else if (month == 3) {
            return "Mar";
        }
        else if (month == 4) {
            return "Apr";
        }
        else if (month == 5) {
            return "May";
        }
        else if (month == 6) {
            return "Jun";
        }
        else if (month == 7) {
            return "Jul";
        }
        else if (month == 8) {
            return "Aug";
        }
        else if (month == 9) {
            return "Sep";
        }
        else if (month == 10) {
            return "Oct";
        }
        else if (month == 11) {
            return "Nov";
        }
        else if (month == 12) {
            return "Dec";
        }
        return "";
    }

    public SwipeAdapter(Context context, ArrayList<Info> mCatsInfo, ArrayList<ArrayList<Info>> mTasksInfo, MyClickListener listener) {
        super();
        this.mLayoutInflator = LayoutInflater.from(context);
        this.mListener = listener;
        this.context = context;
        this.child = mTasksInfo;
        this.group = mCatsInfo;
    }

    //-----------------Child----------------//
    @Override
    public Info getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public View getChildView(final int groupPosition,final int  childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflator.inflate(R.layout.tasks_list_, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.swipelayout = (SwipeLayout) convertView.findViewById(R.id.currentTask);
            SwipeLayout.addSwipeView(holder.swipelayout);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.currTitle.setText(child.get(groupPosition).get(childPosition).getTitle());
        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child.get(groupPosition).remove(childPosition);
                notifyDataSetChanged();
                SwipeLayout.removeSwipeView(holder.swipelayout);
            }
        });
        return convertView;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = mLayoutInflator.inflate(R.layout.category, null);
        Info currentCategory = group.get(groupPosition);
        TextView currCategory = (TextView) convertView.findViewById(R.id.categoryName);
        currCategory.setText(currentCategory.getTitle());
        LinearLayout categoryBar = convertView.findViewById(R.id.colorForCategory);
        categoryBar.setBackgroundResource(currentCategory.getBgColor());
        FloatingActionButton button = (FloatingActionButton)convertView.findViewById(R.id.addTask);
        button.setOnClickListener(this);
        button.setTag(groupPosition);
        convertView.setTag(groupPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {
        public FloatingActionButton button;
        public TextView currTitle;
        public LinearLayout showDetail;
        public LinearLayout Detail;
        public TextView displayDDL;
        public LinearLayout clock;
        public FloatingActionButton editTask;
        public FloatingActionButton deleteTask;
        public SwipeLayout swipelayout;
    }

    @Override
    public void onClick(View v) {
        mListener.onButtonShowPopupWindowClick(v);
    }

    private void expand(LinearLayout LL) {
        LL.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        LL.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, LL.getMeasuredHeight(), LL);
        mAnimator.start();
    }

    private void collapse(final LinearLayout LL) {
        int finalHeight = LL.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, LL);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                LL.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end, final LinearLayout LL) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = LL.getLayoutParams();
                layoutParams.height = value;
                LL.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

}


