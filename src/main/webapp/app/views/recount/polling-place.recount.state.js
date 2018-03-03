(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('district-recount-pollingPlaces', {
            parent: 'entity',
            url: '/district/recount/{id}/polling-places?page',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.district.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/views/recount/polling-places-recount.html',
                    controller: 'DistrictRecountPollingPlaceController',
                    controllerAs: 'vm'
                }
            },
            params: {
                id: null,
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'pollingPlaceWon,desc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        id: $stateParams.id,
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
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
