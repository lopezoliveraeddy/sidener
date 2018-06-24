(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DistrictCausalsPollingPlaceController', DistrictCausalsPollingPlaceController);

    DistrictCausalsPollingPlaceController.$inject = ['$scope', '$state', '$stateParams', 'AlertService', 'District', 'Election', 'DistrictPollingPlacesWonLose', 'ParseLinks', 'PollingPlace', 'paginationConstants', 'pagingParams'];

    function DistrictCausalsPollingPlaceController($scope, $state, $stateParams, AlertService, District, Election, DistrictPollingPlacesWonLose, ParseLinks, PollingPlace, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.addThisPollingPlace = addThisPollingPlace;


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

        function loadAll() {
            DistrictPollingPlacesWonLose.get({
                idDistrict : $stateParams.idDistrict,
                pollingPlaceWon: $stateParams.pollingPlaceWon,
                page: pagingParams.page - 1,
                size: 1000,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate === 'section,asc') {
                    result.push('typePollingPlace');
                    result.push('typeNumber');
                }
                return result;
            }
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
                idDistrict: $stateParams.idDistrict,
                pollingPlaceWon: $stateParams.pollingPlaceWon
            });
        }

        function loadDistrict() {
            District.get({ id : $stateParams.idDistrict }, onSuccess, onError);
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
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function addThisPollingPlace(pollingPlace,confirmed){
            console.log(confirmed);
            pollingPlace.challengedPollingPlace = confirmed;
            PollingPlace.update(pollingPlace, onSaveSuccess, onSaveError);
            function onSaveSuccess(data){

                pollingPlace = data;
                console.log(pollingPlace);
            }
            function onSaveError(error) {
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

        $scope.countingAssumption = function(countingAssumption) {
            if (countingAssumption === true) {
                return "total-recount";
            }
        };

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
