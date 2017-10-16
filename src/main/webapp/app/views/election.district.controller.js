(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDistrictController', ElectionDistrictController);

    ElectionDistrictController.$inject = ['$state', '$stateParams', 'ElectionDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ElectionDistrictController($state, $stateParams, ElectionDistrict, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll () {
                ElectionDistrict.get({ id : $stateParams.id
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
