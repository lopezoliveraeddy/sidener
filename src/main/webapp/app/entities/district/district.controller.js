(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DistrictController', DistrictController);

    DistrictController.$inject = ['$state', 'District', 'DistrictSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Election'];

    function DistrictController($state, District, DistrictSearch, ParseLinks, AlertService, paginationConstants, pagingParams, Election) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        vm.election = [];

        loadAll();

        function loadAll () {
            if (pagingParams.search) {
                DistrictSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                District.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.districts = data;
                vm.page = pagingParams.page;
                // Obtenemos datos de la Elección
                for(var i = 0; i < data.length; i++) {
                    loadElection(data[i].electionId, data[i].id);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadElection(electionId, id) {
            Election.get({ id : electionId }, onSuccess, onError);

            function onSuccess(data) {
                if(angular.isUndefined(vm.election[id])) {
                    vm.election[id] = [];
                }
                vm.election[id] = data;
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
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
