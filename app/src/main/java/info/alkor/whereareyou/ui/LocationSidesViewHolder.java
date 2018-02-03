package info.alkor.whereareyou.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import info.alkor.whereareyou.BR;
import info.alkor.whereareyou.R;
import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.model.LocationActionSide;

/**
 * Location side view holder.
 * Created by Marlena on 2018-01-23.
 */
class LocationSidesViewHolder extends RecyclerView.ViewHolder {

    private static final TextHelper TEXT_HELPER = new TextHelper();
    private final ViewDataBinding binding;

    LocationSidesViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    void bind(LocationActionSide side) {
        binding.setVariable(BR.side, new LocationSideViewModel(side));
        binding.executePendingBindings();
        super.itemView.setOnClickListener(new ClickHandler(side, binding.getRoot().getContext()));
        super.itemView.setOnLongClickListener(new LongClickHandler(side, binding.getRoot().getContext()));
    }

    private WhereAreYouContext getWhereAreYouContext() {
        return (WhereAreYouContext) binding.getRoot().getContext().getApplicationContext();
    }

    private class ClickHandler implements View.OnClickListener {

        private final LocationActionSide side;
        private final Context context;

        ClickHandler(LocationActionSide side, Context context) {
            this.side = side;
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setMessage
                    (context.getString(R.string.confirm_locate_phone, side.getName(), TEXT_HELPER.formatPhone(side.getPhone())))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getWhereAreYouContext()
                                    .getLocationQueryFlowManager()
                                    .sendLocationRequest(side.getPhone(), side.getName());
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
        }
    }

    private class LongClickHandler implements View.OnLongClickListener {

        private final LocationActionSide side;
        private final Context context;

        LongClickHandler(LocationActionSide side, Context context) {
            this.side = side;
            this.context = context;
        }

        @Override
        public boolean onLongClick(View view) {
            new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setMessage
                    (context.getString(R.string.confirm_delete_phone, side.getName(), TEXT_HELPER.formatPhone(side.getPhone())))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getWhereAreYouContext()
                                    .getUserDataAccess().removeUser(side);
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
            return true;
        }
    }
}
