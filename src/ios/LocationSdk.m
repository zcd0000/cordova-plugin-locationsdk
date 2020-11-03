#import <Cordova/CDV.h>
#import <MapManager/MapManager.h>
#import <AMapLocationKit/AMapLocationKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MJExtension/MJExtension.h>

#import "LocationSdk.h"
#import "TraceViewController.h"
#import "MapViewController.h"
#import "MMLocationTracker.h"
#import "MMLocationManager.h"
#import "AMapRouteInfo.h"
#import "AMapVehicleLocationInfo.h"
#import "UpdatePositionOptions.h"
#import "UtilsForLocationsdk.h"

NSString* const WLHY_APP_SECURITY = @"wlhyAppSecurity";
NSString* const ENTERPRISE_SENDER_CODE = @"enterpriseSenderCode";
NSString* const AMAP_KEY = @"amap_apikey";
NSString* const IS_DEBUG = @"isDebug"; //我们需要在ionic主目录下的config文件platform为ios的地方添加name为isDebug的preference用以判断是不是启动的debug模式
NSString* const BUILD_TYPE_DEBUG = @"debug";
NSString* const BUILD_TYPE_RELEASE = @"release";
NSString* const CODE_NAME = @"code";
NSString* const MESSAGE_NAME = @"message";

typedef enum  {
    OpenSucceed = 100007,//服务开启成功
    StartSucceed = 100013, //成功开启上传定位
    StopSucceed = 100014,//成功停止上传定位
    NeedAllowAlways = 100010//未始终允许定位，请到设置中开启
}ResultCode;


@implementation LocationSdk

- (void)init:(CDVInvokedUrlCommand*)command{
    
    CDVPluginResult* pluginResult = nil;
    
    NSString* appId = [[NSBundle mainBundle]bundleIdentifier];
    NSString* appSecurity = [self.commandDelegate.settings objectForKey:[WLHY_APP_SECURITY lowercaseString]];
    NSString* enterpriseSenderCode = [self.commandDelegate.settings objectForKey:[ENTERPRISE_SENDER_CODE lowercaseString]];
    NSString* aMapKey = [self.commandDelegate.settings objectForKey:AMAP_KEY];
    NSString* environment = [self decideDebugOrRelease];;
    
    NSLog(@"appId:%@, appSecurity:%@, enterpriseSenderCode:%@, environment: %@",appId,appSecurity,enterpriseSenderCode,environment);
    
    [AMapServices sharedServices].apiKey = aMapKey;
    if([self isEmpty:appSecurity]){
        NSString* errMsg = @"Invalid appSecurity: null";
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errMsg];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
    
    if([self isEmpty:enterpriseSenderCode]){
        NSString* errMsg = @"Invalid enterpriseSenderCode: null";
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errMsg];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }

    enterpriseSenderCode = [enterpriseSenderCode substringFromIndex:4];
    
    MapService* service = [MapService new];
    [service openServiceWithAppId:appId appSecurity:appSecurity enterpriseSenderCode:enterpriseSenderCode environment:environment listener:^(id model,NSError* error){
        
        NSString * code = [model objectForKey:CODE_NAME];
        NSString * message = [self convertUTF8:[model objectForKey:MESSAGE_NAME]];
        
       
        NSString * str = [NSString stringWithFormat:@"open方法生成的model为：%@,error为：%@",model,error];
        NSLog(str);

        CDVPluginResult* pluginResultFinal;
        if([code isEqualToString:[NSString stringWithFormat: @"%ld", (long)OpenSucceed]]){
            // 开启定时任务上传位置信息
//           NSDictionary* sessionInfoDic = [command.arguments objectAtIndex:0];
//           NSDictionary* optionsDic = [command.arguments objectAtIndex:1];
//
//           // 添加自定义功能
//            MMLocationManager *locationManager = [MMLocationManager sharedManager];
//            __weak MMLocationManager *tempManager = locationManager;
//            locationManager.onReceiveLocation = ^(CLLocation * location){
//                // 处理获取到的位置信息
//                CLLocationCoordinate2D temp = location.coordinate;
//                NSLog(@"执行onReceiveLocation,distanceFilter:%f,latitude:%f,longitude:%f",[tempManager distanceFilter],temp.latitude,temp.longitude);
//
//            };
//            MMLocationTracker *locationTracker = [MMLocationTracker sharedManager];
//            [locationTracker startTimer];
            
           pluginResultFinal = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
        }else{

           pluginResultFinal = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        }
        [self.commandDelegate sendPluginResult:pluginResultFinal callbackId:command.callbackId];
        
    }];
}

     
- (void)start:(CDVInvokedUrlCommand*)command{
    [self locationSdkOperation:YES andCommand:command];
}
- (void)stop:(CDVInvokedUrlCommand*)command{
    [self locationSdkOperation:NO andCommand:command];
}

- (void)startUpdatePosition:(CDVInvokedUrlCommand*)command{
    
}
- (void)stopUpdatePosition:(CDVInvokedUrlCommand*)command{
    
}
- (void)showMap:(CDVInvokedUrlCommand*)command{
    NSDictionary* vehicleLocationInfo = [command.arguments objectAtIndex:0];
    NSString* title = [command.arguments objectAtIndex:1];
    
    AMapVehicleLocationInfo *locationInfo = [AMapVehicleLocationInfo mj_objectWithKeyValues:vehicleLocationInfo];
    
    MapViewController *vc = [[MapViewController alloc] initWithInfo:locationInfo traceTitle:title];
    vc.modalPresentationStyle = UIModalPresentationFullScreen;
    [self.viewController presentViewController:vc animated:YES completion:nil];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
- (void)traceMap:(CDVInvokedUrlCommand*)command{
    NSDictionary* routeInfoDic = [command.arguments objectAtIndex:0];
    NSString* title = [command.arguments objectAtIndex:1];
    
    AMapRouteInfo *routeInfo = [AMapRouteInfo mj_objectWithKeyValues:routeInfoDic];
    
    TraceViewController *vc = [[TraceViewController alloc] initWithInfo:routeInfo traceTitle:title];
    vc.modalPresentationStyle = UIModalPresentationFullScreen;
    [self.viewController presentViewController:vc animated:YES completion:nil];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

//私有帮助方法
//私有帮助方法
 - (NSString*)convertUTF8:(NSString*)message{
     if(message == nil)
     {
         return message;
     }
     NSData* tempData = [message dataUsingEncoding:NSUTF8StringEncoding];
     message = [[NSString alloc] initWithData:tempData encoding:NSUTF8StringEncoding];
     return message;
 }


- (void)locationSdkOperation:(BOOL)isStart andCommand:(CDVInvokedUrlCommand*)command {

    NSArray* noteInfosArr = [command.arguments objectAtIndex:0];
    
    if(noteInfosArr == nil || [noteInfosArr count] == 0){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:SWIFT_CDVCommandStatus_ERROR messageAsString:@"运单信息不能为空！"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
    
    MapService* service = [MapService new];
    
    if(isStart){

        [service startLocationWithShippingNoteInfos:noteInfosArr listener:^(id model,NSError* error){
            
            [self buildResult:YES andModel:model andError:error andCommand:command];
            
        }];
    }else{

        [service stopLocationWithShippingNoteInfos:noteInfosArr listener:^(id model,NSError* error){
            
            [self buildResult:NO andModel:model andError:error andCommand:command];
            
        }];
    }
    
}

- (void)buildResult:(BOOL)isStart andModel:(id)model andError:(NSError*)error andCommand:(CDVInvokedUrlCommand*)command{
    
    ResultCode succeedCode = StartSucceed;
    if(!isStart){
        succeedCode = StopSucceed;
    }
    
    NSString * code = [model objectForKey:CODE_NAME];
    NSString * message = [self convertUTF8:[model objectForKey:MESSAGE_NAME]];
    
    NSString * str = [NSString stringWithFormat:@"start方法生成的model为：%@,error为：%@",model,error];
    NSLog(str);
    
    CDVPluginResult* pluginResult;
    if([code isEqualToString:[NSString stringWithFormat: @"%ld", (long)succeedCode]]){
        
       pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
        
    }else{
        
       pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (NSString*)decideDebugOrRelease{
    NSString* model = [self.commandDelegate.settings objectForKey:[IS_DEBUG lowercaseString]];
    if([self isEmpty:model]|| ![[model lowercaseString] isEqualToString:@"false"]){
        return BUILD_TYPE_DEBUG;
    }
    return BUILD_TYPE_RELEASE;
}

- (BOOL) isEmpty:(id)str {
    return str == nil
    || [str isEqualToString:@""]
    || [str isKindOfClass:[NSNull class]]
    || ([str respondsToSelector:@selector(length)]
        && [(NSData *)str length] == 0)
    || ([str respondsToSelector:@selector(count)]
        && [(NSArray *)str count] == 0);
}


@end

