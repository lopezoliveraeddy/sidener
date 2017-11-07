(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('district-pollingPlaces', {
            parent: 'entity',
            url: '/district/{id}/polling-places?page',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.district.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/views/district-polling-places.html',
                    controller: 'DistrictPollingPlaceController',
                    controllerAs: 'vm'
                }
            },
            params: {
                id: null,
                page: {
                    value: '1',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        id: $stateParams.id,
                        page: PaginationUtil.parsePage($stateParams.page),
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pollingPlace');
                    $translatePartialLoader.addPart('district');
                    $translatePartialLoader.addPart('pollingPlaceType');
                    $translatePartialLoader.addPart('election');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
