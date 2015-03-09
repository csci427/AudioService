package com.example.cobyg.lineintest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
/*
 * Add uses permissions to the xml file for the recording and playing back, see below:
 * <uses-permission android:name="android.permission.RECORD_AUDIO" ></uses-permission>
 * <uses-permission android:name="android.permission.modify_audio_settings"></uses-permission>
 */
//Thread to manage recording/playback of input from the device microphone
public class Audio extends Thread
{
    private boolean stopped = false;
    private String statusInfo = "";
    private AudioRecord recorder = null;
    private AudioTrack track = null;
    private short[] buffer = new short[160];

    //Give the thread high priority and start it.
    public Audio()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        start();
    }

    @Override
    public void run()
    {
        //Initialize buffer to hold recorded audio data, start recording, and start playback
        try
        {
            //smaller buffer size = more cpu usage but less latency
            //decrease the buffer size by change the N*x functions where N=smallest allowed buffer
            int N = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, N*5);//Arbitrarily set to N*10 in most examples
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, N*5, AudioTrack.MODE_STREAM);
            recorder.startRecording();
            track.play();
            /*
             * Loops until stopped boolean is set to true
             * Reads the data from the recorder and writes it to the audio track object for playback
             */
            while (!stopped) {
                statusInfo = "Running Audio Thread.";
                int n = recorder.read(buffer, 0, buffer.length);
                track.write(buffer, 0, n);
            }
        }
        catch(Throwable x)
        {
            statusInfo = "Error reading audio.";
        }
    }

    // Called from outside of the thread to stop the recording/playback loop
    public void close()
    {
        statusInfo = "Stopping Audio Loop.";
        stopped = true;
        //Frees the thread's resources after the loop completes so that it can be run again
        recorder.stop();
        recorder.release();
        track.stop();
        track.release();
    }
    public void open ()
    {
       statusInfo = "Restarting the Audio Loop";
        stopped = false;
        run();
    }

    public String getStatus(){
        return statusInfo;
    }

}