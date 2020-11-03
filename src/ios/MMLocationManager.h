//
//  MMLocationManager.h
//  sxyq
//
//  Created by yang yang on 2020/10/25.
//
#import <UIKit/UIKit.h>
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>

#define IS_OS_8_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)

@interface MMLocationManager : CLLocationManager

+ (instancetype)sharedManager;

@property (nonatomic,copy) void (^onReceiveLocation)(CLLocation*);

@property (nonatomic, assign) CGFloat minSpeed; //最小速度

@property (nonatomic, assign) CGFloat minFilter; //最小范围

@property (nonatomic, assign) CGFloat minInteval; //更新间隔

- (void) getLocation;

@end
