(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('finishReset', {
            parent: 'public',
            url: '/reset/finish?key',
            data: {
                authorities: []
            },
            views: {
                'content@public': {
                    templateUrl: 'app/account/reset/finish/reset.finish.html',
                    controller: 'ResetFinishController',
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
