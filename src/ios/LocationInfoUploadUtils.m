

// #import <Foundation/Foundation.h>
// #import <CoreLocation/CoreLocation.h>
// #import "LocationInfoUploadUtils.h"
// #import "UpdatePositionOptions.h"
// #import "UtilsForLocationsdk.h"
// #import "AMapLocationInfo.h"

// static double lastLat = 0;
// static double lastLng = 0;
// static int count = 0;

// @implementation LocationInfoUploadUtils

// - (void)sendLocationInfo:(CLLocation*)location sessionInfo:(AMapSessionInfo *)sessionInfo
// {
//     UpdatePositionOptions *options = [UtilsForLocationsdk getUpdatePositionOptions];
//     if(!options || [UtilsForLocationsdk isEmpty:options.remoteUrl])
//     {
//         // 如果没有远程API接口地址，那么下面就不需要操作了
//         return;
//     }
//     NSInteger sendLocationTimes = options.postIntervalTimes;
//     // 下面的时间格式之所以这么奇怪是为了跟Android保持一致
//     NSTimeInterval locationTime=[[NSDate date] timeIntervalSince1970]*1000;
    
//     BOOL shouldAccept = [self acceptLocationInfo:location options:options];
    
//     if(shouldAccept){
//         count ++ ;
//         [self saveLocationInfo:location locationTime:locationTime sessionInfo:sessionInfo];
//     }
    
//     if(count < sendLocationTimes){
//         return;
//     }
    
    
    
// }

// - (void)saveLocationInfo:(CLLocation*)location locationTime:(long)locationTime sessionInfo:(AMapSessionInfo*)sessionInfo
// {
//     if(!location || location.coordinate.latitude <= 0 || location.coordinate.longitude <= 0)
//     {
//         return;
//     }
//     AMapLocationInfo *locationInfo = [[AMapLocationInfo alloc] init];
//     locationInfo.longitude = [NSNumber numberWithDouble:location.coordinate.longitude];
//     locationInfo.latitude = [NSNumber numberWithDouble:location.coordinate.latitude];
//     locationInfo.updateTimeStamp = locationTime;
//     locationInfo.remark = @"";
//     locationInfo.accuracy = [NSNumber numberWithDouble:location.horizontalAccuracy];
//     locationInfo.bearing = [NSNumber numberWithDouble:location.course];
//     locationInfo.speed = [NSNumber numberWithDouble:location.speed];
//     locationInfo.sessionInfo = sessionInfo;
    
//     NSMutableArray<AMapLocationInfo *> *locationInfos = [UtilsForLocationsdk getLocationInfos];
//     [locationInfos addObject:locationInfo];
    
//     [UtilsForLocationsdk saveLocationInfos:locationInfos];
// }

// - (BOOL)acceptLocationInfo:(CLLocation*)location options:(UpdatePositionOptions*)options
// {
//     double curLat = location.coordinate.latitude;
//     double curLng = location.coordinate.longitude;
    
//     double vAccuracy = location.verticalAccuracy;
//     double hAccuracy = location.horizontalAccuracy;
    
//     if(vAccuracy > 100 || hAccuracy > 100)
//     {
//         return NO;
//     }
    
//     if(curLat <= 0.0 || curLng <= 0.0)
//     {
//         return NO;
//     }
    
//     NSInteger minDistance = 10;
//     if(options && options.minDistance > 0)
//     {
//         minDistance = options.minDistance;
//     }
//     if(lastLat > 0 && lastLng > 0)
//     {
//         CLLocation *preLocation = [[CLLocation alloc] initWithLatitude:lastLat longitude:lastLng];
//         double distance = [location distanceFromLocation:preLocation];
//         if(distance < minDistance)
//         {
//             return NO;
//         }
//     }
    
//     lastLat = location.coordinate.latitude;
//     lastLng = location.coordinate.longitude;
    
//     return YES;
// }

// - (void)postLocationInfos:(NSString *)remoteUrl sessionInfo:(AMapSessionInfo*)sessionInfo
// {
    // todo 等待上传定位数据到后台
// }

// #pragma mark HttpHelper


// @end
