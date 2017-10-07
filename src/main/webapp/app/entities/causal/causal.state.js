(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('causal', {
            parent: 'entity',
            url: '/causal?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.causal.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/causal/causals.html',
                    controller: 'CausalController',
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
                    $translatePartialLoader.addPart('causal');
                    $translatePartialLoader.addPart('causalType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('causal-detail', {
            parent: 'causal',
            url: '/causal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.causal.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/causal/causal-detail.html',
                    controller: 'CausalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('causal');
                    $translatePartialLoader.addPart('causalType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Causal', function($stateParams, Causal) {
                    return Causal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'causal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('causal-detail.edit', {
            parent: 'causal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal/causal-dialog.html',
                    controller: 'CausalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Causal', function(Causal) {
                            return Causal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('causal.new', {
            parent: 'causal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal/causal-dialog.html',
                    controller: 'CausalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                description: null,
                                color: null,
                                published: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('causal', null, { reload: 'causal' });
                }, function() {
                    $state.go('causal');
                });
            }]
        })
        .state('causal.edit', {
            parent: 'causal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal/causal-dialog.html',
                    controller: 'CausalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Causal', function(Causal) {
                            return Causal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('causal', null, { reload: 'causal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('causal.delete', {
            parent: 'causal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/causal/causal-delete-dialog.html',
                    controller: 'CausalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Causal', function(Causal) {
                            return Causal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('causal', null, { reload: 'causal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
