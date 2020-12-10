package DialogFragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class AddEditDialogFragment extends DialogFragment {

    public static final String MESSAGE_KEY = "message_key";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setMessage(bundle.getString(MESSAGE_KEY))
                .setPositiveButton("OK",(dialog,id) -> { });
        return builder.create();
    }
}
