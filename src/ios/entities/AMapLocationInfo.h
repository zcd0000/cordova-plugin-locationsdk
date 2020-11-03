//
//  AMapLocationInfo.h
//  sxyq
//
//  Created by yang yang on 2020/10/30.
//
#import "AMapSessionInfo.h"

typedef enum  {
    Android = 1, //来自Android
    iOS = 2,//来自iOS
}PlatformType;

@interface AMapLocationInfo : NSObject

@property(nonatomic,assign) NSNumber *longitude;
@property(nonatomic,assign) NSNumber *latitude;
@property(nonatomic,assign) long updateTimeStamp;
@property(nonatomic,copy) NSString *remark;
@property(nonatomic,assign) NSNumber *speed;
@property(nonatomic,assign) NSNumber *bearing;
@property(nonatomic,assign) NSNumber *accuracy;
@property(nonatomic,assign) PlatformType platform;
@property(nonatomic,copy) AMapSessionInfo *sessionInfo;

@end
