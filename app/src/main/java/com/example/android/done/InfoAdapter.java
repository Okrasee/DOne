package com.example.android.done;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class InfoAdapter extends ArrayAdapter<Info> implements View.OnClickListener {
    private final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_COUNT = 2;
    private Context context;
    private ArrayList<Info> tasksInfo;
    private LayoutInflater mLayoutInflator;
    private MyClickListener mListener;
    private LinearLayout Detail;

    public interface MyClickListener {
        public void onButtonShowPopupWindowClick(View v);
        public void editTask(View v);
    }

    public InfoAdapter(Context context, ArrayList<Info> mTasksInfo, MyClickListener listener) {
        super(context, R.layout.category, mTasksInfo);
        this.tasksInfo = mTasksInfo;
        mLayoutInflator = LayoutInflater.from(context);
        mListener = listener;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasksInfo.size();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public Info getItem(int position) {
        return tasksInfo.get(position);
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getItemViewType(int position) {
        Info aTask = getItem(position);
        //////task/////////
        if (aTask.getBgColor() == R.drawable.reccolor0) {
            return TYPE_TWO;
        }
        /////category//////
        else {
            return TYPE_ONE;
        }
    }

    public boolean getMode (int position) {
        Info aTask = getItem(position);
        if (aTask.getMode()) {return true; } //categorized
        else {return false; }
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

    public ArrayList<Info> getArrayList() {
        return tasksInfo;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View listItemView = convertView;
        final ViewHolder holder;
        if (listItemView == null) {
                if (getItemViewType(position) == 0 & getMode(position) == true) { //////category///////
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.category, parent, false);
                    holder = new ViewHolder();
                    Info currentCategory = getItem(position);
                    TextView currCategory = (TextView) listItemView.findViewById(R.id.categoryName);
                    currCategory.setText(currentCategory.getTitle());
                    LinearLayout categoryBar = listItemView.findViewById(R.id.colorForCategory);
                    categoryBar.setBackgroundResource(currentCategory.getBgColor());
                    holder.button = (FloatingActionButton)listItemView.findViewById(R.id.addTask);
                    listItemView.setTag(holder);
                    holder.button.setOnClickListener(this);
                    holder.button.setTag(position);
                }
                else if (getItemViewType(position) == 0 & getMode(position) == false) { /////mixedTasks
                    holder = new ViewHolder();
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.tasks_list_, parent, false);
                    listItemView.setTag(position);
                    final Info currentTask = getItem(position);
                    //Toast.makeText(getContext(), String.valueOf(TYPE_TWO), Toast.LENGTH_LONG).show();
                    holder.currTitle = listItemView.findViewById(R.id.CaptionShow);
                    holder.currTitle.setText(currentTask.getTitle());
                    holder.currTitle.setInputType(InputType.TYPE_NULL);
                    final LinearLayout nameBar = listItemView.findViewById(R.id.colorForNameBar);
                    nameBar.setBackgroundResource(currentTask.getBgColor());
                    final TextView currDescription = (TextView) listItemView.findViewById(R.id.DescriptionShow);
                    currDescription.setText(currentTask.getDescription());
                    currDescription.setInputType(InputType.TYPE_NULL);
                    currDescription.setKeyListener(null);
                    holder.displayDDL = (TextView) listItemView.findViewById(R.id.displayDDL);
                    holder.clock = (LinearLayout) listItemView.findViewById(R.id.deadline);
                    int month = currentTask.getMonth();
                    int day = currentTask.getDay();
                    int hour = currentTask.getHour();
                    int minute = currentTask.getMinute();
                    String hour_str = String.valueOf(hour);
                    String min_str = String.valueOf(minute);
                    if (hour < 10 & hour > 0) {hour_str = '0' + String.valueOf(hour); }
                    if (minute < 10) {min_str = '0' + String.valueOf(minute); }
                    if (hour_str == String.valueOf(0)) {hour_str = "00"; }
                    String concatTime = Month_str(month) + " " + String.valueOf(day) + "   " + hour_str +
                            ":" + min_str;
                    //if (hour < 10 & hour > 0) {hour = '0' + hour; }
                    //if (minute < 10) {minute = '0' + minute; }
                    //String hour_str = String.valueOf(hour);
                    //if (hour_str == String.valueOf(0)) {hour_str = "00"; }
                    //String minute_str = String.valueOf(minute);
                    //if (minute_str == String.valueOf(48)) {
                        //minute_str = "00"; }
                    //String concatTime = Month_str(month) + " " + String.valueOf(day) + "   " + hour_str +
                            //":" + minute_str;
                    if (month == Calendar.MONTH & day == Calendar.DAY_OF_MONTH && hour == Calendar.HOUR_OF_DAY && minute == Calendar.MINUTE) {
                        holder.clock.setVisibility(View.GONE);
                    }
                    else {
                        holder.displayDDL.setText(concatTime);
                        holder.displayDDL.setInputType(InputType.TYPE_NULL);
                        holder.displayDDL.setKeyListener(null);
                    }
                    holder.showDetail = (LinearLayout)listItemView.findViewById(R.id.colorForNameBar);
                    holder.Detail = (LinearLayout)listItemView.findViewById(R.id.theDescription);
                    holder.Detail.setVisibility(View.GONE);
                    holder.currTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (currentTask.getDescription().isEmpty()) {return; }
                            else if (holder.Detail.getVisibility() == View.GONE) {
                                //if currentTask.getBgColor() == R.id.reccolor2
                                //nameBar.setBackgroundResource(R.drawable.effect2);
                                //nameBar.setBackgroundResource(R.drawable.effect...);
                                expand(holder.Detail); }
                            else {
                                collapse(holder.Detail); }
                        }
                    });
                    holder.showDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (currentTask.getDescription().isEmpty()) {return; }
                            else if (holder.Detail.getVisibility() == View.GONE) {
                                nameBar.setBackgroundResource(R.drawable.reccolor00);
                                expand(holder.Detail); }
                            else {
                                nameBar.setBackgroundResource(R.drawable.reccolor0);
                                collapse(holder.Detail); }
                        }
                    });
                }
                else { /////tasks
                    holder = new ViewHolder();
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.tasks_list_, parent, false);
                    final Info currentTask = getItem(position);
                    holder.currTitle = (TextView) listItemView.findViewById(R.id.CaptionShow);
                    holder.currTitle.setText(currentTask.getTitle());
                    holder.currTitle.setInputType(InputType.TYPE_NULL);
                    final LinearLayout nameBar = listItemView.findViewById(R.id.colorForNameBar);
                    nameBar.setBackgroundResource(currentTask.getBgColor());
                    final TextView currDescription = (TextView) listItemView.findViewById(R.id.DescriptionShow);
                    currDescription.setText(currentTask.getDescription());
                    currDescription.setInputType(InputType.TYPE_NULL);
                    currDescription.setKeyListener(null);
                    //holder.editTask = (FloatingActionButton)listItemView.findViewById(R.id.editTask);
                    //holder.editTask.setTag(position);
                    holder.displayDDL = (TextView) listItemView.findViewById(R.id.displayDDL);
                    holder.clock = (LinearLayout) listItemView.findViewById(R.id.deadline);
                    int month = currentTask.getMonth();
                    int day = currentTask.getDay();
                    int hour = currentTask.getHour();
                    int minute = currentTask.getMinute();
                    String hour_str = String.valueOf(hour);
                    String min_str = String.valueOf(minute);
                    if (hour < 10 & hour > 0) {hour_str = '0' + String.valueOf(hour); }
                    if (minute < 10) {min_str = '0' + String.valueOf(minute); }
                    if (hour_str == String.valueOf(0)) {hour_str = "00"; }
                    String concatTime = Month_str(month) + " " + String.valueOf(day) + "   " + hour_str +
                            ":" + min_str;
                    //if (hour < 10 & hour > 0) {hour = '0' + hour; }
                    //if (minute < 10) {minute = '0' + minute; }
                    //String hour_str = String.valueOf(hour);
                    //if (hour_str == String.valueOf(0)) {hour_str = "00"; }
                    //String concatTime = Month_str(month) + " " + String.valueOf(day) + "   " + hour_str +
                            //":" + String.valueOf(minute);
                    if (month == 13 & day == Calendar.DAY_OF_MONTH && hour == Calendar.HOUR_OF_DAY && minute == Calendar.MINUTE) {
                        holder.clock.setVisibility(View.GONE);
                    }
                    else {
                        holder.displayDDL.setText(concatTime);
                        holder.displayDDL.setInputType(InputType.TYPE_NULL);
                        holder.displayDDL.setKeyListener(null);
                    }
                    holder.showDetail = (LinearLayout)listItemView.findViewById(R.id.colorForNameBar);
                    holder.Detail = (LinearLayout)listItemView.findViewById(R.id.theDescription);
                    holder.Detail.setVisibility(View.GONE);
                    holder.currTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.v("position", String.valueOf(position));
                            if (currentTask.getDescription().isEmpty()) {return; }
                            else if (holder.Detail.getVisibility() == View.GONE) {
                                nameBar.setBackgroundResource(R.drawable.reccolor00);
                                expand(holder.Detail); }
                            else {
                                collapse(holder.Detail);
                                nameBar.setBackgroundResource(R.drawable.reccolor0); }
                        }
                    });
                }
        }
        return listItemView;
    }

    public class ViewHolder {
        public FloatingActionButton button;
        public TextView currTitle;
        public LinearLayout showDetail;
        public LinearLayout Detail;
        public TextView displayDDL;
        public LinearLayout clock;
        public FloatingActionButton editTask;
    }

    @Override
    public void onClick(View v) {
        mListener.onButtonShowPopupWindowClick(v);
        mListener.editTask(v);
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
//TextView currDes = (TextView) listItemView.findViewById(R.id.DescribeShow);
//currDes.setText(currentTask.getDescription());