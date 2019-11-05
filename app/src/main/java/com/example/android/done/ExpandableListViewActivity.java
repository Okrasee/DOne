package com.example.android.done;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class ExpandableListViewActivity extends AppCompatActivity {
     ExpandableListView taskslist;
     ArrayList<Info> categoryInfo;
     ArrayList<ArrayList<Info>> tasksInfo;
     MyAdapter adapter;
     private CoordinatorLayout Bg;
     private boolean[] Boolean;
     private String categoryName;
     private Map<String, Integer> catDictionary;
     private boolean isFabOpen = false;
     private FloatingActionButton fabswitch;
     private FloatingActionButton fabadd;
     private FloatingActionButton fabrefresh;
     private Animation fab_open_up;
     private Animation fab_open_left;
     private Animation fab_close_up;
     private Animation fab_close_left;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         InitData();
         taskslist = (ExpandableListView)findViewById(R.id.list);
         adapter = new MyAdapter(this);
         taskslist.setAdapter(adapter);

     }

     private void InitData() {
         categoryInfo = new ArrayList<Info>();
         tasksInfo = new ArrayList<ArrayList<Info>>();
     }

     private void addInfo(Info category, ArrayList<Info> tasklist) {
         categoryInfo.add(category);
         ArrayList<Info> childitem = new ArrayList<Info>();
         for (int i = 0; i < tasklist.size(); i++) {
             childitem.add(tasklist.get(i));
         }
         tasksInfo.add(childitem);
     }
    public void animateFAB(){
        if (isFabOpen) {
            fabswitch.startAnimation(fab_close_up);
            fabadd.startAnimation(fab_close_up);
            fabrefresh.startAnimation(fab_close_up);
            isFabOpen = false;
        } else {
            fabswitch.startAnimation(fab_open_up);
            fabadd.startAnimation(fab_open_up);
            fabrefresh.startAnimation(fab_open_up);
            isFabOpen = true;
        }
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

    public interface MyClickListener {
        public void onButtonShowPopupWindowClick(View v);
    }

    public class MyAdapter extends BaseExpandableListAdapter implements View.OnClickListener{
         private Context context;
         private LayoutInflater mInflater;
         private MyClickListener mListener;

         public MyAdapter(Context context) {
             super();
             this.context = context;
             this.mInflater = LayoutInflater.from(context);

         }

         @Override
         public Info getChild(int groupPosition, int childPosition) {
             return tasksInfo.get(groupPosition).get(childPosition);
         }

         @Override
         public long getChildId(int groupPosition, int childPosition) { return childPosition; }

         @Override
         public int getChildrenCount(int groupPosition) {return tasksInfo.get(groupPosition).size();}

         @Override
         public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
             View listItemView = convertView;
             final ViewHolder holder;
             if (convertView == null) {
                 convertView = mInflater.inflate(R.layout.tasks_list_, null);
                 holder = new ViewHolder();
                 convertView.setTag(holder);

                 SwipeLayout.addSwipeView(holder.swipelayout);
             } else {
                 holder = (ViewHolder) convertView.getTag();
             }
             final Info currentTask = getChild(groupPosition,childPosition);
             holder.currTitle = (TextView) listItemView.findViewById(R.id.CaptionShow);
             holder.currTitle.setText(currentTask.getTitle());
             holder.currTitle.setInputType(InputType.TYPE_NULL);
             final LinearLayout nameBar = listItemView.findViewById(R.id.colorForNameBar);
             nameBar.setBackgroundResource(currentTask.getBgColor());
             final TextView currDescription = (TextView) listItemView.findViewById(R.id.DescriptionShow);
             currDescription.setText(currentTask.getDescription());
             currDescription.setInputType(InputType.TYPE_NULL);
             currDescription.setKeyListener(null);
             holder.editTask = (FloatingActionButton)listItemView.findViewById(R.id.editTask);
             holder.editTask.setTag(groupPosition, childPosition);
             holder.displayDDL = (TextView) listItemView.findViewById(R.id.displayDDL);
             holder.clock = (LinearLayout) listItemView.findViewById(R.id.deadline);
             int month = currentTask.getMonth();
             int day = currentTask.getDay();
             int hour = currentTask.getHour();
             int minute = currentTask.getMinute();
             if (hour < 10 & hour > 0) {hour = '0' + hour; }
             if (minute < 10) {minute = '0' + minute; }
             String hour_str = String.valueOf(hour);
             if (hour_str == String.valueOf(0)) {hour_str = "00"; }
             String concatTime = Month_str(month) + " " + String.valueOf(day) + "   " + hour_str +
                     ":" + String.valueOf(minute);
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
                     if (currentTask.getDescription().isEmpty()) {return; }
                     else if (holder.Detail.getVisibility() == View.GONE) {
                         nameBar.setBackgroundResource(R.drawable.reccolor00);
                         expand(holder.Detail); }
                     else {
                         collapse(holder.Detail);
                         nameBar.setBackgroundResource(R.drawable.reccolor0); }
                 }
             });
             holder.deleteTask = listItemView.findViewById(R.id.deleteTask);
             holder.deleteTask.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     tasksInfo.get(groupPosition).remove(childPosition);
                     notifyDataSetChanged();
                     SwipeLayout.removeSwipeView(holder.swipelayout);
                 }
             });
             return convertView;
         }

         @Override
         public Info getGroup(int groupPosition) {return categoryInfo.get(groupPosition);}

         @Override
         public long getGroupId(int groupPosition) { return groupPosition; }

         @Override
         public int getGroupCount() {return categoryInfo.size();}

         @Override
         public View getGroupView(int groupPosition, boolean isExpanded, View listItemView, ViewGroup parent) {
             listItemView = mInflater.inflate(R.layout.category, null);
             final ViewHolder holder;
             holder = new ViewHolder();
             Info currentCategory = getGroup(groupPosition);
             TextView currCategory = (TextView) listItemView.findViewById(R.id.categoryName);
             currCategory.setText(currentCategory.getTitle());
             LinearLayout categoryBar = listItemView.findViewById(R.id.colorForCategory);
             categoryBar.setBackgroundResource(currentCategory.getBgColor());
             holder.button = (FloatingActionButton)listItemView.findViewById(R.id.addTask);
             holder.button.setTag(groupPosition);
             listItemView.setTag(holder);
             holder.button.setOnClickListener(this);
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
         }
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
        public FloatingActionButton button;
        public TextView currTitle;
        public LinearLayout showDetail;
        public LinearLayout Detail;
        public TextView displayDDL;
        public LinearLayout clock;
        public FloatingActionButton editTask;
        public FloatingActionButton deleteTask;
    }


}
