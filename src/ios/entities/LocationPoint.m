//
//  LocationPoint.m
//  sxyq
//
//  Created by yang yang on 2020/10/29.
//

#import <Foundation/Foundation.h>
#import "LocationPoint.h"
#import <MJExtension/MJExtension.h>
#import "UtilsForLocationsdk.h"

@implementation LocationPoint

- (id)mj_newValueFromOldValue:(id)oldValue property:(MJProperty *)property{
    if ([property.name isEqualToString:@"updateTime"]) {
        if (oldValue) {
            // 格式化时间
            NSString *dateString = [UtilsForLocationsdk dateCurrentTime:oldValue fromDateFormat:@"yyyy-MM-dd'T'HH:mm:ss.SZZZZZ" toDateFormat:@"MM-dd HH:mm:ss"];
            return dateString;
        }
    }
    return oldValue;
    
}

@end
