//
//  MMLocationTracker.h
//  sxyq
//
//  Created by yang yang on 2020/10/27.
//

@interface MMLocationTracker : NSObject
{
    NSString *timerKey;
    BOOL isStart;
}

//@property (nonatomic, copy) void (^taskBlock)(void);

+ (instancetype)sharedManager;

- (void)startTimer;

@end
