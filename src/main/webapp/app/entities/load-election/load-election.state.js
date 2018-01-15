(function(){
    'use strict';
    angular
        .module('sidenerApp')
        .config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];
    
    function stateConfig($stateProvider) {
    		$stateProvider
    		.state('show-election',{
    			parent:'entity',
    			url:'/show-election',
    			data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Detalle de la Elección'
                },
    			views: {
    				'content@private': {
    					templateUrl:'app/entities/load-election/show-election.html',
    					controller: 'ShowElectionController',
                    controllerAs: 'vm'
    					
    				}
    			},
    			resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
    		})
    		.state('nul-review',{
    			parent:'entity',
    			url:'/nul-review',
    			data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Detalle de la Elección'
                },
    			views: {
    				'content@private': {
    					templateUrl:'app/entities/load-election/nul-review.html',
    					controller: 'NulReviewController',
                    controllerAs: 'vm'
    					
    				}
    			}
    		})
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