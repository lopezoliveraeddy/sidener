(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('requestReset', {
            parent: 'public',
            url: '/reset/request',
            data: {
                authorities: []
            },
            views: {
                'content@public': {
                    templateUrl: 'app/account/reset/request/reset.request.html',
                    controller: 'RequestResetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reset');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
