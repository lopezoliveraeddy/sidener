(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('documento',{
          parent: 'entity',
          url: '/election/documento/{id}',
          data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'sidenerApp.election.home.title'
          },
          params: {
              id: null
          },
          views: {
            'content@private': {
                templateUrl: 'app/views/recount/documento.html',
                controller: 'DocumentoController',
                controllerAs: 'vm'
            }
          }
        })
        .state('election-recount-district', {
            parent: 'entity',
            url: '/election/recount/{id}/district?page',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.election.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/views/recount/district-recount.html',
                    controller: 'ElectionRecountDistrictController',
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
