(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DistrictPollingPlaceController', DistrictPollingPlaceController);

    DistrictPollingPlaceController.$inject = ['$scope', '$state', '$stateParams', 'District', 'Election', 'DistrictPollingPlaces', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function DistrictPollingPlaceController($scope, $state, $stateParams, District, Election, DistrictPollingPlaces, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        // Datos del Distrito
        vm.loadDistrct = loadDistrict;
        vm.district = [];

        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.election = [];

        // Causales
        vm.causals = [];
        vm.causalsRecount = [];
        vm.causalsNullity = [];

        loadDistrict();
        loadAll();

        function loadAll () {
            DistrictPollingPlaces.get({
                id : $stateParams.id,
                page: pagingParams.page - 1
            }, onSuccess, onError);

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

        function loadDistrict() {
            District.get({ id : $stateParams.id }, onSuccess, onError);
            function onSuccess(data) {
                vm.district = data;
                // Cargamos Datos de la Elección
                loadElection(vm.district.electionId);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadElection(idElection) {
            Election.get({ id : idElection }, onSuccess, onError);
            function onSuccess(data) {
                vm.election = data;
                vm.causals = vm.election.causals;
                for(var i = 0; i < vm.causals.length; i++) {
                    if(vm.causals[i].typeCausal === "RECOUNT") {
                        vm.causalsRecount.push(vm.causals[i]);
                    }
                    if(vm.causals[i].typeCausal === "NULLITY") {
                        vm.causalsNullity.push(vm.causals[i]);
                    }
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        $scope.pollingPlaceType = function (typePollingPlace, typeNumber) {
            switch (typePollingPlace) {
                case 'BASIC':
                    return 'B';
                    break;
                case 'CONTIGUOUS':
                    return 'C'+typeNumber;
                    break;
                case 'EXTRAORDINARY':
                    return 'E'+typeNumber;
                    break;
                case 'SPECIAL':
                    return 'S'+typeNumber;
                    break;
            }
        };

    }
})();
