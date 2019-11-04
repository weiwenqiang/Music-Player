package com.sample.andremion.musicplayer.wwq;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 魏文强 on 2016/11/3.
 */
public class FileUtils {
    private static FileUtils mInstance;
    private static Context mContext;
    private static ContentResolver mContentResolver;
    private static Object mLock = new Object();

    public static FileUtils getInstance(Context context){
        if (mInstance == null){
            synchronized (mLock){
                if (mInstance == null){
                    mInstance = new FileUtils();
                    mContext = context;
                    mContentResolver = context.getContentResolver();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取本机音乐列表
     * @return
     */
    public List<Music> getMusics() {
        ArrayList<Music> musics = new ArrayList<>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径

                if (!FileUtils.isExists(path)) {
                    continue;
                }

                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); // 专辑
                String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); // 作者
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
                int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
                int time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Music music = new Music(name, path, album, artist, size, duration);



                musics.add(music);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return musics;
    }

    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static Bitmap loadingCover(String mediaUri) {
        Bitmap bitmap= null;
        try {
            MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mediaUri);
            byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
            bitmap = BitmapFactory.decodeByteArray(picture,0,picture.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
