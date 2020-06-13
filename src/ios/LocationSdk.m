#import <Cordova/CDV.h>
#import <MapManager/MapManager.h>
#import <AMapLocationKit/AMapLocationKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <CoreLocation/CoreLocation.h>
#import "LocationSdk.h"

NSString* const WLHY_APP_SECURITY = @"wlhyAppSecurity";
NSString* const ENTERPRISE_SENDER_CODE = @"enterpriseSenderCode";
NSString* const AMAP_KEY = @"amap_apikey";
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
    NSString* appSecurity = [self.commandDelegate.settings objectForKey:[ENTERPRISE_SENDER_CODE lowercaseString]];
    NSString* enterpriseSenderCode = [self.commandDelegate.settings objectForKey:[WLHY_APP_SECURITY lowercaseString]];
    NSString* aMapKey = [self.commandDelegate.settings objectForKey:AMAP_KEY];
    NSString* environment = BUILD_TYPE_DEBUG;//后面需要根据环境变量获取
    
    NSLog(@"appId:%@, appSecurity:%@, enterpriseSenderCode:%@, environment: %@",appId,appSecurity,enterpriseSenderCode,environment);
    
    [AMapServices sharedServices].apiKey = aMapKey;
    if(appSecurity == nil || [appSecurity isEqualToString:@""]){
        NSString* errMsg = @"Invalid appSecurity: null";
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errMsg];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
    
    if(enterpriseSenderCode == nil || [enterpriseSenderCode isEqualToString:@""]){
        NSString* errMsg = @"Invalid enterpriseSenderCode: null";
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errMsg];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
    
    MapService* service = [MapService new];
    [service openServiceWithAppId:appId appSecurity:appSecurity enterpriseSenderCode:enterpriseSenderCode environment:environment listener:^(id model,NSError* error){
        
        NSString* modelStr = [self DataTOjsonString:model];
        NSString * str = [NSString stringWithFormat:@"open方法生成的model为：%@,error为：%@",modelStr,error];
        NSLog(str);
        
        NSString * code = [model objectForKey:CODE_NAME];
        NSString * message = [self DataTOjsonString:[model objectForKey:MESSAGE_NAME]];
        
        if([code isEqualToString:[NSString stringWithFormat: @"%ld", (long)OpenSucceed]]){

           CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
        }else{

           CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        
//        [self alertDialog:str andIsError:NO];
    }];
}
     
- (void)start:(CDVInvokedUrlCommand*)command{
    [self locationSdkOperation:YES andCommand:command];
}
- (void)stop:(CDVInvokedUrlCommand*)command{
    [self locationSdkOperation:NO andCommand:command];
}

//私有帮助方法
 - (NSString*)DataTOjsonString:(id)object{
     NSString *jsonString = nil;
     NSError *error;
     NSData *jsonData = [NSJSONSerialization dataWithJSONObject:object
                                                        options:NSJSONWritingPrettyPrinted
                                                          error:&error];
     if (!jsonData) {
         return nil;
     } else {
         jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
     }
     return jsonString;
 }

- (void)locationSdkOperation:(BOOL)isStart andCommand:(CDVInvokedUrlCommand*)command {

    NSString* noteInfosJson = [command.arguments objectAtIndex:0];
    NSData *jsonData = [noteInfosJson dataUsingEncoding:NSUTF8StringEncoding];
    NSArray *noteInfosArr = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:nil];
    //假数据
//    NSArray *noteInfosArr = [NSArray arrayWithObject:@{@"shippingNoteNumber":@"4560987",@"serialNumber":@"1245790",@"startCountrySubdivisionCode":@"320571000000",@"endCountrySubdivisionCode":@"320506405000"}];
    
    MapService* service = [MapService new];
    
    if(isStart){

        [service startLocationWithShippingNoteInfos:noteInfosArr listener:^(id model,NSError* error){
            NSString* modelStr = [self DataTOjsonString:model];
            NSString * str = [NSString stringWithFormat:@"start方法生成的model为：%@,error为：%@",modelStr,error];
            NSLog(str);
            
            NSString * code = [model objectForKey:CODE_NAME];
            NSString * message = [self DataTOjsonString:[model objectForKey:MESSAGE_NAME]];
            
            CDVPluginResult* pluginResult;
            if([code isEqualToString:[NSString stringWithFormat: @"%ld", (long)StartSucceed]]){
                
               pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
                
            }else{
                
               pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
                
            }
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            
//            [self alertDialog:str andIsError:NO];
        }];
    }else{

        [service stopLocationWithShippingNoteInfos:noteInfosArr listener:^(id model,NSError* error){
            NSString* modelStr = [self DataTOjsonString:model];
            NSString * str = [NSString stringWithFormat:@"stop方法生成的model为：%@,error为：%@",modelStr,error];
            NSLog(str);
            
            NSString * code = [model objectForKey:CODE_NAME];
            NSString * message = [self DataTOjsonString:[model objectForKey:MESSAGE_NAME]];
            
            CDVPluginResult* pluginResult;
            if([code isEqualToString:[NSString stringWithFormat: @"%ld", (long)StopSucceed]]){

               pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
            }else{

               pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
            }
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }];
    }
    
}


@end

