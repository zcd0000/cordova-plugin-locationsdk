#import <UIKit/UIKit.h>

@interface MapHeaderView : UIView

@property (nonatomic, strong) NSString *title;

@property (nonatomic, copy) void(^backCallBack)(void);
@end
