#include <jni.h>
#include <string>
#include<malloc.h>
#include<lame.h>
#include<android/log.h>
#define LOG_TAG "System.out.c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C"
{
/**
 * 返回值 char* 这个代表char数组的首地址
 *  Jstring2CStr 把java中的jstring的类型转化成一个c语言中的char 字符串
 */

char *Jstring2CStr(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;

    jclass ref = env->FindClass("java/lang/String");//String
    jstring    encode    = env->NewStringUTF("utf-8");
    jmethodID  methodId  = env->GetMethodID(ref, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray byteArray = (jbyteArray)env->CallObjectMethod(jstr, methodId, encode);
    jsize      strLen    = env->GetArrayLength(byteArray);
    jbyte      *jBuf     = env->GetByteArrayElements(byteArray, JNI_FALSE);
    if (*jBuf > 0)
    {
        rtn = (char*)malloc(strLen + 1);
        if (!rtn)
        {
            return NULL;
        }
        memcpy(rtn, jBuf, strLen);
        rtn[strLen] = 0;
    }
    env->DeleteLocalRef(ref);
    env->ReleaseByteArrayElements(byteArray, jBuf, 0);
    return rtn;


}

JNIEXPORT jstring JNICALL Java_com_cloud_lame_mp3lame_LameHelper_getVersion(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "ver 3.99.5";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT void JNICALL
Java_com_cloud_lame_mp3lame_LameHelper_wavTomp3(JNIEnv *env, jobject obj, jstring jwav,
                                                jstring jmp3,jint outSamplerate,jint outBrate,jint outChannel,jint mode,jint vbrMode) {
    char *cwav = Jstring2CStr(env, jwav);
    char *cmp3 = Jstring2CStr(env, jmp3);
    LOGI("wav = %s", cwav);
    LOGI("mp3 = %s", cmp3);
    remove(cmp3);
    //1.打开 wav,MP3文件
    FILE *fwav = fopen(cwav, "rb");
    FILE *fmp3 = fopen(cmp3, "wb");
    const int PCM_SIZE = 8192;
    const int MP3_SIZE = 8192;
    short int pcm_buffer[PCM_SIZE * 2];
    unsigned char mp3_buffer[MP3_SIZE];

    fseek(fwav, 4*1024, SEEK_CUR);                                   //skip file header


    //1.初始化lame的编码器
    lame_t lame = lame_init();
    //2. 设置lame mp3编码的采样率
    lame_set_in_samplerate(lame, outSamplerate);
    lame_set_brate(lame,outBrate);
    lame_set_mode(lame , (MPEG_mode)mode);
    lame_set_num_channels(lame, outChannel);
    // 3. 设置MP3的编码方式
    lame_set_VBR(lame, (vbr_mode_e)vbrMode);
    lame_init_params(lame);
    LOGI("lame init finish");
    int read;
    int write; //代表读了多少个次 和写了多少次
    int total = 0; // 当前读的wav文件的byte数目
    do {

        read = fread(pcm_buffer, sizeof(short int) * outChannel, PCM_SIZE, fwav);
        total += read * sizeof(short int) * outChannel;
        LOGI("converting ....%d", total);

        if (read == 0)

            write = lame_encode_flush(lame, mp3_buffer, MP3_SIZE);

        else
            write = lame_encode_buffer(lame,pcm_buffer,NULL,read,mp3_buffer,MP3_SIZE);
        //write = lame_encode_buffer_interleaved(lame, pcm_buffer, read, mp3_buffer, MP3_SIZE);

        fwrite(mp3_buffer, write, 1, fmp3);

    } while (read != 0);
    LOGI("convert  finish");

    lame_close(lame);
    fclose(fwav);
    fclose(fmp3);
}
}
