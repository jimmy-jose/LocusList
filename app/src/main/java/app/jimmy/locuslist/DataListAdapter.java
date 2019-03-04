package app.jimmy.locuslist;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import app.jimmy.locuslist.Models.DataModel;
import app.jimmy.locuslist.Utils.AppConstants;

/**
 * @author Jimmy
 * Created on 4/3/19.
 */
public class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = DataListAdapter.class.getSimpleName();

    List<DataModel> mDataset;
    private WeakReference<ItemClicks> listener;

    class ViewHolderPhoto extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView photo;
        private ImageView remove;
        public ViewHolderPhoto(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            photo = itemView.findViewById(R.id.photo);
            remove = itemView.findViewById(R.id.removeImage);
        }
    }

    class ViewHolderSingleChoice extends RecyclerView.ViewHolder {
        private TextView title;
        private RadioGroup radioGroup;
        public ViewHolderSingleChoice(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            radioGroup = itemView.findViewById(R.id.radio_group);
        }
    }

    class ViewHolderComment extends RecyclerView.ViewHolder {
        private Switch toggleButton;
        private EditText editComment;
        public EditTextListener editTextListener;
        public ViewHolderComment(View itemView, EditTextListener editTextListener) {
            super(itemView);
            this.editTextListener = editTextListener;
            toggleButton = itemView.findViewById(R.id.toggle_comment);
            editComment = itemView.findViewById(R.id.comment_edittext);
            editComment.addTextChangedListener(editTextListener);
        }
    }

    public DataListAdapter(List<DataModel> myDataset, WeakReference<ItemClicks> listener) {
        mDataset = myDataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        switch (viewType){
            case 0: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.photo_item, parent, false);
                return new ViewHolderPhoto(v);
            }
            case 1: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_choice_item, parent, false);
                return new ViewHolderSingleChoice(v);
            }
            case 2: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_item, parent, false);
                return new ViewHolderComment(v,new EditTextListener());
            }
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:{
                final ViewHolderPhoto viewHolderPhoto = (ViewHolderPhoto)holder;
                viewHolderPhoto.title.setText(mDataset.get(position).getTitle());
                if(!(mDataset.get(position).getSelectedValue()).equals("")) {
                    viewHolderPhoto.remove.setVisibility(View.VISIBLE);
                    viewHolderPhoto.photo.setImageURI(Uri.parse(mDataset.get(position).getSelectedValue()));
                    viewHolderPhoto.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else {
                    viewHolderPhoto.remove.setVisibility(View.GONE);
                    viewHolderPhoto.photo.setImageDrawable(ContextCompat.getDrawable(viewHolderPhoto.photo.getContext(),R.drawable.ic_camera_black_24dp));
                }
                viewHolderPhoto.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(mDataset.get(position).getSelectedValue()).equals("")) {
                            listener.get().photoClicked(true,position);
                        }else {
                            listener.get().photoClicked(false,position);
                        }
                    }
                });
                viewHolderPhoto.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataset.get(position).setSelectedValue("");
                        notifyDataSetChanged();
                    }
                });
                break;
            }
            case 1:{
                final ViewHolderSingleChoice viewHolderSingleChoice = (ViewHolderSingleChoice)holder;
                viewHolderSingleChoice.title.setText(mDataset.get(position).getTitle());
                viewHolderSingleChoice.radioGroup.removeAllViews();
                for(int i = 0; i < mDataset.get(position).getDataMap().getOptions().length; i++) {
                    RadioButton button = new RadioButton(((ViewHolderSingleChoice) holder).radioGroup.getContext());
                    button.setId(View.generateViewId());
                    button.setText(mDataset.get(position).getDataMap().getOptions()[i]);
                    viewHolderSingleChoice.radioGroup.addView(button);
                }
                viewHolderSingleChoice.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                        if(radioButton!=null)
                            mDataset.get(position).setSelectedValue(radioButton.getText().toString());
                    }
                });
                if(mDataset.get(position).getSelectedValue().equals("")){
                    viewHolderSingleChoice.radioGroup.clearCheck();
                }
                else {
                    int count = viewHolderSingleChoice.radioGroup.getChildCount();
                    for (int i=0;i<count;i++) {
                        View o = viewHolderSingleChoice.radioGroup.getChildAt(i);
                        if (o instanceof RadioButton){
                                if(((RadioButton) o).getText().equals(mDataset.get(position).getSelectedValue())) {
                                    ((RadioButton) o).setChecked(true);
                                }else {
                                    ((RadioButton) o).setChecked(false);
                                }
                        }
                    }
                }
                break;
            }
            case 2:{
                final ViewHolderComment viewHolderComment = (ViewHolderComment)holder;

                viewHolderComment.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!isChecked) {
                            mDataset.get(position).setToggled(false);
                            viewHolderComment.editComment.setVisibility(View.GONE);
                            viewHolderComment.editComment.setText("");

                        }else {
                            mDataset.get(position).setToggled(true);
                            viewHolderComment.editComment.setVisibility(View.VISIBLE);

                        }
                    }
                });

                viewHolderComment.editTextListener.updatePosition(holder.getAdapterPosition());

                if(mDataset.get(position).isToggled()){
                    viewHolderComment.toggleButton.setChecked(true);
                    viewHolderComment.editComment.setText(mDataset.get(position).getSelectedValue());
                }else {
                    viewHolderComment.toggleButton.setChecked(false);
                    viewHolderComment.editComment.setText("");
                }
                break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mDataset.get(position).getType()) {
            case AppConstants.DATATYPES.TYPE_PHOTO:
                return 0;
            case AppConstants.DATATYPES.TYPE_SINGLE_CHOICE:
                return 1;
            case AppConstants.DATATYPES.TYPE_COMMENT:
                return 2;
            default:
                Log.e(TAG, "getItemViewType: invalid type");
                return -1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ItemClicks{
        void photoClicked(boolean openFullScreen,int position);
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class EditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mDataset.get(position).setSelectedValue(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
