(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('archive', {
            parent: 'entity',
            url: '/archive?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.archive.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/archive/archives.html',
                    controller: 'ArchiveController',
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
                    $translatePartialLoader.addPart('archive');
                    $translatePartialLoader.addPart('archiveStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('archive-detail', {
            parent: 'archive',
            url: '/archive/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sidenerApp.archive.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/archive/archive-detail.html',
                    controller: 'ArchiveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('archive');
                    $translatePartialLoader.addPart('archiveStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Archive', function($stateParams, Archive) {
                    return Archive.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'archive',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('archive-detail.edit', {
            parent: 'archive-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/archive/archive-dialog.html',
                    controller: 'ArchiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Archive', function(Archive) {
                            return Archive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('archive.new', {
            parent: 'archive',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/archive/archive-dialog.html',
                    controller: 'ArchiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                mimeType: null,
                                sizeLength: null,
                                path: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('archive', null, { reload: 'archive' });
                }, function() {
                    $state.go('archive');
                });
            }]
        })
        .state('archive.edit', {
            parent: 'archive',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/archive/archive-dialog.html',
                    controller: 'ArchiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Archive', function(Archive) {
                            return Archive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('archive', null, { reload: 'archive' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('archive.delete', {
            parent: 'archive',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/archive/archive-delete-dialog.html',
                    controller: 'ArchiveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Archive', function(Archive) {
                            return Archive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('archive', null, { reload: 'archive' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
