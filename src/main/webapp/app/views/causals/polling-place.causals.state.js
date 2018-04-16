(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('district-causals-pollingPlaces', {
            parent: 'entity',
            url: '/district/causals/{idDistrict}/polling-places/{pollingPlaceWon}?page&sort',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.district.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/views/causals/polling-places-causals.html',
                    controller: 'DistrictCausalsPollingPlaceController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'section,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
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
                    $translatePartialLoader.addPart('state');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
