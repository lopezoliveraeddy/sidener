(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('causal-description', {
            parent: 'entity',
            url: '/causal-description?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.causalDescription.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/causal-description/causal-descriptions.html',
                    controller: 'CausalDescriptionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('causalDescription');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('causal-description-detail', {
            parent: 'causal-description',
            url: '/causal-description/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.causalDescription.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/causal-description/causal-description-detail.html',
                    controller: 'CausalDescriptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('causalDescription');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CausalDescription', function($stateParams, CausalDescription) {
                    return CausalDescription.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'causal-description',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('causal-description-detail.edit', {
            parent: 'causal-description-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal-description/causal-description-dialog.html',
                    controller: 'CausalDescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CausalDescription', function(CausalDescription) {
                            return CausalDescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('causal-description.new', {
            parent: 'causal-description',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal-description/causal-description-dialog.html',
                    controller: 'CausalDescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                text: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('causal-description', null, { reload: 'causal-description' });
                }, function() {
                    $state.go('causal-description');
                });
            }]
        })
        .state('causal-description.edit', {
            parent: 'causal-description',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal-description/causal-description-dialog.html',
                    controller: 'CausalDescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CausalDescription', function(CausalDescription) {
                            return CausalDescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('causal-description', null, { reload: 'causal-description' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('causal-description.delete', {
            parent: 'causal-description',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal-description/causal-description-delete-dialog.html',
                    controller: 'CausalDescriptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CausalDescription', function(CausalDescription) {
                            return CausalDescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('causal-description', null, { reload: 'causal-description' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
