//
//  AMapVehicleLocationInfo.h
//  sxyq
//
//  Created by yang yang on 2020/10/22.
//
#import "AMapAddress.h"
#import "LocationPoint.h"

@interface AMapVehicleLocationInfo : NSObject

@property (nonatomic,assign) BOOL arrived;
@property (nonatomic,strong) AMapAddress *loadAddress;
@property (nonatomic,strong) AMapAddress *unloadAddress;
@property (nonatomic,strong) NSArray<LocationPoint *> *points;

@end
