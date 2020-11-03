
#import <UIKit/UIKit.h>
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import "MapHeaderView.h"
#import "AMapRouteInfo.h"
 
 
@interface TraceViewController : UIViewController<MAMapViewDelegate>
 
- (id)initWithInfo:(AMapRouteInfo *)route traceTitle:(NSString *)title;

@end
