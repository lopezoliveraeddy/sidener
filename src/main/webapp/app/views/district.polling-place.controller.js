(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DistrictPollingPlaceController', DistrictPollingPlaceController);

    DistrictPollingPlaceController.$inject = ['$state', '$stateParams', 'DistrictPollingPlaces', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function DistrictPollingPlaceController($state, $stateParams, DistrictPollingPlaces, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll () {
            DistrictPollingPlaces.get({ id : $stateParams.id }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.pollingPlaces = data;
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
                page: vm.page,
                id: $stateParams.id
            });
        }

    }
})();
