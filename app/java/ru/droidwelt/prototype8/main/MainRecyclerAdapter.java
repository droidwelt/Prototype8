package ru.droidwelt.prototype8.main;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;


@SuppressWarnings("Annotator")
class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private MainActivity act;

    public void setMainActivity(MainActivity a) {
        act = a;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int lyRes;
        if (Appl.APP_STYLE.equalsIgnoreCase("C")) {
            lyRes = R.layout.activity_main_item;
        } else {
            lyRes = R.layout.activity_main_item_flat;
        }
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(lyRes, parent, false);
        return new ViewHolder(itemLayoutView);
    }


    @Override
    public int getItemCount() {
        return act.getListMain().size();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MainDataStructure myListItem = act.getListMain().get(position);
        viewHolder.MAIN_ID = myListItem.main_id;
        viewHolder.tv_mainitem_title.setText(myListItem.main_title);
        String text = myListItem.main_text;
        if ((text == null) || (text.isEmpty())) {
            viewHolder.tv_mainitem_text.setText("");
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.tv_mainitem_text.getLayoutParams();
            lp.height = 1;
            viewHolder.tv_mainitem_text.setLayoutParams(lp);
        } else {
            viewHolder.tv_mainitem_text.setText(Html.fromHtml(text));
        }
        viewHolder.iv_mainitem_image.setImageDrawable(myListItem.main_img);
        if (Appl.APP_STYLE.equalsIgnoreCase("C")) {
            int color = Color.parseColor(myListItem.main_color);
            if (viewHolder.ly_mainitem != null)
                viewHolder.ly_mainitem.setBackgroundColor(color);   // внутренность
            if (viewHolder.cv_mainitem != null)
                viewHolder.cv_mainitem.setCardBackgroundColor(color);   // рамка
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_mainitem_title, tv_mainitem_text;
        ImageView iv_mainitem_image;
        LinearLayout ly_mainitem;
        android.support.v7.widget.CardView cv_mainitem;
        String MAIN_ID;

        ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);
            tv_mainitem_title = itemLayoutView.findViewById(R.id.tv_mainitem_title);
            tv_mainitem_text = itemLayoutView.findViewById(R.id.tv_mainitem_text);
            iv_mainitem_image = itemLayoutView.findViewById(R.id.iv_mainitem_image);
            if (Appl.APP_STYLE.equalsIgnoreCase("C")) {
                ly_mainitem = itemLayoutView.findViewById(R.id.ly_mainitem);
                cv_mainitem = itemLayoutView.findViewById(R.id.cv_mainitem);
                RelativeLayout ly_main = itemLayoutView.findViewById(R.id.ly_main);
                int h = (int) (Appl.MAIN_RECYCLER_HEIGHT * 0.75 / 5); // 0.8
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                ly_main.setLayoutParams(layoutParams);
            } else {
                iv_mainitem_image.setPadding(30, 30, 30, 30);
            }
            itemLayoutView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            act.startActivity(MAIN_ID);
        }
    }


}