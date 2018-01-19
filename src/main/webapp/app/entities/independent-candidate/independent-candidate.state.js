(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('independent-candidate', {
            parent: 'entity',
            url: '/independent-candidate?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.independentCandidate.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/independent-candidate/independent-candidates.html',
                    controller: 'IndependentCandidateController',
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
                    $translatePartialLoader.addPart('independentCandidate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('independent-candidate-detail', {
            parent: 'independent-candidate',
            url: '/independent-candidate/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.independentCandidate.detail.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/independent-candidate/independent-candidate-detail.html',
                    controller: 'IndependentCandidateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('independentCandidate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'IndependentCandidate', function($stateParams, IndependentCandidate) {
                    return IndependentCandidate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'independent-candidate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('independent-candidate-detail.edit', {
            parent: 'independent-candidate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/independent-candidate/independent-candidate-dialog.html',
                    controller: 'IndependentCandidateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IndependentCandidate', function(IndependentCandidate) {
                            return IndependentCandidate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('independent-candidate.new', {
            parent: 'independent-candidate',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/independent-candidate/independent-candidate-dialog.html',
                    controller: 'IndependentCandidateDialogController',
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
                    $state.go('independent-candidate', null, { reload: 'independent-candidate' });
                }, function() {
                    $state.go('independent-candidate');
                });
            }]
        })
        .state('independent-candidate.edit', {
            parent: 'independent-candidate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/independent-candidate/independent-candidate-dialog.html',
                    controller: 'IndependentCandidateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IndependentCandidate', function(IndependentCandidate) {
                            return IndependentCandidate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('independent-candidate', null, { reload: 'independent-candidate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('independent-candidate.delete', {
            parent: 'independent-candidate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/independent-candidate/independent-candidate-delete-dialog.html',
                    controller: 'IndependentCandidateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['IndependentCandidate', function(IndependentCandidate) {
                            return IndependentCandidate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('independent-candidate', null, { reload: 'independent-candidate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
