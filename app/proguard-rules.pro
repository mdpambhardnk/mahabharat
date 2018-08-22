# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#-libraryjars D:/Android/sdkapi23/sdkapi23/platforms/android-21/android.jar
#-libraryjars D:/Android/sdkapi23/sdkapi23/tools/lib/gson-2.3.jar
-libraryjars D:/sdk/platforms/android-21/android.jar
-libraryjars D:/sdk/tools/lib/gson-2.3.jar
-dontwarn org.xmlpull.v1.**
-dontwarn org.apache.http.**
-ignorewarnings
-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }
-keep class com.google.gson{ *; }
-keep public class com.jorjoto.mahabharat.MyApplication
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keepattributes *Annotation*
-keep class com.google.gson.stream.** { *; }
-keep class com.jorjoto.mahabharat.model.** { <fields>; }

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep class org.json.**{ *; }
-dontpreverify
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
-dontwarn org.xmlpull.v1.**
-dontwarn android.support.v4.**
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class com.google.**{
    *;
}
-ignorewarnings
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}