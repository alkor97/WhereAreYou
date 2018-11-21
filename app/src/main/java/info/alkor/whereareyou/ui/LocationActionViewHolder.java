package info.alkor.whereareyou.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import info.alkor.whereareyou.BR;
import info.alkor.whereareyou.R;
import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.location.minimal.MinimalLocationFormatter;
import info.alkor.whereareyou.model.LocationAction;
import info.alkor.whereareyou.settings.Private;

/**
 * Location action view holder.
 * Created by Marlena on 2018-01-28.
 */
class LocationActionViewHolder extends RecyclerView.ViewHolder {

    private static final MinimalLocationFormatter FORMATTER = new MinimalLocationFormatter();
    private static final TextHelper TEXT_HELPER = new TextHelper();

    private final ViewDataBinding binding;

    LocationActionViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    void bind(LocationAction action) {
        binding.setVariable(BR.action, new LocationActionViewModel(itemView.getContext(), action));
        binding.executePendingBindings();
        if (action.getLocation() != null) {
            super.itemView.setOnClickListener(new ClickHandler(action, itemView.getContext()));
        }
        super.itemView.setOnLongClickListener(new LongClickHandler(action, itemView.getContext()));
    }

    private WhereAreYouContext getWhereAreYouContext() {
        return (WhereAreYouContext) binding.getRoot().getContext().getApplicationContext();
    }

    private class ClickHandler implements View.OnClickListener {

        private final Context context;
        private Intent intent;

        ClickHandler(LocationAction model, Context context) {
            this.context = context;
            String template = context.getString(R.string.location_presenter_url,
                    FORMATTER.format(model.getLocation()),
                    TEXT_HELPER.encode(model.getPhoneNumber()),
                    TEXT_HELPER.encode(model.getDisplayName()),
                    Private.GOOGLE_API_KEY);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(template));
        }

        @Override
        public void onClick(View view) {
            context.startActivity(intent);
        }
    }

    private class LongClickHandler implements View.OnLongClickListener {

        private final LocationAction action;
        private final Context context;

        LongClickHandler(LocationAction side, Context context) {
            this.action = side;
            this.context = context;
        }

        @Override
        public boolean onLongClick(View view) {
            new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setMessage
                    (context.getString(R.string.confirm_delete_action))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getWhereAreYouContext()
                                    .getActionDataAccess().deleteAction(action);
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
            return true;
        }
    }
}
