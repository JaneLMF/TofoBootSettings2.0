package com.txbox.settings.dialog;


import com.txbox.txsdk.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog
{
	private static final String TAG = "PIVOS";

	public static CustomProgressDialog create(Context context, boolean cancellable)
	{
		return create(context, null, null, false, cancellable, null);
	}

	public static CustomProgressDialog create(Context context, CharSequence title,
			CharSequence message, boolean indeterminate)
	{
		return create(context, title, message, indeterminate, false, null);
	}

	public static CustomProgressDialog create(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable)
	{
		return create(context, title, message, indeterminate, cancelable, null);
	}

	public static CustomProgressDialog create(Context context, CharSequence title,
			CharSequence message, boolean indeterminate,
			boolean cancelable, OnCancelListener cancelListener)
	{
		CustomProgressDialog dialog = new CustomProgressDialog(context);

		dialog.setTitle(title);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);

		return dialog;
	}

	private TextView message;
	private ProgressBar progress;

	public CustomProgressDialog(Context context)
	{
		// The magic is all in the style!
		super(context, R.style.custom_progress_dialog);

		/* The next line will add the ProgressBar to the dialog. */
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.custom_progress_dialog, null);
		progress = (ProgressBar)view.findViewById(R.id.custom_preference_dialog_progress_bar);
        message = (TextView)view.findViewById(R.id.custom_preference_dialog_message);
		setContentView(view);
	}

	public void setMessage(CharSequence msg)
	{
		message.setText(msg);
	}
}
