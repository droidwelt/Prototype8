package ru.droidwelt.prototype8.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.contactinfo.ContactInfoActivity;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.OnSwipeTouchListener;
import ru.droidwelt.prototype8.utils.common.PrefUtils;

@SuppressWarnings("Annotator")
public class ContactListAdapter  extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private int adapterPos;
    private ContactActivity act;

    public void setContactActivity(ContactActivity a) {
        act = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contact_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ContactListDataClass myListItem = act.list_cont.get(position);
        viewHolder.clt_id = myListItem.getClt_id();
        viewHolder.textViewName.setText(myListItem.getClt_name());
        viewHolder.textViewAddr.setText(myListItem.getClt_addr());

        byte[] decodedString = Base64.decode(myListItem.getClt_pict(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        viewHolder.imageView.setImageBitmap(decodedByte);
        viewHolder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return act.list_cont.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewAddr;
        ImageView imageView;
        String clt_id;

        ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);
            textViewName = itemLayoutView.findViewById(R.id.textViewName);
            textViewAddr = itemLayoutView.findViewById(R.id.textViewAddr);
            imageView = itemLayoutView.findViewById(R.id.imageView);

            itemLayoutView.setOnTouchListener(new OnSwipeTouchListener(itemLayoutView.getContext()) {

                public void onSwipeTop() {
                    //  Toast.makeText(Appl.getContext(), "top", Toast.LENGTH_SHORT).show();
                }

                public void onSwipeRight() {
                       Toast.makeText(Appl.getContext(), "right", Toast.LENGTH_SHORT).show();
                }

                public void onSwipeLeft() {
                       Toast.makeText(Appl.getContext(), "left", Toast.LENGTH_SHORT).show();
                }

                public void onSwipeBottom() {
                    //  Toast.makeText(Appl.getContext(), "bottom", Toast.LENGTH_SHORT).show();
                }

                public void onClick() {
                    adapterPos = getAdapterPosition();
                    Intent intent = new Intent(act, ContactInfoActivity.class);
                    intent.putExtra("clt_id", clt_id);
                    act.startActivityForResult(intent, Appl.EXIT_CODE_CLIENTINFO);
                    new PrefUtils().animateStart(act);

                }

                public void onLongClick() {
                }
            });
        }
    }





}
