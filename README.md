# Cordova Plugin LocationSdk

LocationSdk Plugin is a wrapper for locationsdk-1.0.0.aar developed by China Academy of Transportation Science(http://wlhy.mot.gov.cn/wlhy/detail.html?newsId=140) (for Android).

## Quick Install
downolad the entire plugin folder and install the plugin on your own project using the following command:
npm:
```
   $ cordova plugin add cordova-plugin-locationsdk --variable API_KEY_FOR_ANDROID="YOUR_AMAP_ANDROID_API_KEY_IS_HERE" --variable API_KEY_FOR_IOS="YOUR_AMAP_IOS_API_KEY_IS_HERE"
```

## Usage
then from your html page just use JS code to call:

```
   locationSdk.init(onSuccess, onError);
```   
