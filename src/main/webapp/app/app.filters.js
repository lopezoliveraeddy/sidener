(function() {
    angular.module('sidenerApp')
    .filter('propsFilter', function() {
        return function(items, props) {
            var out = [];

            if (angular.isArray(items)) {
                var keys = Object.keys(props);

                items.forEach(function(item) {
                    var itemMatches = false;

                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }

                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }
            return out;
        };
    }).filter('percentage', ['$filter', function ($filter) {
        return function (input, decimals) {
            return $filter('number')(input * 100, decimals) + '%';
        };
    }]).filter('removeSpaces', [function() {
        return function(string) {
            if (!angular.isString(string)) {
                    return string;
            }
            return string.replace(/[\s]/g, '').toLowerCase();
        };
    }]);
})();
