(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('election-type', {
            parent: 'entity',
            url: '/election-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.electionType.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/election-type/election-types.html',
                    controller: 'ElectionTypeController',
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
                    $translatePartialLoader.addPart('electionType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('election-type-detail', {
            parent: 'election-type',
            url: '/election-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.electionType.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/election-type/election-type-detail.html',
                    controller: 'ElectionTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('electionType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ElectionType', function($stateParams, ElectionType) {
                    return ElectionType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'election-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('election-type-detail.edit', {
            parent: 'election-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-type/election-type-dialog.html',
                    controller: 'ElectionTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ElectionType', function(ElectionType) {
                            return ElectionType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('election-type.new', {
            parent: 'election-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-type/election-type-dialog.html',
                    controller: 'ElectionTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                published: null,
                                created: null,
                                updated: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('election-type', null, { reload: 'election-type' });
                }, function() {
                    $state.go('election-type');
                });
            }]
        })
        .state('election-type.edit', {
            parent: 'election-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-type/election-type-dialog.html',
                    controller: 'ElectionTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ElectionType', function(ElectionType) {
                            return ElectionType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('election-type', null, { reload: 'election-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('election-type.delete', {
            parent: 'election-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-type/election-type-delete-dialog.html',
                    controller: 'ElectionTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ElectionType', function(ElectionType) {
                            return ElectionType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('election-type', null, { reload: 'election-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
