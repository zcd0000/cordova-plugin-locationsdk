//
//  AMapRouteInfo.h
//  sxyq
//
//  Created by yang yang on 2020/10/19.
//
#import "AMapAddress.h"

@interface AMapRouteInfo : NSObject

@property (nonatomic) NSUInteger distance;
@property (nonatomic) NSUInteger duration;
@property (nonatomic,copy) NSString *path;
@property (nonatomic,strong) AMapAddress *loadAddress;
@property (nonatomic,strong) AMapAddress *unloadAddress;

@end
