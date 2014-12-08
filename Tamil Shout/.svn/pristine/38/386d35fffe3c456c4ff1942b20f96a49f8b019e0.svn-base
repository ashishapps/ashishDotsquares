package com.tamilshout.image;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

public class ImageSelector
{

	public static final String DIALOG_OPTIONS[] = { "Choose from Gallery",
			"Capture a Photo", "Remove", "Cancel" };
	public static final int IMAGE_GALLARY = 1000;
	public static final int IMAGE_CAPTURE = 1001;

	public static void openChooser(final Activity act, final File file,
			final RemoveListener listener)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setTitle("Select Picture");
		builder.setItems(DIALOG_OPTIONS, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int position)
			{

				if (position == 0)
					openGallary(act);
				else if (position == 1)
					openCamera(act, file);
				else if (position == 2 && listener != null)
					listener.onRemove();

			}
		});
		builder.create().show();
	}

	public static void openGallary(Activity act)
	{

		Intent gallery = new Intent();
		gallery.setType("image/*");
		gallery.setAction(Intent.ACTION_GET_CONTENT);
		act.startActivityForResult(	Intent.createChooser(gallery, "Select Picture"), IMAGE_GALLARY);

	}

	public static void openCamera(Activity act, File file)
	{

		Intent camera = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		act.startActivityForResult(camera, IMAGE_CAPTURE);
	}

	public static String getImagePath(Activity act, Intent data)
	{

		try
		{
			Uri uri = data.getData();
			String[] projection = { MediaColumns.DATA };
			Cursor cursor = act.managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return null;
	}

	public interface RemoveListener
	{
		public void onRemove();
	}
}
