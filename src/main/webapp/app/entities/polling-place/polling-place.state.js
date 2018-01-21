(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('polling-place', {
            parent: 'entity',
            url: '/polling-place?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.pollingPlace.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/polling-place/polling-places.html',
                    controller: 'PollingPlaceController',
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
                    $translatePartialLoader.addPart('pollingPlace');
                    $translatePartialLoader.addPart('typePollingPlace');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('polling-place-detail', {
            parent: 'polling-place',
            url: '/polling-place/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.pollingPlace.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/polling-place/polling-place-detail.html',
                    controller: 'PollingPlaceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pollingPlace');
                    $translatePartialLoader.addPart('typePollingPlace');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PollingPlace', function($stateParams, PollingPlace) {
                    return PollingPlace.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'polling-place',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('polling-place-detail.edit', {
            parent: 'polling-place-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polling-place/polling-place-dialog.html',
                    controller: 'PollingPlaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PollingPlace', function(PollingPlace) {
                            return PollingPlace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('polling-place.new', {
            parent: 'polling-place',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polling-place/polling-place-dialog.html',
                    controller: 'PollingPlaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                town: null,
                                typePollingPlace: null,
                                typeNumber: null,
                                section: null,
                                address: null,
                                leftoverBallots: null,
                                votingCitizens: null,
                                exctractedBallots: null,
                                notRegistered: null,
                                nullVotes: null,
                                totalVotes: null,
                                electoralRoll: null,
                                observations: null,
                                president: null,
                                secretary: null,
                                scrutineerOne: null,
                                scrutineerTwo: null,
                                alternateOne: null,
                                alternateTwo: null,
                                alternateThree: null,
                                entityFirstPlace: null,
                                totalFirstPlace: null,
                                entitySecondPlace: null,
                                totalSecondPlace: null,
                                published: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('polling-place', null, { reload: 'polling-place' });
                }, function() {
                    $state.go('polling-place');
                });
            }]
        })
        .state('polling-place.edit', {
            parent: 'polling-place',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polling-place/polling-place-dialog.html',
                    controller: 'PollingPlaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PollingPlace', function(PollingPlace) {
                            return PollingPlace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('polling-place', null, { reload: 'polling-place' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('polling-place.delete', {
            parent: 'polling-place',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polling-place/polling-place-delete-dialog.html',
                    controller: 'PollingPlaceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PollingPlace', function(PollingPlace) {
                            return PollingPlace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('polling-place', null, { reload: 'polling-place' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
