/*global cordova, module*/
var exec = require('cordova/exec');

var LocationSdk = function () { };

LocationSdk.prototype.init = function (onSuccess, onError) {
    exec(successCallback, errorCallback, "LocationSdk", "init");
};

LocationSdk.prototype.start = function (onSuccess, onError, shippingNoteInfos) {
    exec(successCallback, errorCallback, "LocationSdk", "start", [shippingNoteInfos]);
};

LocationSdk.prototype.stop = function (onSuccess, onError, shippingNoteInfos) {
    exec(successCallback, errorCallback, "LocationSdk", "stop", [shippingNoteInfos]);
};

module.exports = LocationSdk;
