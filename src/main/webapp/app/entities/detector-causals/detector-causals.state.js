(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('detector-causals', {
            parent: 'entity',
            url: '/detector-causals',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.detectorCausals.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/detector-causals/detector-causals.html',
                    controller: 'DetectorCausalsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('detectorCausals');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('detector-causals-detail', {
            parent: 'detector-causals',
            url: '/detector-causals/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.detectorCausals.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/detector-causals/detector-causals-detail.html',
                    controller: 'DetectorCausalsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('detectorCausals');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DetectorCausals', function($stateParams, DetectorCausals) {
                    return DetectorCausals.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'detector-causals',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('detector-causals-detail.edit', {
            parent: 'detector-causals-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detector-causals/detector-causals-dialog.html',
                    controller: 'DetectorCausalsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetectorCausals', function(DetectorCausals) {
                            return DetectorCausals.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detector-causals.new', {
            parent: 'detector-causals',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detector-causals/detector-causals-dialog.html',
                    controller: 'DetectorCausalsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idPollingPlace: null,
                                idCausal: null,
                                observations: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('detector-causals', null, { reload: 'detector-causals' });
                }, function() {
                    $state.go('detector-causals');
                });
            }]
        })
        .state('detector-causals.edit', {
            parent: 'detector-causals',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detector-causals/detector-causals-dialog.html',
                    controller: 'DetectorCausalsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetectorCausals', function(DetectorCausals) {
                            return DetectorCausals.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('detector-causals', null, { reload: 'detector-causals' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detector-causals.delete', {
            parent: 'detector-causals',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detector-causals/detector-causals-delete-dialog.html',
                    controller: 'DetectorCausalsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DetectorCausals', function(DetectorCausals) {
                            return DetectorCausals.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('detector-causals', null, { reload: 'detector-causals' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
