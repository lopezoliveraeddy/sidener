(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionCausalsDistrictController', ElectionCausalsDistrictController);

    ElectionCausalsDistrictController.$inject = ['$state', '$stateParams', 'Election', 'ElectionCausalsDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ElectionCausalsDistrictController($state, $stateParams, Election, ElectionCausalsDistrict, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.election = [];

        loadElection();
        loadAll();

        function loadElection () {
            Election.get({ id : $stateParams.id }, onSuccess, onError);
            function onSuccess(data) {
                vm.election = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll () {
            ElectionCausalsDistrict.get({ id : $stateParams.id
                }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.districts = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                id: $stateParams.id,
                page: vm.page
            });
        }

    }
})();
