
#include <jni.h>
#include <string>
#include <vector>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_astarconcepts_brandbanao_api_ApiClient_baseUrlFromJNI(JNIEnv *env, jclass clazz) {

    std::string baseURL = "https://brandboost.astarconcepts.com/";
    return env->NewStringUTF(baseURL.c_str());

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_astarconcepts_brandbanao_api_ApiClient_getFilterComplex(JNIEnv *env, jclass clazz) {

    std::string filterComplex = "[1][0]scale2ref=w=oh*mdar:h=ih*0.05[logo][video];[video][logo]overlay=W-w-10:10";
    return env->NewStringUTF(filterComplex.c_str());

}


std::string removeUnicodeEscapes(const std::string &input) {
    std::string cleanedString;
    std::size_t index = 0;
    std::size_t length = input.length();

    while (index < length) {
        char currentChar = input[index];

        if (currentChar == '\\' && index + 1 < length && input[index + 1] == 'u') {
            // Skip the Unicode escape sequence
            index += 6;
        } else {
            cleanedString += currentChar;
            index++;
        }
    }

    return cleanedString;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_astarconcepts_brandbanao_api_ApiClient_getCommands2(JNIEnv *env, jclass clazz,
                                                             jstring temp_video_path,
                                                             jstring watermark_path,
                                                             jboolean is_watermark_enabled,
                                                             jstring music_path,
                                                             jboolean is_mute,
                                                             jstring output_path) {
    // Rest of the code remains the same...


    std::vector<std::string> ffmppgrender;


    const char *tempVideoPath = env->GetStringUTFChars(temp_video_path, nullptr);
    const char *watermarkPath = env->GetStringUTFChars(watermark_path, nullptr);
    const char *musicPath = env->GetStringUTFChars(music_path, nullptr);
    const char *outputPath = env->GetStringUTFChars(output_path, nullptr);

    //overlay
    ffmppgrender.push_back("-y");
    ffmppgrender.push_back("-i");
    ffmppgrender.push_back(tempVideoPath);

    //watermark
    if (is_watermark_enabled) {
        ffmppgrender.push_back("-i");
        ffmppgrender.push_back(watermarkPath);
    }

    if (musicPath != nullptr && musicPath[0] != '\0' && !is_mute) {
        ffmppgrender.push_back("-i");
        ffmppgrender.push_back(musicPath);
        ffmppgrender.push_back("-c:v");
        ffmppgrender.push_back("copy");
        ffmppgrender.push_back("-strict");
        ffmppgrender.push_back("experimental");
    }

    if (is_watermark_enabled) {
        jmethodID getFilterComplexMethod = env->GetStaticMethodID(clazz, "getFilterComplex",
                                                                  "()Ljava/lang/String;");
        jstring filterComplexString = (jstring) env->CallStaticObjectMethod(clazz,
                                                                            getFilterComplexMethod);
        const char *filterComplex = env->GetStringUTFChars(filterComplexString, nullptr);
        ffmppgrender.push_back("-filter_complex");
        ffmppgrender.push_back(filterComplex);
        env->ReleaseStringUTFChars(filterComplexString, filterComplex);
    }
    if (musicPath != nullptr && musicPath[0] != '\0' && !is_mute) {
        ffmppgrender.push_back("-map");
        ffmppgrender.push_back("0:v:0");
        ffmppgrender.push_back("-map");

        if (is_watermark_enabled) {
            ffmppgrender.push_back("2:a:0");
        } else {
            ffmppgrender.push_back("1:a:0");
        }
    }

    ffmppgrender.push_back("-s");
    ffmppgrender.push_back("720x1280");
    ffmppgrender.push_back("-r");
    ffmppgrender.push_back("60");
    ffmppgrender.push_back("-b");
    ffmppgrender.push_back("15496k");
    ffmppgrender.push_back("-vcodec");
    ffmppgrender.push_back("mpeg4");
    ffmppgrender.push_back("-ab");
    ffmppgrender.push_back("48000");
    ffmppgrender.push_back("-ac");
    ffmppgrender.push_back("2");
    ffmppgrender.push_back("-ar");
    ffmppgrender.push_back("22050");
    ffmppgrender.push_back("-shortest");
    ffmppgrender.push_back("-preset");
    ffmppgrender.push_back("ultrafast");
    ffmppgrender.push_back("-crf");
    ffmppgrender.push_back("15");
    ffmppgrender.push_back(outputPath);

    env->ReleaseStringUTFChars(temp_video_path, tempVideoPath);
    env->ReleaseStringUTFChars(watermark_path, watermarkPath);
    env->ReleaseStringUTFChars(music_path, musicPath);
    env->ReleaseStringUTFChars(output_path, outputPath);

    // Convert vector<string> to Java ArrayList<String>
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jobject arrayListObject = env->NewObject(arrayListClass, arrayListConstructor);


    for (const auto &command: ffmppgrender) {
        // Remove Unicode escape sequences from the command string
        std::string cleanedCommand = removeUnicodeEscapes(command);

        jstring commandString = env->NewStringUTF(cleanedCommand.c_str());
        env->CallBooleanMethod(arrayListObject, arrayListAdd, commandString);
        env->DeleteLocalRef(commandString);
    }

    return arrayListObject;
}

