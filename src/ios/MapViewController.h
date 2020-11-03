
#import <UIKit/UIKit.h>
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
 
 
@interface MapViewController : UIViewController<MAMapViewDelegate>
 
- (id)initWithInfo:(NSDictionary *)vehicleLocationInfoDic traceTitle:(NSString *)title;
 
@end
