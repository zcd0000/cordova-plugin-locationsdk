////
////  AppDelegate+Location.m
////  sxyq
////
////  Created by yang yang on 2020/10/26.
////
//
//#import <Foundation/Foundation.h>
//#import "AppDelegate+Location.h"
//
//@implementation AppDelegate (Location)
//
//- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
//{
//    UIAlertView * alert;
//
//    //We have to make sure that the Background App Refresh is enable for the Location updates to work in the background.
//    if([[UIApplication sharedApplication] backgroundRefreshStatus] == UIBackgroundRefreshStatusDenied){
//
//        alert = [[UIAlertView alloc]initWithTitle:@""
//                                          message:@"The app doesn't work without the Background App Refresh enabled. To turn it on, go to Settings > General > Background App Refresh"
//                                         delegate:nil
//                                cancelButtonTitle:@"Ok"
//                                otherButtonTitles:nil, nil];
//        [alert show];
//
//    }else if([[UIApplication sharedApplication] backgroundRefreshStatus] == UIBackgroundRefreshStatusRestricted){
//
//        alert = [[UIAlertView alloc]initWithTitle:@""
//                                          message:@"The functions of this app are limited because the Background App Refresh is disable."
//                                         delegate:nil
//                                cancelButtonTitle:@"Ok"
//                                otherButtonTitles:nil, nil];
//        [alert show];
//
//    }
//    return [super application:application didFinishLaunchingWithOptions:launchOptions];
//}
//
//@end
