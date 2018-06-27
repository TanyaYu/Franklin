package com.tanyayuferova.franklin.data.virtue

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.support.v4.content.CursorLoader
import com.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI
import javax.inject.Inject

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class VirtueRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val context: Context
) {
    fun query(uri: Uri,
              projection: Array<String>? = null,
              selection: String? = null,
              selectionArgs: Array<String>? = null,
              sortOrder: String? = null): Cursor {
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    fun insert(uri: Uri, values: ContentValues? = null) {
        contentResolver.insert(uri, values)
    }

    fun delete(uri: Uri, where: String? = null, selectionArgs: Array<String>? = null) {
        contentResolver.delete(uri, where, selectionArgs)
    }

    fun virtuesCursorLoader(
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ) = CursorLoader(
        context,
        CONTENT_VIRTUES_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )
}