(function(){
    'use strict';
    angular
        .module('sidenerApp')
        .config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    		$stateProvider
    		.state('district-load',{
    			parent:'entity',
    			url:'/district-load',
    			data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Carga de Distritos'
                },
    			views: {
    				'content@private': {
    					templateUrl:'app/views/district-load/district-load.html',
    					controller: 'DistrictLoadController',
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

    }


})();
