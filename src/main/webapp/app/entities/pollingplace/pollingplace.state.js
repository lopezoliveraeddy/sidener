(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pollingplace', {
            parent: 'entity',
            url: '/pollingplace?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.pollingplace.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pollingplace/pollingplaces.html',
                    controller: 'PollingplaceController',
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
                    $translatePartialLoader.addPart('pollingplace');
                    $translatePartialLoader.addPart('typePollingPlace');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pollingplace-detail', {
            parent: 'pollingplace',
            url: '/pollingplace/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.pollingplace.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pollingplace/pollingplace-detail.html',
                    controller: 'PollingplaceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pollingplace');
                    $translatePartialLoader.addPart('typePollingPlace');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pollingplace', function($stateParams, Pollingplace) {
                    return Pollingplace.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pollingplace',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pollingplace-detail.edit', {
            parent: 'pollingplace-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pollingplace/pollingplace-dialog.html',
                    controller: 'PollingplaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pollingplace', function(Pollingplace) {
                            return Pollingplace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pollingplace.new', {
            parent: 'pollingplace',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pollingplace/pollingplace-dialog.html',
                    controller: 'PollingplaceDialogController',
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
                                urlRecordCount: null,
                                pollingPlaceWon: null,
                                challengedPollingPlace: null,
                                published: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pollingplace', null, { reload: 'pollingplace' });
                }, function() {
                    $state.go('pollingplace');
                });
            }]
        })
        .state('pollingplace.edit', {
            parent: 'pollingplace',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pollingplace/pollingplace-dialog.html',
                    controller: 'PollingplaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pollingplace', function(Pollingplace) {
                            return Pollingplace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pollingplace', null, { reload: 'pollingplace' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pollingplace.delete', {
            parent: 'pollingplace',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pollingplace/pollingplace-delete-dialog.html',
                    controller: 'PollingplaceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pollingplace', function(Pollingplace) {
                            return Pollingplace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pollingplace', null, { reload: 'pollingplace' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
