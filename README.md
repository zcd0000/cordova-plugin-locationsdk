# Cordova Plugin LocationSdk

LocationSdk Plugin is a wrapper for locationsdk-1.0.0.aar developed by China Academy of Transportation Science(http://wlhy.mot.gov.cn/wlhy/detail.html?newsId=140) (for Android).


## Clone the plugin

    $ git clone https://github.com/marcozabo/cordova-plugin-scaffold.git

Modify or add what you need to create your custom plugin

## Usage
downolad the entire plugin folder and install the plugin on your own project using the following command:
```
   $ cordova plugin add 
```

then from your html page just use JS code to call:

```
   Scaffold.customaction("custom message",null, null)
```   



## Sample plugin directory structure

```
foo-plugin/
|- plugin.xml     # xml-based manifest
|- src/           # native source for each platform
|  |- android/
|  |  `- Foo.java
|  `- ios/
|     |- CDVFoo.h
|     `- CDVFoo.m
|- README.md
`- www/
   |- foo.js
   `- foo.png
```
    

### References

```
https://cordova.apache.org/docs/en/latest/plugin_ref/spec.html
http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html
```