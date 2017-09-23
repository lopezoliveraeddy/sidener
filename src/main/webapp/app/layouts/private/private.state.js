(function () {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('private', {
            abstract: true,
            views: {
                'content@': {
                    templateUrl: 'app/layouts/private/private.html',
                    controller: 'PrivateController',
                    controllerAs: 'vm'
                }
            },
            parent: 'app'
        });
    }
})();
