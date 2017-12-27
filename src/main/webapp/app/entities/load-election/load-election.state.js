(function(){
    'use strict';
    angular
        .module('sidenerApp')
        .config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];
    
    function stateConfig($stateProvider) {
    		$stateProvider
    		.state('load-election', {
    			parent: 'entity',
            url: '/load-election',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'sidenerApp.election.home.title'
            },
            views: {
                'content@private': {
                    templateUrl: 'app/entities/load-election/load-election.html',
                    controller: 'LoadElectionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
    			
    		});
    }
    
    
})();