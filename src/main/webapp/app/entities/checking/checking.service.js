(function() {
    'use strict';
    angular
        .module('newApp')
        .factory('Checking', Checking);

    Checking.$inject = ['$resource'];

    function Checking ($resource) {
        var resourceUrl =  'api/checkings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
