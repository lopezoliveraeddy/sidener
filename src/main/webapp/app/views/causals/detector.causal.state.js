(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('detector-causal', {
                parent: 'entity',
                url: '/election/detector/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'sidenerApp.election.home.title'
                },
                views: {
                    'content@private': {
                        templateUrl: 'app/views/causals/detector-causal.html',
                        controller: 'DetectorCausalController',
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
                        $translatePartialLoader.addPart('election');
                        $translatePartialLoader.addPart('district');
                        $translatePartialLoader.addPart('pollingPlace');
                        $translatePartialLoader.addPart('state');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('recountDistrictsRule');
                        $translatePartialLoader.addPart('recountPollingPlaceRule');
                        $translatePartialLoader.addPart('nullity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();
