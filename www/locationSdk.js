/*global cordova, module*/
var exec = require('cordova/exec');

var LocationSdkOriginal = (function () {
    /** Initilize the location sdk */
    LocationSdk.prototype.init = function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, "LocationSdk", "init");
    };
    /** Invoke start operation */
    LocationSdk.prototype.start = function (successCallback, errorCallback, shippingNoteInfos) {
        exec(successCallback, errorCallback, "LocationSdk", "start", [shippingNoteInfos]);
    };
    /** Invoke stop operation */
    LocationSdk.prototype.stop = function (successCallback, errorCallback, shippingNoteInfos) {
        exec(successCallback, errorCallback, "LocationSdk", "stop", [shippingNoteInfos]);
    };
})();

var LocationSdk = new LocationSdkOriginal();

export { LocationSdk };
