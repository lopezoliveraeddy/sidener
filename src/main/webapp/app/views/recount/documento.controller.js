(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DocumentoController', DocumentoController);

    DocumentoController.$inject = ['$scope', '$state', '$stateParams', 'AlertService', 'District', 'Election', 'DistrictRecountPollingPlaces', 'ParseLinks', 'PollingPlace', 'paginationConstants', ];

    function DocumentoController($scope, $state, $stateParams, AlertService, District, Election, DistrictRecountPollingPlaces, ParseLinks, PollingPlace, paginationConstants ) {

        var vm = this;

        vm.loadPage = loadPage;
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

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.pollingPlaces = data;
                
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
                angular.forEach(vm.election.causals, function(causal, key) {
                    if(causal.typeCausal === 'RECOUNT') {
                        vm.causalsRecount.push(causal);
                    }
                    else if(causal.typeCausal === 'NULLITY') {
                        vm.causalsNullity.push(causal);
                    }
                });
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

    }
})();