/*
 * Copyright (c) 2016. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.andremion.musicplayer.wwq;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.music.MusicContent.MusicItem;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private final List<Music> mValues;

    public MusicAdapter(List<Music> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(holder.mItem.getName());
        holder.mArtistView.setText(holder.mItem.getArtist());

//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("G yyyy年MM月dd号 E a hh时mm分ss秒");
//        String format = now.format(pattern);
//        System.out.println(format);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("G yyyy年MM月dd号 E a hh时mm分ss秒");
//        String format = simpleDateFormat.format(new Date());
//        System.out.println(format);

        holder.mDurationView.setText(toTime(holder.mItem.getDuration()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nothing to do
            }
        });
        Bitmap bitmap = FileUtils.loadingCover(holder.mItem.getPath());
//                music.setBitmap(bitmap);

        holder.mCoverView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mCoverView;
        public final TextView mTitleView;
        public final TextView mArtistView;
        public final TextView mDurationView;
        public Music mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCoverView = (ImageView) view.findViewById(R.id.cover);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mArtistView = (TextView) view.findViewById(R.id.artist);
            mDurationView = (TextView) view.findViewById(R.id.duration);
        }
    }

    private String toTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }

}
