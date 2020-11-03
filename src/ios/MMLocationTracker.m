//
//  MMLocationTracker.m
//  sxyq
//
//  Created by yang yang on 2020/10/27.
//

#import <Foundation/Foundation.h>
#import "MMLocationTracker.h"
#import "MMLocationManager.h"
#import "ZTGCDTimerManager.h"

@implementation MMLocationTracker

+ (instancetype)sharedManager
{
    static MMLocationTracker *_sharedSingleton = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken,^{
        _sharedSingleton = [[self alloc] init];
    });
    return _sharedSingleton;
}

- (void)startTimer
{
    if(isStart){
        return;
    }
    isStart = YES;
    timerKey = @"LocationSDK";
    
    [[ZTGCDTimerManager sharedInstance] scheduleGCDTimerWithName:timerKey interval:10 queue:nil repeats:YES option:CancelPreviousTimerAction  action:^{
        
        [[MMLocationManager sharedManager] getLocation];
    }];
}

//- (void) restartLocationUpdates
//{
//    NSLog(@"restartLocationUpdates");
//
//    [[MMLocationManager sharedManager] getLocation];
//}

@end
