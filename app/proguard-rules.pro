# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in 1build.gradle.
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


#EventBus的混淆
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


 -keepattributes LineNumberTable,SourceFile

  -dontwarn org.apache.http.**
  -dontwarn android.net.http.AndroidHttpClient
  -dontwarn com.bugtags.library.vender.**

  #指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose
     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
    #保护注解
    -keepattributes *Annotation*
    # 保持哪些类不被混淆

    -keepattributes LineNumberTable,SourceFile

-keepattributes LineNumberTable,SourceFile

# ProGuard configurations for Bugtags
-keepattributes LineNumberTable,SourceFile

-keep class org.apache.http.** {*;}
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
# End Bugtags

#七牛
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**

#如果引用了v4或者v7包
-dontwarn android.support.**
   #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }



-keep public class * extends android.app.Activity      # 保持哪些类不被混淆

-keep class com.auto.djia.activity.** {*;}
-keep class com.auto.djia.fragment.** {*;}
    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable
    -keepnames class * implements android.os.Parcelable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

 #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
    #gson
    -keepattributes Signature
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.examples.android.model.** { *; }



-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses

-keep class **.R
-keep class **.R$* {
    <fields>;
}
#xutils混淆
-keep class com.lidroid.** { *; }
# universal-image-loader 混淆

-dontwarn com.nostra13.universalimageloader.**

-keep class com.nostra13.universalimageloader.** { *; }


#zxing
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}

-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
-keep class org.json.** { *; }


# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature -keepattributes Exceptions

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**

-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
-keep class org.json.** { *; }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-dontwarn com.kernal.**
-keep class com.kernal.** { *;}
-dontwarn kernal.**
-keep class kernal.** { *;}
-dontwarn com.wintone.**
-keep class com.wintone.** { *;}


#3D 地图 V5.0.0之前：
-dontwarn com.amap.api.maps.**
-keep   class com.amap.api.maps.**{*;}
-dontwarn  com.autonavi.amap.mapcore.**
-keep   class com.autonavi.amap.mapcore.*{*;}
-dontwarn   com.amap.api.trace.**
-keep   class com.amap.api.trace.**{*;}


#3D 地图 V5.0.0之后：
-dontwarn com.amap.api.maps.**
-keep   class com.amap.api.maps.**{*;}
-dontwarn   com.autonavi.**
-keep   class com.autonavi.**{*;}
-dontwarn   com.amap.api.trace.**
-keep   class com.amap.api.trace.**{*;}


#定位
-dontwarn  com.amap.api.location.**
-keep class com.amap.api.location.**{*;}
-dontwarn  com.amap.api.fence.**
-keep class com.amap.api.fence.**{*;}
-dontwarn com.autonavi.aps.amapapi.model.**
-keep class com.autonavi.aps.amapapi.model.**{*;}


#搜索
-dontwarn com.amap.api.services.**
-keep   class com.amap.api.services.**{*;}

#日历
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

#2D地图
#-keep class com.amap.api.maps2d.**{*;}
#-keep class com.amap.api.mapcore2d.**{*;}

#导航
#-keep class com.amap.api.navi.**{*;}
#-keep class com.autonavi.**{*;}


-dontwarn rx.internal.util.unsafe.*
-dontwarn io.bugtags.agent.instrumentation.*
-dontwarn com.zachary.library.uicomp.*
-dontwarn com.igexin.assist.control.*


#视频
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class com.youth.banner.** {
    *;
 }

 #环信
 -keep class com.hyphenate.** {*;}
 -dontwarn  com.hyphenate.**
 -dontwarn com.hyphenate.easeui.model.**
 -keep class com.hyphenate.easeui.model.**{*;}
 -keep class com.superrtc.** {*;}
 -dontwarn com.superrtc.**
 -keep class com.hyphenate.** {*;}
 -dontwarn  com.hyphenate.**
 -dontwarn com.hyphenate.easeui.model.**
 -keep class com.hyphenate.easeui.model.**{*;}
  -keep class com.superrtc.** {*;}
  -dontwarn com.superrtc.**
  -keep class internal.org.** {*;}
-dontwarn internal.org.**
-keep class org.apache.commons.lang.**{*;}
-keep class org.apache.** {*;}
-keep class com.superrtc.** {*;}
-keep class vi.com.gdi.bgl.** {*;}


  -keep public class * implements  com.bumptech.glide.module.GlideModule
  -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
  }
  -keep class com.bumptech.** {
      *;
  }




 -keep class cn.sharesdk.**{*;}
 -keep class com.sina.**{*;}
 -keep class **.R$* {*;}
 -keep class **.R{*;}
 -keep class com.mob.**{*;}
 -keep class m.framework.**{*;}
 -keep class com.bytedance.**{*;}
 -dontwarn cn.sharesdk.**
 -dontwarn com.sina.**
 -dontwarn com.mob.**
 -dontwarn **.R$*


 -keep class com.alibaba.sdk.android.oss.** { *; }
 -dontwarn okio.**
 -dontwarn org.apache.commons.codec.binary.**


 -keep public class * implements  com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
     **[] $VALUES;
     public *;
 }
 -keep class com.bumptech.** {
     *;
 }

 -keep class mapsdkvi.com.** {*;}

 -keep class androidx.appcompat.widget.** { *; }


 #小能
 -dontwarn solid.ren.skinlibrary.**
 -keep class solid.ren.skinlibrary.** { *; }
 -keep interface solid.ren.skinlibrary.** { *; }
 -keep public class * extends solid.ren.skinlibrary.**
 -dontwarn cn.ntalker.**
 -keep class cn.ntalker.** { *; }
 -dontwarn com.ntalker.**
 -keep class com.ntalker.** { *; }
 -dontwarn cn.xiaoneng.**
 -keep class cn.xiaoneng.** { *; }
 -dontwarn okhttp3.**
 -keep class okhttp3.**{*;}
 -dontwarn okio.**
 -keep class okio.**{*;}


 #EventBus
 -keepattributes *Annotation*
 -keepclassmembers class * {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }

 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }

-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }

  -keep class androidx.fragment.app.** {*;}



-keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**



# 银联SDK
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses
-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** {*;}
-keep class org.apache.commons.lang3.** {*; }
-keep class org.ksoap2.transport.** {*;}
-dontwarn org.ksoap2.transport.**
-keep class com.chinaums.pppay.net.action.** { *; }
-keep class com.chinaums.securitykeypad.** { *; }
-dontwarn com.chinaums.securitykeypad.**
-keep class com.chinaums.pppay.model.** { *; }
-keep class com.chinaums.pppay.view.** { *; }
-keep class com.chinaums.pppay.quickpay.service.** { *; }
-keep class com.chinaums.pppay.unify.** { *; }
-keep interface com.tencent.mm.opensdk.** { *; }
-keep class com.tencent.mm.opensdk.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.chinaums.pppay.util.** { *; }

-keep class com.chinaums.pppay.R$**{ *; }

-dontwarn com.unionpay.**
-keep class com.unionpay.** { *; }

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

#web view JavaScriptInterface of native function
-keepclassmembers class com.chinaums.pppay.quickpay.ScanCodePayWebView {
  public *;
}
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keep class com.chinaums.pppay.quickpay.JavaScriptPlugin {
  public <fields>;
  public <methods>;
}

-keep class com.chinaums.pppay.util.Common {
  public <fields>;
  public <methods>;
}


#支付宝SDK
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

#gifdrawable的混淆
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}

-keep class pl.droidsonroids.gif.sample.GifSelectorDrawable { *; }
-keepattributes Signature, LineNumberTable

#pdfView
-keep class com.shockwave.**

# 543--552 一键登录混淆
-ignorewarnings
# for proxy aar
-keep class com.out.proxy.secverify.** {*;}
# for CTCC
-keep class cn.com.chinatelecom.account.**{*;}
# for CUCC
-keep class com.sdk.**{*;}
# for CMCC
-keep class com.cmic.sso.sdk.**{*;}

# design包混淆
-keep class android.support.design.** { *; }
-keep class com.google.android.material.** { *; }

#腾讯Bugly的混淆
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#-------------------------蓝牙SDK混淆规则------------------------

-keep class sun.misc.** {*;}
-keep class sun.security.** {*;}
-dontwarn sun.security.**

-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** { *; }

-assumenosideeffects class android.util.Log {
#    public static *** d(...);
    public static *** e(...);
    public static *** i(...);
    public static *** w(...);
}

-keep class com.pateo.mobile.IVKeySDK.IVKeyCenter {*;}
-keep class com.pateo.mobile.IVKeySDK.IVKeyUser {*;}
-keep class com.pateo.mobile.IVKeySDK.IVKeyScanner {*;}
-keep class com.pateo.mobile.IVKeySDK.IVKeyDevice {*;}
-keep class com.pateo.mobile.IVKeySDK.IVKeyData {*;}
-keep class com.pateo.mobile.IVKeySDK.IVKeyCertificateConfig {*;}

#不混淆某个类
-keep public class com.pateo.mobile.IVKeySDK.IVKeyDeviceStatus { *; }

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

 # keep annotated by NotProguard
 -keep @com.pateo.mobile.IVKeySDK.NotProguard class * {*;}
 -keep class * {
 @com.pateo.mobile.IVKeySDK.NotProguard <fields>;
 }
  -keepclassmembers class * {
 @com.pateo.mobile.IVKeySDK.NotProguard <methods>;
 }



#——————拍照及选择相册避免混淆 否则点击崩溃———— start

-keep public class com.facebook.**
-keep public class com.cyue.multiple_images_selector.models.**


-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

#——————拍照及选择相册避免混淆 否则点击崩溃———— end


#一键登录 start
-ignorewarnings
# for proxy aar
-keep class com.out.proxy.yjyz.** {*;}
# for CTCC
-keep class cn.com.chinatelecom.account.**{*;}
# for CUCC
-keep class com.sdk.**{*;}
# for CMCC


-keep class com.cmic.sso.sdk.**{*;}

#一键登录end


-keep class com.lljjcoder.**{*;}
#修改成你对应的包名
-keep class com.car.eu5plus.library.bean.** {*;}

# x5webview
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}

#bugly的混淆
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}



#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep   class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}