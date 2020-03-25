var exec = require('cordova/exec');
 
exports.all = function (options, success, error) {
    exec(success, error, 'Contacts', 'all', [options]);
}


exports.search = function (searchString, options, success, error) {
    exec(success, error, 'Contacts', 'search', [searchString, options]);
}