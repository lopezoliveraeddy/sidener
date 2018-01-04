(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', 'flowFactoryProvider'];

    function stateConfig($stateProvider, flowFactoryProvider) {
        flowFactoryProvider.defaults = {
            target : 'upload',
            permanentErrors : [ 500, 501 ],
            maxChunkRetries : 3,
            chunkRetryInterval : 5000,
            forceChunkSize : true,
            simultaneousUploads : 4,
            progressCallbacksInterval : 1,
            withCredentials : true,
            method : "octet"
        };

        flowFactoryProvider.on('catchAll', function(event) {
            console.log('catchAll', arguments);
        });

        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@private': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        }),
        $stateProvider.state('login', {
            parent: 'public',
            url: '/login',
            data: {
                authorities: []
            },
            views: {
                'content@public': {
                    templateUrl: 'app/components/login/login.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('login');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
