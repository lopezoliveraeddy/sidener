(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('election-period', {
            parent: 'entity',
            url: '/election-period?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.electionPeriod.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/election-period/election-periods.html',
                    controller: 'ElectionPeriodController',
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
                    $translatePartialLoader.addPart('electionPeriod');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('election-period-detail', {
            parent: 'election-period',
            url: '/election-period/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.electionPeriod.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/election-period/election-period-detail.html',
                    controller: 'ElectionPeriodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('electionPeriod');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ElectionPeriod', function($stateParams, ElectionPeriod) {
                    return ElectionPeriod.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'election-period',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('election-period-detail.edit', {
            parent: 'election-period-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-period/election-period-dialog.html',
                    controller: 'ElectionPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ElectionPeriod', function(ElectionPeriod) {
                            return ElectionPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('election-period.new', {
            parent: 'election-period',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-period/election-period-dialog.html',
                    controller: 'ElectionPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                start: null,
                                end: null,
                                published: null,
                                created: null,
                                updated: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('election-period', null, { reload: 'election-period' });
                }, function() {
                    $state.go('election-period');
                });
            }]
        })
        .state('election-period.edit', {
            parent: 'election-period',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-period/election-period-dialog.html',
                    controller: 'ElectionPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ElectionPeriod', function(ElectionPeriod) {
                            return ElectionPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('election-period', null, { reload: 'election-period' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('election-period.delete', {
            parent: 'election-period',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election-period/election-period-delete-dialog.html',
                    controller: 'ElectionPeriodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ElectionPeriod', function(ElectionPeriod) {
                            return ElectionPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('election-period', null, { reload: 'election-period' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
