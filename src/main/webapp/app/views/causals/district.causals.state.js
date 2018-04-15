(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('election-causals-district', {
            parent: 'entity',
            url: '/election/causals/{idElection}/district?page&sort',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.election.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/views/causals/district-causals.html',
                    controller: 'ElectionCausalsDistrictController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'decimalNumber',
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
                    $translatePartialLoader.addPart('election');
                    $translatePartialLoader.addPart('district');
                    $translatePartialLoader.addPart('state');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('recountDistrictsRule');
                    $translatePartialLoader.addPart('recountPollingPlaceRule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
