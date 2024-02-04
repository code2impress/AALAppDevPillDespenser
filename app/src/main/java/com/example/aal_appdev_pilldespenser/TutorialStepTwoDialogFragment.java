package com.example.aal_appdev_pilldespenser;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.Nullable;


public class TutorialStepTwoDialogFragment extends DialogFragment {

    public interface TutorialStepTwoListener {
        void onTutorialStepTwoCompleted();
    }

    private TutorialStepTwoListener listener;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                // Determine the size of the dialog to be a square based on the smaller screen dimension
                int size = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
                window.setLayout(size, size); // Apply the square layout
                window.setGravity(Gravity.CENTER); // Center the dialog on screen
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TutorialStepTwoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TutorialStepTwoListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // NOTE: Pass null as the parent view because it's going in the dialog layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_tutorial_step_two_dialog, null);
        builder.setView(view)
                .setPositiveButton("Done", (dialog, id) -> listener.onTutorialStepTwoCompleted());

        // You might have an image view or any other element to explain the second step
        // ImageView imageView = view.findViewById(R.id.yourImageViewId);
        // Set up the image or content for your tutorial step here

        // Optional: If you have a custom button in your layout, you can set its onClickListener here
        // Button doneButton = view.findViewById(R.id.done_button);
        // doneButton.setOnClickListener(v -> listener.onTutorialStepTwoCompleted());

        return builder.create();
    }
}
