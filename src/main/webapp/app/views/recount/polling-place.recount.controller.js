(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DistrictRecountPollingPlaceController', DistrictRecountPollingPlaceController);

    DistrictRecountPollingPlaceController.$inject = ['$scope', '$state', '$stateParams', 'AlertService', 'CausalType', 'District', 'Election', 'ElectionPollingPlacesWonLose', 'DistrictRecountPollingPlaces', 'ParseLinks', 'PollingPlace', 'paginationConstants', 'pagingParams'];

    function DistrictRecountPollingPlaceController($scope, $state, $stateParams, AlertService, CausalType, District, Election, ElectionPollingPlacesWonLose, DistrictRecountPollingPlaces, ParseLinks, PollingPlace, paginationConstants, pagingParams) {

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

        // Casillas Ganadas - Perdidas
        vm.pollingPlacesWonLose = ElectionPollingPlacesWonLose.get({ idDistrict : $stateParams.id });

        // Causales
        vm.causals = [];
        vm.causalsRecount = CausalType.get({
            typeCausal: 'RECOUNT'
        });
        loadDistrict();
        loadAll();

        function loadAll () {
            DistrictRecountPollingPlaces.get({
                idDistrict : $stateParams.id,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate == 'pollingPlaceWon') {
                    result.push('section');
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
                id: $stateParams.id,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')
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

        // Actualización dada la selección de causales
        $scope.updateCausals = function (pollingPlace) {
            console.log(pollingPlace);
            vm.isSaving = true;
            PollingPlace.update(pollingPlace, onSaveSuccess, onSaveError);
        };

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:pollingPlaceUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        $scope.countingAssumption = function(countingAssumption) {
            if (countingAssumption === true) {
                return "total-recount";
            }
        };

        $scope.asociatedPosition = function(entity) {
            if (vm.election.coalitionAsociatedId !== null) {
                if (entity == vm.election.coalitionAsociatedAcronym) {
                    return '<i class="fa medium fa-certificate" aria-hidden="true"></i>';
                }
            }
            else if(vm.election.politicalPartyAsociatedId !== null) {
                if (entity == vm.election.politicalPartyAsociatedAcronym) {
                    return '<i class="fa medium fa-certificate" aria-hidden="true"></i>';
                }
            }
            else if (vm.election.independentCandidateAsociatedId !== null) {
                if (entity == vm.election.independentCandidateAsociatedAcronym) {
                    return '<i class="fa medium fa-certificate" aria-hidden="true"></i>';
                }
            }
        }

    }
})();
