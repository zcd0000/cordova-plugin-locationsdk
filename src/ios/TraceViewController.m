#import "TraceViewController.h"
#import "MapHeaderView.h"
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import "AMapRouteInfo.h"
#import "UtilsForLocationsdk.h"
#import "NSObject+ImprovedKVC.h"

@interface TraceViewController ()
{
    AMapRouteInfo *routeInfo;
    NSString *traceTitleStr;
    MAPointAnnotation *loadPointAnnotation;
    MAPointAnnotation *unloadPointAnnotation;
}

@property (nonatomic,strong)MAMapView *mapView;
@property (nonatomic,strong)MapHeaderView *headerView;
@property (nonatomic, strong) MAPolyline *tracePoly;


@end

@implementation TraceViewController

- (id)initWithInfo:(AMapRouteInfo *)route traceTitle:(NSString *)title
{
    self = [super init];
    routeInfo = route;
    
    traceTitleStr = title;
    
    return self;
}


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
   
    [self createUI];
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
    loadPointAnnotation = [[MAPointAnnotation alloc] init];
    loadPointAnnotation.coordinate = CLLocationCoordinate2DMake([routeInfo.loadAddress.latitude doubleValue], [routeInfo.loadAddress.longitude doubleValue]);
    loadPointAnnotation.title = routeInfo.loadAddress.name;
    loadPointAnnotation.subtitle = routeInfo.loadAddress.detail;
    
    unloadPointAnnotation = [[MAPointAnnotation alloc] init];
    unloadPointAnnotation.coordinate = CLLocationCoordinate2DMake([routeInfo.unloadAddress.latitude doubleValue], [routeInfo.unloadAddress.longitude doubleValue]);
    unloadPointAnnotation.title = routeInfo.unloadAddress.name;
    unloadPointAnnotation.subtitle = routeInfo.unloadAddress.detail;

    [_mapView addAnnotation:loadPointAnnotation];
    [_mapView addAnnotation:unloadPointAnnotation];
    
    [self createBrokenLine];
    [self.mapView showOverlays:@[_tracePoly] animated:YES];
}


- (void)dealloc {
}

- (MAAnnotationView *)mapView:(MAMapView *)mapView viewForAnnotation:(id<MAAnnotation>)annotation{
    if ([annotation isKindOfClass:[MAPointAnnotation class]])
    {
        static NSString *pointReuseIndentifier = @"loadUnloadPointsIndentifier";
        MAAnnotationView *annotationView = (MAAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndentifier];
        if (annotationView == nil)
        {
            annotationView = [[MAAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndentifier];
        }
        UIImage *img = nil;
        if(annotation == loadPointAnnotation){
            img = [UtilsForLocationsdk getImageViewForMarker:StartIcon];
        }else if(annotation == unloadPointAnnotation){
            img = [UtilsForLocationsdk getImageViewForMarker:EndIcon];
        }else{
            img = [UtilsForLocationsdk getImageViewForMarker:MiddleIcon];
        }
        
        annotationView.image = img;
        annotationView.canShowCallout= YES;       //设置气泡可以弹出，默认为NO
        annotationView.draggable = YES;        //设置标注可以拖动，默认为NO
        return annotationView;
    }
    return nil;
}

- (MAOverlayRenderer *)mapView:(MAMapView *)mapView rendererForOverlay:(id<MAOverlay>)overlay{
    if ([overlay isKindOfClass:[MAPolyline class]])
    {
        MAPolylineRenderer *polylineRenderer = [[MAPolylineRenderer alloc] initWithPolyline:overlay];
        
        NSURL *bundleURL = [[NSBundle mainBundle] URLForResource:@"CDVLocationsdk" withExtension:@"bundle"];
        NSBundle *bundle = [NSBundle bundleWithURL:bundleURL];
        NSString *imagePath = [bundle pathForResource:@"icon_road_green_arrow" ofType:@"png"];
        UIImage *img = [UIImage imageWithContentsOfFile:imagePath];
        polylineRenderer.strokeImage = img;
        polylineRenderer.lineWidth    = 6.f;
        
        
        return polylineRenderer;
    }
    return nil;
}

#pragma - Initialization

- (void)createUI
{
    if(self.mapView){
        self.mapView = nil;
    }
//  header view
    self.headerView = [[MapHeaderView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.bounds), 64)];
//    self.headerView = [[MapHeaderView alloc] init];
    self.headerView.title = [NSString stringWithFormat:@"%@-%@", routeInfo.loadAddress.name, routeInfo.unloadAddress.name];
    __weak UIViewController * weakSelf = self;
    [self.headerView setBackCallBack:^{
        [weakSelf dismissViewControllerAnimated:YES completion:nil];
    }];
    [self.view addSubview:self.headerView];
    
    
//  Map View
    self.mapView = [[MAMapView alloc] initWithFrame:CGRectMake(0, 64, CGRectGetWidth(self.view.bounds), CGRectGetHeight(self.view.bounds))];
    self.mapView.delegate = self;
    self.mapView.showsScale = YES;
    [self.view addSubview:self.mapView];
    
    [self createAttachedViews];

}

- (void)createBrokenLine
{
    [self.mapView removeOverlay:_tracePoly];
    NSArray *aArray = [routeInfo.path componentsSeparatedByString:@";"];
    CLLocationCoordinate2D commonPolyLineCoords[aArray.count];
    for (int i =0; i < aArray.count; i ++) {
        NSArray *coordinate = [aArray[i] componentsSeparatedByString:@","];
        commonPolyLineCoords[i].longitude = [coordinate[0] doubleValue];
        commonPolyLineCoords[i].latitude = [coordinate[1] doubleValue];
    }
    //构造折线对象
    _tracePoly = [MAPolyline polylineWithCoordinates:commonPolyLineCoords count:aArray.count];
    //在地图上添加折线对象
    [self.mapView addOverlay:_tracePoly];
}

- (void)createAttachedViews
{
    //    Distance View
    UITextField *distanceView = [[UITextField alloc]init];
    distanceView.translatesAutoresizingMaskIntoConstraints = NO;
    distanceView.backgroundColor = [UIColor whiteColor];
    distanceView.textColor = [UtilsForLocationsdk colorWithHexString:@"#488aff"];
    distanceView.enabled = NO;
    distanceView.textAlignment = NSTextAlignmentCenter;
    
    distanceView.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    distanceView.font = [UIFont fontWithName:@"Arial" size:24.0];
    distanceView.layer.borderColor = [UtilsForLocationsdk colorWithHexString:@"#488aff"].CGColor;
    distanceView.layer.borderWidth =2.0;
    distanceView.layer.cornerRadius =12.0;
    distanceView.text = [NSString stringWithFormat:@"%lu%@", (unsigned long)routeInfo.distance, @"公里"];
    [self.view addSubview:distanceView];
    NSLayoutConstraint * constraintx = [NSLayoutConstraint constraintWithItem:distanceView attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:self.view attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
    NSLayoutConstraint * constrainty = [NSLayoutConstraint constraintWithItem:distanceView attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:self.view attribute:NSLayoutAttributeBottom multiplier:1 constant:-60];
    NSLayoutConstraint * constraintWidth = [NSLayoutConstraint constraintWithItem:distanceView attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1 constant:110];
    NSLayoutConstraint * constraintHeight = [NSLayoutConstraint constraintWithItem:distanceView attribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1 constant:40];
    
    [self.view addConstraints:@[constraintx,constrainty,constraintWidth,constraintHeight]];
}

#pragma Helper



@end


