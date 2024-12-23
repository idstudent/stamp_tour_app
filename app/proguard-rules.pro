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

# 기본 설정
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# 프로젝트 특정 클래스들
-keep class com.ljystamp.stamp_tour_app.App { *; }
-keep class com.ljystamp.stamp_tour_app.MainActivity { *; }
-keep class com.ljystamp.stamp_tour_app.FilterClickListener { *; }

# API 관련 클래스
-keep class com.ljystamp.stamp_tour_app.api.** { *; }

# Database 관련 클래스
-keep class com.ljystamp.stamp_tour_app.db.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# DI (Hilt) 관련 클래스
-keep class com.ljystamp.stamp_tour_app.di.** { *; }

# Repository 클래스들
-keep class com.ljystamp.stamp_tour_app.repository.** { *; }

# ViewModel 클래스들
-keep class com.ljystamp.stamp_tour_app.viewmodel.** { *; }

# View 관련 클래스들
-keep class com.ljystamp.stamp_tour_app.view.** { *; }

# Util 클래스들
-keep class com.ljystamp.stamp_tour_app.util.** { *; }

# Room
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# Retrofit & OkHttp
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Hilt
-keep,allowobfuscation,allowshrinking class dagger.hilt.android.internal.managers.ViewComponentManager$ViewComponentBuilderEntryPoint
-keep,allowobfuscation,allowshrinking class dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager
-keep,allowobfuscation,allowshrinking class dagger.hilt.android.internal.managers.ViewComponentManager
-keep,allowobfuscation,allowshrinking class dagger.hilt.android.internal.managers.ViewComponentManager$ViewWithFragmentComponentBuilderEntryPoint
-keep,allowobfuscation,allowshrinking class dagger.hilt.internal.Preconditions
-keepnames class dagger.hilt.android.lifecycle.HiltViewModel
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Sandwich
-keep class com.skydoves.sandwich.** { *; }

# TedPermission
-keep class com.gun0912.tedpermission.** { *; }

# ViewBinding
-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** bind(android.view.View);
    public static *** inflate(android.view.LayoutInflater);
}

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Navigation Component
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class * extends androidx.fragment.app.Fragment{}

# Android의 기본 클래스들
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep class androidx.** { *; }

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }

# Paging
-keep class androidx.paging.** { *; }

# FlexBox
-keep class com.google.android.flexbox.** { *; }

# Dots Indicator
-keep class com.tbuonomo.viewpagerdotsindicator.** { *; }

# 만약 Parcelable 구현체가 있다면
-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Enum 클래스 보존
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Native methods 보존
-keepclasseswithmembernames class * {
    native <methods>;
}

# 애플리케이션 클래스
-keep public class * extends android.app.Application

# View constructors
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}