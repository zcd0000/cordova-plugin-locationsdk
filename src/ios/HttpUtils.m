// //
// //  HttpUtils.m
// //  sxyq
// //
// //  Created by yang yang on 2020/10/30.
// //

// #import <Foundation/Foundation.h>
// #import "HttpUtils.h"
// #import "AFHTTPSessionManager.h"
// #import "TextRequestSerializer.h"
// #import "TextResponseSerializer.h"

// @implementation HttpUtils


// - (void)executeRequestWithData:(NSString*)url withMethod:(NSString*)method withData:(NSDictionary*)data withHeaders:(NSDictionary*)headers withTimeoutInSeconds:(NSTimeInterval)timeoutInSeconds success:(void (^)(NSHTTPURLResponse* _Nonnull, id _Nullable))success
// failure:(void (^)(NSHTTPURLResponse* _Nullable, NSError * _Nonnull))failure{
//     AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
//     manager.requestSerializer = [AFJSONRequestSerializer serializer];
    
//     [self setupAuthChallengeBlock: manager];
//     [self setRequestHeaders: headers forManager: manager];
//     [self setTimeout:timeoutInSeconds forManager:manager];
//     manager.responseSerializer = [TextResponseSerializer serializer];

//     @try {

//         void (^onSuccess)(NSURLSessionTask *, id) = ^(NSURLSessionTask *task, id responseObject) {

//             success((NSHTTPURLResponse*)task.response,responseObject);
//         };

//         void (^onFailure)(NSURLSessionTask *, NSError *) = ^(NSURLSessionTask *task, NSError *error) {
            
//             failure((NSHTTPURLResponse*)task.response,error);
//         };

//         NSURLSessionDataTask *task;
//         task = [manager uploadTaskWithHTTPMethod:method URLString:url parameters:data progress:nil success:onSuccess failure:onFailure];
//     }
//     @catch (NSException *exception) {
        
//     }
// }

// - (void)setRequestHeaders:(NSDictionary*)headers forManager:(AFHTTPSessionManager*)manager {
//     [headers enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
//         [manager.requestSerializer setValue:obj forHTTPHeaderField:key];
//     }];
// }

// - (void)setTimeout:(NSTimeInterval)timeout forManager:(AFHTTPSessionManager*)manager {
//     [manager.requestSerializer setTimeoutInterval:timeout];
// }

// - (void)setupAuthChallengeBlock:(AFHTTPSessionManager*)manager {
//     [manager setSessionDidReceiveAuthenticationChallengeBlock:^NSURLSessionAuthChallengeDisposition(
//         NSURLSession * _Nonnull session,
//         NSURLAuthenticationChallenge * _Nonnull challenge,
//         NSURLCredential * _Nullable __autoreleasing * _Nullable credential
//     ) {
//         if ([challenge.protectionSpace.authenticationMethod isEqualToString: NSURLAuthenticationMethodServerTrust]) {
//             *credential = [NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust];

//             if (![self->securityPolicy evaluateServerTrust:challenge.protectionSpace.serverTrust forDomain:challenge.protectionSpace.host]) {
//                 return NSURLSessionAuthChallengeRejectProtectionSpace;
//             }

//             if (credential) {
//                 return NSURLSessionAuthChallengeUseCredential;
//             }
//         }

//         if ([challenge.protectionSpace.authenticationMethod isEqualToString: NSURLAuthenticationMethodClientCertificate] && self->x509Credential) {
//             *credential = self->x509Credential;
//             return NSURLSessionAuthChallengeUseCredential;
//         }

//         return NSURLSessionAuthChallengePerformDefaultHandling;
//     }];
// }

// @end
