//
//  UtilsForLocationsdk.h
//  sxyq
//
//  Created by yang yang on 2020/10/20.
//

enum MarkerIcon: NSUInteger {
    StartIcon = 1,
    MiddleIcon,
    EndIcon,
};
typedef enum MarkerIcon MarkerIcon;

@interface UtilsForLocationsdk : NSObject

+ (UIImage*) getImageViewForMarker:(MarkerIcon)icon;

+ (UIColor *) colorWithHexString: (NSString *) hexString;

+ (NSString *) dateCurrentTime: (NSString *)dateStr fromDateFormat:(NSString *)fromDateFormat toDateFormat:(NSString *)toDateFormat;

@end
