(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('election', {
            parent: 'entity',
            url: '/election?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.election.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/election/elections.html',
                    controller: 'ElectionController',
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
                    $translatePartialLoader.addPart('election');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('recountDistrictsRule');
                    $translatePartialLoader.addPart('recountPollingPlaceRule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('election-detail', {
            parent: 'election',
            url: '/election/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.election.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/election/election-detail.html',
                    controller: 'ElectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('election');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('recountDistrictsRule');
                    $translatePartialLoader.addPart('recountPollingPlaceRule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Election', function($stateParams, Election) {
                    return Election.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'election',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('election-detail.edit', {
            parent: 'election-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election/election-dialog.html',
                    controller: 'ElectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Election', function(Election) {
                            return Election.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('election.new', {
            parent: 'election',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election/election-dialog.html',
                    controller: 'ElectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                location: null,
                                date: null,
                                status: null,
                                prepUrl: null,
                                ballotUrl: null,
                                insetUrl: null,
                                demandTemplateUrl: null,
                                recountTemplateUrl: null,
                                recountDistrictsRule: null,
                                recountPollingPlaceRule: null,
                                published: null,
                                created: null,
                                updated: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('election', null, { reload: 'election' });
                }, function() {
                    $state.go('election');
                });
            }]
        })
        .state('election.edit', {
            parent: 'election',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election/election-dialog.html',
                    controller: 'ElectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Election', function(Election) {
                            return Election.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('election', null, { reload: 'election' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('election.delete', {
            parent: 'election',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/election/election-delete-dialog.html',
                    controller: 'ElectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Election', function(Election) {
                            return Election.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('election', null, { reload: 'election' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
