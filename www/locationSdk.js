var exec = require('cordova/exec');

var LocationSdk = (function () {
    function LocationSdk() {}
    /** Initilize the location sdk */
    LocationSdk.prototype.init = function (successCallback, errorCallback, sessionInfo, options) {
        exec(successCallback, errorCallback, "LocationSdk", "init", [sessionInfo, options]);
    };
    /** Invoke start operation */
    LocationSdk.prototype.start = function (successCallback, errorCallback, shippingNoteInfos) {
        exec(successCallback, errorCallback, "LocationSdk", "start", [shippingNoteInfos]);
    };
    /** Invoke stop operation */
    LocationSdk.prototype.stop = function (successCallback, errorCallback, shippingNoteInfos) {
        exec(successCallback, errorCallback, "LocationSdk", "stop", [shippingNoteInfos]);
    };

    
    /** 开启持续定位(登陆成功会调用) */
    LocationSdk.prototype.startUpdatePosition = function (successCallback, errorCallback, sessionInfo, options) {
        exec(successCallback, errorCallback, "LocationSdk", "startUpdatePosition", [sessionInfo, options]);
    };
    /** 停止持续定位(退出会调用) */
    LocationSdk.prototype.stopUpdatePosition = function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, "LocationSdk", "stopUpdatePosition", []);
    };
    /** 展示地图 */
    LocationSdk.prototype.showMap = function (successCallback, errorCallback, vehicleLocationInfo, title) {
        exec(successCallback, errorCallback, "LocationSdk", "showMap", [vehicleLocationInfo, title]);
    };
    /** 轨迹地图 */
    LocationSdk.prototype.traceMap = function (successCallback, errorCallback, routeInfo, title) {
        exec(successCallback, errorCallback, "LocationSdk", "traceMap", [routeInfo, title]);
    };
    return LocationSdk;
})();

module.exports = new LocationSdk();