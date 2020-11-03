/********* locSdkPlugin.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface LocationSdk : CDVPlugin {
  // Member variables go here.
}

- (void)init:(CDVInvokedUrlCommand*)command;
- (void)start:(CDVInvokedUrlCommand*)command;
- (void)stop:(CDVInvokedUrlCommand*)command;

- (void)startUpdatePosition:(CDVInvokedUrlCommand*)command;
- (void)stopUpdatePosition:(CDVInvokedUrlCommand*)command;
- (void)showMap:(CDVInvokedUrlCommand*)command;
- (void)traceMap:(CDVInvokedUrlCommand*)command;
@end
