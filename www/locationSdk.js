/*global cordova, module*/
var exec = require('cordova/exec');

var LocationSdk = function () { };

LocationSdk.prototype.init = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, "LocationSdk", "init");
};

LocationSdk.prototype.start = function (successCallback, errorCallback, shippingNoteInfos) {
    exec(successCallback, errorCallback, "LocationSdk", "start", [shippingNoteInfos]);
};

LocationSdk.prototype.stop = function (successCallback, errorCallback, shippingNoteInfos) {
    exec(successCallback, errorCallback, "LocationSdk", "stop", [shippingNoteInfos]);
};

module.exports = LocationSdk;
