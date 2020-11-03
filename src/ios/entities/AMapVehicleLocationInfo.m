//
//  AMapVehicleLocationInfo.m
//  sxyq
//
//  Created by yang yang on 2020/10/22.
//

#import <Foundation/Foundation.h>
#import "AMapVehicleLocationInfo.h"

@implementation AMapVehicleLocationInfo

+ (NSDictionary *)mj_objectClassInArray{
    return @{@"points" : @"LocationPoint"};//前边，是属性数组的名字，后边就是类名
}

@end
