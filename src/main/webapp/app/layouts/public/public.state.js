(function () {
    'use strict';

    angular
        .module('sidenerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('public', {
            abstract: true,
            views: {
                'content@': {
                    templateUrl: 'app/layouts/public/public.html',
                    // controller: 'NavbarController',
                    // controllerAs: 'vm'
                }
            },
            parent: 'app'
        });
    }
})();
