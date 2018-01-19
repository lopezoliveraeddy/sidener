(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('political-party', {
            parent: 'entity',
            url: '/political-party?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.politicalParty.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/political-party/political-parties.html',
                    controller: 'PoliticalPartyController',
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
                    $translatePartialLoader.addPart('politicalParty');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('political-party-detail', {
            parent: 'political-party',
            url: '/political-party/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.politicalParty.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/political-party/political-party-detail.html',
                    controller: 'PoliticalPartyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('politicalParty');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PoliticalParty', function($stateParams, PoliticalParty) {
                    return PoliticalParty.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'political-party',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('political-party-detail.edit', {
            parent: 'political-party-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/political-party/political-party-dialog.html',
                    controller: 'PoliticalPartyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PoliticalParty', function(PoliticalParty) {
                            return PoliticalParty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('political-party.new', {
            parent: 'political-party',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/political-party/political-party-dialog.html',
                    controller: 'PoliticalPartyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                acronym: null,
                                published: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('political-party', null, { reload: 'political-party' });
                }, function() {
                    $state.go('political-party');
                });
            }]
        })
        .state('political-party.edit', {
            parent: 'political-party',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/political-party/political-party-dialog.html',
                    controller: 'PoliticalPartyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PoliticalParty', function(PoliticalParty) {
                            return PoliticalParty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('political-party', null, { reload: 'political-party' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('political-party.delete', {
            parent: 'political-party',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/political-party/political-party-delete-dialog.html',
                    controller: 'PoliticalPartyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PoliticalParty', function(PoliticalParty) {
                            return PoliticalParty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('political-party', null, { reload: 'political-party' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
