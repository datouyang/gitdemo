package domain.mycompany.pulltorefreshpaul;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        initData();

        listView = (ListView) findViewById(R.id.listview_items);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return list.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                Holder holder;
                TextView tv;
                if(convertView==null){
                    holder = new Holder();
                    convertView = getLayoutInflater().inflate(R.layout.letter_list_item,null);
                    tv = convertView.findViewById(R.id.textView_letter);
                    holder.letter = tv;
                    convertView.setTag(holder);
                }else {
                    holder = (Holder)convertView.getTag();
                    tv = holder.letter;
                }

                tv.setText(list.get(position));
                return convertView;
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        for(int i = 0 ; i<26;i++){
            list.add("a" +i + "");
        }
    }

    class Holder {
        TextView letter;
    }

}
