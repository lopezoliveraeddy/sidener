(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('register-causals-district', {
            parent: 'entity',
            url: '/district/causals/{id}/register',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Detector de Nulidades'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/views/causals/register-causals.html',
                    controller: 'RegisterCausalsDistrictController',
                    controllerAs: 'vm'
                }
            },
            params: {
                id: null
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
