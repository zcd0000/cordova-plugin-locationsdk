//
//  MMLocationManager.m
//  sxyq
//
//  Created by yang yang on 2020/10/25.
//

#import <UIKit/UIKit.h>
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import "MMLocationManager.h"



@interface MMLocationManager() <CLLocationManagerDelegate>

@end

@implementation MMLocationManager

+ (instancetype)sharedManager
{
    static MMLocationManager *_sharedSingleton = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken,^{
        _sharedSingleton = [[self alloc] init];
    });
    return _sharedSingleton;
}

- (instancetype)init
{
    self = [super init];
    if ( self )
    {
        self.minSpeed = 3;
        self.minFilter = 50;
        self.minInteval = 10;
        
        self.delegate = self;
        self.distanceFilter  = self.minFilter;
        self.desiredAccuracy = kCLLocationAccuracyBest;
        
        self.pausesLocationUpdatesAutomatically = NO;
        if ([self respondsToSelector:@selector(setAllowsBackgroundLocationUpdates:)]) {
            self.allowsBackgroundLocationUpdates = YES;
        }
        
    }
    return self;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations
{
    CLLocation *location = [locations lastObject];
    [self adjustDistanceFilter:location];
    if(!self.onReceiveLocation){
        NSException *ex = [[NSException alloc] initWithName:@"LocationSDK" reason:@"onReceiveLocation can not be nil!" userInfo:nil];
        @throw(ex);
    }
    self.onReceiveLocation(location);
    [self stopLocation];
}


-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"定位失败:%@",error);
}


/**
 *  规则: 如果速度小于minSpeed m/s 则把触发范围设定为50m
 *  否则将触发范围设定为minSpeed*minInteval
 *  此时若速度变化超过10% 则更新当前的触发范围(这里限制是因为不能不停的设置distanceFilter,
 *  否则uploadLocation会不停被触发)
 */
- (void)adjustDistanceFilter:(CLLocation*)location
{
//    NSLog(@"adjust:%f",location.speed);
    
    if ( location.speed < self.minSpeed )
    {
        if ( fabs(self.distanceFilter-self.minFilter) > 0.1f )
        {
            self.distanceFilter = self.minFilter;
        }
    }
    else
    {
        CGFloat lastSpeed = self.distanceFilter/self.minInteval;
        
        if ( (fabs(lastSpeed-location.speed)/lastSpeed > 0.1f) || (lastSpeed < 0) )
        {
            CGFloat newSpeed  = (int)(location.speed+0.5f);
            CGFloat newFilter = newSpeed*self.minInteval;
            
            self.distanceFilter = newFilter;
        }
    }
}

- (void) getLocation
{
    NSLog(@"执行getLocation");
    
    if ([CLLocationManager locationServicesEnabled] == NO) {
        NSLog(@"locationServicesEnabled false");
    } else {
        CLAuthorizationStatus authorizationStatus= [CLLocationManager authorizationStatus];
        
        if(authorizationStatus == kCLAuthorizationStatusDenied || authorizationStatus == kCLAuthorizationStatusRestricted) {
            NSLog(@"authorizationStatus failed");
        } else {
            NSLog(@"authorizationStatus authorized");
            if(IS_OS_8_OR_LATER) {
              [self requestAlwaysAuthorization];
            }
            
            [self startUpdatingLocation];
        }
    }
}

- (void) stopLocation
{
    [self stopUpdatingLocation];
}


@end
