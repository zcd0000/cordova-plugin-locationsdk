#import "MapViewController.h"
#import "MapHeaderView.h"
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import "AMapVehicleLocationInfo.h"
#import "UtilsForLocationsdk.h"
#import "NSObject+ImprovedKVC.h"

@interface MapViewController ()
{
    AMapVehicleLocationInfo *locationInfo;
    NSString *traceTitleStr;
    MAPointAnnotation *loadPointAnnotation;
    MAPointAnnotation *unloadPointAnnotation;
}

@property (nonatomic,strong)MAMapView *mapView;
@property (nonatomic,strong)MapHeaderView *headerView;
@property (nonatomic, strong) MAPolyline *tracePoly;

@end

@implementation MapViewController



- (id)initWithInfo:(AMapVehicleLocationInfo *)vehicleLocationInfo traceTitle:(NSString *)title
{
    self = [super init];
    
    locationInfo = vehicleLocationInfo;
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
    loadPointAnnotation.coordinate = CLLocationCoordinate2DMake([locationInfo.loadAddress.latitude doubleValue], [locationInfo.loadAddress.longitude doubleValue]);
    loadPointAnnotation.title = locationInfo.loadAddress.name;
    loadPointAnnotation.subtitle = locationInfo.loadAddress.detail;
    
    unloadPointAnnotation = [[MAPointAnnotation alloc] init];
    unloadPointAnnotation.coordinate = CLLocationCoordinate2DMake([locationInfo.unloadAddress.latitude doubleValue], [locationInfo.unloadAddress.longitude  doubleValue]);
    unloadPointAnnotation.title = locationInfo.unloadAddress.name;
    unloadPointAnnotation.subtitle = locationInfo.unloadAddress.detail;

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
        annotationView.draggable = YES;        //设置标注可以拖动，默认为NO
        annotationView.canShowCallout= YES;       //设置气泡可以弹出，默认为NO
        if(annotation == loadPointAnnotation){
            img = [UtilsForLocationsdk getImageViewForMarker:StartIcon];
        }else if(annotation == unloadPointAnnotation){
            img = [UtilsForLocationsdk getImageViewForMarker:EndIcon];
        }else{
            img = [UtilsForLocationsdk getImageViewForMarker:MiddleIcon];
            annotationView.draggable = NO;
        }
        annotationView.image = img;
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
    self.headerView.title = [NSString stringWithFormat:@"%@-%@",locationInfo.loadAddress.name, locationInfo.unloadAddress.name];
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
    
//    [self createAttachedViews];

}

- (void)createBrokenLine
{
    [self.mapView removeOverlay:_tracePoly];
    NSArray *aArray = locationInfo.points;
    CLLocationCoordinate2D commonPolyLineCoords[aArray.count];
    for (int i =0; i < aArray.count; i ++) {
        LocationPoint *pointInfo = aArray[i];
        CLLocationCoordinate2D coordinnate2D = CLLocationCoordinate2DMake([pointInfo.latitude doubleValue],[pointInfo.longitude doubleValue]);
        commonPolyLineCoords[i] = coordinnate2D;
        // 为中间点添加“经”标记
        MAPointAnnotation *pointAnnotation = [[MAPointAnnotation alloc] init];
        pointAnnotation.coordinate = coordinnate2D;
        NSString *dateStr = pointInfo.updateTime;
        pointAnnotation.title = dateStr;
        pointAnnotation.subtitle = [NSString stringWithFormat:@"时速:%.2fkm/h",[pointInfo.speed doubleValue]];
        [_mapView addAnnotation:pointAnnotation];
    }
    //构造折线对象
    _tracePoly = [MAPolyline polylineWithCoordinates:commonPolyLineCoords count:aArray.count];
    //在地图上添加折线对象
    [self.mapView addOverlay:_tracePoly];
}

@end
