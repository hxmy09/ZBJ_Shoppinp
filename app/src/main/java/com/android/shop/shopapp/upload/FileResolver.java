package com.android.shop.shopapp.upload;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class FileResolver
{

    private final ContentResolver contentResolver;

    public FileResolver(ContentResolver contentResolver)
    {
        this.contentResolver = contentResolver;
    }

    String getFilePath(Uri selectedImage)
    {
        if (selectedImage == null)
        {
            return null;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
        if (cursor == null)
        {
            return null;
        }

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }
}
