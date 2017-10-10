package domain.mycompany.pulltorefreshlibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Created by paul on 2017/9/20.
 */

public class RefreshableView extends LinearLayout implements View.OnTouchListener{

    //三个状态
    private final int PULL_TO_REFRESH = 0;
    private final int RELEASE_TO_REFRESH = 1;
    private final int UPDATING = 2;

    //状态域
    private int state = PULL_TO_REFRESH;

    //头部 view
    private View header;
    //头部包含的view
    private ProgressBar progressBar;
    private ImageView arrow_image;
    private TextView description;
    private TextView description_update_time;
    //头部高度
    private int headerHight;

    //列表
    ListView listView;

    //下拉头的布局参数，用于决定下拉位置
    MarginLayoutParams marginLayoutParams;

    //手指按下时的位置
    int initialY;

    //手指滑动到的当前位置
    int currentY;

    //loadOnce只加载一次
    private boolean loadOnce;


    //构造方法，里边将获得各种对象
    public RefreshableView(Context context,  AttributeSet attrs) {
        super(context,attrs);

        //设置RefreshableView的方向。
        setOrientation(VERTICAL);
        //当ListView之类的使用RefreshableView，自然会有一个头。因为Refreshable本身就是个LinearLayout，包含这些元素。
        //而这里的header其实就是获得了这个布局里边的内容。也就是获得整个layout。

        header = LayoutInflater.from(context).inflate(R.layout.paul_to_refresh, null, true);
        progressBar = header.findViewById(R.id.progress_bar_refresh);
        arrow_image = header.findViewById(R.id.image_view_arrow);
        description = header.findViewById(R.id.description);
        description_update_time = header.findViewById(R.id.updated_at);


        //把header加入RefreshableVIew当中。
        addView(header,0);


        //将包含RefreshableView中的ListView取出来。填充数据在Activity当中填充。这里的ListView负责监听onTouchListener。








    }

    //进行一些初始化方法
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //初始化布局参数
        if(changed && !loadOnce){
            marginLayoutParams =(MarginLayoutParams) header.getLayoutParams();
            headerHight = header.getHeight();
            marginLayoutParams.topMargin = -headerHight;
            Log.e("HEADERHIGHT", "***************RefreshableView: " + marginLayoutParams.topMargin, null );
            header.setLayoutParams(marginLayoutParams);
            listView = (ListView)getChildAt(1);
            listView.setOnTouchListener(this);

            loadOnce = true;

        }


    }


    //关键方法
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN :
                initialY =(int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE :
                float position = header.getY();

                Log.e("header Y", "onTouch: Y轴坐标" + position +"header高度" + headerHight ,null);

                currentY =(int) motionEvent.getRawY();

                    marginLayoutParams.topMargin = -headerHight + (currentY-initialY)/2;

                    this.setLayoutParams(marginLayoutParams);

                break;
            case MotionEvent.ACTION_UP :

                new RefreshTask().execute();




        }
        return false;
    }


    private class RefreshTask extends AsyncTask<Void, Integer,Integer >{

        @Override
        protected Integer doInBackground(Void... voids) {
            while (true){
                if ( marginLayoutParams.topMargin > -headerHight){
                    marginLayoutParams.topMargin = marginLayoutParams.topMargin -10;
                    publishProgress(marginLayoutParams.topMargin);
                    sleep(10);

                }else{
                    marginLayoutParams.topMargin = -headerHight;
                    publishProgress(marginLayoutParams.topMargin);
                    Log.e("--------------", "doInBackground: "+ marginLayoutParams.topMargin,null );
                    break;
                }
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer hight = values[0];
            setLayoutParams(marginLayoutParams);
            Log.e("--------------", "onProgressUpdate: "+ marginLayoutParams.topMargin,null );

        }
    }

    private void sleep(int i){

        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
