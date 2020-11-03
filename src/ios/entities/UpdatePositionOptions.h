//
//  UpdatePositionOptions.h
//  sxyq
//
//  Created by yang yang on 2020/10/29.
//

@interface UpdatePositionOptions : NSObject

@property (nonatomic, assign) NSInteger interval;

@property (nonatomic, assign) NSInteger minDistance;

@property (nonatomic, assign) NSInteger postIntervalTimes;

@property (nonatomic, copy) NSString *remoteUrl;

@property (nonatomic, copy) NSString *forgroundTitle;

@property (nonatomic, copy) NSString *forgroundDescriptionn;

@end
