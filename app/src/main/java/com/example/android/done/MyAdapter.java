package com.example.android.done;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MyAdapter extends BaseExpandableListAdapter implements View.OnClickListener{
    ExpandableListView expandableListView;
    ArrayList<Info> group;
    ArrayList<ArrayList<Info>> child;
    ArrayList<Info> mixedChild;
    ArrayList<Info> completedChild;
    private Context context;
    private LayoutInflater mInflater;
    private MyClickListener mListener;
    private long firstClickTime = 0;
    final private long secondClickTime = 0;
    private Anim new_Anim;
    private boolean expand = false;

    public interface MyClickListener {
        public void onButtonShowPopupWindowClick(View v);
        public void editTask(View v);
        public void removeTask(View v);
    }

    public MyAdapter(Context context, ArrayList<Info> mCatsInfo, ArrayList<ArrayList<Info>> mTasksInfo, ArrayList<Info> mMixedTasksInfo, ArrayList<Info> mCompletedTasksInfo, MyClickListener listener) {
        super();
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.group = mCatsInfo;
        this.child = mTasksInfo;
        this.mixedChild = mMixedTasksInfo;
        this.completedChild = mCompletedTasksInfo;
        mListener = listener;
    }

    @Override
    public Info getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    @Override
    public int getChildrenCount(int groupPosition) {return child.get(groupPosition).size();}

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

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.v("hehehe", "hehehehe");
        final Calendar cal = Calendar.getInstance();
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tasks_list, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            SwipeLayout.addSwipeView(holder.swipelayout);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Info currentTask = getChild(groupPosition,childPosition);
        holder.currTitle = (TextView) convertView.findViewById(R.id.CaptionShow);
        holder.currTitle.setText(currentTask.getTitle());
        holder.currTitle.setInputType(InputType.TYPE_NULL);
        final LinearLayout nameBar = convertView.findViewById(R.id.colorForNameBar);
        nameBar.setBackgroundResource(currentTask.getBgColor());
        final TextView currDescription = (TextView) convertView.findViewById(R.id.DescriptionShow);
        currDescription.setText(currentTask.getDescription());
        currDescription.setInputType(InputType.TYPE_NULL);
        currDescription.setKeyListener(null);
        holder.editTask = (FloatingActionButton)convertView.findViewById(R.id.editTask);
        holder.editTask.setTag(groupPosition);
        holder.displayDDL = (TextView) convertView.findViewById(R.id.displayDDL);
        holder.clock = (LinearLayout) convertView.findViewById(R.id.deadline);
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
        Log.v("minute", String.valueOf(minute));
        if (month == 13) {
            holder.clock.setVisibility(View.GONE);
        }
        //if (month == 13 & day == cal.get(Calendar.DAY_OF_MONTH) && hour == cal.get(Calendar.HOUR_OF_DAY) && minute == cal.get(Calendar.MINUTE)) {holder.clock.setVisibility(View.GONE); }
        else {
            holder.displayDDL.setText(concatTime);
            holder.displayDDL.setInputType(InputType.TYPE_NULL);
            holder.displayDDL.setKeyListener(null);
        }
        holder.showDetail = (LinearLayout)convertView.findViewById(R.id.colorForNameBar);
        holder.Detail = (LinearLayout)convertView.findViewById(R.id.theDescription);
        holder.Detail.setVisibility(View.GONE);
        holder.currTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTask.getDescription().isEmpty()) {return; }
                else if (holder.Detail.getVisibility() == View.GONE) {
                    nameBar.setBackgroundResource(R.drawable.reccolor00);
                    expand(holder.Detail); }
                else {
                    collapse(holder.Detail);
                    nameBar.setBackgroundResource(R.drawable.reccolor0); }
            }
        });
        final View listItemView = (View) convertView;
        listItemView.setTag(R.id.groupPosition, groupPosition);
        listItemView.setTag(R.id.childPosition, childPosition);
        holder.deleteTask = convertView.findViewById(R.id.deleteTask);
        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View confirmationView = mInflater.inflate(R.layout.confirmation_delete, null, false);
                LinearLayout No = (LinearLayout) confirmationView.findViewById(R.id.no);
                LinearLayout Yes = (LinearLayout) confirmationView.findViewById(R.id.yes);

                //////////////////////// create the category popup window ////////////////////////////////
                final PopupWindow confirmationWindow = new PopupWindow(confirmationView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                confirmationWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                confirmationWindow.setAnimationStyle(R.style.popup_window_animation);
                //popupWindow.setFocusable(false);
                confirmationWindow.setOutsideTouchable(false);
                confirmationWindow.showAtLocation(confirmationView, Gravity.CENTER, 0, 0);
                confirmationWindow.update();
                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationWindow.dismiss();
                        //Bg.getForeground().setAlpha(0);
                    }
                });
                Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationWindow.dismiss();
                        //holder.Bg.getBackground().setAlpha(0);
                        Info currTask = child.get(groupPosition).get(childPosition);
                        child.get(groupPosition).remove(childPosition);
                        Info mixedCurrInfo = new Info(currTask.getTitle(),
                                currTask.getCatColor(), currTask.getDescription(), currTask.getMonth(),
                                currTask.getDay(),
                                currTask.getHour(), currTask.getMinute(), false);
                        int mixedPos = mixedChild.indexOf(mixedCurrInfo);
                        mixedChild.remove(mixedPos);
                        notifyDataSetChanged();
                        SwipeLayout.removeSwipeView(holder.swipelayout);
                    }
                });
            }
        });
        holder.editTask = convertView.findViewById(R.id.editTask);
        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editTask(listItemView);
            }
        });
        holder.finishTask = convertView.findViewById(R.id.finishTask);
        holder.finishTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View confirmationView = mInflater.inflate(R.layout.confirmation_finish, null, false);
                LinearLayout No = (LinearLayout) confirmationView.findViewById(R.id.no);
                LinearLayout Yes = (LinearLayout) confirmationView.findViewById(R.id.yes);

                //////////////////////// create the category popup window ////////////////////////////////
                final PopupWindow confirmationWindow = new PopupWindow(confirmationView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                confirmationWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                confirmationWindow.setAnimationStyle(R.style.popup_window_animation);
                //popupWindow.setFocusable(false);
                confirmationWindow.setOutsideTouchable(false);
                confirmationWindow.showAtLocation(confirmationView, Gravity.CENTER, 0, 0);
                confirmationWindow.update();
                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationWindow.dismiss();
                        //Bg.getForeground().setAlpha(0);
                    }
                });
                Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationWindow.dismiss();
                        Info finishedTask = child.get(groupPosition).get(childPosition);
                        child.get(groupPosition).remove(childPosition);
                        Info mixedCurrInfo = new Info(finishedTask.getTitle(),
                                finishedTask.getCatColor(), finishedTask.getDescription(), finishedTask.getMonth(),
                                finishedTask.getDay(),
                                finishedTask.getHour(), finishedTask.getMinute(), false);
                        int mixedPos = mixedChild.indexOf(mixedCurrInfo);
                        mixedChild.remove(mixedPos);
                        completedChild.add(finishedTask);
                        notifyDataSetChanged();
                        SwipeLayout.removeSwipeView(holder.swipelayout);
                    }
                });
            }
        });
        return convertView;
    }

    @Override
    public Info getGroup(int groupPosition) {return group.get(groupPosition);}

    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public int getGroupCount() {return group.size();}

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View listItemView, ViewGroup parent) {
        new_Anim = new Anim(context);
        listItemView = mInflater.inflate(R.layout.category, null);
        listItemView.setTag(R.id.groupPosition, R.id.childPosition);
        final View contextView = listItemView;
        final ViewHolder holder;
        holder = new ViewHolder();
        Info currentCategory = getGroup(groupPosition);
        TextView currCategory = (TextView) listItemView.findViewById(R.id.categoryName);
        currCategory.setText(currentCategory.getTitle());
        holder.categoryBar = listItemView.findViewById(R.id.colorForCategory);
        holder.categoryBar.setBackgroundResource(currentCategory.getBgColor());
        holder.addbutton = (FloatingActionButton)listItemView.findViewById(R.id.addTaskButton);
        holder.addbutton.setTag(groupPosition);
        holder.editbutton = listItemView.findViewById(R.id.edit_category);
        holder.deletebutton = listItemView.findViewById(R.id.delete_category);
        listItemView.setTag(holder);
        //holder.button.setOnClickListener(this);
        holder.addbutton.setFocusable(false);
        holder.addbutton.setClickable(true);
        holder.more = listItemView.findViewById(R.id.more);
        holder.more.setFocusable(false);
        holder.currentTask = listItemView.findViewById(R.id.currentTask);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand) {
                    holder.addbutton.show(); holder.editbutton.show();holder.deletebutton.show();
                    expand = !expand; }
                else {
                    holder.addbutton.hide(); holder.editbutton.hide();holder.deletebutton.hide();
                    expand = !expand; }
            }
        });
        holder.editbutton.setTag(groupPosition);
        holder.editbutton.setFocusable(false);
        holder.editbutton.setClickable(true);
        holder.deletebutton.setTag(groupPosition);
        holder.deletebutton.setFocusable(false);
        holder.deletebutton.setClickable(true);
        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View confirmationView = mInflater.inflate(R.layout.confirmation_delete_category, null, false);
                LinearLayout No = (LinearLayout) confirmationView.findViewById(R.id.no);
                LinearLayout Yes = (LinearLayout) confirmationView.findViewById(R.id.yes);

                //////////////////////// create the category popup window ////////////////////////////////
                final PopupWindow confirmationWindow = new PopupWindow(confirmationView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                confirmationWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                confirmationWindow.setAnimationStyle(R.style.popup_window_animation);
                //popupWindow.setFocusable(false);
                confirmationWindow.setOutsideTouchable(false);
                confirmationWindow.showAtLocation(confirmationView, Gravity.CENTER, 0, 0);
                confirmationWindow.update();
                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationWindow.dismiss();
                        //Bg.getForeground().setAlpha(0);
                    }
                });
                Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmationWindow.dismiss();
                        int BgColor = group.get(groupPosition).getBgColor();
                        Log.v("BgColor", String.valueOf(BgColor));
                        int size = mixedChild.size();
                        group.remove(groupPosition);
                        child.get(groupPosition).clear();
                        ArrayList<Info> alias = new ArrayList<Info>();
                        for (int i = 0; i < size; i++) {
                            alias.add(mixedChild.get(i));
                        }
                        Log.v("alias", String.valueOf(alias.size()));
                        for (int i = 0; i < size; i++) {
                            Log.v("i", String.valueOf(i));
                            Log.v("alias1", String.valueOf(alias.size()));
                            if (mixedChild.get(i).getBgColor() == BgColor) {
                                Log.v("alias2", String.valueOf(alias.size()));
                                alias.remove(0);
                                Log.v("alias3", String.valueOf(alias.size()));
                            }
                        }
                        mixedChild.clear();
                        for (int i = 0; i < alias.size(); i++) {
                            mixedChild.add(alias.get(i));
                        }
                notifyDataSetChanged();
                SwipeLayout.removeSwipeView(holder.swipelayout);
                    }
                });
            }
        });
        expand = false;
        return listItemView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onClick(View v) {
        mListener.onButtonShowPopupWindowClick(v);
        mListener.editTask(v);
        mListener.removeTask(v);
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

    class ViewHolder {
        SwipeLayout swipelayout;
        public LinearLayout currCategory;
        public FloatingActionButton addbutton;
        public FloatingActionButton editbutton;
        public FloatingActionButton deletebutton;
        public TextView currTitle;
        public LinearLayout showDetail;
        public LinearLayout Detail;
        public TextView displayDDL;
        public LinearLayout clock;
        public FloatingActionButton editTask;
        public FloatingActionButton finishTask;
        public FloatingActionButton deleteTask;
        public TimePicker timepicker;
        public LinearLayout categoryBar;
        public FloatingActionButton more;
        public CoordinatorLayout toolBar;
        public LinearLayout currentTask;
    }

}
