package com.cloud.lame.mp3lame;

/**
 * Created by cloud on 2017/10/13.
 */

public class LameHelper {
    public enum MPEG_mode_e {
        STEREO,
        JOINT_STEREO,
        DUAL_CHANNEL,   /* LAME doesn't supports this! */
        MONO,
        NOT_SET,
        MAX_INDICATOR   /* Don't use this! It's used for sanity checks. */
    };

    public enum vbr_mode_e {
        vbr_off,
        vbr_mt,               /* obsolete, same as vbr_mtrh */
        vbr_rh,
        vbr_abr,
        vbr_mtrh,
        vbr_max_indicator    /* Don't use this! It's used for sanity checks.       */
    };

    private LameHelper() {

    }

    private int outSamplerate = 16000;

    private int outBrate = 16;

    private int outChannel = 1;

    private MPEG_mode_e outMode = MPEG_mode_e.MONO;

    private vbr_mode_e outVbrMode = vbr_mode_e.vbr_mtrh;

    private static LameHelper single=null;
    public static synchronized LameHelper getInstance(){
        if(single == null)
            single = new LameHelper();
        return single;
    }

    static {
        System.loadLibrary("mp3lame");
    }

    public String lameVersion(){
        String version = getVersion();
        return version;
    }

    public  void setOutSamplerate(int samplerate)
    {
        outSamplerate = samplerate;
    }

    public  void setOutBrate(int brate)
    {
        outBrate = brate;
    }

    public  void setOutChannel(int channel)
    {
        outChannel = channel;
    }

    public void setOutMode(MPEG_mode_e mode)
    {
        outMode = mode;
    }

    public void setOutVbrMode(vbr_mode_e vbrMode)
    {
        outVbrMode = vbrMode;
    }

    public void convertmp3(String wav,String mp3)
    {
        wavTomp3(wav,mp3,outSamplerate,outBrate,outChannel,outMode.ordinal(),outVbrMode.ordinal());
    }

    public native void wavTomp3(String wav,String mp3,int samplerate,int brate,int channel,int mode,int vbrMode);
    public native String getVersion();


}
