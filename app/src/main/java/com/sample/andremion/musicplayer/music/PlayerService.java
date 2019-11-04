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

package com.sample.andremion.musicplayer.music;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.piterwilson.audio.MP3RadioStreamPlayer;

import java.io.IOException;

public class PlayerService extends Service {

    private static final String TAG = PlayerService.class.getSimpleName();
    private static final int DURATION = 335;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private Worker mWorker;

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mWorker != null) {
            mWorker.interrupt();
        }
        return super.onUnbind(intent);
    }

    public void play() {
        if (mWorker == null) {
            mWorker = new Worker();
            mWorker.start();
        } else {
            mWorker.doResume();
        }
    }

    public boolean isPlaying() {
        return mWorker != null && mWorker.isPlaying();
    }

    public void pause() {
        if (mWorker != null) {
            mWorker.doPause();
        }
    }

    public int getPosition() {
        if (mWorker != null) {
            return mWorker.getPosition();
        }
        return 0;
    }

    public int getDuration() {
        return DURATION;
    }

    private class Worker extends Thread {

        boolean paused = false;
        int position = 0;
        MP3RadioStreamPlayer player = new MP3RadioStreamPlayer();
        @Override
        public void run() {
            try {
//                player.setUrlString(PlayerService.this, true, "http://www.stephaniequinn.com/Music/Commercial%20DEMO%20-%2005.mp3");
                player.setUrlString("/storage/emulated/0/wwq/音乐/绿钻/英文/卢广仲 - 100种生活.mp3");
                try {
                    player.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                while (position < DURATION) {
                    sleep(1000);
                    if (!paused) {
                        position++;
                    }
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "Player unbounded");
            }
        }

        void doResume() {
            paused = false;
        }

        void doPause() {
            paused = true;
        }

        boolean isPlaying() {
            return !paused;
        }

        int getPosition() {
            return position;
        }
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public PlayerService getService() {
            // Return this instance of PlayerService so clients can call public methods
            return PlayerService.this;
        }
    }
}
