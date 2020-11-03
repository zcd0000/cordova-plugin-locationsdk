
#import "MapHeaderView.h"

@interface MapHeaderView ()
@property (nonatomic, strong) UILabel *titleLab;
@end

@implementation MapHeaderView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self createViews];
    }
    
    self.backgroundColor = [UIColor colorWithRed:87/255.0 green:142/255.0 blue:220/255.0 alpha:1];
    self.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    return self;
}



- (void)createViews {
    UIButton *backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [self addSubview:backBtn];
    backBtn.frame = CGRectMake(0, 20, 35, 44);
    [backBtn addTarget:self action:@selector(backAction:) forControlEvents:UIControlEventTouchUpInside];
    
    NSURL *bundleURL = [[NSBundle mainBundle] URLForResource:@"CDVLocationsdk" withExtension:@"bundle"];
    NSBundle *bundle = [NSBundle bundleWithURL:bundleURL];
    NSString *imagePath = [bundle pathForResource:@"naviBackIcon@3x" ofType:@"png"];
    UIImage *img = [UIImage imageWithContentsOfFile:imagePath];
    [backBtn setImage:img forState:UIControlStateNormal];
}


- (void)backAction:(id)sender {
    if (self.backCallBack) {
        self.backCallBack();
    }
}

#pragma mark - ==============set & get==============

- (void)setTitle:(NSString *)title {
    _title = title;
    self.titleLab.text = _title;

    [self.titleLab sizeToFit];
//    self.titleLab.center = self.center;
//    CGRect titleFrame = CGRectMake(0, 20, 0, 44);
//    titleFrame.origin.x = CGRectGetMinX(self.titleLab.frame);
//    titleFrame.size.width = CGRectGetWidth(self.titleLab.frame);
//    self.titleLab.frame = titleFrame;
}

- (UILabel *)titleLab {
    if (_titleLab) {
        return _titleLab;
    }
    _titleLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 30, 30)];
    _titleLab.translatesAutoresizingMaskIntoConstraints = NO;
    _titleLab.textColor = [UIColor whiteColor];
    _titleLab.font = [UIFont systemFontOfSize:18.];
    [self addSubview:_titleLab];
    
    NSLayoutConstraint * constraintX,* constraintY;
    constraintX = [NSLayoutConstraint constraintWithItem:_titleLab attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
    constraintY = [NSLayoutConstraint constraintWithItem:_titleLab attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeBottom multiplier:1.0 constant:-10];
    [self addConstraints:@[constraintX,constraintY]];
    return _titleLab;
}

@end
