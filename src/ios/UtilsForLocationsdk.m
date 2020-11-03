//
//  UtilsForLocationsdk.m
//  sxyq
//
//  Created by yang yang on 2020/10/20.
//

#import <Foundation/Foundation.h>
#import <objc/runtime.h>
#import "UtilsForLocationsdk.h"
#import <MJExtension/MJExtension.h>

static NSString *sessionKey = @"session_info";
static NSString *optionsKey = @"options_info";
static NSString *locationKey = @"location_info";

@implementation UtilsForLocationsdk


+ (UIImage*) getImageViewForMarker:(MarkerIcon)icon
{
    
    NSURL *bundleURL = [[NSBundle mainBundle] URLForResource:@"CDVLocationsdk" withExtension:@"bundle"];
    NSBundle *bundle = [NSBundle bundleWithURL:bundleURL];
    NSString *imagePath = [bundle pathForResource:@"dir_marker" ofType:@"png"];
    UIImage *img = [UIImage imageWithContentsOfFile:imagePath];
    
    CGSize sourceImageSize = img.size;
    CGRect destRect;
    switch (icon) {
        case StartIcon:
            destRect = CGRectMake(0, 0, sourceImageSize.width / 3, sourceImageSize.height);
            break;
        case MiddleIcon:
            destRect = CGRectMake(sourceImageSize.width / 3, 0, sourceImageSize.width / 3, sourceImageSize.height);
            break;
        case EndIcon:
            destRect = CGRectMake(sourceImageSize.width * 2 / 3, 0, sourceImageSize.width / 3, sourceImageSize.height);
            break;
    }
    
    CGImageRef sourceImageRef = img.CGImage;
    CGImageRef destImageRef = CGImageCreateWithImageInRect(sourceImageRef, destRect);
    UIImage *newImage = [UIImage imageWithCGImage:destImageRef scale:[UIScreen mainScreen].scale orientation:UIImageOrientationUp];
    
    return newImage;
}

+ (CGFloat) colorComponentFrom: (NSString *) string start: (NSUInteger) start length: (NSUInteger) length {
    NSString *substring = [string substringWithRange: NSMakeRange(start, length)];
    NSString *fullHex = length == 2 ? substring : [NSString stringWithFormat: @"%@%@", substring, substring];
    unsigned hexComponent;
    [[NSScanner scannerWithString: fullHex] scanHexInt: &hexComponent];
    return hexComponent / 255.0;
}

+ (UIColor *) colorWithHexString: (NSString *) hexString {
    NSString *colorString = [[hexString stringByReplacingOccurrencesOfString: @"#" withString: @""] uppercaseString];
    CGFloat alpha, red, blue, green;
    switch ([colorString length]) {
        case 3: // #RGB
            alpha = 1.0f;
            red   = [self colorComponentFrom: colorString start: 0 length: 1];
            green = [self colorComponentFrom: colorString start: 1 length: 1];
            blue  = [self colorComponentFrom: colorString start: 2 length: 1];
            break;
        case 4: // #ARGB
            alpha = [self colorComponentFrom: colorString start: 0 length: 1];
            red   = [self colorComponentFrom: colorString start: 1 length: 1];
            green = [self colorComponentFrom: colorString start: 2 length: 1];
            blue  = [self colorComponentFrom: colorString start: 3 length: 1];
            break;
        case 6: // #RRGGBB
            alpha = 1.0f;
            red   = [self colorComponentFrom: colorString start: 0 length: 2];
            green = [self colorComponentFrom: colorString start: 2 length: 2];
            blue  = [self colorComponentFrom: colorString start: 4 length: 2];
            break;
        case 8: // #AARRGGBB
            alpha = [self colorComponentFrom: colorString start: 0 length: 2];
            red   = [self colorComponentFrom: colorString start: 2 length: 2];
            green = [self colorComponentFrom: colorString start: 4 length: 2];
            blue  = [self colorComponentFrom: colorString start: 6 length: 2];
            break;
        default:
            return nil;
    }
    return [UIColor colorWithRed: red green: green blue: blue alpha: alpha];
}

+ (NSString *) dateCurrentTime: (NSString *)dateStr fromDateFormat:(NSString *)fromDateFormat toDateFormat:(NSString *)toDateFormat
{
    NSDateFormatter * dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.locale = [NSLocale localeWithLocaleIdentifier:@"en"];
    dateFormatter.dateFormat = fromDateFormat;
    NSDate *currentDate = [dateFormatter dateFromString:dateStr];
    
    dateFormatter.dateFormat = toDateFormat;
    NSString *currentDateStr = [dateFormatter stringFromDate:currentDate];
    return currentDateStr;
}

+ (BOOL) isEmpty:(id)str {
    return str == nil
    || [str isEqualToString:@""]
    || [str isKindOfClass:[NSNull class]]
    || ([str respondsToSelector:@selector(length)]
        && [(NSData *)str length] == 0)
    || ([str respondsToSelector:@selector(count)]
        && [(NSArray *)str count] == 0);
}

// + (AMapSessionInfo *) getSessionInfo
// {
//     NSDictionary * values = [[NSUserDefaults standardUserDefaults] dictionaryForKey:sessionKey];
//     if(values){
//         AMapSessionInfo *result = [AMapSessionInfo mj_objectWithKeyValues:values];
//         return result;
//     }
    
//     return nil;
// }
// + (void) saveSessionInfo:(NSDictionary *)sessionInfo
// {
//     if(!sessionInfo || [sessionInfo count] <= 0){
//         return;
//     }
//     [[NSUserDefaults standardUserDefaults] setObject:sessionInfo forKey:sessionKey];
// }
// + (void) clearSessionInfo
// {
//     [[NSUserDefaults standardUserDefaults] removeObjectForKey:sessionKey];
// }

// + (UpdatePositionOptions *) getUpdatePositionOptions
// {
//     NSDictionary * values = [[NSUserDefaults standardUserDefaults] dictionaryForKey:optionsKey];
//     if(values){
//         UpdatePositionOptions *result = [UpdatePositionOptions mj_objectWithKeyValues:values];
//         return result;
//     }
    
//     return nil;
// }
// + (void) saveUpdatePositionOptions:(NSDictionary *)options
// {
//     if(!options || [options count] <= 0){
//         return;
//     }
//     [[NSUserDefaults standardUserDefaults] setObject:options forKey:optionsKey];
// }
// + (NSMutableArray<AMapLocationInfo *> *) getLocationInfos
// {
//     NSString * values = [[NSUserDefaults standardUserDefaults] stringForKey:locationKey];
//     if([self isEmpty:values] ){
//         return nil;
//     }
// //    [AMapLocationInfo mj_objectArrayWithKeyValuesArray:<#(id)#>]
    
//     return nil;
// }
// + (void) saveLocationInfos:(NSMutableArray<AMapLocationInfo *> *)list
// {
    
// }
// + (void) clearLocationInfos
// {
//     [[NSUserDefaults standardUserDefaults] removeObjectForKey:locationKey];
// }

@end
